{
	"List": [
	   <#list commissioni as commissione>
	   { 
		   	"commissione" : 
		   	{
		   	<#if commissione.properties["cm:title"]?exists>"descrizione":"${commissione.title}"<#else>
		   	"descrizione":"${commissione.name}"</#if>"	
		    }
	   }<#if commissione_has_next>,</#if>
	   </#list>
	]
}