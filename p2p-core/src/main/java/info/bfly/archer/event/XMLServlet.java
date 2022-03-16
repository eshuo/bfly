package info.bfly.archer.event;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;
import info.bfly.archer.node.model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class XMLServlet extends HttpServlet {
    private static final Logger log              = LoggerFactory.getLogger(XMLServlet.class);
    /**
     *
     */
    private static final long   serialVersionUID = 7969759709932200354L;
    @Resource
    public InfoPush infoPush;

    /**
     * Constructor of the object.
     */
    public XMLServlet() {
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
        response.setCharacterEncoding("utf-8");
        List<Node> items = infoPush.nodeList.getPingInfo();
        Channel ch = null;
        try {
            ch = infoPush.createXML(items);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        WireFeedOutput out1 = new WireFeedOutput();
        try {
            out.println(out1.outputString(ch));
        } catch (IllegalArgumentException | FeedException e) {
            log.debug(e.getMessage());
        } finally {
            out.flush();
            out.close();
        }

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
        doGet(request, response);
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
