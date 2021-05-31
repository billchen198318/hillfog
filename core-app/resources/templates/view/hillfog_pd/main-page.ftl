<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<link rel="stylesheet" href="${qifu_basePath}highcharts-gantt/css/highcharts.css">
<script src="${qifu_basePath}highcharts-gantt/highcharts-gantt.js"></script>

<script type="text/javascript" src="${qifu_basePath}js/jquery-ui.min.js?ver=${qifu_jsVerBuild}"></script>
<script type="text/javascript" src="${qifu_basePath}js/vue.global.js"></script>

<style type="text/css">

.ui-autocomplete {
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 1000;
  display: none;
  float: left;
  min-width: 160px;
  padding: 5px 0;
  margin: 2px 0 0;
  list-style: none;
  font-size: 14px;
  text-align: left;
  background-color: #ffffff;
  border: 1px solid #cccccc;
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: 4px;
  -webkit-box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
  background-clip: padding-box;
}

.ui-autocomplete > li > div {
  display: block;
  padding: 3px 20px;
  clear: both;
  font-weight: normal;
  line-height: 1.42857143;
  color: #333333;
  white-space: nowrap;
}

.ui-state-hover,
.ui-state-active,
.ui-state-focus {
  text-decoration: none;
  color: #262626;
  background-color: #f5f5f5;
  cursor: pointer;
}

.ui-helper-hidden-accessible {
  border: 0;
  clip: rect(0 0 0 0);
  height: 1px;
  margin: -1px;
  overflow: hidden;
  padding: 0;
  position: absolute;
  width: 1px;
}

</style>


<script type="text/javascript">

var empList = [ ${empInputAutocomplete} ];
var currUserId = '${employeeSelect}';
var currKpiFrequency = '3'; // 6 - Year, 5 - Half-of-year, 4 - Quarter, 3 - Month
var startDateY = '${startDateY}';
var endDateY = '${endDateY}';
var startDateH = '${startDateH}';
var endDateH = '${endDateH}';
var startDateQ = '${startDateQ}';
var endDateQ = '${endDateQ}';
var startDateM = '${startDateM}';
var endDateM = '${endDateM}';

$( document ).ready(function() {
	
	$("#accountId").autocomplete({
		source: empList,
		select: function( event, ui ) {
			$("#accountId").val( ui.item.label );
			$("#accountId").trigger('change');
		}
	}).focus(function() {
		$(this).autocomplete("search", " ");
	});		
	
	$("#accountId").change(function(){
		var inputEmployee = $("#accountId").val();
		var checkInEmployee = false;
		for (var n in empList) {
			if ( empList[n] == inputEmployee ) {
				checkInEmployee = true;
			}
		}
		if (!checkInEmployee) {
			inputEmployee = currUserId;
			$("#accountId").val( currUserId );
			return;
		}
		if (currUserId != inputEmployee) {
			currUserId = inputEmployee;
			vm.queryObjectives();
			vm.queryPdcaProjects();
			vm.queryKpiChart(currKpiFrequency);
		}
	});
	
});



function gaugeChart(chartId, seriesName, dataValue, dataName) {
	var myChart = echarts.init(document.getElementById( chartId ));
	var option = {
	    tooltip: {
	        formatter: '{a} <br/>{b} : {c}%'
	    },
	    toolbox: {
	        feature: {
	            restore: {},
	            saveAsImage: {}
	        }
	    },
	    series: [
	        {
				title:{
					show:true,
					offsetCenter:[0,-120],
					color:'#1C1C1C',
					//fontWeight:'bold',
					fontSize:14
				},  
				clockwise:true,
				startAngle:180,
				endAngle:0,			      
				pointer:{show:true},
				axisTick:{show:true},
				splitLine:{show:false},        
	            name: seriesName,
	            type: 'gauge',
	            detail: {
	            	offsetCenter:[5,30],
	            	formatter: '{value}%'
	            },
	            data: [{value: dataValue, name: seriesName}] // [{value: dataValue, name: dataName}]
	        }
	    ]
	};
	
	myChart.setOption(option, true);
	myChart.resize();
}



