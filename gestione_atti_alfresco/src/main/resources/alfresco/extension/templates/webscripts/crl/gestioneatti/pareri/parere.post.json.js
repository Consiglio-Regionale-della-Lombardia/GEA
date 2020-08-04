<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var organismoTarget = json.get("target").get("organismoStatutario");


var organismi = json.get("atto").get("organismiStatutari");

var organismo = null;
//selezione dell'organismo statutario corrente
for(var i=0; i<organismi.length(); i++) {
	var organismoTemp = organismi.get(i);
	if(""+organismoTemp.get("descrizione") == ""+organismoTarget+"") {
		organismo = organismoTemp;		
	}	
}
	
var dataAnnullo = filterParam(organismo.get("dataAnnullo"));

var descrizioneParere = filterParam(organismo.get("parere").get("descrizione"));
var dataRicezioneParere = filterParam(organismo.get("parere").get("dataRicezioneParere"));
var esito = filterParam(organismo.get("parere").get("esito"));
var dataRicezioneOrgano = filterParam(organismo.get("parere").get("dataRicezioneOrgano"));
var note = filterParam(organismo.get("parere").get("note"));
var commissioneDestinataria = filterParam(organismo.get("parere").get("commissioneDestinataria")); 



if(checkIsNotNull(id)
		&& checkIsNotNull(organismoTarget)) {
	
	var attoFolderNode = utils.getNodeFromString(id);
	
	var pareriXPathQuery = "*[@cm:name='Pareri']";
	var pareriFolderNode = attoFolderNode.childrenByXPath(pareriXPathQuery)[0];
	
	var organismoStatutarioXPathQuery = "*[@cm:name='"+organismoTarget+"']";
	var organismoStatutarioNode = pareriFolderNode.childrenByXPath(organismoStatutarioXPathQuery)[0];
	
	if(checkIsNotNull(dataAnnullo)) {
		var dataAnnulloSplitted = dataAnnullo.split("-");
		var dataAnnulloParsed = new Date(dataAnnulloSplitted[0],dataAnnulloSplitted[1]-1,dataAnnulloSplitted[2]);
		organismoStatutarioNode.properties["crlatti:dataAnnulloParere"] = dataAnnulloParsed;
	}else {
		organismoStatutarioNode.properties["crlatti:dataAnnulloParere"] = null;
	}
	
	if(checkIsNotNull(dataRicezioneParere)) {
		var dataRicezioneParereSplitted = dataRicezioneParere.split("-");
		var dataRicezioneParereParsed = new Date(dataRicezioneParereSplitted[0],dataRicezioneParereSplitted[1]-1,dataRicezioneParereSplitted[2]);	
		organismoStatutarioNode.properties["crlatti:dataRicezioneParere"] = dataRicezioneParereParsed;
	}else {
		organismoStatutarioNode.properties["crlatti:dataRicezioneParere"] = null;
	}
	
	if(checkIsNotNull(dataRicezioneOrgano)) {
		var dataRicezioneOrganoSplitted = dataRicezioneOrgano.split("-");
		var dataRicezioneOrganoParsed = new Date(dataRicezioneOrganoSplitted[0],dataRicezioneOrganoSplitted[1]-1,dataRicezioneOrganoSplitted[2]);
		organismoStatutarioNode.properties["crlatti:dataRicezioneOrganoParere"] = dataRicezioneOrganoParsed;
	}else {
		organismoStatutarioNode.properties["crlatti:dataRicezioneOrganoParere"] = null;
	}
	
	organismoStatutarioNode.properties["crlatti:noteParere"] = note;
	organismoStatutarioNode.properties["crlatti:esitoParere"] = esito;
	organismoStatutarioNode.properties["crlatti:commissioneDestinatariaParere"] = commissioneDestinataria;
	
	organismoStatutarioNode.save();

	
	
} else {
	status.code = 500;
	status.message = "id atto e organismo statutario sono obbligatori";
	status.redirect = true;
}