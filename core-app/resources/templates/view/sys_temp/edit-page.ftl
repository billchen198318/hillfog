<html>
<head>
<title>qifu2</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<!-- Include Editor style. -->
<link href="./froala_editor/css/froala_editor.pkgd.min.css" rel="stylesheet" type="text/css" />
<link href="./froala_editor/css/froala_style.min.css" rel="stylesheet" type="text/css" />
<!-- Include Editor JS files. -->
<script type="text/javascript" src="./froala_editor/js/froala_editor.pkgd.min.js"></script>

<style type="text/css">


</style>


<script type="text/javascript">

var messageEditor;
$( document ).ready(function() {
	
	messageEditor = new FroalaEditor("#message", { height: 250 });
	
});

var msgFields = new Object();
msgFields['templateId'] 	= 'id';
msgFields['title'] 			= 'title';
msgFields['message'] 		= 'message';

var formGroups = new Object();
formGroups['id'] 		= 'form-group1';
formGroups['title'] 	= 'form-group1';
formGroups['message'] 	= 'form-group2';

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
	parent.getProgUrlForOid('CORE_PROG001D0004E', '${template.oid}');
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG001D0004E_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('CORE_PROG001D0004E', '${template.oid}');"  
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnUpdate();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG001D0004E');"
	programName="${programName}"
	programId="${programId}"
	description="Modify template item content.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="id" value="template.templateId" id="id" label="Id" requiredFlag="Y" maxlength="10" placeholder="Enter Id" readonly="Y"></@qifu.textbox>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="title" value="template.title" id="title" label="Title" requiredFlag="Y" maxlength="200" placeholder="Enter title"></@qifu.textbox>
		</div>
	</div>	
</div>
<div class="form-group" id="form-group2">
	<@qifu.textarea name="message" id="message" value="template.message" label="Message" requiredFlag="Y" escapeHtml="N"></@qifu.textarea>
</div>
<div class="form-group" id="form-group3">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="template.description" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>
</div>

<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnUpdate" label="Save"
			xhrUrl="./sysTemplateUpdateJson"
			xhrParameter="
			{
				'oid'			:	'${template.oid}',
				'templateId'	:	'${template.templateId}',
				'title'			:	$('#title').val(),
				'description'	:	$('#description').val(),
				'message'		:	messageEditor.html.get()
			}
			"
			onclick="btnUpdate();"
			loadFunction="updateSuccess(data);"
			errorFunction="clearUpdate();">
		</@qifu.button>
		<@qifu.button id="btnClear" label="Clear" onclick="clearUpdate();"></@qifu.button>
	</div>
</div>

</body>
</html>