<#escape x as jsonUtils.encodeJSONString(x)>
{
	"List": [
	   <#list commissioniReferenti as commissioneReferente>
	   { 
		   	"commissioneReferente" : 
		   	{
				"descrizione":"${commissioneReferente}"
		    }
	   }<#if commissioneReferente_has_next>,</#if>
	   </#list>
	]
}
</#escape>