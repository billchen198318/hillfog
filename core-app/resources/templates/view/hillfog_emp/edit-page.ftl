<html>
<head>
<title>qifu3</title>
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
var selDeptList = [ ${orgInputAutocompleteValue} ];

$( document ).ready(function() {
	
	$("#employeeOrganization").autocomplete({
		source: orgDeptList
	}).focus(function() {
		if ($(this).val() == ' ' || $(this).val() =='') {
			$(this).autocomplete("search", " ");
		}
	});
	
	paintOrganization();
	
});

var msgFields = new Object();
msgFields['empId'] 					= 'empId';
msgFields['account'] 				= 'account';
msgFields['password1'] 				= 'password1';
msgFields['password2'] 				= 'password2';
msgFields['name'] 					= 'name';
msgFields['employeeOrganization']	= 'employeeOrganization';

var formGroups = new Object();
formGroups['empId'] 				= 'form-group1';
formGroups['account'] 				= 'form-group1';
formGroups['password1'] 			= 'form-group1';
formGroups['password2'] 			= 'form-group1';
formGroups['name'] 					= 'form-group1';
formGroups['employeeOrganization']	= 'form-group1';

function updateSuccess(data) {
	clearWarningMessageField(formGroups, msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.toastrWarning( data.message );
		setWarningMessageField(formGroups, msgFields, data.checkFields);
		return;
	}
	parent.toastrInfo( data.message );
	clearUpdate();
}

function clearUpdate() {
	clearWarningMessageField(formGroups, msgFields);
	window.location=parent.getProgUrlForOid('HF_PROG001D0002E', '${employee.oid}');
}

// ====================================================================
function addOrganization() {
	var inputOrgDept = $("#employeeOrganization").val();
	if (null == inputOrgDept || '' == inputOrgDept) {
		parent.toastrInfo( 'Please input organization!' );
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
		parent.toastrInfo( 'Please input organization!' );
		return;		
	}
	for (var n in selDeptList) {
		if ( selDeptList[n] == inputOrgDept) {
			checkInSelOrgDept = true;
		}
	}
	if (checkInSelOrgDept) {
		parent.toastrInfo( 'Organization is add found!' );
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
	id="HF_PROG001D0002E_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('HF_PROG001D0002E', '${employee.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnUpdate();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0002E');"
	programName="${programName}"
	programId="${programId}"
	description="Modify employee item." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="empId" value="employee.empId" id="empId" label="Id" requiredFlag="Y" maxlength="15" placeholder="Enter Id" />
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="account" value="employee.account" id="account" label="Account" requiredFlag="Y" maxlength="24" placeholder="Enter account" readonly="Y" />
		</div>
	</div>	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
		    <label for="password1">Password</label>
		    <input type="password" class="form-control" id="password1" name="password1" placeholder="Enter password" maxlength="12">		
    		<div class="form-control-feedback" id="password1-feedback"></div>
		</div>
	</div>		
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
		    <label for="password2">Password(retry)</label>
		    <input type="password" class="form-control" id="password2" name="password2" placeholder="Enter password(retry)" maxlength="12">		
    		<div class="form-control-feedback" id="password2-feedback"></div>
		</div>
	</div>		
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="name" value="employee.name" id="name" label="Name" requiredFlag="Y" maxlength="25" placeholder="Enter organization name" />
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="employeeOrganization" value="" id="employeeOrganization" label="Organization" requiredFlag="Y" maxlength="100" placeholder="Enter organization" />
			<button type="button" class="btn btn-info" id="btnAddOrganization" title="add organization" onclick="addOrganization();">ADD</button>
			<div>
				<span id="selOrgDeptShowLabel">&nbsp;</span>
			</div>					
		</div>		
	</div>		
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="employee.description" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>	
</div>
						
<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnUpdate" label="Save"
			xhrUrl="./hfEmployeeUpdateJson"
			xhrParameter="
			{
				'oid'					:	'${employee.oid}',
				'empId'					:	$('#empId').val(),
				'account'				:	$('#account').val(),
				'password1'				:	$('#password1').val(),
				'password2'				:	$('#password2').val(),
				'name'					:	$('#name').val(),
				'description'			:	$('#description').val(),
				'employeeOrganization'	:	JSON.stringify( { 'items' : selDeptList } )		
			}
			"
			onclick="btnUpdate();"
			loadFunction="updateSuccess(data);"
			errorFunction="clearUpdate();" />
		<@qifu.button id="btnClear" label="Clear" onclick="clearUpdate();" />
	</div>
</div>

<br/>
<br/>
<br/>

</body>
</html>