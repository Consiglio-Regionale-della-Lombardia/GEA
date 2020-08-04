<#escape x as jsonUtils.encodeJSONString(x)>
{  
   "List":[
   <#list tipiAtto as tipoAtto>
   "${tipoAtto}"
   
   <#if tipoAtto_has_next>,</#if>
   </#list>
   ]
}
</#escape>