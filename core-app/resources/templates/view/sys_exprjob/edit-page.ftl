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

function updateSuccess(data) {
	clearWarningMessageField(formGroups, msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.toastrWarning( data.message );
		setWarningMessageField(formGroups, msgFields, data.checkFields);
		return;
	}
	parent.toastrInfo( data.message );
	clearUpdate();
}

function clearUpdate() {
	window.location=parent.getProgUrlForOid('CORE_PROG003D0006E', '${sysExprJob.oid}');
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG003D0006E_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('CORE_PROG003D0006E', '${sysExprJob.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnUpdate();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG003D0006E');"
	programName="${programName}"
	programId="${programId}"
	description="Modify expression task schedule.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="sysMap" name="systemOid" id="systemOid" value="systemOid" requiredFlag="Y" label="System"></@qifu.select>
		</div>
	</div>	
</div>
<div class="form-group" id="form-group2">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="id" id="id" value="sysExprJob.id" maxlength="20" requiredFlag="Y" placeholder="Enter Id" label="Id" readonly="Y"></@qifu.textbox>
		</div>
	</div>	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="name" id="name" value="sysExprJob.name" maxlength="100" requiredFlag="Y" placeholder="Enter name" label="Name"></@qifu.textbox>
		</div>
	</div>	
</div>
<div class="form-group" id="form-group3">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="expressionMap" name="expressionOid" id="expressionOid" value="expressionOid" requiredFlag="Y" label="Expression"></@qifu.select>
		</div>
	</div>	
</div>
<div class="form-group" id="form-group4">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.checkbox name="active" id="active" label="Active" checkedTest=" \"Y\" == sysExprJob.active "></@qifu.checkbox>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.checkbox name="checkFault" id="checkFault" label="Check fault" checkedTest=" \"Y\" == sysExprJob.checkFault "></@qifu.checkbox>
		</div>
	</div>
</div>
<div class="form-group" id="form-group5">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="runDayOfWeekMap" name="runDayOfWeek" id="runDayOfWeek" value="sysExprJob.runDayOfWeek" label="Day of week"></@qifu.select>
			
			<@qifu.select dataSource="runHourMap" name="runHour" id="runHour" value="sysExprJob.runHour" label="Hour"></@qifu.select>
			
			<@qifu.select dataSource="runMinuteMap" name="runMinute" id="runMinute" value="sysExprJob.runMinute" label="Minute"></@qifu.select>
		</div>
	</div>	
</div>
<div class="form-group" id="form-group6">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="{ \"0\":\"No\", \"1\":\"Only fault\", \"2\":\"Only success\", \"3\":\"Both fault/success\" }" name="contactMode" id="contactMode" value="sysExprJob.contactMode" label="Contact mode"></@qifu.select>
		</div>	
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="contact" id="contact" value="sysExprJob.contact" maxlength="100" requiredFlag="Y" placeholder="Enter contact" label="Contact"></@qifu.textbox>
		</div>
	</div>	
</div>	
<div class="form-group" id="form-group7">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="sysExprJob.description" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>
</div>


<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnUpdate" label="<i class=\"icon fa fa-floppy-o\"></i>&nbsp;Save"
			xhrUrl="./sysExpressionJobUpdateJson"
			xhrParameter="
			{
				'oid'			:	'${sysExprJob.oid}',
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
			onclick="btnUpdate();"
			loadFunction="updateSuccess(data);"
			errorFunction="clearUpdate();">
		</@qifu.button>
		<@qifu.button id="btnClear" label="<i class=\"icon fa fa-hand-paper-o\"></i>&nbsp;Clear" onclick="clearUpdate();"></@qifu.button>
	</div>
</div>

<br/>
<br/>
<br/>

</body>
</html>