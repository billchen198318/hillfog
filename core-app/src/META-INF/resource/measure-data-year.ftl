<#setting number_format="0.##">
<input type="hidden" name="dataFor" id="dataFor" value="${dataFor}" />
<input type="hidden" name="frequency" id="frequency" value="${frequency}" />
<input type="hidden" name="account" id="account" value="${account}" />
<input type="hidden" name="orgId" id="orgId" value="${orgId}" />
<input type="hidden" name="queryCalendar" id="queryCalendar" value="Y" />
<table width="100%" border="0" cellspacing="1" cellpadding="1" bgcolor="#E9E9E9" style="border:1px #E9E9E9 solid; border-radius: 5px;">		
	<tr>
		<td colspan="1" bgcolor="#F6F6F6" align="center">
			<img src="./images/go-previous.png" alt="prev" border="0" onclick="prevCalendar();" title="click to query The previous period" />
			&nbsp;
			<b><font color="#333333" size="+3">${yyyy}</font></b>
			&nbsp;
			<img src="./images/go-next.png" alt="next" border="0" onclick="nextCalendar();" title="click to query Next period" />
		</td>
	</tr>	
	<tr>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">Year</font></b></div></td>
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
															
			<table border="0" width="85px">
				<tr>
					<td width="15px" align="center">
						T:
					</td>
					<td width="70px" align="center">
						<input name="MEASURE_DATA_TARGET:${yyyy}0101" 
							type="text"
						    data-dojo-type="dijit/form/NumberTextBox"
						    value="${targetValue}"
						    data-dojo-props="constraints:{pattern: '#.##',min:-9999999.00,max:9999999.00, locale: 'en-us'},
						    invalidMessage:'Please enter a numeric value.',
						    rangeMessage:'Invalid value.'" 
						    style="width: 70px;"
						    maxlength="10" />							
					</td>														
				</tr>
				<tr>
					<td width="15px" align="center">
						A:	
					</td>
					<td width="70px" align="center">
						<input name="MEASURE_DATA_ACTUAL:${yyyy}0101" 
							type="text"
						    data-dojo-type="dijit/form/NumberTextBox"
						    value="${actualValue}"
						    data-dojo-props="constraints:{pattern: '#.##', min:-9999999.00,max:9999999.00, locale: 'en-us'},
						    invalidMessage:'Please enter a numeric value.',
						    rangeMessage:'Invalid value.'" 
						    style="width: 70px;"
						    maxlength="10" />								
					</td>														
				</tr>						
			</table> 	
			    	
		</td>							
	</tr>		
	
	<tr>
		<td bgcolor="#E9E9E9" colspan="1"><b><font color="#333333">T (${targetValueName}) , A (${actualValueName})</font></b></td>
	</tr>		
</table>	