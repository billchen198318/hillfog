<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 


<!-- #################### codemirror #################### -->
<script src="${qifu_basePath}codemirror/lib/codemirror.js" type="text/javascript"></script>
<script src="${qifu_basePath}codemirror/addon/edit/matchbrackets.js"></script>
<script src="${qifu_basePath}codemirror/addon/hint/show-hint.js"></script>	
<script src="${qifu_basePath}codemirror/mode/clike/clike.js"></script>	
<!--  
<link rel="stylesheet" type="text/css" href="${qifu_basePath}codemirror/doc/docs.css" />
-->
<link rel="stylesheet" type="text/css" href="${qifu_basePath}codemirror/lib/codemirror.css" />		
<link rel="stylesheet" href="${qifu_basePath}codemirror/addon/hint/show-hint.css">	
		
<script src="${qifu_basePath}codemirror/mode/xml/xml.js"></script>
<script src="${qifu_basePath}codemirror/mode/javascript/javascript.js"></script>
<script src="${qifu_basePath}codemirror/mode/css/css.js"></script>
<script src="${qifu_basePath}codemirror/mode/htmlmixed/htmlmixed.js"></script>
<script src="${qifu_basePath}codemirror/addon/mode/multiplex.js"></script>
<script src="${qifu_basePath}codemirror/mode/htmlembedded/htmlembedded.js"></script>
<!-- #################### codemirror #################### -->


<style type="text/css">

.CodeMirror {
	height:	150px;
}

</style>


<script type="text/javascript">

var javaEditor = null;

$( document ).ready(function() {
	
	$("#returnMode").change(function(){
		
		var mode = $("#returnMode").val();
		if ('C' != mode) {
			$("#returnVar").val('');
			$("#returnVar").prop("readonly", true);			
		} else {
			$("#returnVar").prop("readonly", false);
		}
		
	});
	$("#returnMode").trigger('change');
	
	$("#testMeasureActual").change(function(){
		var valStr = $("#testMeasureActual").val();
		$("#testMeasureActualLabel").text('Test actual value: ' + valStr);
	});
	$("#testMeasureTarget").change(function(){
		var valStr = $("#testMeasureTarget").val();
		$("#testMeasureTargetLabel").text('Test target value: ' + valStr);		
	});
	
	$("#testMeasureActual").trigger('change');
	$("#testMeasureTarget").trigger('change');
	
	javaEditor = CodeMirror.fromTextArea(document.getElementById("expression"), {
		lineNumbers: true,
		matchBrackets: true,
		mode: 'text/x-java'
	});		
	
});

var msgFields = new Object();
msgFields['forId'] 			= 'forId';
msgFields['name'] 			= 'name';
msgFields['type'] 			= 'type';
msgFields['returnMode'] 	= 'returnMode';
msgFields['returnVar'] 		= 'returnVar';
msgFields['expression'] 	= 'expression';

function updateSuccess(data) {
	clearWarningMessageField(msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.notifyWarning( data.message );
		setWarningMessageField(msgFields, data.checkFields);
		return;
	}
	parent.notifyInfo( data.message );
	clearUpdate();
}

function clearUpdate() {
	clearWarningMessageField(msgFields);
	window.location=parent.getProgUrlForOid('HF_PROG001D0003E', '${formula.oid}');
}

function putFormulaValue(str) {
	var formulaValue = javaEditor.getValue();
	javaEditor.setValue(formulaValue + str);
}

function clsFormulaValue() {
	javaEditor.setValue( '' );
}

function testFormulaSuccess(data) {
	clearWarningMessageField(msgFields);
	if ( _qifu_success_flag != data.success ) {
		parent.notifyWarning( data.message );
		setWarningMessageField(msgFields, data.checkFields);
		return;
	}
	parent.notifyInfo( data.message );	
}

</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG001D0003E_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrlForOid('HF_PROG001D0003E', '${formula.oid}');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="Y" 
	saveJsMethod="btnUpdate();" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0003E');"
	programName="${programName}"
	programId="${programId}"
	description="Modify formula item." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="forId" value="formula.forId" id="forId" label="Id" requiredFlag="Y" maxlength="14" placeholder="Enter Id" readonly="Y" />
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="name" value="formula.name" id="name" label="Name" requiredFlag="Y" maxlength="100" placeholder="Enter name" />
		</div>		
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="typeMap" name="type" id="type" value="formula.type" label="Type" requiredFlag="Y"></@qifu.select>
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
		&nbsp;
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.select dataSource="returnModeMap" name="returnMode" id="returnMode" value="formula.returnMode" label="Variable mode" requiredFlag="Y"></@qifu.select>
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="returnVar" value="formula.returnVar" id="returnVar" label="Variable" maxlength="50" placeholder="Enter expression return variable" />
		</div>		
	</div>	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textarea name="description" value="formula.description" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>	
