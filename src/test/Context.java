package test;
import java.util.HashMap;
import java.util.Map;

import resoft.xlink.comm.Packager;
import resoft.xlink.comm.Protocol;

public class Context
{

    public Context()
    {
        properties = new HashMap();
    }

    public Protocol getProtocol()
    {
        return (Protocol)getProperty("protocol");
    }

    public void setProtocol(Protocol protocal)
    {
        setProperty("protocol", protocal);
    }

    public Packager getPacker()
    {
        return (Packager)getProperty("paker");
    }

    public void setPacker(Packager packager)
    {
        setProperty("paker", packager);
    }

    public String getRequestId()
    {
        return (String)getProperty("requestId");
    }

    public void setRequestId(String id)
    {
        setProperty("requestId", id);
    }

    public Object getProperty(String name)
    {
        return properties.get(name);
    }

    public void setProperty(String name, Object value)
    {
        properties.put(name, value);
    }

    private static final String Property_Protocol = "protocol";
    private static final String Property_Packer = "paker";
    private static final String Property_RequestID = "requestId";
    private Map properties;
}
