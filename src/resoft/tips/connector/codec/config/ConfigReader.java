// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConfigReader.java

package resoft.tips.connector.codec.config;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class ConfigReader
{

	private SAXReader saxReader;
	private Document doc4j;

	public ConfigReader(String uri)
		throws DocumentException
	{
		saxReader = new SAXReader();
		doc4j = saxReader.read(uri);
	}

	public ConfigReader(InputStream is)
		throws DocumentException
	{
		saxReader = new SAXReader();
		doc4j = saxReader.read(is);
	}

	public ConfigReader(InputSource is)
		throws DocumentException
	{
		saxReader = new SAXReader();
		doc4j = saxReader.read(is);
	}

	public ConfigReader(File file)
		throws DocumentException, MalformedURLException
	{
		saxReader = new SAXReader();
		doc4j = saxReader.read(file);
	}

	public String getStringValue(String xpath)
	{
		Node node = doc4j.selectSingleNode(xpath);
		if (node == null)
			return "";
		else
			return node.getStringValue();
	}

	public String getStringValue(Node node, String xpath)
	{
		if (node == null)
			return "";
		Node node1 = node.selectSingleNode(xpath);
		if (node1 == null)
			return "";
		else
			return node1.getStringValue();
	}

	public boolean getBooleanValue(String xpath)
	{
		String v = getStringValue(xpath);
		return v.toUpperCase().equals("TRUE") || v.equals("1");
	}

	public boolean getBooleanValue(Node node, String xpath)
	{
		String v = getStringValue(node, xpath);
		return v.toUpperCase().equals("TRUE") || v.equals("1");
	}

	public List getNodeList(String xpath)
	{
		return doc4j.selectNodes(xpath);
	}

	public List getNodeList(Node node, String xpath)
	{
		return node.selectNodes(xpath);
	}
}