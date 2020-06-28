package junit;

import java.sql.Connection;

import junit.framework.TestCase;
import resoft.basLink.util.DBUtil;
import resoft.tips.action.UpdateCommonInfo;

/**
 * <p></p>
 * Author: liguoyin
 * Date: 2007-6-15
 * Time: 1:20:26
 */
public class TestUpdateCommonInfo extends TestCase {
    public void test() throws Exception {
        Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);
        new UpdateCommonInfo().execute(null);
    }
}
