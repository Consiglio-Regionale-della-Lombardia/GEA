<#escape x as jsonUtils.encodeJSONString(x)>
{
   "attoRecord": 
   {
	"id":"${attoRecord.nodeRef}",
	"nome":"${attoRecord.name}",
	"downloadUrl":"${attoRecord.downloadUrl}",
	"mimetype" : "${attoRecord.mimetype}",
	"tipologia" : "<#if attoRecord.properties["crlatti:tipologia"]?exists>${attoRecord.properties["crlatti:tipologia"]}<#else></#if>",
	"tipologiaTesto" : "<#if attoRecord.properties["crlatti:tipologiaTesto"]?exists>${attoRecord.properties["crlatti:tipologiaTesto"]}<#else></#if>",
	"pubblico" : "<#if attoRecord.properties["crlatti:pubblico"]?exists>${attoRecord.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>"
   }
}
</#escape>