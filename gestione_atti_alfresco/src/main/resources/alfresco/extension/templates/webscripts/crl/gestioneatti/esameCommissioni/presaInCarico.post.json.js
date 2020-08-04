<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var statoAtto = atto.get("stato");
var commissioneUtente = json.get("target").get("commissione");
var passaggio = json.get("target").get("passaggio");

// selezione della commissione e del passaggio corrente
var commissioneTarget = getCommissioneTarget(json, passaggio, commissioneUtente);

var dataPresaInCarico = filterParam(commissioneTarget.get("dataPresaInCarico"));
var dataScadenza = filterParam(commissioneTarget.get("dataScadenza"));

var sospensioneFeriale = commissioneTarget.get("sospensioneFeriale");
var dataInterruzione = filterParam(commissioneTarget.get("dataInterruzione"));
var dataRicezioneIntegrazioni = filterParam(commissioneTarget.get("dataRicezioneIntegrazioni"));


var materia = filterParam(commissioneTarget.get("materia"));
var statoCommissione = filterParam(commissioneTarget.get("stato"));
var ruoloCommissione = filterParam(commissioneTarget.get("ruolo"));




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
			
			// presa in carico
			if(checkIsNotNull(dataPresaInCarico)) {
				var dataPresaInCaricoSplitted = dataPresaInCarico.split("-");
				var dataPresaInCaricoParsed = new Date(dataPresaInCaricoSplitted[0],dataPresaInCaricoSplitted[1]-1,dataPresaInCaricoSplitted[2]);
				commissioneFolderNode.properties["crlatti:dataPresaInCaricoCommissione"] = dataPresaInCaricoParsed;
			}
	
			if(checkIsNotNull(dataScadenza)) {
				var dataScadenzaSplitted = dataScadenza.split("-");
				var dataScadenzaParsed = new Date(dataScadenzaSplitted[0],dataScadenzaSplitted[1]-1,dataScadenzaSplitted[2]);
				commissioneFolderNode.properties["crlatti:dataScadenzaCommissione"] = dataScadenzaParsed;
			}else{
				commissioneFolderNode.properties["crlatti:dataScadenzaCommissione"] = null;
			}
		
			
			if(checkIsNotNull(dataInterruzione)) {
				var dataInterruzioneSplitted = dataInterruzione.split("-");
				var dataInterruzioneParsed = new Date(dataInterruzioneSplitted[0],dataInterruzioneSplitted[1]-1,dataInterruzioneSplitted[2]);
				commissioneFolderNode.properties["crlatti:dataInterruzioneCommissione"] = dataInterruzioneParsed;
			}
			
		
			if(checkIsNotNull(dataRicezioneIntegrazioni)) {
				var dataRicezioneIntegrazioniSplitted = dataRicezioneIntegrazioni.split("-");
				var dataRicezioneIntegrazioniParsed = new Date(dataRicezioneIntegrazioniSplitted[0],dataRicezioneIntegrazioniSplitted[1]-1,dataRicezioneIntegrazioniSplitted[2]);
				commissioneFolderNode.properties["crlatti:dataRicezioneIntegrazioniCommissione"] = dataRicezioneIntegrazioniParsed;
			}
			
			commissioneFolderNode.properties["crlatti:materiaCommissione"] = materia;
			commissioneFolderNode.properties["crlatti:sospensioneFerialeCommissione"] = sospensioneFeriale;
			
			
			
			// passaggio di stato per la commissione: presa in carico
			commissioneFolderNode.properties["crlatti:statoCommissione"] = statoCommissione;
			commissioneFolderNode.save();
			
			// passaggio di stato per l'atto in caso di commissione Referente
			if(canChangeStatoAtto(ruoloCommissione)) {
				attoNode.properties["crlatti:statoAtto"] = statoAtto;
				attoNode.save();
			}
			
		} else {
			status.code = 400;
			status.message = "commissione utente non valorizzata";
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