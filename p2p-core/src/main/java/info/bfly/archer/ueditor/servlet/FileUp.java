package info.bfly.archer.ueditor.servlet;

import info.bfly.core.annotations.Log;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class FileUp extends HttpServlet {
    @Log
    Logger log;
    private static final long serialVersionUID = -8466749084781286971L;

    /**
     * Constructor of the object.
     */
    public FileUp() {
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
     * <p>
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * The doPost method of the servlet. <br>
     * <p>
     * This method is called when a form has its tag value method equals to
     * post.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Uploader up = new Uploader(request);
        up.setSavePath("upload"); // ????????????
        String[] fileType = {".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".swf", ".wmv"}; // ?????????????????????
        up.setAllowFiles(fileType);
        up.setMaxSize(10000); // ????????????????????????????????????KB
        try {
            up.upload();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        response.getWriter().print("{'url':'" + up.getUrl() + "','fileType':'" + up.getType() + "','state':'" + up.getState() + "','original':'" + up.getOriginalName() + "'}");
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    @Override
    public void init() throws ServletException {
        // Put your code here
    }
}
