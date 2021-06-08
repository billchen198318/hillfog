var _gaugeIdHead = 'gauge_';
var _lineIdHead = 'line_';
var _chartsArr = [];
function showContent(data) {
	_chartsArr = [];
	var str = '';
	for (var d in data) {
		var scoreData = data[d];
		str += createContent(scoreData);
	}
	$("#content").html(str);
	for (var d in data) {
		var scoreData = data[d];
		showCharts(scoreData);
	}
	for (var d in data) {
		var scoreData = data[d];
		showPdcaChartContent(scoreData);
	}
}

function createContent(scoreData) {
	var currGaugeIdHead = _gaugeIdHead + scoreData.kpi.id;
	var detailContent = '';
	var lineContent = lineChartContent(scoreData);
	var pdcaContent = '';
	var tableStart = `
	
	<div class="row">
		<div class="col p-2 bg-secondary rounded">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<div class="col-xs-12 col-md-12 col-lg-12 text-white">
						<span class="badge badge-info"><h6>${scoreData.kpi.id}&nbsp;-&nbsp;${scoreData.kpi.name}</h6></span>
						<span class="badge badge-danger"><h6>${scoreData.kpi.managementName}</h6></span>			
						<span class="badge badge-warning"><h6>Target:&nbsp;${scoreData.kpi.target}&nbsp;，Maximum:&nbsp;${scoreData.kpi.max}&nbsp;，Minimum:&nbsp;${scoreData.kpi.min}</h6></span>
						<span class="badge badge-dark"><h6>Unit:&nbsp;${scoreData.kpi.unit}</h6></span>
						<span class="badge badge-dark"><h6>Formula:&nbsp;${scoreData.formula.forId}-${scoreData.formula.name}</h6></span>
						<span class="badge badge-dark"><h6>Aggregation:&nbsp;${scoreData.kpi.aggrId}-${scoreData.aggregationMethodName}</h6></span>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="row mx-auto flex-column">
		<span class="badge badge-info"><h2>${scoreData.kpi.id}&nbsp;-&nbsp;${scoreData.kpi.name} &nbsp; <span class="btn badge btn-warning" onclick="btnCreatePdca('${scoreData.kpi.oid}');"><h6><i class="icon fa fa-plus"></i>&nbsp;Create PDCA</h6></span></h2></span>
		<span class="badge badge-secondary"><h2>${scoreData.date1}&nbsp;~&nbsp;${scoreData.date2}</h2></span>
		<div class="col-12 align-self-center" id="${currGaugeIdHead}" style="width: 97%;height:450px;"></div>
		${lineContent}
	</div>
	<table class="table">
		<thead class="thead-dark">
			<tr>
				<th scope="col">Date range score</th>
			</tr>
		</thead>
	<tbody>
	
	`;
	
	for (var n in scoreData.dataRangeScores) {
		var currDateRangeGaugeIdHead = currGaugeIdHead + '_' + n;
		var infoDetail = infoDetailContent(scoreData.dataRangeScores[n]);
		detailContent += `
		<tr>
			<td class="text-center">${infoDetail}<div class="row mx-auto flex-column"><div class="col-6 align-self-center" id="${currDateRangeGaugeIdHead}" style="width: 97%;height:350px;"></div></div></td>
		</tr>
		`;
	}
	
	if (scoreData.pdcaItems != null && scoreData.pdcaItems.length > 0) {
		var pdcaChartDivStr = '';
		for (var n in scoreData.pdcaItems) {	
			var pdca = scoreData.pdcaItems[n].main;
			pdcaChartDivStr += `
			<div id="gantt_container_${pdca.oid}"></div>
			<br>
			<button type="button" class="btn btn-primary" title="view" onclick="viewDetail('${pdca.oid}')"><i class="icon fa fa-eye"></i>&nbsp;View detail</button>			
			`;
		}
		pdcaContent += `
		<tr>
			<td class="text-center">
			
				<div class="row mx-auto flex-column">
				
					<div class="card border-warning">
					  <div class="card-body">			
						<h4><span class="badge badge-pill badge-warning">PDCA</span></h4>	  		
						${pdcaChartDivStr}
						</div>
					</div>				
					
				</div>
				
			</td>
		</tr>
		`;						
	}
	
	var tableEnd = `
	</tbody>
	</table>
	`;
	
	return `${tableStart}${detailContent}${pdcaContent}${tableEnd}`;
}

