<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 


<!-- #################### codemirror #################### -->
<script src="${qifu_basePath}codemirror/lib/codemirror.js" type="text/javascript"></script>
<script src="${qifu_basePath}codemirror/addon/edit/matchbrackets.js"></script>
<script src="${qifu_basePath}codemirror/addon/hint/show-hint.js"></script>	
<script src="${qifu_basePath}codemirror/mode/clike/clike.js"></script>	
<!-- 
<link rel="stylesheet" type="text/css" href="${qifu_basePath}codemirror/doc/docs.css" />
-->
<link rel="stylesheet" type="text/css" href="${qifu_basePath}codemirror/lib/codemirror.css" />		
<link rel="stylesheet" href="${qifu_basePath}codemirror/addon/hint/show-hint.css">	
		
<script src="${qifu_basePath}codemirror/mode/xml/xml.js"></script>
<script src="${qifu_basePath}codemirror/mode/javascript/javascript.js"></script>
<script src="${qifu_basePath}codemirror/mode/css/css.js"></script>
<script src="${qifu_basePath}codemirror/mode/htmlmixed/htmlmixed.js"></script>
<script src="${qifu_basePath}codemirror/addon/mode/multiplex.js"></script>
<script src="${qifu_basePath}codemirror/mode/htmlembedded/htmlembedded.js"></script>
<!-- #################### codemirror #################### -->


<style type="text/css">


</style>


<script type="text/javascript">

var javaEditor = null;

$( document ).ready(function() {
	
	javaEditor = CodeMirror.fromTextArea(document.getElementById("content"), {
		lineNumbers: true,
		matchBrackets: true,
		mode: 'text/x-java'
	});	
	
});

var msgFields = new Object();
msgFields['exprId'] 		= 'exprId';
msgFields['name'] 			= 'name';
msgFields['type'] 			= 'type';
msgFields['content']		= 'content';

var formGroups = new Object();
formGroups['exprId'] 		= 'form-group1';
formGroups['name'] 			= 'form-group1';
formGroups['type'] 			= 'form-group1';
formGroups['content']		= 'form-group3';

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
	window.location=parent.getProgUrlForOid('CORE_PROG003D0002E', '${sysExpression.oid}');
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG003D0002E_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('CORE_PROG003D0002E', '${sysExpression.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnUpdate();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG003D0002E');"
	programName="${programName}"
	programId="${programId}"
	description="Modify expression content.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="exprId" id="exprId" value="sysExpression.exprId" maxlength="20" requiredFlag="Y" label="Id" placeholder="Enter Id" readonly="Y"></@qifu.textbox>
		</div>
	</div>	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			
			<@qifu.textbox name="name" id="name" value="sysExpression.name" maxlength="100" requiredFlag="Y" label="Name" placeholder="Enter name"></@qifu.textbox>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="typeMap" name="type" id="type" value="sysExpression.type" label="Type" requiredFlag="Y"></@qifu.select>
		</div>
	</div>		
</div>
<div class="form-group" id="form-group2">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="sysExpression.description" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>
</div>
<div class="form-group" id="form-group3">
	<div class="row">
		<div class="col-xs-11 col-md-11 col-lg-11">
			<@qifu.textarea name="content" id="content" value="sysExpression.content" label="Expression content" requiredFlag="Y" escapeHtml="N" escapeJavaScript="N"></@qifu.textarea>
		</div>
	</div>
</div>

<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnUpdate" label="<i class=\"icon fa fa-floppy-o\"></i>&nbsp;Save"
			xhrUrl="./sysExpressionUpdateJson"
			xhrParameter="	
			{
				'oid'				:	'${sysExpression.oid}',
				'exprId'			:	$('#exprId').val(),
				'name'				:	$('#name').val(),
				'type'				:	$('#type').val(),
				'description'		:	$('#description').val(),
				'content'			:	javaEditor.getValue()
			}
			"
			onclick="btnUpdate();"
			loadFunction="updateSuccess(data);"
			errorFunction="clearUpdate();">
		</@qifu.button>
		<@qifu.button id="btnClear" label="<i class=\"icon fa fa-hand-paper-o\"></i>&nbsp;Clear" onclick="clearUpdate();"></@qifu.button>
	</div>
</div>

<br/>
<br/>
<br/>

</body>
</html>