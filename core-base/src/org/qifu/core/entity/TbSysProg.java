package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class TbSysProg implements java.io.Serializable {
	private static final long serialVersionUID = 8801640884943547614L;
	
	private String oid;
	private String progId;
	private String name;
	private String url;
	private String editMode;
	private String isDialog;
	private Integer dialogW;
	private Integer dialogH;
	private String progSystem;
	private String itemType;
	private String icon;
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
	
	@EntityUK(name = "progId")
	public String getProgId() {
		return progId;
	}
	
	public void setProgId(String progId) {
		this.progId = progId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getEditMode() {
		return editMode;
	}
	
	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}
	
	public String getIsDialog() {
		return isDialog;
	}
	
	public void setIsDialog(String isDialog) {
		this.isDialog = isDialog;
	}
	
	public Integer getDialogW() {
		return dialogW;
	}
	
	public void setDialogW(Integer dialogW) {
		this.dialogW = dialogW;
	}
	
	public Integer getDialogH() {
		return dialogH;
	}
	
	public void setDialogH(Integer dialogH) {
		this.dialogH = dialogH;
	}
	
	public String getProgSystem() {
		return progSystem;
	}
	
	public void setProgSystem(String progSystem) {
		this.progSystem = progSystem;
	}
	
	public String getItemType() {
		return itemType;
	}
	
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
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