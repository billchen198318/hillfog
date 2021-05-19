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
var selEmpList = [];

$( document ).ready(function() {
	
	
});

function btnSave() {
	
}

function btnClear() {
	window.location = parent.getProgUrlForOid('HF_PROG004D0001A', '${oid}') + '&masterType=${masterType}';
}

</script>

</head>

<body>

<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

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
			PDCA Number:&nbsp;${pdcaNum}
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
		
		</div>		
	</div>		
	
</div>	


<br/>
<br/>
<br/>

</body>
</html>