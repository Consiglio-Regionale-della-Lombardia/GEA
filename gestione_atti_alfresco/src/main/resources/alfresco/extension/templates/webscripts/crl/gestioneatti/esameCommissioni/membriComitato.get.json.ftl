<#escape x as jsonUtils.encodeJSONString(x)>
{  
   "idAtto": "${atto.nodeRef}",
   "commissione": "${commissione.name}",
   "passaggio": "${passaggio.name}",
   "presenzaComitato" : "<#if presenzaComitato?exists>${presenzaComitato?string("true","false")}<#else></#if>",
   "dataIstituzioneComitato" : "<#if dataIstituzioneComitato?exists>${dataIstituzioneComitato?string("yyyy-MM-dd")}<#else></#if>",
    "tipologia" : "<#if tipologia?exists>${tipologia}<#else></#if>",
   "List":[
   <#list membriComitato as membro>
   { "membro" : 
	   {
	    "descrizione" : "${membro.name}",
		"dataNomina" : "<#if membro.properties["crlatti:dataNominaMCR"]?exists>${membro.properties["crlatti:dataNominaMCR"]?string("yyyy-MM-dd")}<#else></#if>",
		"dataUscita" : "<#if membro.properties["crlatti:dataUscitaMCR"]?exists>${membro.properties["crlatti:dataUscitaMCR"]?string("yyyy-MM-dd")}<#else></#if>",
		"coordinatore" : "<#if membro.properties["crlatti:coordinatoreMCR"]?exists>${membro.properties["crlatti:coordinatoreMCR"]?string("true","false")}<#else></#if>"
		
	   }
   }<#if membro_has_next>,</#if>
   </#list>
   ]
}
</#escape>