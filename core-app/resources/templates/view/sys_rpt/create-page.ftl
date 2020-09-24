<html>
<head>
<title>qifu3</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<style type="text/css">


</style>


<script type="text/javascript">

$( document ).ready(function() {
	
	$("#uploadLabel").html('<span class="badge badge-warning">No upload</span>');
	
});

var msgFields = new Object();
msgFields['reportId'] 	= 'reportId';

var formGroups = new Object();
formGroups['reportId'] 	= 'form-group1';

function saveSuccess(data) {
	clearWarningMessageField(formGroups, msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.toastrWarning( data.message );
		setWarningMessageField(formGroups, msgFields, data.checkFields);
		return;
	}
	parent.toastrInfo( data.message );
	clearSave();
}

function clearSave() {
	clearWarningMessageField(formGroups, msgFields);
	$("#uploadOid").val( '' );
	$("#reportId").val( '' );
	$("#isCompile").prop('checked', false);
	$("#description").val( '' );
	$("#uploadLabel").html('<span class="badge badge-warning">No upload</span>');
}

function uploadModal() {
	showCommonUploadModal(
			'uploadOid', 
			'tmp', 
			'Y',
			function() {
				$("#uploadLabel").html('<span class="badge badge-success">Upload success</span>');
			},
			function() {
				$("#uploadLabel").html('<span class="badge badge-danger">Upload fail</span>');
			}
	);
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG001D0005A_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('CORE_PROG001D0005A');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnSave();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG001D0005A');"
	programName="${programName}"
	programId="${programId}"
	description="Create upload new report (Jasperreport) data.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<input type="hidden" name="uploadOid" id="uploadOid" value="" />
<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="reportId" value="" id="reportId" label="Id" requiredFlag="Y" maxlength="50" placeholder="Enter report Id"></@qifu.textbox>
		</div>
	</div>
</div>
<div class="form-group" id="form-group2">	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.checkbox name="isCompile" id="isCompile" label="Compiler" checked="N"></@qifu.checkbox>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.button id="uploadBtn" label="Upload file (zip)" cssClass="btn btn-info" onclick="uploadModal();">&nbsp;</@qifu.button><div id="uploadLabel"></div>
		</div>
	</div>
</div>

<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnSave" label="Save"
			xhrUrl="./sysReportSaveJson"
			xhrParameter="
			{
				'uploadOid'		:	$('#uploadOid').val(),
				'reportId'		:	$('#reportId').val(),
				'isCompile'		:	( $('#isCompile').is(':checked') ? 'Y' : 'N' ),
				'description'	:	$('#description').val()
			}
			"
			onclick="btnSave();"
			loadFunction="saveSuccess(data);"
			errorFunction="clearSave();">
		</@qifu.button>
		<@qifu.button id="btnClear" label="Clear" onclick="clearSave();"></@qifu.button>
	</div>
</div>

<br/>
<br/>
<br/>

</body>
</html>