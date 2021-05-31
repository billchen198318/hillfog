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

var empList = [ ${empInputAutocomplete} ];
var currUserId = '${employeeSelect}';

$( document ).ready(function() {
	
	$("#accountId").autocomplete({
		source: empList,
		select: function( event, ui ) {
			$("#accountId").val( ui.item.label );
			$("#accountId").trigger('change');
		}
	}).focus(function() {
		$(this).autocomplete("search", " ");
	});		
	
	$("#accountId").change(function(){
		var inputEmployee = $("#accountId").val();
		var checkInEmployee = false;
		for (var n in empList) {
			if ( empList[n] == inputEmployee ) {
				checkInEmployee = true;
			}
		}
		if (!checkInEmployee) {
			inputEmployee = currUserId;
			$("#accountId").val( currUserId );
			return;
		}
		if (currUserId != inputEmployee) {
			currUserId = inputEmployee;
			vm.queryObjectives();
		}
	});
	
});

</script>

</head>

<body>

<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="col-xs-12">

	<div class="row">
		<div class="col p-2 bg-secondary rounded">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
				<@qifu.if test=" null == empList || empList.size <= 1 ">
				
					<h4>
					<span class="badge badge-success"><@qifu.out value="employeeSelect" escapeHtml="Y" /></span>
					</h4>
					
					<input type="hidden" name="accountId" id="accountId" value="${employeeSelect}"/>
									
				</@qifu.if>
				<@qifu.else>
					
					<@qifu.textbox name="accountId" value="employeeSelect" id="accountId" label="Employee" requiredFlag="N" maxlength="100" placeholder="Select employee" />
					
				</@qifu.else>
					
				</div>	
			</div>	
		</div>	
	</div>
	
		
</div>

<br>

<div class="col-xs-12" id="main-content">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
		
			<div class="card border-info">
			  <div class="card-body">		
						<h4><span class="badge badge-pill badge-info">OKRs</span></h4>	
		
		<div v-if=" objectives == null || objectives.length < 1 ">	
		<h4><span class="badge badge-secondary">No your OKR can display.</span></h4>
		</div>
		
		<div v-for="d in objectives">			
			  <div class="card border-dark">
			    <div class="card-body">
			      <h5 class="card-title">{{ d.name }}</h5>
			      
				Progress:&nbsp;{{ d.progressPercentage }}%
				<div v-html=" progressDiv(d.progressPercentage) "></div>		
				<br>	      
			      
			      <h5><span class="badge badge-secondary">Objective:&nbsp;<span class="badge badge-info">{{ d.objectiveSize }}</span></span></h5>
			      <h5><span class="badge badge-secondary">Initiative:&nbsp;<span class="badge badge-info">{{ d.initiativeSize }}</span></span></h5>
			      
			      <p class="card-text"><pre>{{ d.description }}</pre></p>
			      
			      <br>
			      
<button type="button" class="btn btn-info" title="view" v-on:click="viewObjectiveDetail(d.oid)"><i class="icon fa fa-eye"></i></button>
&nbsp;      

			    </div>
			  </div>	
			  
			  <br>
			  
		</div>		  		
					
						
				</div>
			</div>
			
		</div>	
		<div class="col-xs-6 col-md-6 col-lg-6">
		
			<div class="card border-info">
			  <div class="card-body">		
						<h4><span class="badge badge-pill badge-info">KPIs</span></h4>	
						
				</div>
			</div>
			
		</div>			
	</div>
	
	<br>
	
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
		
			<div class="card border-warning">
			  <div class="card-body">		
						<h4><span class="badge badge-pill badge-warning">PDCA</span></h4>	
						
				</div>
			</div>
			
		</div>	
	</div>
</div>

<br/>
<br/>
<br/>

<script type="text/javascript" src="${qifu_basePath}js/hillfog/HF_PROG001D0007Q.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>