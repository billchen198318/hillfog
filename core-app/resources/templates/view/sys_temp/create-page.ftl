<html>
<head>
<title>qifu3</title>
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
	$("#id").val( '' );
	$("#title").val( '' );
	messageEditor.html.set('');
	$("#description").val( '' );
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG001D0004A_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('CORE_PROG001D0004A');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnSave();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG001D0004A');"
	programName="${programName}"
	programId="${programId}"
	description="Create template item.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="id" value="" id="id" label="Id" requiredFlag="Y" maxlength="10" placeholder="Enter Id"></@qifu.textbox>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="title" value="" id="title" label="Title" requiredFlag="Y" maxlength="200" placeholder="Enter title"></@qifu.textbox>
		</div>
	</div>	
</div>
<div class="form-group" id="form-group2">
	<@qifu.textarea name="message" id="message" value="" label="Message" requiredFlag="Y" escapeHtml="N"></@qifu.textarea>
</div>
<div class="form-group" id="form-group3">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>
</div>

<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnSave" label="Save"
			xhrUrl="./sysTemplateSaveJson"
			xhrParameter="
			{
				'templateId'	:	$('#id').val(),
				'title'			:	$('#title').val(),
				'description'	:	$('#description').val(),
				'message'		:	messageEditor.html.get()
			}
			"
			onclick="btnSave();"
			loadFunction="saveSuccess(data);"
			errorFunction="clearSave();">
		</@qifu.button>
		<@qifu.button id="btnClear" label="Clear" onclick="clearSave();"></@qifu.button>
	</div>
</div>

</body>
</html>