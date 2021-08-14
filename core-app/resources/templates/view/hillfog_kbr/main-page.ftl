<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<script type="text/javascript" src="${qifu_basePath}js/jquery-ui.min.js?ver=${qifu_jsVerBuild}"></script>

<link rel="stylesheet" href="${qifu_basePath}highcharts-gantt/css/highcharts.css">
<script src="${qifu_basePath}highcharts-gantt/highcharts-gantt.js"></script>

<style type="text/css">

.ui-autocomplete {
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 1000;
  display: none;
  float: left;
  min-width: 160px;
  padding: 5px 0;
  margin: 2px 0 0;
  list-style: none;
  font-size: 14px;
  text-align: left;
  background-color: #ffffff;
  border: 1px solid #cccccc;
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: 4px;
  -webkit-box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
  background-clip: padding-box;
}

.ui-autocomplete > li > div {
  display: block;
  padding: 3px 20px;
  clear: both;
  font-weight: normal;
  line-height: 1.42857143;
  color: #333333;
  white-space: nowrap;
}

.ui-state-hover,
.ui-state-active,
.ui-state-focus {
  text-decoration: none;
  color: #262626;
  background-color: #f5f5f5;
  cursor: pointer;
}

.ui-helper-hidden-accessible {
  border: 0;
  clip: rect(0 0 0 0);
  height: 1px;
  margin: -1px;
  overflow: hidden;
  padding: 0;
  position: absolute;
  width: 1px;
}

</style>


<script type="text/javascript">

var orgDeptList = [ ${orgInputAutocomplete} ];
var empList = [ ${empInputAutocomplete} ];
var dateStatus = "0";

$( document ).ready(function() {
	
	$("#frequency").change(function(){	
		changeQueryButtonStatus();
	});
	$("#frequency").val('3'); // default month
	$("#frequency").trigger('change');
	
	$("#kpiOrga").autocomplete({
		source: orgDeptList,
		select: function( event, ui ) {
			$("#kpiOrga").val( ui.item.label );
			$("#kpiOrga").trigger('change');
		}
	}).focus(function() {
		$(this).autocomplete("search", " ");
	});
	$("#kpiEmpl").autocomplete({
		source: empList,
		select: function( event, ui ) {
			$("#kpiEmpl").val( ui.item.label );
			$("#kpiEmpl").trigger('change');
		}
	}).focus(function() {
		$(this).autocomplete("search", " ");
	});		
	
	$("#noDistinction").change(function(){
		if($(this).is(":checked")) {
			$("#kpiEmpl").val( '' );
			$("#kpiOrga").val( '' );			
			$("#kpiOrga").prop("readonly", true);
			$("#kpiEmpl").prop("readonly", true);
			$("#orgaOrEmplDiv").hide();
		} else {
			$("#kpiEmpl").val( '' );
			$("#kpiOrga").val( '' );			
			$("#kpiOrga").prop("readonly", false);
			$("#kpiEmpl").prop("readonly", false);
			$("#orgaOrEmplDiv").show();
		}
		changeQueryButtonStatus();
	});	
	
	$("#kpiOrga").change(function(){
		var inputOrgDept = $(this).val();
		var checkInOrgDept = false;
		for (var n in orgDeptList) {
			if ( orgDeptList[n] == inputOrgDept ) {
				checkInOrgDept = true;
			}
		}
		if (checkInOrgDept) {
			$("#kpiEmpl").val('');
		} else {
			$("#kpiOrga").val('');
		}
		changeQueryButtonStatus();
	});
	$("#kpiEmpl").change(function(){
		var inputEmployee = $(this).val();
		var checkInEmployee = false;
		for (var n in empList) {
			if ( empList[n] == inputEmployee ) {
				checkInEmployee = true;
			}
		}
		if (checkInEmployee) {
			$("#kpiOrga").val('');
		} else {
			$("#kpiEmpl").val('');
		}
		changeQueryButtonStatus();
	});
	
	$("#kpiOid").change(function(){
		orgDeptList.splice(0);
		empList.splice(0);
		$("#kpiEmpl").val('');
		$("#kpiOrga").val('');
		$("#kpiEmpl").trigger('change');
		xhrSendParameter(
				'./hfCommonKpiInputAutocompleteJson', 
				{
					'oid'	:	$("#kpiOid").val()
				}, 
				function(data) {
					if ( _qifu_success_flag != data.success ) {
						parent.notifyWarning( data.message );
						return;
					}
					if ( _qifu_success_flag == data.success ) {
						parent.notifyInfo( data.message );
						for (var d in data.value.org) {
							orgDeptList.push( data.value.org[d] );
						}
						for (var d in data.value.emp) {
							empList.push( data.value.emp[d] );
						}
					}
				}, 
				function() {
					
				},
				_qifu_defaultSelfPleaseWaitShow
		);		
	});
	
	$("#date1").change(function(){
		var freq = $("#frequency").val();
		var bDateStr = $("#date1").val();
		if ( _qifu_please_select_id == freq ) {
			return;
		}
		var nDateStr = getStartDate(freq, bDateStr);
		if (bDateStr != nDateStr && nDateStr != null && nDateStr.length == 10) {
			$("#date1").val(nDateStr);
			parent.notifyInfo( 'reset start date!' );
		}
	});
	$("#date2").change(function(){
		var freq = $("#frequency").val();
		var bDateStr = $("#date2").val();
		if ( _qifu_please_select_id == freq ) {
			return;
		}
		var nDateStr = getEndDate(freq, bDateStr);
		if (bDateStr != nDateStr && nDateStr != null && nDateStr.length == 10) {
			$("#date2").val(nDateStr);
			parent.notifyInfo( 'reset end date!' );
		}
	});	
	
	$("#noDistinction").trigger('click');
	
	window.addEventListener('resize',function(){
		for (var n = 0; !(_chartsArr === undefined) && null != _chartsArr && n < _chartsArr.length; n++) {
			_chartsArr[n].resize();
		}
	});	
	
});

