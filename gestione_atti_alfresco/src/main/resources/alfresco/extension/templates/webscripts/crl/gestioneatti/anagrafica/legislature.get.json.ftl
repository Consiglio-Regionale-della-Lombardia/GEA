<#escape x as jsonUtils.encodeJSONString(x)>
{
   "List": [
   <#list legislatureResults as legislaturaNode>
   { 
   	"legislatura" : 
	   {
		"nome":"${legislaturaNode}"
	   }
   }<#if legislaturaNode_has_next>,</#if>
   </#list>
   ]
}
</#escape>