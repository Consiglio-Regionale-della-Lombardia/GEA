<#escape x as jsonUtils.encodeJSONString(x)>
{  
   "List":[
   <#list collegamenti as collegamento>
   { "collegamento" : 
	   {
	    "idAttoCollegato" : "${collegamento.assocs["crlatti:attoAssociatoCollegamento"][0].nodeRef}",
		"numeroAttoCollegato" : "${collegamento.name?split(" ")[1]}",
		"tipoAttoCollegato" : "${collegamento.assocs["crlatti:attoAssociatoCollegamento"][0].typeShort?substring(12)?upper_case}",
		"note" : "<#if collegamento.properties["crlatti:noteCollegamento"]?exists>${collegamento.properties["crlatti:noteCollegamento"]}<#else></#if>"
	   }
   }<#if collegamento_has_next>,</#if>
   </#list>
   ]
}
</#escape>