package resoft.tips.chqsh;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.chqxh.ACEPackUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

public class ProcessCheckAcctWithBank20090911 implements Action{

	private static final Log logger = LogFactory.getLog(ProcessCheckAcctWithBank20090911.class);
	SendMsgToBankSystem send = new SendMsgToBankSystem();
	String sql="";
	int Count=20,StartIndex=0;

	String chkDate="",preChkDate="",payeeBankNo="";
	
	public int execute(Message msg) {
		
		chkDate = msg.getString("ChkDate");        				//��������
		payeeBankNo = msg.getString("PayeeBankNo");
		String chkAcctType=msg.getString("ChkAcctType");		//��������
		
		if(chkAcctType.equals("0")){							//0:�ռ�;1:����
			//�ռ䲻�����ڽ��к˶�
			return FAIL;
		}
		
		String[] IADC_DATES=getIADAC_DATES();
		for(int i=0;i<IADC_DATES.length;i++){
			process_check(IADC_DATES[i]);
		}
		
		//�޸������к�
		process_bankOrgCode();
		
		process_status();
		return SUCCESS;
	}
	
	public void process_check(String date){
		logger.info("��ʼ����"+date+"���Ķ�����Ϣ");
		try{
			//����Թ���ϸ
			process_cdd(date);
			//������ϸ
			process_crdtl(date);
			//��������ϸ
			process_sdl(date);
		}catch(Exception e){
			logger.info("����"+date+"���Ķ����쳣");
			e.printStackTrace();
		}
		logger.info("��ɴ���"+date+"���Ķ�����Ϣ");
				
	}
	
	//�Թ���ϸ
	public void process_cdd(String bankDate) throws SQLException{
	    
		int queryCount=0,sum=0;
		StartIndex=1;
		
		
		String sqlField="CDDAC_NO,CDDVCHN,CDDGACBK,CDDAMT";
		do{
			
			//��ҳ��ѯ
			String splitPage=" c.row_Num between "+(StartIndex)+" and "+(StartIndex+Count-1)+" ";		
			sql="select "+sqlField+" from (select b.*,rownum row_num from (";
			sql+="  select "+sqlField+" from cdd ";
			sql+="  where substr(CDDVCHN,1,4) in ('8801','8802','8803','8804') and cddac_dat='"+bankDate+"'  ";
			sql+="  order by CDDAC_DAT,CDDVCHN ";
			sql+=") b) c where "+splitPage;
			logger.info("��ѯ�Թ���ϸSQL��"+sql);
			String recvMsg=send.sendMsg(sql,"8801");
			queryCount = Integer.parseInt(recvMsg.substring(0,7).trim());
			//logger.info("��������"+bankDate+",�Թ���ϸ������"+queryCount);
			String[] fieldLen={"15","8","4","18"};
			int rowBytes=15+8+4+18;
			String[] field={"PAYACCT","VCH_NO","BANKORG","TRAAMT"};
			recvMsg=recvMsg.substring(7);
			for (int j=0;j<queryCount;j++){
				String temp=recvMsg.substring(0,rowBytes);
				Map info=new HashMap();
				for (int i=0;i<field.length;i++){
					String[] tempArray=ACEPackUtil.subBytes(0, Integer.parseInt(fieldLen[i]), temp);
					temp=tempArray[1];
					info.put(field[i],tempArray[0].trim());		
				}				
				recvMsg=recvMsg.substring(rowBytes+1);
				//System.out.println("�Թ���ϸ��Ϣ��"+info);
				
				//��ǰ�ý�������˶�
				info.put("IADAC_DAT", bankDate);
				info.put("payAcctType", "1");							//�Թ��˺�
				processCheckWithTips(info);
				
				info=null;
			}
			StartIndex+=Count;
			sum+=queryCount;
		}while(queryCount==Count);
		
		logger.info("��������"+bankDate+",�Թ���ϸ������"+sum);
		
	}
	
