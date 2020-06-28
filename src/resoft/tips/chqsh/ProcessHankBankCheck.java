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
		logger.info("开始处理"+date+"核心对账信息");
		try{
			//处理对公明细
			bankCheck.process_cdd(date);
			//处理卡明细
			bankCheck.process_crdtl(date);
			//处理储蓄明细
			bankCheck.process_sdl(date);
		}catch(Exception e){
			logger.info("处理"+date+"核心对账异常");
			e.printStackTrace();
		}
		logger.info("完成处理"+date+"核心对账信息");
		
		//修改总账行号
		bankCheck.process_bankOrgCode();
	}	
	
	/**
	 * 计算上一个TIPS日期对应的行内核心日期跨度
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
            	System.out.println("需核对核心日期："+temp[i]);
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
		
		System.out.println("手工核对开始");
		Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);   			
        test.execute();
        
        System.out.println("手工核对完成");
        System.in.read();
        
	}
	
	public static void showUsage() {
		System.out.print("用法： checkwithbank.sh <对帐日期> <核心开始日期>");
		System.out.println("对帐日期格式：yyyymmdd，核心开始日期：8位核心开始日期");

	}
}
