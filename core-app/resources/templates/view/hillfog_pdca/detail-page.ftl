<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 

<link rel="stylesheet" href="${qifu_basePath}highcharts-gantt/css/highcharts.css">
<script src="${qifu_basePath}highcharts-gantt/highcharts-gantt.js"></script>

<style type="text/css">

pre {
	text-align: left;
    white-space: pre-line;
}

.styled-table {
    border-collapse: collapse;
    margin: 25px 0;
    font-size: 0.9em;
    font-family: sans-serif;
    min-width: 400px;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}
.styled-table thead tr {
    background-color: #454545;
    color: #ffffff;
    text-align: left;
    font-size: 16px;
}
.styled-table th,
.styled-table td {
    /* padding: 12px 15px; */
    padding: 2px 2px;
    border-left: 1px solid #dddddd;
}
.styled-table tbody tr {
    border-bottom: 1px solid #dddddd;
}
.styled-table tbody tr:nth-of-type(even) {
    background-color: #f3f3f3;
}
.styled-table tbody tr:last-of-type {
    border-bottom: 2px solid #454545;
}
.styled-table tbody tr.active-row {
    font-weight: bold;
    color: #454545;
}

</style>


<script type="text/javascript">

$( document ).ready(function() {
	
	queryPdcaChart('${pdca.oid}');
	
});

function editPdca(pdcaOid) {
	parent.addTab('HF_PROG004D0001E', parent.getProgUrlForOid('HF_PROG004D0001E', pdcaOid));
}

function confirmPdca(pdcaOid) {
	parent.bootbox.confirm(
			"Close this PDCA project?", 
			function(result) { 
				if (!result) {
					return;
				}
				xhrSendParameter(
						'./hfPdcaConfirmJson', 
						{ 
							'pdcaOid' 		: 	pdcaOid,
							'description'	:	$("#description").val()
						}, 
						function(data) {
							if ( _qifu_success_flag != data.success ) {
								parent.notifyWarning( data.message );
							}
							if ( _qifu_success_flag == data.success ) {
								parent.notifyInfo( data.message );
								window.location = parent.getProgUrlForOid('HF_PROG004D0001V', pdcaOid);
							}
						}, 
						function() {
							
						},
						_qifu_defaultSelfPleaseWaitShow
				);
			}
	);		
}

