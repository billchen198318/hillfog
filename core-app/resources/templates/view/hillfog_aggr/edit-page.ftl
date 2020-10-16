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

.CodeMirror {
	height:	200px;
}

</style>


<script type="text/javascript">

var javaEditor1 = null;
var javaEditor2 = null;

$( document ).ready(function() {
	
	javaEditor1 = CodeMirror.fromTextArea(document.getElementById("expression1"), {
		lineNumbers: true,
		matchBrackets: true,
		mode: 'text/x-java'
	});	

	javaEditor2 = CodeMirror.fromTextArea(document.getElementById("expression2"), {
		lineNumbers: true,
		matchBrackets: true,
		mode: 'text/x-java'
	});	
	
});

var msgFields = new Object();
msgFields['aggrId'] 		= 'aggrId';
msgFields['name'] 			= 'name';
msgFields['type'] 			= 'type';
msgFields['expression1'] 	= 'expression1';
msgFields['expression2'] 	= 'expression2';

var formGroups = new Object();
formGroups['aggrId'] 		= 'form-group1';
formGroups['name'] 			= 'form-group1';
formGroups['type'] 			= 'form-group1';
formGroups['expression1'] 	= 'form-group2';
formGroups['expression2'] 	= 'form-group2';

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
	window.location=parent.getProgUrlForOid('HF_PROG001D0004E', '${aggrMethod.oid}');
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG001D0004E_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('HF_PROG001D0004E', '${aggrMethod.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnUpdate();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0004E');"
	programName="${programName}"
	programId="${programId}"
	description="Modify aggregation-method item." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="aggrId" value="aggrMethod.aggrId" id="aggrId" label="Id" requiredFlag="Y" maxlength="14" placeholder="Enter Id" readonly="Y" />
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="name" value="aggrMethod.name" id="name" label="Name" requiredFlag="Y" maxlength="100" placeholder="Enter name" />
		</div>		
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="typeMap" name="type" id="type" value="aggrMethod.type" label="Type" requiredFlag="Y"></@qifu.select>
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
		&nbsp;
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="aggrMethod.description" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>			
</div>
<div class="form-group" id="form-group2">
	
	<hr>
	
	<div class="row">
		<div class="col-xs-11 col-md-11 col-lg-11">
			<@qifu.textarea name="expression1" id="expression1" value="aggrMethod.expression1" label="Expression content" requiredFlag="Y" escapeHtml="N" escapeJavaScript="N"></@qifu.textarea>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-11 col-md-11 col-lg-11">
			<@qifu.textarea name="expression2" id="expression2" value="aggrMethod.expression2" label="Expression content (Date range)" requiredFlag="Y" escapeHtml="N" escapeJavaScript="N"></@qifu.textarea>
		</div>
	</div>	
	
	<hr>
	
</div>	
						
<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnUpdate" label="Save"
			xhrUrl="./hfAggregationMethodUpdateJson"
			xhrParameter="
			{
				'oid'			:	'${aggrMethod.oid}',
				'aggrId'		:	'${aggrMethod.aggrId}',
				'name'			:	$('#name').val(),
				'type'			:	$('#type').val(),
				'expression1'	:	javaEditor1.getValue(),
				'expression2'	:	javaEditor2.getValue(),
				'description'	:	$('#description').val()		
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