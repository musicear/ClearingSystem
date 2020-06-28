package resoft.tips.domain;

/**
 * <p>
 * </p>
 * Author: liguoyin Date: 2007-7-9 Time: 15:09:29
 */
public class SysStatus {

	public String getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}


	public String getSysStatus() {
		return SysStatus;
	}

	public void setSysStatus(String sysStatus) {
		SysStatus = sysStatus;
	}
	private String loginStatus;

	private String workDate;
	
	private String SysStatus;
}
