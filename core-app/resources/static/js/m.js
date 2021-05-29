/* Custom method */

var _tabData = [];


function getPageUrl(url) {
	if ( url.indexOf(_m_PAGE_CHANGE_URL_PARAM) > -1 ) {
		return url;
	}
	if ( url.indexOf("?") > -1 ) {
		url += '&' + _m_PAGE_CHANGE_URL_PARAM + '=Y';
	} else {
		url += '?' + _m_PAGE_CHANGE_URL_PARAM + '=Y';
	}	
	return url;
}


function getProgUrl(progId) {
	var pUrl = '';
	for (var i=0; pUrl == '' && i< _prog.length; i++) {
		if ( _prog[i].id == progId ) {
			pUrl = _prog[i].url;
		}
	}	
	return getPageUrl(pUrl);
}

function getProgUrlForOid(progId, oid) {
	var pUrl = '';
	for (var i=0; pUrl == '' && i< _prog.length; i++) {
		if ( _prog[i].id == progId ) {
			pUrl = _prog[i].url;
		}
	}	
	return getPageUrl(pUrl) + '&oid=' + oid;
}

function addTab( tabId, srcUrl ) {
	for (var i=0; _tabData != null && i< _tabData.length; i++) {
		if ( _tabData[i].tabId == tabId ) {
			activaTab(tabId);
			
			/**
			 * refresh iframe content , when before are show active this Tab.
			 * 重新整理 Tab 中的 iframe 內容, 例如如果之前該 Tab 已經顯示了, 在次點擊選單 link 時, 除了 activaTab 顯示該 Tab, 然後重整其 iframe 內容 
			 */
			var src = $("#" + tabId).find('iframe').attr("src");
			if ( '' == srcUrl || null == srcUrl) {
				srcUrl = src;
			}
			$("#" + tabId).find('iframe').attr("src", srcUrl);
			//showPleaseWait();
			return;
		}
	}
	var progName = 'unknown';
	var progIcon = './unknown.png';
	var progUrl = '';
	var fontIconClassId = 'circle-o';
	for (var i=0; _prog != null && i< _prog.length; i++) {
		if ( _prog[i].id == tabId ) {
			progName = _prog[i].name;
			progIcon = _prog[i].icon;
			progUrl = _prog[i].url;
			fontIconClassId = _prog[i].font_icon_class_id;
		}
	}
	if ('' == progUrl || null == progUrl) {
		alert('PROG_ID: ' + tabId + ' no url, cannot call addTab');
		return;
	}
	if ( '' == srcUrl || null == srcUrl) {
		srcUrl = progUrl;
	}
	
	srcUrl = getPageUrl(srcUrl);
	// 2020-01-20 rem
	//$('#myTab').append('<li class="nav-item" id="_tab_' + tabId + '"><a class="nav-link" data-toggle="tab" href="#' + tabId + '" role="tab" aria-controls="' + tabId + '"><img src="' + progIcon + '" broder="0">&nbsp;' + progName + '&nbsp;<span class="badge badge-warning btn" onclick="closeTab(\'' + tabId + '\');">X</span></a></li>');
	// 2020-01-20 add
	$('#myTab').append('<li class="nav-item" id="_tab_' + tabId + '"><a class="nav-link" data-toggle="tab" href="#' + tabId + '" role="tab" aria-controls="' + tabId + '"><i class="icon fa fa-' + fontIconClassId + '"></i>&nbsp;' + progName + '&nbsp;<i class="icon fa fa-close btn-circle btn" style="color:#FF9933;background-color: #F3F3F3" onclick="closeTab(\'' + tabId + '\');"></i></a></li>');
	$('#myTabContent').append('<div class="tab-pane" style="height: 100%;" id="' + tabId + '" data-src="' + srcUrl + '"><iframe src="' + srcUrl + '" ></iframe></div>');
	
	
	_tabData.push({"tabId": tabId});
	
	activaTab(tabId);
	
	//showPleaseWait();
}


/*
function closeTab(tab_li, tabId) {
	
    var tabContentId = $(tab_li).parent().attr("href");
    var li_list = $(tab_li).parent().parent().parent();
    $(tab_li).parent().parent().remove(); //remove li of tab
    if ($(tabContentId).is(":visible")) {
        li_list.find("a").eq(0).tab('show'); // Select first tab
    }

    $(tabContentId).remove(); //remove respective tab content
	
	for (var i=0; _tabData != null && i< _tabData.length; i++) {
		if ( _tabData[i].tabId == tabId ) {
			_tabData.splice(i, 1);
			return;
		}
	}
	
}
*/


