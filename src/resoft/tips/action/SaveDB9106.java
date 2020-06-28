package resoft.tips.action;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>运行参数通知保存</p>
 * Author: liwei
 * Date: 2007-07-18
 * Time: 14:06:06
 */
public class SaveDB9106 implements Action {
    

    public int execute(Message msg) throws Exception {
                
        Map params=new HashMap();
        String filePath = msg.getString("批量文件");
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filePath), "GBK");

        List packTags;
        packTags = new ArrayList() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
                add("ParamTypeNo");
                add("Description");
            }
        };

        List packDetailTags;
        packDetailTags = new ArrayList(){
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
                add("DetailNo");
                add("Description");
                add("ParamValue");
            }
        };

        String localName = "",ParamTypeNo="";
        int packCount=0;        
        for (int event = reader.next(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    localName = reader.getLocalName();
                    if (localName.equals("ParamTypeList9106")) {
                    	ParamTypeNo="";
                    	params.clear();                    	
                    	packCount=0;
                    }
                    //保存主表信息
                    if (localName.equals("ParamList9106")  && packCount == 0 ) {
                    	DBUtil.insert("ParamTypeMng",params);                    	
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:                	
                    String text = reader.getText().trim();
                    if (!text.equals("")) {
                        if (packTags.contains(localName) || packDetailTags.contains(localName) ) {
                            params.put(localName, text);
                        } 
                        if (localName.equals("ParamTypeNo")) {
                        	ParamTypeNo=text;                        	
                        }                        
                    }                    
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    localName = reader.getLocalName();
                    //保存明细信息
                    if (localName.equals("ParamList9106")){
                    	params.put("ParamTypeNo",ParamTypeNo);
                    	DBUtil.insert("ParamDetailMng",params);
                    	packCount++;
                    }
                    break;
            }
        }
              
        return SUCCESS;
    }
}