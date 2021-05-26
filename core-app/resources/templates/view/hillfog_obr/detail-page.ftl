<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<link rel="stylesheet" href="${qifu_basePath}highcharts-gantt/css/highcharts.css">
<script src="${qifu_basePath}highcharts-gantt/highcharts-gantt.js"></script>

<style type="text/css">

.borderless td, .borderless th {
	border: none;
}

#container {
  max-width: 1024px;
  margin: 1em auto;
}

.highcharts-treegrid-node-level-1 {
	font-size: 13px;
	font-weight: bold;
	fill: black;
}

</style>


<script type="text/javascript">

var selDeptList = [ ${selOrgInputAutocomplete} ];
var selEmpList = [ ${selEmpInputAutocomplete} ];

$( document ).ready(function() {	
	
	paintOrganization();
	paintEmployee();
	
	<@qifu.if test=" pdcaList != null && pdcaList.size > 0 ">
		<#list pdcaList as pdca>
		qeruyPdcaChart('${pdca.oid}');
		</#list>
	</@qifu.if>
	
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

function btnCreatePdca() {
	parent.addTab('HF_PROG004D0001A', parent.getProgUrlForOid('HF_PROG004D0001A', '${objective.oid}') + '&masterType=O' );
}



// ----------------------------------------------------------------------------------
// PDCA 甘特圖
// ----------------------------------------------------------------------------------
function qeruyPdcaChart(pdcaOid) {
	xhrSendParameter(
			'./hfPdcaItemsForGanttDataJson', 
			{
				'oid'	:	pdcaOid
			}, 
			function(data) {
				if ( _qifu_success_flag != data.success ) {
					parent.toastrWarning( data.message );
					return;
				}
				if ( _qifu_success_flag == data.success ) {
					pdcaProjectTimeChart(data.value);
				}
			}, 
			function() {
				
			},
			_qifu_defaultSelfPleaseWaitShow
	);	
}
function pdcaProjectTimeChart(data) {
	
	var today = new Date(), day = 1000 * 60 * 60 * 24;

	// Set to 00:00:00:000 today
	today.setUTCHours(0);
	today.setUTCMinutes(0);
	today.setUTCSeconds(0);
	today.setUTCMilliseconds(0);	
	
	var chartId = 'gantt_container_' + data.main.oid;
	
	if (!($("#"+chartId).length)) {
		return;
	}
	if (data.main.confirmDate != null) { // 結案不顯示
		return;
	}
	
	var pdcaItemSeries = pdcaProjectChartFillItems( data );
	
	console.log( JSON.stringify( pdcaItemSeries ) );
	
	Highcharts.ganttChart(chartId, {
		
		chart: {
			styledMode: true
		},
		
	    title: {
	        text: '<b>' + data.main.name + '</b>',
	    	style: {
            	fontSize:'18px'
            }	        
	    },

	    subtitle: {
	        text: '<b>' + data.main.startDateShow + ' ~ ' + data.main.endDateShow + '</b>',
	    	style: {
            	fontSize:'14px'
            }		        
	    },
	    
	    xAxis: {  	
	    	min: (new Date(data.main.startDateShow + 'T00:00:00Z')).getTime(),
	    	max: (new Date(data.main.endDateShow + 'T00:00:00Z')).getTime()
	    },	    
	    
	    tooltip: {
	        xDateFormat: '%a %b %d'
	    },
	    
	    /*
	    plotOptions: {
	        series: {
	            animation: true, // false - Do not animate dependency connectors
	            dataLabels: {
	                enabled: true,
	                format: '{point.name}',
	                style: {
	                	fontSize: '13px',
	                    cursor: 'default',
	                    pointerEvents: 'none'
	                }
	            }
	        }
	    },
	    */
	    
	    series: pdcaItemSeries
	    
	});	
	
}
function pdcaProjectChartFillItems(pdcaItems) {
	var series = [{
		name	:	pdcaItems.main.name,
		data	:	[]
	}];
	for (var n in pdcaItems.planItemList) {
		var item = pdcaItems.planItemList[n];
		series[0].data.push({
			name		: '(P) - ' + item.name,
			id			: item.oid,
			start		: (new Date(item.startDateShow + 'T00:00:00Z')).getTime(),
			end			: (new Date(item.endDateShow + 'T00:00:00Z')).getTime()		
		});
	}
	for (var n in pdcaItems.doItemList) {
		var item = pdcaItems.doItemList[n];
		series[0].data.push({
			name		: '(D) - ' + item.name,
			id			: item.oid,
			parent		: item.parentOid,
			dependency	: item.parentOid,
			start		: (new Date(item.startDateShow + 'T00:00:00Z')).getTime(),
			end			: (new Date(item.endDateShow + 'T00:00:00Z')).getTime()		
		});
	}
	for (var n in pdcaItems.checkItemList) {
		var item = pdcaItems.checkItemList[n];
		series[0].data.push({
			name		: '(C) - ' + item.name,
			id			: item.oid,
			parent		: item.parentOid,
			dependency	: item.parentOid,
			start		: (new Date(item.startDateShow + 'T00:00:00Z')).getTime(),
			end			: (new Date(item.endDateShow + 'T00:00:00Z')).getTime()		
		});	
	}
	for (var n in pdcaItems.actItemList) {
		var item = pdcaItems.actItemList[n];
		series[0].data.push({
			name		: '(A) - ' + item.name,
			id			: item.oid,
			parent		: item.parentOid,
			dependency	: item.parentOid,
			start		: (new Date(item.startDateShow + 'T00:00:00Z')).getTime(),
			end			: (new Date(item.endDateShow + 'T00:00:00Z')).getTime()		
		});		
	}
	return series;
}
// ----------------------------------------------------------------------------------


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
					<span class="btn badge btn-warning" onclick="btnCreatePdca();"><h6><i class="icon fa fa-plus"></i>&nbsp;Create PDCA</h6></span>
					&nbsp;
				</div>
			</div>
		</div>
	</div>
	
	<br>
	
	<table class="table table-bordered borderless">
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
	
	
	
	<@qifu.if test=" pdcaList != null && pdcaList.size > 0 ">
	<br>
	<h3><span class="badge badge-pill badge-warning">PDCA</span></h3>
		<#list pdcaList as pdca>
			<div id="gantt_container_${pdca.oid}"></div>
		</#list>
	</@qifu.if>
	
	
	
</div>
	
<br/>
<br/>
<br/>

</body>
</html>