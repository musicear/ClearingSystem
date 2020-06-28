/*
 * Created on 2009-3-9
 *
 */
package resoft.tips.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
/**
 * @author haoruibing
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IDUtil {
	 		private static IDUtil instance = null;
	 		private static final Log logger = LogFactory.getLog(IDUtil.class);	
	    private IDUtil() {
	    }
	    /**
	     * 得到唯一实例
	     */
	    public static IDUtil getInstance() {
	        if(null==instance){
	        	instance = new IDUtil();
	        }
	        return instance;
	    }
	    /**
	     * 获得ID
	     * @param table
	     * @return
	     * @throws Exception
	     */
			public synchronized  long getMaxIDFromPayOrderPack() throws Exception{
				String sql="select max(ID) from PayOrderPack";
				String num=DBUtil.queryForString(sql);
				if(null==num || num.equals("")){
					return 1;
				}
				long Id=Long.parseLong(num)+1;
				return Id;
			}
			public synchronized  long getMaxIDFromPayRequestPack() throws Exception{
				String sql="select max(ID) from PayRequestPack";
				String num=DBUtil.queryForString(sql);
				if(null==num || num.equals("")){
					return 1;
				}
				long Id=Long.parseLong(num)+1;
				return Id;
			}
			public synchronized  long getMaxIDFromPayOrder() throws Exception{
				String sql="select max(ID) from PayOrder";
				String num=DBUtil.queryForString(sql);
				if(null==num || num.equals("")){
					return 1;
				}
				long Id=Long.parseLong(num)+1;
				return Id;
			}
			public synchronized  long getMaxIDFromPayRequest() throws Exception{
				String sql="select max(ID) from PayRequest";
				String num=DBUtil.queryForString(sql);
				if(null==num || num.equals("")){
					return 1;
				}
				long Id=Long.parseLong(num)+1;
				return Id;
			}
			public synchronized  long getMaxIDFromPayOrderDetail() throws Exception{
				String sql="select max(ID) from PayOrderDetail";
				String num=DBUtil.queryForString(sql);
				if(null==num || num.equals("")){
					return 1;
				}
				long Id=Long.parseLong(num)+1;
				return Id;
			}
			public synchronized  long getMaxIDFromRefundPack() throws Exception{
				String sql="select max(ID) from RefundPack";
				String num=DBUtil.queryForString(sql);

				if(null==num || num.equals("")){
					logger.info("num is 1..");
					return 1;
				}
				long Id=Long.parseLong(num)+1;
				return Id;
			}
			public synchronized  long getMaxIDFromRefundinfo() throws Exception{
				String sql="select max(ID) from Refundinfo";
				String num=DBUtil.queryForString(sql);
				if(null==num || num.equals("")){
					return 1;
				}
				long Id=Long.parseLong(num)+1;
				return Id;
			}
			public synchronized  long getMaxIDFromRefundDetail() throws Exception{
				String sql="select max(ID) from RefundDetail";
				String num=DBUtil.queryForString(sql);
				if(null==num || num.equals("")){
					return 1;
				}
				long Id=Long.parseLong(num)+1;
				return Id;
			}
}
