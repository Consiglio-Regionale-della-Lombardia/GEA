<#escape x as jsonUtils.encodeJSONString(x)>
{
	"List": [
	   <#list tipiAttoResults as tipoAttoNode>
	   { 
		   	"tipoAtto" : 
		   	{
				"descrizione":"${tipoAttoNode.name} - ${tipoAttoNode.properties.title}",
				"codice":"${tipoAttoNode.name}"
		    }
	   }<#if tipoAttoNode_has_next>,</#if>
	   </#list>
	]
}
</#escape>