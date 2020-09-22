<html>
<head>
<title>qifu2</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<style type="text/css">

/*-- 這個寬度不會有寬的scrollbar出現 --*/
body {
  width: 94%;
}

</style>


<script type="text/javascript">

$( document ).ready(function() {
	
});

function previewReport() {
	var paramData = [];
	<@qifu.if test=" null != paramList && paramList.size > 0 ">
	<#list paramList as item>
	paramData.${item.urlParam} = $("#CORE_PROG001D0005S02Q_field\\:${item.urlParam}" ).val();
	</#list>	
	</@qifu.if>
	commonOpenJasperReport('${sysJreport.reportId}', paramData);
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="CORE_PROG001D0005S02Q_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('CORE_PROG001D0005S02Q', '${sysJreport.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.hideModal('CORE_PROG001D0005S02Q');">		
</@qifu.toolBar>
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<@qifu.if test=" null != paramList && paramList.size > 0 ">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.button id="preview" label="Preview" onclick="previewReport();"></@qifu.button>
		</div>
	</div>
	
	<p style="margin-bottom: 10px"></p>
	
	
		<table class="table">
		<thead class="thead-light">
		<tr>
			<th>URL / Report parameter</th>
			<th>Variable value</th>
		</tr>
		</thead>
		<tbody>		
			<#list paramList as item>
			<tr>
				<td>${item.urlParam} / ${item.rptParam}</td>
				<td>
					<@qifu.textbox name="CORE_PROG001D0005S02Q_field:${item.urlParam}" id="CORE_PROG001D0005S02Q_field:${item.urlParam}" value="" maxlength="50" placeholder="Enter variable value"></@qifu.textbox>
				</td>
			</tr>
			</#list>
		</tbody>
		</table>
		
		
</@qifu.if>
<@qifu.else>
	<h4><span class="badge badge-warning">No settings report parameter</span></h4>
</@qifu.else>

</body>
</html>