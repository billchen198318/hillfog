<script>

var _before_select_page = 1;

function ${clearFunction}() {
	$("#rowCount").html( '0' );
	$("#sizeShow").html( '/1' );
	$("#pageSize").val( '1' );
	$("#showRow").val( '10' );
	$("#select").val( '1' );
	_before_select_page = 1;
	hiddenQueryGridToolBarTable();
	$("#${id}").html( '' );	
}

/**
 * 不顯示換頁TABLE
 */
function hiddenQueryGridToolBarTable() {
	$("#${id}Toolbar").css( "display", "none" );
}

/**
 * 顯示換頁TABLE
 */
function showQueryGridToolBarTable() {
	$("#${id}Toolbar").css( "display", "" );
}

function changeQueryGridPageOfSelect() {
	if ( !( /^\+?(0|[1-9]\d*)$/.test( $("#select").val() ) ) ) { // not a page number
		$("#select").val("1");
	}
	var page = parseInt( $("#select").val() );
	if ( isNaN(page) || page <= 0 ) { // not a page number
		$("#select").val("1");
	}
	if (page>( parseInt( $("#pageSize").val(), 10) || 1 ) ) { // 頁面最小要是1
		page=( parseInt( $("#pageSize").val(), 10) || 1 );
		$("#select").val( page+'' );
	}
	// ----------------------------------------------------------------------------
	
	if ( _before_select_page != page ) {
		${queryFunction}();
	}		
	
}
function changeQueryGridPageOfShowRow() {
	$("#select").val("1");
	${queryFunction}();	
}
function getQueryGridShowRow() {
	return $("#showRow").val();
}
function getQueryGridSelect() {
	return $("#select").val();
}
/**
 * 到第1頁icon click
 */
function changeQueryGridToFirst() {	
	$("#select").val("1");
	${queryFunction}();
}

/**
 * 到最後1頁icon click
 */
function changeQueryGridToLast() {
	$("#select").val( $("#pageSize").val() );
	${queryFunction}();
}

/**
 * 到上1頁icon click
 */
function changeQueryGridToPrev() {
	var page=( parseInt( $("#select").val(), 10 ) || 0 )-1;
	if (page<=0) {
		page=1;
	}
	$("#select").val( page+'' );
	${queryFunction}();
}

/**
 * 到下1頁icon click
 */
function changeQueryGridToNext() {
	var page=( parseInt( $("#select").val(), 10) || 0 )+1;
	if (page>( parseInt( $("#pageSize").val(), 10) || 1 ) ) { // 頁面最小要是1
		page=( parseInt( $("#pageSize").val(), 10) || 1 );
	}
	$("#select").val( page+'' );
	${queryFunction}();
}

function ${queryFunction}() {
	xhrSendParameterForQueryGrid(
			'${xhrUrl}', 
			${xhrParameter}, 
			function(data) {
				if ( _qifu_success_flag != data.success) {
					
					${clearFunction}();
					
					parent.toastrInfo( data.message ); //parent.toastrWarning( data.message );
					return;
				}
				
				var str = '<table class="table table-hover table-bordered">';
				str += '<thead class="thead-light">';
				str += '<tr>';
				var girdHead = ${gridFieldStructure};
				for (var i=0; i<girdHead.length; i++) {
					str += '<th>' + girdHead[i].name + '</th>';
				}
				str += '</tr>';
				str += '</thead>';
				str += '<tbody>';
				
				for (var n=0; n<data.value.length; n++) {
					str += '<tr>';
					for (var i=0; i<girdHead.length; i++) {
						var f = girdHead[i].field;
						var val = data.value[n][f];		
						
						
						if ( !(typeof girdHead[i].formatter == 'undefined') && (typeof girdHead[i].formatter === 'function') ) {
							
							str += '<td>' + girdHead[i].formatter(val) + '</td>';
							continue;
						}
						
						
						if($.type(val) === "string") {
							str += '<td>' + val.replace(/</g, "&lt;").replace(/>/g, "&gt;"); + '</td>';
						} else {
							str += '<td>' + val + '</td>';
						}
					}			
					str += '</tr>';
				}
				
				str += '</tbody>';
				str += '</table>';
				
				
				/* ================================ dataTables paginate ================================ */
				var paginatePrevious = data.pageOfSelect - 1;
				var paginateNext = data.pageOfSelect + 1;
				if (paginatePrevious < 1) {
					paginatePrevious = 1;
				}
				if (paginateNext > data.pageOfSize) {
					paginateNext = data.pageOfSize;
				}
				
				str += '<div class="dataTables_paginate paging_simple_numbers" id="${id}_paginate">';
				str += '<ul class="pagination">';
				str += '<li class="paginate_button page-item previous" id="${id}_paginatePrevious"><a href="###" class="page-link" onclick="changeDataTablePaginate(' + paginatePrevious + ');"><span aria-hidden="true">&laquo;</span><span class="sr-only">Previous</span></a></li>';
				
				var midMaxShow = 3;
				var pStart = data.pageOfSelect - midMaxShow;
				var pEnd = data.pageOfSelect + midMaxShow;
				if (pStart < 1) {
					pStart = 1;
				}
				if (pEnd > data.pageOfSize) {
					pEnd = data.pageOfSize;
				}
				
				if ( pStart > 1 ) {
					str += '<li class="paginate_button page-item " id="${id}_paginate_1"><a href="###" class="page-link" onclick="changeDataTablePaginate(1);">1</a></li>';
					if (pStart > 2) {
						str += '<li class="paginate_button page-item disabled" id="${id}_paginate_left_disabled"><a href="###" class="page-link">&#183;&#183;</a></li>';
					}
				} 
				for ( var p = pStart; p <= pEnd ; p++) {
					var activeStr = ' ';
					if (p == data.pageOfSelect) {
						activeStr = ' active ';
					}
					str += '<li class="paginate_button page-item ' + activeStr + '" id="${id}_paginate_' + p + '"><a href="###" class="page-link" onclick="changeDataTablePaginate(' + p + ');">' + p + '</a></li>';
				}
				if ( pEnd < data.pageOfSize ) {
					if ((pEnd + 1) < data.pageOfSize) {
						str += '<li class="paginate_button page-item disabled" id="${id}_paginate_right_disabled"><a href="###" class="page-link">&#183;&#183;</a></li>';
					}
					str += '<li class="paginate_button page-item " id="${id}_paginate_' + data.pageOfSize + '"><a href="###" class="page-link" onclick="changeDataTablePaginate(' + data.pageOfSize + ');">' + data.pageOfSize + '</a></li>';
				} 				
				
				str += '<li class="paginate_button page-item next" id="${id}_paginateNext"><a href="###" class="page-link" onclick="changeDataTablePaginate(' + paginateNext + ');"><span aria-hidden="true">&raquo;</span><span class="sr-only">Next</span></a></li>';
				str += '</ul>';
				str += '</div>';
				/* ================================ dataTables paginate ================================ */
				
				
				$("#rowCount").html( data.pageOfCountSize );
				$("#sizeShow").html( '/'+data.pageOfSize );
				$("#pageSize").val( data.pageOfSize );
				$("#select").val( data.pageOfSelect );
				_before_select_page = data.pageOfSelect;
				
				showQueryGridToolBarTable();
				
				$("#${id}").html( str );
			}, 
			function(){
				${clearFunction}();
			},
			'${selfPleaseWaitShow}'
	);
}    	

function changeDataTablePaginate(pageNum) {
	$("#select").val( ''+pageNum );
	//${queryFunction}();
	changeQueryGridPageOfSelect();
}

</script>
