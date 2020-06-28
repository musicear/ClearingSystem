package resoft.tips.action;

import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>接收9004报文</p>
 * @author chenzifei
 * Date: 2007-9-18 
 * Time: 14:40:06
 *
 */
public class ReceiveReply implements Action{

	public int execute(Message msg) throws Exception {
		try{
			String xml = (String) msg.get("packet");
			Packager packager = new GenericXmlPackager();
			Message returnMsg = packager.unpack(xml.getBytes());
            //看返回的报文是9004报文,还是9120报文.
			if(returnMsg.getString("//CFX/HEAD/MsgNo").equals("9004")){
				
				String opStat = returnMsg.getString("//CFX/MSG/TraStatusReturn9004/OpStat");
				//看交易类型是批量的,还是单笔的.
				if(returnMsg.getString("//CFX/MSG/TraStatusReturn9004/OriPackNo").equals("00000000")){
					//如果是单笔
					if(opStat.equals("10")){
						msg.set("AddWord", "转发成功");
					}
					else if(opStat.equals("11")){
						msg.set("AddWord", "转发失败");
					}
					else if(opStat.equals("20")){
						msg.set("AddWord", "接收到扣款成功回执后状态");
					}
					else if(opStat.equals("21")){
						msg.set("AddWord", "接收到扣款失败回执后状态");
					}
					else if(opStat.equals("97")){
						msg.set("AddWord", "已冲正");
					}
					else if(opStat.equals("98")){
						msg.set("AddWord", "交易不存在");
					}
					else if(opStat.equals("99")){
						msg.set("AddWord", "已作废");
					}
				}else{
					//如果是批量的
					if(opStat.equals("10")){
					msg.set("AddWord", "已受理");
					}
					else if(opStat.equals("11")){
					msg.set("AddWord", "已拒绝");
					}
					else if(opStat.equals("20")){
						msg.set("AddWord", "已扣款");
					}
					else if(opStat.equals("21")){
						msg.set("AddWord", "扣款失败");
					}
					else if(opStat.equals("97")){
						msg.set("AddWord", "已止付");
					}
					else if(opStat.equals("98")){
						msg.set("AddWord", "交易不存在");
					}
					else if(opStat.equals("99")){
						msg.set("AddWord", "已作废");
					}
				}
				return SUCCESS;
			}else{
				//不是9004报文,就是9120.
				msg.set("AddWord",returnMsg.get("//CFX/MSG/MsgReturn9120/AddWord"));
				return SUCCESS;
			}
		}catch(Exception e){
			msg.set("AddWord", "查询失败");
			return FAIL;
		}
	
	}

}
