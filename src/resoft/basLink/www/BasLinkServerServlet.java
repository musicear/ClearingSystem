package resoft.basLink.www;

import javax.servlet.http.HttpServlet;

import resoft.basLink.BasLinkServer;

/**
 * function: 用于BasLinkServer在web环境下启动
 * User: albert lee
 * Date: 2005-9-30
 * Time: 23:17:46
 */
public class BasLinkServerServlet extends HttpServlet {
    /**
     * 用于Servlet中的自启动
     */
    public void init() {
        new BasLinkServer();

    }
}
