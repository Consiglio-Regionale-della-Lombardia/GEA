<#escape x as jsonUtils.encodeJSONString(x)>
{
   "allegato": 
   {
	"id":"${allegato.nodeRef}",
	"nome":"${allegato.name}",
	"downloadUrl":"${allegato.downloadUrl}"
   }
}
</#escape>