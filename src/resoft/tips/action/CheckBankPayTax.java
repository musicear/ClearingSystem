package resoft.tips.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

/**
 * <p>����TIPS������������δ������</p>
 * Author: zhuchangwu
 * Date: 2007-9-1
 * Time: 9:49:33
 */
public class CheckBankPayTax implements Action {

    public int execute(Message msg) throws Exception {
        String filePath = msg.getString("�����ļ�");
        BatchXmlProcessor processor = new BatchXmlProcessor(filePath);
        processor.setHandler(new BankTaxCheckHandler(msg));
        processor.execute();
        return SUCCESS;
    }
}

class BankTaxCheckHandler implements BatchXmlHandler {
//    private static final Log logger = LogFactory.getLog(TaxCheckHandler.class);

    BankTaxCheckHandler(Message msg) {
        this.msg = msg;
    }

    public Collection getTags() {
        String[] tags = {"BatchHead3113","BankCompare3113"};
        return Arrays.asList(tags);
    }

    public void process(String tag,Map children) throws Exception {
        if(tag.equals("BatchHead3113")) {
            processSum(children);
        } else if(tag.equals("BankCompare3113")) {
            processDetail(children);
            msg.set("compareList", list);
        }
    }
    /**
     * ������ܲ���
     * */
    public void processSum(Map sumMap) throws Exception {
    	msg.set("PayBkCode",sumMap.get("PayBkCode"));
    	msg.set("ChkDate",sumMap.get("ChkDate"));
    	msg.set("PackNo",sumMap.get("PackNo"));
    	msg.set("AllNum",sumMap.get("AllNum"));
    	msg.set("AllAmt",sumMap.get("AllAmt"));
    	msg.set("ChildPackNum",sumMap.get("ChildPackNum"));
    	msg.set("CurPackNo",sumMap.get("CurPackNo"));
    	msg.set("CurPackNum",sumMap.get("CurPackNum"));
    	msg.set("CurPackAmt",sumMap.get("CurPackAmt"));
    }
    /**
     * ������ϸ����
     * */
    public void processDetail(Map detailMap) throws Exception {
        //���������ϸ��
        Message subMsg=new DefaultMessage();
        subMsg.set("PayOpBkCode", detailMap.get("PayOpBkCode"));
        subMsg.set("OriTaxOrgCode", detailMap.get("OriTaxOrgCode"));
        subMsg.set("OriEntrustDate", detailMap.get("OriEntrustDate"));
        subMsg.set("OriTraNo", detailMap.get("OriTraNo"));
        subMsg.set("TaxVouNo", detailMap.get("TaxVouNo"));
        subMsg.set("TraAmt", detailMap.get("TraAmt"));
        list.add(subMsg);

    }

    public void end() {

    }
//    private int sumNum=0;
    private Message msg; 
    private Collection list=new ArrayList();
}

