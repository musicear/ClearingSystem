package resoft.basLink;

import java.util.HashMap;
import java.util.Map;

/**
 * function: 通讯上下文环境
 * User: albert lee
 * Date: 2005-10-28
 * Time: 14:54:26
 */
public class Context {
    private static final String Property_Protocol = "protocol";
    private static final String Property_Packer = "paker";
    private static final String Property_RequestID = "requestId";
    /**
     * 得到通讯协议
     * */
    public Protocol getProtocol() {
        return (Protocol) getProperty(Property_Protocol);
    }
    /**
     * 设置通讯协议
     * */
    public void setProtocol(Protocol protocal) {
        setProperty(Property_Protocol,protocal);
    }
    /**
     * 得到打包器
     * */
    public Packer getPacker() {
        return (Packer) getProperty(Property_Packer);
    }
    /**
     * 设置打包器
     * */
    public void setPacker(Packer packer) {
        setProperty(Property_Packer,packer);
    }
    /**
     * 得到请求ID
     * */
    public String getRequestId() {
        return (String) getProperty(Property_RequestID);
    }
    /**
     * 设置请求ID
     * */
    public void setRequestId(String id) {
        setProperty(Property_RequestID,id);
    }
    /**
     * 得到属性
     * */
    public Object getProperty(String name) {
        return properties.get(name);
    }
    /**
     * 设置属性
     * */
    public void setProperty(String name,Object value) {
        properties.put(name,value);
    }

    private Map properties = new HashMap();
}
