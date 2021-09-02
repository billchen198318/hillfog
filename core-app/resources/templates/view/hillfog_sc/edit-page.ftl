<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<script type="text/javascript" src="${qifu_basePath}js/vue.global.js"></script>

<style type="text/css">

.hrDash{
  border: 0 none;
  border-top: 5px dashed #75B0FF;
  background: none;
  height:0;
}



.btn-circle.btn-xl {
    width: 70px;
    height: 70px;
    padding: 10px 16px;
    border-radius: 35px;
    font-size: 24px;
    line-height: 1.33;
}

.btn-circle {
    width: 35px;
    height: 30px;
    padding: 6px 0px;
    border-radius: 15px;
    text-align: center;
    font-size: 12px;
    line-height: 1.42857;
}



.nav-tabs .nav-link {
    color: gray;
    border: 0;
    border-bottom: 1px solid grey;
}

.nav-tabs .nav-link:hover {
    border: 0;
    border-bottom: 1px solid grey;
}

.nav-tabs .nav-link.active {
    color: #000000;
    border: 0;
    border-radius: 0;
    border-bottom: 2px solid #007bff;
    background-color: #FFEEBC;
}

</style>


<script type="text/javascript">

var _oid = '${scorecard.oid}';

// ====================================================================
var okrs = [];
var kpis = [];
<#list okrMap?keys as k>
okrs.push({'oid' : '${k}', 'name' : '${okrMap[k]?js_string?html}'});
</#list>
<#list kpiMap?keys as k>
kpis.push({'oid' : '${k}', 'name' : '${kpiMap[k]?js_string?html}'});
</#list>
// ====================================================================


var msgFields = new Object();
msgFields['name']		= 'name';
msgFields['content']	= 'content';
msgFields['mission']	= 'mission';

var formGroups = new Object();
formGroups['name']		= 'form-group1';
formGroups['content']	= 'form-group1';
formGroups['mission']	= 'form-group1';

$( document ).ready(function() {
	
	
});

function btnUpdate() {
	if (vm.autoAllocationWeightFlag) {
		vm.autoAllocationWeight();
	}
	xhrSendParameter(
		'./hfScorecardUpdateJson', 
		{ 
			'oid'			:	'${scorecard.oid}',
			'name' 			:	$("#name").val(),
			'content'		:	$("#content").val(),
			'mission'		:	$("#mission").val(),
			'perspectives'	:	JSON.stringify( { 'items' : vm.perspectives } )
		},
		function(data) {
			updateSuccess(data);
		}, 
		function() {
			clearUpdate();
		},
		_qifu_defaultSelfPleaseWaitShow
	);	
}

function updateSuccess(data) {
	clearWarningMessageField(formGroups, msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.notifyWarning( data.message );
		setWarningMessageField(formGroups, msgFields, data.checkFields);
		return;
	}
	parent.notifyInfo( data.message );
	clearUpdate();
}

function clearUpdate() {
	appUnmount();
	window.location = parent.getProgUrlForOid('HF_PROG001D0008E', '${scorecard.oid}');	
}

// ====================================================================