	//����ϸ
	public void process_crdtl(String bankDate){
	    
		int queryCount=0,sum=0;
		StartIndex=1;
		String sqlField="CRDTLCRDNO,CRDTLVCHNO,CRDTLBR,CRDTLAMT";
		do{
			//��ҳ��ѯ
			String splitPage=" c.row_Num between "+(StartIndex)+" and "+(StartIndex+Count-1)+" ";		
			sql="select "+sqlField+" from (select b.*,rownum row_num from (";
			sql+="  select "+sqlField+" from CRDTL ";
			sql+=" where CRDTLECIND='0' and substr(CRDTLVCHNO,1,4) in ('8801','8802','8803','8804')  and CRDTLTRNDAT='"+bankDate+"' ";
			sql+="  order by CRDTLTRNDAT,CRDTLVCHNO ";
			sql+=") b) c where "+splitPage;
			logger.info("��ѯ����ϸSQL��"+sql);
			String recvMsg=send.sendMsg(sql,"8801");
			queryCount = Integer.parseInt(recvMsg.substring(0,7).trim());
			//logger.info("��������"+bankDate+",����ϸ������"+queryCount);
			String[] fieldLen={"19","8","4","18"};
			int rowBytes=19+8+4+18;
			String[] field={"PAYACCT","VCH_NO","BANKORG","TRAAMT"};
			recvMsg=recvMsg.substring(7);
			for (int j=0;j<queryCount;j++){
				String temp=recvMsg.substring(0,rowBytes);
				Map info=new HashMap();
				for (int i=0;i<field.length;i++){
					String[] tempArray=ACEPackUtil.subBytes(0, Integer.parseInt(fieldLen[i]), temp);
					temp=tempArray[1];
					info.put(field[i],tempArray[0].trim());		
				}				
				recvMsg=recvMsg.substring(rowBytes+1);
				//System.out.println("����ϸ��Ϣ��"+info);
				
				//��ǰ�ý�������˶�
				info.put("IADAC_DAT", bankDate);
				info.put("payAcctType", "2");							//��
				processCheckWithTips(info);
				
				info=null;
			}
			StartIndex+=Count;
			sum+=queryCount;
		}while(queryCount==Count);
		
		logger.info("��������"+bankDate+",����ϸ������"+sum);
		
	}	
	