function changeQueryButtonStatus() {
	$("#date1").trigger('change');
	$("#date2").trigger('change');	
	var freq = $("#frequency").val();
	var kpiEmpl = $("#kpiEmpl").val();
	var kpiOrga = $("#kpiOrga").val();
	if ( _qifu_please_select_id == freq || (!$('#noDistinction').is(':checked') && ( kpiEmpl == '' && kpiOrga == '' )) ) {
		$("#content").html('<br><span class="badge badge-warning"><h6>Please select Frequency and Organization or Employee!</h6></span><br>');
		$("#btnQuery").attr('disabled', 'disabled');
		$("#btnClear").attr('disabled', 'disabled');
		return;
	}
	$("#btnQuery").removeAttr('disabled');
	$("#btnClear").removeAttr('disabled');
	$("#content").html('&nbsp;');		
}

function queryReport() {
	if ($("#btnQuery").is(':disabled')) {
		parent.notifyWarning( 'Please select Frequency and Organization or Employee!' );
		return;
	}
	var date1 = $("#date1").val();
	var date2 = $("#date2").val();
	if ('' == date1 || '' == date2) {
		parent.notifyWarning( 'Please input start & end date!' );
		return;		
	}
	$("#content").html( '&nbsp;' );
	xhrSendParameter(
			'./hfKpiReportContentDataJson', 
			{
				'kpiOid'		:	$("#kpiOid").val(),
				'date1'			:	$("#date1").val(),
				'date2'			:	$("#date2").val(),
				'frequency'		:	$("#frequency").val(),
				'kpiEmpl'		:	$("#kpiEmpl").val(),
				'kpiOrga'		:	$("#kpiOrga").val()
			}, 
			function(data) {
				if ( _qifu_success_flag != data.success ) {
					parent.notifyWarning( data.message );
					$("#content").html('<br><span class="badge badge-warning"><h6>' + data.message + '</h6></span><br>');
					return;
				}
				if ( _qifu_success_flag == data.success ) {
					parent.notifyInfo( data.message );
					showContent( data.value );
				}
			}, 
			function() {
				$("#content").html('<br><span class="badge badge-warning"><h6>no result!</h6></span><br>');
			},
			_qifu_defaultSelfPleaseWaitShow
	);	
}

