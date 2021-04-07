<#setting number_format="0.##">
<input type="hidden" name="queryCalendar" id="queryCalendar" value="Y" />
<table class="table table-bordered">
	<tr>
		<td colspan="7" bgcolor="#F6F6F6" align="center">
			<img src="./images/go-previous.png" class="btn btn-light btn-sm" alt="prev" border="0" onclick="prevCalendar();" title="click to query The previous period" />
			&nbsp;
			<b><font color="#333333" size="+3">${yyyy}/${mm}</font></b>
			&nbsp;
			<img src="./images/go-next.png" class="btn btn-light btn-sm" alt="next" border="0" onclick="nextCalendar();" title="click to query Next period" />
		</td>
	</tr>	
	<tr>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">Sunday</font></b></div></td>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">Monday</font></b></div></td>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">Tuesday</font></b></div></td>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">Wednesday</font></b></div></td>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">Thursday</font></b></div></td>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">Friday</font></b></div></td>
		<td bgcolor="#F6F6F6" width="14%"><div align="center"><b><font color="#333333" size="+1">Saturday</font></b></div></td>
	</tr>	
	
<#assign len=1 >
<#assign count=1 >
<#assign dayCount=1 >
<#assign nextMonthDay=1 >
<#assign prevMonthDayMinus=dayOfWeek-2 >

<#list len..showLen as i>		<!-- 當月日曆份所需幾星期列 -->

	<tr>
		<#assign day=1 >
		<#list day..7 as d >		<!-- 每列7日 -->
			
			<#if (count >= dayOfWeek) && (dayCount <= maxday) >		<!-- 資料起於當月開始日(dayOfWeek), 並迄於當月最大日(maxday) -->				
				<#assign dayStr = yyyy+mm+dayCount?string?left_pad(2, "0") >
				
				<#assign actualValue = "" >
				<#list masureDatas as masure >
					<#if masure.date == dayStr >
						<#assign actualValue = masure.value >					
					</#if>
				</#list>				

				
				<td bgcolor="#FFFFFF" align="center">
					<b><font color="#FF8000" >${dayCount?string?left_pad(2, "0")}</font></b>	
					
					<br/>
		      		<input type="text" class="form-control" name="MEASURE_DATA_VALUE:${dayStr}" id="MEASURE_DATA_VALUE:${dayStr}" value="${actualValue}" maxlength="12" placeholder="Enter value"></input>
		      		<br/>
      		
				</td>				
											
				<#assign dayCount = dayCount + 1 >
			<#else>
				<!-- 放空白的區塊, 本月無此日期 -->				
				<#if (dayCount > maxday) >		
					<!-- 過完當月了 -->
					<#assign nextMonthDayStr = nextMonthDay?string >					
					<td bgcolor="#F6F6F6" align="center" ><b><font color="#808080" >${nextMonthDayStr?left_pad(2, "0")}</font></b></td>
					<#assign nextMonthDay = nextMonthDay + 1 >				
				<#else>
					<!-- 前一個月月底 -->
					<#assign prevMonthDay = previousMonthMaxDay - prevMonthDayMinus >
					<#assign prevMonthDayStr = prevMonthDay?string >					
					<td bgcolor="#F6F6F6" align="center" ><b><font color="#808080" >${prevMonthDayStr?left_pad(2, "0")}</font></b></td>					
					<#assign prevMonthDayMinus = prevMonthDayMinus - 1 >
				</#if>
				
			</#if>
			
			<#assign count = count+1 >			
		</#list>
	</tr>		
			
</#list>  	

</table>
