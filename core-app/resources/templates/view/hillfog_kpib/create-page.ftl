<html>
<head>
<title>hillfog</title>
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
var selDeptList = [];
var selEmpList = [];

$( document ).ready(function() {
	
	/*
	$("#weight").change(function(){
		var valStr = $("#weight").val();
		$("#weightLabel").text('Weight: ' + valStr);
	});
	
	$("#weight").trigger('change');
	*/
	
	$("#management").change(function(){
		var valStr = $("#management").val();
		if ('3' == valStr) {
			$("#quasiRange").removeAttr('disabled');
		} else {
			$("#quasiRange").val('0');
			$("#quasiRange").attr('disabled', 'disabled');
		}
	});
	
	$("#management").trigger('change');
	
	$("#kpiOrga").autocomplete({
		source: orgDeptList
	}).focus(function() {
		if ($(this).val() == ' ' || $(this).val() =='') {
			$(this).autocomplete("search", " ");
		}
	});
	$("#kpiEmpl").autocomplete({
		source: empList
	}).focus(function() {
		if ($(this).val() == ' ' || $(this).val() =='') {
			$(this).autocomplete("search", " ");
		}
	});	
	
});

var msgFields = new Object();
msgFields['aggrOid'] 		= 'aggrOid';
msgFields['forOid'] 		= 'forOid';
msgFields['id'] 			= 'id';
msgFields['name'] 			= 'name';
msgFields['weight'] 		= 'weight';
msgFields['unit'] 			= 'unit';
msgFields['max'] 			= 'max';
msgFields['target'] 		= 'target';
msgFields['min'] 			= 'min';
msgFields['management'] 	= 'management';
msgFields['quasiRange'] 	= 'quasiRange';
msgFields['compareType'] 	= 'compareType';
msgFields['dataType'] 		= 'dataType';
msgFields['kpiOrga'] 		= 'kpiOrga';
msgFields['kpiEmpl'] 		= 'kpiEmpl';

function saveSuccess(data) {
	clearWarningMessageField(msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.notifyWarning( data.message );
		setWarningMessageField(msgFields, data.checkFields);
		return;
	}
	parent.notifyInfo( data.message );
	clearSave();
}

function clearSave() {
	clearWarningMessageField(msgFields);
	$("#aggrOid").val( _qifu_please_select_id );
	$("#forOid").val( _qifu_please_select_id );
	$("#id").val('');
	$("#name").val('');
	$("#weight").val('50');
	$("#weight").trigger('change');
	$("#unit").val('');
	$("#max").val('');
	$("#target").val('');
	$("#min").val('');
	$("#management").val( _qifu_please_select_id );
	$("#compareType").val( _qifu_please_select_id );
	$("#dataType").val( _qifu_please_select_id );
	$("#description").val('');
	$("#management").trigger('change');
	$("#kpiOrga").val('');
	$("#kpiEmpl").val('');
	selDeptList = [];
	selEmpList = [];
	paintOrganization();
	paintEmployee();
}

// ====================================================================

function addOrganization() {
	var inputOrgDept = $("#kpiOrga").val();
	if (null == inputOrgDept || '' == inputOrgDept) {
		parent.notifyInfo( 'Please input organization!' );
		return;
	}
	var checkInOrgDept = false;
	var checkInSelOrgDept = false;	
	for (var n in orgDeptList) {
		if ( orgDeptList[n] == inputOrgDept ) {
			checkInOrgDept = true;
		}
	}
	if (!checkInOrgDept) {
		parent.notifyInfo( 'Please input organization!' );
		return;		
	}
	for (var n in selDeptList) {
		if ( selDeptList[n] == inputOrgDept) {
			checkInSelOrgDept = true;
		}
	}
	if (checkInSelOrgDept) {
		parent.notifyInfo( 'Organization is add found!' );
		return;
	}
	selDeptList.push( inputOrgDept );
	$('#kpiOrga').val('');
	paintOrganization();
	
}
function paintOrganization() {
	$('#selOrgDeptShowLabel').html( '' );
	var htmlContent = '';
	for (var n in selDeptList) {
		htmlContent += '<span class="badge badge-secondary"><font size="3">' + selDeptList[n] + '</font><span class="badge badge-danger btn" onclick="delAddOrganization(' + n + ');">X</span></span>&nbsp;';
	}
	$('#selOrgDeptShowLabel').html( htmlContent );
}
function delAddOrganization(pos) {
	removeArrayByPos(selDeptList, pos);
	paintOrganization();	
}

function addEmployee() {
	var inputEmployee = $("#kpiEmpl").val();
	if (null == inputEmployee || '' == inputEmployee) {
		parent.notifyInfo( 'Please input employee!' );
		return;
	}
	var checkInEmployee = false;
	var checkInSelEmployee = false;	
	for (var n in empList) {
		if ( empList[n] == inputEmployee ) {
			checkInEmployee = true;
		}
	}
	if (!checkInEmployee) {
		parent.notifyInfo( 'Please input employee!' );
		return;		
	}
	for (var n in selEmpList) {
		if ( selEmpList[n] == inputEmployee) {
			checkInSelEmployee = true;
		}
	}
	if (checkInSelEmployee) {
		parent.notifyInfo( 'Employee is add found!' );
		return;
	}
	selEmpList.push( inputEmployee );
	$('#kpiEmpl').val('');
	paintEmployee();
	
}
function paintEmployee() {
	$('#selEmpShowLabel').html( '' );
	var htmlContent = '';
	for (var n in selEmpList) {
		htmlContent += '<span class="badge badge-secondary"><font size="3">' + selEmpList[n] + '</font><span class="badge badge-danger btn" onclick="delAddEmployee(' + n + ');">X</span></span>&nbsp;';
	}
	$('#selEmpShowLabel').html( htmlContent );
}
function delAddEmployee(pos) {
	removeArrayByPos(selEmpList, pos);
	paintEmployee();	
}

