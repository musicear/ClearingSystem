package junit.core;

import java.util.ArrayList;
import java.util.Collection;

import junit.core.mock.Person;
import junit.framework.TestCase;
import resoft.xlink.core.Message;
import resoft.xlink.core.MessageUtil;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p></p>
 * User: liguoyin
 * Date: 2007-4-11
 * Time: 23:27:23
 */
public class TestMessageUtil extends TestCase {
    public void testSimpleToObject() {
        Message msg = new DefaultMessage();
        msg.set("name","Albert Li");
        msg.set("age","29");
        Person p = new Person();
        MessageUtil.message2Object(msg,p);
        assertEquals("Albert Li",p.getName());
        assertEquals(29,p.getAge());
    }
    public void testNastedToObject() {
        Message msg = new DefaultMessage();
        msg.set("name","Albert Li");
        msg.set("age","29");
        Message msgChild = new DefaultMessage();
        msgChild.set("name","Carol Li");
        msgChild.set("age","1");
        Collection children = new ArrayList();
        children.add(msgChild);
        msg.set("children",children);
        Person p = new Person();
        MessageUtil.message2Object(msg,p);
        assertEquals("Albert Li",p.getName());
        assertEquals(29,p.getAge());
        assertEquals(1,p.getChildren().size());
//        Person carol = (Person) p.getChildren().iterator().next();
//        assertEquals("Carol Li",carol.getName());
//        assertEquals(1,carol.getAge());
    }
    public void testObjectToMessage() {
        Person p = new Person();
        p.setName("dbo");
        p.setAge(29);
        Message msg = new DefaultMessage();
        MessageUtil.object2Message(p,msg);
//        assertEquals("dbo",msg.get("name"));
//        assertEquals("29",msg.get("age"));
    }
    

}
