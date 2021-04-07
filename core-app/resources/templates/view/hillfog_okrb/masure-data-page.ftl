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

var dateStatus = "0";

$( document ).ready(function() {
	
	$("#keyRes").change(function(){
		paintContent();
	});
	
	paintContent();
	
});

function prevCalendar() {
	dateStatus = "-1";
	paintContent();
}

function nextCalendar() {
	dateStatus = "1";
	paintContent();
}

function paintContent() {
	$("#content").html('&nbsp;');
	var keyResOid = $("#keyRes").val();
	$("#keyResOid").val('');
	if ('all' == keyResOid || '' == keyResOid) {
		$("#content").html('<div class="alert alert-info" role="alert">Please first to select Key Result option!</div>');
		return;
	}
	$("#keyResOid").val(keyResOid);
	
	xhrSendParameter(
			'./hfOkrBaseEnterMasureDataBodyJson', 
			{
				'objectiveOid'	:	'${objective.oid}',
				'keyResOid'		:	keyResOid,
				'date'			:	$("#date").val(),
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

function btnObjectiveList() {
	window.location = parent.getProgUrl('HF_PROG001D0006Q');
}

function btnSave() {
	
}

function btnClear() {
	window.location = parent.getProgUrlForOid('HF_PROG001D0006M', '${objective.oid}');
}

</script>

</head>

<body>

<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col p-2 bg-secondary rounded">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<span class="btn badge btn-info" onclick="btnObjectiveList();"><h6><i class="icon fa fa-backward"></i>&nbsp;Back objective list</h6></span>	
					&nbsp;
					<span class="btn badge btn-info" onclick="btnSave();"><h6><i class="icon fa fa-floppy-o"></i>&nbsp;Save</h6></span>
					&nbsp;
					<span class="btn badge btn-info" onclick="btnClear();"><h6><i class="icon fa fa-hand-paper-o"></i>&nbsp;Cancel</h6></span>
					&nbsp;
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<@qifu.select dataSource="keyResOptionsMap" name="keyRes" id="keyRes" value="" label="Key Result" requiredFlag="Y"></@qifu.select>
				</div>	
			</div>				
		</div>
	</div>

</div>	

	<form id="measureDataForm" name="measureDataForm" action=".">
		
		<input type="hidden" name="objectiveOid" id="objectiveOid" value="${objective.oid}">
		<input type="hidden" name="keyResOid" id="keyResOid" value="">
		<input type="hidden" name="date" id="date" value="${systemDate}">
		
		<br>
		
		<span id="content"></span>
		
	</form>

<br/>
<br/>
<br/>

</body>
</html>