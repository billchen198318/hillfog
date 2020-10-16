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

	str += '<div class="row mx-auto justify-content-center align-items-center flex-column ">';
	str += '<div class="col-6" id="gauge_' + scoreData.kpi.id + '" style="width: 500px;height:450px;"></div>';
	str += '</div>';
	
	return str;
}
function showCharts(scoreData) {
	var myChart = echarts.init(document.getElementById('gauge_'+scoreData.kpi.id));
	
	option = {
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
	            name: scoreData.kpi.name,
	            type: 'gauge',
	            detail: {
	            	offsetCenter:[5,-40],
	            	formatter: '{value}%'
	            },
	            data: [{value: scoreData.score, name: 'The completion rate'}]
	        }
	    ]
	};
	
	myChart.setOption(option, true);
	    
}
