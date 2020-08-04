<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var statoAtto = atto.get("stato");
var passaggio = json.get("target").get("passaggio");

// selezione del passaggio corrente
var passaggioTarget = getPassaggioTarget(json, passaggio);

var dataSedutaStralcioAula = filterParam(passaggioTarget.get("aula").get("dataSedutaStralcio"));
var dataStralcioAula = filterParam(passaggioTarget.get("aula").get("dataStralcio"));
var dataIniziativaStralcioAula = filterParam(passaggioTarget.get("aula").get("dataIniziativaStralcio"));
var articoliAula = filterParam(passaggioTarget.get("aula").get("articoli"));
var noteStralcioAula = filterParam(passaggioTarget.get("aula").get("noteStralcio"));
var quorumEsameAula = filterParam(passaggioTarget.get("aula").get("quorumEsameAula"));



	
if(checkIsNotNull(id)
		&& checkIsNotNull(passaggio)){
	var attoNode = utils.getNodeFromString(id);
	
	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='"+passaggio+"']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
	
	var aulaXPathQuery = "*[@cm:name='Aula']";
	var aulaFolderNode = passaggioFolderNode.childrenByXPath(aulaXPathQuery)[0];
	
	
	aulaFolderNode.properties["crlatti:articoliAula"] = articoliAula;
	aulaFolderNode.properties["crlatti:noteStralcioAula"] = noteStralcioAula;
	aulaFolderNode.properties["crlatti:quorumEsameAula"] = quorumEsameAula;	
	
	if(checkIsNotNull(dataSedutaStralcioAula)) {
		var dataSedutaStralcioAulaSplitted = dataSedutaStralcioAula.split("-");
		var dataSedutaStralcioAulaParsed = new Date(dataSedutaStralcioAulaSplitted[0],dataSedutaStralcioAulaSplitted[1]-1,dataSedutaStralcioAulaSplitted[2]);
		aulaFolderNode.properties["crlatti:dataSedutaStralcioAula"] = dataSedutaStralcioAulaParsed;
	}
	
	if(checkIsNotNull(dataStralcioAula)) {
		var dataStralcioAulaSplitted = dataStralcioAula.split("-");
		var dataStralcioAulaParsed = new Date(dataStralcioAulaSplitted[0],dataStralcioAulaSplitted[1]-1,dataStralcioAulaSplitted[2]);
		aulaFolderNode.properties["crlatti:dataStralcioAula"] = dataStralcioAulaParsed;
	}
	
	if(checkIsNotNull(dataIniziativaStralcioAula)) {
		var dataIniziativaStralcioAulaSplitted = dataIniziativaStralcioAula.split("-");
		var dataIniziativaStralcioAulaParsed = new Date(dataIniziativaStralcioAulaSplitted[0],dataIniziativaStralcioAulaSplitted[1]-1,dataIniziativaStralcioAulaSplitted[2]);
		aulaFolderNode.properties["crlatti:dataIniziativaStralcioAula"] = dataIniziativaStralcioAulaParsed;
	}
			
	aulaFolderNode.save();
			
	attoNode.properties["crlatti:statoAtto"] = statoAtto;
	attoNode.save();

	model.atto = attoNode;
	model.aula = aulaFolderNode;
	
} else {
	status.code = 400;
	status.message = "id atto e passaggio non valorizzato";
	status.redirect = true;
}