package resoft.basLink.packer;

import java.util.ArrayList;
import java.util.List;

import resoft.basLink.Configuration;
import resoft.basLink.Message;
import resoft.basLink.PackException;
import resoft.basLink.Packer;
import resoft.junit.StringTokenizer;

/**
 * function: 青岛国税前置用到的报文格式
 * User: albert lee
 * Date: 2005-9-14
 * Time: 14:35:55
 */
public class TagPacker implements Packer{
    public Message pack(byte[] m) throws PackException {
        String str = new String(m);
        List values = new ArrayList();
        StringTokenizer st = new StringTokenizer(str,"@-@");
        while(st.hasMoreTokens()) {
            values.add(st.nextToken());
        }
        //根据交易码得到响应报文定义
        String transCode = (String) values.get(0);
        String packDefine = Configuration.getInstance().getProperty("tagDefine",transCode);
        st = new StringTokenizer(packDefine,",");
        List tags = new ArrayList();
        while(st.hasMoreTokens()) {
            tags.add(st.nextToken());
        }
        Message msg = new Message();
        //对应Tag及其响应值
        for(int i=0;i<tags.size();i++) {
            String tag = (String) tags.get(i);
            if(i>=values.size()) {
                break;
            }
            String value = (String) values.get(i);
            msg.setValue(tag,value);
        }

        return msg;
    }

    public byte[] unpack(Message msg) {
        return new byte[0];
    }

    public byte[] unpackByFile(Message msg) throws PackException {
        StringBuffer str = new StringBuffer();
        str.append(msg.getValue("响应码")).append(msg.getValue("响应描述")).append("\r\n");
        return str.toString().getBytes();
    }

    public byte[] unpackByFile(Message msg, String filePath) throws PackException {
        return new byte[0];
    }

    public Message parse(Message msg, String filePath) throws PackException {
        return null;
    }
}
