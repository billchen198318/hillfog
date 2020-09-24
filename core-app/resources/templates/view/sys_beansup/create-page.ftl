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

var msgFields = new Object();
msgFields['systemOid'] 			= 'systemOid';
msgFields['beanId'] 			= 'beanId';
msgFields['method'] 			= 'method';
msgFields['enableFlag'] 		= 'enable';

var formGroups = new Object();
formGroups['systemOid'] 		= 'form-group1';
formGroups['beanId'] 			= 'form-group1';
formGroups['method'] 			= 'form-group1';

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
	$("#beanId").val( '' );
	$("#method").val( '' );
	$("#enable").prop('checked', false);
	$("#description").val( '' );
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG003D0003A_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('CORE_PROG003D0003A');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnSave();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG003D0003A');"
	programName="${programName}"
	programId="${programId}"
	description="Create service bean support item.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="sysMap" name="systemOid" id="systemOid" value="" requiredFlag="Y" label="System"></@qifu.select>
		</div>
	</div>	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="beanId" id="beanId" value="" maxlength="255" requiredFlag="Y" label="Service bean Id" placeholder="Enter service bean Id"></@qifu.textbox>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="method" id="method" value="" maxlength="100" requiredFlag="Y" label="Method" placeholder="Enter method name"></@qifu.textbox>
		</div>
	</div>		
</div>
<div class="form-group" id="form-group2">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.checkbox name="enable" id="enable" label="Enable"></@qifu.checkbox>
		</div>	
	</div>
	<div class="row">	
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>	
</div>

<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnSave" label="Save"
			xhrUrl="./sysBeanSupportSaveJson"
			xhrParameter="	
			{
				'systemOid'			:	$('#systemOid').val(),
				'beanId'			:	$('#beanId').val(),
				'method'			:	$('#method').val(),
				'enableFlag'		:	( $('#enable').is(':checked') ? 'Y' : 'N' ),
				'description'		:	$('#description').val()
			}
			"
			onclick="btnSave();"
			loadFunction="saveSuccess(data);"
			errorFunction="clearSave();">
		</@qifu.button>
		<@qifu.button id="btnClear" label="Clear" onclick="clearSave();"></@qifu.button>
	</div>
</div>

<br/>
<br/>
<br/>

</body>
</html>