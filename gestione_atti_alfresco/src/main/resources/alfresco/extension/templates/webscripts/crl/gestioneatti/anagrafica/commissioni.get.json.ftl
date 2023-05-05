<#escape x as jsonUtils.encodeJSONString(x)>
{
	"List": [
	   <#list commissioni as commissione>
	   { 
		   	"commissione" : {
				"descrizione":"${commissione}"
		    }
	   }<#if commissione_has_next>,</#if>
	   </#list>
	]
}
</#escape>