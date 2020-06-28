package resoft.tips.chqsh;


//import resoft.xlink.impl.DefaultMessage;
//import resoft.basLink.util.MessageSender;
//import resoft.basLink.util.MessageSendException;
//import resoft.tips.util.MessageSenderUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>�ʺ�״̬��ѯ</p>
 * Author: fanchengwei
 * �ʺŽṹ��ǰ4λ������|����2λ����|����7��8��Ӧ�˻�����|
 */
public class QueryAcct implements Action {
    
	private static final Log logger = LogFactory.getLog(QueryAcct.class);
    private static final SendMsgToBankSystem send = new SendMsgToBankSystem();
    
    public int execute(Message msg) throws Exception {
        
    	String acctNo = msg.getString("PayAcct").trim();
        String BranchNo = msg.getString("BranchNo").trim();    
        
        int result = 0;
        if(acctNo.length()<8){
        	msg.set("ReturnResult", "N");
            msg.set("AddWord", "�Ƿ����ʺţ�����С��8��");
            logger.info("�Ƿ����ʺţ�����С��8��");
            return FAIL;
        }
        String id = acctNo.substring(6,8);  
        logger.info("payAcct is:"+acctNo+",Id is:"+id);
                
        //������֤�Ƿ��Ǳ�������ʻ�
        try{
	        result=checkSzhh(msg);        
	        if ( result!=0 ) {
	        	return result;
	        }
        }catch(Exception e){
        	e.printStackTrace();
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "�˺Ŵ���");
        	return FAIL;
        }
                
