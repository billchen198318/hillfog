window.addEventListener("beforeunload", function (event) { // for refresh icon click in page toolbar
	showPleaseWaitModal();
});
/*
window.addEventListener("load", function(event) { // for addTab() will call showPleaseWait(), in file f.js
	parent.hidePleaseWait();
});
*/

// ====================================================================
// https://stackoverflow.com/questions/995183/how-to-allow-only-numeric-0-9-in-html-inputbox-using-jquery
// Numeric only control handler
jQuery.fn.ForceNumericOnly =
function()
{
    return this.each(function()
    {
        $(this).keydown(function(e)
        {
            var key = e.charCode || e.keyCode || 0;
            // allow backspace, tab, delete, enter, arrows, numbers and keypad numbers ONLY
            // home, end, period, and numpad decimal
            return (
                key == 8 || 
                key == 9 ||
                key == 13 ||
                key == 46 ||
                key == 110 ||
                key == 190 ||
                (key >= 35 && key <= 40) ||
                (key >= 48 && key <= 57) ||
                (key >= 96 && key <= 105));
        });
    });
};
// ====================================================================

/**
 * ====================================================================
 * 後來 please wait modal 改為用 bootbox dialog
 * selfPleaseWaitShow = 'Y' , 使用iframe 內部的 please wait dialog
 * selfPleaseWaitShow = 'N' , NO使用外部的 parent 的 please wait dialog
 * ====================================================================
 */

function xhrSendParameterForQueryGrid(xhrUrl, jsonParam, successFn, errorFn, selfPleaseWaitShow) {
	if (null == selfPleaseWaitShow || _qifu_success_flag != selfPleaseWaitShow) {
		parent.showPleaseWaitForQueryGrid();
	} else {
		showPleaseWaitForQueryGrid();
	}
	$.ajax({
		type : _qifu_jqXhrType,
	    url : xhrUrl,
	    timeout: _qifu_jqXhrTimeout,
	    dataType : 'json',
	    data : jsonParam,
	    cache: _qifu_jqXhrCache,
	    async: _qifu_jqXhrAsync,
	    success : function(data, textStatus) {
	    	if (null == selfPleaseWaitShow || _qifu_success_flag != selfPleaseWaitShow) {
	    		parent.hidePleaseWaitForQueryGrid();
	    	} else {
	    		hidePleaseWaitForQueryGrid();
	    	}
			if (data==null || (typeof data=='undefined') ) {
				alert('Unexpected error!');
				return;
			}    			
			if ( _qifu_success_flag != data.login ) {
				alert("Please try login again!");
				return;
			}       
			if ( _qifu_success_flag != data.isAuthorize ) {
				alert("No permission!");
				return;        				
			}        						
			if ( 'E' == data.success ) { // xhr load success, but has Exception or Error
				parent.notifyError( data.message );
				return;
			}
			successFn(data, textStatus);
	    },
	    error : function(jqXHR, textStatus, errorThrown) {
	    	alert(textStatus);
	    	if (null == selfPleaseWaitShow || _qifu_success_flag != selfPleaseWaitShow) {
	    		parent.hidePleaseWaitForQueryGrid();
	    	} else {
	    		hidePleaseWaitForQueryGrid();
	    	}    	
	        errorFn(jqXHR, textStatus, errorThrown);
	    }
	});
}

function xhrSendParameter(xhrUrl, jsonParam, successFn, errorFn, selfPleaseWaitShow) {
	if (null == selfPleaseWaitShow || _qifu_success_flag != selfPleaseWaitShow) {
		parent.showPleaseWait();
	} else {
		showPleaseWait();
	}
	$.ajax({
		type : _qifu_jqXhrType,
	    url : xhrUrl,
	    timeout: _qifu_jqXhrTimeout,
	    dataType : 'json',
	    data : jsonParam,
	    cache: _qifu_jqXhrCache,
	    async: _qifu_jqXhrAsync,
	    success : function(data, textStatus) {
	    	if (null == selfPleaseWaitShow || _qifu_success_flag != selfPleaseWaitShow) {
	    		parent.hidePleaseWait();
	    	} else {
	    		hidePleaseWait();
	    	}
			if (data==null || (typeof data=='undefined') ) {
				alert('Unexpected error!');
				return;
			}    			
			if ( _qifu_success_flag != data.login ) {
				alert("Please try login again!");
				return;
			}       
			if ( _qifu_success_flag != data.isAuthorize ) {
				alert("No permission!");
				return;        				
			}        						
			if ( 'E' == data.success ) { // xhr load success, but has Exception or Error
				parent.notifyError( data.message );
				return;
			}			
			successFn(data, textStatus);
	    },
	    error : function(jqXHR, textStatus, errorThrown) {
	    	alert(textStatus);
	    	if (null == selfPleaseWaitShow || _qifu_success_flag != selfPleaseWaitShow) {
	    		parent.hidePleaseWait();
	    	} else {
	    		hidePleaseWait();
	    	}
	        errorFn(jqXHR, textStatus, errorThrown);
	    }
	});
}

