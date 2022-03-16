package info.bfly.archer.ueditor.servlet;

import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class ImageManager extends HttpServlet {
    private static final long serialVersionUID = 2246863787212907136L;

    /**
     * Constructor of the object.
     */
    public ImageManager() {
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
        String path = "upload";
        String imgStr = "";
        String realpath = getRealPath(request, path) + "/" + path;
        List<File> files = getFiles(realpath, new ArrayList());
        for (File file : files) {
            imgStr += file.getPath().replace(getRealPath(request, path), "") + "ue_separate_ue";
        }
        if (imgStr != "") {
            imgStr = imgStr.substring(0, imgStr.lastIndexOf("ue_separate_ue")).replace(File.separator, "/").trim();
        }
        response.getWriter().print(imgStr);
    }

    private List getFiles(String realpath, List files) {
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files);
                } else {
                    if (!getFileType(file.getName()).equals("")) {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }

    private String getFileType(String fileName) {
        String[] fileType = {".gif", ".png", ".jpg", ".jpeg", ".bmp"};
        Iterator<String> type = Arrays.asList(fileType).iterator();
        while (type.hasNext()) {
            String t = type.next();
            if (fileName.endsWith(t)) {
                return t;
            }
        }
        return "";
    }

    private String getRealPath(HttpServletRequest request, String path) {
        ServletContext application = request.getSession().getServletContext();
        String str = application.getRealPath(request.getServletPath());
        return new File(str).getParent();
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
