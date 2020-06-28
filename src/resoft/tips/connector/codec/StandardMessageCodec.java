// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StandardMessageCodec.java

package resoft.tips.connector.codec;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.xml.sax.InputSource;

import resoft.tips.connector.codec.config.ConfigReader;
import resoft.tips.connector.codec.config.MessageElement;
import resoft.tips.connector.codec.config.MessageTemplate;
import resoft.tips.connector.codec.config.MessageTemplateBuilder;
import resoft.tips.connector.codec.converter.BigDecimalTypeConverter;
import resoft.tips.connector.codec.converter.DatetimeTypeConverter;
import resoft.tips.connector.codec.converter.IntTypeConverter;
import resoft.tips.connector.codec.converter.StringTypeConverter;
import resoft.tips.connector.codec.converter.TypeConverter;
import resoft.tips.connector.codec.converter.VarStringTypeConverter;
import resoft.tips.connector.codec.util.StringUtil;
import resoft.tips.connector.message.ReturnMessage;
import resoft.xlink.comm.helper.Controller;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

public class StandardMessageCodec
{

	private static final Logger logger;
	private static Map typeConverters;
	private Map txCodeNames;

	public StandardMessageCodec()
	{
		txCodeNames = new HashMap();
	}

	public ReturnMessage decodeTipsServerMessage(String code, String message)
		throws Exception
	{
		return decodeTipsMessage(code, message, false);
	}

	public ReturnMessage decodeTipsServerMessage(String code, Document doc)
		throws Exception
	{
		return decodeTipsMessage(code, doc, false);
	}

	public Message xmlReadDemo(Document doc)
	{
		Message msg = new DefaultMessage();
		List sys_head_list = doc.selectNodes("/service/sys-header/data/struct/data");
		List sys_head_field_list = doc.selectNodes("/service/sys-header/data/struct/data/field");
		for (int i = 0; i < sys_head_list.size(); i++)
			msg.set(((Element)sys_head_list.get(i)).attribute("name").getValue(), ((Element)sys_head_field_list.get(i)).getText());

		List app_header_list = doc.selectNodes("/service/app-header/data/struct/data");
		List app_header_field_list = doc.selectNodes("/service/app-header/data/struct/data/field");
		for (int i = 0; i < app_header_list.size(); i++)
			msg.set(((Element)app_header_list.get(i)).attribute("name").getValue(), ((Element)app_header_field_list.get(i)).getText());

		List local_header_list = doc.selectNodes("/service/local-header/data/struct/data");
		List local_header_field_list = doc.selectNodes("/service/app-header/data/struct/data/field");
		for (int i = 0; i < local_header_list.size(); i++)
			msg.set(((Element)local_header_list.get(i)).attribute("name").getValue(), ((Element)local_header_field_list.get(i)).getText());

		List body_list = doc.selectNodes("/service/body/data");
		List body_field_list = doc.selectNodes("/service/body/data/field");
		for (int i = 0; i < body_list.size(); i++)
			msg.set(((Element)body_list.get(i)).attribute("name").getValue(), ((Element)body_field_list.get(i)).getText());

		return msg;
	}

	private ReturnMessage decodeTipsMessage(String code, Document doc, boolean isClient)
		throws Exception
	{
		ReturnMessage request = new ReturnMessage();
		logger.info("doc to message:" + new Date());
		Message msg = xmlReadDemo(doc);
		logger.info("doc to message end:" + new Date());
		if (code == null)
			code = msg.getString("MESSAGE_CODE");
		request.setCode(code);
		msg.set("交易码", code);
		logger.info("controller start:" + new Date());
		Controller controller = new Controller();
		controller.setNameOfTransCode("交易码");
		logger.info("*******************:" + new Date());
		controller.execute(msg);
		logger.info("*******************:" + new Date());
		request.setDetails(msg);
		return request;
	}

