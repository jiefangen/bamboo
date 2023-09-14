package org.panda.service.doc.core.domain.factory.ppt.helper;

import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.ddf.EscherTextboxRecord;
import org.apache.poi.hslf.model.textproperties.TextProp;
import org.apache.poi.hslf.model.textproperties.TextPropCollection;
import org.apache.poi.hslf.record.EscherTextboxWrapper;
import org.apache.poi.hslf.record.PPDrawing;
import org.apache.poi.hslf.record.StyleTextPropAtom;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextShape;
import org.apache.poi.sl.draw.Drawable;
import org.apache.poi.sl.extractor.SlideShowExtractor;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.*;
import org.apache.poi.xslf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.panda.bamboo.common.constant.file.FileExtensions;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.clazz.BeanUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.service.doc.common.DocExceptionCodes;
import org.panda.service.doc.common.util.DocUtil;
import org.panda.service.doc.core.domain.document.DocOutline;
import org.springframework.util.Assert;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * PPT文档
 */
public class PptDocHelper {

	private static final String TEXT_PROP_NAME__FONT_INDEX = "font.index";

	private SlideShow<?, ?> origin;
	private String defaultFontFamily = "SimSun"; // 默认字体：宋体，改动时需为中文字体

	public PptDocHelper(InputStream in, String extension) {
		extension = DocUtil.standardizeExtension(extension);
		try {
			if (FileExtensions.PPT.equals(extension)) {
				this.origin = new HSLFSlideShow(in);
			} else {
				this.origin = new XMLSlideShow(in);
			}
		} catch (IOException e) {
			throw new BusinessException(DocUtil.getError(DocExceptionCodes.CAN_NOT_LOAD), FileExtensions.PPTX.toUpperCase());
		}
	}

	public SlideShow<?, ?> getOrigin() {
		return this.origin;
	}

	public void setDefaultFontFamily(String defaultFontFamily) {
		Assert.isTrue(StringUtil.isChineseFontFamily(defaultFontFamily),
				"The 'defaultFontFamily' must be a chinese font family");
		this.defaultFontFamily = defaultFontFamily;
	}

	public DocOutline getOutline() {
		DocOutline outline = new DocOutline();
		outline.setPageCount(this.origin.getSlides().size());
		return outline;
	}

	public BufferedImage renderImage(int pageIndex) {
		return renderImage(pageIndex, 1);
	}