// ====================================================================

function removeArrayByPos(arr, pos) {
    for (var i = arr.length; i--;) {
    	if (pos == i) {
    		arr.splice(pos, 1);
    	}
    }
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG001D0005A_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('HF_PROG001D0005A');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnSave();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0005A');"
	programName="${programName}"
	programId="${programId}"
	description="Create KPI base item." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="card border-secondary">
  <div class="card-body">		
			<h4><span class="badge badge-pill badge-dark">Basic</span></h4>
			
<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="id" value="" id="id" label="Id" requiredFlag="Y" maxlength="14" placeholder="Enter Id" />
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="name" value="" id="name" label="Name" requiredFlag="Y" maxlength="100" placeholder="Enter name" />
		</div>		
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="unit" value="" id="unit" label="Unit" requiredFlag="Y" maxlength="20" placeholder="Enter unit" />
			<!--  
			<label for="weight" id="weightLabel">Weight&nbsp;(deprecated): </label>
			<input type="range" class="custom-range" min="0" max="100" value="50" id="weight" name="weight" readonly="readonly">			
			-->
			<input type="hidden" name="weight" id="weight" value="50">					
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			&nbsp;
		</div>		
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="formulaMap" name="forOid" id="forOid" value="" label="Formula" requiredFlag="Y"></@qifu.select>
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="aggrMethodMap" name="aggrOid" id="aggrOid" value="" label="Aggregation method" requiredFlag="Y"></@qifu.select>
		</div>
	</div>
</div>

  </div>
</div> 

<p style="margin-bottom: 10px"></p>

<div class="card border-secondary">
  <div class="card-body">		
			<h4><span class="badge badge-pill badge-dark">Parameter</span></h4>
			
<div class="form-group" id="form-group2">	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="max" value="" id="max" label="Maximum" requiredFlag="Y" maxlength="10" placeholder="Maximum" type="number" />
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="min" value="" id="min" label="Minimum" requiredFlag="Y" maxlength="10" placeholder="Minimum" type="number" />
		</div>	
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="target" value="" id="target" label="Target" requiredFlag="Y" maxlength="10" placeholder="Target" type="number" />
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="compareTypeMap" name="compareType" id="compareType" value="" label="Compare" requiredFlag="Y"></@qifu.select>
		</div>	
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="managementMap" name="management" id="management" value="" label="Management" requiredFlag="Y"></@qifu.select>
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="quasiRangeMap" name="quasiRange" id="quasiRange" value="" label="Quasi range" requiredFlag="Y"></@qifu.select>
		</div>
	</div>			
</div>

  </div>
</div> 

<p style="margin-bottom: 10px"></p>

<div class="card border-secondary">
  <div class="card-body">		
			<h4><span class="badge badge-pill badge-dark">Executor or executive department</span></h4>
			
<div class="form-group" id="form-group3">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="dataTypeMap" name="dataType" id="dataType" value="" label="Data type" requiredFlag="Y"></@qifu.select>
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			&nbsp;
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="kpiOrga" value="" id="kpiOrga" label="Organization" requiredFlag="Y" maxlength="100" placeholder="Enter organization" />
			<button type="button" class="btn btn-info" id="btnAddOrganization" title="add organization" onclick="addOrganization();"><i class="icon fa fa-plus"></i>&nbsp;ADD</button>
			<div>
				<span id="selOrgDeptShowLabel">&nbsp;</span>
			</div>					
		</div>		
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="kpiEmpl" value="" id="kpiEmpl" label="Employee" requiredFlag="Y" maxlength="100" placeholder="Enter employee" />
			<button type="button" class="btn btn-info" id="btnAddEmployee" title="add employee" onclick="addEmployee();"><i class="icon fa fa-plus"></i>&nbsp;ADD</button>
			<div>
				<span id="selEmpShowLabel">&nbsp;</span>
			</div>					
		</div>			
	</div>			
</div>

  </div>
</div> 

<p style="margin-bottom: 10px"></p>

<div class="form-group" id="form-group4">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			&nbsp;
		</div>
	</div>
</div>
		
<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnSave" label="<i class=\"icon fa fa-floppy-o\"></i>&nbsp;Save"
			xhrUrl="./hfKpiBaseSaveJson"
			xhrParameter="
			{
				'aggrOid'		:	$('#aggrOid').val(),
				'forOid'		:	$('#forOid').val(),
				'id'			:	$('#id').val(), 
				'name'			:	$('#name').val(),
				'weight'		:	$('#weight').val(),
				'unit'			:	$('#unit').val(),
				'max'			:	$('#max').val(),
				'target'		:	$('#target').val(),
				'min'			:	$('#min').val(),
				'management'	:	$('#management').val(),
				'compareType'	:	$('#compareType').val(),
				'dataType'		:	$('#dataType').val(),
				'quasiRange'	:	$('#quasiRange').val(),
				'description'	:	$('#description').val(),
				'selKpiDept'	:	JSON.stringify( { 'items' : selDeptList } ),
				'selKpiEmp'		:	JSON.stringify( { 'items' : selEmpList } )
			}
			"
			onclick="btnSave();"
			loadFunction="saveSuccess(data);"
			errorFunction="clearSave();" />
		<@qifu.button id="btnClear" label="<i class=\"icon fa fa-hand-paper-o\"></i>&nbsp;Clear" onclick="clearSave();" />
	</div>
</div>

<br/>
<br/>
<br/>

</body>
</html>