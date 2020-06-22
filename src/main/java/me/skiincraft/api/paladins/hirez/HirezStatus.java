package me.skiincraft.api.paladins.hirez;

import java.util.Date;

public class HirezStatus {

	private Date entryDateTime;
	private String environment;
	private boolean limitedAccess;
	private String platform;
	private String status;
	private String version;
	private String ret_msg;
	public HirezStatus(Date entryDateTime, String environment, boolean limitedAccess, String platform, String status,
			String version, String ret_msg) {
		this.entryDateTime = entryDateTime;
		this.environment = environment;
		this.limitedAccess = limitedAccess;
		this.platform = platform;
		this.status = status;
		this.version = version;
		this.ret_msg = ret_msg;
	}
	public Date getEntryDateTime() {
		return entryDateTime;
	}
	public String getEnvironment() {
		return environment;
	}
	public boolean hasLimitedAccess() {
		return limitedAccess;
	}
	public String getPlatform() {
		return platform;
	}
	public String getStatus() {
		return status;
	}
	public String getVersion() {
		return version;
	}
	public String getRet_msg() {
		return ret_msg;
	}
	
	
	
}