// ----------------------------------------------------------------------------------
// PDCA 甘特圖
// ----------------------------------------------------------------------------------
function queryPdcaChart(pdcaOid) {
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
	
	var chartId = 'gantt_container_' + data.main.oid;
	
	if (!($("#"+chartId).length)) {
		return;
	}
	if (data.main.confirmDate != null) { // 結案不顯示
		return;
	}
	
	var pdcaItemSeries = pdcaProjectChartFillItems( data );
	
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

<div class="col-xs-12">

	<div class="row">
		<div class="col p-2 bg-secondary rounded">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
				<@qifu.if test=" null == empList || empList.size <= 1 ">
				
					<h4>
					<span class="badge badge-success"><@qifu.out value="employeeSelect" escapeHtml="Y" /></span>
					</h4>
					
					<input type="hidden" name="accountId" id="accountId" value="${employeeSelect}"/>
									
				</@qifu.if>
				<@qifu.else>
					
					<@qifu.textbox name="accountId" value="employeeSelect" id="accountId" label="Employee" requiredFlag="N" maxlength="100" placeholder="Select employee" />
					
				</@qifu.else>
					
				</div>	
			</div>	
		</div>	
	</div>
	
		
</div>

<br>

<div class="col-xs-12" id="main-content">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
		
			<div class="card border-info">
			  <div class="card-body">		
						<h4><span class="badge badge-pill badge-info">Personal's OKRs</span></h4>	
		
		<div v-if=" objectives == null || objectives.length < 1 ">	
		<h4><span class="badge badge-secondary">No your OKR can display.</span></h4>
		</div>
		
		<div v-for="d in objectives">			
			  <div class="card border-dark">
			    <div class="card-body">
			      <h5 class="card-title">{{ d.name }}</h5>
			      
				Progress:&nbsp;{{ d.progressPercentage }}%
				<div v-html=" progressDiv(d.progressPercentage) "></div>		
				<br>	      
			      
			      <h5><span class="badge badge-secondary">Objective:&nbsp;<span class="badge badge-info">{{ d.objectiveSize }}</span></span></h5>
			      <h5><span class="badge badge-secondary">Initiative:&nbsp;<span class="badge badge-info">{{ d.initiativeSize }}</span></span></h5>
			      
			      <p class="card-text"><pre>{{ d.description }}</pre></p>
			      
			      <br>
			      
<button type="button" class="btn btn-info" title="view" v-on:click="viewObjectiveDetail(d.oid)"><i class="icon fa fa-eye"></i></button>
&nbsp;      

			    </div>
			  </div>	
			  
			  <br>
			  
		</div>		  		
					
						
				</div>
			</div>
			
		</div>	
		<div class="col-xs-6 col-md-6 col-lg-6">
		
			<div class="card border-info">
			  <div class="card-body">		
					<h4><span class="badge badge-pill badge-info">Personal owner KPIs</span></h4>	
					
					<div v-if=" kpis == null || kpis.length < 1 ">
					<h4><span class="badge badge-secondary">No your KPIs can display.</span></h4>
					</div>
										
					<div class="row" v-if=" kpis != null && kpis.length > 0 " >
						<button type="button" class="btn btn-success" title="Current year" v-on:click="queryKpiChart('6')">Y</button>
						&nbsp;
						<button type="button" class="btn btn-success" title="Current half-of-year" v-on:click="queryKpiChart('5')">H</button>
						&nbsp;
						<button type="button" class="btn btn-success" title="Current quarter" v-on:click="queryKpiChart('4')">Q</button>
						&nbsp;
						<button type="button" class="btn btn-success" title="Current month" v-on:click="queryKpiChart('3')">M</button>
					</div>

					<div v-for="d in kpis">
						
						<div class="row mx-auto flex-column"><div class="col-6 align-self-center" v-bind:id="'gauge_'+d.kpi.oid" style="width: 100%;height:350px;"></div></div>
						
						<button v-if=" d.measureDatas == null || d.measureDatas.length < 1 " type="button" class="btn btn-primary" title="view" v-on:click="toMeasureDataInput(d)"><i class="icon fa fa-plus"></i>&nbsp;Current frequency measure-data no input</button>
							
						<br>
							
					</div>
				
										
				</div>
			</div>
			
		</div>			
	</div>
	
	<br>
	
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
		
			<div class="card border-warning">
			  <div class="card-body">		
					<h4><span class="badge badge-pill badge-warning">Personal related PDCA projects</span></h4>	
						
					<div v-if=" pdcaOids == null || pdcaOids.length < 1 ">
					<h4><span class="badge badge-secondary">No your PDCA project can display.</span></h4>
					</div>
					
					
					<div v-for="d in pdcaOids">
					
						<div v-bind:id="'gantt_container_'+d"></div>
						<br>
						<button type="button" class="btn btn-primary" title="view" v-on:click="viewPdcaDetail(d)"><i class="icon fa fa-eye"></i>&nbsp;View detail</button>	
						
						{{ paintCurrentPdcaChart(d) }}
											
					</div>
					
					
				</div>
			</div>
			
		</div>	
	</div>
	
	
</div>

<br/>
<br/>
<br/>

<script src="${qifu_basePath}echarts/echarts.min.js"></script>
<script type="text/javascript" src="${qifu_basePath}js/hillfog/HF_PROG001D0007Q.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>