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
		{ name: "Id", 			field: "exprId"			},
		{ name: "Name",			field: "name"			},
		{ name: "Type", 		field: "type"			},
		{ name: "Description",	field: "description"	}
	];
}

function queryClear() {
	$("#type").val( _qifu_please_select_id );
	$("#exprId").val('');
	$("#name").val('');
	
	clearQueryGridTable();
	
}  

function editPage(oid) {
	parent.addTab('CORE_PROG003D0002E', parent.getProgUrlForOid('CORE_PROG003D0002E', oid) );
}

function deleteRecord(oid) {
	parent.bootbox.confirm(
			"Delete?", 
			function(result) { 
				if (!result) {
					return;
				}
				xhrSendParameter(
						'./sysExpressionDeleteJson', 
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
	id="CORE_PROG003D0002Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('CORE_PROG003D0002Q');" 
	createNewEnable="Y"
	createNewJsMethod="parent.addTab('CORE_PROG003D0002A', null);"
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG003D0002Q');"
	programName="${programName}"
	programId="${programId}"
	description="Management expression item.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

      <div class="row">     
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="exprId" value="" id="exprId" label="Id" placeholder="Enter Id" maxlength="20"></@qifu.textbox>
        </div>
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="name" value="" id="name" label="Name" placeholder="Enter name" maxlength="100"></@qifu.textbox>
        </div>
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.select dataSource="typeMap" name="type" id="type" value="" label="Type"></@qifu.select>
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
		'parameter[type]'		: $('#type').val(),
		'parameter[exprId]'		: $('#exprId').val(),
		'parameter[name]'		: $('#name').val(),
		'select'				: getQueryGridSelect(),
		'showRow'				: getQueryGridShowRow()	
	}
	"
	xhrUrl="./sysExpressionQueryGridJson" 
	id="CORE_PROG003D0002Q_grid"
	queryFunction="queryGrid()"
	clearFunction="clearQueryGridTable()">
</@qifu.grid>

</body>
</html>