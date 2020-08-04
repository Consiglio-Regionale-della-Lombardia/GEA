<#escape x as jsonUtils.encodeJSONString(x)>
{
   "Atto": 
   {
	"id":"${atto.nodeRef}",
	"passaggio":"${passaggio.name}",
	"noteAula":"<#if note?exists>${note.content}<#else></#if>",
	"linksAula":[
	<#list links as link>
		{
			"link" : 
			{
				"descrizione":"${link.name}",
				"indirizzo":"<#if link.properties["crlatti:indirizzoCollegamento"]?exists>${link.properties["crlatti:indirizzoCollegamento"]}<#else></#if>",
				"pubblico":"<#if link.properties["crlatti:pubblico"]?exists>${link.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>"
			}
		}<#if link_has_next>,</#if>
	</#list>
	]
   }
}
</#escape>