<#escape x as jsonUtils.encodeJSONString(x)>
{
   "allegato": 
   {
	"id":"${allegato.nodeRef}",
	"nome":"${allegato.name}",
	"downloadUrl":"${allegato.downloadUrl}",
	"mimetype" : "${allegato.mimetype}",
	"pubblico" : "<#if allegato.properties["crlatti:pubblico"]?exists>${allegato.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>",
	"provenienza" : "<#if allegato.properties["crlatti:provenienza"]?exists>${allegato.properties["crlatti:provenienza"]}<#else></#if>"
   }
}
</#escape>