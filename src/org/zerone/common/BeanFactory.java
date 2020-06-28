package org.zerone.common;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class BeanFactory {
  private static Log logger = LogFactory.getLog(BeanFactory.class);

  private static Map beanMap = new HashMap();
  static {
    //读取配置文件
    InputStream is = BeanFactory.class.getResourceAsStream("/bean-config.xml");
    if (is == null) {
      logger.info("bean-config.xml not fond");
    } else {
      //read the cfg file
      SAXReader reader = new SAXReader();
      try {
        Document doc = reader.read(is);
        Element root = doc.getRootElement();
        if(root!=null) {
          Iterator itr = root.nodeIterator();
          while(itr.hasNext()) {
            Object o = itr.next();
            if(o instanceof Node) {
              Node bean = (Node)o;
              String id = bean.valueOf("@id");
              String className = bean.valueOf("@class");
              beanMap.put(id,className);
            }
          }
        }
      } catch (DocumentException e) {
        logger.info(e);
      }
    }
  }

  public static Object getBean(String objectId) {
    Object o = null;
    if (beanMap.containsKey(objectId)) {
      //建立新的实例
      try {
        o = Class.forName((String)beanMap.get(objectId)).
            newInstance();
      } catch (InstantiationException e) {
        logger.info(e);
      } catch (IllegalAccessException e) {
        logger.info(e);
      } catch (ClassNotFoundException e) {
        logger.info(e);
      }
      return o;
    } else {
      //未定义
      throw new ObjectNotFoundException();
    }
  }
}
