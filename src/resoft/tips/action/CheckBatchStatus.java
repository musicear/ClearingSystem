package resoft.tips.action;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>��������ۿ���Ϣ�Ŀۿ�״̬</p>
 * Author: liwei
 * Date: 2007-07-06
 * Time: 16:16:16
 */

public class CheckBatchStatus implements Action {

    private static final Log logger = LogFactory.getLog(CheckBatchStatus.class);

    public int execute(Message msg) throws Exception {

        Map params = (HashMap) msg.get("packFields");
        String stopType = (String) params.get("StopType");
        String oriPackNo = (String) params.get("OriPackNo");
        String oriTraNo = (String) params.get("OriTraNo");

        try {
            String sql = "",packProcFlag = "",packDetailProcFlag = "";
            String savedFlag = msg.getString("canAnsFlag");//�Ƿ�����ֹ��Ӧ��
            if (savedFlag.equals("F")) {
                if (stopType.trim().equals("0")) {//����
                    packDetailProcFlag = DBUtil.queryForString("select result from BatchPackDetail where packNo='" + oriPackNo + "' and traNo='" + oriTraNo + "'");                    
                    if( packDetailProcFlag == null || !packDetailProcFlag.equals("00000")){//����ֹ��
                    	DBUtil.executeUpdate("update BatchPackDetail set stopFlag='Y' where packNo='" + oriPackNo + "' and traNo='" + oriTraNo + "' ");
                        msg.set("//CFX/MSG/DelApply2123/StopAnswer", "T");
                        msg.set("//CFX/MSG/DelApply2123/AddWord", "ֹ���ɹ�");
                    }else {//"00000"����ֹ��
                    	msg.set("//CFX/MSG/DelApply2123/StopAnswer", "N");
                        msg.set("//CFX/MSG/DelApply2123/AddWord", "ֹ��ʧ��");
                    }
                    msg.set("//CFX/MSG/DelApply2123/OriTraNo", (String) params.get("OriTraNo"));
                } else if (stopType.trim().equals("1")) {//����
                    sql = "select procFlag from batchpackage where packNo='" + oriPackNo + "' ";
                    packProcFlag = DBUtil.queryForString(sql);
                    if (packProcFlag.equals("0") || packProcFlag.equals("1")) {//����ֹ��
                        DBUtil.executeUpdate("update batchpackage set stopFlag='Y' where packNo='" + oriPackNo + "' ");
                        msg.set("//CFX/MSG/DelApply2123/StopAnswer", "T");
                        msg.set("//CFX/MSG/DelApply2123/AddWord", "ֹ���ɹ�");
                    } else if (packProcFlag.equals("3") || packProcFlag.equals("4")) {//����ֹ��
                        msg.set("//CFX/MSG/DelApply2123/StopAnswer", "N");
                        msg.set("//CFX/MSG/DelApply2123/AddWord", "ֹ��ʧ��");
                    }
                    msg.set("//CFX/MSG/DelApply2123/OriTraNo", "");
                }
            } else if (savedFlag.equals("T")) {
                msg.set("//CFX/MSG/DelApply2123/StopAnswer", "N");
                msg.set("//CFX/MSG/DelApply2123/AddWord", "�Ѿ����͹�ֹ������");
                msg.set("//CFX/MSG/DelApply2123/OriTraNo", "");
            }
                        
            msg.set("//CFX/MSG/DelApply2123/TaxOrgCode", (String) params.get("TaxOrgCode"));
            msg.set("//CFX/MSG/DelApply2123/OriStopNo", (String) params.get("StopNo"));
            msg.set("//CFX/MSG/DelApply2123/EntrustDate", (String) params.get("EntrustDate"));
            msg.set("//CFX/MSG/DelApply2123/OriPackNo", (String) params.get("OriPackNo"));
            msg.set("//CFX/MSG/DelApply2123/OriEntrustDate", (String) params.get("OriEntrustDate"));

        } catch (SQLException e) {
            logger.error("", e);
            return FAIL;
        }

        return SUCCESS;
    }
}
