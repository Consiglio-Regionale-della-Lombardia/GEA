<#escape x as jsonUtils.encodeJSONString(x)>
{  
   "List" : [
   <#list records as record>
   { 
   		"attoRecord" : 
	   {
	    "id" : "${record.nodeRef}",
		"nome" : "${record.name}",
		"mimetype" : "${record.mimetype}",
		"tipologia" : "<#if record.properties["crlatti:tipologia"]?exists>${record.properties["crlatti:tipologia"]}<#else></#if>",
		"pubblico" : "<#if record.properties["crlatti:pubblico"]?exists>${record.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>",
		"provenienza" : "<#if record.properties["crlatti:provenienza"]?exists>${record.properties["crlatti:provenienza"]}<#else></#if>"
	  }
   }<#if record_has_next>,</#if>
   </#list>
   ]
}
</#escape>