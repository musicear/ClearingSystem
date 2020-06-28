package resoft.basLink;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.XLinkServer;

/**
 * <p>BasLink∆Ù∂Ø≥Ã–Ú</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 21:54:39
 */
public class BasLinkServer {
    private static Log logger = null;
    static {
        //log
        System.setProperty(LogFactory.FACTORY_PROPERTY, "resoft.basLink.log.BasLinkLogFactory");
        logger = LogFactory.getLog(BasLinkServer.class);
    }

    public BasLinkServer() {
        XLinkServer xlinkServer;
        try {
            xlinkServer = new XLinkServer("resoft.basLink.BasServiceFinder");
            xlinkServer.startup();
        } catch (Exception e) {
            logger.error("BasLinkServer∆Ù∂Ø ß∞‹",e);
        }

    }

    public static void main(String[] args) throws Exception {
        new BasLinkServer();

    }
}
