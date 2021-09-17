<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<script type="text/javascript" src="${qifu_basePath}js/jquery-ui.min.js?ver=${qifu_jsVerBuild}"></script>

<style type="text/css">

.ui-autocomplete {
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 1000;
  display: none;
  float: left;
  min-width: 160px;
  padding: 5px 0;
  margin: 2px 0 0;
  list-style: none;
  font-size: 14px;
  text-align: left;
  background-color: #ffffff;
  border: 1px solid #cccccc;
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: 4px;
  -webkit-box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
  background-clip: padding-box;
}

.ui-autocomplete > li > div {
  display: block;
  padding: 3px 20px;
  clear: both;
  font-weight: normal;
  line-height: 1.42857143;
  color: #333333;
  white-space: nowrap;
}

.ui-state-hover,
.ui-state-active,
.ui-state-focus {
  text-decoration: none;
  color: #262626;
  background-color: #f5f5f5;
  cursor: pointer;
}

.ui-helper-hidden-accessible {
  border: 0;
  clip: rect(0 0 0 0);
  height: 1px;
  margin: -1px;
  overflow: hidden;
  padding: 0;
  position: absolute;
  width: 1px;
}

</style>


<script type="text/javascript">

var orgDeptList = [ ${orgInputAutocomplete} ];
var selDeptList = [];

$( document ).ready(function() {
	
	$("#employeeOrganization").autocomplete({
		source: orgDeptList
	}).focus(function() {
		if ($(this).val() == ' ' || $(this).val() =='') {
			$(this).autocomplete("search", " ");
		}
	});
	
});

var msgFields = new Object();
msgFields['empId'] 					= 'empId';
msgFields['account'] 				= 'account';
msgFields['password1'] 				= 'password1';
msgFields['password2'] 				= 'password2';
msgFields['name'] 					= 'name';
msgFields['jobTitle'] 				= 'jobTitle';
msgFields['employeeOrganization']	= 'employeeOrganization';

function saveSuccess(data) {
	clearWarningMessageField(msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.notifyWarning( data.message );
		setWarningMessageField(msgFields, data.checkFields);
		return;
	}
	parent.notifyInfo( data.message );
	clearSave();
}

function clearSave() {
	clearWarningMessageField(msgFields);
	$("#empId").val('');
	$("#account").val('');
	$("#password1").val('');
	$("#password2").val('');
	$("#name").val('');
	$("#jobTitle").val('');
	$("#description").val('');
	$('#employeeOrganization').val('');
	clearUpload();
	selDeptList = [];
	paintOrganization();
}

// ====================================================================
function addOrganization() {
	var inputOrgDept = $("#employeeOrganization").val();
	if (null == inputOrgDept || '' == inputOrgDept) {
		parent.notifyInfo( 'Please input organization!' );
		return;
	}
	var checkInOrgDept = false;
	var checkInSelOrgDept = false;	
	for (var n in orgDeptList) {
		if ( orgDeptList[n] == inputOrgDept ) {
			checkInOrgDept = true;
		}
	}
	if (!checkInOrgDept) {
		parent.notifyInfo( 'Please input organization!' );
		return;		
	}
	for (var n in selDeptList) {
		if ( selDeptList[n] == inputOrgDept) {
			checkInSelOrgDept = true;
		}
	}
	if (checkInSelOrgDept) {
		parent.notifyInfo( 'Organization is add found!' );
		return;
	}
	selDeptList.push( inputOrgDept );
	$('#employeeOrganization').val('');
	paintOrganization();
	
}
function paintOrganization() {
	$('#selOrgDeptShowLabel').html( '' );
	var htmlContent = '';
	for (var n in selDeptList) {
		htmlContent += '<span class="badge badge-secondary"><font size="3">' + selDeptList[n] + '</font><span class="badge badge-danger btn" onclick="delAddOrganization(' + n + ');">X</span></span>&nbsp;';
	}
	$('#selOrgDeptShowLabel').html( htmlContent );
}
function delAddOrganization(pos) {
	removeArrayByPos(selDeptList, pos);
	paintOrganization();	
}

function uploadModal() {
	showCommonUploadModal(
			'uploadOid', 
			'tmp', 
			'Y',
			function() {
				parent.notifyInfo('Upload success!');
				var uploadOid = $("#uploadOid").val();
				var imgStr = '<img src="./commonViewFile?oid=' + uploadOid + '" class="rounded-circle" style="max-height:128px;max-width:128px;">';
				$("#uploadLabel").html( imgStr );
			},
			function() {
				parent.notifyWarning('Upload fail!');
				$("#uploadLabel").html( "&nbsp;" );
			}
	);
}

