<#escape x as jsonUtils.encodeJSONString(x)>
{
   "atto": 
   {
	"id":"${atto.nodeRef}",
	"numeroAtto":"${atto.name}",
        "pubblico":"${atto.properties["crlatti:pubblico"]?string}"
   }
}
</#escape>