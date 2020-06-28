package resoft.tips.action;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Message;

/**
 * <p> * ����������˰���ݿ� * </p>
 * User: liguoyin 
 * Date: 2007-4-24 
 * Time: 22:43:08
 * 
 * UpdateTime:2007-09-3 22:42:00 by zhuchangwu
 * UpdateDetail:���˰Ʊ��ϸ����
 */
public class SaveDB3102 extends AbstractSyncAction {
    private static final Log logger = LogFactory.getLog(SaveDB3102.class);

    public int execute(Message msg) throws Exception {
        try{
        	
        	return processSave3102(msg);
        	
        }catch(Exception e){
        	e.printStackTrace();
        	
        	msg.set("//CFX/MSG/MsgReturn9121/Result","92004");
            msg.set("//CFX/MSG/MsgReturn9121/AddWord","�������Ĵ�");
        	
            return FAIL;
        }
        
    }
    
    public int processSave3102(Message msg) throws Exception{
    	
    	String filePath = msg.getString("�����ļ�");
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filePath), "UTF-8");
        String localName = "";
        Map params = new HashMap();
        //noinspection MismatchedQueryAndUpdateOfCollection
        List packTags = new ArrayList() {
            {
                add("TaxOrgCode");
                add("EntrustDate");
                add("PackNo");
                add("ReturnTerm");
                add("AllNum");
                add("AllAmt");
                add("HandleType");
                add("PayeeBankNo");
                add("PayeeOrgCode");
                add("PayeeAcct");
                add("PayeeName");
                add("PayBkCode");
            }
        };
        //noinspection MismatchedQueryAndUpdateOfCollection
        List detailTags = new ArrayList() {
            {
                add("TraNo");
                add("PayOpBkCode");
                add("PayAcct");
                add("HandOrgName");
                add("TraAmt");
                add("TaxVouNo");
                add("TaxPayName");
                add("ProtocolNo");
                add("Remark");
                add("Remark1");
                add("Remark2");
            }
        };
        
        List taxTypeTags =new ArrayList(){
        	{
        		add("ProjectId");
        		add("TaxTypeName");
        		add("TaxStartDate");
        		add("TaxEndDate");
        		add("TaxTypeAmt");
        	}
        };
        //yangyuanxu update
        String taxOrgCode = "",entrustDate = "",packNo = "",traNo="",WorkDate="",msgRef="",payeeBankNo="";
        int serialNo = 0;
        boolean taxVouBegin = false;
        for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    localName = reader.getLocalName();
                    //��ʼ˰Ʊ��Ϣ����
