var _gaugeIdHead = 'gauge_';
var _lineIdHead = 'line_';
function showContent(data) {
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
	<div class="row mx-auto flex-column">
		<span class="badge badge-info"><h2>${scoreData.kpi.id}&nbsp;-&nbsp;${scoreData.kpi.name}</h2></span>
		<span class="badge badge-secondary"><h2>${scoreData.date1}&nbsp;~&nbsp;${scoreData.date2}</h2></span>
		<div class="col-6" id="${currGaugeIdHead}" style="width: 500px;height:450px;"></div>
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
			<td>${infoDetail}<div class="col-6" id="${currDateRangeGaugeIdHead}" style="width: 400px;height:350px;"></div></td>
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
	<div class="col-6" id="${currLineIdHead}" style="width: 500px;height:450px;"></div>
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
	dateRangeLineChart(currLineIdHead, scoreData);
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
}

function dateRangeLineChart(chartId, scoreData) {
	if (scoreData.dataRangeScores.length <= 1) {
		return;
	}	
	
	var xAxisData = [];
	var score = [];
	var kpiTarget = [];
	for (var n in scoreData.dataRangeScores) {
		xAxisData.push(scoreData.dataRangeScores[n].date);
		score.push(scoreData.dataRangeScores[n].score);
		kpiTarget.push(scoreData.dataRangeScores[n].target);
	}
	
	/*
	console.log('xAxisData='+xAxisData);
	console.log('kpiTarget='+kpiTarget);
	console.log('score='+score);
	*/
	
	var myChart = echarts.init(document.getElementById( chartId ));
	
	option = {
	    title: {
	        text: scoreData.kpi.name
	    },
	    tooltip: {
	        trigger: 'axis'
	    },
	    legend: {
	        data: ['KPI Target', 'Score']
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
	            name: 'KPI Target',
	            type: 'line',
	            stack: 'Target value',
	            data: kpiTarget
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
	
}
