package info.bfly.archer.system.service.impl;

import info.bfly.archer.config.ConfigConstants;
import info.bfly.archer.config.model.Config;
import info.bfly.archer.system.service.AppLocalFilter;
import info.bfly.core.annotations.Log;
import info.bfly.core.util.ImageUtils;
import info.bfly.core.util.SpringBeanUtil;
import info.bfly.core.util.StringManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class WatermarkFilterImpl implements AppLocalFilter {
    @Log
    static Logger log;
    private final static StringManager sm = StringManager.getManager(ConfigConstants.Package);
    // @Resource
    HibernateTemplate ht;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
        ht = (HibernateTemplate) SpringBeanUtil.getBeanByName("ht");
        Config config = null;
        try {
            config = ht.get(Config.class, ConfigConstants.WaterMark.IF_OPEN_WATERMARK);
        } catch (Exception e) {
            WatermarkFilterImpl.log.error(WatermarkFilterImpl.sm.getString("log.error.configWatermarkNotFound", ConfigConstants.WaterMark.IF_OPEN_WATERMARK, e.toString()));
            log.debug(e.getMessage());
            return;
        }
        String isOpen = ConfigConstants.WaterMark.UN_OPEN_WATERMARK;
        if (config == null) {
            WatermarkFilterImpl.log.warn(WatermarkFilterImpl.sm.getString("log.notFoundConfig", ConfigConstants.WaterMark.IF_OPEN_WATERMARK));
        } else {
            isOpen = config.getValue();
        }
        if (isOpen.equals(ConfigConstants.WaterMark.UN_OPEN_WATERMARK)) {
            return;
        }
        HttpServletRequest req = (HttpServletRequest) request;
        String url = req.getRequestURI();
        boolean needDeal = url.endsWith(".gif") || url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".bmp");
        final String uploadPath = "/upload/";
        final String uploadCachePath = "/upload/cache/";
        boolean needDealConfirm = StringUtils.contains(url, uploadPath);
        if ((needDealConfirm && needDeal) == false) {
            return;
        }
        String contextPath = req.getContextPath();
        url = url.replaceFirst(contextPath, "");
        String pictureAbsolutePath = req.getSession().getServletContext().getRealPath(url);// 得到图片所在的据对路径
        // 原始图片
        File picDirFile = new File(pictureAbsolutePath);
        if (!picDirFile.exists()) {
            return; // 如果文件不存在，直接返回
        }
        // 缓存的图片
        String picCachePath = url.replaceFirst(uploadPath, uploadCachePath);
        String picCacheAbsolutePath = req.getSession().getServletContext().getRealPath(picCachePath);
        File picCacheFile = new File(picCacheAbsolutePath);
        BufferedImage image;
        if (!picCacheFile.exists()) {
            // 缓存不存在，创建
            String folderPath = picCacheAbsolutePath.substring(0, picCacheAbsolutePath.lastIndexOf(File.separator));
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            Color c = new Color(0, 0, 0);
            Config configWibeSite = ht.get(Config.class, ConfigConstants.Website.SITE_NAME);
            image = ImageUtils.addWaterMark(configWibeSite.getValue(), pictureAbsolutePath, WatermarkFilterImpl.sm.getString("waterMarkFontFamily"), Font.LAYOUT_RIGHT_TO_LEFT, c, 18, 300, 200);
            ImageIO.write(image, "JPEG", picCacheFile);
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher(picCachePath);// 转发到缓存
        dispatcher.forward(request, response);// 实现转发
    }
}
