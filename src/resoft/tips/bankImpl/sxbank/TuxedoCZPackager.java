package resoft.tips.bankImpl.sxbank;

public class TuxedoCZPackager {
	public String ConPackaer(){
		String packName = "O78671";
		return ToChangeDate(packName.length()) + packName ;
	}
	
	//ʮ������ת��
	public String ToChangeDate(int ori){
		
		return Integer.toHexString(ori).toString();
	
	}
}
