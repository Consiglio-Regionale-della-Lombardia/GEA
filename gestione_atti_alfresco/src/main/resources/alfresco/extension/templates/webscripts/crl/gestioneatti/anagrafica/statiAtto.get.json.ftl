<#escape x as jsonUtils.encodeJSONString(x)>
{
	"List": [
	   <#list statiAtto as statoAtto>
	   { 
		   	"statoAtto" : 
		   	{
				"descrizione":"${statoAtto.name}"
		    }
	   }<#if statoAtto_has_next>,</#if>
	   </#list>
	]
}
</#escape>