function closeTab(tabId) {
	
	// --------------------------------------------------------------------------
	// 2021-05-29 add
	// --------------------------------------------------------------------------
	var currIframe = $("#" + tabId).find('iframe');
	if (currIframe.length > 0) {
		var currIframeWin = currIframe[currIframe.length-1].contentWindow;
		if (!(currIframeWin === undefined) && !(currIframeWin.appUnmount === undefined)) {
			currIframeWin.appUnmount();
		}
	}
	// --------------------------------------------------------------------------
	
	$("#_tab_" + tabId).remove();
	$("#" + tabId).remove();
	
	var prevTabId = '';
	for (var i=0; _tabData != null && i< _tabData.length; i++) {
		if ( _tabData[i].tabId == tabId ) {
			_tabData.splice(i, 1);
			if ( '' != prevTabId && prevTabId != tabId ) {
				activaTab( prevTabId );
			} else {
				$(".nav-tabs a:last").tab('show');
			}
			i = _tabData.length;
		}
		if ( i < _tabData.length) {
			prevTabId = _tabData[i].tabId;
		}
	}
	
}


function activaTab(tab){
    $('.nav-tabs a[href="#' + tab + '"]').tab('show');
};


function showModal(progId, srcUrl) {
	
	var progName = 'unknown';
	var progIcon = './unknown.png';
	var progUrl = '';	
	for (var i=0; _prog != null && i< _prog.length; i++) {
		if ( _prog[i].id == progId ) {
			progName = _prog[i].name;
			progIcon = _prog[i].icon;
			progUrl = _prog[i].url;			
		}
	}
	if ('' == progUrl || null == progUrl) {
		alert('PROG_ID: ' + progId + ' no url, cannot call showModal');
		return;
	}
	if ( '' == srcUrl || null == srcUrl) {
		srcUrl = progUrl;
	}
	
	srcUrl = getPageUrl(srcUrl);
	
	var _modalId = 'modal-' + progId;
	var _modalIframeId = 'modal-iframe-' + progId;	
	
	$('#' + _modalId).on('shown.bs.modal', function() {
		
		$(this).find('iframe').attr('src', srcUrl);
		
	});
	$('#' + _modalId).on('hidden.bs.modal', function() {
		
		//$('#' + _modalIframeId).attr('src', 'about:blank');
		$(this).find('iframe').attr('src', 'about:blank');
		
	});		
	$('#' + _modalId).modal({show:true});	
	
}
function hideModal(progId) {
	var _modalId = 'modal-' + progId;
	var _modalIframeId = 'modal-iframe-' + progId;
	$('#' + _modalId).modal('hide');
}

/* 
 * http://stackoverflow.com/questions/105034/create-guid-uuid-in-javascript 
 */
function guid() {
	
	  function s4() {
	    return Math.floor((1 + Math.random()) * 0x10000)
	      .toString(16)
	      .substring(1);
	  }
	  
	  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
	    s4() + '-' + s4() + s4() + s4();
	  
}

function logoutEvent() {
	bootbox.confirm(
			"Logout! are you sure?", 
			function(result){ 
				if (!result) {
					return;
				}
				window.location = _qifu_basePath + "logout";
			}
	);	
}

function showPleaseWait() {
	//$('#myPleaseWait').modal('show');
	bootbox.dialog({ message: '<div class="text-center"><i class="fa fa-spin fa-spinner"></i> Loading...</div>', closeButton: false });
}
function hidePleaseWait() {
	//$('#myPleaseWait').modal('hide');
	bootbox.hideAll();
}

function showPleaseWaitForQueryGrid() {
	//$('#myPleaseWaitForQueryGrid').modal('show');
	bootbox.dialog({ message: '<div class="text-center"><i class="fa fa-spin fa-spinner"></i> Loading...</div>', closeButton: false });
}
function hidePleaseWaitForQueryGrid() {
	//$('#myPleaseWaitForQueryGrid').modal('hide');
	bootbox.hideAll();
}

function toastrInfo(message) {
	toastr.options = { 
		onclick: function () { alert(message); },
		positionClass: 'toast-bottom-right'
	}
	toastr.info( message.replace(/\n/gi, "<br>").replace("/\r\n", "<br>") );
}
function toastrWarning(message) {
	toastr.options = { 
		onclick: function () { alert(message); },
		positionClass: 'toast-bottom-right'
	}
	toastr.warning( message.replace(/\n/gi, "<br>").replace("/\r\n", "<br>") );
}
function toastrError(message) {
	toastr.options = { 
		onclick: function () { alert(message); },
		positionClass: 'toast-bottom-right'
	}
	toastr.error( message.replace(/\n/gi, "<br>").replace("/\r\n", "<br>") );
}
