<#escape x as jsonUtils.encodeJSONString(x)>
{
   
   "List":[
   <#list tipologieAtto as tipologiaAttoNode>
   { "tipologia" : 
	   {
		"descrizione":"${tipologiaAttoNode.name}"
	   }
   }<#if tipologiaAttoNode_has_next>,</#if>
   </#list>
   ]
}
</#escape>