package resoft.xlink.comm;

import resoft.xlink.core.Message;

/**
 * <p>���Ľ�����</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 4:38:17
 */
public interface Packager {
    /**
     * to Message
     * */
    Message unpack(byte[] bytes) throws PackException;
    /**
     * Message ---->   byte[]
     * */
    byte[] pack(Message msg);
}
