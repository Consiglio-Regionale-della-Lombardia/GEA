{
	"List": [
	   <#list gruppi as gruppo>
	   { 
		   	"gruppoConsiliare" : 
		   	{
				"descrizione":"<#if gruppo.properties["crlatti:codiceGruppoConsiliareAnagrafica"]?exists>${gruppo.properties["crlatti:codiceGruppoConsiliareAnagrafica"]}<#else></#if>"		
		    }
	   }<#if gruppo_has_next>,</#if>
	   </#list>
	]
}