<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

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

var orgDeptList = [ ${orgInputAutocomplete} ];
var empList = [ ${empInputAutocomplete} ];

$( document ).ready(function() {
	
	$("#objOrg").autocomplete({
		source: orgDeptList,
		select: function( event, ui ) {
			$("#objOrg").val( ui.item.label );
			$("#objOrg").trigger('change');
		}
	}).focus(function() {
		$(this).autocomplete("search", " ");
	});
	$("#objOwner").autocomplete({
		source: empList,
		select: function( event, ui ) {
			$("#objOwner").val( ui.item.label );
			$("#objOwner").trigger('change');
		}
	}).focus(function() {
		$(this).autocomplete("search", " ");
	});		
	
});

function hierarchyPage() {
	parent.addTab('HF_PROG003D0001H', null);
}

function orgBarChartPage() {
	parent.addTab('HF_PROG003D0001H', parent.getProgUrl('HF_PROG003D0001H') + '&organizationMode=' + _qifu_success_flag);
}

function gaugeChart(chartId, seriesName, dataValue, dataName) {
	var myChart = echarts.init(document.getElementById( chartId ));
	var option = {
	    tooltip: {
	        formatter: '{a} <br/>Progress : {c}%'
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
					offsetCenter:[0,-130],
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

</script>

</head>

<body>

<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div id="main-content" class="col-xs-12">


	<div class="row">
		<div class="col p-2 bg-secondary rounded">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<!--  
					<span class="badge badge-info"><h6>Objectives</h6></span>
					&nbsp;
					-->
					<span class="btn badge btn-info" v-on:click="queryObjectives"><h6><i class="icon fa fa-search"></i>&nbsp;Query</h6></span>	
					&nbsp;
					<span class="btn badge btn-info" v-on:click="clearObjectives"><h6><i class="icon fa fa-hand-paper-o"></i>&nbsp;Clear</h6></span>	
					&nbsp;&nbsp;
					<span class="btn badge btn-info" onclick="hierarchyPage();"><h6><i class="icon fa fa-users"></i>&nbsp;Hierarchy</h6></span>					
					&nbsp;
					<span class="btn badge btn-info" onclick="orgBarChartPage();"><h6><i class="icon fa fa-bar-chart"></i>&nbsp;Organization progress</h6></span>
				</div>
			</div>			
			<div class="row">
				<div class="col-xs-6 col-md-6 col-lg-6 text-white">
					<@qifu.textbox type="date" name="date1" value="" id="date1" label="Start" requiredFlag="N" maxlength="10" placeholder="Enter start date" />
				</div>
				<div class="col-xs-6 col-md-6 col-lg-6 text-white">
					<@qifu.textbox type="date" name="date2" value="" id="date2" label="End" requiredFlag="N" maxlength="10" placeholder="Enter end date" />
				</div>		
			</div>				
			<div class="row">
				<div class="col-xs-6 col-md-6 col-lg-6 text-white">
					<@qifu.textbox name="objOrg" value="" id="objOrg" label="Organization" requiredFlag="N" maxlength="100" placeholder="Enter organization" />
				</div>
				<div class="col-xs-6 col-md-6 col-lg-6 text-white">
					<@qifu.textbox name="objOwner" value="defaultQuerySelectEmployee" id="objOwner" label="Employee" requiredFlag="N" maxlength="100" placeholder="Enter employee" />
				</div>			
			</div>
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<@qifu.textbox name="name" value="" id="name" label="Name" requiredFlag="N" maxlength="100" placeholder="Enter name" />
				</div>
			</div>
		</div>
	</div>
	
	<br>

	<div class="row">
		
		
			<div class="card-columns col-xs-12 col-md-12 col-lg-12" v-if=" objectives.length > 0 ">
			
			  <div class="card border-dark" v-for="d in objectives">
			    <div class="card-body">
			      <h5 class="card-title">{{ d.name }}</h5>
			      
			    <div class="row mx-auto flex-column">
			    	<div class="col-6 align-self-center" v-bind:id="'gauge_'+d.oid" style="min-width: 350px;width: 400px;height:350px;"></div>
			    </div>
			    
				Progress:&nbsp;{{ d.progressPercentage }}%
				<div v-html=" progressDiv(d.progressPercentage) "></div>		
				<br>	      
			      
			      <h5><span class="badge badge-secondary">Key Result:&nbsp;<span class="badge badge-info">{{ d.keyResultSize }}</span></span></h5>
			      <h5><span class="badge badge-secondary">Initiative:&nbsp;<span class="badge badge-info">{{ d.initiativeSize }}</span></span></h5>
			      
			      <p class="card-text"><pre>{{ d.description }}</pre></p>
			      
			      <br>
			      
<button type="button" class="btn btn-info" title="view" v-on:click="viewDetail(d.oid)"><i class="icon fa fa-eye"></i></button>
&nbsp;      

			    </div>
			  </div>
			  
			</div>
		
  		
	</div>
	
	
</div>
	
<br/>
<br/>
<br/>

<script src="${qifu_basePath}echarts/echarts.min.js"></script>
<script type="text/javascript" src="${qifu_basePath}js/hillfog/HF_PROG003D0001Q.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>