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
	$("#orgId").val('');
	$("#name").val('');
	$("#description").val('');
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG001D0001A_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('HF_PROG001D0001A');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnSave();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0001A');"
	programName="${programName}"
	programId="${programId}"
	description="Create organization / department item." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="orgId" value="" id="orgId" label="${getText('page.organization.id')}" requiredFlag="Y" maxlength="15" placeholder="Enter organization Id" />
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="name" value="" id="name" label="${getText('page.organization.name')}" requiredFlag="Y" maxlength="25" placeholder="Enter organization name" />
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="" id="description" label="${getText('page.organization.description')}" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>	
</div>
						
<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnSave" label="<i class=\"icon fa fa-floppy-o\"></i>&nbsp;${getText('page.button.save')}"
			xhrUrl="./hfOrgDeptSaveJson"
			xhrParameter="
			{
				'orgId'			:	$('#orgId').val(),
				'name'			:	$('#name').val(),
				'description'	:	$('#description').val()
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