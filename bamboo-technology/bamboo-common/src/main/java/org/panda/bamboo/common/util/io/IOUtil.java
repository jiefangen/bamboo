package org.panda.bamboo.common.util.io;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.EncryptUtil;
import org.panda.bamboo.common.util.LogUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * IO工具类
 *
 * @author fangen
 */
public class IOUtil {

    /**
     * 文件路径分隔符
     */
    public static final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");

    public static final int DEFAULT_BUFFER_SIZE = 4096;

    private IOUtil() {
    }

    public static void coverToFile(File file, String data, String encoding) throws IOException {
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        OutputStream out = new FileOutputStream(file);
        out.write(data.getBytes(encoding));
        out.flush();
        out.close();
    }

    /**
     * 以非阻塞方式，读取指定字节输入流中的数据为字符串
     *
     * @param in 字节输入流
     * @return 结果字符串
     * @throws IOException 如果读取过程中出现错误
     */
    public static String readUnblocklyToString(InputStream in) throws IOException {
        return readUnblocklyToString(new BufferedReader(new InputStreamReader(in)));
    }

    /**
     * 以非阻塞方式，以指定字符集读取指定字节输入流中的数据为字符串
     *
     * @param in          字节输入流
     * @param charsetName 字符集
     * @return 结果字符串
     * @throws IOException 如果读取过程中出现错误
     */
    public static String readUnblocklyToString(InputStream in, String charsetName) throws IOException {
        return readUnblocklyToString(new BufferedReader(new InputStreamReader(in, charsetName)));
    }

    /**
     * 以非阻塞方式，读取指定字符输入流中的数据为字符串。如果对字符集有要求，请先将字符输入流的字符集设置好
     *
     * @param reader 字符输入流
     * @return 结果字符串
     * @throws IOException 如果读取过程中出现错误
     */
    public static String readUnblocklyToString(Reader reader) throws IOException {
        String s = "";
        char[] c = new char[1024];
        while (reader.ready()) {
            s += new String(c, 0, reader.read(c));
        }
        return s;
    }

