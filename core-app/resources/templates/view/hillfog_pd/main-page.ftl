<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<script type="text/javascript" src="${qifu_basePath}js/jquery-ui.min.js?ver=${qifu_jsVerBuild}"></script>

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

$( document ).ready(function() {
	
	
});

function refreshPage() {
	
}

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
					<h4>
					<span class="badge badge-success"><@qifu.out value="employee.empId" escapeHtml="Y" />&nbsp;-&nbsp;<@qifu.out value="employee.name" escapeHtml="Y" /></span>
					&nbsp;
					&nbsp;
					<span class="btn badge btn-info" onclick="refreshPage();"><h6><i class="icon fa fa-refresh"></i>&nbsp;Refresh</h6></span>		
				</div>
			</div>	
		</div>	
	</div>
	
		
</div>

<br>

<div class="col-xs-12">
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
		
			<div class="card border-info">
			  <div class="card-body">		
						<h4><span class="badge badge-pill badge-info">OKRs</span></h4>	
						
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

</body>
</html>