<#escape x as jsonUtils.encodeJSONString(x)>
{  
   "List":[
   <#list abbinamenti as abbinamento>
   { "abbinamento" : 
	   {
	   	"idAtto": "${atto.nodeRef}",
	   	"idAttoAbbinato": "${abbinamento.assocs["crlatti:attoAssociatoAbbinamento"][0].nodeRef}",
		"tipoTesto" : "<#if abbinamento.properties["crlatti:tipoTestoAbbinamento"]?exists>${abbinamento.properties["crlatti:tipoTestoAbbinamento"]}<#else></#if>",
		"dataAbbinamento" : "<#if abbinamento.properties["crlatti:dataAbbinamento"]?exists>${abbinamento.properties["crlatti:dataAbbinamento"]?string("yyyy-MM-dd")}<#else></#if>",
		"dataDisabbinamento" : "<#if abbinamento.properties["crlatti:dataDisabbinamento"]?exists>${abbinamento.properties["crlatti:dataDisabbinamento"]?string("yyyy-MM-dd")}<#else></#if>",
		"note" : "<#if abbinamento.properties["crlatti:noteAbbinamento"]?exists>${abbinamento.properties["crlatti:noteAbbinamento"]}<#else></#if>",
		"numeroAttoAbbinato": "${abbinamento.assocs["crlatti:attoAssociatoAbbinamento"][0].name?split(" ")[1]}",
		"tipoAttoAbbinato" : "${abbinamento.assocs["crlatti:attoAssociatoAbbinamento"][0].typeShort?substring(12)?upper_case}"
	   }
   }<#if abbinamento_has_next>,</#if>
   </#list>
   ]
}
</#escape>

   
   
