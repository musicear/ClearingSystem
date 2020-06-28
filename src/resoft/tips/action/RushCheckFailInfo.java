package resoft.tips.action;

import java.util.StringTokenizer;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;



/**
 * <p>接受WEB端发过来的文件,更改调帐信息标志,并且冲帐</p>
 * Author: zhuchangwu
 * Date: 2007-10-25
 * Time: 17:55:07
 */
public class RushCheckFailInfo implements Action {
    public int execute(Message msg) throws Exception {
        String total=msg.getString("total");
        String sql="";
        int totNum=Integer.parseInt(total);
        for(int i=0;i<totNum;i++){
        	String id=msg.getString("id"+i);
        	StringTokenizer tokens=new StringTokenizer(id.trim(),"-");
        	String bankTraDate=tokens.nextToken();
        	String bankTraNo=tokens.nextToken();
        	String payAcct=tokens.nextToken();
        	sql="update AdjustAcct set adjustStatus='1' where bankTraDate='"+bankTraDate+"' and bankTraNo='"+bankTraNo+"' and payAcct='"+payAcct+"'";
        DBUtil.executeUpdate(sql);
        }
        msg.clear();
        msg.set("Result", "Y");
        return SUCCESS;
    }
    }
