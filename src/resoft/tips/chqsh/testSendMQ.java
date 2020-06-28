package resoft.tips.chqsh;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.action.SendToMQ;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;


public class testSendMQ implements Action{
	
	public int execute(Message msg) {
		try{
			
			List sendList=QueryUtil.queryRowSet("select * from testBatch where SENDFLAG='Y' ");
	        if(sendList.size()>0){
	        	Map row=(Map)sendList.get(0);
	        	String nodeCode="613220000026";	        	
	        	SendToMQ action = new SendToMQ();
	            action.setQueueManagerName("QM_TIPS_"+nodeCode+"_01");
	            String sendType=(String)row.get("TYPE");
	        	if(sendType.equals("1")){//实时	        	        
	    	        action.setQueueName("PBC."+nodeCode+".ONLINE.OUT");
	        	}else{
	        		action.setQueueName("PBC."+nodeCode+".BATCH.OUT");
	        	}	            
	        	String filePath=(String)row.get("FILENAME");
	            
	            File f=new File(filePath);
	            	            
	            byte[] bytes = new byte[(int) f.length()];
	            InputStream is = new FileInputStream(f);
	            is.read(bytes);
	            msg.set("报文内容",new String(bytes));        
	            //msg.set("correlationId","20070710064910634739");
	            msg.set("correlationId","REQ\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0");
	            msg.set("//CFX/HEAD/MsgID","123");
	            action.execute(msg);
	        	
	            
	            DBUtil.executeUpdate("update testBatch set SENDFLAG='N' ");
	            return SUCCESS;
	        }else{
	        	return FAIL;
	        }
				        
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return SUCCESS;
	}
}
