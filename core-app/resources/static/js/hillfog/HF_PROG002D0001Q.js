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
}

function createContent(scoreData) {
	var currGaugeIdHead = _gaugeIdHead + scoreData.kpi.id;
	var detailContent = '';
	var lineContent = lineChartContent(scoreData);
	var tableStart = `
	
	<div class="row">
		<div class="col p-2 bg-secondary rounded">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<div class="col-xs-12 col-md-12 col-lg-12 text-white">
						<span class="badge badge-info"><h6>${scoreData.kpi.id}&nbsp;-&nbsp;${scoreData.kpi.name}</h6></span>
						<span class="badge badge-danger"><h6>${scoreData.kpi.managementName}</h6></span>			
						<span class="badge badge-warning"><h6>Target:&nbsp;${scoreData.kpi.target}&nbsp;，Maximum:&nbsp;${scoreData.kpi.max}&nbsp;，Minimum:&nbsp;${scoreData.kpi.min}</h6></span>
						<span class="badge badge-dark"><h6>Weight:&nbsp;${scoreData.kpi.weight}&nbsp;，Unit:&nbsp;${scoreData.kpi.unit}</h6></span>
						<span class="badge badge-dark"><h6>Formula:&nbsp;${scoreData.formula.forId}-${scoreData.formula.name}</h6></span>
						<span class="badge badge-dark"><h6>Aggregation:&nbsp;${scoreData.kpi.aggrId}-${scoreData.aggregationMethodName}</h6></span>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="row mx-auto flex-column">
		<span class="badge badge-info"><h2>${scoreData.kpi.id}&nbsp;-&nbsp;${scoreData.kpi.name}</h2></span>
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
	
	var tableEnd = `
	</tbody>
	</table>
	`;
	
	return `${tableStart}${detailContent}${tableEnd}`;
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

