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
	
	$("#methodParamIndex").ForceNumericOnly();
	queryGrid();
	
});

function getQueryGridFormatter(value) {
	var str = '';
	str += '<img alt="delete" title="Delete" src="./images/delete.png" onclick="deleteRecord(\'' + value + '\');"/>';
	return str;
}
function getQueryGridHeader() {
	return [
		{ name: "#", 					field: "oid", 	formatter: getQueryGridFormatter },
		{ name: "Variable",				field: "varName"				},
		{ name: "Method result", 		field: "methodResultFlag"		},
		{ name: "Method param class",	field: "methodParamClass"		},
		{ name: "Method param index",	field: "methodParamIndex"		}
	];
}

var msgFields = new Object();
msgFields['varName'] 		= 'varName';
msgFields['methodParamClass'] 			= 'methodParamClass';
msgFields['methodParamIndex'] 			= 'methodParamIndex';

var formGroups = new Object();
formGroups['varName'] 	= 'form-group1';
formGroups['methodParamClass'] 			= 'form-group2';
formGroups['methodParamIndex'] 			= 'form-group2';

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
	$("#varName").val('');
	$("#methodResultFlag").prop('checked', false);
	$("#methodParamClass").val('');
	$("#methodParamIndex").val('');
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
						'./sysBeanSupportExpressionParamDeleteJson', 
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
	id="CORE_PROG003D0003S02Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('CORE_PROG003D0003S02Q', '${sysBeanHelpExpr.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnSave();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG003D0003S02Q');"
	programName="${programName}"
	programId="${programId}"
	description="Settings service bean support expression item parameter mapper.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			Expresion Id&nbsp;/&nbsp;SEQ&nbsp;/&nbsp;Type
			<p style="margin-bottom: 10px"></p>
			${sysBeanHelpExpr.exprId}&nbsp;/&nbsp;${sysBeanHelpExpr.exprSeq}&nbsp;/&nbsp;${sysBeanHelpExpr.runType}
		</div>
	</div>			
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="varName" id="varName" value="" maxlength="255" label="Variable" requiredFlag="Y"></@qifu.textbox>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.checkbox name="methodResultFlag" id="methodResultFlag" label="Method result"></@qifu.checkbox>
		</div>
	</div>
</div>
<div class="form-group" id="form-group2">	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="methodParamClass" id="methodParamClass" value="" maxlength="255" label="Method parameter class" requiredFlag="Y"></@qifu.textbox>
		</div>
	</div>	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="methodParamIndex" id="methodParamIndex" value="" maxlength="2" label="Method parameter index" requiredFlag="Y"></@qifu.textbox>
		</div>
	</div>		
</div>

<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<button type="button" class="btn btn-primary" id="btnQuery" onclick="queryGrid();">Query</button>
		&nbsp;	
		<@qifu.button id="btnSave" label="Save"
			xhrUrl="./sysBeanSupportExpressionParamSaveJson"
			xhrParameter="	
			{
				'sysBeanHelpExprOid'	:	'${sysBeanHelpExpr.oid}',
				'varName'			:	$('#varName').val(),
				'methodResultFlag'			:	( $('#methodResultFlag').is(':checked') ? 'Y' : 'N' ),
				'methodParamClass'			:	$('#methodParamClass').val(),
				'methodParamIndex'			:	( isNaN(parseInt( $('#methodParamIndex').val() )) ? -1 : parseInt( $('#methodParamIndex').val() ) )
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
		'parameter[helpExprOid]'	: '${sysBeanHelpExpr.oid}',
		'select'					: getQueryGridSelect(),
		'showRow'					: getQueryGridShowRow()	
	}
	"
	xhrUrl="./sysBeanSupportExpressionParamQueryGridJson" 
	id="CORE_PROG003D0003S02Q_grid"
	queryFunction="queryGrid()"
	clearFunction="clearQueryGridTable()">
</@qifu.grid>

<br/>
<br/>
<br/>

</body>
</html>