//                    if (localName.equals("ProtocolNo") && !taxVouBegin) {
//                        serialNo = 0;
//                        taxVouBegin = true;
//                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    String text = reader.getText().trim();
                    if (!text.equals("")) {
                        if (packTags.contains(localName)) {
                            params.put(localName, text);
                        }
                        if (localName.equals("TaxOrgCode")) {
                            taxOrgCode = text;
                        } else if (localName.equals("EntrustDate")) {
                            entrustDate = text;
                        } else if (localName.equals("PackNo")) {
                            packNo = text;
                        } else if (localName.equals("TraNo")){
                        	traNo = text;
                        }
                        //yangyuanxu add
                        else if (localName.equals("PayeeBankNo")){
                        	payeeBankNo = text;
                        }
                        if(localName.equals("WorkDate")){
                        	WorkDate=text;
                        }
                        if(localName.equals("MsgRef")){
                        	msgRef=text;
                        }
                        //������ϸ����
                        if (detailTags.contains(localName)) {
                            params.put(localName, text);
                        }
                        //����˰Ʊ��ϸ����
                        if(taxTypeTags.contains(localName)){
                        	params.put(localName, text);
                        }
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    localName = reader.getLocalName();
                    if (localName.equals("TurnAccount3102")) {
                        //��������Ϣ����
                        //��д��ִ��Ϣ
                        msg.set("//CFX/MSG/MsgReturn9121/OriMsgNo", "3102");
                        msg.set("//CFX/MSG/MsgReturn9121/OriSendOrgCode", taxOrgCode);
                        msg.set("//CFX/MSG/MsgReturn9121/OriEntrustDate", entrustDate);
                        msg.set("//CFX/MSG/MsgReturn9121/OriRequestNo", packNo);
                        //�����Ƿ��ѳ�����ִ����
                        int returnTerm = Integer.parseInt((String) params.get("ReturnTerm"));
                        DateFormat df = new SimpleDateFormat("yyyyMMdd");
                        Calendar c = Calendar.getInstance();
                        c.setTime(df.parse(WorkDate));
                        c.add(Calendar.DATE, returnTerm);
                        Date returnDate = c.getTime();
                        
                        //��ǩ��У�����ʱ������SUCCESS���ɺ�����CheckSign action��װ9121����
                        //Boolean checkSignResult = (Boolean) msg.get("��Ѻ���");
                        //if(!checkSignResult.booleanValue()) {
                        	//return SUCCESS;  
                        //}
                        
                        /*
                        if (returnDate.compareTo(new Date()) < 0) {
                            //�ѳ�����ִ����
                            //@todo �˴�������Ӧ�Ƕ��٣�
                            msg.set(getResultNodePath(), "24020");
                            msg.set(getAddWordNodePath(), "������");
                            return FAIL;
                        }*/
                        //�����Ƿ�����ԭ����Ϣ
                        //����Ϣ���Ѵ��ڣ���ע��<������>Ϊ����ɹ�����ʾ���������Ѿ��������
                        //yangyuanxu update
                        int cnt = DBUtil.queryForInt("select count(*) from BatchPackage " +
                                " where taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and payeeBankNo='" + payeeBankNo + "'");
                        if (cnt > 0) {
                            //���Ѵ���
                            msg.set(getResultNodePath(), "90000");
                            msg.set(getAddWordNodePath(), "����ɹ�");
                            return FAIL;
                        }
                        DateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        params.put("procFlag","0");//����״̬Ϊ�������С�
                        params.put("recvTime", dtFormat.format(new Date()));
                        logger.info("recvTime        "+dtFormat.format(new Date()));
                        //����ת��
                        params.put("AllNum",new Integer((String) params.get("AllNum")));
                        params.put("AllAmt",new Double((String) params.get("AllAmt")));
                        params.put("ReturnTerm",new Integer((String) params.get("ReturnTerm"))); 
                        params.put("msgRef", msgRef);
                        DBUtil.insert("BatchPackage", params);
                        //yangyuanxu update
                        logger.info("����������ɹ�.packNo=" + packNo + ";entrustDate=" + entrustDate + ";taxOrgCode=" + taxOrgCode + ";payeeBankNo="+payeeBankNo);
                        params.clear();
                    }
                    if (localName.equals("Payment3102")) {//Payment3102
                        //����ϸ��Ϣ�ĸ�����Ϣ���ֽ���
                    	Map params3102 = new HashMap();
                    	params3102.put("TaxOrgCode", taxOrgCode);
                    	params3102.put("EntrustDate", entrustDate);
                    	params3102.put("PackNo", packNo);
                    	//yangyuanxu add
                    	params3102.put("PayeeBankNo", payeeBankNo);
                    	params3102.put("TraNo", (String)params.get("TraNo"));
                    	params3102.put("TipsWorkDate", WorkDate);
                    	params3102.put("PayOpBkCode", (String)params.get("PayOpBkCode"));
                    	params3102.put("PayAcct", (String)params.get("PayAcct"));
                    	params3102.put("HandOrgName", (String)params.get("HandOrgName"));
                        serialNo ++;
                        //����ת��                        
                        params3102.put("TaxVouNo",(String) params.get("TaxVouNo"));
                        params3102.put("TaxPayName",(String) params.get("TaxPayName"));  
                        logger.info("                    taxpayname        "+(String) params.get("TaxPayName"));
                        params3102.put("Remark",(String) params.get("Remark"));
                        params3102.put("Remark1",(String) params.get("Remark1"));
                        params3102.put("Remark2",(String) params.get("Remark2")); 
                        params3102.put("TraAmt",new Double((String) params.get("TraAmt")==null?"0":(String) params.get("TraAmt")));
                        params3102.put("SerialNo", new Integer(serialNo));
                        params3102.put("ProtocolNo", (String)params.get("ProtocolNo"));
                        params3102.put("ChkDate",WorkDate);
                        DBUtil.insert("BatchPackDetail", params3102);
                        params.clear();
                    }                    
                    if (localName.equals("TaxTypeList3102")) {
                    	Map paramsType3102 = new HashMap();
                    	//˰Ʊ��ϸ��Ϣ���ֽ���
                    	paramsType3102.put("TaxOrgCode", taxOrgCode);
                    	paramsType3102.put("EntrustDate", entrustDate);
                    	paramsType3102.put("PackNo", packNo);
                    	paramsType3102.put("TraNo", traNo);
                    	//yangyuanxu add
                    	paramsType3102.put("PayeeBankNo", payeeBankNo);
                    	paramsType3102.put("bizCode", "2");
                        //����ת��
                    	paramsType3102.put("ProjectId",new Integer((String) params.get("ProjectId")));
                    	paramsType3102.put("TaxTypeAmt",new Double((String) params.get("TaxTypeAmt")));
                    	paramsType3102.put("TaxTypeName",(String) params.get("TaxTypeName"));
                    	paramsType3102.put("TaxStartDate",(String) params.get("TaxStartDate"));
                    	paramsType3102.put("TaxEndDate",(String) params.get("TaxEndDate"));
                        DBUtil.insert("VoucherTaxType",paramsType3102);
                        params.remove("ProjectId");
                        params.remove("TaxTypeAmt");
                        params.remove("TaxTypeName");
                        params.remove("TaxStartDate");
                        params.remove("TaxEndDate");
                        //params.clear();
                    }
                                        
                    if(localName.equals("MSG")) {
                        //ȫ����ϸ������ϣ���������������״̬Ϊ��������ϡ�
                        //yangyuanxu update
                    	logger.info("Packet is processed");
                    	String sql = "update BatchPackage set procFlag='1' " +
                                " where taxOrgCode='" + taxOrgCode + "' and entrustDate='" + entrustDate + "' and packNo='" + packNo + "' and payeeBankNo='" + payeeBankNo + "'";
                    	logger.info("sql");
                    	DBUtil.executeUpdate(sql);
                    }
                    break;
            }
        }
        
        msg.set("//CFX/MSG/MsgReturn9121/Result","90000");
        msg.set("//CFX/MSG/MsgReturn9121/AddWord","���׳ɹ�");
        
        return SUCCESS;
    }
    
    
}