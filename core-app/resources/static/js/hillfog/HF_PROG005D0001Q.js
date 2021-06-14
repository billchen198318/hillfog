var backgroundColor = '#292929';
var fontColor = '#FCFCFC';
var infoBackgroundColor = '#F9F9F9';
var infoFontColor = '#212121';
function showContent(vision) {
	var str = `
	<table width="100%" border="1" cellspacing="1" cellpadding="1" bgcolor="${backgroundColor}" style="border:1px ${backgroundColor} solid;">
		<tr>
			<td colspan="3" bgcolor="${vision.bgColor}" align="center">
				<b>
				<font color="${vision.fontColor}" size="+2">
					${vision.name}
					<br>
					Score:&nbsp;${vision.score}
				</font>
				</b>
			</td>
		</tr>
		<tr>
			<td colspan="3" bgcolor="${infoBackgroundColor}" align="left">
				<pre>${vision.content}</pre>
			</td>
		</tr>	
		<tr>
			<td colspan="3" bgcolor="${infoBackgroundColor}" align="left">
				<pre>${vision.mission}</pre>
			</td>
		</tr>			
		<tr>
			<td width="25%" align="left" bgcolor="${backgroundColor}"><b><font color='${fontColor}' size="+1">Perspectives</font></b></td>
			<td width="25%" align="left" bgcolor="${backgroundColor}"><b><font color='${fontColor}' size="+1">Strategy Objectives</font></b></td>
			<td width="50%" align="left" bgcolor="${backgroundColor}"><b><font color='${fontColor}' size="+1">KPIs & OKRs</font></b></td>
		</tr>	
	`;
	str += bodyContent(vision.perspectives);
	str += infoContent(vision);
	str += kpiDateRangeScore(vision.perspectives);
	str += `</table>`;
	//console.log( str );
	$("#content").html(str);
}

function kpiDateRangeScore(perspectives) {
	var str = `
	<tr>
		<td colspan="3" bgcolor="${backgroundColor}" align="left">
			<b><font color="${fontColor}" size="3">KPIs date range score</font></b>
		</td>
	</tr>		
	<tr>
		<td colspan="3" bgcolor="${backgroundColor}" align="left">
			<table width="100%" border="1" cellspacing="1" cellpadding="1" bgcolor="${backgroundColor}" style="border:1px ${backgroundColor} solid;">
	`;
	for (var p = 0; p < perspectives.length; p++) {
		var perspective = perspectives[p];
		for (var so = 0; so < perspective.strategyObjectives.length; so++) {
			var strategyObjective = perspective.strategyObjectives[so];
			for (var k = 0; k < strategyObjective.kpis.length; k++) {
				var kpi = strategyObjective.kpis[k];
				str += `
				<tr>
					<td rowspan="2" align="left" bgcolor="${kpi.bgColor}" width="200px">
						<font color="${kpi.fontColor}" >
						<b>${kpi.name}</b>
						</font>			
					</td>										
				`;
				for (var d = 0; d < kpi.dataRangeScores.length; d++) {
					var dateRangeScore = kpi.dataRangeScores[d];
					str += `
					<td align="center" width="80px" bgcolor="${dateRangeScore.bgColor}">
						<font color="${dateRangeScore.fontColor}" >
						<b>${dateRangeScore.date}</b>
						</font>
					</td>										
					`;
				}
				str += '</tr>';
				
				str += '<tr>';
				for (var d = 0; d < kpi.dataRangeScores.length; d++) {
					var dateRangeScore = kpi.dataRangeScores[d];
					str += `
					<td align="center" width="80px" bgcolor="${dateRangeScore.bgColor}">
						<font color="${dateRangeScore.fontColor}" >
						${dateRangeScore.score}
						</font>
					</td>					
					`;
				}
				str += '</tr>';
			}
		}
	}
	str += '</table></td></tr>';
	return str;
}

function infoContent(vision) {
	var str = `
		<tr>
			<td colspan="3" bgcolor="${infoBackgroundColor}" align="left">
				<b>
				<font color='${infoFontColor}'>
				Frequency: &nbsp;${vision.frequencyName}&nbsp;,&nbsp;Measure-data for employee:&nbsp;${vision.measureDataEmployee}&nbsp;,&nbsp;Measure-data for department:${vision.measureDataOrganization}
				</font>
				</b>
			</td>
		</tr>		
		<tr>
			<td colspan="3" bgcolor="${infoBackgroundColor}" align="left">
				<b>
				<font color='${infoFontColor}'>
				Scorecard scores are used KPIs to calculate, and scores have nothing to do with OKR.
				</font>
				</b>
			</td>
		</tr>		
	`;
	return str;
}

