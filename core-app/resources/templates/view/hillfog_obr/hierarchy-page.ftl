<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 


<link rel="stylesheet" href="${qifu_basePath}orgchart/css/jquery.orgchart-custom.css">
<link rel="stylesheet" href="${qifu_basePath}orgchart/css/style.css">

<script type="text/javascript" src="${qifu_basePath}orgchart/js/jquery.orgchart.js"></script>


<style type="text/css">


</style>


<script type="text/javascript">

var datascource = ${datascourceJsonData};

$( document ).ready(function() {
    
	$('#chart-container').orgchart({
    	'data' 			: datascource,
    	'nodeContent'	: 'title'
    });
    
});

</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG003D0001H_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('HF_PROG003D0001H');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG003D0001H');"
	programName="${programName}"
	programId="${programId}"
	description="OKRs view for employee hierarchy." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 


<div id="chart-container"></div>

<br/>
<br/>
<br/>

</body>
</html>