<#escape x as jsonUtils.encodeJSONString(x)>
{
	"List": [
	   <#list tipiIniziative as tipoIniziativa>
	   { 
		   	"tipoIniziativa" : 
		   	{
				"descrizione":"${tipoIniziativa.name}"
		    }
	   }<#if tipoIniziativa_has_next>,</#if>
	   </#list>
	]
}
</#escape>