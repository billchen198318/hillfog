<label for="${id}">${label}<#if requiredFlag == "Y" >&nbsp;<font color="RED">*</font></#if></label>
<select class="${cssClass}" id="${id}" name="${name}" <#if disabled == "Y" >disabled="disabled"</#if> <#if onchange != "" >onchange="${onchange}"</#if> >
	<#if dataSource?? && dataSource?size gt 0>
		<#list dataSource?keys as key>
			<option value="${key}" <#if key == value > selected="selected" </#if> >${dataSource[key]}</option>
		</#list>
	</#if>
</select>
<div class="form-control-feedback" id="${id}-feedback"></div>