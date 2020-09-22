<html>
<head>
<title>qifu2</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<style type="text/css">


</style>


<script type="text/javascript">

$( document ).ready(function() {
	queryGrid();
});

function getQueryGridFormatter(value) {
	var str = '';
	str += '<img alt="delete" title="Delete" src="./images/delete.png" onclick="deleteRecord(\'' + value + '\');"/>';
	return str;
}
function getQueryGridHeader() {
	return [
		{ name: "#", 				field: "oid", 		formatter: getQueryGridFormatter },
		{ name: "Template Id", 		field: "templateId"		},
		{ name: "Variable", 		field: "templateVar"	},
		{ name: "Object variable", 	field: "objectVar"		},
		{ name: "Title only", 		field: "isTitle"		}
	];
}

var msgFields = new Object();
msgFields['templateVar'] 	= 'templateVar';
msgFields['objectVar'] 		= 'objectVar';

var formGroups = new Object();
formGroups['templateVar']	= 'form-group1';
formGroups['objectVar']		= 'form-group1';

function saveSuccess(data) {
	clearWarningMessageField(formGroups, msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.toastrWarning( data.message );
		setWarningMessageField(formGroups, msgFields, data.checkFields);
		return;
	}
	parent.toastrInfo( data.message );
	clearSave();
	queryGrid();
}

function clearSave() {
	clearWarningMessageField(formGroups, msgFields);
	$("#templateVar").val( '' );
	$("#objectVar").val( '' );
	$("#isTitle").prop('checked', false);
	clearQueryGridTable();
}

function deleteRecord(oid) {
	parent.bootbox.confirm(
			"Delete?", 
			function(result) { 
				if (!result) {
					return;
				}
				xhrSendParameter(
						'./sysTemplateParamDeleteJson', 
						{ 'oid' : oid }, 
						function(data) {
							if ( _qifu_success_flag != data.success ) {
								parent.toastrWarning( data.message );
							}
							if ( _qifu_success_flag == data.success ) {
								parent.toastrInfo( data.message );
							}
							queryGrid();
						}, 
						function() {
							
						},
						_qifu_defaultSelfPleaseWaitShow
				);
			}
	);	
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG001D0004S01Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('CORE_PROG001D0004S01Q', '${template.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnSave();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG001D0004S01Q');"
	programName="${programName}"
	programId="${programId}"
	description="Settings template parameter.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
        <div class="col-xs-6 col-md-6 col-lg-6">
        	Template&nbsp;:&nbsp;${template.templateId}
        </div>
	</div>        
	<div class="row">
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="templateVar" value="" id="templateVar" label="Template variable" requiredFlag="Y" maxlength="100" placeholder="Enter template variable"></@qifu.textbox>
       </div>
	</div>
	<div class="row">
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="objectVar" value="" id="objectVar" label="Object variable" requiredFlag="Y" maxlength="100" placeholder="Enter object variable"></@qifu.textbox>
       </div>
	</div>	
</div>
<div class="form-group" id="form-group2">	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.checkbox name="isTitle" id="isTitle" label="for title only"></@qifu.checkbox>
		</div>
	</div>	
</div>

<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<button type="button" class="btn btn-primary" id="btnQuery" onclick="queryGrid();">Query</button>
		&nbsp;
		<@qifu.button id="btnSave" label="Save"
			xhrUrl="./sysTemplateParamSaveJson"
			xhrParameter="
			{
				'templateOid'	:	'${template.oid}',
				'templateVar'	:	$('#templateVar').val(),
				'objectVar'		:	$('#objectVar').val(),
				'isTitle'		:	( $('#isTitle').is(':checked') ? 'Y' : 'N' )
			}
			"
			onclick="btnSave();"
			loadFunction="saveSuccess(data);"
			errorFunction="clearSave();">
		</@qifu.button>
		<@qifu.button id="btnClear" label="Clear" onclick="clearSave();"></@qifu.button>
	</div>
</div>

<p style="margin-bottom: 10px"></p>
<p style="margin-bottom: 10px"></p>

<@qifu.grid gridFieldStructure="getQueryGridHeader()" 
	xhrParameter="
	{
		'parameter[templateOid]'	: '${template.oid}',
		'select'					: getQueryGridSelect(),
		'showRow'					: getQueryGridShowRow()	
	}
	"
	xhrUrl="./sysTemplateParamQueryGridJson" 
	id="CORE_PROG001D0004S01Q_grid"
	queryFunction="queryGrid()"
	clearFunction="clearQueryGridTable()">
</@qifu.grid>

</body>
</html>