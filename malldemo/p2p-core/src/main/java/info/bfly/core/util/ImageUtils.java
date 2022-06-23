package info.bfly.core.util;

import info.bfly.archer.config.ConfigConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;




public final class ImageUtils {

    private final static Logger        log = LoggerFactory.getLogger(ImageUtils.class);
    private final static StringManager sm  = StringManager.getManager(ConfigConstants.Package);

    /**
     * 打印文字水印图片
     *
     * @param pressText --文字
     * @param sourceImg --
     *                  <p>
     *                  目标图片
     * @param fontName  --
     *                  <p>
     *                  字体名
     * @param fontStyle --
     *                  <p>
     *                  字体样式
     * @param color     --
     *                  <p>
     *                  字体颜色
     * @param fontSize  --
     *                  <p>
     *                  字体大小
     * @param x         --
     *                  <p>
     *                  偏移量
     * @param y
     */
    public static BufferedImage addWaterMark(String pressText, String sourceImg, String fontName, int fontStyle, Color color, int fontSize, int x, int y) {
        BufferedImage image = null;
        try {
            File _file = new File(sourceImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.drawString(pressText, wideth - fontSize - fontSize * 5, height - fontSize / 2 - fontSize * 2);
            g.dispose();
        } catch (Exception e) {
            ImageUtils.log.error(ImageUtils.sm.getString("log.error.watermark"));
            log.debug(e.getMessage());
        }
        return image;
    }

    /**
     * 把图片印刷到图片上
     *
     * @param pressImg  -- 水印文件
     * @param targetImg -- 目标文件
     * @param x         --x坐标
     * @param y         --y坐标
     */
    public final static BufferedImage pressImage(String pressImg, String targetImg, int x, int y) {
        BufferedImage image = null;
        try {
            // 目标文件
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            // 水印文件
            File _filebiao = new File(pressImg);
            Image src_biao = ImageIO.read(_filebiao);
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.drawImage(src_biao, (wideth - wideth_biao) / 2, (height - height_biao) / 2, wideth_biao, height_biao, null);
            // 水印文件结束
            g.dispose();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return image;
    }

    public ImageUtils() {
    }
}
