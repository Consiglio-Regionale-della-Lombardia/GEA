<#escape x as jsonUtils.encodeJSONString(x)>
{  
   "List":[
   <#list allegati as allegato>
   { "allegato" : 
	   {
	    "id" : "${allegato.nodeRef}",
		"nome" : "${allegato.name}",
		"mimetype" : "${allegato.mimetype}",
		"tipologia" : "<#if allegato.properties["crlatti:tipologia"]?exists>${allegato.properties["crlatti:tipologia"]}<#else></#if>",
		"pubblico" : "<#if allegato.properties["crlatti:pubblico"]?exists>${allegato.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>",
		"provenienza" : "<#if allegato.properties["crlatti:provenienza"]?exists>${allegato.properties["crlatti:provenienza"]}<#else></#if>"
	  }
   }
   <#if allegato_has_next>,</#if>
   </#list>
   ]
}
</#escape>