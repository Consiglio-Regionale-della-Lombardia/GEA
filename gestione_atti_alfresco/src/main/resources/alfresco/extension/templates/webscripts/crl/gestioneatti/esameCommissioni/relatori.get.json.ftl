<#escape x as jsonUtils.encodeJSONString(x)>
{  
   "idAtto": "${atto.nodeRef}",
   "commissione": "${commissione.name}",
   "passaggio": "${passaggio.name}",
   "List":[
   <#list relatori as relatore>
   { "relatore" : 
	   {
	    "descrizione" : "${relatore.name}",
		"dataNomina" : "<#if relatore.properties["crlatti:dataNominaRelatore"]?exists>${relatore.properties["crlatti:dataNominaRelatore"]?string("yyyy-MM-dd")}<#else></#if>",
		"dataUscita" : "<#if relatore.properties["crlatti:dataUscitaRelatore"]?exists>${relatore.properties["crlatti:dataUscitaRelatore"]?string("yyyy-MM-dd")}<#else></#if>"
	   }
   }<#if relatore_has_next>,</#if>
   </#list>
   ]
}
</#escape>