function clearUpload() {
	$('#uploadOid').val('');
	$("#uploadLabel").html( "" );	
}

function removeArrayByPos(arr, pos) {
    for (var i = arr.length; i--;) {
    	if (pos == i) {
    		arr.splice(pos, 1);
    	}
    }
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG001D0002A_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('HF_PROG001D0002A');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnSave();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0002A');"
	programName="${programName}"
	programId="${programId}"
	description="Create employee item." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent imageUploadOnly="Y" /> 

<input type="hidden" name="uploadOid" id="uploadOid" value="" />

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="empId" value="" id="empId" label="${getText('page.employee.empId')}" requiredFlag="Y" maxlength="15" placeholder="Enter Id" />
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="account" value="" id="account" label="${getText('page.employee.account')}" requiredFlag="Y" maxlength="24" placeholder="Enter account" />
		</div>		
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
		    <label for="password1">${getText('page.employee.password1')}&nbsp;<font color="RED">*</font></label>
		    <input type="password" class="form-control" id="password1" name="password1" placeholder="Enter password" maxlength="12">		
    		<div class="form-control-feedback" id="password1-feedback"></div>
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
		    <label for="password2">${getText('page.employee.password2')}&nbsp;<font color="RED">*</font></label>
		    <input type="password" class="form-control" id="password2" name="password2" placeholder="Enter password(retry)" maxlength="12">		
    		<div class="form-control-feedback" id="password2-feedback"></div>
		</div>		
	</div>	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="name" value="" id="name" label="${getText('page.employee.nameLike')}" requiredFlag="Y" maxlength="25" placeholder="Enter organization name" />
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="jobTitle" value="" id="jobTitle" label="${getText('page.employee.jobTitleLike')}" requiredFlag="N" maxlength="100" placeholder="Enter job title" />
		</div>		
	</div>		
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
		
			<p style="margin-bottom: 10px"></p>
			
			<@qifu.button id="uploadBtn" label="<i class=\"icon fa fa-upload\"></i>&nbsp;${getText('page.employee.uploadImage')}" cssClass="btn btn-info" onclick="uploadModal();">&nbsp;</@qifu.button>
			<button type="button" class="btn btn-dark" title="remove employee image" onclick="clearUpload();"><i class="icon fa fa-remove"></i></button>
			
			<p style="margin-bottom: 10px"></p>
			<div id="uploadLabel"></div>
			<p style="margin-bottom: 10px"></p>
			
		</div>		
	</div>	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="employeeOrganization" value="" id="employeeOrganization" label="${getText('page.employee.employeeOrganization')}" requiredFlag="Y" maxlength="100" placeholder="Enter organization" />
			<button type="button" class="btn btn-info" id="btnAddOrganization" title="add organization" onclick="addOrganization();"><i class="icon fa fa-plus"></i>&nbsp;${getText('page.button.add')}</button>
			<div>
				<span id="selOrgDeptShowLabel">&nbsp;</span>
			</div>					
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="" id="description" label="${getText('page.employee.description')}" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>			
	</div>		
</div>
						
<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnSave" label="<i class=\"icon fa fa-floppy-o\"></i>&nbsp;${getText('page.button.save')}"
			xhrUrl="./hfEmployeeSaveJson"
			xhrParameter="
			{
				'empId'					:	$('#empId').val(),
				'account'				:	$('#account').val(),
				'password1'				:	$('#password1').val(),
				'password2'				:	$('#password2').val(),
				'name'					:	$('#name').val(),
				'description'			:	$('#description').val(),
				'jobTitle'				:	$('#jobTitle').val(),
				'uploadOid'				:	$('#uploadOid').val(),
				'employeeOrganization'	:	JSON.stringify( { 'items' : selDeptList } )
			}
			"
			onclick="btnSave();"
			loadFunction="saveSuccess(data);"
			errorFunction="clearSave();" />
		<@qifu.button id="btnClear" label="<i class=\"icon fa fa-hand-paper-o\"></i>&nbsp;${getText('page.button.clear')}" onclick="clearSave();" />
	</div>
</div>

<br/>
<br/>
<br/>

</body>
</html>