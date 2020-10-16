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

function sysChange() {
	
	$('#folderProgOid').find('option').remove().end();
	$("#progListGrid").html( '' );
	
	var sysOid = $("#sysOid").val();
	xhrSendParameter(
			'./getCommonProgramFolderJson', 
			{ 'oid' : sysOid }, 
			function(data) {
				if ( _qifu_success_flag != data.success ) {
					
					$('#folderProgOid')
				    .find('option')
				    .remove()
				    .end()
				    .append('<option value="' + _qifu_please_select_id + '">' + _qifu_please_select_name + '</option>')
				    .val( _qifu_please_select_id );
					
					return;
				}
				for (var n in data.value) {
					$('#folderProgOid').append($('<option>', {
					    value: n,
					    text: data.value[n]
					}));
				}
				
			}, 
			function() {
				
			},
			_qifu_defaultSelfPleaseWaitShow
	);	
}

function progFolderChange() {
	
	$("#progListGrid").html( '' );
	var folderProgOid = $("#folderProgOid").val();
	if (null == folderProgOid || '' == folderProgOid || _qifu_please_select_id == folderProgOid) {
		return;
	}
	xhrSendParameter(
			'./menuSettingsQueryProgramListByFolderOidJson', 
			{ 'oid' : folderProgOid }, 
			function(data) {
				if ( _qifu_success_flag != data.success ) {
					parent.toastrWarning( data.message );
					return;
				}
				var progAll = data.value.all;
				var progEnable = data.value.enable;
				
				var str = '';
				str += '<table class="table table-hover table-bordered">';
				str += '<thead class="thead-light">';
				str += '<tr>';
				str += '<th>&nbsp;&nbsp;#&nbsp;&nbsp;</th>';
				str += '<th>Name</th>';
				str += '</tr>';
				str += '</thead>';
				str += '<tbody>';
				for (var p in progAll) {
					var checkIt = false;
					for (var e in progEnable) {
						if ( progEnable[e].oid == progAll[p].oid ) {
							checkIt = true;
						}
					}
					var chkStr = '';
					if (checkIt) {
						chkStr = ' checked="checked" ';
					}
					str += '<tr>';
					str += '<td><div class="form-check"><input type="checkbox" class="form-check-input" id="prog' + progAll[p].oid + '" name="prog' + progAll[p].oid + '" onclick="updateMenu();" ' + chkStr + ' value="' + progAll[p].oid + '" ></div></td>';
					str += '<td><img src="' + parent.getIconUrlFromId(progAll[p].icon) + '" border="0">&nbsp;' + progAll[p].name + '</td>';
					str += '</tr>';
				}
				str += '</tbody>';
				str += '</table>';				
				$("#progListGrid").html( str );
			}, 
			function() {
				
			},
			_qifu_defaultSelfPleaseWaitShow
	);	
	
}

function updateMenu() {
	var parentOid = $("#folderProgOid").val();
	var progAppendOid = '';
	$('input.form-check-input:checkbox:checked').each(function() {
	    progAppendOid += $(this).val() + _qifu_delimiter;
	});
	xhrSendParameterNoPleaseWait(
			'./menuSettingsUpdateJson', 
			{ 
				'folderProgramOid'	: parentOid,
				'appendOid'			: progAppendOid
			}, 
			function(data) {
				if ( _qifu_success_flag == data.success ) {
					parent.toastrInfo( data.message );
				} else {
					parent.toastrWarning( data.message );
				}
				progFolderChange(); // 重取 table 資料
			}, 
			function() {
				window.location=parent.getProgUrl('CORE_PROG001D0003Q');
			}
	);	
	
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG001D0003Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('CORE_PROG001D0003Q');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('CORE_PROG001D0003Q');"
	programName="${programName}"
	programId="${programId}"
	description="Management menu-tree options.">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="sysMap" name="sysOid" id="sysOid" value="" label="System" requiredFlag="Y" onchange="sysChange();"></@qifu.select>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="folderProgMap" name="folderProgOid" id="folderProgOid" value="" label="Program folder" requiredFlag="Y" onchange="progFolderChange();"></@qifu.select>
		</div>
	</div>		
	<div class="row">&nbsp;</div>
	<div class="row">
		<div id="progListGrid" class="col-md-12"></div>
	</div>			
</div>	

<br/>
<br/>
<br/>

</body>
</html>