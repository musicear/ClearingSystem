package resoft.basLink.www;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import resoft.basLink.monitor.Monitor;

/**
 * function: 显示最新的交易信息
 * 生成格式为：
 * <CFX>
 *    <group id="1">
 *        <![CDATA[
 *            <CFX>
 *               <HEAD>
 *                 ...
 *               </HEAD>
 *               </MSG>
 *                  ...
 *               </MSG>
 *            </CFX>
 *        ]]>
 *    </group>
 * </CFX>
 * User: albert lee
 * Date: 2005-9-16
 * Time: 9:54:05
 */
public class MonitorServlet extends HttpServlet{

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml;charset=GB2312");
        response.setBufferSize(0);
        //生成xml格式
        StringBuffer xml = new StringBuffer();
        xml.append("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
        xml.append("<CFX>");
        Map msgs = Monitor.getInstance().getRecentMsgs();
        for(Iterator itr = msgs.keySet().iterator();itr.hasNext();) {
            Integer key = (Integer) itr.next();
            String value = (String) msgs.get(key);
            xml.append("<group id=\"").append(key).append("\">");
            xml.append("<![CDATA[\n");
            xml.append(value);
            xml.append("]]>");
            xml.append("</group>");
        }
        xml.append("</CFX>");
        response.getWriter().println(xml);
        response.flushBuffer();
    }
}
