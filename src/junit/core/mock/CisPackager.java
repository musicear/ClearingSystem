package junit.core.mock;

import resoft.xlink.comm.PackException;
import resoft.xlink.comm.Packager;
import resoft.xlink.core.Message;

/**
 * <p></p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 10:27:46
 */
public class CisPackager implements Packager {
    /**
     * to Message
     */
    public Message unpack(byte[] bytes) throws PackException {
        return null;
    }

    /**
     * Message ---->   byte[]
     */
    public byte[] pack(Message msg) {
        return new byte[0];
    }
}
