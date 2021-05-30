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
	str += '<img alt="edit" title="View" src="./images/view.png" onclick="viewDetail(\'' + value + '\');"/>';
	str += '&nbsp;&nbsp;';			
	str += '<img alt="edit" title="Edit" src="./images/edit.png" onclick="editPage(\'' + value + '\');"/>';
	str += '&nbsp;&nbsp;';		
	str += '<img alt="delete" title="Delete" src="./images/delete.png" onclick="deleteRecord(\'' + value + '\');"/>';
	return str;
}
function getQueryGridHeader() {
	return [
		{ name: "#", 				field: "oid", 	formatter: getQueryGridFormatter },
		{ name: "Number", 			field: "pdcaNum"			},
		{ name: "Name", 			field: "name"				},
		{ name: "Start/End date",	field: "startEndDateShow"	},
		{ name: "Close",			field: "confirm"			}
	];
}

function queryClear() {
	var masterTypeRadios = $('input:radio[name=masterTypeRadioOptions]');
	masterTypeRadios.filter('[value=all]').prop('checked', true);
	var confirmRadio = $('input:radio[name=confirmRadioOptions]');
	confirmRadio.filter('[value=all]').prop('checked', true);	
	$("#name").val('');
	$("#pdcaNum").val('');
	$("#startDate").val('');
	$("#endDate").val('');
	clearQueryGridTable();
	
}  

function viewDetail(oid) {
	parent.addTab('HF_PROG004D0001V', parent.getProgUrlForOid('HF_PROG004D0001V', oid) );
}

function editPage(oid) {
	parent.addTab('HF_PROG004D0001E', parent.getProgUrlForOid('HF_PROG004D0001E', oid) );
}

function deleteRecord(oid) {
	parent.bootbox.confirm(
			"Delete?", 
			function(result) { 
				if (!result) {
					return;
				}
				xhrSendParameter(
						'./hfPdcaDeleteJson', 
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


function getMstTypeRadioButtonValue() {
	var mstType = $("input:radio[name=masterTypeRadioOptions]:checked").val();
	if ( _qifu_please_select_id == mstType ) {
		return '';
	}
	return mstType;
}

function getConfirmRadioButtonValue() {
	var confirm = $("input:radio[name=confirmRadioOptions]:checked").val();
	if ( _qifu_please_select_id == confirm ) {
		return '';
	}
	return confirm;
}

function replaceAll(str, find, replace) {
    return str.replace(new RegExp(find, 'g'), replace);
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG004D0001Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('HF_PROG004D0001Q');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG004D0001Q');"
	programName="${programName}"
	programId="${programId}"
	description="Management PDCA item." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

      <div class="row">
        <div class="col-xs-6 col-md-6 col-lg-6">     	
			<div class="form-check form-check-inline">
			  <input class="form-check-input" type="radio" name="masterTypeRadioOptions" id="masterTypeRadio1" value="all" checked>
			  <label class="form-check-label" for="masterTypeRadio1">All</label>
			</div>
			<div class="form-check form-check-inline">
			  <input class="form-check-input" type="radio" name="masterTypeRadioOptions" id="masterTypeRadio2" value="O">
			  <label class="form-check-label" for="masterTypeRadio2">Objectives</label>
			</div>
 			<div class="form-check form-check-inline">
			  <input class="form-check-input" type="radio" name="masterTypeRadioOptions" id="masterTypeRadio3" value="K">
			  <label class="form-check-label" for="masterTypeRadio3">KPI</label>
			</div>       	
        </div>

        <div class="col-xs-6 col-md-6 col-lg-6">
			<div class="form-check form-check-inline">
			  <input class="form-check-input" type="radio" name="confirmRadioOptions" id="confirmRadio1" value="all" checked>
			  <label class="form-check-label" for="confirmRadio1">All</label>
			</div>
			<div class="form-check form-check-inline">
			  <input class="form-check-input" type="radio" name="confirmRadioOptions" id="confirmRadio2" value="Y">
			  <label class="form-check-label" for="confirmRadio2">Closed</label>
			</div>
 			<div class="form-check form-check-inline">
			  <input class="form-check-input" type="radio" name="confirmRadioOptions" id="confirmRadio3" value="N">
			  <label class="form-check-label" for="confirmRadio3">Open-case</label>
			</div>        	
       </div>
      </div> 
      
      <p style="margin-bottom: 10px"></p>
      
      <div class="row">
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="name" value="" id="name" label="Name" placeholder="Enter name" maxlength="100" />
        </div>
        <div class="col-xs-6 col-md-6 col-lg-6">
        	<@qifu.textbox name="pdcaNum" value="" id="pdcaNum" label="PDCA Number" placeholder="Enter number" maxlength="100" />
       </div>
      </div>
      <div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox type="date" name="startDate" value="" id="startDate" label="Start" requiredFlag="Y" maxlength="10" placeholder="Enter start date" />
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox type="date" name="endDate" value="" id="endDate" label="End" requiredFlag="Y" maxlength="10" placeholder="Enter end date" />
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
		'parameter[mstType]'	: getMstTypeRadioButtonValue(),
		'parameter[confirm]'	: getConfirmRadioButtonValue(),
		'parameter[nameLike]'	: $('#name').val(),
		'parameter[pdcaNumLike]': $('#pdcaNum').val(),
		'parameter[startDate]'	: ($('#startDate').val() != null ? replaceAll($('#startDate').val(), '-', '') : ''),
		'parameter[endDate]'	: ($('#endDate').val() != null ? replaceAll($('#endDate').val(), '-', '') : ''),		
		'select'				: getQueryGridSelect(),
		'showRow'				: getQueryGridShowRow()
	}
	"
	xhrUrl="./hfPdcaQueryGridJson" 
	id="HF_PROG004D0001Q_grid"
	queryFunction="queryGrid()"
	clearFunction="clearQueryGridTable()" />

<br/>
<br/>
<br/>

</body>
</html>