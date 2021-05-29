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

var _oid = '${objective.oid}';

var orgDeptList = [ ${orgInputAutocomplete} ];
var empList = [ ${empInputAutocomplete} ];
var selDeptList = [ ${selOrgInputAutocomplete} ];
var selEmpList = [ ${selEmpInputAutocomplete} ];

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
	
	$("#btnClearKeyRes").hide();
	$("#btnClearInitiative").hide();
	
	paintOrganization();
	paintEmployee();	
	
	$("#date1").val('${objective.startDateShow}');
	$("#date2").val('${objective.endDateShow}');
	
});

function btnObjectiveList() {
	appUnmount();
	window.location = parent.getProgUrl('HF_PROG001D0006Q');
}

var msgFields = new Object();
msgFields['name'] 		= 'name';
msgFields['date1'] 		= 'date1';
msgFields['date2'] 		= 'date2';
msgFields['objOrg'] 	= 'objOrg';

var formGroups = new Object();
formGroups['name'] 		= 'form-group1';
formGroups['date1'] 	= 'form-group1';
formGroups['date2'] 	= 'form-group1';
formGroups['objOrg'] 	= 'form-group1';

function btnSave() {
	clearWarningMessageField(formGroups, msgFields);
	xhrSendParameter(
			'./hfOkrBaseUpdateJson', 
			{ 
				'oid'			:	_oid,
				'name' 			:	$("#name").val(),
				'date1'			:	$("#date1").val(),
				'date2'			:	$("#date2").val(),
				'description'	:	$("#description").val(),
				'objDept'		:	JSON.stringify( { 'items' : selDeptList } ),
				'objOwner'		:	JSON.stringify( { 'items' : selEmpList } ),
				'keyResults'	:	JSON.stringify( { 'items' : vm.keyResultList } ),
				'initiatives'	:	JSON.stringify( { 'items' : vm.initiativesList } )
			}, 
			function(data) {
				if ( _qifu_success_flag != data.success ) {
					setWarningMessageField(formGroups, msgFields, data.checkFields);
					parent.toastrWarning( data.message );
					return;
				}
				parent.toastrInfo( data.message );
			}, 
			function() {
				btnClear();
			},
			_qifu_defaultSelfPleaseWaitShow
	);
}

function btnClear() {
	appUnmount();
	window.location = parent.getProgUrlForOid('HF_PROG001D0006E', _oid);
}

//====================================================================

function addOrganization() {
	var inputOrgDept = $("#objOrg").val();
	if (null == inputOrgDept || '' == inputOrgDept) {
		parent.toastrInfo( 'Please input organization!' );
		return;
	}
	var checkInOrgDept = false;
	var checkInSelOrgDept = false;	
	for (var n in orgDeptList) {
		if ( orgDeptList[n] == inputOrgDept ) {
			checkInOrgDept = true;
		}
	}
	if (!checkInOrgDept) {
		parent.toastrInfo( 'Please input organization!' );
		return;		
	}
	for (var n in selDeptList) {
		if ( selDeptList[n] == inputOrgDept) {
			checkInSelOrgDept = true;
		}
	}
	if (checkInSelOrgDept) {
		parent.toastrInfo( 'Organization is add found!' );
		return;
	}
	selDeptList.push( inputOrgDept );
	$('#objOrg').val('');
	paintOrganization();
	
}
function paintOrganization() {
	$('#selOrgDeptShowLabel').html( '' );
	var htmlContent = '';
	for (var n in selDeptList) {
		htmlContent += '<span class="badge badge-secondary"><font size="3">' + selDeptList[n] + '</font><span class="badge badge-danger btn" onclick="delAddOrganization(' + n + ');">X</span></span>&nbsp;';
	}
	$('#selOrgDeptShowLabel').html( htmlContent );
}
function delAddOrganization(pos) {
	removeArrayByPos(selDeptList, pos);
	paintOrganization();	
}

