package info.bfly.archer.common.service;

import java.awt.image.BufferedImage;

import javax.servlet.http.HttpSession;

/**
 * Description: 验证码service
 */
public interface CaptchaService {
    /**
     * 清空验证码
     *
     * @param session
     */
    void clearCaptcha(HttpSession session);

    /**
     * 生成验证码图片，验证码在session中
     *
     * @return 验证码图片
     */
    BufferedImage generateCaptchaImg(HttpSession session);

    /**
     * 验证验证码正确与否，验证码在session中
     *
     * @param captchaStr
     *            被验证的验证码
     * @return
     */
    boolean verifyCaptcha(String captcha, HttpSession session);
}
