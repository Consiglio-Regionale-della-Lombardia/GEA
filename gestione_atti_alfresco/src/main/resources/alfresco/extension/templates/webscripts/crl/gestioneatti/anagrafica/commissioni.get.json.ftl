{
	"List": [
	   <#list commissioni as commissione>
	   { 
		   	"commissione" : 
		   	{
				"descrizione":"${commissione.name}"
		    }
	   }<#if commissione_has_next>,</#if>
	   </#list>
	]
}