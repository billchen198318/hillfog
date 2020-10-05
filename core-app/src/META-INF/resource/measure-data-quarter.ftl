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
			<b><font color="#333333" size="+3">${yyyy}</font></b>
			&nbsp;
			<img src="./images/go-next.png" class="btn btn-light btn-sm" alt="next" border="0" onclick="nextCalendar();" title="click to query Next period" />
		</td>
	</tr>	
	<tr>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">First quarter</font></b></div></td>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">Second quarter</font></b></div></td>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">The third quarter</font></b></div></td>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">Fourth quarter</font></b></div></td>
	</tr>	
	
	<tr>
		<td bgcolor="#FFFFFF" align="center">
				
			<#assign targetValue = "" >
			<#assign actualValue = "" >
			<#list masureDatas as masure >
				<#if masure.date == yyyy+"0101" >
					<#assign targetValue = masure.target >
					<#assign actualValue = masure.actual >					
				</#if>
			</#list>				
			
			<br/>
		    <input type="text" class="form-control" name="MEASURE_DATA_TARGET:${yyyy}0101" id="MEASURE_DATA_TARGET:${yyyy}0101" value="${targetValue}" maxlength="12" placeholder="Enter target"></input>
			<br/>
			<input type="text" class="form-control" name="MEASURE_DATA_ACTUAL:${yyyy}0101" id="MEASURE_DATA_ACTUAL:${yyyy}0101" value="${actualValue}" maxlength="12" placeholder="Enter actual"></input>
			<br/>	
			 	
		</td>	
		<td bgcolor="#FFFFFF" align="center">
				
			<#assign targetValue = "" >
			<#assign actualValue = "" >
			<#list masureDatas as masure >
				<#if masure.date == yyyy+"0401" >
					<#assign targetValue = masure.target >
					<#assign actualValue = masure.actual >					
				</#if>
			</#list>
			
			<br/>
		    <input type="text" class="form-control" name="MEASURE_DATA_TARGET:${yyyy}0401" id="MEASURE_DATA_TARGET:${yyyy}0401" value="${targetValue}" maxlength="12" placeholder="Enter target"></input>
			<br/>
			<input type="text" class="form-control" name="MEASURE_DATA_ACTUAL:${yyyy}0401" id="MEASURE_DATA_ACTUAL:${yyyy}0401" value="${actualValue}" maxlength="12" placeholder="Enter actual"></input>
			<br/>	
			
		</td>	
		<td bgcolor="#FFFFFF" align="center">
				
			<#assign targetValue = "" >
			<#assign actualValue = "" >
			<#list masureDatas as masure >
				<#if masure.date == yyyy+"0701" >
					<#assign targetValue = masure.target >
					<#assign actualValue = masure.actual >					
				</#if>
			</#list>
			
			<br/>
		    <input type="text" class="form-control" name="MEASURE_DATA_TARGET:${yyyy}0701" id="MEASURE_DATA_TARGET:${yyyy}0701" value="${targetValue}" maxlength="12" placeholder="Enter target"></input>
			<br/>
			<input type="text" class="form-control" name="MEASURE_DATA_ACTUAL:${yyyy}0701" id="MEASURE_DATA_ACTUAL:${yyyy}0701" value="${actualValue}" maxlength="12" placeholder="Enter actual"></input>
			<br/>	
				
		</td>	
		<td bgcolor="#FFFFFF" align="center">
				
			<#assign targetValue = "" >
			<#assign actualValue = "" >
			<#list masureDatas as masure >
				<#if masure.date == yyyy+"1001" >
					<#assign targetValue = masure.target >
					<#assign actualValue = masure.actual >					
				</#if>
			</#list>
			
			<br/>
		    <input type="text" class="form-control" name="MEASURE_DATA_TARGET:${yyyy}1001" id="MEASURE_DATA_TARGET:${yyyy}1001" value="${targetValue}" maxlength="12" placeholder="Enter target"></input>
			<br/>
			<input type="text" class="form-control" name="MEASURE_DATA_ACTUAL:${yyyy}1001" id="MEASURE_DATA_ACTUAL:${yyyy}1001" value="${actualValue}" maxlength="12" placeholder="Enter actual"></input>
			<br/>	
			
		</td>							
	</tr>		
		
</table>	