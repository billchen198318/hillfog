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

var msgFields = new Object();
msgFields['systemOid'] 		= 'systemOid';
msgFields['id'] 			= 'id';
msgFields['name'] 			= 'name';
msgFields['expressionOid']	= 'expressionOid';
msgFields['active']			= 'active';
msgFields['checkFault']		= 'checkFault';
msgFields['runDayOfWeek']	= 'runDayOfWeek';
msgFields['runHour']		= 'runHour';
msgFields['runMinute']		= 'runMinute';
msgFields['contactMode']	= 'contactMode';
msgFields['contact']		= 'contact';

var formGroups = new Object();
formGroups['systemOid'] 	= 'form-group1';
formGroups['id'] 			= 'form-group2';
formGroups['name'] 			= 'form-group2';
formGroups['expressionOid']	= 'form-group3';
formGroups['active']		= 'form-group4';
formGroups['checkFault']	= 'form-group4';
formGroups['runDayOfWeek']	= 'form-group5';
formGroups['runHour']		= 'form-group5';
formGroups['runMinute']		= 'form-group5';
formGroups['contactMode']	= 'form-group6';
formGroups['contact']		= 'form-group6';

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
	$("#systemOid").val( _qifu_please_select_id );
	$("#expressionOid").val( _qifu_please_select_id );
	$("#runDayOfWeek").val( '*' );
	$("#runHour").val( '*' );
	$("#runMinute").val( '*' );
	$("#contactMode").val( '0' );
	$("#active").prop('checked', false);
	$("#checkFault").prop('checked', false);
	$("#id").val( '' );
	$("#name").val( '' );
	$("#contact").val( '' );
	$("#description").val( '' );
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG003D0006A_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('CORE_PROG003D0006A');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnSave();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG003D0006A');"
	programName="${programName}"
	programId="${programId}"
	description="Create expression task schedule.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="sysMap" name="systemOid" id="systemOid" value="" requiredFlag="Y" label="System"></@qifu.select>
		</div>
	</div>	
</div>
<div class="form-group" id="form-group2">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="id" id="id" value="" maxlength="20" requiredFlag="Y" placeholder="Enter Id" label="Id"></@qifu.textbox>
		</div>
	</div>	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="name" id="name" value="" maxlength="100" requiredFlag="Y" placeholder="Enter name" label="Name"></@qifu.textbox>
		</div>
	</div>	
</div>
<div class="form-group" id="form-group3">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="expressionMap" name="expressionOid" id="expressionOid" value="" requiredFlag="Y" label="Expression"></@qifu.select>
		</div>
	</div>	
</div>
<div class="form-group" id="form-group4">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.checkbox name="active" id="active" label="Active"></@qifu.checkbox>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.checkbox name="checkFault" id="checkFault" label="Check fault"></@qifu.checkbox>
		</div>
	</div>
</div>
<div class="form-group" id="form-group5">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="runDayOfWeekMap" name="runDayOfWeek" id="runDayOfWeek" value="" label="Day of week"></@qifu.select>
			
			<@qifu.select dataSource="runHourMap" name="runHour" id="runHour" value="" label="Hour"></@qifu.select>
			
			<@qifu.select dataSource="runMinuteMap" name="runMinute" id="runMinute" value="" label="Minute"></@qifu.select>
		</div>
	</div>	
</div>
<div class="form-group" id="form-group6">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="{ \"0\":\"No\", \"1\":\"Only fault\", \"2\":\"Only success\", \"3\":\"Both fault/success\" }" name="contactMode" id="contactMode" value="" label="Contact mode"></@qifu.select>
		</div>	
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="contact" id="contact" value="" maxlength="100" requiredFlag="Y" placeholder="Enter contact" label="Contact"></@qifu.textbox>
		</div>
	</div>	
</div>	
<div class="form-group" id="form-group7">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>
</div>


<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnSave" label="<i class=\"icon fa fa-floppy-o\"></i>&nbsp;Save"
			xhrUrl="./sysExpressionJobSaveJson"
			xhrParameter="
			{
				'systemOid'		:	$('#systemOid').val(),
				'id'			:	$('#id').val(),
				'name'			:	$('#name').val(),
				'expressionOid'	:	$('#expressionOid').val(),
				'active'		:	( $('#active').is(':checked') ? 'Y' : 'N' ),
				'checkFault'	:	( $('#checkFault').is(':checked') ? 'Y' : 'N' ),
				'runDayOfWeek'	:	$('#runDayOfWeek').val(),
				'runHour'		:	$('#runHour').val(),
				'runMinute'		:	$('#runMinute').val(),
				'contactMode'	:	$('#contactMode').val(),
				'contact'		:	$('#contact').val(),
				'description'	:	$('#description').val()
			}
			"
			onclick="btnSave();"
			loadFunction="saveSuccess(data);"
			errorFunction="clearSave();">
		</@qifu.button>
		<@qifu.button id="btnClear" label="<i class=\"icon fa fa-hand-paper-o\"></i>&nbsp;Clear" onclick="clearSave();"></@qifu.button>
	</div>
</div>

<br/>
<br/>
<br/>

</body>
</html>