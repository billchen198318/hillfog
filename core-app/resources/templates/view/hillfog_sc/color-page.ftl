<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<script type="text/javascript" src="${qifu_basePath}js/vue.global.js"></script>

<style type="text/css">

</style>


<script type="text/javascript">
var _oid = '${scorecard.oid}';

function btnUpdate() {
	xhrSendParameter(
		'./hfScorecardUpdateColorJson', 
		{ 
			'oid'			:	'${scorecard.oid}',
			'defaultColors'	:	JSON.stringify( { 'items' : vm.scorecolorsDefault } ),
			'customColors'	:	JSON.stringify( { 'items' : vm.scorecolorsCustom } )
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
	if ( _qifu_success_flag != data.success ) {
		parent.notifyWarning( data.message );
		return;
	}
	parent.notifyInfo( data.message );
}

function clearUpdate() {
	appUnmount();
	window.location = parent.getProgUrlForOid('HF_PROG001D0008S', '${scorecard.oid}');	
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
	id="HF_PROG001D0008S_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('HF_PROG001D0008S', '${scorecard.oid}')" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnUpdate();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0008S');"
	programName="${programName}"
	programId="${programId}"
	description="Score range color, for Scorecard report show color." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div id="main-content">

<table class="table">	
	<thead class="thead-dark">
		<tr>
			<th>#</th>
			<th>Score range start</th>
			<th>Score range end</th>
			<th>Font color</th>
			<th>Background color</th>
			<th>Test show</th>
		</tr>
	</thead>		
	<tr v-for=" (n, currIndex) in scorecolorsCustom ">
		<td width="15%">
			{{currIndex+1}}
			&nbsp;			
			<button v-if=" currIndex+1 == scorecolorsCustom.length " type="button" class="btn btn-dark" title="remove color item" v-on:click="removeColor(currIndex)"><i class="icon fa fa-remove"></i></button>
			
		</td>
		<td width="20%"><input type="number" class="form-control" placeholder="Enter range start" min="-9999999" max="9999999" v-model="n.range1" maxlength="8"></td>
		<td width="20%"><input type="number" class="form-control" placeholder="Enter range end" min="-9999999" max="9999999" v-model="n.range2" maxlength="8"></td>
		<td width="15%"><input type="color" class="custom" v-model="n.fontColor"></td>
		<td width="15%"><input type="color" class="custom" v-model="n.bgColor"></td>
		<td width="15%"><span v-bind:style="'border-radius: 9%; background: ' + n.bgColor"><font v-bind:color="'' + n.fontColor">&nbsp;TEST&nbsp;</font></span></td>
	</tr>			        			
</table>

<button type="button" class="btn btn-primary btn-circle" title="add color item" v-on:click="addColor()"><i class="icon fa fa-plus"></i></button>

<br><br>

<table class="table">	
	<thead class="thead-dark">
		<tr>
			<th>Font color<br>(Default for out score range)</th>
			<th>Background color<br>(Default for out score range)</th>
			<th>Test show</th>
		</tr>
	</thead>		
	<tr v-for=" (n, currIndex) in scorecolorsDefault ">
		<td width="15%"><input type="color" class="custom" v-model="n.fontColor"></td>
		<td width="15%"><input type="color" class="custom" v-model="n.bgColor"></td>
		<td width="15%"><span v-bind:style="'background: ' + n.bgColor"><font v-bind:color="'' + n.fontColor">TEST</font></span></td>
	</tr>			        			
</table>


</div>

<br/>
<br/>
<br/>

<script type="text/javascript" src="${qifu_basePath}js/hillfog/HF_PROG001D0008S.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>
