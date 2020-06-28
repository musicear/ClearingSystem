package resoft.junit;

import resoft.basLink.util.JmsSender;
import resoft.basLink.util.Sender;

/**
 * function: 发送JMS消息，以便测试双定户
 * User: albert lee
 * Date: 2005-10-20
 * Time: 19:32:30
 */
public class JmsSenderTest {
    public static void main(String[] args) throws Exception{
//        //初始化Context
//        Properties prop = new Properties();
//        prop.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
//        prop.put(Context.PROVIDER_URL,"t3://127.0.0.1:7001");
//
//
//        Context ctx = new InitialContext(prop);
//        //
//        QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ctx.lookup("dc.jms.tb.QCF");
//        QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();
//        QueueSession queueSession = queueConnection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
//        Queue queue = (Queue) ctx.lookup("dc.jms.tb.QUEUE");
//        QueueSender sender = queueSession.createSender(queue);
//        TextMessage message = queueSession.createTextMessage();
//        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<business id=\"Depo.Freeze.Ctais.TaxBank\">\n" +
//                "\t<group>\n" +
//                "\t\t<data name=\"xtsphm\" value=\"370228918719011278\"/>\n" +
//                "\t\t<data name=\"jyrq\" value=\"2005-11-09\"/>\n" +
//                "\t\t<data name=\"nsrsbh\" value=\"37013242324324\"/>\n" +
//                "\t\t<data name=\"nsrmc\" value=\"毛泽东\"/>\n" +
//                "\t\t<data name=\"nsr_swjg_dm\" value=\"1234\"/>\n" +
//                "\t\t<data name=\"zsjg_dm\" value=\"34324\"/>\n" +
//                "\t\t<data name=\"zsjg_jc\" value=\"市南区国税局\"/>\n" +
//                "\t\t<data name=\"zgswry_dm\" value=\"3423412\"/>\n" +
//                "\t\t<data name=\"lrr_dm\" value=\"342134\"/>\n" +
//                "\t\t<data name=\"skss_swjg_dm\" value=\"632123\"/>\n" +
//                "\t\t<data name=\"djzclx_dm\" value=\"02\"/>\n" +
//                "\t\t<data name=\"djzclx_mc\" value=\"私营企业\"/>\n" +
//                "\t\t<data name=\"yhzl_dm\" value=\"0011\"/>\n" +
//                "\t\t<data name=\"yh_dm\" value=\"342142\"/>\n" +
//                "\t\t<data name=\"yh_mc\" value=\"工商银行延安三路支行\"/>\n" +
//                "\t\t<data name=\"yhzh\" value=\"3242398127221\"/>\n" +
//                "\t\t<data name=\"sssq_q\" value=\"2005-11-01\"/>\n" +
//                "\t\t<data name=\"sssq_z\" value=\"2005-11-29\"/>\n" +
//                "\t\t<data name=\"spzl_dm\" value=\"3212\"/>\n" +
//                "\t\t<data name=\"yskm_dm\" value=\"010102\"/>\n" +
//                "\t\t<data name=\"yskm_mc\" value=\"私营企业增值税\"/>\n" +
//                "\t\t<data name=\"ysfpbl_dm\" value=\"007525\"/>\n" +
//                "\t\t<data name=\"ysfpbl_mc\" value=\"3412424\"/>\n" +
//                "\t\t<data name=\"skgk_dm\" value=\"0012342\"/>\n" +
//                "\t\t<data name=\"skgk_mc\" value=\"市南国库\"/>\n" +
//                "\t\t<data name=\"lsgx_dm\" value=\"07\"/>\n" +
//                "\t\t<data name=\"lsgx_jc\" value=\"个体\"/>\n" +
//                "\t\t<data name=\"spjkqx\" value=\"2005-12-01\"/>\n" +
//                "\t\t<data name=\"zsxm_dm\" value=\"02\"/>\n" +
//                "\t\t<data name=\"zsxm_mc\" value=\"增值税\"/>\n" +
//                "\t\t<data name=\"zspm_dm\" value=\"0102\"/>\n" +
//                "\t\t<data name=\"zspm_mc\" value=\"批发零售\"/>\n" +
//                "\t\t<data name=\"kssl\" value=\"500\"/>\n" +
//                "\t\t<data name=\"xssr\" value=\"500\"/>\n" +
//                "\t\t<data name=\"sl\" value=\"0.70\"/>\n" +
//                "\t\t<data name=\"yjhkcje\" value=\"0\"/>\n" +
//                "\t\t<data name=\"sjse\" value=\"350\"/>\n" +
//                "\t\t<data name=\"bz1\" value=\"宁要社会主义的草，不要资本主义的苗\"/>\n" +
//                "\t\t<data name=\"bz2\" value=\"\"/>\n" +
//                "\t\t<data name=\"jdxz_dm\" value=\"05\" />\n" +
//                "\t\t<data name=\"szsc_dm\" value=\"02\" />\n" +
//                "\t\t<data name=\"fddbrmc\" value=\"RPC\" />\n" +
//                "\t\t<data name=\"scjydz\" value=\"宇宙、太阳系、地球、中国\" />\n" +
//                "\t</group>\n" +
//                "</business>";
//        message.setText(xml);
//        sender.send(message);
//        queueConnection.close();
//

        Sender s = new JmsSender();
        s.setProperty(JmsSender.Property_QueueFactoryName,"dc.jms.tb.QCF");
        s.setProperty(JmsSender.Property_QueueName,"dc.jms.tb.QUEUE");
        s.send("<business></business>".getBytes());

    }
}