function bodyContent(perspectives) {
	var str = '';
	for (var p = 0; p < perspectives.length; p++) {
		var perspective = perspectives[p];
		str += `
		<tr>
			<td width="25%" bgcolor="${perspective.bgColor}" rowspan="${perspective.row}" >
				<table border="0" width="100%">
					<tr>
						<td width="100%" colspan="2" bgcolor="${perspective.bgColor}">
							<font color="${perspective.fontColor}" >
							<b>${perspective.name}</b>
							</font>
						</td>						
					</tr>
					<tr>
						<td align="left" bgcolor="${perspective.bgColor}" width="15%">
							<font color="${perspective.fontColor}" >
							Score:
							</font>
						</td>
						<td align="left" bgcolor="${perspective.bgColor}" width="85%">
							<font color="${perspective.fontColor}" >
							<b>${perspective.score}</b>
							</font>
						</td>
					</tr>						
					<tr>
						<td align="left" bgcolor="${perspective.bgColor}" width="15%">
							<font color="${perspective.fontColor}" >
							Weight:
							</font>
						</td>
						<td align="left">
							<font color="${perspective.fontColor}" width="85%">
							${perspective.weight}%
							</font>
						</td>
					</tr>
				</table>		
			</td>				
		`;
		for (var so = 0; so < perspective.strategyObjectives.length; so++) {
			var strategyObjective = perspective.strategyObjectives[so];
			if ( so > 0 ) {
				str += '<tr>';
			}
			str += `
			<td width="25%" bgcolor="${strategyObjective.bgColor}" rowspan="${strategyObjective.row}" >
				<table border="0" width="100%">
					<tr>
						<td width="100%" colspan="2" bgcolor="${strategyObjective.bgColor}">
							<font color="${strategyObjective.fontColor}" >
							<b>${strategyObjective.name}</b>
							</font>
						</td>						
					</tr>
					<tr>
						<td align="left" bgcolor="${strategyObjective.bgColor}" width="15%">
							<font color="${strategyObjective.fontColor}" >
							Score:
							</font>
						</td>
						<td align="left" bgcolor="${strategyObjective.bgColor}" width="85%">
							<font color="${strategyObjective.fontColor}" >
							<b>${strategyObjective.score}</b>
							</font>
						</td>
					</tr>					
					<tr>
						<td align="left" bgcolor="${strategyObjective.bgColor}" width="15%">
							<font color="${strategyObjective.fontColor}" >
							Weight:
							</font>
						</td>
						<td align="left">
							<font color="${strategyObjective.fontColor}" width="85%">
							${strategyObjective.weight}%
							</font>
						</td>
					</tr>					
															
				</table>	
			</td>			
			`;
			for (var k = 0; k < strategyObjective.kpis.length; k++) {
				var kpi = strategyObjective.kpis[k];
				var kpiEmployees = '';
				var kpiOrganizations = '';
				for (var n in kpi.employees) {
					kpiEmployees += kpi.employees[n] + ',';
				}
				for (var n in kpi.organizations) {
					kpiOrganizations += kpi.organizations[n] + ',';
				}
				if ( k > 0 ) {
					str += '<tr>';
				}
				str += `
					<td width="50%" bgcolor="${kpi.bgColor}" >
						<table border="0" width="100%">
							<tr>
								<td width="100%" colspan="2" bgcolor="${kpi.bgColor}">
									<font color="${kpi.fontColor}" >
									<b>${kpi.name}</b>
									</font>
								</td>						
							</tr>
							<tr>
								<td align="left" bgcolor="${kpi.bgColor}" width="15%">
									<font color="${kpi.fontColor}" >
									Score:
									</font>
								</td>
								<td align="left" bgcolor="${kpi.bgColor}" width="85%">
									<font color="${kpi.fontColor}" >
									<b>${kpi.score}</b>
									</font>
								</td>
							</tr>							
							<tr>
								<td align="left" bgcolor="${kpi.bgColor}" width="15%">
									<font color="${kpi.fontColor}" >
									Weight:
									</font>
								</td>
								<td align="left">
									<font color="${kpi.fontColor}" width="85%">
									${kpi.weight}%
									</font>
								</td>
							</tr>
							<tr>
								<td align="left" bgcolor="${kpi.bgColor}" width="15%">
									<font color="${kpi.fontColor}" >
									Max:
									</font>
								</td>
								<td align="left" bgcolor="${kpi.bgColor}" width="85%">
									<font color="${kpi.fontColor}" >
									${kpi.max}
									</font>
								</td>
							</tr>								
							<tr>
								<td align="left" bgcolor="${kpi.bgColor}" width="15%">
									<font color="${kpi.fontColor}" >
									Target:
									</font>
								</td>
								<td align="left" bgcolor="${kpi.bgColor}" width="85%">
									<font color="${kpi.fontColor}" >
									${kpi.target}
									</font>
								</td>
							</tr>						
							<tr>
								<td align="left" bgcolor="${kpi.bgColor}" width="15%">
									<font color="${kpi.fontColor}" >
									Min:
									</font>
								</td>
								<td align="left" bgcolor="${kpi.bgColor}" width="85%">
									<font color="${kpi.fontColor}" >
									${kpi.min}
									</font>
								</td>
							</tr>								
							<tr>
								<td align="left" bgcolor="${kpi.bgColor}" width="15%">
									<font color="${kpi.fontColor}" >
									Management:
									</font>
								</td>
								<td align="left" bgcolor="${kpi.bgColor}" width="85%">
									<font color="${kpi.fontColor}" >
									${kpi.managementName}
									</font>
								</td>
							</tr>
							<tr>
								<td align="left" bgcolor="${kpi.bgColor}" width="15%">
									<font color="${kpi.fontColor}" >
									Calculation:
									</font>
								</td>
								<td align="left" bgcolor="${kpi.bgColor}" width="85%">
									<font color="${kpi.fontColor}" >
									${kpi.calculationName}
									</font>
								</td>
							</tr>
							<tr>
								<td align="left" bgcolor="${kpi.bgColor}" width="15%">
									<font color="${kpi.fontColor}" >
									Unit:
									</font>
								</td>
								<td align="left" bgcolor="${kpi.bgColor}" width="85%">
									<font color="${kpi.fontColor}" >
									${kpi.unit}
									</font>
								</td>
							</tr>							
							<tr>
								<td align="left" bgcolor="${kpi.bgColor}" width="15%">
									<font color="${kpi.fontColor}" >
									Formula:
									</font>
								</td>
								<td align="left" bgcolor="${kpi.bgColor}" width="85%">
									<font color="${kpi.fontColor}" >
									${kpi.formulaName}
									</font>
								</td>
							</tr>
							<tr>
								<td align="left" bgcolor="${kpi.bgColor}" width="15%">
									<font color="${kpi.fontColor}" >
									Department:
									</font>
								</td>							
								<td align="left" bgcolor="${kpi.bgColor}" width="85%">
									<font color="${kpi.fontColor}" >									
										${kpiOrganizations}
									</font>
								</td>
							</tr>
							<tr>
								<td align="left" bgcolor="${kpi.bgColor}" width="15%">
									<font color="${kpi.fontColor}" >
									Employee:
									</font>
								</td>								
								<td align="left" bgcolor="${kpi.bgColor}" width="85%">
									<font color="${kpi.fontColor}" >
										${kpiEmployees}
									</font>
								</td>
							</tr>															
							<tr>
								<td align="left" bgcolor="${kpi.bgColor}" width="100%" colspan="2">
									<font color="${kpi.fontColor}" >
									${kpi.description}
									</font>
								</td>
							</tr>
																																								
						</table>				
					</td>				
				`;
				
				if (strategyObjective.okrs.length < 1) {
					str += '</tr>';
				}
				
			}
			
			for (var o = 0; o < strategyObjective.okrs.length; o++) {
				var okr = strategyObjective.okrs[o];
				str += '<tr>'; // 與 KPI 同 col 顯示, 所以都要 <tr>
				str += `
					<td width="50%" bgcolor="${infoBackgroundColor}" >
						<table border="0" width="100%">
							<tr>
								<td width="100%" colspan="2" bgcolor="${infoBackgroundColor}">
									<font color="${infoFontColor}" >
									<b>${okr.name}(OKR)</b>
									</font>
								</td>						
							</tr>
							<tr>
								<td align="left" bgcolor="${infoBackgroundColor}" width="15%">
									<font color="${infoFontColor}" >
									Date:
									</font>
								</td>
								<td align="left" bgcolor="${infoBackgroundColor}" width="85%">
									<font color="${infoFontColor}" >
									${okr.startDate}&nbsp;~&nbsp;${okr.endDate}
									</font>
								</td>
							</tr>	
							<tr>
								<td align="left" bgcolor="${infoBackgroundColor}" width="15%">
									<font color="${infoFontColor}" >
									Process:
									</font>
								</td>
								<td align="left" bgcolor="${infoBackgroundColor}" width="85%">
									<font color="${infoFontColor}" >${okr.progressPercentage}%</font>
									<br>
									<div class="progress">
										<div class="progress-bar bg-info" role="progressbar" style="width: ${okr.progressPercentage}%" aria-valuenow="${okr.progressPercentage}" aria-valuemin="0" aria-valuemax="100"></div>
									</div> 									
								</td>
							</tr>							
						</table>
					</td>						
				`;
				str += '</tr>';
			}
			
		}
		
	}
	return str;
}
