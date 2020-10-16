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
	
	$("#uploadLabel").html('<span class="badge badge-success">${sysJreport.reportId}.zip</span>');
	
});

var msgFields = new Object();
msgFields['reportId'] 	= 'reportId';

var formGroups = new Object();
formGroups['reportId'] 	= 'form-group1';

function updateSuccess(data) {
	clearWarningMessageField(formGroups, msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.toastrWarning( data.message );
		setWarningMessageField(formGroups, msgFields, data.checkFields);
		return;
	}
	parent.toastrInfo( data.message );
}

function clearUpdate() {
	window.location=parent.getProgUrlForOid('CORE_PROG001D0005E', '${sysJreport.oid}');
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
	id="CORE_PROG001D0005E_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('CORE_PROG001D0005E', '${sysJreport.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnUpdate();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG001D0005E');"
	programName="${programName}"
	programId="${programId}"
	description="Modify content or upload new report (Jasperreport) data.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<input type="hidden" name="uploadOid" id="uploadOid" value="" />
<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="reportId" value="sysJreport.reportId" id="reportId" label="Id" requiredFlag="Y" maxlength="50" placeholder="Enter report Id" readonly="Y"></@qifu.textbox>
		</div>
	</div>
</div>
<div class="form-group" id="form-group2">	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.checkbox name="isCompile" id="isCompile" label="Compiler" checked="N" checkedTest=" \"Y\" == sysJreport.isCompile "></@qifu.checkbox>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="sysJreport.description" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
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
		<@qifu.button id="btnUpdate" label="Save"
			xhrUrl="./sysReportUpdateJson"
			xhrParameter="
			{
				'oid'			:	'${sysJreport.oid}',
				'uploadOid'		:	$('#uploadOid').val(),
				'reportId'		:	$('#reportId').val(),
				'isCompile'		:	( $('#isCompile').is(':checked') ? 'Y' : 'N' ),
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