package info.bfly.archer.common.service.impl;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.service.CaptchaService;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 */
@Service("captchaService")
public class CaptchaServiceImpl implements CaptchaService {
    /**
     * codeSequence 表示字符允许出现的序列值
     */
    private static char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', '3', '4', '5', '6', '7', '8', '9'};
    /**
     * 验证码字符个数
     */
    private        int    codeCount    = 4;

    @Override
    public void clearCaptcha(HttpSession session) {
        session.removeAttribute(CommonConstants.CaptchaFlag.CAPTCHA_SESSION);
    }

    @Override
    public BufferedImage generateCaptchaImg(HttpSession session) {
        int width = 80;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, 1);
        Graphics g = image.getGraphics();
        Random random = new Random();
        int fc = 200;
        int bc = 250;
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r1 = fc + random.nextInt(bc - fc);
        int g1 = fc + random.nextInt(bc - fc);
        int b1 = fc + random.nextInt(bc - fc);
        g.setColor(new Color(r1, g1, b1));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Arial", 0, 25));
        int r2 = fc + random.nextInt(bc - fc);
        int g2 = fc + random.nextInt(bc - fc);
        int b2 = fc + random.nextInt(bc - fc);
        g.setColor(new Color(r2, g2, b2));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width + 100);
            int y = random.nextInt(height + 100);
            int xl = random.nextInt(10);
            int yl = random.nextInt(12);
            g.drawOval(x, y, x + xl, y + yl);
        }
        StringBuffer sRand = new StringBuffer();
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String strRand = String.valueOf(CaptchaServiceImpl.codeSequence[random.nextInt(CaptchaServiceImpl.codeSequence.length)]);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(strRand, 14 * i + 5, 25);
            sRand.append(strRand);
        }
        g.dispose();
        session.setAttribute(CommonConstants.CaptchaFlag.CAPTCHA_SESSION, sRand.toString().toUpperCase());
        return image;
    }

    @Override
    public boolean verifyCaptcha(String captcha, HttpSession session) {
        Object captchaObj = session.getAttribute(CommonConstants.CaptchaFlag.CAPTCHA_SESSION);
        String captchaInSession = (null == captchaObj ? "" : captchaObj.toString());
        return !(StringUtils.isEmpty(captchaInSession) || !captchaInSession.equalsIgnoreCase(captcha));
    }
}
