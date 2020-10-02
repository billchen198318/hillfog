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
	
	$("#weight").change(function(){
		var valStr = $("#weight").val();
		$("#weightLabel").text('Weight: ' + valStr);
	});
	
	$("#weight").trigger('change');
	
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
msgFields['compareType'] 	= 'compareType';
msgFields['dataType'] 		= 'dataType';

var formGroups = new Object();
formGroups['aggrId'] 		= 'form-group1';
formGroups['aggrOid'] 		= 'form-group1';
formGroups['forOid'] 		= 'form-group1';
formGroups['id'] 			= 'form-group1';
formGroups['name'] 			= 'form-group1';
formGroups['weight'] 		= 'form-group1';
formGroups['unit'] 			= 'form-group1';
formGroups['max'] 			= 'form-group1';
formGroups['target'] 		= 'form-group1';
formGroups['min'] 			= 'form-group1';
formGroups['management'] 	= 'form-group1';
formGroups['compareType'] 	= 'form-group1';
formGroups['dataType'] 		= 'form-group1';

function saveSuccess(data) {
	clearWarningMessageField(formGroups, msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.toastrWarning( data.message );
		setWarningMessageField(formGroups, msgFields, data.checkFields);
		return;
	}
	parent.toastrInfo( data.message );
	clearSave();
}

function clearSave() {
	clearWarningMessageField(formGroups, msgFields);
	$("#aggrOid").val( _qifu_please_select_id );
	$("#forOid").val( _qifu_please_select_id );
	$("#id").val('');
	$("#name").val('');
	$("#weight").val('');
	$("#unit").val('');
	$("#max").val('');
	$("#target").val('');
	$("#min").val('');
	$("#management").val( _qifu_please_select_id );
	$("#compareType").val( _qifu_please_select_id );
	$("#dataType").val( _qifu_please_select_id );
	$("#description").val('');
	$("#management").trigger('change');
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
			<label for="weight" id="weightLabel">Weight: </label>
			<input type="range" class="custom-range" min="0" max="100" value="50" id="weight" name="weight">								
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="unit" value="" id="unit" label="Unit" requiredFlag="Y" maxlength="20" placeholder="Enter unit" />
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
		<@qifu.button id="btnSave" label="Save"
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
				'description'	:	$('#description').val()
			}
			"
			onclick="btnSave();"
			loadFunction="saveSuccess(data);"
			errorFunction="clearSave();" />
		<@qifu.button id="btnClear" label="Clear" onclick="clearSave();" />
	</div>
</div>

<br/>
<br/>
<br/>

</body>
</html>