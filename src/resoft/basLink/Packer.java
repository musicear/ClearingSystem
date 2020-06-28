package resoft.basLink;

/* Generated by Together */

public interface Packer {
    /**
     * to Message
     * */
    Message pack(byte[] m) throws PackException;
    /**
     * Message ---->   byte[]
     * */
    byte[] unpack(Message msg);

    /**
     * ��Message�еĽ�����õ����ĸ�ʽ�����ļ�
     * */
    public byte[] unpackByFile(Message msg) throws PackException;

    /**
     * ���ļ���ȡ�ñ��ĸ�ʽ�����ļ�
     * @param msg Message
     * @param filePath String   ��ʽ�����ļ�·����ָ����ClassPath�е����·��
     * */
    public byte[] unpackByFile(Message msg,String filePath) throws PackException;

    /** @link dependency */
    /*# resoft.basLink.Message lnkMessage; */
}