<#if label?? && label != ""> <label for="${id}"> ${label}<#if requiredFlag == "Y" >&nbsp;<font color="RED">*</font></#if></label> </#if>
<textarea class="${cssClass}" id="${id}" name="${name}" placeholder="${placeholder}" rows="${rows}" <#if readonly == "Y" > readonly="readonly" </#if> >${value}</textarea>
<div class="form-control-feedback" id="${id}-feedback"></div>    