package info.bfly.pay.util;

import info.bfly.core.annotations.Log;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Created by XXSun on 5/18/2017.
 */
@Service
public class ZipUtil {

    @Log
    Logger log;
    private static int      bufSize = 1024;    //size of bytes
    private byte[]          buf;

    /**
     * @param zipFile    目的地Zip文件
     * @param sourceFile 源文件（带压缩的文件或文件夹）
     * @throws Exception
     */
    public boolean zip(File zipFile, File sourceFile)  {
        buf = new byte[bufSize];
        log.info("压缩中...");
        try{

        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        //调用函数
        compress(out, sourceFile, sourceFile.getName());
        out.close();
        }catch (Exception e){
            log.error("压缩失败 {}",e);
            return false;
        }
        log.info("压缩完成");
        return true;

    }

    public void compress(ZipOutputStream out,  File sourceFile, String base) throws Exception {
        //如果路径为目录（文件夹）
        if (sourceFile.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();
            if (flist.length == 0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
            {
                log.info("zip {}/ ",base);
                out.putNextEntry(new ZipEntry(base + "/"));
                out.closeEntry();
            } else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            {
                for (File aFlist : flist) {
                    compress(out,  aFlist, base + "/" + aFlist.getName());
                }
            }
        } else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
        {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream fos = new FileInputStream(sourceFile);
            int tag;
            log.info("zip {} ",base);
            //将源文件写入到zip文件中
            while ((tag = fos.read(this.buf)) != -1) {
                out.write(this.buf,0,tag);
            }
            out.closeEntry();
            fos.close();
        }
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public static int getBufSize() {
        return bufSize;
    }

    public static void setBufSize(int bufSize) {
        ZipUtil.bufSize = bufSize;
    }


}