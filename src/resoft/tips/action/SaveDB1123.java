package resoft.tips.action;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Message;

/**
 * <p>保存止付信息到数据库</p>
 * User: liwei
 * Date: 2007-07-05
 * Time: 11:06:06
 */
public class SaveDB1123 extends AbstractSyncAction {

    private static final Log logger = LogFactory.getLog(SaveDB1123.class);

    public int execute(Message msg) throws Exception {
        String filePath = msg.getString("批量文件");
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filePath), "GBK");
        Map params = new HashMap();

        List packTags = new ArrayList() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
                add("TaxOrgCode");
                add("EntrustDate");
                add("StopNo");
                add("StopType");
                add("OriEntrustDate");
                add("OriPackNo");
                add("OriTraNo");
                add("StopReason");
                
                add("CancelAnswer");
                add("AddWord");
            }
        };

        String localName = "";
        for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    localName = reader.getLocalName();
                    break;
                case XMLStreamConstants.CHARACTERS:
                    String text = reader.getText().trim();
                    if (!text.equals("")) {
                        if (packTags.contains(localName)) {
                            params.put(localName, text);
                        }
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    localName = reader.getLocalName();
                    break;
            }
        }

        logger.info(params);
        msg.set("packFields", params);

        //验证是否已有止付应答
        int count = DBUtil.queryForInt("select count(*) from DelApply where cancelAnswer='1' and EntrustDate='" + (String) params.get("EntrustDate") + "' and taxOrgCode='" + (String) params.get("TaxOrgCode") + "' and stopNo='" + (String) params.get("StopNo") + "' ");
        if (count > 0) {
            msg.set("canAnsFlag", "T");
        } else {
            msg.set("canAnsFlag", "F");
            DBUtil.insert("DelApply", params);
        }
        return SUCCESS;
    }
}