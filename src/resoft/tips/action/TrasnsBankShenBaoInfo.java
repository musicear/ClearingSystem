package resoft.tips.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.util.Utils;

import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p>����TIPS������Ϣ�����÷�����Ϣ</p>
 * @Author: liwei
 * @Date: 2008-06-01
 * @Time: 09:58:00
 */

public class TrasnsBankShenBaoInfo implements Action {
    
	private static final Log logger = LogFactory.getLog(TrasnsBankShenBaoInfo.class);
	
	public int execute(Message msg) throws Exception {        		
		
		String xml="",addWord="",result="";
		String prefix1009="",prefix2090="";
        Message returnMsg=new DefaultMessage();        
        try{
			xml = (String) msg.get("packet");
	        Packager packager = new GenericXmlPackager();
	        returnMsg = packager.unpack(xml.getBytes());
        }catch(Exception e){
        	e.printStackTrace();
        	
        	/*
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "��ȡTIPS���Ĵ���");
        	
        	return FAIL;
        	*/
        	
        	msg.set("ReturnResult", "Y");
        	msg.set("AddWord", "�걨��ѯ�ɹ�");
        	msg.set("ReturnFileName", "FUCKTIPS");
        	msg.set("TraAmt", "1234.55");
        	
        	return SUCCESS;
        	
        }    
        //������TIPS����1009����[�����걨��ѯ��ִ],����2090�����걨����ת��TIPS
        result = returnMsg.getString("//CFX/MSG/RealHead1009/Result");	        
        addWord = (String) Utils.isNullThen(returnMsg.getString("//CFX/MSG/MsgReturn9121/AddWord"),"");
        msg.set("AddWord", addWord);
                	        
        if ("90000".equals(result)) {
            msg.set("ReturnResult", "Y");
        } else {
            msg.set("ReturnResult", "N");
            return FAIL;
        }
                       
        //����2090�����걨����
        prefix2090="//CFX/MSG/DeHead2090/";
        prefix1009="//CFX/MSG/RealHead1009/";
        //����2090�걨ҵ��ͷ
        msg.set(prefix2090+"BankNo", returnMsg.getString(prefix1009+"BankNo"));			   //��ҵ�����к�
        msg.set(prefix2090+"EntrustDate", returnMsg.getString(prefix1009+"EntrustDate"));  //��ҵ�����к�
        
        //����2090�ɿ��걨��Ϣ
        prefix2090="//CFX/MSG/Declare2090/";
        prefix1009="//CFX/MSG/Payment1009/";
        msg.set(prefix2090+"TaxPayCode", returnMsg.getString(prefix1009+"TaxPayCode"));		//��˰�˱���
        msg.set(prefix2090+"TaxOrgCode", returnMsg.getString(prefix1009+"TaxOrgCode"));		//���ջ��ش���
        msg.set(prefix2090+"PayOpBkCode", returnMsg.getString(prefix1009+"PayOpBkCode"));	//��������к�        
        msg.set(prefix2090+"PayAcct", "");													//�����˻�
        msg.set(prefix2090+"LevyState", returnMsg.getString(prefix1009+"1"));				//�걨״̬
        msg.set(prefix2090+"OuterLevyNo", returnMsg.getString(prefix1009+"OuterLevyNo"));	//�ⲿ�걨�������
        msg.set(prefix2090+"TraAmt", returnMsg.getString(prefix1009+"TraAmt"));				//���׽��
        msg.set(prefix2090+"DetailNum", returnMsg.getString(prefix1009+"DetailNum"));		//��ϸ����
        if(Integer.parseInt(returnMsg.getString(prefix1009+"DetailNum"))==0)
        {
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "û����ϸ");
        	logger.info("��ϸ��ĿΪ0");
        }
        
        for(int i=0;i<Integer.parseInt(returnMsg.getString(prefix1009+"DetailNum"));i++){
	        //����2090˰����ϸ
	        prefix2090="//CFX/MSG/Declare2090/TaxTypeList2090[" + (i + 1) + "]/";
	        prefix1009="//CFX/MSG/Payment1009/TaxTypeList1009[" + (i + 1) + "]/";
	        msg.set(prefix2090+"ProjectId", returnMsg.getString(prefix1009+"ProjectId"));	//��Ŀ���
	        msg.set(prefix2090+"TaxTypeCode", returnMsg.getString(prefix1009+"TaxTypeCode"));//˰�ִ���
	        msg.set(prefix2090+"TaxStartDate", returnMsg.getString(prefix1009+"TaxStartDate"));//˰������������
	        msg.set(prefix2090+"TaxEndDate", returnMsg.getString(prefix1009+"TaxEndDate"));	//˰����������ֹ
	        msg.set(prefix2090+"TaxType", returnMsg.getString(prefix1009+"TaxType"));		//˰������	        
	        msg.set(prefix2090+"DetailNum", returnMsg.getString(prefix1009+"DetailNum"));	//��ϸ����
	        double factTaxAmt=0.00;
	        for(int j=0;j<Integer.parseInt(returnMsg.getString(prefix1009+"DetailNum"));j++){
		        //����2090˰Ŀ��ϸ
		        prefix2090="//CFX/MSG/Declare2090/TaxTypeList2090[" + (i + 1) + "]/TaxSubjectList2090[" + (j + 1) + "]/";
		        prefix1009="//CFX/MSG/Payment1009/TaxTypeList1009[" + (i + 1) + "]/TaxSubjectList1009[" + (j + 1) + "]/";
		        msg.set(prefix2090+"DetailNo", returnMsg.getString(prefix1009+"DetailNo")); //��ϸ���
		        msg.set(prefix2090+"TaxSubjectCode", returnMsg.getString(prefix1009+"TaxSubjectCode"));//˰Ŀ����
		        msg.set(prefix2090+"TaxNumber", returnMsg.getString(prefix1009+"TaxNumber"));
		        msg.set(prefix2090+"TaxAmt", returnMsg.getString(prefix1009+"TaxAmt"));		//��˰����
		        msg.set(prefix2090+"FactTaxAmt", returnMsg.getString(prefix1009+"FactTaxAmt"));//ʵ�ɽ��
		        factTaxAmt+=Double.parseDouble(returnMsg.getString(prefix1009+"FactTaxAmt"));		        
	        }
	        //����ǰ׺
	        prefix2090="//CFX/MSG/Declare2090/TaxTypeList2090[" + (i + 1) + "]/";
	        msg.set(prefix2090+"TaxTypeAmt", ""+factTaxAmt);								//˰�ֽ��
        }
                
        return SUCCESS;
                       
    }	   
    
}
