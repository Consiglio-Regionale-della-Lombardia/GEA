<#escape x as jsonUtils.encodeJSONString(x)>
{
	"List": [
	   <#list commissioniConsultive as commissioneConsultiva>
	   { 
		   	"commissioneConsultiva" : 
		   	{
				"descrizione":"${commissioneConsultiva.name}"
		    }
	   }<#if commissioneConsultiva_has_next>,</#if>
	   </#list>
	]
}
</#escape>