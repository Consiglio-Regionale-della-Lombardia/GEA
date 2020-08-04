<#escape x as jsonUtils.encodeJSONString(x)>
{
   "Atto": 
   {
	"id":"${atto.nodeRef}",
	"numeroAtto":"<#if atto.properties["crlatti:numeroAtto"]?exists>${atto.properties["crlatti:numeroAtto"]}<#else></#if>",
	"classificazione":"<#if atto.properties["crlatti:classificazione"]?exists>${atto.properties["crlatti:classificazione"]}<#else></#if>",
	"oggetto":"<#if atto.properties["crlatti:oggetto"]?exists>${atto.properties["crlatti:oggetto"]}<#else></#if>",
	"numeroRepertorio":"<#if atto.properties["crlatti:numeroRepertorio"]?exists>${atto.properties["crlatti:numeroRepertorio"]}<#else></#if>",
	"dataRepertorio":"<#if atto.properties["crlatti:dataRepertorio"]?exists>${atto.properties["crlatti:dataRepertorio"]?string("yyyy-MM-dd")}<#else></#if>",
	"tipoIniziativa":"<#if atto.properties["crlatti:tipoIniziativa"]?exists>${atto.properties["crlatti:tipoIniziativa"]}<#else></#if>",
	"dataIniziativa":"<#if atto.properties["crlatti:dataIniziativa"]?exists>${atto.properties["crlatti:dataIniziativa"]?string("yyyy-MM-dd")}<#else></#if>",
	"descrizioneIniziativa":"<#if atto.properties["crlatti:descrizioneIniziativa"]?exists>${atto.properties["crlatti:descrizioneIniziativa"]}<#else></#if>",
	"assegnazione":"<#if atto.properties["crlatti:assegnazione"]?exists>${atto.properties["crlatti:assegnazione"]}<#else></#if>",
	"numeroDgr":"<#if atto.properties["crlatti:numeroDgr"]?exists>${atto.properties["crlatti:numeroDgr"]}<#else></#if>",
	"dataDgr":"<#if atto.properties["crlatti:dataDgr"]?exists>${atto.properties["crlatti:dataDgr"]?string("yyyy-MM-dd")}<#else></#if>",
        "tipologia":"<#if atto.properties["crlatti:tipologia"]?exists>${atto.properties["crlatti:tipologia"]}<#else></#if>",
	"firmatari":[
	<#list firmatari as firmatario>
		{
			"firmatario" : 
			{
				"descrizione":"${firmatario.name}",
				"gruppoConsiliare":"<#if firmatario.properties["crlatti:gruppoConsiliare"]?exists>${firmatario.properties["crlatti:gruppoConsiliare"]}<#else></#if>",
    			"firmatarioPopolare" : "<#if firmatario.properties["crlatti:isFirmatarioPopolare"]?exists>${firmatario.properties["crlatti:isFirmatarioPopolare"]?string("true","false")}<#else></#if>",
    			"dataFirma":"<#if firmatario.properties["crlatti:dataFirma"]?exists>${firmatario.properties["crlatti:dataFirma"]?string("yyyy-MM-dd")}<#else></#if>",
				"dataRitiro":"<#if firmatario.properties["crlatti:dataRitiro"]?exists>${firmatario.properties["crlatti:dataRitiro"]?string("yyyy-MM-dd")}<#else></#if>",
				"primoFirmatario":"<#if firmatario.properties["crlatti:isPrimoFirmatario"]?exists>${firmatario.properties["crlatti:isPrimoFirmatario"]?string("true","false")}<#else></#if>"
			}
		}<#if firmatario_has_next>,</#if>
	</#list>
	]
   }
}
</#escape>