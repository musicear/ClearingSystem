package resoft.tips.util;

import java.util.List;
import java.util.Map;

import org.zerone.jdbc.QueryUtil;

/**
 * 清洗实时扣税、批量扣税的开户行行号
 * */

public class ClearData {
	public static void main(String []args) throws Exception{
		String sql="select * from realtimePayMent ";
		List realList=QueryUtil.queryRowSet(sql);		
		for(int i=0;i<realList.size();i++){
			Map row=(Map)realList.get(i);
			
		}
	}
}
