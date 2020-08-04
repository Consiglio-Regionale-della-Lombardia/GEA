<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/commonMigrazione.js">

var atto = json.get("atto").get("atto");
var id = atto.get("id");
var statoAtto = atto.get("stato");
var commissioneUtente = json.get("target").get("target").get("commissione");
var passaggio = json.get("target").get("target").get("passaggio");

//selezione della commissione e del passaggio corrente
var commissioneTarget = getCommissioneTarget(json, passaggio, commissioneUtente);

var passaggioDirettoInAula = filterParam(commissioneTarget.get("passaggioDirettoInAula"));
var dataRichiestaIscrizioneAula = filterParam(commissioneTarget.get("dataRichiestaIscrizioneAula"));
var dataTrasmissione = filterParam(commissioneTarget.get("dataTrasmissione"));
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
			
			commissioneFolderNode.properties["crlatti:passaggioDirettoInAulaCommissione"] = passaggioDirettoInAula;
			
			// data trasmissione
			if(checkIsNotNull(dataTrasmissione)) {
				var dataTrasmissioneSplitted = dataTrasmissione.split("-");
				var dataTrasmissioneParsed = new Date(dataTrasmissioneSplitted[0],dataTrasmissioneSplitted[1]-1,dataTrasmissioneSplitted[2]);
				commissioneFolderNode.properties["crlatti:dataTrasmissioneCommissione"] = dataTrasmissioneParsed;
			}
			
			// data trasmissione
			if(checkIsNotNull(dataRichiestaIscrizioneAula)) {
				var dataRichiestaIscrizioneAulaSplitted = dataRichiestaIscrizioneAula.split("-");
				var dataRichiestaIscrizioneAulaParsed = new Date(dataRichiestaIscrizioneAulaSplitted[0],dataRichiestaIscrizioneAulaSplitted[1]-1,dataRichiestaIscrizioneAulaSplitted[2]);
				commissioneFolderNode.properties["crlatti:dataRichiestaIscrizioneAulaCommissione"] = dataRichiestaIscrizioneAulaParsed;
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