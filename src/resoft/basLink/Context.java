package resoft.basLink;

import java.util.HashMap;
import java.util.Map;

/**
 * function: ͨѶ�����Ļ���
 * User: albert lee
 * Date: 2005-10-28
 * Time: 14:54:26
 */
public class Context {
    private static final String Property_Protocol = "protocol";
    private static final String Property_Packer = "paker";
    private static final String Property_RequestID = "requestId";
    /**
     * �õ�ͨѶЭ��
     * */
    public Protocol getProtocol() {
        return (Protocol) getProperty(Property_Protocol);
    }
    /**
     * ����ͨѶЭ��
     * */
    public void setProtocol(Protocol protocal) {
        setProperty(Property_Protocol,protocal);
    }
    /**
     * �õ������
     * */
    public Packer getPacker() {
        return (Packer) getProperty(Property_Packer);
    }
    /**
     * ���ô����
     * */
    public void setPacker(Packer packer) {
        setProperty(Property_Packer,packer);
    }
    /**
     * �õ�����ID
     * */
    public String getRequestId() {
        return (String) getProperty(Property_RequestID);
    }
    /**
     * ��������ID
     * */
    public void setRequestId(String id) {
        setProperty(Property_RequestID,id);
    }
    /**
     * �õ�����
     * */
    public Object getProperty(String name) {
        return properties.get(name);
    }
    /**
     * ��������
     * */
    public void setProperty(String name,Object value) {
        properties.put(name,value);
    }

    private Map properties = new HashMap();
}
