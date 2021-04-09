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

var selDeptList = [ ${selOrgInputAutocomplete} ];
var selEmpList = [ ${selEmpInputAutocomplete} ];

$( document ).ready(function() {	
	
	paintOrganization();
	paintEmployee();
	
});

function paintOrganization() {
	$('#selOrgDeptShowLabel').html( '' );
	var htmlContent = '';
	for (var n in selDeptList) {
		htmlContent += '<span class="badge badge-secondary"><font size="3">' + selDeptList[n] + '</font></span>&nbsp;';
	}
	$('#selOrgDeptShowLabel').html( htmlContent );
}

function paintEmployee() {
	$('#selEmpShowLabel').html( '' );
	var htmlContent = '';
	for (var n in selEmpList) {
		htmlContent += '<span class="badge badge-secondary"><font size="3">' + selEmpList[n] + '</font></span>&nbsp;';
	}
	$('#selEmpShowLabel').html( htmlContent );
}

function btnBackReport() {
	window.location = parent.getProgUrl('HF_PROG003D0001Q');
}

</script>

</head>

<body>

<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div id="main-content" class="col-xs-12">

	<div class="row">
		<div class="col p-2 bg-secondary rounded">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<span class="btn badge btn-info" onclick="btnBackReport();"><h6><i class="icon fa fa-backward"></i>&nbsp;Back objective list</h6></span>	
					&nbsp;
				</div>
			</div>
		</div>
	</div>
	
	<br>
	
	<table class="table table-bordered">
	  <tbody>
	    <tr>
	      <td><h3><span class="badge badge-pill badge-success">${objective.name}</span></h3></td>
	      <td><h3><span class="badge badge-pill badge-secondary">${objective.startDateShow}&nbsp;~&nbsp;${objective.endDateShow}</span></h3></td>
	    </tr>
	    <tr>
	    	<td colspan="2">
	    		Progress:&nbsp;${objective.progressPercentage}%
				<div class="progress">
					<div class="progress-bar bg-info" role="progressbar" style="width: ${objective.progressPercentage}%" aria-valuenow="${objective.progressPercentage}" aria-valuemin="0" aria-valuemax="100"></div>
				</div>    	
	    	</td>
	    </tr>
	    <tr>
	    	<td colspan="2">
	    		Department:&nbsp;<br>
	    		<span id="selOrgDeptShowLabel">&nbsp;</span>
	    		<br><br>
	    		Owner:&nbsp;<br>
	    		<span id="selEmpShowLabel">&nbsp;</span>
	    	</td>
	    </tr>    
	    <@qifu.if test=" objective.description != null && objective.description.length > 0 ">
	    <tr>
	    	<td colspan="2"><pre>${objective.description}</pre></td>
	    </tr>
	    </@qifu.if>
	</tbody>
	
	<@qifu.if test=" objective.keyResList != null && objective.keyResList.size > 0 ">    	
	<table class="table">
	  <thead class="thead-dark">
	    <tr>
	      <th scope="col">Key Result</th>
	      <th scope="col">Progress Rate</th>
	    </tr>
	  </thead>
	  <tbody>
		<#list objective.keyResList as keyRes >
		    <tr>
		      <td>
		      	<h4><span class="badge badge-pill badge-secondary">${keyRes.name}</span></h4>
		      </td>
		      <td>
				Progress:&nbsp;${keyRes.progressPercentage}%
				<div class="progress">
					<div class="progress-bar bg-info" role="progressbar" style="width: ${keyRes.progressPercentage}%" aria-valuenow="${keyRes.progressPercentage}" aria-valuemin="0" aria-valuemax="100"></div>
				</div>  	      
		      </td>
		    </tr>	
		</#list>	  
	  </tbody>
	</table>	
	</@qifu.if>
	
	<@qifu.if test=" objective.initiativeList != null && objective.initiativeList.size > 0 ">
	<table class="table">
	  <thead class="thead-dark">
	    <tr>
	      <th scope="col">Initiatives</th>
	    </tr>
	  </thead>
	  <tbody>
		<#list objective.initiativeList as initiative >
		    <tr>
		      <td><pre>${initiative.content}</pre></td>
		    </tr>	
		</#list>
	  </tbody>
	</table>		
	</@qifu.if>  
	
</div>
	
<br/>
<br/>
<br/>

</body>
</html>