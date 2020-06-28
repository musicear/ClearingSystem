package resoft.tips.action2;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.action.AbstractSyncAction;
import resoft.xlink.core.Message;

/**
 * <p> * 保存批量直接支付额度回执到数据库 * </p>
 * User: chenlujia 
 * Date: 2008-8-20 
 */
public class SaveDB313X extends AbstractSyncAction {
    private static final Log logger = LogFactory.getLog(SaveDB313X.class);

    public int execute(Message msg) throws Exception {
        String filePath = msg.getString("批量文件");
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filePath), "GBK");
        String localName = "", parentLocalName="";
        String treCode = "", entrustDate = "", oriPackNo= "", acctDate= "",text="",oriTraNo="",result="",description="";
        //包信息
        Map packParams = new HashMap();
        //额度信息
        Map quotaParams = new HashMap();
        //额度明细信息
        Map detailParams = new HashMap();
        Stack tagStack = new Stack();
        int billCount=0, detailCount=0;
        for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                	text = reader.getText().trim();
                    localName = reader.getLocalName();

                    tagStack.push(localName);
                    if (localName.equals("BatchHead"+ msgNo)) {
                    	packParams.clear();
                    	billCount=0;
                    } else if (localName.equals("BatchReturn"+ msgNo)) {
                    	quotaParams.clear();
                    	billCount ++;
                    	detailCount=0;
                    } 
                    if (tagStack.size() > 1) {
                    	parentLocalName=(String)(tagStack.get(tagStack.size()-2));
                    } else {
                    	parentLocalName="";
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                	 text = reader.getText().trim();
                	 if (!text.equals("")) {
                		 if (parentLocalName.equals("BatchHead"+ msgNo)) {
                			 if (localName.equals("OriPackNo")) {
                				 oriPackNo = text;
                			 } else if (localName.equals("TreCode")) {
                				 treCode = text;
                			 } else if (localName.equals("EntrustDate")) {
                				 entrustDate = text;
                			 }
                		 }else if (parentLocalName.equals("BatchReturn"+ msgNo)) {
                			 if (localName.equals("OriTraNo")) {
                				 oriTraNo = text;
                			 }  else if (localName.equals("Result")) {
                				 result = text;
                			 }else if (localName.equals("Description")) {
                				 description = text;
                			 }else if (localName.equals("AcctDate")) {
                				 acctDate = text;
                			 }
                			 
                		 }
                	 }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    localName = reader.getLocalName();
                    tagStack.pop();
                    if (localName.equals("BatchReturn"+ msgNo)) {
                    	String sql = "update PayQuota set acctDate='"+acctDate+"',RESULT='"+result+"',description='"+description+"' " +
						                    			"where TreCode='"+treCode+"' and packNo='"+oriPackNo+"' and TraNo='"+oriTraNo+"' and entrustDate='"+entrustDate+"'";
						DBUtil.executeUpdate(sql);
						logger.info("更新支付额度表："+sql);
                    	
                    }  else if (localName.equals("MSG")) {
//                    	全部明细处理完毕，设置批量包回执状态为“已回执”，并设置回执时间
                    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    	String sql = "update QuotaPack set returnStatus='1',returnTime='" + df.format(new Date()) + "' "+
                        " where TreCode='" + treCode + "' and entrustDate='" + entrustDate + "' and packNo='" + oriPackNo + "' and payType='1'";
                    	DBUtil.executeUpdate(sql);
                    	logger.info("更新支付额度包表："+sql);
                    	updateQuotaBalance(oriPackNo,oriTraNo,entrustDate,treCode);
                    }
                    break;
            }
        }
        return SUCCESS;
    }
    /**
     * 更新余额表，增加相应额度
     * @param packNo
     * @param oriTraNo
     * @param entrustDate
     * @param treCode
     * @throws Exception
     */
    private void updateQuotaBalance(String packNo,String oriTraNo,String entrustDate,String treCode) throws Exception{
    	String sql = "SELECT * FROM PayQuotaDetail " +
    			"WHERE TreCode='"+treCode+"' and packNo='"+packNo+"' and TraNo='"+oriTraNo+"' and entrustDate='"+entrustDate+"'";
		List result = QueryUtil.queryRowSet(sql);
		for(int i=0;i<result.size();i++){
			Map detailMap = (Map)(result.get(i));
			String amt=(String)(detailMap.get("amt"));
			String bdgOrgCode=(String) detailMap.get("bdgOrgCode");
			String funcSbtCode=(String) detailMap.get("FuncBdgSbtCode");
			String update="update PayQuotaBalance set BalanceAmt=BalanceAmt-"+amt+" where BdgOrgCode='" + bdgOrgCode + "' AND FuncSbtCode='" + funcSbtCode + "'";
			DBUtil.executeUpdate(update);
			logger.info("更新支付额度明细表："+update);
		}
		
		
		
    }
	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
	}
	 private String msgNo;
}
