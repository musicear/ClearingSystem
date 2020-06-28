package junit.ruby;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import resoft.xlink.core.Action;
import resoft.xlink.core.ActionManager;
import resoft.xlink.ruby.RubyActionManager;

/**
 * <p>≤‚ ‘RubyActionManager</p>
 * User: liguoyin
 * Date: 2007-4-24
 * Time: 15:09:36
 */
public class TestRubyActionManager extends TestCase {
    private ActionManager actionManager = null;
    public void setUp() {
        actionManager = new RubyActionManager();
    }
    public void testCreateAction() throws Exception{
        Map properties = new HashMap();
        properties.put("name","Albert Li");
        Action action = actionManager.createAction("mock.rb",properties);
////        assertEquals(Action.SUCCESS,action.execute(new DefaultMessage()));
////        properties.put("name","Not Albert Li");
////        properties.put("age","29");
////        action = actionManager.createAction("mock.rb",properties);
////        Message msg = new DefaultMessage();
////        assertEquals(Action.FAIL,action.execute(msg));
//        assertEquals("29",msg.get("age"));
    }
}
