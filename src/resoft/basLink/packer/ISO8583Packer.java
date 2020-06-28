package resoft.basLink.packer;

import java.util.ArrayList;
import java.util.List;

import resoft.basLink.Message;
import resoft.basLink.PackException;
import resoft.basLink.Packer;

/**
 * function: 解析ISO8583格式
 * User: albert lee
 * Date: 2005-10-27
 * Time: 21:28:17
 */
public class ISO8583Packer implements Packer {
    private static List elementList = null;

    static {
        elementList = new ArrayList();
        elementList.add(new ISO8583Element("PrimaryAccountNumber", 2, ISO8583Element.DataType_Char, 0));
        elementList.add(new ISO8583Element("ProcessingCode", 0, ISO8583Element.DataType_Char, 6));
        elementList.add(new ISO8583Element("TransactionAmount", 0, ISO8583Element.DataType_Number, 12));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("TransactionDateTime", 0, ISO8583Element.DataType_DateTime, 10));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("SystemTraceAuditNumber", 0, ISO8583Element.DataType_Number, 6));
        elementList.add(new ISO8583Element("LocalTransactionTime", 0, ISO8583Element.DataType_Char, 6));
        elementList.add(new ISO8583Element("LocalTransactionDate", 0, ISO8583Element.DataType_Char, 4));
        elementList.add(new ISO8583Element("ExpirationDate", 0, ISO8583Element.DataType_Char, 4));
        elementList.add(new ISO8583Element("SettlementDate", 0, ISO8583Element.DataType_Char, 4));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("CaptureDate", 0, ISO8583Element.DataType_Char, 4));
        elementList.add(new ISO8583Element("MerchantType", 0, ISO8583Element.DataType_Char, 4));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("PointOfServiceEntryMode", 0, ISO8583Element.DataType_Char, 3));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("PointOfServiceConditionCode", 0, ISO8583Element.DataType_Char, 2));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("Field27", 0, ISO8583Element.DataType_Char, 6));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("ACQUIRER INSTITUTION ID. CODE", 2, ISO8583Element.DataType_Char, 0));
        elementList.add(new ISO8583Element("FORWARDING INSTITUTION ID. CODE", 2, ISO8583Element.DataType_Char, 0));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("Track2Data", 2, ISO8583Element.DataType_Char, 0));
        elementList.add(new ISO8583Element("Trace3Data", 3, ISO8583Element.DataType_Char, 0));
        elementList.add(new ISO8583Element("ETRIEVAL REFERENCE NUMBER", 0, ISO8583Element.DataType_Char, 12));
        elementList.add(new ISO8583Element("AUTH. IDENTIFICATION RESPONSE", 0, ISO8583Element.DataType_Char, 6));
        elementList.add(new ISO8583Element("ResponseCode", 0, ISO8583Element.DataType_Char, 2));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("CARD ACCEPTOR TERMINAL ID", 0, ISO8583Element.DataType_Char, 8));
        elementList.add(new ISO8583Element("CARD ACCEPTOR IDENTIFICATION CODE", 0, ISO8583Element.DataType_Char, 15));
        elementList.add(new ISO8583Element("CARD ACCEPTOR NAME LOCATION", 0, ISO8583Element.DataType_Char, 40));
        elementList.add(new ISO8583Element("ADDITIONAL RESPONSE DATA", 2, ISO8583Element.DataType_Char, 25));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("Field47", 3, ISO8583Element.DataType_Char, 999));
        elementList.add(new ISO8583Element("ADDITIONAL DATA PRIVATE", 3, ISO8583Element.DataType_Char, 0));
        elementList.add(new ISO8583Element("TransactionCurrencyCode", 0, ISO8583Element.DataType_Char, 3));
        elementList.add(new ISO8583Element("SettlementCurrencyCode", 0, ISO8583Element.DataType_Char, 3));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("PERSONAL IDENTIFICATION NUMBER DATA", 0, ISO8583Element.DataType_Char, 8));
        elementList.add(new ISO8583Element("SecurityRelatedControlInformation", 0, ISO8583Element.DataType_Char, 16));
        elementList.add(new ISO8583Element("AdditionalAmounts", 0, ISO8583Element.DataType_Char, 3));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("MessageAuthenticationCode", 0, ISO8583Element.DataType_Char, 8));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("SystemManagementInformationCode", 0, ISO8583Element.DataType_Char, 3));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("NumberOfCredits", 0, ISO8583Element.DataType_Char, 10));
        elementList.add(new ISO8583Element("ReversalNumberOfCredits", 0, ISO8583Element.DataType_Char, 10));
        elementList.add(new ISO8583Element("NumberOfDebits", 0, ISO8583Element.DataType_Char, 10));
        elementList.add(new ISO8583Element("ReversalNumberOfDebits", 0, ISO8583Element.DataType_Char, 10));
        elementList.add(new ISO8583Element("NumberOfTransfer", 0, ISO8583Element.DataType_Char, 10));
        elementList.add(new ISO8583Element("ReversalNumberOfTransfer", 0, ISO8583Element.DataType_Char, 10));
        elementList.add(new ISO8583Element("NumberOfInquirs", 0, ISO8583Element.DataType_Char, 10));
        elementList.add(new ISO8583Element("AuthorizationNumber", 0, ISO8583Element.DataType_Char, 10));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("CREDITS,TRANSCATION FEEAMOUNT", 0, ISO8583Element.DataType_Char, 12));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("DEBITS,TRANSCATION FEEAMOUNT", 0, ISO8583Element.DataType_Char, 12));
        elementList.add(new ISO8583Element("AMOUNT OF CREDITS", 0, ISO8583Element.DataType_Char, 16));
        elementList.add(new ISO8583Element("REVERSAL AMOUNT OF CREDITS", 0, ISO8583Element.DataType_Char, 16));
        elementList.add(new ISO8583Element("AMOUNT OF DEBITS", 0, ISO8583Element.DataType_Char, 16));
        elementList.add(new ISO8583Element("REVERSAL AMOUNT OF DEBITS", 0, ISO8583Element.DataType_Char, 16));
        elementList.add(new ISO8583Element("ORIGINAL DATA ELEMENTS", 0, ISO8583Element.DataType_Char, 42));
        elementList.add(new ISO8583Element("FileUpdateCode", 0, ISO8583Element.DataType_Char, 1));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("ServiceIndicator", 0, ISO8583Element.DataType_Char, 7));
        elementList.add(new ISO8583Element("ReplacementAmounts", 0, ISO8583Element.DataType_Char, 42));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("AMOUNT OF NET SETTLEMENT", 0, ISO8583Element.DataType_Char, 16));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("SETTLEMENT INSTITUTION ID", 2, ISO8583Element.DataType_Char, 11));
        elementList.add(new ISO8583Element("RECVEING INSTITUTION ID", 2, ISO8583Element.DataType_Char, 11));
        elementList.add(new ISO8583Element("FileName", 2, ISO8583Element.DataType_Char, 17));
        elementList.add(new ISO8583Element("ACCOUNT IDENTIFICATION1", 2, ISO8583Element.DataType_Char, 28));
        elementList.add(new ISO8583Element("ACCOUNT IDENTIFICATION2", 2, ISO8583Element.DataType_Char, 28));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("NewPinData", 3, ISO8583Element.DataType_Char, 8));
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(ISO8583Element.ReservedElement);
        elementList.add(new ISO8583Element("MessageAuthenticationCode",0,ISO8583Element.DataType_Char,8));//@todo 此处似乎要进行MAC计算，以避免窜改

    }

    public Message pack(byte[] packet) throws PackException {
        int currentPos = 8;
        int bits = 64;
        boolean usingExtend = (byte)(packet[0] >>> 7) == (byte)(-1);
        if(usingExtend) {
            bits = 128;
            currentPos = 16;
        }
        Message msg = new Message();
        for (int i = 1; i < bits; i++) {
            byte header = packet[i / 8];
            header = (byte) (header << (i % 8));
            byte mark = (byte) (header >>> 7);
            if (mark == -1) {
                //此位存在
                ISO8583Element element = (ISO8583Element) elementList.get(i - 1);
                if (element == ISO8583Element.ReservedElement) {
                    throw new PackException("第" + i + "位应是保留位");
                }
                int length = 0;
                if (element.getFlag() == 0) {
                    //定长部分
                    length = element.getLength();
                } else {
                    //变长部分
                    if (element.getFlag() == 2) {
                        length = packet[currentPos] * 10 + packet[currentPos + 1];
                    } else {
                        length = packet[currentPos] * 100 + packet[currentPos + 1] * 10 + packet[currentPos + 2];
                    }
                    currentPos += element.getFlag();
                }
                String value = new String(packet, currentPos, length);
                currentPos += length;
                msg.setValue(element.getName(), value);
            }
        }
        return msg;
    }

    public byte[] unpack(Message msg) {
        boolean[] usedElements = new boolean[128];
        StringBuffer packet = new StringBuffer();
        boolean usingExtend = false;
        for(int i=0;i<127;i++) {
            ISO8583Element element = (ISO8583Element) elementList.get(i);
            String name = element.getName();
            String value = msg.getValue(name);
            if(value==null || value.equals("")) {
                usedElements[i + 1] = false;
                continue;
            }
            if(!usingExtend && i==64) {
                usingExtend = true;
            }
            if(element.getFlag()==0) {
                //定长部分
                if(value.length()!=element.getLength()) {
                    throw new PackException("This element's length is wrong:" + name);
                }

            } else {
                //变长部分
                packet.append(value.length());
            }
            packet.append(value);
            usedElements[i + 1] = true;
        }
        usedElements[0] = usingExtend;

        //构造位图
        int usedBytes = 0;
        if(usingExtend) {
            usedBytes = 16;
        } else {
            usedBytes = 8;
        }
        byte[] bitMap = new byte[usedBytes];
        for(int i=0;i<usedBytes;i++) {
            for(int bitPos=0;bitPos<8;bitPos++) {
                if(usedElements[i * 8 + bitPos]) {
                    //bitMap[i] += 2 ^ (7 - bitPos);
                    bitMap[i] += 1<<(7 - bitPos);
                }
            }
        }
        byte[] result = new byte[usedBytes + packet.toString().getBytes().length];
        System.arraycopy(bitMap,0,result,0,usedBytes);
        System.arraycopy(packet.toString().getBytes(),0,result,usedBytes,packet.toString().length());
        return result;
    }

    public byte[] unpackByFile(Message msg) throws PackException {

        return new byte[0];
    }

    public byte[] unpackByFile(Message msg, String filePath) throws PackException {
        return new byte[0];
    }
}
