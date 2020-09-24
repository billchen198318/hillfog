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

$( document ).ready(function() {
	
	$("#icon").trigger("change");
	
});

//fields[ data.checkFields map 的 key ]	= '頁面的欄位id';
var msgFields = new Object();
msgFields['systemId'] 			= 'sysId';
msgFields['systemName'] 		= 'name';
msgFields['systemHost'] 		= 'host';
msgFields['systemContextPath']	= 'contextPath';

//formGroups[ 頁面的欄位id ] = ' 欄位form-group的 id ';
var formGroups = new Object();
formGroups['sysId'] 		= 'form-group1';
formGroups['name'] 			= 'form-group1';
formGroups['host'] 			= 'form-group2';
formGroups['contextPath'] 	= 'form-group2';

function updateSuccess(data) {
	clearWarningMessageField(formGroups, msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.toastrWarning( data.message );
		setWarningMessageField(formGroups, msgFields, data.checkFields);
		return;
	}
	parent.toastrInfo( data.message );
}

function clearUpdate() {
	clearWarningMessageField(formGroups, msgFields);
	$("#name").val( '' );
	$("#host").val( '' );
	$("#contextPath").val( '' );
	$("#icon").val( '${firstIconKey}' );
	$("#local").prop('checked', false);
	$("#icon").trigger("change");
}

</script>


</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG001D0001E_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('CORE_PROG001D0001E', '${sys.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnUpdate();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG001D0001E');"
	programName="${programName}"
	programId="${programId}"
	description="Modify system site module config item." />
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="sysId" value="sys.sysId" id="sysId" label="Id (read only)" requiredFlag="Y" maxlength="10" placeholder="Enter Id (only normal character)" readonly="Y" />
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="name" value="sys.name" id="name" label="Name" requiredFlag="Y" maxlength="100" placeholder="Enter name" />
		</div>
	</div>
</div>
<div class="form-group" id="form-group2">	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="host" value="sys.host" id="host" label="Host" requiredFlag="Y" maxlength="200" placeholder="Enter host e.g: 127.0.0.1:8080" />
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="contextPath" value="sys.contextPath" id="contextPath" label="Context path" requiredFlag="Y" maxlength="100" placeholder="Enter host e.g: demo-web" />
		</div>
	</div>	
</div>	
<div class="form-group">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="iconDataMap" name="icon" id="icon" value="firstIconKey" label="Icon" requiredFlag="Y" onchange="showIcon();" />
			<div id="iconShow"></div>
			<script type="text/javascript">
			function showIcon() {
				var iconOid = $("#icon").val();
				if ( _qifu_please_select_id == iconOid ) {
					$("#iconShow").html( '' );
					return;
				}
				var iconUrl = parent.getIconUrlFromOid( iconOid );
				if (null == iconUrl || '' == iconUrl) {
					$("#iconShow").html( '' );
					return;
				}
				$("#iconShow").html( '<img src="' + iconUrl + '" border="0">' );
			}
			</script>			
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.checkbox name="local" id="local" label="Local" checkedTest=" \"Y\" == sys.isLocal " />
		</div>
	</div>
</div>


<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnUpdate" label="Save"
			xhrUrl="./sysSiteUpdateJson"
			xhrParameter="
			{
				'oid'			:	'${sys.oid}',
				'sysId'			:	$('#sysId').val(),
				'name'			:	$('#name').val(),
				'host'			:	$('#host').val(),
				'contextPath'	:	$('#contextPath').val(),
				'icon'			:	$('#icon').val(),		
				'isLocal'		:	( $('#local').is(':checked') ? 'Y' : 'N' )
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
