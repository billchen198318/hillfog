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
	str += '<img alt="delete" title="Delete" src="./images/delete.png" onclick="deleteRecord(\'' + value + '\');"/>';
	return str;
}
function getQueryGridHeader() {
	return [
		{ name: "#", 				field: "oid", 		formatter: getQueryGridFormatter },
		{ name: "Report Id", 		field: "reportId"		},
		{ name: "URL parameter", 	field: "urlParam"		},
		{ name: "Report variable", 	field: "rptParam"		}
	];
}

var msgFields = new Object();
msgFields['urlParam']	= 'urlParam';
msgFields['rptParam'] 	= 'rptParam';

var formGroups = new Object();
formGroups['urlParam']	= 'form-group1';
formGroups['rptParam']	= 'form-group1';

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
	$("#urlParam").val( '' );
	$("#rptParam").val( '' );
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
						'./sysJreportParamDeleteJson', 
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
	id="CORE_PROG001D0005S01Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('CORE_PROG001D0005S01Q', '${sysJreport.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnSave();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG001D0005S01Q');"
	programName="${programName}"
	programId="${programId}"
	description="Settings report parameter mapper.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
        <div class="col-xs-6 col-md-6 col-lg-6">
        	Report&nbsp;:&nbsp;${sysJreport.reportId}
        </div>
	</div>        
	<div class="row">
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="urlParam" value="" id="urlParam" label="URL parameter" requiredFlag="Y" maxlength="100" placeholder="Enter URL parameter"></@qifu.textbox>
       </div>
	</div>
	<div class="row">
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="rptParam" value="" id="rptParam" label="Report variable" requiredFlag="Y" maxlength="100" placeholder="Enter report variable"></@qifu.textbox>
       </div>
	</div>	
</div>

<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<button type="button" class="btn btn-primary" id="btnQuery" onclick="queryGrid();"><i class="icon fa fa-search"></i>&nbsp;Query</button>
		&nbsp;
		<@qifu.button id="btnSave" label="<i class=\"icon fa fa-floppy-o\"></i>&nbsp;Save"
			xhrUrl="./sysJreportParamSaveJson"
			xhrParameter="
			{
				'reportOid'	:	'${sysJreport.oid}',
				'urlParam'	:	$('#urlParam').val(),
				'rptParam'	:	$('#rptParam').val()
			}
			"
			onclick="btnSave();"
			loadFunction="saveSuccess(data);"
			errorFunction="clearSave();">
		</@qifu.button>
		<@qifu.button id="btnClear" label="<i class=\"icon fa fa-hand-paper-o\"></i>&nbsp;Clear" onclick="clearSave();"></@qifu.button>
	</div>
</div>

<p style="margin-bottom: 10px"></p>
<p style="margin-bottom: 10px"></p>

<@qifu.grid gridFieldStructure="getQueryGridHeader()" 
	xhrParameter="
	{
		'parameter[sysJreportOid]'	: '${sysJreport.oid}',
		'select'					: getQueryGridSelect(),
		'showRow'					: getQueryGridShowRow()	
	}
	"
	xhrUrl="./sysJreportParamQueryGridJson" 
	id="CORE_PROG001D0005S01Q_grid"
	queryFunction="queryGrid()"
	clearFunction="clearQueryGridTable()">
</@qifu.grid>

<br/>
<br/>
<br/>

</body>
</html>