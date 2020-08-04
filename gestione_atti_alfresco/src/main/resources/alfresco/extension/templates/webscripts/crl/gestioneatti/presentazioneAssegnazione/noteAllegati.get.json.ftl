<#escape x as jsonUtils.encodeJSONString(x)>
{
   "Atto": 
   {
	"id":"${atto.nodeRef}",
	"notePresentazioneAssegnazione":"<#if note?exists>${note.content}<#else></#if>",
	"linksPresentazioneAssegnazione":[
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