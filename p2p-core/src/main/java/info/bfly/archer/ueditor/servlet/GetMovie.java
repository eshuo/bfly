package info.bfly.archer.ueditor.servlet;

import info.bfly.core.annotations.Log;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

@Service
public class GetMovie extends HttpServlet {
    @Log
    Logger log;
    private static final long serialVersionUID = 7414184399324410866L;

    /**
     * Constructor of the object.
     */
    public GetMovie() {
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
        StringBuffer readOneLineBuff = new StringBuffer();
        String content = "";
        String searchkey = request.getParameter("searchKey");
        String videotype = request.getParameter("videoType");
        try {
            searchkey = URLEncoder.encode(searchkey, "utf-8");
            URL url = new URL("http://api.tudou.com/v3/gw?method=item.search&appKey=myKey&format=json&kw=" + searchkey + "&pageNo=1&pageSize=20&channelId=" + videotype + "&inDays=7&media=v&sort=s");
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                readOneLineBuff.append(line);
            }
            content = readOneLineBuff.toString();
            reader.close();
        } catch (MalformedURLException e) {
            log.debug(e.getMessage());
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        response.getWriter().print(content);
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
