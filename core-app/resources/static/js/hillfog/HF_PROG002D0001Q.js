var _gaugeIdHead = 'gauge_';
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
	var tableStart = `
	<div class="row mx-auto justify-content-center align-items-center flex-column ">
		<div class="col-6" id="${currGaugeIdHead}" style="width: 500px;height:450px;"></div>
	</div>
	<table class="table">
		<thead class="thead-dark">
			<tr>
				<th scope="col">Date range score</th>
				<th scope="col">Info</th>
			</tr>
		</thead>
	<tbody>
	`;
	
	for (var n in scoreData.dataRangeScores) {
		var currDateRangeGaugeIdHead = currGaugeIdHead + '_' + n;
		detailContent += `
		<tr>
			<td><div class="col-6" id="${currDateRangeGaugeIdHead}" style="width: 400px;height:350px;"></div></td>
			<td>&nbsp;</td>
		</tr>
		`;
	}
	
	var tableEnd = `
	</tbody>
	</table>
	`;
	//console.log( `${tableStart}${detailContent}${tableEnd}` );
	return `${tableStart}${detailContent}${tableEnd}`;
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

