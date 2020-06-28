package resoft.tips.action;

import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p></p>
 *
 * @Author:
 * Date: 2007-5-13
 * Time: 2:18:52
 */
public class PrintPacket implements Action {
    private static final Log logger = LogFactory.getLog(PrintPacket.class);
    public int execute(Message msg) throws Exception {
        Packager packager = new GenericXmlPackager();
        String xml = new String(packager.pack(msg));
        //logger.info(xml);
        Document doc = DocumentHelper.parseText(xml);
        OutputFormat format = OutputFormat.createPrettyPrint();
        StringWriter strWriter = new StringWriter();
        XMLWriter writer = new XMLWriter(strWriter,format);
        writer.write(doc);
        logger.info(strWriter.toString());
        return SUCCESS;
    }
}