// ----------------------------------------------------------------------------------
// PDCA 甘特圖
// ----------------------------------------------------------------------------------
function queryPdcaChart(pdcaOid) {
	xhrSendParameter(
			'./hfPdcaItemsForGanttDataJson', 
			{
				'oid'			:	pdcaOid,
				'fetchOwner'	:	'Y'
			}, 
			function(data) {
				if ( _qifu_success_flag != data.success ) {
					parent.notifyWarning( data.message );
					return;
				}
				if ( _qifu_success_flag == data.success ) {
					pdcaProjectTimeChart(data.value);
					paintPdcaProjectTableContent( data.value );
				}
			}, 
			function() {
				
			},
			_qifu_defaultSelfPleaseWaitShow
	);	
}
function pdcaProjectTimeChart(data) {
	
	var chartId = 'gantt_container_' + data.main.oid;
	
	if (!($("#"+chartId).length)) {
		return;
	}
	// detail view頁面要顯示
	/*
	if (data.main.confirmDate != null) { // 結案不顯示
		return;
	}
	*/
	var pdcaItemSeries = pdcaProjectChartFillItems( data );
	
	Highcharts.ganttChart(chartId, {
		
		chart: {
			styledMode: true
		},
		
	    title: {
	        text: '<b>' + data.main.name + '</b>',
	    	style: {
         	fontSize:'18px'
         }	        
	    },

	    subtitle: {
	        text: '<b>' + data.main.startDateShow + ' ~ ' + data.main.endDateShow + '</b>',
	    	style: {
         	fontSize:'14px'
         }		        
	    },
	    
	    xAxis: {  	
	    	min: (new Date(data.main.startDateShow + 'T00:00:00Z')).getTime(),
	    	max: (new Date(data.main.endDateShow + 'T00:00:00Z')).getTime()
	    },	    
	    
	    tooltip: {
	        xDateFormat: '%a %b %d'
	    },
	    
	    /*
	    plotOptions: {
	        series: {
	            animation: true, // false - Do not animate dependency connectors
	            dataLabels: {
	                enabled: true,
	                format: '{point.name}',
	                style: {
	                	fontSize: '13px',
	                    cursor: 'default',
	                    pointerEvents: 'none'
	                }
	            }
	        }
	    },
	    */
	    
	    series: pdcaItemSeries
	    
	});	
	
}
function pdcaProjectChartFillItems(pdcaItems) {
	var series = [{
		name	:	pdcaItems.main.name,
		data	:	[]
	}];
	for (var n in pdcaItems.planItemList) {
		var item = pdcaItems.planItemList[n];
		series[0].data.push({
			name		: '(P) - ' + item.name,
			id			: item.oid,
			start		: (new Date(item.startDateShow + 'T00:00:00Z')).getTime(),
			end			: (new Date(item.endDateShow + 'T00:00:00Z')).getTime()		
		});
	}
	for (var n in pdcaItems.doItemList) {
		var item = pdcaItems.doItemList[n];
		series[0].data.push({
			name		: '(D) - ' + item.name,
			id			: item.oid,
			parent		: item.parentOid,
			dependency	: item.parentOid,
			start		: (new Date(item.startDateShow + 'T00:00:00Z')).getTime(),
			end			: (new Date(item.endDateShow + 'T00:00:00Z')).getTime()		
		});
	}
	for (var n in pdcaItems.checkItemList) {
		var item = pdcaItems.checkItemList[n];
		series[0].data.push({
			name		: '(C) - ' + item.name,
			id			: item.oid,
			parent		: item.parentOid,
			dependency	: item.parentOid,
			start		: (new Date(item.startDateShow + 'T00:00:00Z')).getTime(),
			end			: (new Date(item.endDateShow + 'T00:00:00Z')).getTime()		
		});	
	}
	for (var n in pdcaItems.actItemList) {
		var item = pdcaItems.actItemList[n];
		series[0].data.push({
			name		: '(A) - ' + item.name,
			id			: item.oid,
			parent		: item.parentOid,
			dependency	: item.parentOid,
			start		: (new Date(item.startDateShow + 'T00:00:00Z')).getTime(),
			end			: (new Date(item.endDateShow + 'T00:00:00Z')).getTime()		
		});		
	}
	return series;
}
// ----------------------------------------------------------------------------------



