var _gaugeIdHead = 'gauge_';
function showContent(data) {
	var str = '';
	for (var d in data) {
		var scoreData = data[d];
		str += createContent(str, scoreData);
	}
	$("#content").html(str);
	for (var d in data) {
		var scoreData = data[d];
		showCharts(scoreData);
	}
}
function createContent(str, scoreData) {
	var currGaugeIdHead = _gaugeIdHead + scoreData.kpi.id;
	str += '<div class="row mx-auto justify-content-center align-items-center flex-column ">';
	str += '<div class="col-6" id="' + currGaugeIdHead + '" style="width: 500px;height:450px;"></div>';
	str += '</div>';
	
	str += '<table class="table">';
	str += '<thead class="thead-dark">';
	str += '<tr>';
	str += '<th scope="col">Date range score</th>';
	str += '<th scope="col">Info</th>';
	str += '</tr>';
	str += '</thead>';
	str += '<tbody>';
	for (var n in scoreData.dataRangeScores) {
		//console.log(scoreData.dataRangeScores[n]);
		var currDateRangeGaugeIdHead = currGaugeIdHead + '_' + n;
		str += '<tr>';
		str += '<td><div class="col-6" id="' + currDateRangeGaugeIdHead + '" style="width: 400px;height:350px;"></div></td>';
		str += '<td>&nbsp;</td>';
		str += '</tr>';
	}
	str += '</tbody>';
	str += '</table>';
	
	return str;
}
function showCharts(scoreData) {
	var currGaugeIdHead = _gaugeIdHead + scoreData.kpi.id;
	gaugeChart(currGaugeIdHead, scoreData.kpi.name, scoreData.score, 'The completion rate');
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