        if(acctNo.length() == 16){
        	result = getCard(id, acctNo, msg, BranchNo);
        }else if(id.equals("04") && acctNo.length() == 15){
        	result = getCDM(id, acctNo, msg);
        }else if(id.equals("10") && acctNo.length() == 15){
        	result = getSDM(id, acctNo, msg);
        }else if(id.equals("01") && acctNo.length() == 15){
        	result = getIAC(id, acctNo, msg);
        }
        return result;
    }
    
    
    
    public static int checkSzhh(Message msg) throws Exception{
    	
    	SendMsgToBankSystem send = new SendMsgToBankSystem();
    	String rcvMsg = "";
    	String SQL = "";
    	
    	String payAcct = msg.getString("PayAcct").trim();
        String BranchNo = msg.getString("BranchNo").trim(); 
    	/**
    	 * ���˻��Ĵ���
    	 * ���ȣ���ѯMCA--���ʶ��ձ��ȡ�ʺźͻ��������ڿ����˻���һ�Զ����ʽ�������޶�ֻ��ר����Ϊ��99���Ŀ��ſ���ǩ������Э��
    	 * �ڶ�����ѯSDM---���ڷֻ��ʻ�ȡ�����к�
    	 * ���ͨ�������кŲ�ѯPS_BKD���ȡ�ʺŵĿ������кź͸�������кš�
    	 */
    	if(payAcct.length() == 16){
    		SQL = "select lpad(p.brtbr,12),'313653000013',s.SDMACNAM,lpad(p.brtbrnam,60) from MCA m,brt p,SDM s where m.MCAEPTCOD='99' and p.brtbr=s.SDMGACBK and m.MCAACTNO=s.SDMACTNO and m.MCACRDNO='"+ payAcct +"'";
    		logger.info("���˻��Ĵ��� ��ѯSQL is:"+SQL);
    		rcvMsg = send.sendMsg(SQL,"8801" );
    		logger.info("���˻��Ĵ�����̨TUXEDO������Ϣ:["+rcvMsg+"]");
    		int i = rcvMsg.indexOf(10);
    		if(i < 0) {
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "δ�ܲ�ѯ���ʺ���Ϣ��");
                logger.info("δ�ܲ�ѯ���ʺ���Ϣ��");
                return FAIL;
    		}
    		int j = rcvMsg.lastIndexOf(10);
    		try{
    			rcvMsg = rcvMsg.substring(i+1,j-1);
    		}catch(Exception e){
    			e.printStackTrace();
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "δ��ȡ���������кź͸�������кţ�");
                logger.info("δ��ȡ���������кź͸�������кţ�");
                return FAIL;
    		}
    	}
    	/**
    	 * ֧Ʊ�˻��Ĵ���
    	 *�ʺŵ� 7��8λ�� 04 ���ʺŲ�ѯ cdm ���ȡ�ɿλ���ơ�
    	 *��ѯps_bkd ���ȡ�������кź͸�������кš�
    	 */
    	if( payAcct.substring(6, 8).equals("04") && payAcct.length() == 15 ){
    		SQL = "select lpad(p.brtbr,12),'313653000013',c.cdmac_nam,lpad(p.brtbrnam,60) from cdm c,brt p where p.brtbr=c.cdmgacbk and c.cdmac_no='"+ payAcct +"'";
    		logger.info("֧Ʊ�˻��Ĵ��� ��ѯSQL is:"+SQL);
    		rcvMsg = send.sendMsg(SQL,"8801" );         //p.bkdagdbkno
    		logger.info("֧Ʊ�˻��Ĵ�����̨TUXEDO������Ϣ:["+rcvMsg+"]");
    		int i = rcvMsg.indexOf(10);
    		if(i < 0){
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "δ�ܲ�ѯ���ʺ���Ϣ��");
                logger.info("δ�ܲ�ѯ���ʺ���Ϣ��");
                return FAIL;
    		}
    		int j = rcvMsg.lastIndexOf(10);
    		try{
    			rcvMsg = rcvMsg.substring(i+1,j-1);
    		}catch(Exception e){
    			e.printStackTrace();
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "δ��ȡ���������кź͸�������кţ�");
                logger.info("δ��ȡ���������кź͸�������кţ�");
                return FAIL;
    		}
    	}
    	/**
    	 * ��������˻��Ĵ���
    	 * �ʺŵ�7��8λ�� 10 ���ʺŲ�ѯ sdm ���ȡ�ɿλ���ơ�
    	 * ��ѯps_bkd ���ȡ�������кź͸�������кš�
    	 */
    	if(payAcct.substring(6, 8).equals("10") && payAcct.length() == 15){
    		SQL = "select lpad(p.brtbr,12),'313653000013',s.SDMACNAM,lpad(p.brtbrnam,60) from sdm s,brt p where p.brtbr=s.SDMGACBK and s.SDMACTNO='"+ payAcct +"'";
    		logger.info("��������˻��Ĵ��� ��ѯSQL is:"+SQL);
    		rcvMsg = send.sendMsg(SQL,"8801" );
    		logger.info("��������˻��Ĵ�����̨TUXEDO������Ϣ:["+rcvMsg+"]");
    		int i = rcvMsg.indexOf(10);
    		if(i < 0){
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "δ�ܲ�ѯ���ʺ���Ϣ��");
                logger.info("δ�ܲ�ѯ���ʺ���Ϣ��");
                return FAIL;
    		}
    		int j = rcvMsg.lastIndexOf(10);
    		try{
    			rcvMsg = rcvMsg.substring(i+1,j-1);
    		}catch(Exception e){
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "��ǰ�����µĿ����ʺŲ����ڣ�");
                logger.info("δ��ȡ���������кź͸�������кţ�");
                return FAIL;
    		}
    	}
    	/**
    	 * �ڲ��˻��Ĵ���
    	 * �ʺŵ�7��8λ�� 01 ���ʺŲ�ѯ iac ���ȡ�ɿλ���ơ�
    	 * ��ѯps_bkd ���ȡ�������кź͸�������кš�
    	 */
    	if( payAcct.substring(6, 8).equals("01") && payAcct.length() == 15 ){
    		SQL = "select lpad(p.brtbr,12),'313653000013',i.IACAC_NAM,lpad(p.brtbrnam,60) from iac i,brt p where p.brtbr=i.IACGACBR and i.iacac_no='"+ payAcct +"'";
    		logger.info("�ڲ��˻��Ĵ��� ��ѯSQL is:"+SQL);
    		rcvMsg = send.sendMsg(SQL,"8801" );
    		logger.info("�ڲ��˻��Ĵ�����̨TUXEDO������Ϣ:["+rcvMsg+"]");
    		int i = rcvMsg.indexOf(10);
    		logger.info("@@@@@@@@:"+i);
    		if(i < 0){
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "δ�ܲ�ѯ���ʺ���Ϣ��");
                logger.info("δ�ܲ�ѯ���ʺ���Ϣ��");
                return FAIL;
    		}
    		int j = rcvMsg.lastIndexOf(10);
    		try{
    			rcvMsg = rcvMsg.substring(i+1,j-1);
    		}catch(Exception e){
    			e.printStackTrace();
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "δ��ȡ���������кź͸�������кţ�");
                logger.info("δ��ȡ���������кź͸�������кţ�");
                return FAIL;
    		}
    	}  
    	    	
    	String str = rcvMsg.substring(24);
    	String PayOpBkName = "�����в����ڣ�";
    	String handOrgName = "";
    	if(str.length()>0){
    		for(int k=0;k<str.length();k++){
    			if(str.substring(k, k+1).equals(" ")){
    				handOrgName = str.substring(0, k);
    				PayOpBkName = str.substring(k).trim();
    				break;
    			}
    		}
    	}
    	
    	//����������ǩ��Э��
    	if(!(rcvMsg.substring(0, 12)).trim().equals(BranchNo)){
    		msg.set("ReturnResult", "N");
            msg.set("AddWord", "�뵽������ǩ��Э�飡");
            return FAIL;
    	}
    	
    	msg.set("payOpBkCode", (rcvMsg.substring(0, 12)).trim());
    	msg.set("handOrgName", handOrgName);
    	msg.set("PayOpBkName", PayOpBkName);
    	
    	return SUCCESS;
    }
    
    
    
    /**
     * ���˻���ѯ��Ҫ�ֱ��ѯ����״̬�Ϳ���Ӧ���˺ŵ�״̬��
     * �ֱ��ѯMCA��SDM
     */
    public int getCard(String id,String acctNo,Message msg,String BranchNo)
    {
    	String SQL = "";
    	String rcvMsg = "NULL";
    	String status = "";
    	String PayAcctName = "";
    	int i = 0;
    	int j = 0;
    	int count = 0;
    	if(acctNo.length() == 16)
         {
         	SQL = "select s.sdmgacbk,m.MCAACTSTS,s.SDMACTSTS,s.SDMACNAM from MCA m,SDM s where m.MCAACTNO=s.SDMACTNO and m.MCAEPTCOD='99' and m.MCACRDNO='"+ acctNo +"'";
         	rcvMsg = send.sendMsg(SQL, "8801");
         	if(!rcvMsg.equals("NULL"))
         	{
         		i = rcvMsg.indexOf(10);
         		if(i < 0)
        		{
        			msg.set("AddWord", "δ�ܲ�ѯ���˻���Ϣ��");
                	msg.set("ReturnResult", "N");
                	return FAIL;
        		}
         		count = Integer.parseInt(rcvMsg.substring(i-1,i));
         		if(count > 0)
         		{
         			j = rcvMsg.lastIndexOf(10);
         			String str = rcvMsg.substring(i+1, i+5);
         			if(!str.equals(BranchNo))
         			{
         				msg.set("ReturnResult", "N");
         	            msg.set("AddWord", "���ʺ�δ�ڱ��п������뵽�����а���ҵ��");
         	            logger.info("���ʺ�δ�ڱ��п������뵽�����а���ҵ��");
         	            return FAIL;
         			}
         			String cardStatus = rcvMsg.substring(i+5, i+6);
         			status = rcvMsg.substring(i+6, i+16);
         			PayAcctName = rcvMsg.substring(i+16 , j-1).trim();
 	            	msg.set("AcctName", PayAcctName);
         			if("0".equals(cardStatus)
         					&&status.substring(0, 1).equals("1")&&status.substring(1, 2).equals("1")||status.substring(1, 2).equals("2")
                 			&&status.substring(2,3).equals("1")&&status.substring(3, 4).equals("1")||status.substring(3, 4).equals("3")
                 			&&status.subSequence(4, 5).equals("1")||status.substring(4, 5).equals("2")&&status.subSequence(5, 6).equals("1")||status.substring(5, 6).equals("2")
                 			&&status.subSequence(6, 7).equals("1")&&status.subSequence(7, 8).equals("1")
                 			&&status.subSequence(8, 9).equals("1")||status.subSequence(8, 9).equals("2")||status.subSequence(8, 9).equals("3"))
         			{
         				msg.set("AddWord", "�˻�״̬����");
 	                	msg.set("ReturnResult", "Y");
 	                	return SUCCESS;
         			}
         			if("1".equals(cardStatus) || "2".equals(cardStatus))
         			{
         				msg.set("AddWord", "�˻�����");
 	                	msg.set("ReturnResult", "N");
 	                	return FAIL;
         			}
         			if("3".equals(cardStatus) || "4".equals(cardStatus))
         			{
         				msg.set("AddWord", "�˻�ֹ��");
 	                	msg.set("ReturnResult", "N");
 	                	return FAIL;
         			}
         			if("5".equals(cardStatus) || "6".equals(cardStatus))
         			{
         				msg.set("AddWord", "�˻���������Ĩ��");
 	                	msg.set("ReturnResult", "N");
 	                	return FAIL;
         			}
         			msg.set("AddWord", "�ʺ�״̬��������");
         			msg.set("ReturnResult", "N");
                 	return FAIL;
         		}
         	}
         	else
         	{
         		msg.set("ReturnResult", "N");
                 msg.set("AddWord", "�������к���ϵͳʧ��");
                 return FAIL;
         	}
         }
    	 msg.set("ReturnResult", "N");
         msg.set("AddWord", "�ʺŲ�����");
         return FAIL;
    }
    /**
     * 04����֧Ʊ�˻������Թ��˻�������ѯCDM��
     */
    public int getCDM(String id,String acctNo,Message msg)
    {
    	String SQL = "";
    	String rcvMsg = "";
    	String status = "";
    	String PayAcctName = "";
    	int i = 0;
    	int j = 0;
    	int count = 0;
    	if(id.equals("04"))
        {
        	SQL = "select CDMAC_STS,CDMAC_NAM from CDM where CDMAC_NO = '"+acctNo+"'";
        	logger.info("��֤�˺�״̬ sql is:"+SQL);
        	rcvMsg = send.sendMsg(SQL,"8801" );
        	logger.info("��ȡTUXEDO���ݣ�["+rcvMsg+"]");
        	if(!rcvMsg.equals("NULL"))
        	{
        		i = rcvMsg.indexOf(10);
        		if(i < 0)
        		{
        			msg.set("AddWord", "δ�ܲ�ѯ���˻���Ϣ��");
                	msg.set("ReturnResult", "N");
                	return FAIL;
        		}
            	count = Integer.parseInt(rcvMsg.substring(i-1, i));
	        	if(count>0)
	        	{
	        		j = rcvMsg.lastIndexOf(10);
	        		logger.info("i          ==              " + i);
        			logger.info("j          ==              " + j);
        			logger.info("rcvMsg          ==              " + rcvMsg);
	            	status = rcvMsg.substring(i+1, i+2);
	            	PayAcctName = rcvMsg.substring(i+2, j-1);
	            	msg.set("AcctName", PayAcctName.trim());
	            	if(status.equals("0"))
	            	{
	            		msg.set("AddWord", "�˻�״̬����");
	                	msg.set("ReturnResult", "Y");
	                	return SUCCESS;
	            	}
	            	if(status.equals("1"))
	            	{
	            		msg.set("AddWord", "�˻�ȫ�⣡");
	            		msg.set("ReturnResult", "N");
	            		return FAIL;
	            	}
	            	if(status.equals("2")||status.equals("3")||status.equals("4"))
	            	{	            		
	            		msg.set("AddWord", "�˻������ã�");
	            		msg.set("ReturnResult", "N");
	            		return FAIL;
	            	}
	        	}
	        	else
	        	{
	        		msg.set("AddWord", "�˻�������");
	            	msg.set("ReturnResult", "N");
	            	return FAIL;
	        	}
        	}
        	else
        	{
        		msg.set("ReturnResult", "N");
                msg.set("AddWord", "�������к���ϵͳʧ��");
                return FAIL;
        	}
        }
    	 msg.set("ReturnResult", "N");
         msg.set("AddWord", "�ʺŲ�����");
         return FAIL;
    }
    /**
     * 10�����˻�����ѯSDM��(SDM---���ڷֻ���)
     */
    public int getSDM(String id,String acctNo,Message msg)
    {
    	String SQL = "";
    	String rcvMsg = "";
    	String status = "";
    	String PayAcctName = "";
    	int i = 0;
    	int j = 0;
    	int count = 0;
    	if(id.equals("10"))
        {
        	SQL = "select SDMACTSTS,SDMACNAM from SDM where SDMACTNO = '"+acctNo+"'";
        	rcvMsg = send.sendMsg(SQL,"8801");
        	if(!rcvMsg.equals("NULL"))
        	{
        		i = rcvMsg.indexOf(10);
        		logger.info("i          ==              " + i);
        		logger.info("rcvMsg          ==              [" + rcvMsg + "]");
        		if(i < 0)
        		{
        			msg.set("AddWord", "δ�ܲ�ѯ���˻���Ϣ��");
                	msg.set("ReturnResult", "N");
                	return FAIL;
        		}
        		count = Integer.parseInt(rcvMsg.substring(i-1, i));
        		if(count>0)
        		{
        			j = rcvMsg.lastIndexOf(10);       			
        			logger.info("j          ==              " + j);      			
                	status = rcvMsg.substring(i+1, i+10);
                	PayAcctName = rcvMsg.substring(i+11, j-1);
                	msg.set("AcctName", PayAcctName.trim());
                	if(status.substring(0, 1).equals("1")&&status.substring(1, 2).equals("1")||status.substring(1, 2).equals("2")
                			&&status.substring(2,3).equals("1")&&status.substring(3, 4).equals("1")||status.substring(3, 4).equals("3")
                			&&status.subSequence(4, 5).equals("1")||status.substring(4, 5).equals("2")&&status.subSequence(5, 6).equals("1")||status.substring(5, 6).equals("2")
                			&&status.subSequence(6, 7).equals("1")&&status.subSequence(7, 8).equals("1")
                			&&status.subSequence(8, 9).equals("1")||status.subSequence(8, 9).equals("2")||status.subSequence(8, 9).equals("3"))
                	{
                		msg.set("AddWord", "�˻�״̬������");
	                	msg.set("ReturnResult", "Y");
	                	return SUCCESS;
                	}
                	else
                	{
                		msg.set("AddWord", "�˻�״̬����");
	                	msg.set("ReturnResult", "N");
	                	return FAIL;
                	}
        		}
        		else
        		{
        			msg.set("AddWord", "�˻�������");
        			msg.set("ReturnResult", "N");
        			return FAIL;
        		}
        	}
	        else
	        {
	        	msg.set("ReturnResult", "N");
	            msg.set("AddWord", "�������к���ϵͳʧ��");
	            return FAIL;
	        }
        }
    	 msg.set("ReturnResult", "N");
         msg.set("AddWord", "�ʺŲ�����");
         return FAIL;
    }
    /**
     * IAC---ͨ�÷ֻ���
     */
    public int getIAC(String id,String acctNo,Message msg)
    {
    	String SQL = "";
    	String rcvMsg = "";
    	String status = "";
    	String PayAcctName = "";
    	int i = 0;
    	int j = 0;
    	int count = 0;
    	if(id.equals("01"))
        {
        	SQL = "select IACAC_STS,IACAC_NAM from IAC where IACAC_NO = '"+ acctNo +"'";
        	rcvMsg = send.sendMsg(SQL,"8801" );
        	if(!rcvMsg.equals("NULL"))
        	{
        		i = rcvMsg.indexOf(10);
        		if(i < 0)
        		{
        			msg.set("AddWord", "δ�ܲ�ѯ���˻���Ϣ��");
                	msg.set("ReturnResult", "N");
                	return FAIL;
        		}
        		count = Integer.parseInt(rcvMsg.substring(i-1, i));
        		if(count>0)
        		{
        			j = rcvMsg.lastIndexOf(10);
                	status = rcvMsg.substring(i+1, i+2);
                	PayAcctName = rcvMsg.substring(i+2, j-1);
                	msg.set("AcctName", PayAcctName.trim());
                	if(status.equals("2"))
                	{
                		msg.set("AddWord", "�˻�״̬����");
	                	msg.set("ReturnResult", "Y");
	                	return SUCCESS;
                	}
                	if(status.equals("1"))
                	{
                		msg.set("AddWord", "�˻�������");
            			msg.set("ReturnResult", "N");
            			return FAIL;
                	}
                	if(status.equals("3"))
                	{
                		msg.set("AddWord", "ȫ����");
            			msg.set("ReturnResult", "N");
            			return FAIL;
                	}
                	if(status.equals("4"))
                	{
                		msg.set("AddWord", "�տ��ֻ�ղ�����");
            			msg.set("ReturnResult", "N");
            			return FAIL;
                	}
        		}
        		else
        		{
        			msg.set("AddWord", "�˻�������");
        			msg.set("ReturnResult", "N");
        			return FAIL;
        		}
        	}
	        else
	        {
	        	msg.set("ReturnResult", "N");
	            msg.set("AddWord", "�������к���ϵͳʧ��");
	            return FAIL;
	        }
        }
        msg.set("ReturnResult", "N");
        msg.set("AddWord", "�ʺŲ�����");
        return FAIL;
    }
}