	//������ϸ
	public void process_sdl(String bankDate){
	    
		int queryCount=0,sum=0;
		StartIndex=1;
		String sqlField="SDLACTNO,SDLVCHNO,SDLBR,SDLTRNAMT";
		do{
			//��ҳ��ѯ
			String splitPage=" c.row_Num between "+(StartIndex)+" and "+(StartIndex+Count-1)+" ";		
			sql="select "+sqlField+" from (select b.*,rownum row_num from (";
			sql+="  select "+sqlField+" from SDL ";
			sql+="  where SDLECIND='0' and substr(SDLVCHNO,1,4) in ('8801','8802','8803','8804') and SDLTRNDAT='"+bankDate+"'";
			sql+="  order by SDLTRNDAT,SDLVCHNO ";
			sql+=") b) c where "+splitPage;
			logger.info("��ѯ������ϸSQL��"+sql);
			String recvMsg=send.sendMsg(sql,"8801");
			queryCount = Integer.parseInt(recvMsg.substring(0,7).trim());
			//logger.info("��������"+bankDate+",����ϸ������"+queryCount);
			String[] fieldLen={"15","8","4","18"};
			int rowBytes=15+8+4+18;
			String[] field={"PAYACCT","VCH_NO","BANKORG","TRAAMT"};
			recvMsg=recvMsg.substring(7);
			for (int j=0;j<queryCount;j++){
				String temp=recvMsg.substring(0,rowBytes);
				Map info=new HashMap();
				for (int i=0;i<field.length;i++){
					String[] tempArray=ACEPackUtil.subBytes(0, Integer.parseInt(fieldLen[i]), temp);
					temp=tempArray[1];
					info.put(field[i],tempArray[0].trim() );		
				}				
				recvMsg=recvMsg.substring(rowBytes+1);
				//System.out.println("������ϸ��Ϣ��"+info);

				//��ǰ�ý�������˶�
				info.put("IADAC_DAT", bankDate);
				info.put("payAcctType", "3");							//����
				processCheckWithTips(info);

				info=null;
			}
			StartIndex+=Count;
			sum+=queryCount;
		}while(queryCount==Count);
		
		logger.info("��������"+bankDate+",������ϸ������"+sum);
		
	}
	
	
	/**
	 * ��ǰ�ý���������ϸ�ȶ�
	 * */
	public void processCheckWithTips(Map temp){
		
		try{
			String sqlRealtime = "select count(*) from realtimepayment where MSG_DATA='9004' and traAmt*(-1)="+Double.parseDouble((String)temp.get("TRAAMT"))+" and IADAC_DAT='" + (String)temp.get("IADAC_DAT") + "' and VCH_NO='" + (String)temp.get("VCH_NO") + "' ";
			String sqlBatch = "select count(*) from batchpackdetail where MSG_DATA='9004' and traAmt*(-1)="+Double.parseDouble((String)temp.get("TRAAMT"))+" and IADAC_DAT='" + (String)temp.get("IADAC_DAT") + "' and VCH_NO='" + (String)temp.get("VCH_NO") + "' ";
			int realCount = DBUtil.queryForInt(sqlRealtime);
			int batchCount = DBUtil.queryForInt(sqlBatch);
			
			//������ʵʱ��û�иö��ʼ�¼����ҪĨ��
			if ( (realCount + batchCount) == 0 ) {
				logger.info(temp+"\nǰ���޽�˰��Ϣ����������");
				temp.put("REASON", "TIPS����û�и���Ϣ,��Ҫ�ֹ�����˶�");
				insertAdjustAcct(temp);
				return;
			}else {					
				//����ʵʱ
				if (realCount>0) {
					String checkStatus=DBUtil.queryForString(" select checkStatus from realtimepayment where MSG_DATA='9004' and traAmt*(-1)="+Double.parseDouble((String)temp.get("TRAAMT"))+" and IADAC_DAT='" + (String)temp.get("IADAC_DAT") + "' and VCH_NO='" + (String)temp.get("VCH_NO") + "'  ");
					if(checkStatus.equals("0") || checkStatus.equals("1")){		//δ���˺Ͷ��˳ɹ�  ����Ҫ����
						logger.info(temp+"\n��ʵʱ��˰��Ϣ�Ѷ��˻�δ���ˣ��ݲ���Ҫ�ֹ�����˶�");
						return;
					}else {														//������Ҫ�ֹ��˶�
						logger.info(temp+"\nʵʱ��Ҫ�ֹ�����˶�");
						temp.put("REASON", "ʵʱ��˰��Ҫ�ֹ�����˶�");
						insertAdjustAcct(temp);	
						return;
					}
				}
				
				//��������					
				if (batchCount>0) {
					String checkStatus=DBUtil.queryForString(" select checkStatus from batchpackdetail where MSG_DATA='9004' and traAmt*(-1)="+Double.parseDouble((String)temp.get("TRAAMT"))+" and IADAC_DAT='" + (String)temp.get("IADAC_DAT") + "' and VCH_NO='" + (String)temp.get("VCH_NO") + "'  ");
					if(checkStatus.equals("0") || checkStatus.equals("1")){		//δ���˺Ͷ��˳ɹ�  ����Ҫ����
						logger.info(temp+"\n��������˰��Ϣ�Ѷ��˻�δ���ˣ��ݲ���Ҫ�ֹ�����˶�");
						return;
					}else {														//������Ҫ�ֹ��˶�
						logger.info(temp+"\n������˰��Ҫ�ֹ�����˶�");
						temp.put("REASON", "������˰��Ҫ�ֹ�����˶�");
						insertAdjustAcct(temp);	
						return;
					}
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
	/**
	 * ��¼adjustAcct
	 * */
	public void insertAdjustAcct(Map temp) throws Exception{		
		
		//��ѯ����Ϣ�Ƿ��Ѿ�����
		String adjustSql="select count(*) from adjustAcct where traAmt*(-1)="+Double.parseDouble((String)temp.get("TRAAMT"))+" and IADAC_DAT='" + (String)temp.get("IADAC_DAT") + "' and VCH_NO='" + (String)temp.get("VCH_NO") + "' ";
		//logger.info("�Ƿ���������SQL��"+adjustSql);
		int adjustCount=DBUtil.queryForInt(adjustSql);
		if ( adjustCount>0 ) {			//���е�����Ϣ�������ٴ���	
			logger.info(temp+"\n���и���Ϣ��������");
			return;
		}
		
		Map param = new HashMap();											
		param.put("chkDate", chkDate);
		param.put("IADAC_DAT", (String)temp.get("IADAC_DAT"));	
		param.put("VCH_NO", (String)temp.get("VCH_NO"));
		param.put("payAcct", (String)temp.get("PAYACCT"));
		param.put("traAmt", ""+Double.parseDouble((String)temp.get("TRAAMT"))*(-1));
		param.put("BRANCHNO", (String)temp.get("BANKORG"));												
		param.put("reason", (String)temp.get("REASON"));
		param.put("payAcctType", (String)temp.get("payAcctType"));
											
		DBUtil.insert("AdjustAcct", param);
		
	}
	
	
	/**
	 * �����˺ŵ������к�
	 * */
	public void process_bankOrgCode(){
		try{
			sql="select * from adjustAcct where chkDate='"+ chkDate+"' and BRANCHNO='9998'";
			logger.info(sql);
			List adjustList=QueryUtil.queryRowSet(sql);
			for(int j=0;j<adjustList.size();j++){
				Map row=(Map)adjustList.get(j);
				get_bankOrgCode(row);
				DBUtil.executeUpdate("Update adjustAcct set BRANCHNO='"+(String)row.get("BRANCHNO")+"' where traAmt="+Double.parseDouble((String)row.get("TRAAMT"))+" and IADAC_DAT='" + (String)row.get("IADAC_DAT") + "' and VCH_NO='" + (String)row.get("VCH_NO") + "' ");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void get_bankOrgCode(Map row){
		String payAcctType=(String)row.get("payAcctType");
		//logger.info("�˺����ͣ�"+payAcctType);
		//�Թ�
		if(payAcctType.equals("1")){
			return;
		}
		//��
		if(payAcctType.equals("2")){		
			String payAcct=(String)row.get("payAcct");
			String tempSql = "select p.brtbr from MCA m,brt p,SDM s where m.MCAEPTCOD='99' and p.brtbr=s.SDMGACBK and m.MCAACTNO=s.SDMACTNO and m.MCACRDNO='"+ payAcct +"'";
    		logger.info("���˻��Ĵ��� ��ѯSQL is:"+tempSql);
    		String recvMsg = send.sendMsg(tempSql,"8801" );
    		int count=Integer.parseInt(recvMsg.substring(0,7).trim());
    		if(count>0){
    			recvMsg=recvMsg.substring(7);
    			String bankOrgCode=recvMsg.substring(0,4);
    			row.put("BRANCHNO", bankOrgCode);
    		}
		}
		
		//����
		if(payAcctType.equals("3")){		
			String payAcct=(String)row.get("payAcct");
			String tempSql = "select p.brtbr from sdm s,brt p where p.brtbr=s.SDMGACBK and s.SDMACTNO='"+ payAcct +"' ";
    		logger.info("�����˻��Ĵ��� ��ѯSQL is:"+tempSql);
    		String recvMsg = send.sendMsg(tempSql,"8801" );
    		int count=Integer.parseInt(recvMsg.substring(0,7).trim());
    		if(count>0){
    			recvMsg=recvMsg.substring(7);
    			String bankOrgCode=recvMsg.substring(0,4);
    			row.put("BRANCHNO", bankOrgCode);
    		}
		}
		
		//logger.info("�����кţ�"+(String)row.get("BRANCHNO"));
	}
	
	/**
	 * ������һ��TIPS���ڶ�Ӧ�����ں������ڿ��
	 * */
	public String[] getIADAC_DATES(){
		String[] temp;
		try {						
			preChkDate=DBUtil.queryForString("select max(chkDate) from payCheck where chkDate<'"+chkDate+"' ");						
			SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
	             
            int day = ProcessTipsWork.compDate(preChkDate,chkDate);
            temp=new String[day+2];
            
            GregorianCalendar gc =new GregorianCalendar();
            gc.setTime(sf.parse(preChkDate));            
    		gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));            
    		
            for(int i=2;i<temp.length;i++){            	
            	gc.add(Calendar.DAY_OF_MONTH, +1);            	
            	temp[i]=sf.format(gc.getTime());
            	gc.setTime(sf.parse(temp[i]));
            }
            temp[1]=preChkDate;
            temp[0]=DBUtil.queryForString("select to_char(to_date('"+preChkDate+"','YYYYMMDD')-1,'YYYYMMDD') from dual");
            temp[temp.length-1]=chkDate;
                        
            for(int i=0;i<temp.length;i++){
            	System.out.println(temp[i]);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        	temp=new String[0];
        }
        return temp;
	}	
	
	/**
	 * �����˺ŵ������к�
	 * */
	public void process_status(){
		

		DBUtil.executeUpdate("Update bank_relation set status='0'");
			
		
		}
/*	
	//yangyuanxu add 
	public String getOrgCode(String payeeBaNo) throws SQLException{
		String OrgCode = "";
		OrgCode=DBUtil.queryForString("select bankorgcode from bank_relation where payeeBankNo='"+payeeBaNo+"'");	
		OrgCode=OrgCode.trim();
		return OrgCode;
	}	
*/	
	public static void main(String[] args){
		
		ProcessCheckAcctWithBank20090911 test=new ProcessCheckAcctWithBank20090911();
		Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);
		Message msg=new DefaultMessage();
		msg.set("ChkDate", "20091229");
		msg.set("ChkAcctType", "1");
		test.execute(msg);
		
		//test.chkDate="20091225";
		//test.process_bankOrgCode();
		
		//test.chkDate="20091225";
		//test.getIADAC_DATES();
		
	}
}
