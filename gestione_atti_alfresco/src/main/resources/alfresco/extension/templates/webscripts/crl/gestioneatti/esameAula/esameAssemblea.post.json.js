<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var statoAtto = atto.get("stato");
var passaggio = json.get("target").get("passaggio");

// selezione del passaggio corrente
var passaggioTarget = getPassaggioTarget(json, passaggio);

var esitoVotoPassaggioAula = filterParam(passaggioTarget.get("aula").get("esitoVotoAula"));
var tipologiaVotazioneAula = filterParam(passaggioTarget.get("aula").get("tipologiaVotazione"));
var dataSedutaPassaggioAula = filterParam(passaggioTarget.get("aula").get("dataSedutaAula"));
var numeroDcrPassaggioAula = filterParam(passaggioTarget.get("aula").get("numeroDcr"));
var numeroLcrPassaggioAula = filterParam(passaggioTarget.get("aula").get("numeroLcr"));
var emendatoAula = filterParam(passaggioTarget.get("aula").get("emendato"));
var noteVotazioneAula = filterParam(passaggioTarget.get("aula").get("noteVotazione"));
var numeroReg = filterParam(passaggioTarget.get("aula").get("numeroReg"));
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
	
	aulaFolderNode.properties["crlatti:esitoVotoPassaggioAula"] = esitoVotoPassaggioAula;
	aulaFolderNode.properties["crlatti:tipologiaVotazioneAula"] = tipologiaVotazioneAula;
	aulaFolderNode.properties["crlatti:numeroDcrPassaggioAula"] = numeroDcrPassaggioAula;
	aulaFolderNode.properties["crlatti:numeroLcrPassaggioAula"] = numeroLcrPassaggioAula;
	aulaFolderNode.properties["crlatti:emendatoAula"] = emendatoAula;
	aulaFolderNode.properties["crlatti:noteVotazioneAula"] = noteVotazioneAula;
        aulaFolderNode.properties["crlatti:numeroRegAula"] = numeroReg;
        aulaFolderNode.properties["crlatti:quorumEsameAula"] = quorumEsameAula;	
	
	// presa in carico
	if(checkIsNotNull(dataSedutaPassaggioAula)) {
		var dataSedutaPassaggioAulaSplitted = dataSedutaPassaggioAula.split("-");
		var dataSedutaPassaggioAulaParsed = new Date(dataSedutaPassaggioAulaSplitted[0],dataSedutaPassaggioAulaSplitted[1]-1,dataSedutaPassaggioAulaSplitted[2]);
		aulaFolderNode.properties["crlatti:dataSedutaPassaggioAula"] = dataSedutaPassaggioAulaParsed;
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