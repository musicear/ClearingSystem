package resoft.tips.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import java.text.NumberFormat;
//import java.text.DecimalFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>���ñ���ͷ</p>
 * Author: liguoyin
 * Date: 2007-6-11
 * Time: 23:52:10
 */
public class SetHeadValue implements Action {
	
//	private static final Log logger = LogFactory.getLog(SaveDB1123.class); 
	private Configuration conf = Configuration.getInstance();
	private String desNodeCode=null;	//Ŀ�Ľڵ����	
	
	private static final Log logger = LogFactory.getLog(SetHeadValue.class);
    public int execute(Message msg) throws Exception {
        
        msg.set("//CFX/HEAD/VER","1.0");
        msg.set("//CFX/HEAD/DES", this.desNodeCode!=null?conf.getProperty("general", "TIPSDesNodeCode"):msg.getString("//CFX/HEAD/SRC"));//����Ŀ�Ľڵ㣬������ԭ�ڵ�
        msg.set("//CFX/HEAD/SRC",conf.getProperty("general","BankSrcNodeCode"));	 
        msg.set("//CFX/HEAD/DES",conf.getProperty("general","TIPSDesNodeCode"));
        msg.set("//CFX/HEAD/APP","TIPS");
        msg.set("//CFX/HEAD/MsgNo",msgNo);
        //String msgId = getMsgId();
        //msg.set("//CFX/HEAD/MsgID",msgId);
        //���ù�������                
        try{
        	msg.set("//CFX/HEAD/WorkDate", DBUtil.queryForString("select workDate from sysstatus"));
        	//msg.set("//CFX/HEAD/WorkDate", msg.get("//CFX/HEAD/WorkDate"));
        }catch(Exception e){
        	DateFormat df = new SimpleDateFormat("yyyyMMdd");
        	msg.set("//CFX/HEAD/WorkDate",df.format(new Date()));
        }
        //Ԥ���ֶ�  
        msg.set("//CFX/HEAD/Reserve","Ԥ��");
        return SUCCESS;
    }

    /**
    private String getMsgId() {
        //�������Ϊ"����" + 6λ�����
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        String id = df.format(new Date());
        NumberFormat nf = new DecimalFormat("000000");
        id += nf.format(Math.random() * 1000000);
        return id;
    }
    */
    public void setMsgNo(String msgNo) {
        this.msgNo = msgNo;
    }
    
    public void setDesNodeCode(String desNode){
    	this.desNodeCode=desNode;     	
    }

    private String msgNo;
}
