<#setting number_format="0.##">
<input type="hidden" name="dataFor" id="dataFor" value="${dataFor}" />
<input type="hidden" name="frequency" id="frequency" value="${frequency}" />
<input type="hidden" name="account" id="account" value="${account}" />
<input type="hidden" name="orgId" id="orgId" value="${orgId}" />
<input type="hidden" name="queryCalendar" id="queryCalendar" value="Y" />
<table class="table table-bordered">
	<tr>
		<td colspan="4" bgcolor="#F6F6F6" align="center">
			<img src="./images/go-previous.png" class="btn btn-light btn-sm" alt="prev" border="0" onclick="prevCalendar();" title="click to query The previous period" />
			&nbsp;
			<b><font color="#333333" size="+3">${yyyy}/${mm}</font></b>
			&nbsp;
			<img src="./images/go-next.png" class="btn btn-light btn-sm" alt="next" border="0" onclick="nextCalendar();" title="click to query Next period" />
		</td>
	</tr>	
	<tr>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">One week</font></b></div></td>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">Two weeks</font></b></div></td>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">Three weeks</font></b></div></td>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">Four weeks</font></b></div></td>
	</tr>	
	
	<tr>
		<td bgcolor="#FFFFFF" align="center">
				
			<#assign targetValue = "" >
			<#assign actualValue = "" >
			<#list masureDatas as masure >
				<#if masure.date == yyyyMM+"01" >
					<#assign targetValue = masure.target >
					<#assign actualValue = masure.actual >					
				</#if>
			</#list>				
			
			<br/>
		    <input type="text" class="form-control" name="MEASURE_DATA_TARGET:${yyyyMM}01" id="MEASURE_DATA_TARGET:${yyyyMM}01" value="${targetValue}" maxlength="12" placeholder="Enter target"></input>
			<br/>
			<input type="text" class="form-control" name="MEASURE_DATA_ACTUAL:${yyyyMM}01" id="MEASURE_DATA_ACTUAL:${yyyyMM}01" value="${actualValue}" maxlength="12" placeholder="Enter actual"></input>
			<br/>	
			   	
		</td>	
		<td bgcolor="#FFFFFF" align="center">
				
			<#assign targetValue = "" >
			<#assign actualValue = "" >
			<#list masureDatas as masure >
				<#if masure.date == yyyyMM+"08" >
					<#assign targetValue = masure.target >
					<#assign actualValue = masure.actual >					
				</#if>
			</#list>

			<br/>
		    <input type="text" class="form-control" name="MEASURE_DATA_TARGET:${yyyyMM}08" id="MEASURE_DATA_TARGET:${yyyyMM}08" value="${targetValue}" maxlength="12" placeholder="Enter target"></input>
			<br/>
			<input type="text" class="form-control" name="MEASURE_DATA_ACTUAL:${yyyyMM}08" id="MEASURE_DATA_ACTUAL:${yyyyMM}08" value="${actualValue}" maxlength="12" placeholder="Enter actual"></input>
			<br/>	
			
		</td>	
		<td bgcolor="#FFFFFF" align="center">
				
			<#assign targetValue = "" >
			<#assign actualValue = "" >
			<#list masureDatas as masure >
				<#if masure.date == yyyyMM+"15" >
					<#assign targetValue = masure.target >
					<#assign actualValue = masure.actual >					
				</#if>
			</#list>
			
			<br/>
		    <input type="text" class="form-control" name="MEASURE_DATA_TARGET:${yyyyMM}15" id="MEASURE_DATA_TARGET:${yyyyMM}15" value="${targetValue}" maxlength="12" placeholder="Enter target"></input>
			<br/>
			<input type="text" class="form-control" name="MEASURE_DATA_ACTUAL:${yyyyMM}15" id="MEASURE_DATA_ACTUAL:${yyyyMM}15" value="${actualValue}" maxlength="12" placeholder="Enter actual"></input>
			<br/>	
			    		
		</td>	
		<td bgcolor="#FFFFFF" align="center">
				
			<#assign targetValue = "" >
			<#assign actualValue = "" >
			<#list masureDatas as masure >
				<#if masure.date == yyyyMM+"22" >
					<#assign targetValue = masure.target >
					<#assign actualValue = masure.actual >					
				</#if>
			</#list>

			<br/>
		    <input type="text" class="form-control" name="MEASURE_DATA_TARGET:${yyyyMM}22" id="MEASURE_DATA_TARGET:${yyyyMM}22" value="${targetValue}" maxlength="12" placeholder="Enter target"></input>
			<br/>
			<input type="text" class="form-control" name="MEASURE_DATA_ACTUAL:${yyyyMM}22" id="MEASURE_DATA_ACTUAL:${yyyyMM}22" value="${actualValue}" maxlength="12" placeholder="Enter actual"></input>
			<br/>	
			
		</td>							
	</tr>		
		
</table>	