// ----------------------------------------------------------------------------------
// PDCA 項目 table顯示
// ----------------------------------------------------------------------------------
function paintPdcaProjectTableContent(data) {
	var itemList = [];
	var str = `
		<table width="100%" class="styled-table">
		<thead>
			<tr>
				<th width="25%" align="center">Plan</th>
				<th width="25%" align="center">Do</th>
				<th width="25%" align="center">Check</th>
				<th width="25%" align="center">Act</th>
			</tr>	
		</thead>
	`;
	//str += '<tbody>';
	for (var p = 0; p < data.planItemList.length; p++) {
		var pItem = data.planItemList[p];
		var pChild = [];
		itemList.push({
			'type'	:	'P',
			'data'	:	pItem,
			'child'	:	pChild
		});
		for (var d = 0; d < data.doItemList.length; d++) {
			var dItem = data.doItemList[d];
			var dChild = [];
			if (dItem.parentOid == pItem.oid) {
				pChild.push({
					'type'	:	'D',
					'data'	:	dItem,
					'child'	:	dChild
				});
			}
			for (var c = 0; c < data.checkItemList.length; c++) {
				var cItem = data.checkItemList[c];
				var cChild = [];
				if (cItem.parentOid == dItem.oid) {
					dChild.push({
						'type'	:	'C',
						'data'	:	cItem,
						'child'	:	cChild
					});
				}
				for (var a = 0; a < data.actItemList.length; a++) {
					var aItem = data.actItemList[a];
					if (aItem.parentOid == cItem.oid) {
						cChild.push({
							'type'	:	'A',
							'data'	:	aItem,
							'child'	:	[]
						});
					}
				}
			}
		}
	}
	
	// 沒有child的, 組出空白的child
	for (var p = 0; p < itemList.length; p++) {
		var pItem = itemList[p];
		if (pItem.child.length == 0) {
			pItem.child.push({
				'type'	:	'D',
				'data'	:	{ 'name' : '' },
				'child'	:	[]				
			});
		}
		for (var d = 0; d < pItem.child.length; d++) {
			var dItem = pItem.child[d];
			if (dItem.child.length == 0) {
				dItem.child.push({
					'type'	:	'C',
					'data'	:	{ 'name' : '' },
					'child'	:	[]					
				});
			}
			for (var c = 0; c < dItem.child.length; c++) {
				var cItem = dItem.child[c];
				if (cItem.child.length == 0) {
					cItem.child.push({
						'type'	:	'A',
						'data'	:	{ 'name' : '' },
						'child'	:	[]							
					});
				}
			}
		}
	}
	
	
	//console.log('itemList = '+JSON.stringify(itemList));
	
	for (var p = 0; p < itemList.length; p++) {
		var pRowspan = 0;
		var pItem = itemList[p];
		var dItem = null;
		var cItem = null;
		var aItem = null;		
		// 算出P的 rowspan
		for (var d = 0; d < pItem.child.length; d++) {
			dItem = pItem.child[d];
			if (dItem.child.length > 0) {
				pRowspan += dItem.child.length;
			} else {
				pRowspan += 1;
			}
		}
		if (pRowspan == 0) {
			pRowspan = 1;
		}
		
		/* --------------------------------------------------------------------------------- */
		// 組tr, td body
		dItem = null;
		cItem = null;
		aItem = null;
		
		str += '<tr>';
		if ('' == pItem.data.name) { // 空白資料
			str += '<td rowspan="' + pRowspan + '" bgcolor="#F2F2F2">' + pItem.data.name + '</td>';
		} else {
			str += '<td rowspan="' + pRowspan + '" bgcolor="#ffffff">' + paintPdcaProjectTableItemConent(pItem.data) + '</td>';
		}
		for (var d = 0; d < pItem.child.length; d++) {
			if (d > 0) {
				str += '<tr>';
			}
			dItem = pItem.child[d];
			cItem = null;
			aItem = null;
			var dRowspan = dItem.child.length;
			if (dRowspan < 1) {
				dRowspan = 1;
			}
			if ('' == dItem.data.name) { // 空白資料
				str += '<td rowspan="' + dRowspan + '" bgcolor="#F2F2F2">' + dItem.data.name + '</td>';
			} else {
				str += '<td rowspan="' + dRowspan + '" bgcolor="#ffffff">' + paintPdcaProjectTableItemConent(dItem.data) + '</td>';
			}
			for (var c = 0; c < dItem.child.length; c++) {
				if (c > 0) {
					str += '<tr>';
				}				
				cItem = dItem.child[c];
				aItem = null;
				var cRowspan = cItem.child.length;
				if (cRowspan < 1) {
					cRowspan = 1;
				}
				if ('' == cItem.data.name) { // 空白資料
					str += '<td rowspan="' + cRowspan + '" bgcolor="#F2F2F2">' + cItem.data.name + '</td>';
				} else {
					str += '<td rowspan="' + cRowspan + '" bgcolor="#ffffff">' + paintPdcaProjectTableItemConent(cItem.data) + '</td>';
				}
				for (var a = 0; a < cItem.child.length; a++) {
					if (a > 0) {
						str += '<tr>';
					}
					aItem = cItem.child[a];
					if ('' == aItem.data.name) { // 空白資料
						str += '<td bgcolor="#F2F2F2">' + aItem.data.name + '</td>';
					} else {
						str += '<td bgcolor="#ffffff">' + paintPdcaProjectTableItemConent(aItem.data) + '</td>';
					}
					str += '</tr>';
				}				
			}
		}
		if (p <= 1) {
			str += '</tr>';
		}
		/* --------------------------------------------------------------------------------- */
		
	}
	
	//str += '</tbody>';
	str += '</table>';
	$("#tableContent").html( str );
	
}
function paintPdcaProjectTableItemConent(itemData) {
	var str = '';
	str += '<span class="badge badge-primary"><H6>' + itemData.name + '</h6></span>';
	str += '<br/>';
	str += '<b>Date:</b>&nbsp;<span class="badge badge-info"><font size="2">' + itemData.startDateShow + ' ~ ' + itemData.endDateShow + '</font></span>';
	str += '<br/>';
	str += '<b>Item owner:</b><br/>';
	for (var i = 0; itemData.ownerNameList != null && i < itemData.ownerNameList.length; i++) {
		str += '<span class="badge badge-secondary"><font size="2">' + itemData.ownerNameList[i] + '</font></span><br/>';
	}
	str += '<b>Description:</b>&nbsp;';
	str += '<br/>';
	str += replaceAll(itemData.description, '\n', '<br/>');
	return str;
}
// ----------------------------------------------------------------------------------