function removeArrayByPos(arr, pos) {
    for (var i = arr.length; i--;) {
    	if (pos == i) {
    		arr.splice(pos, 1);
    	}
    }
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG001D0008E_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('HF_PROG001D0008E', '${scorecard.oid}')" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnUpdate();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0008E');"
	programName="${programName}"
	programId="${programId}"
	description="Modify scorecard item." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div id="main-content">

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<@qifu.textbox name="name" value="scorecard.name" id="name" requiredFlag="Y" label="Vision name" placeholder="Enter vision name" maxlength="100" />
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="content" value="scorecard.content" id="content" requiredFlag="Y" label="Vision content" rows="3" placeholder="Enter vision content"></@qifu.textarea>
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="mission" value="scorecard.mission" id="mission" requiredFlag="Y" label="Vision mission" rows="3" placeholder="Enter vision mission"></@qifu.textarea>
		</div>		
	</div>			
</div>
<div class="form-group" id="form-group2">
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
		
		<button type="button" class="btn btn-primary" id="btnUpdate" onclick="btnUpdate();"><i class='icon fa fa-floppy-o'></i>&nbsp;Save</button>
		&nbsp;
		<button type="button" class="btn btn-primary" id="btnClear" onclick="clearUpdate();"><i class='icon fa fa-hand-paper-o'></i>&nbsp;Clear</button>	
		&nbsp;
		&nbsp;
		
		<button type="button" class="btn btn-info" id="btnAddPerspective" title="Add perspective" v-on:click="addPerspective"><i class="icon fa fa-plus-circle"></i>&nbsp;Add perspective</button>
		&nbsp;
		&nbsp;
		
		<p style="margin-bottom: 10px"></p>
		
		<div class="custom-control custom-checkbox">
			&nbsp;
			<input type="checkbox" class="custom-control-input" id="autoAllocationCheckbox" v-model="autoAllocationWeightFlag" v-on:click="autoAllocationWeight">
			<label class="custom-control-label" for="autoAllocationCheckbox">Auto allocation weight</label>
		</div>
		&nbsp;
		
		</div>
	</div>		
</div>
<div class="form-group" id="form-group3">
	<ul class="nav nav-tabs" id="subMyTab" role="tablist">
		<li class="nav-item" v-for="(d, index) in perspectives">
			<a v-on:click="setCurrSelTab(index)" v-bind:class="'nav-link' + (index == currSelTabNum ? ' active ' : ' ')" v-bind:id="'tab'+d.numTab" data-toggle="tab" v-bind:href="'#tabContent'+d.numTab" role="tab" v-bind:aria-controls="'tab'+d.numTab" v-bind:aria-selected="(index == currSelTabNum ? 'true' : 'false')"><h6>{{d.name != '' ? d.name : '?'}}</h6></a>
		</li>
	</ul>
	
	<div class="tab-content" id="subMyTabContent">
		
	<template v-for="(d, index) in perspectives">
		<div v-bind:class="'tab-pane fade ' + (index == currSelTabNum ? 'true active show' : 'false')" v-bind:id="'tabContent'+d.numTab" role="tabpanel" v-bind:aria-labelledby="'tab'+d.numTab">

			<div class="row">
				<div class="col-xs-6 col-md-6 col-lg-6">
					<label v-bind:for="'perName'+index" id="weightLabel">Perspective name&nbsp;<font color="RED">*</font></label>
					<input type="text" v-bind:id="'perName'+index" class="form-control" placeholder="Enter perspective name" v-model="d.name" maxlength="100">
				</div>
				<div class="col-xs-6 col-md-6 col-lg-6">
					<label v-bind:for="'weight'+index">Perspective weight&nbsp;<font color="RED">*</font>&nbsp;({{d.weight}})</label>
					<input type="range" class="custom-range" min="0" max="100" step="0.25" v-bind:id="'weight'+index" name="weight" v-model="d.weight">							
				</div>
			</div>	
			
			<p style="margin-bottom: 10px"></p>
			
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12">
					<button type="button" class="btn btn-warning" id="btnRemovePerspective" title="Remove perspective" v-on:click="removePerspective(index)"><i class="icon fa fa-trash-o"></i>&nbsp;Remove perspective</button>
				<div class="col-xs-12 col-md-12 col-lg-12">
			</div>
			
			<p style="margin-bottom: 10px"></p>
			
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12">
				<table class="table">
					<thead class="thead-dark">
						<tr>
							<th>Strategy objective name</th>
							<th>weight</th>
							<th>#</th>
						</tr>
					</thead>
					
					<template v-for="(p, pIndex) in d.strategyObjectives">
					<tr>
						<td width="45%">
							<input type="text" class="form-control" placeholder="Enter strategy objective name" v-model="p.name" maxlength="100">
						</td>
						<td width="35%">
							<label>weight value&nbsp;({{p.weight}})</label>
							<input type="range" class="custom-range" min="0" max="100" step="0.25" v-model="p.weight">
						</td>
						<td width="10%">
							<button type="button" class="btn btn-dark" title="remove strategy objective item" v-on:click="removeStrategyObjective(index, pIndex)"><i class="icon fa fa-remove"></i></button>
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<select class="form-control" v-model="p.currentSelect1" v-on:change="kpisSelChange(index, pIndex, $event)" style="color:#0F7CFF">
								<option value="all">[&nbsp;&nbsp;&nbsp;Please&nbsp;&nbsp;select&nbsp;&nbsp;a&nbsp;&nbsp;KPI&nbsp;&nbsp;&nbsp;]</option>
								<#list kpiMap?keys as k>
								<option value="${k}">${kpiMap[k]}</option>
								</#list>
							</select>
							
							<br>
							
							<table class="table table-bordered">
								<thead>
									<tr>
										<th>Owner KPI</th>
										<th>KPI weight</th>
										<th>#</th>
									</tr>
								</thead>
								<tr v-for="(n, ownerIndex) in p.kpis">
									<td width="45%">
										{{n.name}}
									</td>
									<td width="35%">
										<label>weight value&nbsp;({{n.weight}})</label>
										<input type="range" class="custom-range" min="0" max="100" step="0.25" v-model="n.weight">
									</td>
									<td width="10%">
										<button type="button" class="btn btn-secondary btn-circle" title="remove strategy objective owner KPI item" v-on:click="removeStrategyObjectiveOwnerKpi(index, pIndex, ownerIndex)"><i class="icon fa fa-remove"></i></button>
									</td>
								</tr>							
							</table>
																
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<select class="form-control" v-model="p.currentSelect2" v-on:change="okrsSelChange(index, pIndex, $event)" style="color:#0F7CFF">
								<option value="all">[&nbsp;&nbsp;&nbsp;Please&nbsp;&nbsp;select&nbsp;&nbsp;a&nbsp;&nbsp;OKR&nbsp;&nbsp;&nbsp;]</option>
								<#list okrMap?keys as k>
								<option value="${k}">${okrMap[k]}</option>
								</#list>
							</select>
							Owner OKRs:&nbsp;
							
			        		<span v-for="(n, ownerIndex) in p.okrs">
			        		<span class="badge badge-secondary"><font size="3">{{n.name}}</font><span class="badge badge-danger btn" v-on:click="removeStrategyObjectiveOwnerOkr(index, pIndex, ownerIndex)">X</span></span>
			        		&nbsp;
			        		</span>
			        		
			        		<hr class="hrDash">
			        									
						</td>
					</tr>										
					</template>
					
				</table>	
				
				<button type="button" class="btn btn-primary btn-circle" title="add Strategy objective item" v-on:click="addStrategyObjective(index)"><i class="icon fa fa-plus"></i></button>
				</div>
								
			</div>

		</div>
	</template>
		
	</div>
	
</div>




</div>

<br/>
<br/>
<br/>

<script type="text/javascript" src="${qifu_basePath}js/hillfog/HF_PROG001D0008E.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>