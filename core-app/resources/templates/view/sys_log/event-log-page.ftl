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
	
	
});

function getQueryGridFormatter(value) {
	var str = '';
	str += '<img alt="delete" title="Delete" src="./images/delete.png" onclick="deleteRecord(\'' + value + '\');"/>';
	return str;
}
function getQueryGridHeader() {
	return [
		{ name: "#", 			field: "oid", 	formatter: getQueryGridFormatter },
		{ name: "System",		field: "sysId"			},
		{ name: "User", 		field: "user"			},
		{ name: "Event", 		field: "executeEvent"	},
		{ name: "Permit", 		field: "isPermit"		},
		{ name: "Date time", 	field: "cdateString"	}
	];
}

function queryClear() {
	$("#user").val('');
	$("#sysId").val('');
	
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
						'./sysEventLogDeleteJson', 
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
	id="CORE_PROG004D0001Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('CORE_PROG004D0001Q');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG004D0001Q');"
	programName="${programName}"
	programId="${programId}"
	description="Event history log.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

      <div class="row">     
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="user" value="" id="user" label="Account" placeholder="Enter account" maxlength="24"></@qifu.textbox>
        </div>
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="sysId" value="" id="sysId" label="System Id" placeholder="Enter system id" maxlength="10"></@qifu.textbox>
        </div>       
      </div>
      
<p style="margin-bottom: 10px"></p>
      
<button type="button" class="btn btn-primary" id="btnQuery" onclick="queryGrid();">Query</button>
<button type="button" class="btn btn-primary" id="btnClear" onclick="queryClear();">Clear</button>

<p style="margin-bottom: 10px"></p>
<p style="margin-bottom: 10px"></p>

<@qifu.grid gridFieldStructure="getQueryGridHeader()" 
	xhrParameter="
	{
		'parameter[user]'		: $('#user').val(),
		'parameter[sysId]'		: $('#sysId').val(),
		'select'				: getQueryGridSelect(),
		'showRow'				: getQueryGridShowRow()	
	}
	"
	xhrUrl="./sysEventLogQueryGridJson" 
	id="CORE_PROG004D0001Q_grid"
	queryFunction="queryGrid()"
	clearFunction="clearQueryGridTable()">
</@qifu.grid>

<p style="margin-bottom: 10px"></p>
<p style="margin-bottom: 10px"></p>

<@qifu.button id="btnDeleteAll" label="Clear log"
	xhrUrl="./sysEventLogDeleteAllJson"
	xhrParameter="{	}"
	onclick="btnDeleteAll();"
	loadFunction="queryGrid();"
	errorFunction=""
	cssClass="btn btn-warning"
	bootboxConfirm="Y"
	bootboxConfirmTitle="Clear(delete) all log">
</@qifu.button>

</body>
</html>