package info.bfly.archer.ueditor.servlet;

import info.bfly.core.annotations.Log;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

@Service
public class GetRemoteImage extends HttpServlet {
    private static final long serialVersionUID = -2992793247000204583L;

    @Log
    private Logger log;

    /**
     * Constructor of the object.
     */
    public GetRemoteImage() {
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
        String url = request.getParameter("upfile");
        String state = "???????????????????????????";
        ServletContext application = request.getSession().getServletContext();
        String filePath = "upload";
        String[] arr = url.split("ue_separate_ue");
        String[] outSrc = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            // ??????????????????
            String str = application.getRealPath(request.getServletPath());
            File f = new File(str);
            String savePath = f.getParent() + "/" + filePath;
            // ????????????
            String type = getFileType(arr[i]);
            if (type.equals("")) {
                state = "????????????????????????";
                continue;
            }
            String saveName = Long.toString(new Date().getTime()) + type;
            // ????????????
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection conn = (HttpURLConnection) new URL(arr[i]).openConnection();
            if (conn.getContentType().indexOf("image") == -1) {
                state = "????????????????????????";
                continue;
            }
            if (conn.getResponseCode() != 200) {
                state = "????????????????????????";
                continue;
            }
            File dir = new File(savePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File savetoFile = new File(savePath + "/" + saveName);
            outSrc[i] = filePath + "/" + saveName;
            try {
                InputStream is = conn.getInputStream();
                OutputStream os = new FileOutputStream(savetoFile);
                int b;
                while ((b = is.read()) != -1) {
                    os.write(b);
                }
                os.close();
                is.close();
                // ???????????? inputStream
            } catch (Exception e) {
                log.debug(e.getMessage());
                System.err.println("??????????????????");
            }
        }
        String outstr = "";
        for (int i = 0; i < outSrc.length; i++) {
            outstr += outSrc[i] + "ue_separate_ue";
        }
        outstr = outstr.substring(0, outstr.lastIndexOf("ue_separate_ue"));
        response.getWriter().print("{'url':'" + outstr + "','tip':'" + state + "','srcUrl':'" + url + "'}");
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
