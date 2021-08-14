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
		{ name: "#", 										field: "oid", 	formatter: getQueryGridFormatter },
		{ name: "${getText('page.employee.account')}",		field: "account"		},
		{ name: "${getText('page.employee.empId')}",		field: "empId"			},
		{ name: "${getText('page.employee.nameLike')}",		field: "name"			},
		{ name: "${getText('page.employee.jobTitleLike')}",	field: "jobTitle"		},
		{ name: "${getText('page.employee.description')}",	field: "description"	}
	];
}

function queryClear() {
	$("#account").val('');
	$("#empId").val('');
	$("#nameLike").val('');
	$("#jobTitleLike").val('');
	
	clearQueryGridTable();
	
}  

function editPage(oid) {
	parent.addTab('HF_PROG001D0002E', parent.getProgUrlForOid('HF_PROG001D0002E', oid) );
}

function deleteRecord(oid) {
	parent.bootbox.confirm(
			"${getText('page.info.delete')}", 
			function(result) { 
				if (!result) {
					return;
				}
				xhrSendParameter(
						'./hfEmployeeDeleteJson', 
						{ 'oid' : oid }, 
						function(data) {
							if ( _qifu_success_flag != data.success ) {
								parent.notifyWarning( data.message );
							}
							if ( _qifu_success_flag == data.success ) {
								parent.notifyInfo( data.message );
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

function hierarchyPage() {
	parent.addTab('HF_PROG001D0002S', null);
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG001D0002Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('HF_PROG001D0002Q');" 
	createNewEnable="Y"
	createNewJsMethod="parent.addTab('HF_PROG001D0002A', null);"
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0002Q');"
	programName="${programName}"
	programId="${programId}"
	description="Management employee item." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 
      
      <div class="row">
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="account" value="" id="account" label="${getText('page.employee.account')}" placeholder="Enter account" maxlength="24" />
        </div>
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="empId" value="" id="empId" label="${getText('page.employee.empId')}" placeholder="Enter Id" maxlength="15" />
        </div>
      </div>
      <div class="row">                
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="nameLike" value="" id="nameLike" label="${getText('page.employee.nameLike')}" placeholder="Enter name" maxlength="25" />
       </div>      
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="jobTitleLike" value="" id="jobTitleLike" label="${getText('page.employee.jobTitleLike')}" placeholder="Enter job title" maxlength="100" />
       </div>                   
      </div>

<p style="margin-bottom: 10px"></p>
      
<button type="button" class="btn btn-primary" id="btnQuery" onclick="queryGrid();"><i class="icon fa fa-search"></i>&nbsp;${getText('page.button.query')}</button>
<button type="button" class="btn btn-primary" id="btnClear" onclick="queryClear();"><i class="icon fa fa-hand-paper-o"></i>&nbsp;${getText('page.button.clear')}</button>
&nbsp;&nbsp;
<button type="button" class="btn btn-info" id="btnHierarchy" onclick="hierarchyPage();"><i class="icon fa fa-users"></i>&nbsp;${getText('page.employee.btnHierarchy')}</button>

<p style="margin-bottom: 10px"></p>
<p style="margin-bottom: 10px"></p>

<@qifu.grid gridFieldStructure="getQueryGridHeader()" 
	xhrParameter="
	{
		'parameter[account]'		: $('#account').val(),
		'parameter[empId]'			: $('#empId').val(),
		'parameter[nameLike]'		: $('#nameLike').val(),
		'parameter[jobTitleLike]'	: $('#jobTitleLike').val(),
		'select'					: getQueryGridSelect(),
		'showRow'					: getQueryGridShowRow()
	}
	"
	xhrUrl="./hfEmployeeQueryGridJson" 
	id="HF_PROG001D0002Q_grid"
	queryFunction="queryGrid()"
	clearFunction="clearQueryGridTable()" />

<br/>
<br/>
<br/>

</body>
</html>