    /**
     * 执行指定命令行指令，如果等待毫秒数大于0，则当前线程等待指定毫秒数之后返回，
     *
     * @param command 命令行指令
     * @return 执行结果
     */
    public static String executeCommand(String command) {
        String result = "";
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            result = IOUtils.toString(process.getInputStream(), Strings.ENCODING_UTF8);
        } catch (IOException | InterruptedException e) {
            LogUtil.error(IOUtil.class, e);
        }
        return result;
    }

    /**
     * 创建指定文件到本地文件系统，如果该文件已存在则不创建
     *
     * @param file 文件
     * @throws IOException 如果创建文件时出现错误
     */
    public static void createFile(File file) throws IOException {
        if (!file.exists()) {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file.createNewFile();
        }
    }

    /**
     * 创建指定目录到本地文件系统，如果该目录已存在则不创建
     *
     * @param dir 目录
     */
    public static void createDirectory(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 替换指定文件指定内容
     *
     * @param filePath    被修改文件路径
     * @param regex       被替换内容
     * @param replacement 替换内容
     */
    public static void replaceFileContent(String filePath, String regex, String replacement) {
        BufferedReader br = null;
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
            }
        } catch (Exception e) {
            LogUtil.error(IOUtil.class, e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
        String s = sb.toString().replaceAll(regex, replacement);
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filePath));
            bw.write(s);
        } catch (Exception e) {
            LogUtil.error(IOUtil.class, e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    bw = null;
                }
            }
        }
    }

    /**
     * 查找与指定区域匹配的国际化的资源
     *
     * @param basename  文件基本名
     * @param locale    区域
     * @param extension 文件扩展名
     * @return 与指定区域匹配的国际化的资源，如果找不到则返回null
     */
    public static Resource findI18nResource(String basename, Locale locale, String extension) {
        basename = basename.trim();
        Assert.hasText(basename, "Basename must not be empty");
        basename = basename.replace('\\', '/');
        // 把basename中classpath:替换为classpath*:后进行查找
        StringBuilder searchBasename = new StringBuilder(
                basename.replace(ResourceUtils.CLASSPATH_URL_PREFIX, ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX))
                .append(Strings.ASTERISK);
        if (!extension.startsWith(Strings.DOT)) {
            searchBasename.append(Strings.DOT);
        }
        searchBasename.append(extension);
        // 查找文件资源
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resourcePatternResolver.getResources(searchBasename.toString());
            Resource result = null;
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                String[] fileNameArray = fileName.split(Strings.UNDERLINE);
                if (StringUtils.indexOfIgnoreCase(fileName, locale.getLanguage() + "_" + locale.getCountry(), 0) >= 0) {
                    result = resource;
                    break;
                } else if (StringUtils.indexOfIgnoreCase(fileName, locale.getLanguage(), 0) >= 0) {
                    result = resource;
                } else if (result == null && fileNameArray.length == 1) {
                    result = resource;
                }
            }
            return result;
        } catch (IOException e) {
            LogUtil.error(IOUtil.class, e);
        }
        return null;
    }

    /**
     * 查找与指定目录下区域匹配的国际化的文件
     *
     * @param baseDir   目录
     * @param basename  文件基本名称 aa，不含扩展名
     * @param locale    区域
     * @param extension 文件扩展名，句点包含与否均可
     * @return 找到的文件，如果没找到则返回null
     */
    public static File findI18nFileByDir(String baseDir, String basename, String extension, Locale locale) {
        StringBuilder searchFileName = new StringBuilder(basename).append(Strings.ASTERISK);
        if (!extension.startsWith(Strings.DOT)) {
            searchFileName.append(Strings.DOT);
        }
        searchFileName.append(extension);
        List<File> resultList = new ArrayList<>();
        findFiles(baseDir, searchFileName.toString(), resultList);

        File returnFile = null;
        if (resultList.size() > 0) {
            for (File file : resultList) {
                String resultFileName = file.getName();
                String[] fileNameArray = resultFileName.split(Strings.UNDERLINE);
                if (StringUtils.indexOfIgnoreCase(resultFileName, locale.getLanguage() + "_" + locale.getCountry(),
                        0) >= 0) {
                    returnFile = file;
                    break;
                } else if (StringUtils.indexOfIgnoreCase(resultFileName, locale.getLanguage(), 0) >= 0) {
                    returnFile = file;
                } else if (returnFile == null && fileNameArray.length == 1) {
                    returnFile = file;
                }
            }

        }
        return returnFile;
    }

    /**
     * 递归查找文件
     *
     * @param baseDirName    查找的文件夹路径
     * @param targetFileName 需要查找的文件名
     * @param fileList       查找到的文件集合
     */
    public static void findFiles(String baseDirName, String targetFileName, List<File> fileList) {
        File baseDir = new File(baseDirName); // 创建一个File对象
        if (baseDir.exists() && baseDir.isDirectory()) { // 判断目录是否存在
            // 判断目录是否存在
            File tempFile;
            File[] files = baseDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    tempFile = file;
                    if (tempFile.isDirectory()) {
                        findFiles(tempFile.getAbsolutePath(), targetFileName, fileList);
                    } else if (tempFile.isFile()) {
                        if (wildcardMatch(targetFileName, tempFile.getName())) {
                            // 匹配成功，将文件名添加到结果集
                            fileList.add(tempFile);
                        }
                    }
                }
            }
        }
    }

    /**
     * 通配符匹配
     *
     * @param pattern 通配符模式
     * @param str     待匹配的字符串
     * @return 匹配成功则返回true，否则返回false
     */
    private static boolean wildcardMatch(String pattern, String str) {
        int patternLength = pattern.length();
        int strLength = str.length();
        int strIndex = 0;
        char ch;
        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
            ch = pattern.charAt(patternIndex);
            if (ch == '*') {
                // 通配符星号*表示可以匹配任意多个字符
                while (strIndex < strLength) {
                    if (wildcardMatch(pattern.substring(patternIndex + 1), str.substring(strIndex))) {
                        return true;
                    }
                    strIndex++;
                }
            } else if (ch == '?') {
                // 通配符问号?表示匹配任意一个字符
                strIndex++;
                if (strIndex > strLength) {
                    // 表示str中已经没有字符匹配?了。
                    return false;
                }
            } else {
                if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
                    return false;
                }
                strIndex++;
            }
        }
        return (strIndex == strLength);
    }

    public static File getTomcatTempDir() throws IOException {
        File root = new ClassPathResource(Strings.DOT).getFile().getParentFile().getParentFile().getParentFile()
                .getParentFile();
        return new File(root, "/temp");
    }

    public static boolean copyFile(File source, File target) {
        if (source.exists()) {
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                createFile(target);
                in = new FileInputStream(source);
                FileChannel inChannel = in.getChannel();
                FileLock readLock = inChannel.lock(0, inChannel.size(), true);
                out = new FileOutputStream(target);
                FileChannel outChannel = out.getChannel();
                FileLock writeLock = outChannel.lock();
                outChannel.transferFrom(inChannel, 0, inChannel.size());
                writeLock.release();
                readLock.release();
                outChannel.close();
                inChannel.close();
                return true;
            } catch (IOException e) {
                LogUtil.error(IOUtil.class, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        LogUtil.error(IOUtil.class, e);
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        LogUtil.error(IOUtil.class, e);
                    }
                }
            }
        }
        return false;
    }

    /**
     * 克隆原生流
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static InputStream cloneInputStream(InputStream inputStream, Integer bufferSize) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bufferSize == null || bufferSize <= 0) {
            bufferSize = 1024;
        }
        // 将输入流的内容读取到字节数组缓存中
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        // 从字节数组缓存创建新的输入流
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    /**
     * 克隆原生流
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static InputStream cloneInputStream(InputStream inputStream) throws IOException {
        return cloneInputStream(inputStream, -1);
    }

    /**
     * 从指定输入流当前位置开始，偏移指定偏移量后，尽可能地复制指定期望长度的内容至指定输出流，即使中途因错中止，也返回实际已复制的长度
     *
     * @param in     输入流
     * @param out    输出流
     * @param offset 偏移量
     * @param length 期望复制长度，小于0时表示复制剩余全部
     * @return 实际已复制的长度，无论复制过程中是否出现错误而中止
     */
    public static long copyAsPossible(InputStream in, OutputStream out, long offset, long length) {
        // 改编自IOUtils.copyLarge(InputStream input, OutputStream output, long inputOffset, long length,
        // byte[] buffer)
        long totalRead = 0; // 已读总量
        try {
            if (offset > 0) {
                IOUtils.skipFully(in, offset);
            }
            if (length == 0) {
                return 0;
            }
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            final int bufferLength = buffer.length;
            int bytesToRead = bufferLength;
            if (length > 0 && length < bufferLength) {
                bytesToRead = (int) length;
            }
            int bufferRead; // 缓存区已读量
            while (bytesToRead > 0 && (bufferRead = in.read(buffer, 0, bytesToRead)) >= 0) {
                out.write(buffer, 0, bufferRead);
                totalRead += bufferRead;
                if (length > 0) {
                    bytesToRead = (int) Math.min(length - totalRead, bufferLength);
                }
            }
        } catch (IOException e) {
            LogUtil.warn(IOUtil.class, e.getMessage());
        }
        return totalRead;
    }

    public static boolean isBinary(InputStream in) {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        if (!in.markSupported()) {
            in = new BufferedInputStream(in);
        }
        try {
            in.mark(buffer.length);
            int length = in.read(buffer);
            in.reset();

            int ctrlNum = 0;
            for (int i = 0; i < length; i++) {
                char c = (char) buffer[i];
                if (!Character.isWhitespace(c) && Character.isISOControl(c)) {
                    if (ctrlNum++ > 4) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            LogUtil.error(IOUtil.class, e);
        }
        return false;
    }

    public static boolean isEmptyDictionary(File file) {
        if (file.exists() && file.isDirectory()) {
            try {
                DirectoryStream<Path> stream = Files.newDirectoryStream(file.toPath());
                return !stream.iterator().hasNext();
            } catch (IOException e) {
                LogUtil.error(IOUtil.class, e);
            }
        }
        return false;
    }

    public static String toBase64Data(byte[] bytes, String extension) {
        String base64String = EncryptUtil.encryptByBase64(bytes);
        return "data:" + Mimetypes.getInstance().getMimetype(extension) + ";base64," + base64String;
    }

    public static FileLock lock(File file, boolean readOnly) throws IOException {
        if (file.exists()) {
            String mode = readOnly ? "r" : "rw";
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, mode);
            FileChannel channel = randomAccessFile.getChannel();
            FileLock lock = channel.lock(0, file.length(), readOnly);
            randomAccessFile.close();
            return lock;
        }
        return null;
    }

}
