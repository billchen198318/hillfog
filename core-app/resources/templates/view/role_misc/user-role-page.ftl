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

function accountChange() {
	
	$("#roleListGrid").html( '' );
	
	var accountOid = $("#accountOid").val();
	
	if ( _qifu_please_select_id == accountOid || null == accountOid ) {
		return;
	}
	
	xhrSendParameter(
			'./userRoleListByAccountOidJson', 
			{ 'accountOid' : accountOid }, 
			function(data) {
				if ( _qifu_success_flag != data.success ) {
					parent.toastrWarning( data.message );
					return;
				}
				
				var roleAll = data.value.all;
				var roleEnable = data.value.enable;
				
				var str = '';
				str += '<table class="table table-hover table-bordered">';
				str += '<thead class="thead-light">';
				str += '<tr>';
				str += '<th>&nbsp;&nbsp;#&nbsp;&nbsp;</th>';
				str += '<th>Role</th>';
				str += '</tr>';
				str += '</thead>';
				str += '<tbody>';
				for (var p in roleAll) {
					var checkIt = false;
					for (var e in roleEnable) {
						if ( roleEnable[e].oid == roleAll[p].oid ) {
							checkIt = true;
						}
					}
					var chkStr = '';
					if (checkIt) {
						chkStr = ' checked="checked" ';
					}
					str += '<tr>';
					str += '<td><div class="form-check"><input type="checkbox" class="form-check-input" id="role' + roleAll[p].oid + '" name="role' + roleAll[p].oid + '" onclick="updateRoleEnable();" ' + chkStr + ' value="' + roleAll[p].oid + '" ></div></td>';
					str += '<td>' + roleAll[p].role + '</td>';
					str += '</tr>';
				}
				str += '</tbody>';
				str += '</table>';				
				$("#roleListGrid").html( str );
				
			}, 
			function() {
				
			},
			_qifu_defaultSelfPleaseWaitShow
	);
}

function updateRoleEnable() {
	var accountOid = $("#accountOid").val();
	var roleAppendOid = '';
	$('input.form-check-input:checkbox:checked').each(function() {
		roleAppendOid += $(this).val() + _qifu_delimiter;
	});
	xhrSendParameterNoPleaseWait(
			'./userRoleUpdateJson', 
			{ 
				'accountOid'	: accountOid,
				'appendOid'		: roleAppendOid
			}, 
			function(data) {
				if ( _qifu_success_flag == data.success ) {
					parent.toastrInfo( data.message );
				} else {
					parent.toastrWarning( data.message );
				}
				accountChange(); // 重取 table 資料
			}, 
			function() {
				window.location=parent.getProgUrl('CORE_PROG002D0002Q');
			}
	);
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG002D0002Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('CORE_PROG002D0002Q');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG002D0002Q');"
	programName="${programName}"
	programId="${programId}"
	description="Management user role.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="accountMap" name="accountOid" id="accountOid" value="" label="Account" requiredFlag="Y" onchange="accountChange();"></@qifu.select>
		</div>
	</div>
	<div class="row">&nbsp;</div>
	<div class="row">
		<div id="roleListGrid" class="col-md-12"></div>
	</div>	
</div>	

</body>
</html>