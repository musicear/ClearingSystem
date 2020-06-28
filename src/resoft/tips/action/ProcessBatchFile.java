package resoft.tips.action;

import java.io.FileInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.tips.util.SaveRecvLogUtil;
import resoft.xlink.comm.helper.Controller;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>处理批量文件</p>
 * Author: Albert Li
 * Date: 2007-6-8
 * Time: 2:10:37
 */
public class ProcessBatchFile implements Action {
    private static final Log logger = LogFactory.getLog(ProcessBatchFile.class);

    private static Configuration conf = Configuration.getInstance();
    public int execute(Message msg) throws Exception {
        String filePath = msg.getString("批量文件");
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filePath), "GBK");
        String localName = "";
        String msgId = "",msgRef = "",workDate = "",msgNo = "";
        boolean breakParse = false;
        for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    localName = reader.getLocalName();
                    break;
                case XMLStreamConstants.CHARACTERS:
                    String text = reader.getText().trim();
                    if (!text.equals("")) {
                        if (localName.equals("MsgID")) {
                            msgId = text;
                        } else if (localName.equals("MsgRef")) {
                            msgRef = text;
                            msg.set("//CFX/HEAD/MsgRef",msgRef);
                        } else if (localName.equals("WorkDate")) {
                            workDate = text;
                            msg.set("//CFX/HEAD/WorkDate",workDate);
                        } else if (localName.equals("MsgNo")) {
                            msgNo = text;
                        }
                        //logger.info(text);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    localName = reader.getLocalName();
                    if (localName.equals("HEAD")) {
                        breakParse = true;
                    }
                    break;
            }
            if (breakParse) {
                //停止解析
                SaveRecvLogUtil.saveRecvLog(msgId, workDate, msgRef, msgNo,"_0_"+msg.getString("tranCode"),"0");
                msg.set("交易码", msgNo);
                //进行调度
                Controller controller = new Controller();
                controller.setNameOfTransCode("交易码");
                controller.setGlobalPropertiesReader(conf.getProperty("general", "globalPropertiesReader"));
                controller.setInternalErrorAction(new Action() {
                    public int execute(Message msg) throws Exception {
                        logger.error("交易执行失败，交易码：" + msg.get("交易码"));
                        return SUCCESS;
                    }
                });
                logger.info("开始想实时队列发送批处理文件的相应文件！");
                try
                {
                	controller.execute(msg);
                }
                catch(Exception e)
        		{ 
                	logger.error("交易执行失败，交易码：" + msg.get("交易码")); 
        		}
                break;
            }
        }
        return SUCCESS;
    }
}
