package info.bfly.core.util;

import info.bfly.archer.ueditor.servlet.ImageUpload;
import info.bfly.core.jsf.util.FacesUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUploadUtil {
    private static final Logger           log         = LoggerFactory.getLogger(ImageUpload.class);
    public final static String           UPLOAD_PATH = "/upload";
    public final static String          ZIP_PATH = "/zip";
    public static       SimpleDateFormat formater    = new SimpleDateFormat("yyyyMMdd");

    /**
     * 获取文件扩展名
     *
     * @return string
     */
    private static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 依据原始文件名生成新文件名
     *
     * @return
     */
    private static String getName(String fileName) {
        Random random = new Random();
        return "" + random.nextInt(10000) + System.currentTimeMillis() + ImageUploadUtil.getFileExt(fileName);
    }

    /**
     * 根据字符串创建本地目录 并按照日期建立子目录返回
     *
     * @param path
     * @return
     */
    private static void mkdir(final String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        }
    }

    public static String upload(InputStream is, String fileName) {
        return upload(is, fileName, StringUtils.EMPTY);
    }

    public static String upload(InputStream is, String fileName, String type) {
        try {
            final String path = type + ImageUploadUtil.UPLOAD_PATH + "/" + ImageUploadUtil.formater.format(new Date());
            final String absPath = FacesUtil.getRealPath(path);
            fileName = ImageUploadUtil.getName(fileName);
            final String savefile = absPath + "/" + fileName;
            ImageUploadUtil.mkdir(absPath);
            FileOutputStream out = new FileOutputStream(savefile);
            byte[] buffer = new byte[2048];
            int x = 0;
            while ((x = is.read(buffer)) != -1) {
                out.write(buffer, 0, x);
            }
            is.close();
            out.close();
            return (path + "/" + fileName);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void delete(String filePath) {
        if(StringUtils.isEmpty(filePath))
            return;
        File file = new File(FacesUtil.getRealPath(filePath));
        if (file.exists()) {
            final String fileFolder = StringUtils.substringBeforeLast(filePath, "/");
            final String absPath = FacesUtil.getPicBackUpPath() + fileFolder;
            ImageUploadUtil.mkdir(absPath);
            file.renameTo(new File(FacesUtil.getPicBackUpPath() + filePath));
        }

    }
}
