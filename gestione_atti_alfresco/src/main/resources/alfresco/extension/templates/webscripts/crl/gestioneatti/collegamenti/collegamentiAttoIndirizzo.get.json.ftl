<#escape x as jsonUtils.encodeJSONString(x)>
{  
   "List":[
   <#list collegamenti as collegamento>
   { "collegamentoAttiSindacato" : 
   <#assign attoIndirizzoCollegato = collegamento.assocs["crlatti:attoAssociatoCollegamentoAttiIndirizzo"][0]>
	   {
	    "idAtto" : "${attoIndirizzoCollegato.nodeRef}",
		"numeroAtto" : "<#if attoIndirizzoCollegato.properties["crlatti:numeroAttoIndirizzo"]?exists>${attoIndirizzoCollegato.properties["crlatti:numeroAttoIndirizzo"]}<#else></#if>",
		"oggettoAtto" : "<#if attoIndirizzoCollegato.properties["crlatti:oggettoAttoIndirizzo"]?exists>${attoIndirizzoCollegato.properties["crlatti:oggettoAttoIndirizzo"]}<#else></#if>",
		"tipoAtto" : "<#if attoIndirizzoCollegato.properties["crlatti:tipoAttoIndirizzo"]?exists>${attoIndirizzoCollegato.properties["crlatti:tipoAttoIndirizzo"]}<#else></#if>",
		"descrizione" : "<#if collegamento.properties["crlatti:descrizioneCollegamentoAttiIndirizzo"]?exists>${collegamento.properties["crlatti:descrizioneCollegamentoAttiIndirizzo"]}<#else></#if>"
	   }
   }<#if collegamento_has_next>,</#if>
   </#list>
   ]
}
</#escape>