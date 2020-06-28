// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MessageTemplateBuilder.java

package resoft.tips.connector.codec.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Node;

// Referenced classes of package resoft.tips.connector.codec.config:
//			ConfigReader, MessageTemplate, MessageElement

public class MessageTemplateBuilder
{

	private static final Map templateMap = new HashMap();

	public MessageTemplateBuilder()
	{
	}

	private static MessageTemplate buildMessageTemplate(String tranCode)
		throws Exception
	{
		ConfigReader cReader = new ConfigReader(resoft.tips.connector.codec.config.MessageTemplateBuilder.class.getClassLoader().getResourceAsStream("connector/" + tranCode + ".xml"));
		List list = cReader.getNodeList("/head/messageElement");
		Node node = null;
		MessageTemplate mt = new MessageTemplate();
		mt.setMainItems(new ArrayList());
		mt.setDetailItems(new ArrayList());
		for (int i = 0; i < list.size(); i++)
		{
			node = (Node)list.get(i);
			MessageElement me = new MessageElement();
			me.setName(node.valueOf("@name"));
			me.setPtName(node.valueOf("@ptName"));
			me.setLength(Integer.parseInt(node.valueOf("@length")));
			me.setDataType(node.valueOf("@dataType"));
			me.setFormat(node.valueOf("@format"));
			me.setMandatory(node.valueOf("@mandatory"));
			me.setDataSource(node.valueOf("@dataSource"));
			me.setValue(node.valueOf("@value"));
			me.setType(node.valueOf("@type"));
			me.setDescription(node.valueOf("@description"));
			if (me.getType().equalsIgnoreCase("mb"))
				mt.getMainItems().add(me);
			else
				mt.getDetailItems().add(me);
		}

		return mt;
	}

	public static MessageTemplate getMessageTemplate(String tranCode)
		throws Exception
	{
		MessageTemplate mt = (MessageTemplate)templateMap.get(tranCode);
		if (mt == null)
		{
			mt = buildMessageTemplate(tranCode);
			templateMap.put(tranCode, mt);
		}
		return mt;
	}

}