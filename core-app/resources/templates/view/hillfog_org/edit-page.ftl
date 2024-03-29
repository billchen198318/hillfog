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
msgFields['orgId'] 			= 'orgId';
msgFields['name'] 			= 'name';

function updateSuccess(data) {
	clearWarningMessageField(msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.notifyWarning( data.message );
		setWarningMessageField(msgFields, data.checkFields);
		return;
	}
	parent.notifyInfo( data.message );
	clearUpdate();
}

function clearUpdate() {
	clearWarningMessageField(msgFields);
	window.location=parent.getProgUrlForOid('HF_PROG001D0001E', '${orgDept.oid}');
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG001D0001E_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('HF_PROG001D0001E', '${orgDept.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnUpdate();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0001E');"
	programName="${programName}"
	programId="${programId}"
	description="Modify organization / department item." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="orgId" value="orgDept.orgId" id="orgId" label="${getText('page.organization.id')}" requiredFlag="Y" maxlength="15" placeholder="Enter organization Id" readonly="Y" />
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="name" value="orgDept.name" id="name" label="${getText('page.organization.name')}" requiredFlag="Y" maxlength="25" placeholder="Enter organization name" />
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="orgDept.description" id="description" label="${getText('page.organization.description')}" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>	
</div>
						
<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnUpdate" label="<i class=\"icon fa fa-floppy-o\"></i>&nbsp;${getText('page.button.save')}"
			xhrUrl="./hfOrgDeptUpdateJson"
			xhrParameter="
			{
				'oid'			:	'${orgDept.oid}',
				'orgId'			:	$('#orgId').val(),
				'name'			:	$('#name').val(),
				'description'	:	$('#description').val()				
			}
			"
			onclick="btnUpdate();"
			loadFunction="updateSuccess(data);"
			errorFunction="clearUpdate();" />
		<@qifu.button id="btnClear" label="<i class=\"icon fa fa-hand-paper-o\"></i>&nbsp;${getText('page.button.clear')}" onclick="clearUpdate();" />
	</div>
</div>

<br/>
<br/>
<br/>

</body>
</html>