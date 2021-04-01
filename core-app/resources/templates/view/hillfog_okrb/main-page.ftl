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
	
	btnQuery();
	
});

function btnQuery() {
	// query objective items.
}

function btnClear() {
	$("#date1").val('');
	$("#date2").val('');	
	$("#objOrg").val('');
	$("#objOwner").val('');
}

function btnAdd() {
	// to add objective item.
	window.location = parent.getProgUrl('HF_PROG001D0006A');
}

</script>

</head>

<body>

<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

	<div class="row">
		<div class="col p-2 bg-secondary rounded">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-lg-12 text-white">
					<!--  
					<span class="badge badge-info"><h6>Objectives</h6></span>
					&nbsp;
					-->
					<span class="btn badge btn-info" onclick="btnQuery();"><h6><i class="icon fa fa-refresh"></i>&nbsp;Refresh</h6></span>	
					<span class="btn badge btn-info" onclick="btnClear();"><h6><i class="icon fa fa-hand-paper-o"></i>&nbsp;Clear</h6></span>	
					&nbsp;
					&nbsp;
					<span class="btn badge btn-info" onclick="btnAdd();"><h6><i class="icon fa fa-plus-circle"></i>&nbsp;Add objective</h6></span>
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
					<@qifu.textbox name="objOwner" value="" id="objOwner" label="Employee" requiredFlag="N" maxlength="100" placeholder="Enter employee" />
				</div>			
			</div>
		</div>
	</div>
	
	
<br/>
<br/>
<br/>

<script type="text/javascript" src="${qifu_basePath}js/hillfog/HF_PROG001D0006Q.js?ver=${qifu_jsVerBuild}"></script>

</body>
</html>