<#escape x as jsonUtils.encodeJSONString(x)>
{
   "attoRecord": 
   {
	"id":"${attoRecord.nodeRef}",
	"nome":"${attoRecord.name}",
	"downloadUrl":"${attoRecord.downloadUrl}"
   }
}
</#escape>