function xhrSendForm(xhrUrl, formId, successFn, errorFn, selfPleaseWaitShow) {
	if (null == selfPleaseWaitShow || _qifu_success_flag != selfPleaseWaitShow) {
		parent.showPleaseWait();
	} else {
		showPleaseWait();
	}
	$.ajax({
		type : _qifu_jqXhrType,
	    url : xhrUrl,
	    timeout: _qifu_jqXhrTimeout,
	    dataType : 'json',
	    data : $("#"+formId).serialize(),
	    cache: _qifu_jqXhrCache,
	    async: _qifu_jqXhrAsync,
	    success : function(data, textStatus) {
	    	if (null == selfPleaseWaitShow || _qifu_success_flag != selfPleaseWaitShow) {
	    		parent.hidePleaseWait();
	    	} else {
	    		hidePleaseWait();
	    	}
			if (data==null || (typeof data=='undefined') ) {
				alert('Unexpected error!');
				return;
			}    			
			if ( _qifu_success_flag != data.login ) {
				alert("Please try login again!");
				return;
			}       
			if ( _qifu_success_flag != data.isAuthorize ) {
				alert("No permission!");
				return;        				
			}        			
			if ( 'E' == data.success ) { // xhr load success, but has Exception or Error
				parent.notifyError( data.message );
				return;
			}			
			successFn(data, textStatus);
	    },
	    error : function(jqXHR, textStatus, errorThrown) {
	    	alert(textStatus);
	    	if (null == selfPleaseWaitShow || _qifu_success_flag != selfPleaseWaitShow) {
	    		parent.hidePleaseWait();
	    	} else {
	    		hidePleaseWait();
	    	}
	        errorFn(jqXHR, textStatus, errorThrown);
	    }
	});
}

function xhrSendParameterNoPleaseWait(xhrUrl, jsonParam, successFn, errorFn) {
	$.ajax({
		type : _qifu_jqXhrType,
	    url : xhrUrl,
	    timeout: _qifu_jqXhrTimeout,
	    dataType : 'json',
	    data : jsonParam,
	    cache: _qifu_jqXhrCache,
	    async: false, // _qifu_jqXhrAsync
	    success : function(data, textStatus) { 	    	
			if (data==null || (typeof data=='undefined') ) {
				alert('Unexpected error!');
				return;
			}    			
			if ( _qifu_success_flag != data.login ) {
				alert("Please try login again!");
				return;
			}       
			if ( _qifu_success_flag != data.isAuthorize ) {
				alert("No permission!");
				return;        				
			}        					
			if ( 'E' == data.success ) { // xhr load success, but has Exception or Error
				parent.notifyError( data.message );
				return;
			}			
			successFn(data, textStatus);
	    },
	    error : function(jqXHR, textStatus, errorThrown) {    	
	        alert(textStatus);
	        errorFn(jqXHR, textStatus, errorThrown);
	    }
	});
}

function xhrSendFormNoPleaseWait(xhrUrl, formId, successFn, errorFn) {
	$.ajax({
		type : _qifu_jqXhrType,
	    url : xhrUrl,
	    timeout: _qifu_jqXhrTimeout,
	    dataType : 'json',
	    data : $("#"+formId).serialize(),
	    cache: _qifu_jqXhrCache,
	    async: false, // _qifu_jqXhrAsync
	    success : function(data, textStatus) {   	
			if (data==null || (typeof data=='undefined') ) {
				alert('Unexpected error!');
				return;
			}    			
			if ( _qifu_success_flag != data.login ) {
				alert("Please try login again!");
				return;
			}       
			if ( _qifu_success_flag != data.isAuthorize ) {
				alert("No permission!");
				return;        				
			}        			
			if ( 'E' == data.success ) { // xhr load success, but has Exception or Error
				parent.notifyError( data.message );
				return;
			}			
			successFn(data, textStatus);
	    },
	    error : function(jqXHR, textStatus, errorThrown) {	    	
	        alert(textStatus);
	        errorFn(jqXHR, textStatus, errorThrown);
	    }
	});
}

function setWarningMessageField(fields, checkFields) {
	if (null == fields) {
		return;
	}
	for (var k in fields) {
		var idKey = '';
		var msgContent = '';
		for (var d in checkFields) {			
			if ( k == d ) {
				idKey = fields[k];
				msgContent = checkFields[d];
			}
		}
		if (null == idKey || idKey == '') {
			continue;
		}
		$("#"+idKey+"-feedback").addClass( "invalid-feedback" );
		$("#"+idKey+"-feedback").html( msgContent );
		$("#"+idKey).addClass( "is-invalid" );
	}
}
function clearWarningMessageField(fields) {
	for (var k in fields) {
		var idKey = fields[k];
		$("#"+idKey+"-feedback").removeClass( "invalid-feedback" );
		$("#"+idKey+"-feedback").html( "" );
		$("#"+idKey).removeClass( "is-invalid" );
	}
}

function commonOpenJasperReport(jreportId, paramData) {
	var url = "./commonOpenJasperReport" + "?jreportId=" + jreportId;
	for (var key in paramData) {
		url += "&" + key + "=" + paramData[key];
	}
	// 2018-03-29 rem
	//window.open(url, "_blank", "resizable=yes, scrollbars=yes, titlebar=yes, width=970, height=700, top=10, left=10");
	
	// 2018-03-29 add
	var win = window.open(url, '_blank');
	if (win) {
	    win.focus();
	} else {
	    alert('Please allow popups for this website');
	}
}

function commonViewUploadFile(oid) {
	var url = "./commonViewFile" + "?oid=" + oid;
	// 2018-03-29 rem
	//window.open(url, "_blank", "resizable=yes, scrollbars=yes, titlebar=yes, width=970, height=700, top=10, left=10");
	
	// 2018-03-29 add
	var win = window.open(url, '_blank');
	if (win) {
	    win.focus();
	} else {
	    alert('Please allow popups for this website');
	}	
}
