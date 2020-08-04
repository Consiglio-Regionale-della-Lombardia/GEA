<#escape x as jsonUtils.encodeJSONString(x)>
{
   "allegato": 
   {
	"id":"${allegato.nodeRef}",
	"nome":"${allegato.name}",
	"downloadUrl":"${allegato.downloadUrl}",
	"mimetype" : "${allegato.mimetype}",
	"dataSeduta": "<#if allegato.properties["crlatti:dataSedutaTestoCR"]?exists>${allegato.properties["crlatti:dataSedutaTestoCR"]?string("yyyy-MM-dd")}<#else></#if>",
	"tipologia" : "<#if allegato.properties["crlatti:tipologia"]?exists>${allegato.properties["crlatti:tipologia"]}<#else></#if>",
	"pubblico" : "<#if allegato.properties["crlatti:pubblico"]?exists>${allegato.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>"			
   }
}
</#escape>