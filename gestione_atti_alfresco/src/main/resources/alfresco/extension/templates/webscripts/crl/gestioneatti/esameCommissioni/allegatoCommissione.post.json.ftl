<#escape x as jsonUtils.encodeJSONString(x)>
{
   "allegato": 
   {
	"id":"${allegato.nodeRef}",
	"nome":"${allegato.name}",
	"downloadUrl":"${allegato.downloadUrl}",
	"mimetype" : "${allegato.mimetype}",
	"tipologia" : "<#if allegato.properties["crlatti:tipologia"]?exists>${allegato.properties["crlatti:tipologia"]}<#else></#if>",
	"pubblico" : "<#if allegato.properties["crlatti:pubblico"]?exists>${allegato.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>"			
   }
}
</#escape>