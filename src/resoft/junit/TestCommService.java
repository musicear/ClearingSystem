package resoft.junit;

import resoft.basLink.Service;
import resoft.basLink.ServiceRunner;
import resoft.basLink.service.CommService;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-9-22
 * Time: 17:33:43
 * To change this template use File | Settings | File Templates.
 */
public class TestCommService  {
    public static void main(String[] args) {
        Service service = new CommService();
        new ServiceRunner("commService",service);
    }
}
