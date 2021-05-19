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



.hrDash{
  border: 0 none;
  border-top: 2px dashed #DBDBDB;
  background: none;
  height:0;
}

</style>


<script type="text/javascript">

var empList = [ ${empInputAutocomplete} ];
var selEmpList = [];
var uploadAttc = []; // put upload file OID
var uploadName = []; // put upload file show name

$( document ).ready(function() {
	
	$("#ownerUid").autocomplete({
		source: empList,
		select: function( event, ui ) {
			$("#ownerUid").val( ui.item.label );
			$("#ownerUid").trigger('change');
		}
	}).focus(function() {
		$(this).autocomplete("search", " ");
	});		
	
});

function btnSave() {
	
}

function btnClear() {
	window.location = parent.getProgUrlForOid('HF_PROG004D0001A', '${oid}') + '&masterType=${masterType}';
}

function addEmployee() {
	var inputEmployee = $("#ownerUid").val();
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
	$('#ownerUid').val('');
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

function uploadModal() {
	showCommonUploadModal(
			'uploadOid', 
			'tmp', 
			'Y',
			function() {
				parent.toastrInfo('Upload success!');
				var uploadOid = $("#uploadOid").val();
				xhrSendParameterNoPleaseWait(
						'./hfPdcaLoadUploadFileShowName', 
						{ 'oid' : uploadOid }, 
						function(data, textStatus) {
							if ( _qifu_success_flag == data.success ) {
								uploadName.push( data.value );
							} else {
								uploadName.push( uploadOid );
							}
							uploadAttc.push( uploadOid );
							paintAttc();
						}, 
						function(jqXHR, textStatus, errorThrown) {
							
						}
				);
			},
			function() {
				parent.toastrWarning('Upload fail!');
				paintAttc();
			}
	);
}
function paintAttc() {
	$('#uploadLabel').html( '' );
	var htmlContent = '';
	for (var n in uploadName) {
		htmlContent += '<span class="badge badge-secondary"><font size="3">' + uploadName[n] + '</font><span class="badge badge-danger btn" onclick="delAttc(' + n + ');">X</span></span>&nbsp;';
	}
	$('#uploadLabel').html( htmlContent );
}
function delAttc(pos) {
	removeArrayByPos(uploadAttc, pos);
	removeArrayByPos(uploadName, pos);
	paintAttc();	
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

<input type="hidden" name="uploadOid" id="uploadOid" value="" />

<div class="form-group" id="form-group1">
	<div class="row">
		<div class="col p-2 bg-secondary rounded">
		
		<@qifu.if test=" null != masterType && \"O\" == masterType ">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<h4>
					<span class="badge badge-success"><@qifu.out value="objective.name" escapeHtml="Y" /></span>
					&nbsp;
					<span class="badge badge-light"><@qifu.out value="objective.startDateShow" escapeHtml="Y" />&nbsp;~&nbsp;<@qifu.out value="objective.endDateShow" escapeHtml="Y" /></span>
					</h4>
				</div>
			</div>
		</@qifu.if>
		<@qifu.if test=" null != masterType && \"K\" == masterType ">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<h4>
					<span class="badge badge-success"><@qifu.out value="kpi.name" escapeHtml="Y" /></span>
					&nbsp;
					
					</h4>
				</div>
			</div>		
		</@qifu.if>
			
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<span class="btn badge btn-info" onclick="btnSave();"><h6><i class="icon fa fa-floppy-o"></i>&nbsp;Save</h6></span>
					&nbsp;
					<span class="btn badge btn-info" onclick="btnClear();"><h6><i class="icon fa fa-hand-paper-o"></i>&nbsp;Clear</h6></span>
					&nbsp;
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			PDCA Number:<br><span class="badge badge-success">${pdcaNum}</span>
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox type="text" name="name" value="" id="name" label="Name" requiredFlag="Y" maxlength="100" placeholder="Enter name" />
		</div>		
	</div>		
	
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox type="date" name="startDate" value="startDate" id="startDate" label="Start" requiredFlag="Y" maxlength="10" placeholder="Enter start date" />
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox type="date" name="endDate" value="endDate" id="endDate" label="End" requiredFlag="Y" maxlength="10" placeholder="Enter end date" />
		</div>		
	</div>	
		
	<br>
	
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<@qifu.textbox name="ownerUid" value="" id="ownerUid" label="Owner" requiredFlag="N" maxlength="100" placeholder="Enter employee" />
			<button type="button" class="btn btn-info" id="btnAddEmployee" title="add employee" onclick="addEmployee();"><i class="icon fa fa-plus"></i>&nbsp;ADD</button>
			<br>
			<span id="selEmpShowLabel">&nbsp;</span>
		</div>
	</div>			
	
</div>	

<@qifu.if test=" null != masterType && \"K\" == masterType ">
<div class="form-group" id="form-group2">
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<@qifu.select dataSource="frequencyMap" name="frequency" id="frequency" value="" label="Frequency" requiredFlag="Y"></@qifu.select>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox type="date" name="kpiMeasureDate1" value="" id="kpiMeasureDate1" label="KPI measure-data date start" requiredFlag="Y" maxlength="10" placeholder="Enter measure-data date start" />
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<@qifu.textbox type="date" name="kpiMeasureDate2" value="" id="kpiMeasureDate2" label="KPI measure-data date end" requiredFlag="Y" maxlength="10" placeholder="Enter measure-data date end" />
		</div>		
	</div>
</div>	
</@qifu.if>

<div class="form-group" id="form-group3">
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<@qifu.textarea name="description" value="" id="description" label="Description" rows="3" placeholder="Enter description"></@qifu.textarea>
		</div>
	</div>
	
	<br>
	
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<@qifu.button id="uploadBtn" label="<i class=\"icon fa fa-upload\"></i>&nbsp;Upload file" cssClass="btn btn-info" onclick="uploadModal();">&nbsp;</@qifu.button><div id="uploadLabel"></div>
		</div>
	</div>	
</div>



<div id="main-content">
<div class="form-group" id="form-group4">

	<div class="row">	
		<div class="col-xs-12 col-md-12 col-lg-12">
		
			<h4><span class="badge badge-pill badge-success">Plan</span></h4>
			
			
		</div>
	</div>	
	
	<hr class="hrDash">
	
	<div class="row">	
		<div class="col-xs-12 col-md-12 col-lg-12">
		
			<h4><span class="badge badge-pill badge-success">Do</span></h4>
			
			
		</div>
	</div>	
	
	<hr class="hrDash">
	
	<div class="row">	
		<div class="col-xs-12 col-md-12 col-lg-12">
		
			<h4><span class="badge badge-pill badge-success">Check</span></h4>
			
			
		</div>
	</div>		
	
	<hr class="hrDash">
	
	<div class="row">	
		<div class="col-xs-12 col-md-12 col-lg-12">
		
			<h4><span class="badge badge-pill badge-success">Act</span></h4>
			
			
		</div>
	</div>
	
</div>
</div>



<br/>
<br/>
<br/>

<script type="text/javascript" src="${qifu_basePath}js/hillfog/HF_PROG004D0001A.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>