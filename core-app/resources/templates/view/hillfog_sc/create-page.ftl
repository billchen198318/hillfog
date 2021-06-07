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


// ==========================================================================================
var okrs = [];
var kpis = [];
<#list okrMap?keys as k>
okrs.push({'oid' : '${k}', 'name' : '${okrMap[k]?js_string?html}'});
</#list>
<#list kpiMap?keys as k>
kpis.push({'oid' : '${k}', 'name' : '${kpiMap[k]?js_string?html}'});
</#list>
// ==========================================================================================



$( document ).ready(function() {
	
	
});


</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG001D0008A_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('HF_PROG001D0008A');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnSave();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0008A');"
	programName="${programName}"
	programId="${programId}"
	description="Create scorecard item." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 


<ul class="nav nav-tabs" id="subMyTab" role="tablist">
	<li class="nav-item">
		<a class="nav-link" id="test1-tab" data-toggle="tab" href="#test1" role="tab" aria-controls="test1" aria-selected="true"><h6>配置-test1</h6></a>
	</li>
	<li class="nav-item">
		<a class="nav-link" id="test2-tab" data-toggle="tab" href="#test2" role="tab" aria-controls="test2" aria-selected="false"><h6>配置-test2</h6></a>
	</li>
	<li class="nav-item">
		<a class="nav-link" id="test3-tab" data-toggle="tab" href="#test3" role="tab" aria-controls="test3" aria-selected="false"><h6>配置-test3</h6></a>
	</li>
</ul>

<div class="tab-content" id="subMyTabContent">
	<div class="tab-pane fade true" id="test1" role="tabpanel" aria-labelledby="test1-tab">
		<div class="row">
		aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
		</div>
	</div>
	<div class="tab-pane fade false" id="test2" role="tabpanel" aria-labelledby="test2-tab">
		<div class="row">
		bbbbbbbbbbbbbbbbbbbbbbbbb
		</div>
	</div>
	<div class="tab-pane fade false" id="test3" role="tabpanel" aria-labelledby="test3-tab">
		<div class="row">
		ccccccccccccccccccccccccccccccc
		</div>
	</div>
</div>

<br/>
<br/>
<br/>

<script type="text/javascript" src="${qifu_basePath}js/hillfog/HF_PROG001D0008A.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>