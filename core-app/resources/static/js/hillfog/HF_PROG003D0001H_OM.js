function paintContent() {
	if ( objectivesData == null || objectivesData.length < 1) {
		return;
	}
	var str = '';
	str += `
	<table border="1" width="100%" cellspacing="1" cellpadding="1" style="border:1px #3B3B3B solid; border-radius: 5px; background: #3B3B3B;">
	<tr>
		<td width="30%" align="center" bgcolor="#454545"><b><font color="#FBFBFB">Organization/Department</font></b></td>
		<td width="35%" align="center" bgcolor="#454545"><b><font color="#FBFBFB">Objectives</font></b></td>
		<td width="35%" align="center" bgcolor="#454545"><b><font color="#FBFBFB">Key results</font></b></td>
	</tr>	
	`;
	for (var o in objectivesData) {
		var oo = objectivesData[o];
		var orgDept = oo.orgDept;
		var objectives = oo.objectives;
		var trows = 0;
		for (var i in objectives) {
			trows += objectives[i].keyResultSize;
		}
		str += '<tr>';
		str += `
			<td rowspan='${trows}' bgcolor="#F7F9F9">
				${orgDept.orgId}&nbsp;/&nbsp;${orgDept.name}
				<br><br>
	    		Progress:&nbsp;${oo.totalProgressPercentage}%
				<div class="progress">
					<div class="progress-bar bg-info" role="progressbar" style="width: ${oo.totalProgressPercentage}%" aria-valuenow="${oo.totalProgressPercentage}" aria-valuemin="0" aria-valuemax="100"></div>
				</div>    				
			</td>
		`;
		for (var i in objectives) {
			var obj = objectives[i];
			var orows = obj.keyResultSize;
			if (i > 0) {
				str += '<tr>';
			}
			str += `
				<td rowspan='${orows}' bgcolor="#ffffff">
					${obj.name}
					<br><br>
					Progress:&nbsp;${obj.progressPercentage}%
					<div class="progress">
						<div class="progress-bar bg-info" role="progressbar" style="width: ${obj.progressPercentage}%" aria-valuenow="${obj.progressPercentage}" aria-valuemin="0" aria-valuemax="100"></div>
					</div>    					
				</td>
			`;
			for (var k in obj.keyResList) {
				var kr = obj.keyResList[k];
				if (k > 0) {
					str += '<tr>';
				}
				str += `
					<td bgcolor="#ffffff">
						${kr.name}
						<br><br>
						Progress:&nbsp;${kr.progressPercentage}%
						<div class="progress">
							<div class="progress-bar bg-info" role="progressbar" style="width: ${kr.progressPercentage}%" aria-valuenow="${kr.progressPercentage}" aria-valuemin="0" aria-valuemax="100"></div>
						</div>    						
					</td>
				`;
				str += '</tr>';
			}
		}
	}
	str += '</table>';
	$("#orgOkrsContent").html( str );
}