	@SuppressWarnings("unchecked")
	public BufferedImage renderImage(int pageIndex, float scale) {
		Slide<?, ?> slide = getPage(pageIndex);
		if (slide != null) {
			List<Shape<?, ?>> shapes = (List<Shape<?, ?>>) slide
					.getShapes();
			List<Runnable> preparedTasks = new ArrayList<>();
			for (Shape<?, ?> shape : shapes) {
				preparedTasks.addAll(prepare(shape));
			}
			preparedTasks.forEach(Runnable::run);

			Dimension pageSize = this.origin.getPageSize();
			BufferedImage image = new BufferedImage((int) (pageSize.width * scale), (int) (pageSize.height * scale),
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = image.createGraphics();
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			graphics.setRenderingHint(Drawable.BUFFERED_IMAGE, new WeakReference<>(image));
			graphics.setPaint(Color.WHITE);
			graphics.fill(new Rectangle(0, 0, image.getWidth(), image.getHeight()));
			graphics.scale(scale, scale);
			slide.draw(graphics);
			graphics.dispose();
			image.flush();
			return image;
		}
		return null;
	}

	private Slide<?, ?> getPage(int pageIndex) {
		List<? extends Slide<?, ?>> slides = this.origin.getSlides();
		if (0 <= pageIndex && pageIndex < slides.size()) {
			return slides.get(pageIndex);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private List<Runnable> prepare(Shape<?, ?> shape) {
		List<Runnable> preparedTasks = new ArrayList<>();
		if (shape instanceof GroupShape) {
			GroupShape<?, ?> groupShape = (GroupShape<?, ?>) shape;
			for (Shape<?, ?> child : groupShape) {
				preparedTasks.addAll(prepare(child));
			}
		} else if (shape instanceof TextShape) {
			TextShape<?, ?> textShape = (TextShape<?, ?>) shape;
			Integer fontIndex = null;
			for (TextParagraph<?, ?, ?> textParagraph : textShape) {
				List<TextRun> textRuns = (List<TextRun>) textParagraph.getTextRuns();
				if (textRuns.isEmpty()) {
					if (textParagraph instanceof XSLFTextParagraph) {
						XSLFTextParagraph xslfTextParagraph = (XSLFTextParagraph) textParagraph;
						if (isMathParagraph(xslfTextParagraph)) {
							preparedTasks.add(() -> createTextBox((XSLFShape) shape,
									"tnxjeex.doc.ppt.unable_to_display_formula"));
						}
					}
				} else {
					for (TextRun textRun : textRuns) {
						String text = textRun.getRawText();
						// 文本中包含中文但字体为非中文字体，则一律修改为默认字体，以解决中英文混杂时乱码的问题
						if (StringUtil.containsChinese(text)) {
							String fontFamily = textRun.getFontFamily();
							if (!StringUtil.isChineseFontFamily(fontFamily)) {
								textRun.setFontFamily(this.defaultFontFamily);
								fontIndex = textRun.getFontInfo(FontGroup.LATIN).getIndex();
							}
						}
					}
				}
			}
			// 使ppt格式文件的字体更改在生成图片时生效
			if (fontIndex != null && textShape instanceof HSLFTextShape) {
				HSLFTextShape hslfTextShape = (HSLFTextShape) textShape;
				EscherTextboxWrapper textboxWrapper = getTextboxWrapper(hslfTextShape);
				if (textboxWrapper != null) { // 修改样式中的字体索引，该字体索引为生成图片时使用字体的标识
					StyleTextPropAtom styleTextPropAtom = textboxWrapper.getStyleTextPropAtom();
					if (styleTextPropAtom != null) {
						List<TextPropCollection> characterStyles = styleTextPropAtom.getCharacterStyles();
						for (TextPropCollection textPropCollection : characterStyles) {
							boolean modified = false;
							List<TextProp> textPropList = textPropCollection.getTextPropList();
							for (TextProp textProp : textPropList) {
								if (textProp.getName().endsWith(TEXT_PROP_NAME__FONT_INDEX)) {
									textProp.setValue(fontIndex);
									modified = true;
								}
							}
							if (!modified) { // 如果样式中原本没有字体索引，则加入字体索引以备用
								textPropCollection.addWithName(TEXT_PROP_NAME__FONT_INDEX).setValue(fontIndex);
							}
						}
					}
				}
			}
		} else if (shape.getClass() == XSLFGraphicFrame.class) {
			XSLFGraphicFrame graphicFrame = (XSLFGraphicFrame) shape;
			preparedTasks.add(() -> {
				String messageKey = graphicFrame.getChart() == null ? "tnxjeex.doc.ppt.unable_to_display_smart_art"
						: "tnxjeex.doc.ppt.unable_to_display_chart";
				createTextBox(graphicFrame, messageKey);
			});
		}
		return preparedTasks;
	}

	private boolean isMathParagraph(XSLFTextParagraph xslfTextParagraph) {
		CTTextParagraph ctTextParagraph = xslfTextParagraph.getXmlObject();
		Node node = ctTextParagraph.getDomNode();
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node item = nodeList.item(i);
			if ("m".equals(item.getLocalName())) {
				nodeList = item.getChildNodes();
				for (int j = 0; j < nodeList.getLength(); j++) {
					item = nodeList.item(j);
					if ("oMathPara".equals(item.getLocalName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void createTextBox(XSLFShape templateShape, String messageKey) {
		XSLFSheet sheet = templateShape.getSheet();
		XSLFTextBox textBox = sheet.createTextBox();
		textBox.setAnchor(templateShape.getAnchor());
		textBox.setText(DocUtil.getMessage(messageKey));
		textBox.setVerticalAlignment(VerticalAlignment.MIDDLE);
		textBox.setLineColor(Color.GRAY);
		XSLFTextParagraph textParagraph = textBox.getTextParagraphs().get(0);
		textParagraph.setTextAlign(TextParagraph.TextAlign.CENTER);
		XSLFTextRun textRun = textParagraph.getTextRuns().get(0);
		textRun.setFontColor(Color.GRAY);
		textRun.setFontFamily(this.defaultFontFamily);
		if (templateShape instanceof XSLFTextShape) {
			List<XSLFTextParagraph> textParagraphs = ((XSLFTextShape) templateShape).getTextParagraphs();
			if (textParagraphs.size() > 0) {
				textParagraph = textParagraphs.get(0);
				textRun.setFontSize(textParagraph.getDefaultFontSize());
			}
		}
	}

	private EscherTextboxWrapper getTextboxWrapper(HSLFTextShape hslfTextShape) {
		PPDrawing drawing = hslfTextShape.getSheet().getPPDrawing();
		if (drawing != null) {
			EscherTextboxWrapper textboxWrapper = null;
			EscherTextboxWrapper[] wrappers = drawing.getTextboxWrappers();
			if (wrappers != null) {
				EscherTextboxRecord textboxRecord = hslfTextShape.getEscherChild(EscherTextboxRecord.RECORD_ID);
				for (EscherTextboxWrapper wrapper : wrappers) {
					if (textboxRecord == wrapper.getEscherRecord()) {
						textboxWrapper = wrapper;
						break;
					}
				}
			}
			// 当在drawing中找不到对应的textboxWrapper时，取出文本形状中的textboxWrapper加入drawing中，
			// 以解决在此之前修改的字体，在后续导出为图片时不生效的问题。
			// TODO 由于HSLFTextShape未提供获取textboxWrapper的支持，使用了反射机制获取，后续版本更新时需检查是否有变化
			if (textboxWrapper == null) {
				textboxWrapper = BeanUtil.getFieldValue(hslfTextShape, "_txtbox");
				drawing.addTextboxWrapper(textboxWrapper);
			}
			return textboxWrapper;
		}
		return null;
	}

	/**
	 * 判断指定页中是否包含不可渲染为图片的形状
	 *
	 * @param pageIndex 页索引序号
	 * @return 指定页中是否包含不可渲染为图片的形状
	 */
	@SuppressWarnings("unchecked")
	public boolean containsUnrenderableShape(int pageIndex) {
		if (this.origin instanceof XMLSlideShow) {
			Slide<?, ?> slide = getPage(pageIndex);
			if (slide != null) {
				List<XSLFShape> shapes = (List<XSLFShape>) slide.getShapes();
				for (XSLFShape shape : shapes) {
					if (isUnrenderable(shape)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isUnrenderable(XSLFShape shape) {
		if (shape instanceof XSLFGroupShape) {
			XSLFGroupShape groupShape = (XSLFGroupShape) shape;
			for (XSLFShape child : groupShape) {
				isUnrenderable(child);
			}
		} else if (shape instanceof XSLFTextShape) {
			XSLFTextShape textShape = (XSLFTextShape) shape;
			for (XSLFTextParagraph textParagraph : textShape) {
				List<XSLFTextRun> textRuns = textParagraph.getTextRuns();
				if (textRuns.isEmpty() && isMathParagraph(textParagraph)) {
					return true;
				}
			}
		}
		return shape.getClass() == XSLFGraphicFrame.class;
	}

	public String getText() {
		SlideShowExtractor<?, ?> extractor = new SlideShowExtractor<>(this.origin);
		String text = extractor.getText();
		try {
			extractor.close();
		} catch (IOException e) {
			LogUtil.error(getClass(), e);
		}
		return text;
	}

	public void close() {
		try {
			this.origin.close();
		} catch (IOException e) {
			LogUtil.error(getClass(), e);
		}
	}

}
