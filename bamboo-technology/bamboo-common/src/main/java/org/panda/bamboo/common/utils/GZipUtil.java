package org.panda.bamboo.common.utils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZip压缩工具
 *
 * @author fangen
 */
public class GZipUtil {
	public static final int BUFFER = 1024;  
    public static final String EXT = ".gz";

    /**
     * 字节数组压缩后输出
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] compress(byte[] data) throws Exception {  
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compress(bais, baos);
        byte[] output = baos.toByteArray();
        baos.flush();  
        baos.close();
        bais.close();
        return output;  
    }

    /**
     * 文件压缩为GZ压缩包
     *
     * @param path 文件路径
     * @param isDelete 是否删除原文件
     * @throws Exception
     */
    public static void compress(String path, boolean isDelete) throws Exception {
        File file = new File(path);
        compress(file, isDelete);
    }

    /**
     * 文件压缩为GZ压缩包
     *
     * @param file 文件
     * @param isDelete 是否删除原文件
     * @throws Exception
     */
    public static void compress(File file, boolean isDelete) throws Exception {
        FileInputStream fis = new FileInputStream(file);  
        FileOutputStream fos = new FileOutputStream(file.getPath() + EXT);
        compress(fis, fos);
        fis.close();  
        fos.flush();  
        fos.close();
        if (isDelete) {
            file.delete();  
        }  
    }

    private static void compress(InputStream is, OutputStream os) throws Exception {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        int count;
        byte data[] = new byte[BUFFER];
        while ((count = is.read(data, 0, BUFFER)) != -1) {
            gos.write(data, 0, count);
        }
        gos.finish();
        gos.flush();
        gos.close();
    }


    /**
     * 解压缩，还原文件本来大小
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] decompress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decompress(bais, baos);
        data = baos.toByteArray();
        baos.flush();  
        baos.close();
        bais.close();
        return data;  
    }

    /**
     * 解压缩
     *
     * @param path
     * @param delete
     * @throws Exception
     */
    public static void decompress(String path, boolean delete) throws Exception {
        File file = new File(path);
        decompress(file, delete);
    }

    /**
     * 解压缩
     *
     * @param file
     * @param delete
     * @throws Exception
     */
    public static void decompress(File file, boolean delete) throws Exception {  
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath().replace(EXT, ""));
        decompress(fis, fos);  
        fis.close();  
        fos.flush();  
        fos.close();
        if (delete) {  
            file.delete();  
        }  
    }  

    private static void decompress(InputStream is, OutputStream os) throws Exception {
        GZIPInputStream gis = new GZIPInputStream(is);
        int count;  
        byte data[] = new byte[BUFFER];
        while ((count = gis.read(data, 0, BUFFER)) != -1) {  
            os.write(data, 0, count);  
        }
        gis.close();  
    }
}
