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
	
	
});

function getQueryGridFormatter(value) {
	var str = '';
	str += '<img alt="edit" title="Edit" src="./images/edit.png" onclick="editPage(\'' + value + '\');"/>';
	str += '&nbsp;&nbsp;';		
	str += '<img alt="delete" title="Delete" src="./images/delete.png" onclick="deleteRecord(\'' + value + '\');"/>';
	return str;
}
function getQueryGridHeader() {
	return [
		{ name: "#", 			field: "oid", 	formatter: getQueryGridFormatter },
		{ name: "Name",			field: "name"			},
		{ name: "Create time",	field: "cdateString"	}
	];
}

function queryClear() {
	$("#nameLike").val('');
	
	clearQueryGridTable();
	
}  

function editPage(oid) {
	parent.addTab('HF_PROG001D0008E', parent.getProgUrlForOid('HF_PROG001D0008E', oid) );
}

function deleteRecord(oid) {
	parent.bootbox.confirm(
			"Delete?", 
			function(result) { 
				if (!result) {
					return;
				}
				xhrSendParameter(
						'./hfScorecardDeleteJson', 
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
	id="HF_PROG001D0008Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('HF_PROG001D0008Q');" 
	createNewEnable="Y"
	createNewJsMethod="parent.addTab('HF_PROG001D0008A', null);"
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0008Q');"
	programName="${programName}"
	programId="${programId}"
	description="Management scorecard." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 
      
      <div class="row">
        <div class="col-xs-12 col-md-12 col-lg-12">
        	<@qifu.textbox name="nameLike" value="" id="nameLike" label="Name" placeholder="Enter name" maxlength="100" />
       </div>
      </div>

<p style="margin-bottom: 10px"></p>
      
<button type="button" class="btn btn-primary" id="btnQuery" onclick="queryGrid();"><i class="icon fa fa-search"></i>&nbsp;Query</button>
<button type="button" class="btn btn-primary" id="btnClear" onclick="queryClear();"><i class="icon fa fa-hand-paper-o"></i>&nbsp;Clear</button>

<p style="margin-bottom: 10px"></p>
<p style="margin-bottom: 10px"></p>

<@qifu.grid gridFieldStructure="getQueryGridHeader()" 
	xhrParameter="
	{
		'parameter[nameLike]'	: $('#nameLike').val(),
		'select'				: getQueryGridSelect(),
		'showRow'				: getQueryGridShowRow()
	}
	"
	xhrUrl="./hfScorecardQueryGridJson" 
	id="HF_PROG001D0008Q_grid"
	queryFunction="queryGrid()"
	clearFunction="clearQueryGridTable()" />

<br/>
<br/>
<br/>

</body>
</html>