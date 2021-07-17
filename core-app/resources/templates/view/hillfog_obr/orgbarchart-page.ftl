<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<script src="${qifu_basePath}echarts/echarts.min.js"></script>

<style type="text/css">

.styled-table {
    border-collapse: collapse;
    margin: 25px 0;
    font-size: 0.9em;
    font-family: sans-serif;
    min-width: 400px;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}
.styled-table thead tr {
    background-color: #454545;
    color: #ffffff;
    text-align: left;
    font-size: 16px;
}
.styled-table th,
.styled-table td {
    /* padding: 12px 15px; */
    padding: 2px 2px;
    border-left: 1px solid #dddddd;
}
.styled-table tbody tr {
    border-bottom: 1px solid #dddddd;
}
.styled-table tbody tr:nth-of-type(even) {
    background-color: #f3f3f3;
}
.styled-table tbody tr:last-of-type {
    border-bottom: 2px solid #454545;
}
.styled-table tbody tr.active-row {
    font-weight: bold;
    color: #454545;
}

</style>


<script type="text/javascript">

var objectivesData = ${objectivesJson};

function orgBarChartPage() {
	parent.addTab('HF_PROG003D0001H', parent.getProgUrl('HF_PROG003D0001H') + '&organizationMode=' + _qifu_success_flag);
}

$( document ).ready(function() {
	
	paintContent();
	
});

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

<br>
<div id="orgOkrsContent"></div>


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

<script type="text/javascript" src="${qifu_basePath}js/hillfog/HF_PROG003D0001H_OM.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>