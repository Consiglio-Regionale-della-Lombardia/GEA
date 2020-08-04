<#escape x as jsonUtils.encodeJSONString(x)>
{  
   "List":[
   <#if firmatari?exists>
   <#list firmatari as firmatario>
   { "firmatario" : 
	   {
	    "id" : "${firmatario.nodeRef}",
		"descrizione" : "${firmatario.name}",<#assign firmatarioname=firmatario.name?trim?replace('[ \t]+', ' ' , 'r')><#assign nomeFirmatario = firmatarioname?replace('([ \t][A-Z0-9_\\p{Punct}]{2,})+(([ \t][a-z]{2,})?([ \t][A-Z0-9_\\p{Punct}]{2,})+)*', '', 'r')><#assign cognomeFirmatario = firmatarioname?replace('^[A-Z][a-z]{1,}([ \t][A-Z][a-z]{1,})*[ \t]', '', 'r')><#assign firmatariArray = companyhome.childrenByXPath['*[@cm:name="CRL"]/*[@cm:name="Gestione Atti"]/*[@cm:name="Anagrafica"]/*/*[@crlatti:nomeConsigliereAnagrafica="'+nomeFirmatario+'" and @crlatti:cognomeConsigliereAnagrafica="'+cognomeFirmatario+'"]']>
		"primoFirmatario" : "<#if firmatario.properties["crlatti:isPrimoFirmatario"]?exists>${firmatario.properties["crlatti:isPrimoFirmatario"]?string("true","false")}<#else></#if>",
   		"firmatarioPopolare" : "<#if firmatario.properties["crlatti:isFirmatarioPopolare"]?exists>${firmatario.properties["crlatti:isFirmatarioPopolare"]?string("true","false")}<#else></#if>",
   		"gruppoConsiliare" : "<#if firmatario.properties["crlatti:gruppoConsiliare"]?exists>${firmatario.properties["crlatti:gruppoConsiliare"]}<#else></#if>",
		"dataFirma" : "<#if firmatario.properties["crlatti:dataFirma"]?exists>${firmatario.properties["crlatti:dataFirma"]?string("yyyy-MM-dd")}<#else></#if>",
		"dataRitiro" : "<#if firmatario.properties["crlatti:dataRitiro"]?exists>${firmatario.properties["crlatti:dataRitiro"]?string("yyyy-MM-dd")}<#else></#if>",
		"numeroOrdinamento" : "<#if firmatario.properties["crlatti:numeroOrdinamento"]?exists>${firmatario.properties["crlatti:numeroOrdinamento"]}<#else></#if>"<#if firmatariArray?size gt 0 >,
		"id_persona" : ${firmatariArray[0].properties["crlatti:idAnagrafica"]}</#if>
	   }
   }<#if firmatario_has_next>,</#if>
   </#list>
   </#if>
   ]
}
</#escape>