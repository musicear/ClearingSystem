package resoft.tips.chqsh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>��ѯ���ջ�������</p>
 * Author: liguoyin
 * Date: 2007-7-5
 * Time: 10:38:10
 */
public class QueryTaxOrgName implements Action {
    private static final Log logger = LogFactory.getLog(QueryTaxOrgName.class);
    public int execute(Message msg) throws Exception {
        String taxOrgCode = msg.getString("TaxOrgCode").trim();
        String taxOrgName = "";
        try {
        	String showmsg="select taxOrgName from taxOrgMng where taxOrgCode='" + taxOrgCode + "'";
            taxOrgName = DBUtil.queryForString(showmsg);
            logger.info("��ѯ���"+showmsg);
        } catch(Exception e) {
            logger.error("��ѯʧ��",e);
            msg.set("AddWord",e);
        }

        String returnResult = "N";
        if(taxOrgName!=null && !taxOrgName.equals("")) {
            returnResult = "Y";
            msg.set("AddWord","��ѯ�ɹ�");
        }
        else
        {
        	
        	msg.set("AddWord","�޴˻��ش���");
        }
        msg.set("ReturnResult",returnResult);
        msg.set("TaxOrgName",taxOrgName);
        return SUCCESS;
    }
}
