package junit.core;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import resoft.xlink.core.Action;
import resoft.xlink.core.ActionManager;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultActionManager;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p>≤‚ ‘ªÓ∂Ø</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 3:04:42
 */
public class TestDefaultActionManager extends TestCase {
    private ActionManager actionManager = null;
    public void setUp() {
        actionManager = new DefaultActionManager();
    }
    public void testCreateAction() throws Exception {
        Map properties = new HashMap();
        properties.put("name","Albert Li");
        Action action = actionManager.createAction("junit.core.mock.MockAction",properties);
        assertEquals(Action.SUCCESS,action.execute(new DefaultMessage()));
        properties.put("name","Not Albert Li");
        properties.put("age","29");
        action = actionManager.createAction("junit.core.mock.MockAction",properties);
        Message msg = new DefaultMessage();
        assertEquals(Action.FAIL,action.execute(msg));
        //assertEquals("29",msg.get("age"));
    }
    
}
