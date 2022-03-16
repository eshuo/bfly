package info.bfly.archer.user.servlet;

import info.bfly.archer.common.service.CaptchaService;
import info.bfly.core.annotations.Log;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class VerifyCodeServlet extends HttpServlet {
    private static final long serialVersionUID = -6424784479369309597L;
    @Log
    private Logger log;
    @Resource
    CaptchaService captchaSrv;

    /**
     * Constructor of the object.
     */
    public VerifyCodeServlet() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    @Override
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * The doPost method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to
     * post.
     *
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // JPEGImageEncoder encoder =
            // JPEGCodec.createJPEGEncoder(response.getOutputStream());
            // encoder.encode(captchaSrv.generateCaptchaImg(request.getSession()));
            ImageIO.write(captchaSrv.generateCaptchaImg(request.getSession()), "JPG", response.getOutputStream());
        }
        catch (IOException e) {
            log.debug(e.getMessage());
        }
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException
     *             if an error occurs
     */
    @Override
    public void init() throws ServletException {
        // Put your code here
    }
}
