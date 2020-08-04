<#escape x as jsonUtils.encodeJSONString(x)>
{
   "atto": 
   {
	"id" : "${atto.nodeRef}",
	"nome" : "${atto.name}",
	"tipoAtto" : "${tipoAtto}",
	"pubblico" : "<#if atto.properties["crlatti:pubblico"]?exists>${atto.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>",
	"numeroAtto" : "${atto.name}",
	"estensioneAtto" : "<#if atto.properties["crlatti:estensioneAtto"]?exists>${atto.properties["crlatti:estensioneAtto"]}<#else></#if>",
	"tipologia" : "<#if atto.properties["crlatti:tipologia"]?exists>${atto.properties["crlatti:tipologia"]}<#else></#if>",
	"legislatura" : "<#if atto.properties["crlatti:legislatura"]?exists>${atto.properties["crlatti:legislatura"]}<#else></#if>",
	"stato" : "<#if atto.properties["crlatti:statoAtto"]?exists>${atto.properties["crlatti:statoAtto"]}<#else></#if>",
	"dataIniziativa" : "<#if atto.properties["crlatti:dataIniziativa"]?exists>${atto.properties["crlatti:dataIniziativa"]?string("yyyy-MM-dd")}<#else></#if>",
	"numeroProtocollo" : "<#if atto.properties["crlatti:numeroProtocollo"]?exists>${atto.properties["crlatti:numeroProtocollo"]}<#else></#if>",
	"tipoIniziativa" : "<#if atto.properties["crlatti:tipoIniziativa"]?exists>${atto.properties["crlatti:tipoIniziativa"]}<#else></#if>",
	"oggetto" : "<#if atto.properties["crlatti:oggetto"]?exists>${atto.properties["crlatti:oggetto"]}<#else></#if>",
	"oggettoOriginale" : "<#if atto.properties["crlatti:oggettoOriginale"]?exists>${atto.properties["crlatti:oggettoOriginale"]}<#else></#if>",
	"dataPubblicazione" : "<#if atto.properties["crlatti:dataPubblicazione"]?exists>${atto.properties["crlatti:dataPubblicazione"]?string("yyyy-MM-dd")}<#else></#if>",
	"dataSedutaSc" : "<#if atto.properties["crlatti:dataSedutaSc"]?exists>${atto.properties["crlatti:dataSedutaSc"]?string("yyyy-MM-dd")}<#else></#if>",
	"redigente" : "<#if atto.properties["crlatti:redigente"]?exists>${atto.properties["crlatti:redigente"]?string("true","false")}<#else></#if>",
	"deliberante" : "<#if atto.properties["crlatti:deliberante"]?exists>${atto.properties["crlatti:deliberante"]?string("true","false")}<#else></#if>",
	"soggettoConsultato" : "<#if atto.properties["crlatti:soggettoConsultato"]?exists>${atto.properties["crlatti:soggettoConsultato"]}<#else></#if>",
	"anno" : "<#if atto.properties["crlatti:anno"]?exists>${atto.properties["crlatti:anno"]?c}<#else></#if>",
	"rinviato" : "<#if atto.properties["crlatti:rinviato"]?exists>${atto.properties["crlatti:rinviato"]?string("true","false")}<#else></#if>",
	"sospeso" : "<#if atto.properties["crlatti:sospeso"]?exists>${atto.properties["crlatti:sospeso"]?string("true","false")}<#else></#if>",
	"abbinamento" : "<#if atto.properties["crlatti:abbinamento"]?exists>${atto.properties["crlatti:abbinamento"]?string("true","false")}<#else></#if>",
	"stralcio" : "<#if atto.properties["crlatti:stralcio"]?exists>${atto.properties["crlatti:stralcio"]?string("true","false")}<#else></#if>",
	"dataPresaInCarico" : "<#if atto.properties["crlatti:dataPresaInCarico"]?exists>${atto.properties["crlatti:dataPresaInCarico"]?string("yyyy-MM-dd")}<#else></#if>",
	"primoFirmatario" : "<#if atto.properties["crlatti:primoFirmatario"]?exists>${atto.properties["crlatti:primoFirmatario"]}<#else></#if>",
	"classificazione":"<#if atto.properties["crlatti:classificazione"]?exists>${atto.properties["crlatti:classificazione"]}<#else></#if>",
	"numeroRepertorio":"<#if atto.properties["crlatti:numeroRepertorio"]?exists>${atto.properties["crlatti:numeroRepertorio"]}<#else></#if>",
	"dataRepertorio":"<#if atto.properties["crlatti:dataRepertorio"]?exists>${atto.properties["crlatti:dataRepertorio"]?string("yyyy-MM-dd")}<#else></#if>",
	"urlFascicolo":"<#if atto.properties["crlatti:urlFascicolo"]?exists>${atto.properties["crlatti:urlFascicolo"]}<#else></#if>",
	"descrizioneIniziativa":"<#if atto.properties["crlatti:descrizioneIniziativa"]?exists>${atto.properties["crlatti:descrizioneIniziativa"]}<#else></#if>",
	"assegnazione":"<#if atto.properties["crlatti:assegnazione"]?exists>${atto.properties["crlatti:assegnazione"]}<#else></#if>",
	"numeroDgr":"<#if atto.properties["crlatti:numeroDgr"]?exists>${atto.properties["crlatti:numeroDgr"]}<#else></#if>",
	"dataDgr":"<#if atto.properties["crlatti:dataDgr"]?exists>${atto.properties["crlatti:dataDgr"]?string("yyyy-MM-dd")}<#else></#if>",
	"numeroDcr":"<#if atto.properties["crlatti:numeroDcr"]?exists>${atto.properties["crlatti:numeroDcr"]}<#else></#if>",
	"tipoChiusura" : "<#if atto.properties["crlatti:tipoChiusura"]?exists>${atto.properties["crlatti:tipoChiusura"]}<#else></#if>",
	"dataChiusura":"<#if atto.properties["crlatti:dataChiusura"]?exists>${atto.properties["crlatti:dataChiusura"]?string("yyyy-MM-dd")}<#else></#if>",
	"noteChiusuraIter" : "<#if atto.properties["crlatti:noteChiusura"]?exists>${atto.properties["crlatti:noteChiusura"]}<#else></#if>",
	"numeroLr" : "<#if atto.properties["crlatti:numeroLr"]?exists>${atto.properties["crlatti:numeroLr"]}<#else></#if>",
	"dataLR":"<#if atto.properties["crlatti:dataLr"]?exists>${atto.properties["crlatti:dataLr"]?string("yyyy-MM-dd")}<#else></#if>",	
	"numeroPubblicazioneBURL" : "<#if atto.properties["crlatti:numeroPubblicazioneBURL"]?exists>${atto.properties["crlatti:numeroPubblicazioneBURL"]}<#else></#if>",
	"dataPubblicazioneBURL":"<#if atto.properties["crlatti:dataPubblicazioneBURL"]?exists>${atto.properties["crlatti:dataPubblicazioneBURL"]?string("yyyy-MM-dd")}<#else></#if>",
	"numeroDgrSeguito":"<#if atto.properties["crlatti:numeroDgrSeguito"]?exists>${atto.properties["crlatti:numeroDgrSeguito"]}<#else></#if>",	
	"dataDgrSeguito":"<#if atto.properties["crlatti:dataDgrSeguito"]?exists>${atto.properties["crlatti:dataDgrSeguito"]?string("yyyy-MM-dd")}<#else></#if>",		
	"valutazioneAmmissibilita":"<#if atto.properties["crlatti:ammissibilitaValutazione"]?exists>${atto.properties["crlatti:ammissibilitaValutazione"]}<#else></#if>",
	"dataRichiestaInformazioni":"<#if atto.properties["crlatti:ammissibilitaDataRichiestaInformazioni"]?exists>${atto.properties["crlatti:ammissibilitaDataRichiestaInformazioni"]?string("yyyy-MM-dd")}<#else></#if>",
	"dataRicevimentoInformazioni":"<#if atto.properties["crlatti:ammissibilitaDataRicevimentoInformazioni"]?exists>${atto.properties["crlatti:ammissibilitaDataRicevimentoInformazioni"]?string("yyyy-MM-dd")}<#else></#if>",
	"aiutiStato" : "<#if atto.properties["crlatti:ammissibilitaAiutiStato"]?exists>${atto.properties["crlatti:ammissibilitaAiutiStato"]?string("true","false")}<#else></#if>",
	"normaFinanziaria" : "<#if atto.properties["crlatti:ammissibilitaNormaFinanziaria"]?exists>${atto.properties["crlatti:ammissibilitaNormaFinanziaria"]?string("true","false")}<#else></#if>",
	"richiestaUrgenza" : "<#if atto.properties["crlatti:ammissibilitaRichiestaUrgenza"]?exists>${atto.properties["crlatti:ammissibilitaRichiestaUrgenza"]?string("true","false")}<#else></#if>",
	"votazioneUrgenza" : "<#if atto.properties["crlatti:ammissibilitaVotazioneUrgenza"]?exists>${atto.properties["crlatti:ammissibilitaVotazioneUrgenza"]?string("true","false")}<#else></#if>",
	"dataVotazioneUrgenza":"<#if atto.properties["crlatti:ammissibilitaDataVotazioneUrgenza"]?exists>${atto.properties["crlatti:ammissibilitaDataVotazioneUrgenza"]?string("yyyy-MM-dd")}<#else></#if>",
	"noteAmmissibilita":"<#if atto.properties["crlatti:ammissibilitaNote"]?exists>${atto.properties["crlatti:ammissibilitaNote"]}<#else></#if>",
    "scadenza60gg":"<#if atto.properties["crlatti:scadenza60gg"]?exists>${atto.properties["crlatti:scadenza60gg"]?string("true","false")}<#else></#if>",
    "iterAula":"<#if atto.properties["crlatti:iterAula"]?exists>${atto.properties["crlatti:iterAula"]?string("true","false")}<#else></#if>",
    "attoProseguente" : "<#if atto.properties["crlatti:attoProseguente"]?exists>${atto.properties["crlatti:attoProseguente"]?string}<#else></#if>",
    "numRegolamento" : "<#if atto.properties["crlatti:numRegolamento"]?exists>${atto.properties["crlatti:numRegolamento"]?string}<#else></#if>",
    "dataRegolamento":"<#if atto.properties["crlatti:dataRegolamento"]?exists>${atto.properties["crlatti:dataRegolamento"]?string("yyyy-MM-dd")}<#else></#if>",
    "noteCollegamenti":"<#if atto.properties["crlatti:noteCollegamenti"]?exists>${atto.properties["crlatti:noteCollegamenti"]}<#else></#if>",
    
    <#if tipoAtto == "ORG">
		"relatori" : [
			<#if relatoriAtto?exists>
			<#list relatoriAtto as relatore>
			{	 
					"relatore" : 
					{ 
						"descrizione" : "${relatore.name}",<#assign relatorename=relatore.name?trim?replace('[ \t]+', ' ' , 'r')><#assign nomeRelatore = relatorename?replace('([ \t][A-Z0-9_\\p{Punct}]{2,})+(([ \t][a-z]{2,})?([ \t][A-Z0-9_\\p{Punct}]{2,})+)*', '', 'r')><#assign cognomeRelatore = relatorename?replace('^[A-Z][a-z]{1,}([ \t][A-Z][a-z]{1,})*[ \t]', '', 'r')><#assign relatoriArray = companyhome.childrenByXPath['*[@cm:name="CRL"]/*[@cm:name="Gestione Atti"]/*[@cm:name="Anagrafica"]/*/*[@crlatti:nomeConsigliereAnagrafica="'+nomeRelatore+'" and @crlatti:cognomeConsigliereAnagrafica="'+cognomeRelatore+'"]']>
						"dataNomina": "<#if relatore.properties["crlatti:dataNominaRelatore"]?exists>${relatore.properties["crlatti:dataNominaRelatore"]?string("yyyy-MM-dd")}<#else></#if>",
						"dataUscita": "<#if relatore.properties["crlatti:dataUscitaRelatore"]?exists>${relatore.properties["crlatti:dataUscitaRelatore"]?string("yyyy-MM-dd")}<#else></#if>"<#if relatoriArray?size gt 0 >,
						"id_persona" : ${relatoriArray[0].properties["crlatti:idAnagrafica"]}</#if>
					}
			}	
			<#if relatore_has_next>,</#if>
			</#list>
			</#if>
				
		],
	<#else>
		"relatori" : [<#if atto.properties["crlatti:relatori"]?exists>
		
		<#list atto.properties["crlatti:relatori"] as relatore>
			{ 
				"relatore" : 
				{ 
					"nome" : "${relatore}"
				}
			}
			<#if relatore_has_next>,</#if>
		</#list>
		
			<#else>
		</#if>],
	</#if>
    
    
    <#if tipoAtto == "EAC">
	"note":"<#if atto.properties["crlatti:noteEac"]?exists>${atto.properties["crlatti:noteEac"]}<#else></#if>",
    "dataAtto":"<#if atto.properties["crlatti:dataAtto"]?exists>${atto.properties["crlatti:dataAtto"]?string("yyyy-MM-dd")}<#else></#if>",
	</#if>

    
    <#if tipoAtto == "MIS">
    "dataIniziativaComitato":"<#if atto.properties["crlatti:dataIniziativaComitatoMis"]?exists>${atto.properties["crlatti:dataIniziativaComitatoMis"]?string("yyyy-MM-dd")}<#else></#if>",
    "dataPropostaCommissione":"<#if atto.properties["crlatti:dataPropostaCommissioneMis"]?exists>${atto.properties["crlatti:dataPropostaCommissioneMis"]?string("yyyy-MM-dd")}<#else></#if>",
    "commissioneCompetente":"<#if atto.properties["crlatti:commissioneCompetenteMis"]?exists>${atto.properties["crlatti:commissioneCompetenteMis"]}<#else></#if>",
    "esitoVotoIntesa":"<#if atto.properties["crlatti:esitoVotoIntesaMis"]?exists>${atto.properties["crlatti:esitoVotoIntesaMis"]}<#else></#if>",
    "dataIntesa":"<#if atto.properties["crlatti:dataIntesaMis"]?exists>${atto.properties["crlatti:dataIntesaMis"]?string("yyyy-MM-dd")}<#else></#if>",
    "dataRispostaComitato":"<#if atto.properties["crlatti:dataRispostaComitatoMis"]?exists>${atto.properties["crlatti:dataRispostaComitatoMis"]?string("yyyy-MM-dd")}<#else></#if>",
    "dataApprovazioneProgetto":"<#if atto.properties["crlatti:dataApprovazioneProgettoMis"]?exists>${atto.properties["crlatti:dataApprovazioneProgettoMis"]?string("yyyy-MM-dd")}<#else></#if>",
    "dataApprovazioneUdP":"<#if atto.properties["crlatti:dataApprovazioneUdpMis"]?exists>${atto.properties["crlatti:dataApprovazioneUdpMis"]?string("yyyy-MM-dd")}<#else></#if>",
    "numeroAttoUdp":"<#if atto.properties["crlatti:numeroAttoUdpMis"]?exists>${atto.properties["crlatti:numeroAttoUdpMis"]}<#else></#if>",
    "istitutoIncaricato":"<#if atto.properties["crlatti:istitutoIncaricatoMis"]?exists>${atto.properties["crlatti:istitutoIncaricatoMis"]}<#else></#if>",
    "dataScadenzaMV":"<#if atto.properties["crlatti:scadenzaMvMis"]?exists>${atto.properties["crlatti:scadenzaMvMis"]?string("yyyy-MM-dd")}<#else></#if>",
    "dataEsameRapportoFinale":"<#if atto.properties["crlatti:dataEsameRapportoFinaleMis"]?exists>${atto.properties["crlatti:dataEsameRapportoFinaleMis"]?string("yyyy-MM-dd")}<#else></#if>",
    "dataTrasmissioneCommissioni":"<#if atto.properties["crlatti:dataTrasmissioneACommissioniMis"]?exists>${atto.properties["crlatti:dataTrasmissioneACommissioniMis"]?string("yyyy-MM-dd")}<#else></#if>",
    "note":"<#if atto.properties["crlatti:noteMis"]?exists>${atto.properties["crlatti:noteMis"]}<#else></#if>",
    "dataTrasmissioneUdP":"<#if atto.properties["crlatti:dataTrasmissioneUdpMis"]?exists>${atto.properties["crlatti:dataTrasmissioneUdpMis"]?string("yyyy-MM-dd")}<#else></#if>",
    "relatore1":"<#if atto.properties["crlatti:relatore1Mis"]?exists>${atto.properties["crlatti:relatore1Mis"]}<#else></#if>",
    "relatore2":"<#if atto.properties["crlatti:relatore2Mis"]?exists>${atto.properties["crlatti:relatore2Mis"]}<#else></#if>",
	</#if>

    "notePresentazioneAssegnazione":"<#if notePresentazioneAssegnazione?exists>${notePresentazioneAssegnazione.content}<#else></#if>",
	"linksPresentazioneAssegnazione":[
	<#if links?exists>
	<#list links as link>
		{
			"link" : 
			{
				"descrizione":"${link.name}",
				"indirizzo":"<#if link.properties["crlatti:indirizzoCollegamento"]?exists>${link.properties["crlatti:indirizzoCollegamento"]}<#else></#if>",
				"pubblico":"<#if link.properties["crlatti:pubblico"]?exists>${link.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>"
			}
		}<#if link_has_next>,</#if>
	</#list>
	</#if>
	],
	"allegati": [
	
	<#assign allegatiAttoFolder = atto.childrenByXPath["*[@cm:name='Allegati']"][0]>
	<#assign allegati = allegatiAttoFolder.getChildAssocsByType("crlatti:allegato")>
	<#list allegati as allegato>
		{
			"allegato" : 
			{
				 "id" : "${allegato.nodeRef}",
				 "nome" : "${allegato.name}",
				 "mimetype" : "${allegato.mimetype}",
				 "tipologia" : "<#if allegato.properties["crlatti:tipologia"]?exists>${allegato.properties["crlatti:tipologia"]}<#else></#if>",
				 "pubblico" : "<#if allegato.properties["crlatti:pubblico"]?exists>${allegato.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>",
				 "provenienza" : "<#if allegato.properties["crlatti:provenienza"]?exists>${allegato.properties["crlatti:provenienza"]}<#else></#if>",
				 "downloadUrl": "${allegato.downloadUrl}"
			}
		}<#if allegato_has_next>,</#if>
	</#list>
	],
	"testiAtto": [
	<#assign testiAttoFolder = atto.childrenByXPath["*[@cm:name='Testi']"][0]>
	<#assign testi = testiAttoFolder.getChildAssocsByType("crlatti:testo")>
	<#list testi as testo>
		{
			"attoRecord" : 
			{
				 "id" : "${testo.nodeRef}",
				 "nome" : "${testo.name}",
				 "mimetype" : "${testo.mimetype}",
				 "tipologia" : "<#if testo.properties["crlatti:tipologia"]?exists>${testo.properties["crlatti:tipologia"]}<#else></#if>",
				 "pubblico" : "<#if testo.properties["crlatti:pubblico"]?exists>${testo.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>",
				 "provenienza" : "<#if testo.properties["crlatti:provenienza"]?exists>${testo.properties["crlatti:provenienza"]}<#else></#if>",
				 "downloadUrl":"${testo.downloadUrl}"
			}
		}<#if testo_has_next>,</#if>
	</#list>
	],
	
	"organismiStatutari" : [
		<#if organismiStatutari?exists>
		<#list organismiStatutari as organismoStatutario>
			{
				"organismoStatutario" :
				{
					"descrizione" : "<#if organismoStatutario.properties["crlatti:organismoStatutarioParere"]?exists>${organismoStatutario.properties["crlatti:organismoStatutarioParere"]}<#else></#if>",
			    	"dataAssegnazione" : "<#if organismoStatutario.properties["crlatti:dataAssegnazioneParere"]?exists>${organismoStatutario.properties["crlatti:dataAssegnazioneParere"]?string("yyyy-MM-dd")}<#else></#if>",
			    	"dataAnnullo" : "<#if organismoStatutario.properties["crlatti:dataAnnulloParere"]?exists>${organismoStatutario.properties["crlatti:dataAnnulloParere"]?string("yyyy-MM-dd")}<#else></#if>",
			    	"obbligatorio" : "<#if organismoStatutario.properties["crlatti:obbligatorio"]?exists>${organismoStatutario.properties["crlatti:obbligatorio"]?string("true","false")}<#else></#if>",
					
					"parere": 
					{
						"parere":
						{
						"descrizione" : "<#if organismoStatutario.properties["crlatti:organismoStatutarioParere"]?exists>${organismoStatutario.properties["crlatti:organismoStatutarioParere"]}<#else></#if>",
						"dataRicezioneParere" : "<#if organismoStatutario.properties["crlatti:dataRicezioneParere"]?exists>${organismoStatutario.properties["crlatti:dataRicezioneParere"]?string("yyyy-MM-dd")}<#else></#if>",
						"esito" : "<#if organismoStatutario.properties["crlatti:esitoParere"]?exists>${organismoStatutario.properties["crlatti:esitoParere"]}<#else></#if>",
						"dataRicezioneOrgano" : "<#if organismoStatutario.properties["crlatti:dataRicezioneOrganoParere"]?exists>${organismoStatutario.properties["crlatti:dataRicezioneOrganoParere"]?string("yyyy-MM-dd")}<#else></#if>",
						"note" : "<#if organismoStatutario.properties["crlatti:noteParere"]?exists>${organismoStatutario.properties["crlatti:noteParere"]}<#else></#if>",
						"commissioneDestinataria" : "<#if organismoStatutario.properties["crlatti:commissioneDestinatariaParere"]?exists>${organismoStatutario.properties["crlatti:commissioneDestinatariaParere"]}<#else></#if>",
						"allegati": [
							<#assign allegatiParereFolder = organismoStatutario.childrenByXPath["*[@cm:name='Allegati']"][0]>
							<#assign allegatiParere = allegatiParereFolder.getChildAssocsByType("crlatti:allegato")>
							<#list allegatiParere as allegato>
							{
								"allegato" : 
								{
									 "id" : "${allegato.nodeRef}",
									 "nome" : "${allegato.name}",
									 "mimetype" : "${allegato.mimetype}",
									 "tipologia" : "<#if allegato.properties["crlatti:tipologia"]?exists>${allegato.properties["crlatti:tipologia"]}<#else></#if>",
									 "pubblico" : "<#if allegato.properties["crlatti:pubblico"]?exists>${allegato.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>",
									 "provenienza" : "<#if allegato.properties["crlatti:provenienza"]?exists>${allegato.properties["crlatti:provenienza"]}<#else></#if>",
									 "downloadUrl": "${allegato.downloadUrl}"
								}
							}<#if allegato_has_next>,</#if>
						</#list>
							]
						}
					}
			
			    }
			}
			<#if organismoStatutario_has_next>,</#if>
    	</#list>
    <#else></#if>],
    "consultazioni" : [<#if consultazioni?exists>
		<#list consultazioni as consultazione>
			{
				"consultazione" :
				{
					"descrizione" : "${consultazione.name}",
					"commissione" : "<#if consultazione.properties["crlatti:commissioneConsultazione"]?exists>${consultazione.properties["crlatti:commissioneConsultazione"]}<#else></#if>",
					"dataConsultazione" : "<#if consultazione.properties["crlatti:dataConsultazione"]?exists>${consultazione.properties["crlatti:dataConsultazione"]?string("yyyy-MM-dd")}<#else></#if>",
					"prevista" : "<#if consultazione.properties["crlatti:previstaConsultazione"]?exists>${consultazione.properties["crlatti:previstaConsultazione"]?string("true","false")}<#else></#if>",
					"discussa" : "<#if consultazione.properties["crlatti:discussaConsultazione"]?exists>${consultazione.properties["crlatti:discussaConsultazione"]?string("true","false")}<#else></#if>",
					"dataSeduta" :  "<#if consultazione.properties["crlatti:dataSedutaConsultazione"]?exists>${consultazione.properties["crlatti:dataSedutaConsultazione"]?string("yyyy-MM-dd")}<#else></#if>",
					"note" : "<#if consultazione.properties["crlatti:noteConsultazione"]?exists>${consultazione.properties["crlatti:noteConsultazione"]}<#else></#if>",
					"soggettiInvitati" : [
						<#assign soggettiConsultazioneFolder = consultazione.childrenByXPath["*[@cm:name='SoggettiInvitati']"][0]>
						<#assign soggettiConsultazione = soggettiConsultazioneFolder.getChildAssocsByType("crlatti:soggettoInvitato")>
						<#list soggettiConsultazione as soggetto>
						{
							"soggettoInvitato" : 
							{
								"descrizione" : "<#if soggetto.properties["crlatti:descrizioneSoggettoInvitato"]?exists>${soggetto.properties["crlatti:descrizioneSoggettoInvitato"]}<#else></#if>",
								"intervenuto" : "<#if soggetto.properties["crlatti:intervenutoSoggettoInvitato"]?exists>${soggetto.properties["crlatti:intervenutoSoggettoInvitato"]?string("true","false")}<#else></#if>"
							}
						}<#if soggetto_has_next>,</#if>
						</#list>
					],
					"allegati" : [
						<#assign allegatiConsultazioneFolder = consultazione.childrenByXPath["*[@cm:name='Allegati']"][0]>
						<#assign allegatiConsultazione = allegatiConsultazioneFolder.getChildAssocsByType("crlatti:allegato")>
						<#list allegatiConsultazione as allegato>
						{
							"allegato" : 
							{
								 "id" : "${allegato.nodeRef}",
								 "nome" : "${allegato.name}",
								 "mimetype" : "${allegato.mimetype}",
								 "tipologia" : "<#if allegato.properties["crlatti:tipologia"]?exists>${allegato.properties["crlatti:tipologia"]}<#else></#if>",
								 "pubblico" : "<#if allegato.properties["crlatti:pubblico"]?exists>${allegato.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>",
								 "provenienza" : "<#if allegato.properties["crlatti:provenienza"]?exists>${allegato.properties["crlatti:provenienza"]}<#else></#if>",
								 "downloadUrl": "${allegato.downloadUrl}"
							}
						}<#if allegato_has_next>,</#if>
						</#list>
					]
					
    			}
    		}

    		<#if consultazione_has_next>,</#if>
    	</#list>
    <#else></#if>],
    
    "seduteAtto" : [<#if seduteAtto?exists>
    	<#list seduteAtto as sedutaAtto>
    		{
    			"sedutaAtto": {
    				"idSeduta": "${sedutaAtto.idSeduta}",
                                "numVerbale":"<#if sedutaAtto.numVerbale?exists>${sedutaAtto.numVerbale}<#else></#if>",
                                "nomeOrgano":"<#if sedutaAtto.nomeOrgano?exists>${sedutaAtto.nomeOrgano}<#else></#if>",
    				"dataSeduta": "${sedutaAtto.dataSeduta?string("yyyy-MM-dd")}",
                                "idVerbale":"<#if sedutaAtto.idVerbale?exists>${sedutaAtto.idVerbale}<#else></#if>",
                                "nomeVerbale":"<#if sedutaAtto.nomeVerbale?exists>${sedutaAtto.nomeVerbale}<#else></#if>",
                                "mimetypeVerbale":"<#if sedutaAtto.mimetypeVerbale?exists>${sedutaAtto.mimetypeVerbale}<#else></#if>",
                                "pubblico" : "<#if sedutaAtto.pubblico?exists>${sedutaAtto.pubblico?string("true","false")}<#else></#if>",
                                "downloadUrl":"<#if sedutaAtto.downloadUrl?exists>${sedutaAtto.downloadUrl}<#else></#if>",
                                "discusso":"<#if sedutaAtto.discusso?exists>${sedutaAtto.discusso?string("true","false")}<#else></#if>",
    				"links": [ 
					<#list sedutaAtto.links as link>
						{
							"link" : 
							{
								"descrizione":"${link.name}",
								"indirizzo":"<#if link.properties["crlatti:indirizzoCollegamento"]?exists>${link.properties["crlatti:indirizzoCollegamento"]}<#else></#if>",
								"pubblico":"<#if link.properties["crlatti:pubblico"]?exists>${link.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>"
							}
						}<#if link_has_next>,</#if>
					</#list>
					]
    			}
    		}
    		<#if sedutaAtto_has_next>,</#if>
    	</#list>
    
    </#if>
    ],
    
    "passaggi" : [<#if passaggi?exists>
		<#list passaggi as passaggio>
			{
				"passaggio" :
				{
			    	"nome" : "${passaggio.name}",
			    	"commissioni" :  [
			    		<#assign commissioni = passaggio.childrenByXPath["*[@cm:name='Commissioni']"][0]>
			    		<#assign commissioniList = commissioni.getChildAssocsByType("crlatti:commissione")>
			    		<#list commissioniList as commissione>
			    		{ 
			    			"commissione" :
						  	 {
						   	"descrizione" : "${commissione.name}",
					      	"ruolo" : "<#if commissione.properties["crlatti:ruoloCommissione"]?exists>${commissione.properties["crlatti:ruoloCommissione"]}<#else></#if>",
					    	"stato" : "<#if commissione.properties["crlatti:statoCommissione"]?exists>${commissione.properties["crlatti:statoCommissione"]}<#else></#if>",
					    	"quorumEsameCommissioni" : "<#if commissione.properties["crlatti:tipoVotazioneCommissione"]?exists>${commissione.properties["crlatti:tipoVotazioneCommissione"]}<#else></#if>",
					    	"esitoVotazione" : "<#if commissione.properties["crlatti:esitoVotazioneCommissione"]?exists>${commissione.properties["crlatti:esitoVotazioneCommissione"]}<#else></#if>",
					    	"materia" : "<#if commissione.properties["crlatti:materiaCommissione"]?exists>${commissione.properties["crlatti:materiaCommissione"]}<#else></#if>",	
					    	"motivazioniContinuazioneInReferente": "<#if commissione.properties["crlatti:motivazioniContinuazioneInReferente"]?exists>${commissione.properties["crlatti:motivazioniContinuazioneInReferente"]}<#else></#if>",
					    	"esitoVotazioneIntesa": "<#if commissione.properties["crlatti:esitoVotazioneIntesaClausolaValutEsame"]?exists>${commissione.properties["crlatti:esitoVotazioneIntesaClausolaValutEsame"]}<#else></#if>",
					    	"noteClausolaValutativa": "<#if commissione.properties["crlatti:noteClausolaValutativaEsame"]?exists>${commissione.properties["crlatti:noteClausolaValutativaEsame"]}<#else></#if>",

					    	"dataProposta" : "<#if commissione.properties["crlatti:dataPropostaCommissione"]?exists>${commissione.properties["crlatti:dataPropostaCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
					    	"dataAnnullo" : "<#if commissione.properties["crlatti:dataAnnulloCommissione"]?exists>${commissione.properties["crlatti:dataAnnulloCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
					    	"dataAssegnazione" : "<#if commissione.properties["crlatti:dataAssegnazioneCommissione"]?exists>${commissione.properties["crlatti:dataAssegnazioneCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
					    	"dataPresaInCarico" : "<#if commissione.properties["crlatti:dataPresaInCaricoCommissione"]?exists>${commissione.properties["crlatti:dataPresaInCaricoCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
					  		"dataIntesa" : "<#if commissione.properties["crlatti:dataIntesaClausolaValutEsame"]?exists>${commissione.properties["crlatti:dataIntesaClausolaValutEsame"]?string("yyyy-MM-dd")}<#else></#if>",
					  		"dataPresaInCaricoProposta" : "<#if commissione.properties["crlatti:dataPresaInCaricoPropClausolaValutEsame"]?exists>${commissione.properties["crlatti:dataPresaInCaricoPropClausolaValutEsame"]?string("yyyy-MM-dd")}<#else></#if>",
					  		"dataNomina" : "<#if commissione.properties["crlatti:dataNominaCommissione"]?exists>${commissione.properties["crlatti:dataNominaCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
					  		"dataSedutaCommissione" : "<#if commissione.properties["crlatti:dataVotazioneCommissione"]?exists>${commissione.properties["crlatti:dataVotazioneCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
					  		"dataTrasmissione" : "<#if commissione.properties["crlatti:dataTrasmissioneCommissione"]?exists>${commissione.properties["crlatti:dataTrasmissioneCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
					  		"dataSedutaContinuazioneInReferente" : "<#if commissione.properties["crlatti:dataSedutaContinuazioneInReferente"]?exists>${commissione.properties["crlatti:dataSedutaContinuazioneInReferente"]?string("yyyy-MM-dd")}<#else></#if>",
							"dataRichiestaIscrizioneAula" : "<#if commissione.properties["crlatti:dataRichiestaIscrizioneAulaCommissione"]?exists>${commissione.properties["crlatti:dataRichiestaIscrizioneAulaCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
		
							"dataScadenza" : "<#if commissione.properties["crlatti:dataScadenzaCommissione"]?exists>${commissione.properties["crlatti:dataScadenzaCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
							"dataInterruzione" : "<#if commissione.properties["crlatti:dataInterruzioneCommissione"]?exists>${commissione.properties["crlatti:dataInterruzioneCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
							"dataRicezioneIntegrazioni" : "<#if commissione.properties["crlatti:dataRicezioneIntegrazioniCommissione"]?exists>${commissione.properties["crlatti:dataRicezioneIntegrazioniCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
							"sospensioneFeriale" : "<#if commissione.properties["crlatti:sospensioneFerialeCommissione"]?exists>${commissione.properties["crlatti:sospensioneFerialeCommissione"]?string("true","false")}<#else></#if>",
		
							"passaggioDirettoInAula": "<#if commissione.properties["crlatti:passaggioDirettoInAulaCommissione"]?exists>${commissione.properties["crlatti:passaggioDirettoInAulaCommissione"]?string("true","false")}<#else></#if>",
		
                                                        "dataCalendarizzazione" : "<#if commissione.properties["crlatti:dataCalendarizzazioneCommissione"]?exists>${commissione.properties["crlatti:dataCalendarizzazioneCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
                                                        "dataDcr" : "<#if commissione.properties["crlatti:dataDcrCommissione"]?exists>${commissione.properties["crlatti:dataDcrCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
                                                        "numeroDcr" : "<#if commissione.properties["crlatti:numeroDcrCommissione"]?exists>${commissione.properties["crlatti:numeroDcrCommissione"]}<#else></#if>",
                                                        "dataRis" : "<#if commissione.properties["crlatti:dataRisCommissione"]?exists>${commissione.properties["crlatti:dataRisCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
                                                        "numeroRis" : "<#if commissione.properties["crlatti:numeroRisCommissione"]?exists>${commissione.properties["crlatti:numeroRisCommissione"]}<#else></#if>",

							<#assign comitato = commissione.childrenByXPath["*[@cm:name='ComitatoRistretto']"][0]>
							"dataIstituzioneComitato" : "<#if comitato.properties["crlatti:dataIstituzioneCR"]?exists>${comitato.properties["crlatti:dataIstituzioneCR"]?string("yyyy-MM-dd")}<#else></#if>",
							"dataFineLavoriComitato" : "<#if comitato.properties["crlatti:dataFineLavoriCR"]?exists>${comitato.properties["crlatti:dataFineLavoriCR"]?string("yyyy-MM-dd")}<#else></#if>",
							"presenzaComitatoRistretto": "<#if comitato.properties["crlatti:presenzaCR"]?exists>${comitato.properties["crlatti:presenzaCR"]?string("true","false")}<#else></#if>",
							"allegati" : [
								<#assign allegatiCommissioneFolder = commissione.childrenByXPath["*[@cm:name='Allegati']"][0]>
								<#assign allegatiCommissione = allegatiCommissioneFolder.children>
								<#list allegatiCommissione as allegato>
								{
									"allegato" : 
									{
										 "id" : "${allegato.nodeRef}",
										 "nome" : "${allegato.name}",
										 "mimetype" : "${allegato.mimetype}",
										 "tipologia" : "<#if allegato.properties["crlatti:tipologia"]?exists>${allegato.properties["crlatti:tipologia"]}<#else></#if>",
										 "pubblico" : "<#if allegato.properties["crlatti:pubblico"]?exists>${allegato.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>",
										 "provenienza" : "<#if allegato.properties["crlatti:provenienza"]?exists>${allegato.properties["crlatti:provenienza"]}<#else></#if>",
										 "downloadUrl": "${allegato.downloadUrl}",
										 "dataSeduta": "<#if allegato.properties["crlatti:dataSedutaTestoCR"]?exists>${allegato.properties["crlatti:dataSedutaTestoCR"]?string("yyyy-MM-dd")}<#else></#if>"
									}
								}<#if allegato_has_next>,</#if>
									</#list>
							],
							"testiAttoVotatoEsameCommissioni" : 
							[
								<#assign testiCommissioneFolder = commissione.childrenByXPath["*[@cm:name='Testi']"][0]>
								<#assign testiCommissione = testiCommissioneFolder.getChildAssocsByType("crlatti:testo")>
								<#list testiCommissione as testo>
							
								{
									"attoRecord" : 
									{
										 "id" : "${testo.nodeRef}",
										 "nome" : "${testo.name}",
										 "mimetype" : "${testo.mimetype}",
										 "tipologia" : "<#if testo.properties["crlatti:tipologia"]?exists>${testo.properties["crlatti:tipologia"]}<#else></#if>",
										 "pubblico" : "<#if testo.properties["crlatti:pubblico"]?exists>${testo.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>",
										 "pubblicoOpendata" : "<#if testo.properties["crlatti:pubblicoOpendata"]?exists>${testo.properties["crlatti:pubblicoOpendata"]?string("true","false")}<#else>false</#if>",
										 "provenienza" : "<#if testo.properties["crlatti:provenienza"]?exists>${testo.properties["crlatti:provenienza"]}<#else></#if>",
										 "downloadUrl":"${testo.downloadUrl}"
									}
								}<#if testo_has_next>,</#if>
								</#list>
							
							],
							
					    	"relatori" : [<#if atto.properties["crlatti:relatori"]?exists>
								<#assign relatoriNode = commissione.childrenByXPath["*[@cm:name='Relatori']"]>
								<#if relatoriNode?size gt 0>
								<#assign relatori = commissione.childrenByXPath["*[@cm:name='Relatori']"][0]>
								<#assign relatoriList = relatori.getChildAssocsByType("crlatti:relatore")>
								<#list  relatoriList as relatore>
								{	 
										"relatore" : 
										{ 
											"descrizione" : "${relatore.name}", <#assign relatorename=relatore.name?trim?replace('[ \t]+', ' ' , 'r')><#assign nomeRelatore = relatorename?replace('([ \t][A-Z0-9_\\p{Punct}]{2,})+(([ \t][a-z]{2,})?([ \t][A-Z0-9_\\p{Punct}]{2,})+)*', '', 'r')><#assign cognomeRelatore = relatorename?replace('^[A-Z][a-z]{1,}([ \t][A-Z][a-z]{1,})*[ \t]', '', 'r')><#assign relatoriArray = companyhome.childrenByXPath['*[@cm:name="CRL"]/*[@cm:name="Gestione Atti"]/*[@cm:name="Anagrafica"]/*/*[@crlatti:nomeConsigliereAnagrafica="'+nomeRelatore+'" and @crlatti:cognomeConsigliereAnagrafica="'+cognomeRelatore+'"]']>
											"dataNomina": "<#if relatore.properties["crlatti:dataNominaRelatore"]?exists>${relatore.properties["crlatti:dataNominaRelatore"]?string("yyyy-MM-dd")}<#else></#if>",
											"dataUscita": "<#if relatore.properties["crlatti:dataUscitaRelatore"]?exists>${relatore.properties["crlatti:dataUscitaRelatore"]?string("yyyy-MM-dd")}<#else></#if>"<#if relatoriArray?size gt 0 >,
											"id_persona" : ${relatoriArray[0].properties["crlatti:idAnagrafica"]}</#if>
										}
								}	
									<#if relatore_has_next>,</#if>
								</#list>
								</#if>
									<#else>
								</#if>],
								
						   	"comitatoRistretto" : 
						   	{
						   		"comitatoRistretto" : {
                                                                        <#assign componenti = commissione.childrenByXPath["*[@cm:name='ComitatoRistretto']"][0]>
                                                                        "tipologia": "<#if componenti.properties["crlatti:tipologiaCR"]?exists>${componenti.properties["crlatti:tipologiaCR"]}<#else></#if>",
							   		"componenti":
							   			[							   			
							   			<#assign componentiList = componenti.getChildAssocsByType("crlatti:membroComitatoRistretto")>
							   			<#list  componentiList as componente>
										 {
											"componente" : 
											{ 
												"descrizione" : "${componente.name}",
												"dataNomina": "<#if componente.properties["crlatti:dataNominaMCR"]?exists>${componente.properties["crlatti:dataNominaMCR"]?string("yyyy-MM-dd")}<#else></#if>",
												"dataUscita": "<#if componente.properties["crlatti:dataUscitaMCR"]?exists>${componente.properties["crlatti:dataUscitaMCR"]?string("yyyy-MM-dd")}<#else></#if>",
												"coordinatore": "<#if componente.properties["crlatti:coordinatoreMCR"]?exists>${componente.properties["crlatti:coordinatoreMCR"]?string("true","false")}<#else></#if>"
											}
										}
										<#if componente_has_next>,</#if>
									</#list>
							   			]
							   		
						   		}
						   	},			
						   	 					
						   	"dataSedutaStralcio": "<#if commissione.properties["crlatti:dataSedutaStralcioCommissione"]?exists>${commissione.properties["crlatti:dataSedutaStralcioCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
   							"dataStralcio": "<#if commissione.properties["crlatti:dataStralcioCommissione"]?exists>${commissione.properties["crlatti:dataStralcioCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
  							"dataIniziativaStralcio": "<#if commissione.properties["crlatti:dataIniziativaStralcioCommissione"]?exists>${commissione.properties["crlatti:dataIniziativaStralcioCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
						    "articoli": "<#if commissione.properties["crlatti:articoliCommissione"]?exists>${commissione.properties["crlatti:articoliCommissione"]}<#else></#if>",
						    "noteStralcio": "<#if commissione.properties["crlatti:noteStralcioCommissione"]?exists>${commissione.properties["crlatti:noteStralcioCommissione"]}<#else></#if>",
						    "quorumStralcio": "<#if commissione.properties["crlatti:quorumEsameCommissione"]?exists>${commissione.properties["crlatti:quorumEsameCommissione"]}<#else></#if>",
				
						   	"numEmendPresentatiMaggiorEsameCommissioni": "<#if commissione.properties["crlatti:numEmendPresentatiMaggiorEsame"]?exists>${commissione.properties["crlatti:numEmendPresentatiMaggiorEsame"]}<#else></#if>",
   							"numEmendPresentatiMinorEsameCommissioni": "<#if commissione.properties["crlatti:numEmendPresentatiMinorEsame"]?exists>${commissione.properties["crlatti:numEmendPresentatiMinorEsame"]}<#else></#if>",
  							"numEmendPresentatiGiuntaEsameCommissioni": "<#if commissione.properties["crlatti:numEmendPresentatiGiuntaEsame"]?exists>${commissione.properties["crlatti:numEmendPresentatiGiuntaEsame"]}<#else></#if>",
						    "numEmendPresentatiMistoEsameCommissioni": "<#if commissione.properties["crlatti:numEmendPresentatiMistoEsame"]?exists>${commissione.properties["crlatti:numEmendPresentatiMistoEsame"]}<#else></#if>",
						    "numEmendApprovatiMaggiorEsameCommissioni": "<#if commissione.properties["crlatti:numEmendApprovatiMaggiorEsame"]?exists>${commissione.properties["crlatti:numEmendApprovatiMaggiorEsame"]}<#else></#if>",
						    "numEmendApprovatiMinorEsameCommissioni": "<#if commissione.properties["crlatti:numEmendApprovatiMinorEsame"]?exists>${commissione.properties["crlatti:numEmendApprovatiMinorEsame"]}<#else></#if>",
						    "numEmendApprovatiGiuntaEsameCommissioni": "<#if commissione.properties["crlatti:numEmendApprovatiGiuntaEsame"]?exists>${commissione.properties["crlatti:numEmendApprovatiGiuntaEsame"]}<#else></#if>",
						    "numEmendApprovatiMistoEsameCommissioni": "<#if commissione.properties["crlatti:numEmendApprovatiMistoEsame"]?exists>${commissione.properties["crlatti:numEmendApprovatiMistoEsame"]}<#else></#if>",
  							"nonAmmissibiliEsameCommissioni": "<#if commissione.properties["crlatti:numEmendNonAmmissibiliEsame"]?exists>${commissione.properties["crlatti:numEmendNonAmmissibiliEsame"]}<#else></#if>",
   							"decadutiEsameCommissioni": "<#if commissione.properties["crlatti:numEmendDecadutiEsame"]?exists>${commissione.properties["crlatti:numEmendDecadutiEsame"]}<#else></#if>",
   							"ritiratiEsameCommissioni": "<#if commissione.properties["crlatti:numEmendRitiratiEsame"]?exists>${commissione.properties["crlatti:numEmendRitiratiEsame"]}<#else></#if>",
   							"respintiEsameCommissioni": "<#if commissione.properties["crlatti:numEmendRespintiEsame"]?exists>${commissione.properties["crlatti:numEmendRespintiEsame"]}<#else></#if>",
							"noteEmendamentiEsameCommissioni": "<#if commissione.properties["crlatti:noteEmendamentiEsame"]?exists>${commissione.properties["crlatti:noteEmendamentiEsame"]}<#else></#if>",
							
							<#if commissione.childrenByXPath["*[@cm:name='Note Generali.txt']"][0]?exists>
							<#assign noteGenerali = commissione.childrenByXPath["*[@cm:name='Note Generali.txt']"][0]>
							
							"noteGeneraliEsameCommissione": "${noteGenerali.content}",
							<#else>
							"noteGeneraliEsameCommissione": "",
							</#if>
							"linksNoteEsameCommissione":[
							<#assign links = commissione.childrenByXPath["*[@cm:name='Links']"][0]>
			    			<#assign linksList = links.getChildAssocsByType("crlatti:link")>
			    			<#list linksList as link>
								{
									"link" : 
									{
										"descrizione":"${link.name}",
										"indirizzo":"<#if link.properties["crlatti:indirizzoCollegamento"]?exists>${link.properties["crlatti:indirizzoCollegamento"]}<#else></#if>",
										"pubblico":"<#if link.properties["crlatti:pubblico"]?exists>${link.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>"
									}
								}<#if link_has_next>,</#if>
							</#list>
							]
							
							}
						}
						<#if commissione_has_next>,</#if>
						</#list>
						
						<#assign commissioniAnnullate = passaggio.childrenByXPath["*[@cm:name='CommissioniAnnullate']"][0]>
			    		<#assign commissioniAnnullateList = commissioniAnnullate.getChildAssocsByType("crlatti:commissione")>
			    		
			    		<#if commissioniList?has_content && commissioniAnnullateList?has_content>
			    		,
			    		</#if>

			    		<#list commissioniAnnullateList as commissione>
			    		<#assign nomeCommissioneAnnullata = commissione.name?split("#")[0]> 
			    		{ 
			    			"commissione" :
						  	 {
						   	"descrizione" : "${nomeCommissioneAnnullata}",
                                                        "ruolo" : "<#if commissione.properties["crlatti:ruoloCommissione"]?exists>${commissione.properties["crlatti:ruoloCommissione"]}<#else></#if>",
                                                        "stato" : "<#if commissione.properties["crlatti:statoCommissione"]?exists>${commissione.properties["crlatti:statoCommissione"]}<#else></#if>",
                                                        "quorumEsameCommissioni" : "<#if commissione.properties["crlatti:tipoVotazioneCommissione"]?exists>${commissione.properties["crlatti:tipoVotazioneCommissione"]}<#else></#if>",
                                                        "esitoVotazione" : "<#if commissione.properties["crlatti:esitoVotazioneCommissione"]?exists>${commissione.properties["crlatti:esitoVotazioneCommissione"]}<#else></#if>",
                                                        "materia" : "<#if commissione.properties["crlatti:materiaCommissione"]?exists>${commissione.properties["crlatti:materiaCommissione"]}<#else></#if>",	
                                                        "motivazioniContinuazioneInReferente": "<#if commissione.properties["crlatti:motivazioniContinuazioneInReferente"]?exists>${commissione.properties["crlatti:motivazioniContinuazioneInReferente"]}<#else></#if>",

                                                        "esitoVotazioneIntesa": "<#if commissione.properties["crlatti:esitoVotazioneIntesaClausolaValutEsame"]?exists>${commissione.properties["crlatti:esitoVotazioneIntesaClausolaValutEsame"]}<#else></#if>",
                                                        "noteClausolaValutativa": "<#if commissione.properties["crlatti:noteClausolaValutativaEsame"]?exists>${commissione.properties["crlatti:noteClausolaValutativaEsame"]}<#else></#if>",

                                                        "dataProposta" : "<#if commissione.properties["crlatti:dataPropostaCommissione"]?exists>${commissione.properties["crlatti:dataPropostaCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
                                                        "dataAnnullo" : "<#if commissione.properties["crlatti:dataAnnulloCommissione"]?exists>${commissione.properties["crlatti:dataAnnulloCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
                                                        "dataAssegnazione" : "<#if commissione.properties["crlatti:dataAssegnazioneCommissione"]?exists>${commissione.properties["crlatti:dataAssegnazioneCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
                                                        "dataPresaInCarico" : "<#if commissione.properties["crlatti:dataPresaInCaricoCommissione"]?exists>${commissione.properties["crlatti:dataPresaInCaricoCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
                                                        "dataIntesa" : "<#if commissione.properties["crlatti:dataIntesaClausolaValutEsame"]?exists>${commissione.properties["crlatti:dataIntesaClausolaValutEsame"]?string("yyyy-MM-dd")}<#else></#if>",
                                                        "dataPresaInCaricoProposta" : "<#if commissione.properties["crlatti:dataPresaInCaricoPropClausolaValutEsame"]?exists>${commissione.properties["crlatti:dataPresaInCaricoPropClausolaValutEsame"]?string("yyyy-MM-dd")}<#else></#if>",
                                                        "dataScadenza" : "<#if commissione.properties["crlatti:dataScadenzaCommissione"]?exists>${commissione.properties["crlatti:dataScadenzaCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
                                                        "dataNomina" : "<#if commissione.properties["crlatti:dataNominaCommissione"]?exists>${commissione.properties["crlatti:dataNominaCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
                                                        "dataSedutaCommissione" : "<#if commissione.properties["crlatti:dataVotazioneCommissione"]?exists>${commissione.properties["crlatti:dataVotazioneCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
                                                        "dataTrasmissione" : "<#if commissione.properties["crlatti:dataTrasmissioneCommissione"]?exists>${commissione.properties["crlatti:dataTrasmissioneCommissione"]?string("yyyy-MM-dd")}<#else></#if>",
                                                        "dataSedutaContinuazioneInReferente" : "<#if commissione.properties["crlatti:dataSedutaContinuazioneInReferente"]?exists>${commissione.properties["crlatti:dataSedutaContinuazioneInReferente"]?string("yyyy-MM-dd")}<#else></#if>",
                                                        "dataRichiestaIscrizioneAula" : "<#if commissione.properties["crlatti:dataRichiestaIscrizioneAulaCommissione"]?exists>${commissione.properties["crlatti:dataRichiestaIscrizioneAulaCommissione"]?string("yyyy-MM-dd")}<#else></#if>",

                                                        "dataScadenza": "",
                                                        "dataInterruzione": "",
                                                        "dataRicezioneIntegrazioni": "",
                                                        "sospensioneFeriale": "",
                                                        "passaggioDirettoInAula": "false",
                                                        "dataCalendarizzazione": "",
                                                        "dataDcr": "",
                                                        "numeroDcr": "",
                                                        "dataRis": "",
                                                        "numeroRis": "",
                                                        "dataIstituzioneComitato": "",
                                                        "dataFineLavoriComitato": "",
                                                        "presenzaComitatoRistretto": "false",
                                                        "allegati": [ ],
                                                        "testiAttoVotatoEsameCommissioni": [ ],
                                                        "relatori": [ ],
                                                        "comitatoRistretto": {

                                                            "comitatoRistretto": {
                                                                "tipologia": "",
                                                                "componenti": [ ]
                                                            }

                                                        },
                                                        "dataSedutaStralcio": "",
                                                        "dataStralcio": "",
                                                        "dataIniziativaStralcio": "",
                                                        "articoli": "",
                                                        "noteStralcio": "",
                                                        "quorumStralcio": "",
                                                        "numEmendPresentatiMaggiorEsameCommissioni": "",
                                                        "numEmendPresentatiMinorEsameCommissioni": "",
                                                        "numEmendPresentatiGiuntaEsameCommissioni": "",
                                                        "numEmendPresentatiMistoEsameCommissioni": "",
                                                        "numEmendApprovatiMaggiorEsameCommissioni": "",
                                                        "numEmendApprovatiMinorEsameCommissioni": "",
                                                        "numEmendApprovatiGiuntaEsameCommissioni": "",
                                                        "numEmendApprovatiMistoEsameCommissioni": "",
                                                        "nonAmmissibiliEsameCommissioni": "",
                                                        "decadutiEsameCommissioni": "",
                                                        "ritiratiEsameCommissioni": "",
                                                        "respintiEsameCommissioni": "",
                                                        "noteEmendamentiEsameCommissioni": "",
                                                        "noteGeneraliEsameCommissione": "",
                                                        "linksNoteEsameCommissione": [ ]
							}
						}
						<#if commissione_has_next>,</#if>
						</#list>		

			    	],
			    	"abbinamenti" : [
			    		<#assign abbinamenti = passaggio.childrenByXPath["*[@cm:name='Abbinamenti']"][0]>
			    		<#assign abbinamentiList = abbinamenti.getChildAssocsByType("crlatti:abbinamento")>
			    		<#list abbinamentiList as abbinamento>
					   { "abbinamento" : 
						   {
						   	"idAtto": "${atto.nodeRef}",
						   	"idAttoAbbinato": "${abbinamento.assocs["crlatti:attoAssociatoAbbinamento"][0].nodeRef}",
							"tipoTesto" : "<#if abbinamento.properties["crlatti:tipoTestoAbbinamento"]?exists>${abbinamento.properties["crlatti:tipoTestoAbbinamento"]}<#else></#if>",
							"dataAbbinamento" : "<#if abbinamento.properties["crlatti:dataAbbinamento"]?exists>${abbinamento.properties["crlatti:dataAbbinamento"]?string("yyyy-MM-dd")}<#else></#if>",
							"dataDisabbinamento" : "<#if abbinamento.properties["crlatti:dataDisabbinamento"]?exists>${abbinamento.properties["crlatti:dataDisabbinamento"]?string("yyyy-MM-dd")}<#else></#if>",
							"note" : "<#if abbinamento.properties["crlatti:noteAbbinamento"]?exists>${abbinamento.properties["crlatti:noteAbbinamento"]}<#else></#if>",
							"numeroAttoAbbinato": "${abbinamento.assocs["crlatti:attoAssociatoAbbinamento"][0].name}",
							"tipoAttoAbbinato" : "${abbinamento.assocs["crlatti:attoAssociatoAbbinamento"][0].typeShort?substring(12)?upper_case}"
						   }
					   }<#if abbinamento_has_next>,</#if>
					   </#list>
			    	],
			    	"aula": {
			    		<#assign aula = passaggio.childrenByXPath["*[@cm:name='Aula']"][0]>
			    			"dataPresaInCaricoEsameAula": "<#if aula.properties["crlatti:dataPresaInCaricoEsameAula"]?exists>${aula.properties["crlatti:dataPresaInCaricoEsameAula"]?string("yyyy-MM-dd")}<#else></#if>",
			    			"relazioneScritta": "<#if aula.properties["crlatti:relazioneScrittaAula"]?exists>${aula.properties["crlatti:relazioneScrittaAula"]}<#else></#if>",
			    			"esitoVotoAula": "<#if aula.properties["crlatti:esitoVotoPassaggioAula"]?exists>${aula.properties["crlatti:esitoVotoPassaggioAula"]}<#else></#if>",
							"tipologiaVotazione": "<#if aula.properties["crlatti:tipologiaVotazioneAula"]?exists>${aula.properties["crlatti:tipologiaVotazioneAula"]}<#else></#if>",
							"dataSedutaAula": "<#if aula.properties["crlatti:dataSedutaPassaggioAula"]?exists>${aula.properties["crlatti:dataSedutaPassaggioAula"]?string("yyyy-MM-dd")}<#else></#if>",
							"numeroDcr": "<#if aula.properties["crlatti:numeroDcrPassaggioAula"]?exists>${aula.properties["crlatti:numeroDcrPassaggioAula"]}<#else></#if>",
						 	"numeroLcr": "<#if aula.properties["crlatti:numeroLcrPassaggioAula"]?exists>${aula.properties["crlatti:numeroLcrPassaggioAula"]}<#else></#if>",
						 	"emendato": "<#if aula.properties["crlatti:emendatoAula"]?exists>${aula.properties["crlatti:emendatoAula"]?string("true","false")}<#else></#if>",
						 	"noteVotazione": "<#if aula.properties["crlatti:noteVotazioneAula"]?exists>${aula.properties["crlatti:noteVotazioneAula"]}<#else></#if>",
						 	"numEmendPresentatiMaggiorEsameAula": "<#if aula.properties["crlatti:numEmendPresentatiMaggiorEsame"]?exists>${aula.properties["crlatti:numEmendPresentatiMaggiorEsame"]}<#else></#if>",
   							"numEmendPresentatiMinorEsameAula": "<#if aula.properties["crlatti:numEmendPresentatiMinorEsame"]?exists>${aula.properties["crlatti:numEmendPresentatiMinorEsame"]}<#else></#if>",
  							"numEmendPresentatiGiuntaEsameAula": "<#if aula.properties["crlatti:numEmendPresentatiGiuntaEsame"]?exists>${aula.properties["crlatti:numEmendPresentatiGiuntaEsame"]}<#else></#if>",
						    "numEmendPresentatiMistoEsameAula": "<#if aula.properties["crlatti:numEmendPresentatiMistoEsame"]?exists>${aula.properties["crlatti:numEmendPresentatiMistoEsame"]}<#else></#if>",
						    "numEmendApprovatiMaggiorEsameAula": "<#if aula.properties["crlatti:numEmendApprovatiMaggiorEsame"]?exists>${aula.properties["crlatti:numEmendApprovatiMaggiorEsame"]}<#else></#if>",
						    "numEmendApprovatiMinorEsameAula": "<#if aula.properties["crlatti:numEmendApprovatiMinorEsame"]?exists>${aula.properties["crlatti:numEmendApprovatiMinorEsame"]}<#else></#if>",
						    "numEmendApprovatiGiuntaEsameAula": "<#if aula.properties["crlatti:numEmendApprovatiGiuntaEsame"]?exists>${aula.properties["crlatti:numEmendApprovatiGiuntaEsame"]}<#else></#if>",
						    "numEmendApprovatiMistoEsameAula": "<#if aula.properties["crlatti:numEmendApprovatiMistoEsame"]?exists>${aula.properties["crlatti:numEmendApprovatiMistoEsame"]}<#else></#if>",
  							"nonAmmissibiliEsameAula": "<#if aula.properties["crlatti:numEmendNonAmmissibiliEsame"]?exists>${aula.properties["crlatti:numEmendNonAmmissibiliEsame"]}<#else></#if>",
   							"decadutiEsameAula": "<#if aula.properties["crlatti:numEmendDecadutiEsame"]?exists>${aula.properties["crlatti:numEmendDecadutiEsame"]}<#else></#if>",
   							"ritiratiEsameAula": "<#if aula.properties["crlatti:numEmendRitiratiEsame"]?exists>${aula.properties["crlatti:numEmendRitiratiEsame"]}<#else></#if>",
   							"respintiEsameAula": "<#if aula.properties["crlatti:numEmendRespintiEsame"]?exists>${aula.properties["crlatti:numEmendRespintiEsame"]}<#else></#if>",
							"noteEmendamentiEsameAula": "<#if aula.properties["crlatti:noteEmendamentiEsame"]?exists>${aula.properties["crlatti:noteEmendamentiEsame"]}<#else></#if>",
							"dataSedutaRinvio": "<#if aula.properties["crlatti:dataSedutaRinvioAula"]?exists>${aula.properties["crlatti:dataSedutaRinvioAula"]?string("yyyy-MM-dd")}<#else></#if>",
						 	"dataTermineMassimo": "<#if aula.properties["crlatti:dataTermineMassimoAula"]?exists>${aula.properties["crlatti:dataTermineMassimoAula"]?string("yyyy-MM-dd")}<#else></#if>",
						 	"motivazioneRinvio": "<#if aula.properties["crlatti:motivazioneRinvioAula"]?exists>${aula.properties["crlatti:motivazioneRinvioAula"]}<#else></#if>",
							"dataSedutaStralcio": "<#if aula.properties["crlatti:dataSedutaStralcioAula"]?exists>${aula.properties["crlatti:dataSedutaStralcioAula"]?string("yyyy-MM-dd")}<#else></#if>",
						 	"dataStralcio": "<#if aula.properties["crlatti:dataStralcioAula"]?exists>${aula.properties["crlatti:dataStralcioAula"]?string("yyyy-MM-dd")}<#else></#if>",
						 	"dataIniziativaStralcio": "<#if aula.properties["crlatti:dataIniziativaStralcioAula"]?exists>${aula.properties["crlatti:dataIniziativaStralcioAula"]?string("yyyy-MM-dd")}<#else></#if>",
						 	"articoli": "<#if aula.properties["crlatti:articoliAula"]?exists>${aula.properties["crlatti:articoliAula"]}<#else></#if>",
						 	"noteStralcio": "<#if aula.properties["crlatti:noteStralcioAula"]?exists>${aula.properties["crlatti:noteStralcioAula"]}<#else></#if>",
						 	"quorumEsameAula": "<#if aula.properties["crlatti:quorumEsameAula"]?exists>${aula.properties["crlatti:quorumEsameAula"]}<#else></#if>",
                                                        "numeroReg": "<#if aula.properties["crlatti:numeroRegAula"]?exists>${aula.properties["crlatti:numeroRegAula"]}<#else></#if>",
                                                        "rinvioCommBilancio": "<#if aula.properties["crlatti:rinvioCommBilancioAula"]?exists>${aula.properties["crlatti:rinvioCommBilancioAula"]?string("true","false")}<#else></#if>",
						 	<#if aula.childrenByXPath["*[@cm:name='Note Generali.txt']"][0]?exists>
							<#assign noteGenerali = aula.childrenByXPath["*[@cm:name='Note Generali.txt']"][0]>
							
							"noteGeneraliEsameAula": "${noteGenerali.content}",
							<#else>
							"noteGeneraliEsameAula": "",
							</#if>
							"linksEsameAula":[
							<#assign links = aula.childrenByXPath["*[@cm:name='Links']"][0]>
			    			<#assign linksList = links.getChildAssocsByType("crlatti:link")>
			    			<#list linksList as link>
								{
									"link" : 
									{
										"descrizione":"${link.name}",
										"indirizzo":"<#if link.properties["crlatti:indirizzoCollegamento"]?exists>${link.properties["crlatti:indirizzoCollegamento"]}<#else></#if>",
										"pubblico":"<#if link.properties["crlatti:pubblico"]?exists>${link.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>"
									}
								}<#if link_has_next>,</#if>
							</#list>
							],
							"allegatiEsameAula" : [
								<#assign allegatiAulaFolder = aula.childrenByXPath["*[@cm:name='Allegati']"][0]>
								<#assign allegatiAula = allegatiAulaFolder.getChildAssocsByType("crlatti:allegato")>
								<#list allegatiAula as allegato>
								{
									"allegato" : 
									{
										 "id" : "${allegato.nodeRef}",
										 "nome" : "${allegato.name}",
										 "mimetype" : "${allegato.mimetype}",
										 "tipologia" : "<#if allegato.properties["crlatti:tipologia"]?exists>${allegato.properties["crlatti:tipologia"]}<#else></#if>",
										 "pubblico" : "<#if allegato.properties["crlatti:pubblico"]?exists>${allegato.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>",
										 "provenienza" : "<#if allegato.properties["crlatti:provenienza"]?exists>${allegato.properties["crlatti:provenienza"]}<#else></#if>",
										 "downloadUrl": "${allegato.downloadUrl}"
									}
								}<#if allegato_has_next>,</#if>
									</#list>
							],
							"testiAttoVotatoEsameAula" : 
							[
								<#assign testiAulaFolder = aula.childrenByXPath["*[@cm:name='Testi']"][0]>
								<#assign testiAula = testiAulaFolder.getChildAssocsByType("crlatti:testo")>
								<#list testiAula as testo>
								{
									"attoRecord" : 
									{
										 "id" : "${testo.nodeRef}",
										 "nome" : "${testo.name}",
										 "mimetype" : "${testo.mimetype}",
										 "tipologia" : "<#if testo.properties["crlatti:tipologia"]?exists>${testo.properties["crlatti:tipologia"]}<#else></#if>",
										 "pubblico" : "<#if testo.properties["crlatti:pubblico"]?exists>${testo.properties["crlatti:pubblico"]?string("true","false")}<#else></#if>",
										 "provenienza" : "<#if testo.properties["crlatti:provenienza"]?exists>${testo.properties["crlatti:provenienza"]}<#else></#if>",
										 "downloadUrl":"${testo.downloadUrl}"
									}
								}<#if testo_has_next>,</#if>
								</#list>
							
							]
			    		
			    	}
			   }
			  
			}
			 <#if passaggio_has_next>,</#if>
    	</#list>
    <#else></#if>]
    
   
	
   }
}
</#escape>