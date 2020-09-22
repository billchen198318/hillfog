<#if xhrParameter != "" >
<script>
function ${onclick}() {

<#if bootboxConfirm == "Y" >
	parent.bootbox.confirm(
			"${bootboxConfirmTitle}", 
			function(result) { 
				if (!result) {
					return;
				}
</#if>

	<#if xhrSendNoPleaseWait == "Y" >
	xhrSendParameterNoPleaseWait(
	</#if>
	<#if xhrSendNoPleaseWait != "Y" >
	xhrSendParameter(
	</#if>
		'${xhrUrl}',
		${xhrParameter},
		function(data, textStatus) {
			${loadFunction};
		},
		function(jqXHR, textStatus, errorThrown) {
			${errorFunction};
		}
		<#if xhrSendNoPleaseWait != "Y" >
		, '${selfPleaseWaitShow}'
		</#if>
	);
	
<#if bootboxConfirm == "Y" >	
			}
	);	
</#if>	
		
}
</script>
</#if>

<#if formId != "" >
<script>
function ${onclick}() {

<#if bootboxConfirm == "Y" >
	parent.bootbox.confirm(
			"${bootboxConfirmTitle}", 
			function(result) { 
				if (!result) {
					return;
				}
</#if>

	<#if xhrSendNoPleaseWait == "Y" >
	xhrSendFormNoPleaseWait(
	</#if>
	<#if xhrSendForm != "Y" >
	xhrSendForm(
	</#if>
		'${xhrUrl}',
		'${formId}', 
		function(data, textStatus) {
			${loadFunction};
		},
		function(jqXHR, textStatus, errorThrown) {
			${errorFunction};
		}
		<#if xhrSendNoPleaseWait != "Y" >
		, '${selfPleaseWaitShow}'
		</#if>		
	);
	
<#if bootboxConfirm == "Y" >	
			}
	);	
</#if>	
	
}
</script>
</#if>