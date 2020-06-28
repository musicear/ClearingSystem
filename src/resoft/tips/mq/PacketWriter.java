package resoft.tips.mq;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import java.text.NumberFormat;
//import java.text.DecimalFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;

/**
 * <p>�����ı�������ʱ�ļ���</p>
 * Author: liguoyin
 * Date: 2007-8-13
 * Time: 23:44:32
 */
public class PacketWriter {
    private static final Log logger = LogFactory.getLog(PacketWriter.class);

    private static Configuration conf = Configuration.getInstance();

    /**
     * �������ݵ���ʱ�ļ��У��������ļ�·��
     */
    public static String writePacket(String data,String tranCode) throws IOException {
        String fileName = getTmpFileName(tranCode);

        String tmpPath = conf.getProperty("general", "tmpPath");
        File f = new File(tmpPath, fileName);
        try {
            Writer writer = new FileWriter(f);
            writer.write(data);
            writer.close();
            logger.info("��Ϣ�������ʱ�ļ�:" + fileName);
        } catch (IOException e) {
            logger.error("������Ϣ����ʱ�ļ�ʧ��:" + fileName, e);
            logger.error("��Ϣ����Ϊ��" + data);
            throw e;
        }
        return f.getAbsolutePath();
    }

    private static String getTmpFileName(String tranCode) {
        //������ʱ�ļ�����ΪyyyyMMddhhmmss+3�����
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        String fileName = df.format(new Date());
//        NumberFormat nf = new DecimalFormat("000");
//        fileName += nf.format(Math.random() * 1000);
        fileName+=tranCode;
        fileName += ".xml";
        return fileName;
    }
}
