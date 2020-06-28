package junit;

import resoft.xlink.XLinkServer;

/**
 * <p></p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 10:02:04
 */
public class TestBasLinkServer {
    public static void main(String[] args) throws Exception {
        XLinkServer bls = new XLinkServer("junit.comm.DickServiceFinder");
        bls.startup();
    }
}
