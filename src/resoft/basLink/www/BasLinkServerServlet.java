package resoft.basLink.www;

import javax.servlet.http.HttpServlet;

import resoft.basLink.BasLinkServer;

/**
 * function: ����BasLinkServer��web����������
 * User: albert lee
 * Date: 2005-9-30
 * Time: 23:17:46
 */
public class BasLinkServerServlet extends HttpServlet {
    /**
     * ����Servlet�е�������
     */
    public void init() {
        new BasLinkServer();

    }
}
