<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<script src="${qifu_basePath}echarts/echarts.min.js"></script>

<style type="text/css">


</style>


<script type="text/javascript">
function orgBarChartPage() {
	parent.addTab('HF_PROG003D0001H', parent.getProgUrl('HF_PROG003D0001H') + '&organizationMode=' + _qifu_success_flag);
}
</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG003D0001H_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="orgBarChartPage();" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG003D0001H');"
	programName="${programName}"
	programId="${programId}"
	description="OKRs progress bar-chart for organization/department." />	
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<@qifu.if test=" \"N\" == foundObjective ">
<br>
<h4><span class="badge badge-warning">No found organization/department 's OKRs to display.</span></h4>
</@qifu.if>

<div id="container" style="height: 90%; width: 100%; min-height: 600px; min-width: 800px;"></div>

<br/>
<br/>
<br/>

<@qifu.if test=" \"Y\" == foundObjective ">
<script type="text/javascript">
var dom = document.getElementById("container");
var myChart = echarts.init(dom);
var app = {};

var option;

option = {
    dataset: [{
        dimensions: ['orgId', 'name', 'score'],
        source: ${source}
    }, {
        transform: {
            type: 'sort',
            config: { dimension: 'orgId', order: 'desc' }
        }
    }],
    xAxis: {
        type: 'category',
        axisLabel: { interval: 0, rotate: 0 }, // interval: 0, rotate: 30
    },
    yAxis: {},
    series: {
        type: 'bar',
        encode: { x: 'name', y: 'score' },
        datasetIndex: 1,
        itemStyle: {
            normal: {
              color: new echarts.graphic.LinearGradient(
                0, 0, 0, 1,
                [
                  { offset: 0, color: '#83bff6' },
                  { offset: 0.5, color: '#188df0' },
                  { offset: 1, color: '#188df0' }
                ]
              )
            },
            emphasis: {
              name: 'OKRs progress',
              color: new echarts.graphic.LinearGradient(
                0, 0, 0, 1,
                [
                  { offset: 0, color: '#2378f7' },
                  { offset: 0.7, color: '#2378f7' },
                  { offset: 1, color: '#83bff6' }
                ]
              )
            }
        },        
        label: {
            normal: {
            	show: true,
            	position: 'top'
            }
        }        
    }
};

if (option && typeof option === 'object') {
    myChart.setOption(option);
}
</script>
</@qifu.if>

</body>
</html>