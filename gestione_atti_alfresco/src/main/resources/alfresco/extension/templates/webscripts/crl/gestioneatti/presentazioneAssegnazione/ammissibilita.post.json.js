<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var valutazione = filterParam(atto.get("valutazioneAmmissibilita"));
var dataRichiestaInformazioni = filterParam(atto.get("dataRichiestaInformazioni"));
var dataRicevimentoInformazioni = filterParam(atto.get("dataRicevimentoInformazioni"));
var aiutiStato = filterParam(atto.get("aiutiStato"));
var normaFinanziaria = filterParam(atto.get("normaFinanziaria"));
var richiestaUrgenza = filterParam(atto.get("richiestaUrgenza"));
var votazioneUrgenza = filterParam(atto.get("votazioneUrgenza"));
var dataVotazioneUrgenza = filterParam(atto.get("dataVotazioneUrgenza"));
var note = filterParam(atto.get("noteAmmissibilita"));
var stato = filterParam(atto.get("stato"));

if(checkIsNotNull(id)){
	var attoNode = utils.getNodeFromString(id);
	
	if(attoNode.hasAspect("crlatti:ammissibilitaBaseAspect")
			|| attoNode.hasAspect("crlatti:ammissibilitaAiutiStatoAspect")
			|| attoNode.hasAspect("crlatti:ammissibilitaUrgenzaAspect")
			|| attoNode.hasAspect("crlatti:ammissibilitaNormaFinanziariaAspect")){
		
		attoNode.properties["crlatti:ammissibilitaValutazione"] = valutazione;
		
		if(checkIsNotNull(dataRichiestaInformazioni)){
			var dataRichiestaInformazioniSplitted = dataRichiestaInformazioni.split("-");
			var dataRichiestaInformazioniParsed = new Date(dataRichiestaInformazioniSplitted[0],dataRichiestaInformazioniSplitted[1]-1,dataRichiestaInformazioniSplitted[2]);
			attoNode.properties["crlatti:ammissibilitaDataRichiestaInformazioni"] = dataRichiestaInformazioniParsed;
		}else {
			attoNode.properties["crlatti:ammissibilitaDataRichiestaInformazioni"] = null;
		}
		
		if(checkIsNotNull(dataRicevimentoInformazioni)){
			var dataRicevimentoInformazioniSplitted = dataRicevimentoInformazioni.split("-");
			var dataRicevimentoInformazioniParsed = new Date(dataRicevimentoInformazioniSplitted[0],dataRicevimentoInformazioniSplitted[1]-1,dataRicevimentoInformazioniSplitted[2]);
			attoNode.properties["crlatti:ammissibilitaDataRicevimentoInformazioni"] = dataRicevimentoInformazioniParsed;
		}else {
			attoNode.properties["crlatti:ammissibilitaDataRicevimentoInformazioni"] = null;
		}
		
		attoNode.properties["crlatti:ammissibilitaNote"] = note;
		
	}
	
	if(attoNode.hasAspect("crlatti:ammissibilitaAiutiStatoAspect")
			|| attoNode.hasAspect("crlatti:ammissibilitaUrgenzaAspect")
			|| attoNode.hasAspect("crlatti:ammissibilitaNormaFinanziariaAspect")){
		attoNode.properties["crlatti:ammissibilitaAiutiStato"] = aiutiStato;
	}
	
	if(attoNode.hasAspect("crlatti:ammissibilitaUrgenzaAspect")
			|| attoNode.hasAspect("crlatti:ammissibilitaNormaFinanziariaAspect")){
		attoNode.properties["crlatti:ammissibilitaRichiestaUrgenza"] = richiestaUrgenza;
		attoNode.properties["crlatti:ammissibilitaVotazioneUrgenza"] = votazioneUrgenza;
		
		if(checkIsNotNull(dataVotazioneUrgenza)){
			var dataVotazioneUrgenzaSplitted = dataVotazioneUrgenza.split("-");
			var dataVotazioneUrgenzaParsed = new Date(dataVotazioneUrgenzaSplitted[0],dataVotazioneUrgenzaSplitted[1]-1,dataVotazioneUrgenzaSplitted[2]);
			attoNode.properties["crlatti:ammissibilitaDataVotazioneUrgenza"] = dataVotazioneUrgenzaParsed;
		}else {
			attoNode.properties["crlatti:ammissibilitaDataVotazioneUrgenza"] = null;
		}
	}
	
	if(attoNode.hasAspect("crlatti:ammissibilitaNormaFinanziariaAspect")){
		attoNode.properties["crlatti:ammissibilitaNormaFinanziaria"] = normaFinanziaria;
	}

	attoNode.properties["crlatti:statoAtto"] = stato;
	attoNode.save();	
	model.atto = attoNode;
	
} else {
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
}