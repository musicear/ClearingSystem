package resoft.xlink.comm.helper;

import java.util.HashMap;
import java.util.Map;

import resoft.xlink.comm.Packager;
import resoft.xlink.comm.Protocol;

/**
 * <p>ͨѶ�����Ļ���</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 4:51:08
 */
public class Context {
    private static final String Property_Protocol = "protocol";
    private static final String Property_Packer = "paker";
    private static final String Property_RequestID = "requestId";

    /**
     * �õ�ͨѶЭ��
     */
    public Protocol getProtocol() {
        return (Protocol) getProperty(Property_Protocol);
    }

    /**
     * ����ͨѶЭ��
     */
    public void setProtocol(Protocol protocal) {
        setProperty(Property_Protocol, protocal);
    }

    /**
     * �õ������
     */
    public Packager getPacker() {
        return (Packager) getProperty(Property_Packer);
    }

    /**
     * ���ô����
     */
    public void setPacker(Packager packager) {
        setProperty(Property_Packer, packager);
    }

    /**
     * �õ�����ID
     */
    public String getRequestId() {
        return (String) getProperty(Property_RequestID);
    }

    /**
     * ��������ID
     */
    public void setRequestId(String id) {
        setProperty(Property_RequestID, id);
    }

    /**
     * �õ�����
     */
    public Object getProperty(String name) {
        return properties.get(name);
    }

    /**
     * ��������
     */
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    private Map properties = new HashMap();
}
