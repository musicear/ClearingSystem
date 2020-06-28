package resoft.tips.chqsh;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;

public class ProcessHankBankCheck {

	private static final Log logger = LogFactory.getLog(ProcessHankBankCheck.class);
	String chkDate="",preChkDate="";
	ProcessCheckAcctWithBank bankCheck=new ProcessCheckAcctWithBank();
	
	public void execute() {
		
		String[] IADC_DATES=this.getIADAC_DATES();
		for(int i=0;i<IADC_DATES.length;i++){
			process_check(IADC_DATES[i]);
		}
	}
	
	public void process_check(String date){
		logger.info("��ʼ����"+date+"���Ķ�����Ϣ");
		try{
			//����Թ���ϸ
			bankCheck.process_cdd(date);
			//������ϸ
			bankCheck.process_crdtl(date);
			//��������ϸ
			bankCheck.process_sdl(date);
		}catch(Exception e){
			logger.info("����"+date+"���Ķ����쳣");
			e.printStackTrace();
		}
		logger.info("��ɴ���"+date+"���Ķ�����Ϣ");
		
		//�޸������к�
		bankCheck.process_bankOrgCode();
	}	
	
	/**
	 * ������һ��TIPS���ڶ�Ӧ�����ں������ڿ��
	 * */
	public String[] getIADAC_DATES(){
		String[] temp;
		try {						
			
			SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
	             
            int day = ProcessTipsWork.compDate(preChkDate,chkDate);
            temp=new String[day+1];
            
            GregorianCalendar gc =new GregorianCalendar();
            gc.setTime(sf.parse(preChkDate));            
    		gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));            
    		
            for(int i=1;i<temp.length;i++){            	
            	gc.add(Calendar.DAY_OF_MONTH, +1);            	
            	temp[i]=sf.format(gc.getTime());
            	gc.setTime(sf.parse(temp[i]));
            }            
            temp[0]=preChkDate;
            temp[temp.length-1]=chkDate;
                        
            for(int i=0;i<temp.length;i++){
            	System.out.println("��˶Ժ������ڣ�"+temp[i]);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        	temp=new String[0];
        }
        return temp;
	}	
	
	public static void main(String[] args) throws Exception{
		
		ProcessHankBankCheck test=new ProcessHankBankCheck();
		
		if (args.length < 2) {
			showUsage();
			return;
		}
		
		test.chkDate = args[0];
		test.bankCheck.chkDate=args[0];
		test.preChkDate=args[1];
		DateFormat df = new SimpleDateFormat("yyyyMMdd");		
		try {
			df.parse(test.chkDate);
		} catch (ParseException e) {
			showUsage();
			return;
		}
		
		System.out.println("�ֹ��˶Կ�ʼ");
		Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);   			
        test.execute();
        
        System.out.println("�ֹ��˶����");
        System.in.read();
        
	}
	
	public static void showUsage() {
		System.out.print("�÷��� checkwithbank.sh <��������> <���Ŀ�ʼ����>");
		System.out.println("�������ڸ�ʽ��yyyymmdd�����Ŀ�ʼ���ڣ�8λ���Ŀ�ʼ����");

	}
}
