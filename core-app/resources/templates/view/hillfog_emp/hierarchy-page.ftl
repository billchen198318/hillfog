<html>
<head>
<title>hillfog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<#import "../common-f-inc.ftl" as cfi />
<@cfi.commonFormHeadResource /> 


<link rel="stylesheet" href="${qifu_basePath}ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${qifu_basePath}ztree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${qifu_basePath}ztree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="${qifu_basePath}ztree/js/jquery.ztree.exedit.js"></script>


<style type="text/css">

</style>


<script type="text/javascript">

</script>

</head>

<body>

<@qifu.toolBar 
	id="HF_PROG001D0002S_toolbar" 
	refreshEnable="Y"
	refreshJsMethod="window.location=parent.getProgUrl('HF_PROG001D0002S');" 
	createNewEnable="N"
	createNewJsMethod=""
	saveEnabel="N" 
	saveJsMethod="" 	
	cancelEnable="Y" 
	cancelJsMethod="parent.closeTab('HF_PROG001D0002S');"
	programName="${programName}"
	programId="${programId}"
	description="Employee hierarchy settings." />		
<#import "../common-f-head.ftl" as cfh />
<@cfh.commonFormHeadContent /> 

<h3><span class="badge badge-pill badge-secondary">Drag and Drop change employee hierarchy</span></h3>

<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<div class="zTreeDemoBackground left">
			<ul id="empTree" class="ztree"></ul>
		</div>
	</div>
</div>	

<br/>
<br/>
<br/>

<script type="text/javascript">

var setting = {
	edit: {
		enable: true,
		showRemoveBtn: false,
		showRenameBtn: false
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		beforeDrag: beforeDrag,
		beforeDrop: beforeDrop,
		onDrop: onDrop
		/*
		,
		onClick: onClick
		*/
	}
};

var zNodes = ${ztreeData};

function beforeDrag(treeId, treeNodes) {
	for (var i=0,l=treeNodes.length; i<l; i++) {
		if (treeNodes[i].drag === false) {
			return false;
		}
	}
	return true;
}
function beforeDrop(treeId, treeNodes, targetNode, moveType) {
	return targetNode ? targetNode.drop !== false : true;
}
function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
	var empOid = treeNodes[0].id; // 當前Id
	var parentOid = treeNodes[0].pId; // 移動到的父Id
	if (parentOid == null) {
		parentOid = '00000000-0000-0000-0000-000000000000';
	}
	xhrSendParameter(
		'./hfEmployeeHierarchyUpdateJson', 
		{ 
			'empOid'	:	empOid,
			'parentOid'	:	parentOid
		}, 
		function(data) {
			if ( _qifu_success_flag != data.success ) {
				parent.toastrWarning( data.message );
			}
			if ( _qifu_success_flag == data.success ) {
				parent.toastrInfo( data.message );
			}
			window.location=parent.getProgUrl('HF_PROG001D0002S');
		}, 
		function() {
			window.location=parent.getProgUrl('HF_PROG001D0002S');
		},
		_qifu_defaultSelfPleaseWaitShow
	);
}

$(document).ready(function(){
	$.fn.zTree.init($("#empTree"), setting, zNodes);
});

</script>

</body>
</html>