function queryClear() {
	$("#content").html('<br><span class="badge badge-warning"><h6>Please select Frequency and Organization or Employee!</h6></span><br>');
	$("#btnQuery").attr('disabled', 'disabled');
	$("#btnClear").attr('disabled', 'disabled');
	$("#kpiEmpl").val('');
	$("#kpiOrga").val('');
	$("#date1").val('');
	$("#date2").val('');
	$("#kpiOid").val( _qifu_please_select_id );
	$("#frequency").val('3');
	$('#noDistinction').prop('checked', false);
	$('#noDistinction').trigger( 'change' );	
}

function btnCreatePdca(kpiOid) {
	parent.addTab('HF_PROG004D0001A', parent.getProgUrlForOid('HF_PROG004D0001A', kpiOid) + '&masterType=K' );
}

function viewDetail(pdcaOid) {
	parent.addTab('HF_PROG004D0001V', parent.getProgUrlForOid('HF_PROG004D0001V', pdcaOid));
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG002D0001Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('HF_PROG002D0001Q');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG002D0001Q');"
	programName="${programName}"
	programId="${programId}"
	description="Query KPI report & chart." />	
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 
    
	<div class="row">
		<div class="col p-2"> <!-- bg-secondary rounded -->
			<div class="row">
				<div class="col-xs-6 col-md-6 col-lg-6">
					<@qifu.select dataSource="frequencyMap" name="frequency" id="frequency" value="" label="Frequency" requiredFlag="Y"></@qifu.select>
				</div>
				<div class="col-xs-6 col-md-6 col-lg-6">
					<@qifu.select dataSource="kpiMap" name="kpiOid" id="kpiOid" value="" label="KPI"></@qifu.select>
				</div>
			</div>	
			<div class="row">
				<div class="col-xs-6 col-md-6 col-lg-6">
					<@qifu.textbox type="date" name="date1" value="date1" id="date1" label="Start" requiredFlag="Y" maxlength="10" placeholder="Enter start date" />
				</div>
				<div class="col-xs-6 col-md-6 col-lg-6">
					<@qifu.textbox type="date" name="date2" value="date2" id="date2" label="End" requiredFlag="Y" maxlength="10" placeholder="Enter end date" />
				</div>		
			</div>						
			<div class="row" id="orgaOrEmplDiv">
				<div class="col-xs-6 col-md-6 col-lg-6">
					<@qifu.textbox name="kpiOrga" value="" id="kpiOrga" label="Organization" requiredFlag="Y" maxlength="100" placeholder="Enter organization" />
				</div>
				<div class="col-xs-6 col-md-6 col-lg-6">
					<@qifu.textbox name="kpiEmpl" value="" id="kpiEmpl" label="Employee" requiredFlag="Y" maxlength="100" placeholder="Enter employee" />
				</div>		
			</div>	
			<div class="row">
				<div class="col-xs-6 col-md-6 col-lg-6">
					
					<p style="margin-bottom: 10px"></p>
					
					<div class="custom-control custom-checkbox">
						&nbsp;
						<input type="checkbox" class="custom-control-input" id="noDistinction" name="noDistinction">
						<label class="custom-control-label" for="noDistinction">No distinction between employee or department measure-data.</label>
					</div>					
					
					<p style="margin-bottom: 10px"></p>
				
					<button type="button" class="btn btn-success" id="btnQuery" onclick="queryReport();"><i class="icon fa fa-search"></i>&nbsp;Query</button>
					&nbsp;
					<button type="button" class="btn btn-success" id="btnClear" onclick="queryClear();"><i class="icon fa fa-hand-paper-o"></i>&nbsp;Clear</button>				
				</div>
			</div>				
		</div>
	</div>

<br/>
	
	<span id="content"></span>

<br/>
<br/>
<br/>

<script src="${qifu_basePath}echarts/echarts.min.js"></script>
<script type="text/javascript" src="${qifu_basePath}js/hillfog/HF_PROG002D0001Q.js?ver=${qifu_jsVerBuild}"></script>
<script type="text/javascript" src="${qifu_basePath}js/hillfog/HF_FREQUENCY_DAY.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>