</div>

<div class="form-group" id="form-group2">
	
	<hr>
	
	<div class="row">
		<div class="col-xs-11 col-md-11 col-lg-11">
			<@qifu.textarea name="expression" id="expression" value="formula.expression" label="Formula content" requiredFlag="Y" escapeHtml="N" escapeJavaScript="N"></@qifu.textarea>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-11 col-md-11 col-lg-11">
		
<!-- ========================================================================================= -->

<div class="card text-center">
  <div class="card-header">
    Formula input key-in
  </div>
  <div class="card-body">		
			  		<table border="0" cellpadding="1" cellspacing="0" >
			  			<tr>
			  				<td colspan="6">		
								<label for="testMeasureActual" id="testMeasureActualLabel">Test actual value</label>
								<input type="range" class="custom-range" min="-100" max="100" value="70" id="testMeasureActual" name="testMeasureActual">
								
								<p style="margin-bottom: 10px"></p>
								
								<label for="testMeasureTarget" id="testMeasureTargetLabel">Test target value</label>
								<input type="range" class="custom-range" min="-100" max="100" value="100" id="testMeasureTarget" name="testMeasureTarget">		
								
								<p style="margin-bottom: 10px"></p>
									  					
			  				</td>
			  			</tr>				  			
			  			<tr>
			  				<td colspan="6">			  			
								<button id="btnCalMeasureActual" class="btn btn-dark" 
								onclick="putFormulaValue('$P{actual}')" >Measure data (actual)</button>
								<button id="btnCalMeasureTarget" class="btn btn-dark"
								onclick="putFormulaValue('$P{target}')">Measure data (target)</button>																								  				
			  				</td>
			  			</tr>			  			
			  			<tr>
			  				<td colspan="6">			  			
								<button id="btnCalKpiMax" class="btn btn-dark" 
								onclick="putFormulaValue('$P{kpi.max}')" >KPI (max)</button>
								<button id="btnCalKpiMin" class="btn btn-dark"
								onclick="putFormulaValue('$P{kpi.min}')">KPI (min)</button>			
								<button id="btnCalKpiTarget" class="btn btn-dark"
								onclick="putFormulaValue('$P{kpi.target}')">KPI (target)</button>			
								<button id="btnCalKpiWeight" class="btn btn-dark"
								onclick="putFormulaValue('$P{kpi.weight}')">KPI (weight)</button>																									  				
			  				</td>
			  			</tr>			
			  						  			
			  			<tr>
			  				<td width="17%">
								<button id="btnCal7" class="btn btn-dark"
								onclick="putFormulaValue('7')">7</button>			  				
			  				</td>
			  				<td width="16%">
								<button id="btnCal8" class="btn btn-dark"
								onclick="putFormulaValue('8')">8</button>			  				
			  				</td>
			  				<td width="16%">
								<button id="btnCal9" class="btn btn-dark"
								onclick="putFormulaValue('9')">9</button>				  				
			  				</td>
			  				<td width="16%">
								<button id="btnCalDivision" class="btn btn-dark"
								onclick="putFormulaValue('÷')">÷</button>				  				
			  				</td>
			  				<td width="16%">
			  				
		<@qifu.button id="btnCalTest" label="TEST"
			cssClass="btn btn-warning"
			xhrUrl="./hfFormulaTestJson"
			xhrParameter="
			{
				'forId'				:	$('#forId').val(),
				'name'				:	$('#name').val(),
				'type'				:	$('#type').val(),
				'returnMode'		:	$('#returnMode').val(),
				'returnVar'			:	$('#returnVar').val(),
				'description'		:	$('#description').val(),
				'expression'		:	javaEditor.getValue(),
				'testMeasureActual'	:	$('#testMeasureActual').val(),
				'testMeasureTarget'	:	$('#testMeasureTarget').val()
			}
			"
			onclick="btnTestFormula();"
			loadFunction="testFormulaSuccess(data);"
			errorFunction="clearSave();" />		
				    												  				
			  				</td>		  				
			  				<td width="16%">
								<button id="btnCalClear" class="btn btn-secondary"
								onclick="clsFormulaValue()">Cls</button>				  				
			  				</td>					  				
			  			</tr>
			  			<tr>
			  				<td width="17%">
								<button id="btnCal4" class="btn btn-dark"
								onclick="putFormulaValue('4')">4</button>			  				
			  				</td>
			  				<td width="16%">
								<button id="btnCal5" class="btn btn-dark"
								onclick="putFormulaValue('5')">5</button>			  				
			  				</td>
			  				<td width="16%">
								<button id="btnCal6" class="btn btn-dark"
								onclick="putFormulaValue('6')">6</button>				  				
			  				</td>
			  				<td width="16%">
								<button id="btnCalMultiply" class="btn btn-dark"
								onclick="putFormulaValue('×')">×</button>				  				
			  				</td>
			  				<td width="16%">
								<button id="btnCalLeftParentheses" class="btn btn-dark"
								onclick="putFormulaValue('(')">(</button>				  				
			  				</td>
			  				<td width="16%">
								<button id="btnCalRightParentheses" class="btn btn-dark"
								onclick="putFormulaValue(')')">)</button>				  				
			  				</td>			  							  				
			  			</tr>				  			
			  			<tr>
			  				<td width="17%">
								<button id="btnCal1" class="btn btn-dark"
								onclick="putFormulaValue('1')">1</button>			  				
			  				</td>
			  				<td width="16%">
								<button id="btnCal2" class="btn btn-dark"
								onclick="putFormulaValue('2')">2</button>			  				
			  				</td>
			  				<td width="16%">
								<button id="btnCal3" class="btn btn-dark"
								onclick="putFormulaValue('3')">3</button>				  				
			  				</td>
			  				<td width="16%">
								<button id="btnCalMinus" class="btn btn-dark"
								onclick="putFormulaValue('−')">−</button>				  				
			  				</td>
			  				<td width="16%">		  				
			  				</td>
			  				<td width="16%">		  				
			  				</td>			  							  				
			  			</tr>	
			  			
			  			<tr>
			  				<td width="17%">
								<button id="btnCal0" class="btn btn-dark"
								onclick="putFormulaValue('0')"">0</button>			  				
			  				</td>
			  				<td width="16%">
								<button id="btnCalDot" class="btn btn-dark"
								onclick="putFormulaValue('.')">.</button>			  				
			  				</td>
			  				<td width="16%">
								<button id="btnCalMod" class="btn btn-dark"
								onclick="putFormulaValue('%')">%</button>				  				
			  				</td>
			  				<td width="16%">
								<button id="btnCalPlus" class="btn btn-dark"
								onclick="putFormulaValue('+')">+</button>				  				
			  				</td>
			  				<td width="16%">
								<button id="btnCalLeftAbs" class="btn btn-dark"
								onclick="putFormulaValue('abs(')">abs</button>				  				
			  				</td>
			  				<td width="16%">
								<button id="btnCalRightSqrt" class="btn btn-dark"
								onclick="putFormulaValue('sqrt(')">sqrt</button>				  				
			  				</td>		  				
			  			</tr>			  					  			
			  					  			
			  		</table>
			  		
			  			
	</div>							
</div>		
<!-- ========================================================================================= -->
		
		</div>
	</div>		
	<hr>
		
</div>
						
<p style="margin-bottom: 10px"></p>

<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<@qifu.button id="btnUpdate" label="<i class=\"icon fa fa-floppy-o\"></i>&nbsp;Save"
			xhrUrl="./hfFormulaUpdateJson"
			xhrParameter="
			{
				'oid'				:	'${formula.oid}',
				'forId'				:	'${formula.forId}',
				'name'				:	$('#name').val(),
				'type'				:	$('#type').val(),
				'returnMode'		:	$('#returnMode').val(),
				'returnVar'			:	$('#returnVar').val(),
				'description'		:	$('#description').val(),
				'expression'		:	javaEditor.getValue(),
				'testMeasureActual'	:	$('#testMeasureActual').val(),
				'testMeasureTarget'	:	$('#testMeasureTarget').val()
			}
			"
			onclick="btnUpdate();"
			loadFunction="updateSuccess(data);"
			errorFunction="clearUpdate();" />
		<@qifu.button id="btnClear" label="<i class=\"icon fa fa-hand-paper-o\"></i>&nbsp;Clear" onclick="clearUpdate();" />
	</div>
</div>

<br/>
<br/>
<br/>

</body>
</html>