<#escape x as jsonUtils.encodeJSONString(x)>
{  
   "List":[
   <#list atti as atto>
   { "collegamentoAttiSindacato" : 
	   {
	    "idAtto" : "${atto.nodeRef}",
		"tipoAtto" : "<#if atto.properties["crlatti:tipoAttoIndirizzo"]?exists>${atto.properties["crlatti:tipoAttoIndirizzo"]}<#else></#if>",
		"numeroAtto" : "<#if atto.properties["crlatti:numeroAttoIndirizzo"]?exists>${atto.properties["crlatti:numeroAttoIndirizzo"]}<#else></#if>",
		"oggettoAtto" : "<#if atto.properties["crlatti:oggettoAttoIndirizzo"]?exists>${atto.properties["crlatti:oggettoAttoIndirizzo"]}<#else></#if>"
	   }
   }<#if atto_has_next>,</#if>
   </#list>
   ]
}
</#escape>