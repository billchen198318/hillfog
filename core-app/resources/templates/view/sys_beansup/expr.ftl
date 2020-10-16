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
	
	queryGrid();
	
});

function getQueryGridFormatter(value) {
	var str = '';
	str += '<img alt="mapper" title="Variable mapper" src="./images/alert.png" onclick="editVarMapperPage(\'' + value + '\');"/>';	
	str += '&nbsp;&nbsp;';		
	str += '<img alt="delete" title="Delete" src="./images/delete.png" onclick="deleteRecord(\'' + value + '\');"/>';
	return str;
}
function getQueryGridHeader() {
	return [
		{ name: "#", 				field: "oid", 	formatter: getQueryGridFormatter },
		{ name: "Expression Id",	field: "exprId"			},
		{ name: "Seq", 				field: "exprSeq"		},
		{ name: "Process type",		field: "runType"		}
	];
}

var msgFields = new Object();
msgFields['expressionOid'] 		= 'expressionOid';
msgFields['exprSeq'] 			= 'exprSeq';
msgFields['runType'] 			= 'runType';

var formGroups = new Object();
formGroups['expressionOid'] 	= 'form-group1';
formGroups['exprSeq'] 			= 'form-group1';
formGroups['runType'] 			= 'form-group1';

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
	$("#expressionOid").val( _qifu_please_select_id );
	$("#runType").val( _qifu_please_select_id );
	$("#exprSeq").val('');
	clearQueryGridTable();
}

function editVarMapperPage(oid) {
	parent.addTab('CORE_PROG003D0003S02Q', parent.getProgUrlForOid('CORE_PROG003D0003S02Q', oid) );
}

function deleteRecord(oid) {
	parent.bootbox.confirm(
			"Delete?", 
			function(result) { 
				if (!result) {
					return;
				}
				xhrSendParameter(
						'./sysBeanSupportExpressionDeleteJson', 
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
	id="CORE_PROG003D0003S01Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('CORE_PROG003D0003S01Q', '${sysBeanHelp.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnSave();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG003D0003S01Q');"
	programName="${programName}"
	programId="${programId}"
	description="Settings service bean support expression item.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			System&nbsp;:&nbsp;${sysBeanHelp.system}
			<p style="margin-bottom: 10px"></p>
			Bean Id&nbsp;:&nbsp;${sysBeanHelp.beanId}&nbsp;&nbsp;/&nbsp;&nbsp;Method&nbsp;:&nbsp;${sysBeanHelp.method}
		</div>
	</div>			
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="expressionMap" name="expressionOid" id="expressionOid" value="" requiredFlag="Y" label="Expression"></@qifu.select>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="exprSeq" id="exprSeq" value="" maxlength="10" requiredFlag="Y" label="Seq" placeholder="Enter process order seq"></@qifu.textbox>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="runTypeMap" name="runType" id="runType" value="" requiredFlag="Y" label="Process type"></@qifu.select>
		</div>
	</div>	
	
</div>

<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<button type="button" class="btn btn-primary" id="btnQuery" onclick="queryGrid();">Query</button>
		&nbsp;	
		<@qifu.button id="btnSave" label="Save"
			xhrUrl="./sysBeanSupportExpressionSaveJson"
			xhrParameter="	
			{
				'sysBeanHelpOid'	:	'${sysBeanHelp.oid}',
				'expressionOid'		:	$('#expressionOid').val(),
				'exprSeq'			:	$('#exprSeq').val(),
				'runType'			:	$('#runType').val()
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
		'parameter[helpOid]'		: '${sysBeanHelp.oid}',
		'select'					: getQueryGridSelect(),
		'showRow'					: getQueryGridShowRow()	
	}
	"
	xhrUrl="./sysBeanSupportExpressionQueryGridJson" 
	id="CORE_PROG003D0003S01Q_grid"
	queryFunction="queryGrid()"
	clearFunction="clearQueryGridTable()">
</@qifu.grid>

<br/>
<br/>
<br/>

</body>
</html>