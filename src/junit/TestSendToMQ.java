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
 * <p>����MQ������Ϣ</p>
 * Author: Administrator
 * Date: 2008-05-29
 * Time: 16:07:17
 */
public class TestSendToMQ { 
    public static void main(String[] args) throws IOException {
    	/**
    	 * args[0]:���м��
    	 * args[1]:0,ʵʱ����;1,��������
    	 * args[2]:�ļ�·��
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
    	//if(args[1].equals("1")){//ʵʱ	        	        
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
        //File f = new File("D:\\�����ĵ�\\��Ŀ����ĵ�\\TIPS\\TIPS���Ա���\\���в���֪ͨ(9106).xml");
        //File f = new File("D:\\�����ĵ�\\��Ŀ����ĵ�\\TIPS\\TIPS���Ա���\\TIPSת��������˰����(3102).xml");
        //File f = new File("D:\\�����ĵ�\\��Ŀ����ĵ�\\TIPS\\TIPS���Ա���\\ʵʱ��������(1021).xml");
        //File f = new File("D:\\�����ĵ�\\��Ŀ����ĵ�\\TIPS\\TIPS���Ա���\\TIPSת��ʵʱ��˰����(3001).xml");
        //File f = new File("D:\\�����ĵ�\\��Ŀ����ĵ�\\TIPS\\TIPS���Ա���\\�������ݸ���֪ͨ(9100).xml");
        //File f = new File("D:\\�����ĵ�\\��Ŀ����ĵ�\\TIPS\\TIPS���Ա���\\���ɸ�ʽ����(9105).xml");
        //File f = new File("D:\\�����ĵ�\\��Ŀ����ĵ�\\TIPS\\TIPS���Ա���\\����Э����֤����9114��.xml");
        //File f = new File("D:\\�����ĵ�\\��Ŀ����ĵ�\\TIPS\\TIPS���Ա���\\״̬���֪ͨ��9101��.xml");
        //File f = new File("D:\\�����ĵ�\\��Ŀ����ĵ�\\TIPS\\TIPS���Ա���\\����֪ͨ��9102��.xml");
        //File f = new File("D:\\�����ĵ�\\��Ŀ����ĵ�\\TIPS\\TIPS���Ա���\\ǿ���˳�֪ͨ��9103��.xml");
        //File f = new File("D:\\�����ĵ�\\��Ŀ����ĵ�\\TIPS\\TIPS���Ա���\\���Ӳ��ԣ�9005��.xml");
        //File f = new File("D:\\�����ĵ�\\��Ŀ����ĵ�\\TIPS\\TIPS���Ա���\\ʵʱ��������(1021).xml");
        
        
        byte[] bytes = new byte[(int) f.length()];
        InputStream is = new FileInputStream(f);
        is.read(bytes);
        msg.set("��������",new String(bytes));        
        //msg.set("correlationId","20070710064910634739");
        msg.set("correlationId","REQ\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0");
        msg.set("//CFX/HEAD/MsgID","123");
        action.execute(msg);

    }
}
