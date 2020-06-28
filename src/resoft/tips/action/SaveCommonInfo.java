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
 * <p>��¼�����������ݴ����</p>
 * Author: liguoyin
 * Date: 2007-6-15
 * Time: 0:27:53
 */
public class SaveCommonInfo implements Action {
    public int execute(Message msg) throws Exception {
        //String filePath = msg.getString("�����ļ�");
        String filePath="/home/tips/test/��������.xml";
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
        logger.info("���ݴ������");
    }

    public void end() {

    }

    private Map tagDataTypeMap = new HashMap();//��ǩ�����������Ͷ�Ӧ��
    private String updateBatch;
    private String lastTag = "";//��һ�����ǩ
    private int rowNo = 1;//�������ݵ��к�
}
