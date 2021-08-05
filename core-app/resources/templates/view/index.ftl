<html>
  <head>
    <meta name="description" content="QiFu 3 is an JAVA base backend WEB system.">
    <title>hillfog</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Main CSS-->
    <link rel="stylesheet" type="text/css" href="${qifu_basePath}css/main.css">
    <!-- Font-icon css-->
    <link rel="stylesheet" type="text/css" href="${qifu_basePath}font-awesome/css/font-awesome.min.css">
    
    <!-- toastr css -->
    <link rel="stylesheet" type="text/css" href="${qifu_basePath}toastr/toastr.min.css">
    
    
    <link rel="stylesheet" type="text/css" href="${qifu_basePath}css/m.css">
    
  </head>
  <body class="app sidebar-mini">
  
  
<!-- Modal Start here -->
<div class="modal fade bd-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true" id="myPleaseWait" data-keyboard="false" data-backdrop="static">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title" id="mySmallModalLabel"><@spring.message code="page.index.pleaseWait"/></h4>
      </div>
      <div class="modal-body">
        <img alt="loading" src="./images/loadingAnimation.gif" border="0">
      </div>
    </div>
  </div>
</div>
<!-- Modal ends Here -->

<!-- Modal Start here for page query grid -->
<div class="modal fade bd-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallQueryGridModalLabel" aria-hidden="true" id="myPleaseWaitForQueryGrid" data-keyboard="false" data-backdrop="static">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title" id="mySmallQueryGridModalLabel"><@spring.message code="page.index.pleaseWait"/></h4>
      </div>
      <div class="modal-body">
        <img alt="loading" src="./images/loadingAnimation.gif" border="0">
      </div>
    </div>
  </div>
</div>
<!-- Modal ends Here for page query grid -->


<!-- ##################### Modal for Program ##################### -->
${menuResult.modalHtmlData}
<!-- ##################### Modal for Program ##################### -->  
  
  
    <!-- Navbar-->
    <header class="app-header"><a class="app-header__logo" href="${qifu_basePath}index"><img alt="hillfog" src="./images/logo3.png" border="0"/></a>
      <!-- Sidebar toggle button--><a class="app-sidebar__toggle" href="#" data-toggle="sidebar" aria-label="Hide Sidebar"></a>
      <!-- Navbar Right Menu-->
      <ul class="app-nav">
        <!-- User Menu-->
        <li class="dropdown"><a class="app-nav__item" href="#" data-toggle="dropdown" aria-label="Open Profile Menu"><i class="fa fa-user fa-lg"></i></a>
          <ul class="dropdown-menu settings-menu dropdown-menu-right">
            <li><a class="dropdown-item" href="javascript:logoutEvent();"><i class="fa fa-sign-out fa-lg"></i> <@spring.message code="page.index.logout"/></a></li>
          </ul>
        </li>
      </ul>
    </header>
    
    <!-- Sidebar menu-->
    <div class="app-sidebar__overlay" data-toggle="sidebar"></div>
    <aside class="app-sidebar">
    
      <ul class="app-menu">
      
        ${menuResult.navItemHtmlData}
        
        
       	<li><a class="app-menu__item" href="#" onclick="addTab('CORE_PROG999D9999Q', null);"><i class="app-menu__icon fa fa-info-circle"></i><span class="app-menu__label"><@spring.message code="page.index.about"/></span></a></li>
       	<li><a class="app-menu__item" href="#" onclick="logoutEvent();"><i class="app-menu__icon fa fa-power-off"></i><span class="app-menu__label"><@spring.message code="page.index.logout"/></span></a></li>
       	        
        
      </ul>
    </aside>
    
    
    
    <main class="app-content">
    
    	<nav class="tabbable"> <!-- 2021-01-29 add -->
			<!-- Tab -->
			<ul class="nav nav-tabs" id="myTab" role="tablist">
			</ul>
			
			<!-- Tab panes -->
			<div class="tab-content" id="myTabContent">
			</div>   	
					
		</nav> <!-- 2021-01-29 add --> 
      
    </main>
    
    <!-- Config variable -->
    <script src="${qifu_basePath}configJs.js"></script>
    
<script type="text/javascript">
var _m_PAGE_CHANGE_URL_PARAM = _qifu_pageInTabIframeParamName;

${menuResult.javascriptData}

${iconJavascriptData}

function getIconUrlFromOid(oid) {
	var iconUrl = '';
	for (var n in _iconData) {
		if (_iconData[n].oid == oid) {
			iconUrl = _qifu_basePath + 'icons/' + _iconData[n].fileName;
		}
	}
	return iconUrl;
}

function getIconUrlFromId(id) {
	var iconUrl = '';
	for (var n in _iconData) {
		if (_iconData[n].iconId == id) {
			iconUrl = _qifu_basePath + 'icons/' + _iconData[n].fileName;
		}
	}
	return iconUrl;
}

</script>    
    
    <!-- Essential javascripts for application to work-->
    <script src="${qifu_basePath}js/jquery-3.5.1.js"></script>
    <script src="${qifu_basePath}js/popper.min.js"></script>
    <script src="${qifu_basePath}js/bootstrap.js"></script>
    <script src="${qifu_basePath}js/main.js"></script>
    <!-- The javascript plugin to display page loading on top-->
    <script src="${qifu_basePath}js/plugins/pace.min.js"></script>
    
    <!-- bootbox & toastr -->
    <script src="${qifu_basePath}bootbox/bootbox.all.js"></script>
    <script src="${qifu_basePath}toastr/toastr.min.js"></script>
    
    <!-- custom function -->
    <script src="${qifu_basePath}js/m.js"></script>
    
    
<script type="text/javascript">
$( document ).ready(function() {
	
	$('#myTab').bind('show', function(e) {  
	    var paneID = $(e.target).attr('href');
	    var src = $(paneID).attr('data-src');
	    // if the iframe hasn't already been loaded once
	    if($(paneID+" iframe").attr("src")=="")
	    {
	        $(paneID+" iframe").attr("src",src);
	    }
	});
	
	// first load on config
	${firstLoadJavascript}
	
});
</script>    
    
    
  </body>
</html>