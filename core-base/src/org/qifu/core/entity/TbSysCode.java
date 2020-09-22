package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class TbSysCode implements java.io.Serializable {
	private static final long serialVersionUID = 3286831622624752995L;
	
	private String oid;
	private String code;
	private String type;
	private String name;
	private String param1;
	private String param2;
	private String param3;
	private String param4;
	private String param5;
	private String cuserid;
	private Date cdate;
	private String uuserid;
	private Date udate;
	
	@EntityPK(name = "oid", autoUUID = true)
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	@EntityUK(name = "code")
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getParam1() {
		return param1;
	}
	
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	
	public String getParam2() {
		return param2;
	}
	
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	
	public String getParam3() {
		return param3;
	}
	
	public void setParam3(String param3) {
		this.param3 = param3;
	}
	
	public String getParam4() {
		return param4;
	}
	
	public void setParam4(String param4) {
		this.param4 = param4;
	}
	
	public String getParam5() {
		return param5;
	}
	
	public void setParam5(String param5) {
		this.param5 = param5;
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
	
}