package resoft.junit;

import junit.framework.TestCase;
import resoft.basLink.Message;
import resoft.basLink.PackException;
import resoft.basLink.Packer;
import resoft.basLink.packer.ISO8583Packer;

/**
 * function:
 * User: albert lee
 * Date: 2005-10-27
 * Time: 21:35:27
 */
public class TestISO8583Packer extends TestCase{
    private Packer packer = null;
    public void setUp() {
        packer = new ISO8583Packer();
    }
    public void testLeftShift() {
        assertEquals(0x80,0x40<<1);
    }
    public void testXOR() {
        //byte a = 0x40;
        //byte result = (byte) (a ^ 0xFF);
        assertEquals(0x01,0x01 ^ 0x00);
        assertEquals(0x00,0x00^0x00);
    }
    public void testOnlyPrimaryAccountNumber() {
        byte[] packet = {0x40,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x03,'3','9','8'};
        Message msg = packer.pack(packet);
        assertEquals("398",msg.getValue("PrimaryAccountNumber"));
    }
    public void testOnlyProcessingCode() {
        byte[] packet = {0x20,0x00,0x00,0x00,0x00,0x00,0x00,0x00,'8','2','3','4','1','9'};
        Message msg = packer.pack(packet);
        assertEquals("823419",msg.getValue("ProcessingCode"));
    }
    public void testPrimaryAccountNumberAndProcessingCode() {
        byte[] packet = {0x60,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,'9','9','0','1','0','0','0'};
        Message msg = packer.pack(packet);
        assertEquals("9",msg.getValue("PrimaryAccountNumber"));
        assertEquals("901000",msg.getValue("ProcessingCode"));
    }
    public void testTransactionDateTime() {
        byte[] packet = {0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,'0','5','1','0','2','6','2','3','0','8'};
        Message msg = packer.pack(packet);
        assertEquals("0510262308",msg.getValue("TransactionDateTime"));
    }
    public void testNotUseElement() {
        byte[] packet = {0x08,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
        try {
            Message msg = packer.pack(packet);
            fail("it must throws exception");
        } catch(PackException e) {

        }

    }
    public void testMessageAuthenticationCode() {
        byte[] packet = {(byte) 0x80,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,'8','9','7','1','6','6','6','6'};
        Message msg = packer.pack(packet);
        assertEquals("89716666",msg.getValue("MessageAuthenticationCode"));
    }
    public void testPackTo8583() {
        byte[] packet = {0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,'0','5','1','0','2','6','2','3','0','8'};
        Message msg = new Message();
        msg.setValue("TransactionDateTime","0510262308");
        //assertEquals(packet,packer.unpack(msg));
        byte[] result = packer.unpack(msg);
        for(int i=0;i<packet.length;i++) {
            if(result[i]!=packet[i]) {
                fail("Wrong");
            }
        }
    }
}
