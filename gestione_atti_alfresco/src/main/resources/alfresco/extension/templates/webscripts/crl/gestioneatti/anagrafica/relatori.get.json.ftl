<#escape x as jsonUtils.encodeJSONString(x)>
{
	"List": [
	   <#list relatori as relatore>
	   { 
		   	"relatore" : 
		   	{
				"descrizione":"<#if relatore.properties["crlatti:nomeConsigliereAnagrafica"]?exists>${relatore.properties["crlatti:nomeConsigliereAnagrafica"]}<#else></#if> <#if relatore.properties["crlatti:cognomeConsigliereAnagrafica"]?exists>${relatore.properties["crlatti:cognomeConsigliereAnagrafica"]}<#else></#if>",
				"cognomeNome":"<#if relatore.properties["crlatti:cognomeConsigliereAnagrafica"]?exists>${relatore.properties["crlatti:cognomeConsigliereAnagrafica"]}<#else></#if> <#if relatore.properties["crlatti:nomeConsigliereAnagrafica"]?exists>${relatore.properties["crlatti:nomeConsigliereAnagrafica"]}<#else></#if>"
		    }
	   }<#if relatore_has_next>,</#if>
	   </#list>
	]
}
</#escape>