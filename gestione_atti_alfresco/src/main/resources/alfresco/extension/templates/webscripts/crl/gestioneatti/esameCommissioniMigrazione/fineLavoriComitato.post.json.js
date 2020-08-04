<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/commonMigrazione.js">

var atto = json.get("atto").get("atto");
var id = atto.get("id");
var statoAtto = atto.get("stato");
var commissioneUtente = json.get("target").get("target").get("commissione");
var passaggio = json.get("target").get("target").get("passaggio");

//selezione della commissione e del passaggio corrente
var commissioneTarget = getCommissioneTarget(json, passaggio, commissioneUtente);


var dataFineLavoriComitato = filterParam(commissioneTarget.get("dataFineLavoriComitato"));
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
			
			// stato della commissione
			commissioneFolderNode.properties["crlatti:statoCommissione"] = statoCommissione;	
			commissioneFolderNode.save();
		} else {
			status.code = 400;
			status.message = "commissione utente non valorizzata";
			status.redirect = true;
		}
	} 
	
	var comitatoXPathQuery = "*[@cm:name='ComitatoRistretto']";
	var comitatoFolderNode = commissioneFolderNode.childrenByXPath(comitatoXPathQuery)[0];
	
	// setting delle proprietà del comitato ristretto
	
	var dataFineLavoriComitatoParsed = null;
	if(checkIsNotNull(dataFineLavoriComitato)){
		var dataFineLavoriComitatoSplitted = dataFineLavoriComitato.split("-");
		dataFineLavoriComitatoParsed = new Date(dataFineLavoriComitatoSplitted[0],dataFineLavoriComitatoSplitted[1]-1,dataFineLavoriComitatoSplitted[2]);
	}
		
	comitatoFolderNode.properties["crlatti:dataFineLavoriCR"] = dataFineLavoriComitatoParsed;
	comitatoFolderNode.save();
	
	
	attoNode.properties["crlatti:statoAtto"] = statoAtto;
	attoNode.save();
	
	model.atto = attoNode;
	model.commissione = commissioneFolderNode;
	
} else {
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
}