<#escape x as jsonUtils.encodeJSONString(x)>
{
   "passaggio": 
   		{
   			"nome" : "${nuovoPassaggio.name}",
   			"commissioni" :  
   				[
		    		<#assign commissioni = nuovoPassaggio.childrenByXPath["*[@cm:name='Commissioni']"][0]>
		    		<#assign commissioniList = commissioni.getChildAssocsByType("crlatti:commissione")>
		    		<#list commissioniList as commissione>
		    		{ 
		    			"commissione" :
					   {
					   	"descrizione" : "${commissione.name}",
				    	"dataProposta" : "<#if commissione.properties["crlatti:dataPropostaCommissione"]?exists>${commissione.properties["crlatti:dataPropostaCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
				    	"dataAnnullo" : "<#if commissione.properties["crlatti:dataAnnulloCommissione"]?exists>${commissione.properties["crlatti:dataAnnulloCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
				    	"dataAssegnazione" : "<#if commissione.properties["crlatti:dataAssegnazioneCommissione"]?exists>${commissione.properties["crlatti:dataAssegnazioneCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
				    	"ruolo" : "<#if commissione.properties["crlatti:ruoloCommissione"]?exists>${commissione.properties["crlatti:ruoloCommissione"]}<#else></#if>",
				    	"stato" : "<#if commissione.properties["crlatti:statoCommissione"]?exists>${commissione.properties["crlatti:statoCommissione"]}<#else></#if>"	   	
					   }
					}
					<#if commissione_has_next>,</#if>
					</#list>
			    ],
   			"aula" : 
   				{
   					
   				
   				}
   		
   		}
}

</#escape>