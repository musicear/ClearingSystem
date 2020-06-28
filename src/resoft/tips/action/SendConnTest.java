package resoft.tips.action;
/**
 * <p>检索是否有连接测试的信息</p>
 * Author: liwei
 * Date: 2007-11-22
 * Time: 09:55:07
 */
import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

public class SendConnTest implements Action {
    public int execute(Message msg) throws Exception {
        int count=DBUtil.queryForInt("select count(*) from ConnTest where sendFlag='Y' ");
        if(count>0){
        	DBUtil.executeUpdate("update ConnTest set SendTime='"+DateTimeUtil.getDateTimeString()+"',SendFlag='N',ReciveTime='',ConnFlag='' ");
        	return SUCCESS;
        }else{
        	return FAIL;
        }
        
    }
}