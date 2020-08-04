<#escape x as jsonUtils.encodeJSONString(x)>
{
   "legislatura":"${nomeLegislatura}",
   "List": [
   <#list anniLegislatura as anno>
   { "anno": 
	   {
		"descrizione":"${anno?c}"
	   }
   }<#if anno_has_next>,</#if>
   </#list>
   ]
}
</#escape>