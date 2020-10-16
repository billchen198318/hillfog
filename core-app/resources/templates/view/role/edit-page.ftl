<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<style type="text/css">


</style>


<script type="text/javascript">

$( document ).ready(function() {
	
	
});

var msgFields = new Object();
msgFields['role'] 	= 'role';

var formGroups = new Object();
formGroups['role'] 	= 'form-group1';

function updateSuccess(data) {
	clearWarningMessageField(formGroups, msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.toastrWarning( data.message );
		setWarningMessageField(formGroups, msgFields, data.checkFields);
		return;
	}
	parent.toastrInfo( data.message );
	clearSave();
}

function clearUpdate() {
	clearWarningMessageField(formGroups, msgFields);
	window.location=parent.getProgUrlForOid('CORE_PROG002D0001E', '${role.oid}');
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG002D0001E_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('CORE_PROG002D0001E', '${role.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnUpdate();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG002D0001E');"
	programName="${programName}"
	programId="${programId}"
	description="Modify role item description content.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="role" value="role.role" id="role" label="Role" requiredFlag="Y" maxlength="50" placeholder="Enter role" readonly="Y"></@qifu.textbox>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="role.description" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>
</div>

<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnUpdate" label="Save"
			xhrUrl="./roleUpdateJson"
			xhrParameter="
			{
				'oid'			:	'${role.oid}',
				'role'			:	'${role.role}',
				'description'	:	$('#description').val()
			}
			"
			onclick="btnUpdate();"
			loadFunction="updateSuccess(data);"
			errorFunction="clearUpdate();">
		</@qifu.button>
		<@qifu.button id="btnClear" label="Clear" onclick="clearUpdate();"></@qifu.button>
	</div>
</div>

<br/>
<br/>
<br/>

</body>
</html>