function lineChartContent(scoreData) {
	if (scoreData.dataRangeScores.length <= 1) {
		return '';
	}
	var currLineIdHead = _lineIdHead + scoreData.kpi.id;
	var str = `
	<div class="col-12 align-self-center" id="${currLineIdHead}" style="width: 97%;height:450px;"></div>
	`;
	return str;
}

function infoDetailContent(dataRangeScore) {
	var str = `
	<span class="badge badge-info"><h2>${dataRangeScore.date}</h2></span>
	<span class="badge badge-secondary"><h2>Score:${dataRangeScore.score}</h2></span>
	`;
	return str;
}

function showCharts(scoreData) {
	var currGaugeIdHead = _gaugeIdHead + scoreData.kpi.id;
	var currLineIdHead = _lineIdHead + scoreData.kpi.id;
	gaugeChart(currGaugeIdHead, scoreData.kpi.name, scoreData.score, 'The completion rate');
	lineChart(currLineIdHead, scoreData);
	for (var n in scoreData.dataRangeScores) {
		var currDateRangeGaugeIdHead = currGaugeIdHead + '_' + n;
		gaugeChart(currDateRangeGaugeIdHead, scoreData.dataRangeScores[n].date, scoreData.dataRangeScores[n].score, 'The completion rate');
	}
}

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
					offsetCenter:[0,-190],
					color:'#888',
					fontWeight:'bold',
					fontSize:24 
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
	            	offsetCenter:[5,-40],
	            	formatter: '{value}%'
	            },
	            data: [{value: dataValue, name: dataName}]
	        }
	    ]
	};
	
	myChart.setOption(option, true);
	_chartsArr.push( myChart );
}

function lineChart(chartId, scoreData) {
	if (scoreData.dataRangeScores.length <= 1) {
		return;
	}	
	
	var xAxisData = [];
	var score = [];
	var target = [];
	
	for (var n in scoreData.dataRangeScores) {
		xAxisData.push(scoreData.dataRangeScores[n].date);
		score.push(scoreData.dataRangeScores[n].score);
		if (n < scoreData.measureDatas.length) {
			target.push(scoreData.measureDatas[n].target);
		} else {
			target.push(scoreData.dataRangeScores[n].target);
		}
	}
	
	var myChart = echarts.init(document.getElementById( chartId ));
	
	option = {
	    title: {
	        text: scoreData.kpi.name
	    },
	    tooltip: {
	        trigger: 'axis'
	    },
	    legend: {
	        data: ['Target', 'Score']
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    toolbox: {
	        feature: {
	            saveAsImage: {}
	        }
	    },
	    xAxis: {
	        type: 'category',
	        boundaryGap: false,
	        data: xAxisData
	    },
	    yAxis: {
	        type: 'value'
	    },
	    series: [
	        {
	            name: 'Target',
	            type: 'line',
	            stack: 'Target value',
	            data: target
	        },
	        {
	            name: 'Score',
	            type: 'line',
	            stack: scoreData.kpi.unit,
	            data: score
	        }
	    ]
	};	
	
	myChart.setOption(option, true);
	_chartsArr.push(myChart);
}



// ----------------------------------------------------------------------------------
// PDCA 甘特圖
// ----------------------------------------------------------------------------------
function showPdcaChartContent(scoreData) {
	for (var n in scoreData.pdcaItems) {
		pdcaProjectTimeChart(scoreData.pdcaItems[n]);
	}
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