function replaceAll(str, find, replace) {
    return str.replace(new RegExp(find, 'g'), replace);
}

</script>

</head>

<body>

<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 


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
		
		</div>
	</div>	
	
	
<div id="main-content" class="col-xs-12">
		
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
	
			<table class="table">
			 	<thead class="thead-dark">
					<tr>
						<th scope="col">PDCA Number</th>
						<th scope="col">Name</th>
						<th scope="col">Start/End date</th>			
			    	</tr>
				</thead>
			  	<tbody>
					<tr>
						<td><@qifu.out value="pdca.pdcaNum" /></td>
						<td><@qifu.out value="pdca.name" escapeHtml="Y" /></td>
						<td><@qifu.out value="pdca.startDateShow" />&nbsp;~&nbsp;<@qifu.out value="pdca.endDateShow" /></td>
					</tr>
					<tr>
						<td colspan="3">
						Owner:<br>
						<#list pdca.ownerNameList as owner>
						<span class="badge badge-secondary"><font size="3">${owner}</font></span>&nbsp;
						</#list>
						
						</td>
					</tr>	
					<@qifu.if test=" null != attcList && attcList.size > 0 ">
					<tr>
						<td colspan="3">
						Attachment:<br>
						<#list attcList as attc>
						<span class="badge badge-light"><font size="3"><a href="javascript:commonDownloadFile('${attc.uploadOid}')">${attc.showName}</a></font></span>&nbsp;
						</#list>
						
						</td>
					</tr>					
					</@qifu.if>
					<tr>
						<td colspan="3">
						Description:<br>
						<pre>
						${pdca.description}
						</pre>
						</td>
					</tr>
				</tbody>
			</table>
			
			<@qifu.if test=" null == pdca.confirmDate ">
	    	<button type="button" class="btn btn-primary" title="view" onclick="editPdca('${pdca.oid}')"><i class="icon fa fa-edit"></i>&nbsp;Edit</button>
	    	</@qifu.if>
	    	
		</div>	
	</div>	
	
	<br>

	<div class="card border-warning">
	  <div class="card-body">		
				<h4><span class="badge badge-pill badge-warning">Task chart</span></h4>	
				<div id="gantt_container_${pdca.oid}"></div>
		</div>
	</div>
	
	
	<br>
	
	<div id="tableContent"></div>
		
</div>

<br>

<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
	
	<@qifu.if test=" null == pdca.confirmDate ">
	  <div class="form-group">
	    <label for="description">Close description</label>
	    <textarea class="form-control" id="description" rows="3" placeholder="Enter PDCA project close description"></textarea>
	    <button type="button" class="btn btn-warning" title="Close" onclick="confirmPdca('${pdca.oid}')"><i class="icon fa fa-check"></i>&nbsp;Close</button>
	  </div>	
	</div>
	</@qifu.if>
	
	
	<@qifu.if test=" null != closeReqList && closeReqList.size > 0 ">
			<table class="table">
			 	<thead class="thead-dark">
					<tr>
						<th scope="col">Confirm User / date</th>
						<th scope="col">Description</th>			
			    	</tr>
				</thead>
			  	<tbody>	
				
				<#list closeReqList as closeReq>
				
					<tr>
						<td width="30%">${closeReq.applyText}</td>
						<td width="70%"><pre>${closeReq.description}</pre></td>
					</tr>
									
				</#list>
				
				</tbody>
			</table>	
	</@qifu.if>
	
	
</div>

<br/>
<br/>
<br/>

</body>
</html>