	private ReturnMessage decodeTipsMessage(String code, String message, boolean isClient)
		throws Exception
	{
		logger.debug("解码:" + message);
		ReturnMessage request = new ReturnMessage();
		Message msg = new DefaultMessage();
		StringReader sr = new StringReader(message);
		InputSource is = new InputSource(sr);
		ConfigReader cReader = new ConfigReader(is);
		List sys_head_list = cReader.getNodeList("/service/sys-header/data/struct/data");
		Node node = null;
		for (int i = 0; i < sys_head_list.size(); i++)
		{
			node = (Node)sys_head_list.get(i);
			msg.set(cReader.getStringValue(node, "name"), cReader.getStringValue(node, "field"));
		}

		if (code == null)
			code = msg.getString("交易码");
		request.setCode(code);
		Controller controller = new Controller();
		controller.setNameOfTransCode("交易码");
		controller.execute(msg);
		Map head = new HashMap();
		head.put("交易码", request.getCode());
		head.put("RET_STATUS", msg.get("ReturnResult"));
		head.put("RET_MSG", msg.get("AddWord"));
		request.setHead(head);
		request.setDetails(msg);
		return request;
	}

	public String encodeServerMessage(String code, ReturnMessage response)
		throws Exception
	{
		StringBuffer message = new StringBuffer();
		message.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		message.append("<service>");
		String templateFileName = code + "_back";
		MessageTemplate msgTemplate = MessageTemplateBuilder.getMessageTemplate(templateFileName);
		String ss = "";
		try
		{
			ss = buildResponse(message, msgTemplate, response);
		}
		catch (Exception e)
		{
			logger.error(e);
			message = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"        ?><service>");
		}
		logger.info("编码:" + message + ss + "</service>");
		logger.info("endEncodeMessageTime:" + new Date());
		return message + ss + "</service>";
	}

	public String encodeServerMessage(String code, ReturnMessage response, Document doc)
		throws Exception
	{
		StringBuffer message = new StringBuffer();
		message.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		try
		{
			String templateFileName = code + "_back";
			MessageTemplate msgTemplate = MessageTemplateBuilder.getMessageTemplate(templateFileName);
			buildResponse(message, msgTemplate, response);
		}
		catch (Exception e)
		{
			logger.error(e);
			message = new StringBuffer();
			buildFailureResponse(message, code);
		}
		logger.debug("编码:" + message);
		return message.toString();
	}

	public String encodeServerMessage2(Document doc, String code, ReturnMessage response)
		throws Exception
	{
		try
		{
			String templateFileName = code + "_back";
			MessageTemplate msgTemplate = MessageTemplateBuilder.getMessageTemplate(templateFileName);
			buildResponse2(doc, msgTemplate, response);
		}
		catch (Exception e)
		{
			logger.error(e);
		}
		return doc.asXML().toString();
	}

	private void buildResponse2(Document doc, MessageTemplate template, ReturnMessage response)
	{
		buildTipsHeader2(doc, template, response.getDetails());
		buildTipsBody2(doc, template, response.getDetails());
	}

	private void buildTipsHeader2(Document doc, MessageTemplate template, Message msg)
	{
		List sys_head_list = doc.selectNodes("/service/sys-header/data/struct/data");
		List sys_head_field_list = doc.selectNodes("/service/sys-header/data/struct/data/field");
		for (int i = 0; i < sys_head_list.size(); i++)
			if (((Element)sys_head_list.get(i)).attribute("name").getValue().equalsIgnoreCase("MESSAGE_TYPE"))
				((Element)sys_head_field_list.get(i)).setText((new StringBuffer(String.valueOf(Integer.parseInt(msg.getString("MESSAGE_TYPE")) + 10))).toString());
			else
			if (((Element)sys_head_list.get(i)).attribute("name").getValue().equalsIgnoreCase("RET_STATUS"))
				((Element)sys_head_field_list.get(i)).setText(msg.getString("RET_STATUS"));
			else
			if (((Element)sys_head_list.get(i)).attribute("name").getValue().equalsIgnoreCase("RET_CODE"))
				((Element)sys_head_field_list.get(i)).setText(msg.getString("RET_CODE"));
			else
			if (((Element)sys_head_list.get(i)).attribute("name").getValue().equalsIgnoreCase("RET_MSG"))
				((Element)sys_head_field_list.get(i)).setText(msg.getString("RET_MSG"));

	}

	private void buildTipsAppHeader2(Document doc, MessageTemplate template, Message msg)
	{
		List app_header_list = doc.selectNodes("/service/app-header/data/struct/data");
		List app_header_field_list = doc.selectNodes("/service/app-header/data/struct/data/field");
		for (int i = 0; i < app_header_list.size(); i++)
			if (((Element)app_header_list.get(i)).attribute("name").getValue().equalsIgnoreCase("CURRENT_NUM"))
				((Element)app_header_field_list.get(i)).setText(msg.getString("CURRENT_NUM"));
			else
			if (((Element)app_header_list.get(i)).attribute("name").getValue().equalsIgnoreCase("PAGE_START"))
				((Element)app_header_field_list.get(i)).setText(msg.getString("PAGE_START"));
			else
			if (((Element)app_header_list.get(i)).attribute("name").getValue().equalsIgnoreCase("PAGE_END"))
				((Element)app_header_field_list.get(i)).setText(msg.getString("PAGE_END"));
			else
			if (((Element)app_header_list.get(i)).attribute("name").getValue().equalsIgnoreCase("TOTAL_ROWS"))
				((Element)app_header_field_list.get(i)).setText(msg.getString("TOTAL_ROWS"));
			else
			if (((Element)app_header_list.get(i)).attribute("name").getValue().equalsIgnoreCase("TOTAL_PAGES"))
				((Element)app_header_field_list.get(i)).setText(msg.getString("TOTAL_PAGES"));

	}

	private void buildTipsBody2(Document doc, MessageTemplate template, Message msg)
	{
		if (msg.getString("RET_CODE").equals("000000"))
		{
			List body_list = doc.selectNodes("/service/body/data");
			for (int i = 0; i < body_list.size(); i++)
				doc.remove((Element)body_list.get(i));

			List elements = template.getDetailItems();
			List elecell = new ArrayList();
			List eleList = new ArrayList();
			List eleInfo = new ArrayList();
			for (int i = 0; i < elements.size(); i++)
			{
				MessageElement element = (MessageElement)elements.get(i);
				if (element.getDataSource().equalsIgnoreCase("list"))
					eleList.add(element);
				else
				if (element.getValue().length() > 0)
					eleInfo.add(element);
				else
					elecell.add(element);
			}

			for (int i = 0; i < elecell.size(); i++)
			{
				Element e = doc.addElement("/service/body/data");
				MessageElement element = (MessageElement)elecell.get(i);
				String ptName = element.getPtName();
				if (StringUtil.isEmpty(ptName))
					ptName = element.getName();
				e.setName(ptName);
				Element e2 = doc.addElement("/service/body/data/field");
				e2.attributeValue("type", element.getDataType());
				e2.attributeValue("length", (new StringBuffer(String.valueOf(element.getLength()))).toString());
				e2.setText(msg.getString(ptName) != null ? msg.getString(ptName) : "");
			}

			if (eleList != null && eleList.size() > 0)
			{
				MessageElement element = (MessageElement)eleList.get(0);
				String ptName = element.getPtName();
				if (StringUtil.isEmpty(ptName))
					ptName = element.getName();
				List infoList = (List)msg.get(ptName);
				if (infoList != null && infoList.size() > 0)
				{
					Element e = doc.addElement("/service/body/data");
					e.setName(ptName);
					Element e2 = doc.addElement("/service/body/data/array");
					for (int j = 0; j < infoList.size(); j++)
					{
						Element e3 = doc.addElement("/service/body/data/array/struts");
						Map map = (Map)infoList.get(j);
						for (int k = 0; k < eleInfo.size(); k++)
						{
							MessageElement me = (MessageElement)eleInfo.get(k);
							if (me.getValue().equalsIgnoreCase(ptName))
							{
								String mePtName = me.getPtName();
								if (StringUtil.isEmpty(mePtName))
									mePtName = me.getName();
								Element e4 = doc.addElement("/service/body/data/array/struts/data");
								e4.setName(mePtName);
								Element e5 = doc.addElement("/service/body/data/array/struts/data/field");
								e5.attributeValue("type", me.getDataType());
								e5.attributeValue("length", (new StringBuffer(String.valueOf(me.getLength()))).toString());
								e5.setText(map.get(mePtName) != null ? map.get(mePtName).toString() : "");
							}
						}

					}

				}
			}
		}
	}

	public String encodeClientMessage(String code, ReturnMessage response)
		throws Exception
	{
		StringBuffer message = new StringBuffer();
		message.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		String templateFileName = code;
		MessageTemplate msgTemplate = MessageTemplateBuilder.getMessageTemplate(templateFileName);
		buildResponse(message, msgTemplate, response);
		logger.debug("编码:" + message);
		return message.toString();
	}

	private String buildResponse(StringBuffer message, MessageTemplate template, ReturnMessage response)
	{
		StringBuffer sysheader = buildTipsHeader(message, template, response.getDetails());
		StringBuffer appheader = buildTipsAppHeader(message, template, response.getDetails());
		StringBuffer body = buildTipsBody(message, template, response.getDetails());
		return sysheader.toString() + appheader.toString() + body.toString();
	}

	private StringBuffer buildTipsHeader(StringBuffer sb, MessageTemplate template, Message msg)
	{
		try
		{
			sb = new StringBuffer();
			sb.append("<sys-header>");
			sb.append("<data name='SYS_HEAD'>");
			sb.append("<struct>");
			List elements = template.getMainItems();
			boolean flag = false;
			for (int i = 0; i < elements.size(); i++)
			{
				MessageElement element = (MessageElement)elements.get(i);
				if (element.getValue().equals(""))
				{
					sb.append("<data ");
					String ptName = element.getPtName();
					if (StringUtil.isEmpty(ptName))
						ptName = element.getName();
					sb.append(" name='").append(ptName).append("' >");
					if (ptName.equalsIgnoreCase("RET"))
					{
						sb.append("<array type='array'>");
						sb.append("<struct>");
						flag = true;
					} else
					{
						sb.append("<field ").append(" type='").append(element.getDataType()).append("' ").append(" length='").append(element.getLength()).append("'>");
						if (ptName.equalsIgnoreCase("MESSAGE_TYPE") && msg.get(ptName) != null && !"".equals(msg.getString(ptName).trim()))
							sb.append((new StringBuffer(String.valueOf(Integer.parseInt(msg.getString(ptName)) + 10))).toString());
						else
							sb.append(msg.get(ptName) != null ? msg.getString(ptName) : "");
						sb.append("</field>");
						sb.append("</data>");
					}
				}
			}

			if (flag)
				sb.append("</struct></array></data>");
			sb.append("</struct>");
			sb.append("</data>");
			sb.append("</sys-header>");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb;
	}

	private StringBuffer buildTipsAppHeader(StringBuffer sb, MessageTemplate template, Message msg)
	{
		sb = new StringBuffer();
		sb.append("<app-header>");
		sb.append("<data name='APP_HEAD'>");
		sb.append("<struct>");
		List elements = template.getMainItems();
		for (int i = 0; i < elements.size(); i++)
		{
			MessageElement element = (MessageElement)elements.get(i);
			if (element.getValue().equals("page"))
			{
				sb.append("<data ");
				String ptName = element.getPtName();
				if (StringUtil.isEmpty(ptName))
					ptName = element.getName();
				sb.append(" name='").append(ptName).append("' >");
				sb.append("<field ").append(" type='").append(element.getDataType()).append("' ").append(" length='").append(element.getLength()).append("'>");
				sb.append(msg.get(ptName) != null ? msg.getString(ptName) : "");
				sb.append("</field>");
				sb.append("</data>");
			}
		}

		sb.append("</struct>");
		sb.append("</data>");
		sb.append("</app-header>");
		return sb;
	}

	private StringBuffer buildTipsBody(StringBuffer sb, MessageTemplate template, Message msg)
	{
		try
		{
			sb = new StringBuffer();
			if (msg.get("RET_CODE") != null && msg.getString("RET_CODE").equals("000000"))
			{
				sb.append("<body>");
				List elements = template.getDetailItems();
				List elecell = new ArrayList();
				List eleList = new ArrayList();
				List eleInfo = new ArrayList();
				for (int i = 0; i < elements.size(); i++)
				{
					MessageElement element = (MessageElement)elements.get(i);
					if (element.getDataSource().equalsIgnoreCase("list") && element.getValue().equals(""))
						eleList.add(element);
					else
					if (element.getValue().length() > 0)
						eleInfo.add(element);
					else
						elecell.add(element);
				}

				for (int i = 0; i < elecell.size(); i++)
				{
					MessageElement element = (MessageElement)elecell.get(i);
					String ptName = element.getPtName();
					if (StringUtil.isEmpty(ptName))
						ptName = element.getName();
					sb.append("<data ");
					sb.append(" name='").append(ptName).append("' >");
					sb.append("<field ").append(" type='").append(element.getDataType()).append("' ").append(" length='").append(element.getLength()).append("'>");
					sb.append(msg.get(ptName) != null ? msg.getString(ptName) : "");
					sb.append("</field>");
					sb.append("</data>");
				}

				for (int i = 0; i < eleList.size(); i++)
				{
					MessageElement element = (MessageElement)eleList.get(i);
					String ptName = element.getPtName();
					if (StringUtil.isEmpty(ptName))
						ptName = element.getName();
					List infoList = (List)msg.get(ptName);
					if (infoList != null && infoList.size() > 0)
						buildTipsBodyInfo(sb, infoList, ptName, eleInfo, 0);
				}

				sb.append("</body>");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb;
	}

	private void buildTipsBodyInfo(StringBuffer sb, List infoList, String ptName, List eleInfo, int t)
	{
		sb.append("<data ");
		sb.append(" name='").append(ptName).append("' >");
		sb.append("<array>");
		for (int j = 0; j < infoList.size(); j++)
		{
			Map map = (Map)infoList.get(j);
			sb.append("<struct>");
			for (int k = t; k < eleInfo.size(); k++)
			{
				MessageElement me = (MessageElement)eleInfo.get(k);
				if (me.getValue().equalsIgnoreCase(ptName))
				{
					String mePtName = me.getPtName();
					if (StringUtil.isEmpty(mePtName))
						mePtName = me.getName();
					if (me.getDataSource().equals("map"))
					{
						sb.append("<data ");
						sb.append(" name='").append(mePtName).append("' >");
						sb.append("<field ").append(" type='").append(me.getDataType()).append("' ").append(" length='").append(me.getLength()).append("'>");
						sb.append(map.get(mePtName) != null ? map.get(mePtName).toString() : "");
						sb.append("</field>");
						sb.append("</data>");
					} else
					if (me.getDataSource().equals("list"))
					{
						List list = (List)map.get(mePtName);
						if (list != null && list.size() > 0)
							buildTipsBodyInfo(sb, list, mePtName, eleInfo, k + 1);
					}
				}
			}

			sb.append("</struct>");
		}

		sb.append("</array>");
		sb.append("</data>");
	}

	private void buildDetails(StringBuffer sb, MessageTemplate template, List objs)
	{
		if (objs == null || objs.size() == 0)
			return;
		sb.append("<details>");
		for (int i = 0; i < objs.size(); i++)
			if (template.getDetailItems() != null && template.getDetailItems().size() > 0)
			{
				sb.append("<mx><value>");
				for (int j = 0; j < template.getDetailItems().size(); j++)
				{
					MessageElement element = (MessageElement)template.getDetailItems().get(j);
					TypeConverter converter = (TypeConverter)typeConverters.get(element.getDataType());
					sb.append(converter.toString(objs.get(i), element));
				}

				sb.append("</value></mx>");
			}

		sb.append("</details>");
	}

	private String buildDetailsHRB(StringBuffer sb, MessageTemplate template, List objs)
	{
		if (objs == null || objs.size() == 0)
			return "";
		for (int i = 0; i < objs.size(); i++)
			if (template.getDetailItems() != null && template.getDetailItems().size() > 0)
			{
				for (int j = 0; j < template.getDetailItems().size(); j++)
				{
					MessageElement element = (MessageElement)template.getDetailItems().get(j);
					if (element.getDataType().equals("int"))
					{
						TypeConverter converter = (TypeConverter)typeConverters.get(element.getDataType());
						sb.append(converter.toString(objs.get(i), element));
					} else
					{
						sb.append("\"");
						TypeConverter converter = (TypeConverter)typeConverters.get(element.getDataType());
						sb.append(converter.toString(objs.get(i), element));
						sb.append("\"");
					}
					sb.append("~");
				}

				sb.append("\n");
			}

		return sb.toString();
	}

	private void buildBody(StringBuffer sb, MessageTemplate template, Map headers)
	{
		sb.append("<bob>");
		sb.append("<pub>");
		List elements = template.getMainItems();
		for (int i = 0; i < elements.size(); i++)
		{
			MessageElement element = (MessageElement)elements.get(i);
			sb.append("<key>");
			String ptName = element.getPtName();
			if (StringUtil.isEmpty(ptName))
				ptName = element.getName();
			sb.append("<name>").append(ptName).append("</name>");
			TypeConverter converter = (TypeConverter)typeConverters.get(element.getDataType());
			sb.append("<value>").append(converter.toString(headers, element)).append("</value>");
			sb.append("</key>");
		}

		sb.append("</pub>");
		sb.append("</bob>");
	}

	protected void buildFailureResponse(StringBuffer sb, String responseCode)
	{
		sb.append("<bob>");
		sb.append("<pub><key>RESPONSECODE</key>");
		sb.append("<value>" + responseCode + "</value></pub>");
		sb.append("</bob>");
	}

	private Map unbuildBody(MessageTemplate template, Map headers)
	{
		Map results = new HashMap();
		List elements = template.getMainItems();
		logger.debug("主报文===========================");
		for (int i = 0; i < elements.size(); i++)
		{
			MessageElement element = (MessageElement)elements.get(i);
			TypeConverter converter = (TypeConverter)typeConverters.get(element.getDataType());
			String ptName = element.getPtName();
			if (StringUtil.isEmpty(ptName))
				ptName = element.getName();
			Object elementValue = converter.valueOf((String)headers.get(ptName), element);
			String elementName = element.getName();
			results.put(elementName, elementValue);
			logger.debug("\t" + element.getDescription() + ":\t" + (String)headers.get(element.getName().toUpperCase()));
		}

		return results;
	}

	private Map unbuildDetail(MessageTemplate template, String message)
	{
		byte bytes[] = message.getBytes();
		int bLength = 0;
		MessageElement element = null;
		Map map = new HashMap();
		for (int i = 0; i < template.getDetailItems().size(); i++)
		{
			element = (MessageElement)template.getDetailItems().get(i);
			String value = substring(bytes, bLength, element.getLength()).trim();
			TypeConverter converter = (TypeConverter)typeConverters.get(element.getDataType());
			map.put(element.getName(), converter.valueOf(value, element));
			bLength += element.getLength();
			logger.debug("\t" + element.getDescription() + ":\t" + value);
		}

		return map;
	}

	private String substring(byte source[], int start, int length)
	{
		byte results[] = new byte[length];
		System.arraycopy(source, start, results, 0, length);
		return new String(results);
	}

	public String getTxCode(Map request)
	{
		String code = null;
		for (Iterator txcodeNames = txCodeNames.keySet().iterator(); txcodeNames.hasNext();)
		{
			String codeName = (String)txcodeNames.next();
			code = (String)request.get(codeName);
			if (code != null && !"".equals(code))
				break;
		}

		return code;
	}

	public void setTxCodeName(Map codeName)
	{
		txCodeNames = codeName;
	}

	static 
	{
		logger = Logger.getLogger(resoft.tips.connector.codec.StandardMessageCodec.class);
		typeConverters = new HashMap();
		typeConverters.put("int", new IntTypeConverter());
		typeConverters.put("bigdecimal", new BigDecimalTypeConverter());
		typeConverters.put("datetime", new DatetimeTypeConverter());
		typeConverters.put("string", new StringTypeConverter());
		typeConverters.put("varstring", new VarStringTypeConverter());
	}
}