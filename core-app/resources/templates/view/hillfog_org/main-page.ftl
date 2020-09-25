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
	str += '<img alt="edit" title="Edit" src="./images/edit.png" onclick="editPage(\'' + value + '\');"/>';
	str += '&nbsp;&nbsp;';		
	str += '<img alt="delete" title="Delete" src="./images/delete.png" onclick="deleteRecord(\'' + value + '\');"/>';
	return str;
}
function getQueryGridHeader() {
	return [
		{ name: "#", 			field: "oid", 	formatter: getQueryGridFormatter },
		{ name: "Id", 			field: "orgId"			},
		{ name: "Name",			field: "name"			},
		{ name: "Description",	field: "description"	}
	];
}

function queryClear() {
	$("#orgId").val('');
	$("#nameLike").val('');
	
	clearQueryGridTable();
	
}  

function editPage(oid) {
	parent.addTab('HF_PROG001D0001E', parent.getProgUrlForOid('HF_PROG001D0001E', oid) );
}

function deleteRecord(oid) {
	parent.bootbox.confirm(
			"Delete?", 
			function(result) { 
				if (!result) {
					return;
				}
				xhrSendParameter(
						'./hfOrgDeptDeleteJson', 
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
	id="HF_PROG001D0001Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('HF_PROG001D0001Q');" 
	createNewEnable="Y"
	createNewJsMethod="parent.addTab('HF_PROG001D0001A', null);"
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0001Q');"
	programName="${programName}"
	programId="${programId}"
	description="Management organization / department item." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 
      
      <div class="row">
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="orgId" value="" id="orgId" label="Id" placeholder="Enter Id" maxlength="50" />
        </div>
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="nameLike" value="" id="nameLike" label="Name" placeholder="Enter name" maxlength="100" />
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
		'parameter[orgId]'		: $('#orgId').val(),
		'parameter[nameLike]'	: $('#nameLike').val(),
		'select'				: getQueryGridSelect(),
		'showRow'				: getQueryGridShowRow(),
		'sortType'				: $('#sortType').val(),
		'orderBy'				: $('#orderBy').val()
	}
	"
	xhrUrl="./hfOrgDeptQueryGridJson" 
	id="HF_PROG001D0001Q_grid"
	queryFunction="queryGrid()"
	clearFunction="clearQueryGridTable()" />

</body>
</html>