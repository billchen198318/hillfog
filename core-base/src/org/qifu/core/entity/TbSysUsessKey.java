package org.qifu.core.entity;

import org.qifu.base.model.EntityPK;

public class TbSysUsessKey implements java.io.Serializable {
	private static final long serialVersionUID = -1487899441465689533L;
	
	private String oid;
	private String sessionId;
	
	@EntityPK(name = "oid", autoUUID = true)
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}