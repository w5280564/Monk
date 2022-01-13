package com.xunda.lib.common.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 版本对象
 */
public class ApkBean implements Serializable {


	private String version;
	private String is_force_update;
	private String remark;
	private String platform;
	private String download_addre;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIs_force_update() {
		return is_force_update;
	}

	public void setIs_force_update(String is_force_update) {
		this.is_force_update = is_force_update;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getDownload_addre() {
		return download_addre;
	}

	public void setDownload_addre(String download_addre) {
		this.download_addre = download_addre;
	}
}
