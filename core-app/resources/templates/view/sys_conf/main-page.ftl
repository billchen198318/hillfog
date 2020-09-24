<html>
<head>
<title>qifu2</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<style type="text/css">


</style>


<script type="text/javascript">

function updateSuccess(data) {
	if ( _qifu_success_flag != data.success ) {
		parent.toastrWarning( data.message );
		return;
	}
	parent.toastrInfo( data.message );
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG001D0007Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('CORE_PROG001D0007Q');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG001D0007Q');"
	programName="${programName}"
	programId="${programId}"
	description="Settings some config base items.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="mailFrom" id="mailFrom" value="mailFrom" requiredFlag="Y" label="Mail from" maxlength="100"></@qifu.textbox>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.checkbox name="mailEnable" id="mailEnable" checkedTest=" \"Y\" == mailEnable " label="Mail sender enable"></@qifu.checkbox>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
		
		<@qifu.button id="btnUpdate" label="Save"
			xhrUrl="./sysSettingUpdateJson"
			xhrParameter="
			{
				'mailFrom'		:	$('#mailFrom').val(),	
				'mailEnable'	:	( $('#mailEnable').is(':checked') ? 'Y' : 'N' )
			}
			"
			onclick="btnUpdate();"
			loadFunction="updateSuccess(data);"
			errorFunction="clearUpdate();">
		</@qifu.button>	
		
		</div>
	</div>		
</div>	

<br/>
<br/>
<br/>

</body>
</html>