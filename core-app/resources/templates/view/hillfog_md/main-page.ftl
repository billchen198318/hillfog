<html>
<head>
<title>qifu3</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<script type="text/javascript" src="${qifu_basePath}js/jquery-ui.min.js?ver=${qifu_jsVerBuild}"></script>

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
		paintContent();
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
		if ($(this).val() == ' ' || $(this).val() =='') {
			$(this).autocomplete("search", " ");
		}
	});
	$("#kpiEmpl").autocomplete({
		source: empList,
		select: function( event, ui ) {
			$("#kpiEmpl").val( ui.item.label );
			$("#kpiEmpl").trigger('change');
		}
	}).focus(function() {
		if ($(this).val() == ' ' || $(this).val() =='') {
			$(this).autocomplete("search", " ");
		}
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
		paintContent();
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
		paintContent();
	});
	
});

function clearUpdate() {
	paintContent( _qifu_please_select_id );
}

function prevCalendar() {
	dateStatus = "-1";
	paintContent();
}

function nextCalendar() {
	dateStatus = "1";
	paintContent();
}

function paintContent() {
	var freq = $("#frequency").val();
	var kpiEmpl = $("#kpiEmpl").val();
	var kpiOrga = $("#kpiOrga").val();
	if ( _qifu_please_select_id == freq || ('' == kpiEmpl && '' == kpiOrga) ) {
		$("#content").html('<br><span class="badge badge-warning"><h6>Please select Frequency and Organization or Employee!</h6></span><br>');
		$("#btnUpdate").attr('disabled', 'disabled');
		$("#btnClear").attr('disabled', 'disabled');
		return;
	}
	$("#btnUpdate").removeAttr('disabled');
	$("#btnClear").removeAttr('disabled');
	$("#content").html('&nbsp;');
	
	xhrSendParameter(
			'./hfMeasureDataBodyJson', 
			{
				'kpiOid'		:	'${kpi.oid}',
				'date'			:	$("#date").val(),
				'frequency'		:	$("#frequency").val(),
				'kpiEmpl'		:	$("#kpiEmpl").val(),
				'kpiOrga'		:	$("#kpiOrga").val(),
				'dateStatus'	:	dateStatus
			}, 
			function(data) {
				dateStatus = "0";
				if ( _qifu_success_flag != data.success ) {
					parent.toastrWarning( data.message );
				}
				if ( _qifu_success_flag == data.success ) {
					$("#content").html( data.value.content );
					$("#date").val( data.value.date );
				}
			}, 
			function() {
				dateStatus = "0";
			},
			_qifu_defaultSelfPleaseWaitShow
	);
	
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG001D0005M_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('HF_PROG001D0005M', '${kpi.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0005M');"
	programName="${programName}"
	programId="${programId}"
	description="Modify KPI measure data." />	
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 
    
    <input type="hidden" name="date" id="date" value="${systemDate}">
    
	<div class="row">
		<div class="col p-2 bg-secondary rounded">
			<span class="badge badge-info"><h6>${kpi.id}&nbsp;-&nbsp;${kpi.name}</h6></span>
			<span class="badge badge-danger">
				<h6>${kpi.managementName}</h6>
			</span>			
			<span class="badge badge-warning"><h6>Target:&nbsp;${kpi.target}&nbsp;，Maximum:&nbsp;${kpi.max}&nbsp;，Minimum:&nbsp;${kpi.min}</h6></span>
			<span class="badge badge-dark"><h6>Weight:&nbsp;${kpi.weight}&nbsp;，Unit:&nbsp;${kpi.unit}</h6></span>
			<span class="badge badge-dark"><h6>Formula:&nbsp;${formula.forId}-${formula.name}</h6></span>
			<span class="badge badge-dark"><h6>Aggregation:&nbsp;${aggrMethod.aggrId}-${aggrMethod.name}</h6></span>
			
			<div class="text-white">
				<@qifu.select dataSource="frequencyMap" name="frequency" id="frequency" value="" label="Frequency" requiredFlag="Y"></@qifu.select>
			</div>
			<div class="text-white">
				<@qifu.textbox name="kpiOrga" value="" id="kpiOrga" label="Organization" requiredFlag="Y" maxlength="100" placeholder="Enter organization" />
			</div>
			<div class="text-white">
				<@qifu.textbox name="kpiEmpl" value="" id="kpiEmpl" label="Employee" requiredFlag="Y" maxlength="100" placeholder="Enter employee" />
			</div>
			
		</div>
	</div>

	<form id="measureDataForm" name="measureDataForm" action=".">
		
		<br>
		
		<span id="content"></span>
		
	</form>
	
<p style="margin-bottom: 10px"></p>
      
<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnUpdate" label="Save"
			xhrUrl="./hfMeasureDataUpdateJson"
			xhrParameter="
			{
			}
			"
			onclick="btnUpdate();"
			loadFunction="updateSuccess(data);"
			errorFunction="clearUpdate();" />
		<@qifu.button id="btnClear" label="Clear" onclick="clearUpdate();" />
	</div>
</div>

<br/>
<br/>
<br/>

</body>
</html>