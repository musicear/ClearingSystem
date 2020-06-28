package junit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import resoft.basLink.util.DBUtil;
import resoft.tips.action.SendToMQ;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p>发送MQ测试信息</p>
 * Author: Administrator
 * Date: 2008-05-29
 * Time: 16:07:17
 */
public class TestSendToMQ { 
    public static void main(String[] args) throws IOException {
    	/**
    	 * args[0]:银行简称
    	 * args[1]:0,实时队列;1,批量队列
    	 * args[2]:文件路径
    	 * */
    	
    	Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);
    	/*
    	String nodeCode="";
    	//if(args[0].toUpperCase().equals("CHQSH")){
    		nodeCode="402100000010";
    	//}
    	SendToMQ action = new SendToMQ();
        action.setQueueManagerName("QM_TIPS_"+nodeCode+"_01");
    	//if(args[1].equals("1")){//实时	        	        
	        action.setQueueName("PBC."+nodeCode+".ONLINE.OUT");
    	//}else{
    		action.setQueueName("PBC."+nodeCode+".BATCH.OUT");
    	//}
        */
        String	nodeCode="402100000010";
        
    	SendToMQ action = new SendToMQ();
        action.setQueueManagerName("QM_TIPSEMLU");
    	//action.setQueueManagerName("QM_TIPS_614050000006_01");
        action.setQueueName("PBC."+nodeCode+".BATCH.OUT");
        Message msg = new DefaultMessage();
        
        File f=new File("D:\\3143.xml");
        //File f = new File("D:\\工作文档\\项目相关文档\\TIPS\\TIPS测试报文\\运行参数通知(9106).xml");
        //File f = new File("D:\\工作文档\\项目相关文档\\TIPS\\TIPS测试报文\\TIPS转发批量扣税请求(3102).xml");
        //File f = new File("D:\\工作文档\\项目相关文档\\TIPS\\TIPS测试报文\\实时冲正请求(1021).xml");
        //File f = new File("D:\\工作文档\\项目相关文档\\TIPS\\TIPS测试报文\\TIPS转发实时扣税请求(3001).xml");
        //File f = new File("D:\\工作文档\\项目相关文档\\TIPS\\TIPS测试报文\\公共数据更新通知(9100).xml");
        //File f = new File("D:\\工作文档\\项目相关文档\\TIPS\\TIPS测试报文\\自由格式报文(9105).xml");
        //File f = new File("D:\\工作文档\\项目相关文档\\TIPS\\TIPS测试报文\\三方协议验证请求（9114）.xml");
        //File f = new File("D:\\工作文档\\项目相关文档\\TIPS\\TIPS测试报文\\状态变更通知（9101）.xml");
        //File f = new File("D:\\工作文档\\项目相关文档\\TIPS\\TIPS测试报文\\故障通知（9102）.xml");
        //File f = new File("D:\\工作文档\\项目相关文档\\TIPS\\TIPS测试报文\\强制退出通知（9103）.xml");
        //File f = new File("D:\\工作文档\\项目相关文档\\TIPS\\TIPS测试报文\\连接测试（9005）.xml");
        //File f = new File("D:\\工作文档\\项目相关文档\\TIPS\\TIPS测试报文\\实时冲正请求(1021).xml");
        
        
        byte[] bytes = new byte[(int) f.length()];
        InputStream is = new FileInputStream(f);
        is.read(bytes);
        msg.set("报文内容",new String(bytes));        
        //msg.set("correlationId","20070710064910634739");
        msg.set("correlationId","REQ\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0");
        msg.set("//CFX/HEAD/MsgID","123");
        action.execute(msg);

    }
}
