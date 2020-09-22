package org.qifu.core.entity;

import java.util.Date;
import java.util.List;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class TbUserRole implements java.io.Serializable {
	private static final long serialVersionUID = -7339988300440734279L;
	
	private String oid;
	private String role;
	private String account;
	private String description;
	private String cuserid;
	private Date cdate;
	private String uuserid;
	private Date udate;
	
	// for 權限檢核時要用, 準備User登入資料.
	private List<TbRolePermission> rolePermission = null;
	
	@EntityPK(name = "oid", autoUUID = true)
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	@EntityUK(name = "role")
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	@EntityUK(name = "account")
	public String getAccount() {
		return account;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@CreateUserField(name = "cuserid")
	public String getCuserid() {
		return cuserid;
	}
	
	public void setCuserid(String cuserid) {
		this.cuserid = cuserid;
	}
	
	@CreateDateField(name = "cdate")
	public Date getCdate() {
		return cdate;
	}
	
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}
	
	@UpdateUserField(name = "uuserid")
	public String getUuserid() {
		return uuserid;
	}
	
	public void setUuserid(String uuserid) {
		this.uuserid = uuserid;
	}
	
	@UpdateDateField(name = "udate")
	public Date getUdate() {
		return udate;
	}
	
	public void setUdate(Date udate) {
		this.udate = udate;
	}

	public List<TbRolePermission> getRolePermission() {
		return rolePermission;
	}

	public void setRolePermission(List<TbRolePermission> rolePermission) {
		this.rolePermission = rolePermission;
	}
	
}