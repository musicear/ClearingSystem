package resoft.tips.action;

import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>����9004����</p>
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
            //�����صı�����9004����,����9120����.
			if(returnMsg.getString("//CFX/HEAD/MsgNo").equals("9004")){
				
				String opStat = returnMsg.getString("//CFX/MSG/TraStatusReturn9004/OpStat");
				//������������������,���ǵ��ʵ�.
				if(returnMsg.getString("//CFX/MSG/TraStatusReturn9004/OriPackNo").equals("00000000")){
					//����ǵ���
					if(opStat.equals("10")){
						msg.set("AddWord", "ת���ɹ�");
					}
					else if(opStat.equals("11")){
						msg.set("AddWord", "ת��ʧ��");
					}
					else if(opStat.equals("20")){
						msg.set("AddWord", "���յ��ۿ�ɹ���ִ��״̬");
					}
					else if(opStat.equals("21")){
						msg.set("AddWord", "���յ��ۿ�ʧ�ܻ�ִ��״̬");
					}
					else if(opStat.equals("97")){
						msg.set("AddWord", "�ѳ���");
					}
					else if(opStat.equals("98")){
						msg.set("AddWord", "���ײ�����");
					}
					else if(opStat.equals("99")){
						msg.set("AddWord", "������");
					}
				}else{
					//�����������
					if(opStat.equals("10")){
					msg.set("AddWord", "������");
					}
					else if(opStat.equals("11")){
					msg.set("AddWord", "�Ѿܾ�");
					}
					else if(opStat.equals("20")){
						msg.set("AddWord", "�ѿۿ�");
					}
					else if(opStat.equals("21")){
						msg.set("AddWord", "�ۿ�ʧ��");
					}
					else if(opStat.equals("97")){
						msg.set("AddWord", "��ֹ��");
					}
					else if(opStat.equals("98")){
						msg.set("AddWord", "���ײ�����");
					}
					else if(opStat.equals("99")){
						msg.set("AddWord", "������");
					}
				}
				return SUCCESS;
			}else{
				//����9004����,����9120.
				msg.set("AddWord",returnMsg.get("//CFX/MSG/MsgReturn9120/AddWord"));
				return SUCCESS;
			}
		}catch(Exception e){
			msg.set("AddWord", "��ѯʧ��");
			return FAIL;
		}
	
	}

}