function addEmployee() {
	var inputEmployee = $("#objOwner").val();
	if (null == inputEmployee || '' == inputEmployee) {
		parent.toastrInfo( 'Please input employee!' );
		return;
	}
	var checkInEmployee = false;
	var checkInSelEmployee = false;	
	for (var n in empList) {
		if ( empList[n] == inputEmployee ) {
			checkInEmployee = true;
		}
	}
	if (!checkInEmployee) {
		parent.toastrInfo( 'Please input employee!' );
		return;		
	}
	for (var n in selEmpList) {
		if ( selEmpList[n] == inputEmployee) {
			checkInSelEmployee = true;
		}
	}
	if (checkInSelEmployee) {
		parent.toastrInfo( 'Employee is add found!' );
		return;
	}
	selEmpList.push( inputEmployee );
	$('#objOwner').val('');
	paintEmployee();
	
}
function paintEmployee() {
	$('#selEmpShowLabel').html( '' );
	var htmlContent = '';
	for (var n in selEmpList) {
		htmlContent += '<span class="badge badge-secondary"><font size="3">' + selEmpList[n] + '</font><span class="badge badge-danger btn" onclick="delAddEmployee(' + n + ');">X</span></span>&nbsp;';
	}
	$('#selEmpShowLabel').html( htmlContent );
}
function delAddEmployee(pos) {
	removeArrayByPos(selEmpList, pos);
	paintEmployee();	
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

<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col p-2 bg-secondary rounded">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<span class="btn badge btn-info" onclick="btnObjectiveList();"><h6><i class="icon fa fa-backward"></i>&nbsp;Back objective list</h6></span>	
					&nbsp;
					<span class="btn badge btn-info" onclick="btnSave();"><h6><i class="icon fa fa-floppy-o"></i>&nbsp;Update</h6></span>
					&nbsp;
					<span class="btn badge btn-info" onclick="btnClear();"><h6><i class="icon fa fa-hand-paper-o"></i>&nbsp;Cancel</h6></span>
					&nbsp;
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<@qifu.textbox type="text" name="name" value="${objective.name}" id="name" label="Name" requiredFlag="Y" maxlength="100" placeholder="Enter name" />
		</div>	
	</div>		
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox type="date" name="date1" value="" id="date1" label="Start" requiredFlag="Y" maxlength="10" placeholder="Enter start date" />
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox type="date" name="date2" value="" id="date2" label="End" requiredFlag="Y" maxlength="10" placeholder="Enter end date" />
		</div>		
	</div>		
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="objOrg" value="" id="objOrg" label="Organization" requiredFlag="N" maxlength="100" placeholder="Enter organization" />
			<button type="button" class="btn btn-info" id="btnAddOrganization" title="add organization" onclick="addOrganization();"><i class="icon fa fa-plus"></i>&nbsp;ADD</button>
			<div>
				<span id="selOrgDeptShowLabel">&nbsp;</span>
			</div>			
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox name="objOwner" value="" id="objOwner" label="Employee" requiredFlag="N" maxlength="100" placeholder="Enter employee" />
			<button type="button" class="btn btn-info" id="btnAddEmployee" title="add employee" onclick="addEmployee();"><i class="icon fa fa-plus"></i>&nbsp;ADD</button>
			<div>
				<span id="selEmpShowLabel">&nbsp;</span>
			</div>			
		</div>			
	</div>	
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<@qifu.textarea name="description" value="${objective.description}" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>	
	</div>		
</div>
<div class="form-group" id="form-group2">	
	<div class="row">	
		<div id="main-content" class="col-xs-12 col-md-12 col-lg-12">
		
			<br>
			
			<h4><span class="badge badge-pill badge-success">Key Result</span></h4>
			
			<table v-if=" keyResultList.length > 0 " class="table">
	        	<thead>
	        		<tr>
	        			<th>#</th>
	        			<th>Name</th>
	        			<th>Target</th>
	        			<th>Method</th>
	        			<th>Operator</th>
	        			<th>Description</th>
	        		</tr>
	        	</thead>
	        	<tr v-for="(d, index) in keyResultList">  
	        		<td width="10%">
	        			<button type="button" class="btn btn-dark" id="btnRemoveKeyRes" title="remove key result" v-on:click="removeKeyResult(index)"><i class="icon fa fa-remove"></i></button>
	        		</td>
	        		<td width="25%"><input type="text" class="form-control" placeholder="Enter name" v-model="d.name"></td>
	        		<td width="15%"><input type="number" class="form-control" placeholder="Enter target" v-model="d.target"></td>
	        		<td width="15%">
	        			<select class="form-control" v-model="d.gpType">
	        				<option value="1">1 - Sum</option>
	        				<option value="2">2 - Avg</option>
	        				<option value="3">3 - Max</option>
	        				<option value="4">4 - Min</option>
	        			</select>		
	        		</td>
	        		<td width="10%">
	        			<select class="form-control" v-model="d.opTarget">
	        				<option value="1">&nbsp;&gt;</option>
	        				<option value="2">&nbsp;&lt;</option>
	        				<option value="3">&nbsp;=</option>
	        				<option value="4">&nbsp;&gt;=</option>
	        				<option value="5">&nbsp;&lt;=</option>
	        			</select>		
	        		</td>
	        		<td width="25%"><textarea class="form-control" rows="1" placeholder="Enter description" v-model="d.description"></textarea></td>  
	        	</tr>  
			</table>			
			
			<br>
			
			<button type="button" class="btn btn-primary" id="btnAddKeyRes" title="add key result" v-on:click="addKeyResult"><i class="icon fa fa-plus"></i>&nbsp;Add Key Result</button>
			<button type="button" class="btn btn-primary" id="btnClearKeyRes" title="clear key result" v-on:click="clearKeyResult"><i class="icon fa fa-hand-paper-o"></i>&nbsp;Clear Key Result</button>
			
			<br>
			<br>
			<br>
			
			<h4><span class="badge badge-pill badge-success">Initiatives</span></h4>
			
			<table v-if=" initiativesList.length > 0 " class="table">
	        	<thead>
	        		<tr>
	        			<th>#</th>
	        			<th>Content</th>
	        		</tr>
	        	</thead>
	        	<tr v-for="(d, index) in initiativesList">  
	        		<td width="10%">
	        			<button type="button" class="btn btn-dark" id="btnRemoveInitiative" title="remove initiative" v-on:click="removeInitiative(index)"><i class="icon fa fa-remove"></i></button>
	        		</td>
	        		<td width="90%"><textarea class="form-control" rows="2" placeholder="Enter content" v-model="d.content"></textarea></td>  
	        	</tr>  
			</table>	
			
			<button type="button" class="btn btn-primary" id="btnAddInitiative" title="add initiative" v-on:click="addInitiative"><i class="icon fa fa-plus"></i>&nbsp;Add Initiative</button>
			<button type="button" class="btn btn-primary" id="btnClearInitiative" title="clear initiative" v-on:click="clearInitiative"><i class="icon fa fa-hand-paper-o"></i>&nbsp;Clear Initiatives</button>
						
			
		</div>
	</div>
</div>

<br/>
<br/>
<br/>

<script type="text/javascript" src="${qifu_basePath}js/hillfog/HF_PROG001D0006E.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>