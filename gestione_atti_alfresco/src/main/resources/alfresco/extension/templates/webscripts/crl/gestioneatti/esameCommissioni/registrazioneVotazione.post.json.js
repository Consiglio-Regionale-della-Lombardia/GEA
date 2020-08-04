<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var statoAtto = atto.get("stato");
var commissioneUtente = json.get("target").get("commissione");
var passaggio = json.get("target").get("passaggio");

// selezione della commissione e del passaggio corrente
var commissioneTarget = getCommissioneTarget(json, passaggio, commissioneUtente);

var tipoVotazione = filterParam(commissioneTarget.get("quorumEsameCommissioni"));
var esitoVotazione = filterParam(commissioneTarget.get("esitoVotazione"));
var dataVotazione = filterParam(commissioneTarget.get("dataSedutaCommissione"));
var statoCommissione = filterParam(commissioneTarget.get("stato"));
var ruoloCommissione = filterParam(commissioneTarget.get("ruolo"));

var dataCalendarizzazione = filterParam(commissioneTarget.get("dataCalendarizzazione"));
var dataDcr = filterParam(commissioneTarget.get("dataDcr"));
var numeroDcr = filterParam(commissioneTarget.get("numeroDcr"));
var dataRis = filterParam(commissioneTarget.get("dataRis"));
var numeroRis = filterParam(commissioneTarget.get("numeroRis"));

if(checkIsNotNull(id)){
	var attoNode = utils.getNodeFromString(id);

	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='"+passaggio+"']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
	
	var commissioneFolderNode = null;
	
	//cerco la commissione di riferimento dell'utente corrente
	var commissioniXPathQuery = "*[@cm:name='Commissioni']";
	var commissioniFolderNode = passaggioFolderNode.childrenByXPath(commissioniXPathQuery)[0];
	
	if(checkIsNotNull(commissioneUtente)){
		var commissioneUtenteXPathQuery = "*[@cm:name=\""+commissioneUtente+"\"]";
		var commissioneUtenteResults = commissioniFolderNode.childrenByXPath(commissioneUtenteXPathQuery);
		if(commissioneUtenteResults!=null && commissioneUtenteResults.length>0){
			commissioneFolderNode = commissioneUtenteResults[0];
			
			commissioneFolderNode.properties["crlatti:tipoVotazioneCommissione"] = tipoVotazione;
			commissioneFolderNode.properties["crlatti:esitoVotazioneCommissione"] = esitoVotazione;                        
                        commissioneFolderNode.properties["crlatti:numeroDcrCommissione"] = numeroDcr;
			commissioneFolderNode.properties["crlatti:numeroRisCommissione"] = numeroRis;
                        
			// data votazione
			if(checkIsNotNull(dataVotazione)) {
				var dataVotazioneSplitted = dataVotazione.split("-");
				var dataVotazioneParsed = new Date(dataVotazioneSplitted[0],dataVotazioneSplitted[1]-1,dataVotazioneSplitted[2]);
				commissioneFolderNode.properties["crlatti:dataVotazioneCommissione"] = dataVotazioneParsed;
			}else{
				commissioneFolderNode.properties["crlatti:dataVotazioneCommissione"] = null;
			}
                        
                        // data calendarizzazione
			if(checkIsNotNull(dataCalendarizzazione)) {
				var dataCalendarizzazioneSplitted = dataCalendarizzazione.split("-");
				var dataCalendarizzazioneParsed = new Date(dataCalendarizzazioneSplitted[0],dataCalendarizzazioneSplitted[1]-1,dataCalendarizzazioneSplitted[2]);
				commissioneFolderNode.properties["crlatti:dataCalendarizzazioneCommissione"] = dataCalendarizzazioneParsed;
			}else{
				commissioneFolderNode.properties["crlatti:dataCalendarizzazioneCommissione"] = null;
			}
			
			// data Dcr
			if(checkIsNotNull(dataDcr)) {
				var dataDcrSplitted = dataDcr.split("-");
				var dataDcrParsed = new Date(dataDcrSplitted[0],dataDcrSplitted[1]-1,dataDcrSplitted[2]);
				commissioneFolderNode.properties["crlatti:dataDcrCommissione"] = dataDcrParsed;
			}else{
				commissioneFolderNode.properties["crlatti:dataDcrCommissione"] = null;
			}
                        
                        // data Ris
			if(checkIsNotNull(dataRis)) {
				var dataRisSplitted = dataRis.split("-");
				var dataRisParsed = new Date(dataRisSplitted[0],dataRisSplitted[1]-1,dataRisSplitted[2]);
				commissioneFolderNode.properties["crlatti:dataRisCommissione"] = dataRisParsed;
			}else{
				commissioneFolderNode.properties["crlatti:dataRisCommissione"] = null;
			}

			// passaggio di stato per la commissione: votato in Commissione
			commissioneFolderNode.properties["crlatti:statoCommissione"] = statoCommissione;
			commissioneFolderNode.save();
			
			// passaggio di stato per l'atto in caso di commissione Referente
			if(canChangeStatoAtto(ruoloCommissione)) {
				attoNode.properties["crlatti:statoAtto"] = statoAtto;
				attoNode.save();
			}
			
		} else {
			status.code = 400;
			status.message = "commissione utente non trovata";
			status.redirect = true;
		}
	} 
	
	
	model.atto = attoNode;
	model.commissione = commissioneFolderNode;
	
} else {
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
}