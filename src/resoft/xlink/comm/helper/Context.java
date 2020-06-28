package resoft.xlink.comm.helper;

import java.util.HashMap;
import java.util.Map;

import resoft.xlink.comm.Packager;
import resoft.xlink.comm.Protocol;

/**
 * <p>通讯上下文环境</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 4:51:08
 */
public class Context {
    private static final String Property_Protocol = "protocol";
    private static final String Property_Packer = "paker";
    private static final String Property_RequestID = "requestId";

    /**
     * 得到通讯协议
     */
    public Protocol getProtocol() {
        return (Protocol) getProperty(Property_Protocol);
    }

    /**
     * 设置通讯协议
     */
    public void setProtocol(Protocol protocal) {
        setProperty(Property_Protocol, protocal);
    }

    /**
     * 得到打包器
     */
    public Packager getPacker() {
        return (Packager) getProperty(Property_Packer);
    }

    /**
     * 设置打包器
     */
    public void setPacker(Packager packager) {
        setProperty(Property_Packer, packager);
    }

    /**
     * 得到请求ID
     */
    public String getRequestId() {
        return (String) getProperty(Property_RequestID);
    }

    /**
     * 设置请求ID
     */
    public void setRequestId(String id) {
        setProperty(Property_RequestID, id);
    }

    /**
     * 得到属性
     */
    public Object getProperty(String name) {
        return properties.get(name);
    }

    /**
     * 设置属性
     */
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    private Map properties = new HashMap();
}
