<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var statoAtto = atto.get("stato");
var commissioneUtente = json.get("target").get("commissione");
var passaggio = json.get("target").get("passaggio");

//selezione della commissione e del passaggio corrente
var commissioneTarget = getCommissioneTarget(json, passaggio, commissioneUtente);


var dataSedutaStralcioCommissione = filterParam(commissioneTarget.get("dataSedutaStralcio"));
var dataStralcioCommissione = filterParam(commissioneTarget.get("dataStralcio"));
var dataIniziativaStralcioCommissione = filterParam(commissioneTarget.get("dataIniziativaStralcio"));
var articoliCommissione = filterParam(commissioneTarget.get("articoli"));
var noteStralcioCommissione = filterParam(commissioneTarget.get("noteStralcio"));
var quorumEsameCommissione = filterParam(commissioneTarget.get("quorumStralcio"));
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
	
	
			commissioneFolderNode.properties["crlatti:articoliCommissione"] = articoliCommissione;
			commissioneFolderNode.properties["crlatti:noteStralcioCommissione"] = noteStralcioCommissione;
			commissioneFolderNode.properties["crlatti:quorumEsameCommissione"] = quorumEsameCommissione;	
	
			if(checkIsNotNull(dataSedutaStralcioCommissione)) {
				var dataSedutaStralcioCommissioneSplitted = dataSedutaStralcioCommissione.split("-");
				var dataSedutaStralcioCommissioneParsed = new Date(dataSedutaStralcioCommissioneSplitted[0],dataSedutaStralcioCommissioneSplitted[1]-1,dataSedutaStralcioCommissioneSplitted[2]);
				commissioneFolderNode.properties["crlatti:dataSedutaStralcioCommissione"] = dataSedutaStralcioCommissioneParsed;
			}
	
			if(checkIsNotNull(dataStralcioCommissione)) {
				var dataStralcioCommissioneSplitted = dataStralcioCommissione.split("-");
				var dataStralcioCommissioneParsed = new Date(dataStralcioCommissioneSplitted[0],dataStralcioCommissioneSplitted[1]-1,dataStralcioCommissioneSplitted[2]);
				commissioneFolderNode.properties["crlatti:dataStralcioCommissione"] = dataStralcioCommissioneParsed;
			}
	
			if(checkIsNotNull(dataIniziativaStralcioCommissione)) {
				var dataIniziativaStralcioCommissioneSplitted = dataIniziativaStralcioCommissione.split("-");
				var dataIniziativaStralcioCommissioneParsed = new Date(dataIniziativaStralcioCommissioneSplitted[0],dataIniziativaStralcioCommissioneSplitted[1]-1,dataIniziativaStralcioCommissioneSplitted[2]);
				commissioneFolderNode.properties["crlatti:dataIniziativaStralcioCommissione"] = dataIniziativaStralcioCommissioneParsed;
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