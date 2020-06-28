package resoft.tips.chqsh;

import org.zerone.util.Utils;

import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>�������Э�鷵�ؽ��</p>
 * Author: liguoyin
 * Date: 2007-8-8
 * Time: 15:55:07
 */
public class CheckProveResult implements Action {
    public int execute(Message msg) throws Exception {
        String xml = (String) msg.get("packet");
        Packager packager = new GenericXmlPackager();
        Message returnMsg = packager.unpack(xml.getBytes());
        msg.set("AddWord", Utils.isNullThen(returnMsg.getString("//CFX/MSG/MsgReturn9120/AddWord"),
                returnMsg.getString("//CFX/MSG/ProveReturn9115/AddWord")));
        String result = returnMsg.getString("//CFX/MSG/MsgReturn9120/Result");
        if (result != null && !result.equals("")) {
            msg.set("ReturnResult", "N");
            return FAIL;
        }
        String vcResult = returnMsg.getString("//CFX/MSG/ProveReturn9115/VCResult");
        msg.set("verifyResult", vcResult);
        int flag=0;
        if ("0".equals(vcResult)) {
            //��֤�ɹ����������ݿ�
            msg.set("ReturnResult", "Y");
            msg.set("AddWord", "Э���ѿ���ʹ��");
            msg.set("verifyResult", "0");
            //msg.set("EnabledFlag", "Y");
            //return SUCCESS;=
            flag=0;
        }
        if("1".equals(vcResult)){
            msg.set("ReturnResult", "N");
            msg.set("AddWord", "��֤ʧ�ܣ�Э�鲻���ڻ���Ϣ����");
            msg.set("verifyResult", "1");
            //msg.set("EnabledFlag", "N");
            //return FAIL;	
            flag=-1;
        }
        if("2".equals(vcResult)){
            msg.set("ReturnResult", "Y");
            msg.set("AddWord", "����ͨ����Э���Ѳ���ʹ��");
            msg.set("verifyResult", "1");
            msg.set("EnabledFlag", "N");
            //return SUCCESS;	
            flag=0;
        }
        if("3".equals(vcResult)){
            msg.set("ReturnResult", "N");
            msg.set("AddWord", "����ʧ�ܣ�Э�鲻���ڻ���Ϣ����");
            msg.set("verifyResult", "0");
            msg.set("EnabledFlag", "Y");
            //return FAIL;	
            flag=-1;
        }
        return flag;
    }
}
