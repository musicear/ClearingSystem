package resoft.tips.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>记录到公共数据暂存表中</p>
 * Author: liguoyin
 * Date: 2007-6-15
 * Time: 0:27:53
 */
public class SaveCommonInfo implements Action {
    public int execute(Message msg) throws Exception {
        //String filePath = msg.getString("批量文件");
        String filePath="/home/tips/test/公共数据.xml";
        BatchXmlProcessor processor = new BatchXmlProcessor(filePath);
        processor.setHandler(new CommonInfoHandler());
        processor.execute();
        return SUCCESS;
    }
}

class CommonInfoHandler implements BatchXmlHandler {
    CommonInfoHandler() {
        tagDataTypeMap.put("BatchHead9100", "");
        tagDataTypeMap.put("TaxCodeInfo101", "101");
        tagDataTypeMap.put("BankCodeInfo102", "102");
        tagDataTypeMap.put("NodeCodeInfo103", "103");
        tagDataTypeMap.put("TreCodeInfo104", "104");
        
        tagDataTypeMap.put("TaxTypeCode108", "108");
        tagDataTypeMap.put("TaxSubjectCode109", "109");
    }
    private static final Log logger = LogFactory.getLog(CommonInfoHandler.class);
    public Collection getTags() {
        return tagDataTypeMap.keySet();
    }

    public void process(String tagName, Map children) throws Exception {
        if (tagName.equals("BatchHead9100")) {
            updateBatch = (String) children.get("UpdateBatch");
        } else {
            if(lastTag.equals(tagName)) {
                rowNo ++;
            } else {
                rowNo = 1;
                lastTag = tagName;
            }
            String dataType = (String) tagDataTypeMap.get(tagName);
            
            for (Iterator itr = children.keySet().iterator(); itr.hasNext();) {
                String key = (String) itr.next();
                String value = (String) children.get(key);
                String sql = "insert into CommonInfo(updateBatch,dataType,rowNo,infoKey,infoValue) ";
                sql += " values('" + updateBatch + "','" + dataType + "'," + rowNo + ",'" + key + "','" + value + "')";
                DBUtil.executeUpdate(sql);
            }
        }
        logger.info("数据处理完成");
    }

    public void end() {

    }

    private Map tagDataTypeMap = new HashMap();//标签名与数据类型对应表
    private String updateBatch;
    private String lastTag = "";//上一处理标签
    private int rowNo = 1;//公共数据的行号
}
