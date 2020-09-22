package org.qifu.core.entity;

import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;

public class TbAccountKey implements java.io.Serializable {
	private static final long serialVersionUID = 5943857183417718509L;
	
	private String oid;
	private String account;
	
	@EntityPK(name = "oid", autoUUID = true)
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	@EntityUK(name = "account")
	public String getAccount() {
		return account;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}
}