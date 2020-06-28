package resoft.basLink.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import resoft.xlink.comm.PackException;

/**
 * <p>����Unpackʱ�ĸ�ʽ����</p>
 * User: liguoyin
 * Date: 2007-3-24
 * Time: 23:28:09
 */
public class UnpackFormatCache  extends AbstractCacheTemplate{
    private static SAXParserFactory spf = null;
    //private static SAXParser saxParser = null;

    static {
        //��ʼ��SAX������
        spf = SAXParserFactory.newInstance();

    }
    /**
     * ��һʵ��
     * */
    private static UnpackFormatCache instance = new UnpackFormatCache();
    private UnpackFormatCache() {
    }
    /**
     * �õ���Ψһʵ��
     * */
    public static UnpackFormatCache getInstance() {
        return instance;
    }
    /**
     * �õ���ʽ����
     * */
    public Map getFormat(String filePath) {
        return (Map) get(filePath);
    }
    /**
     * Cache���޴˸�ʽ���壬��ȡ�ļ�
     * */
    protected Object load(String filePath) throws Exception {
        Map map = new HashMap();

        String fmtFileContent;
        try {
            fmtFileContent = FilesCache.getInstance().getCfxFmtFile(filePath);
        } catch (Exception e) {
            throw new PackException("δ�ҵ����Ĵ���ļ�:" + filePath);
        }
        if(fmtFileContent ==null) {
            throw new PackException("����Ϊnull");
        }
        InputSource is = new InputSource(new StringReader(fmtFileContent));

        SAXParser saxParser;
        try {
            saxParser = spf.newSAXParser();
        } catch (Exception e) {
            throw e;
        }
        //SAX
        try {
            saxParser.parse(is,new FormatReader(map));
        } catch (Exception e) {
            throw new PackException("����XML����",e);
        }
        return map;
    }
    private class FormatReader extends DefaultHandler {
        FormatReader(Map map) {
            this.map = map;
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if(qName.equals("HEAD")) {
                currentSection = 1;
            } else if(qName.equals("MSG")) {
                currentSection = 2;
            } else if(qName.equals("PRI")) {
                currentSection = 3;
            } else {
                //Ԫ��
                switch (currentSection) {
                    case 1:
                        headList.add(qName);
                        break;
                    case 2:
                        msgList.add(qName);
                        break;
                    case 3:
                        priList.add(qName);
                        break;
                }
            }
        }

        public void endDocument() throws SAXException {
            map.put("head",headList);
            map.put("msg",msgList);
            map.put("pri",priList);
        }
        private Map map;
        private List headList = new ArrayList(),msgList = new ArrayList(),priList = new ArrayList();
        private int currentSection = 0;//��ʶ��ǰ�Ρ�HEAD:1   MSG:2   PRI:3
    }
}
