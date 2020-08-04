<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var consultazioneTarget = json.get("target").get("consultazione");

var consultazioni = json.get("atto").get("consultazioni");

var consultazione = null;
//selezione della consultazione
for(var i=0; i<consultazioni.length(); i++) {
	var consTemp = consultazioni.get(i).get("consultazione");
	if(""+consTemp.get("descrizione") == ""+consultazioneTarget+"") {
		consultazione = consTemp;		
	}	
}
	
var dataSeduta = filterParam(consultazione.get("dataSeduta"));
var dataConsultazione =  filterParam(consultazione.get("dataConsultazione"));
var prevista =  filterParam(consultazione.get("prevista"));
var discussa =  filterParam(consultazione.get("discussa"));
var note =  filterParam(consultazione.get("note"));



if(checkIsNotNull(id)
		&& checkIsNotNull(consultazioneTarget)){
	
	var attoFolderNode = utils.getNodeFromString(id);
	
	var consultazioniXPathQuery = "*[@cm:name='Consultazioni']";
	var consultazioniFolderNode = attoFolderNode.childrenByXPath(consultazioniXPathQuery)[0];
	
	var consultazionePathQuery = "*[@cm:name=\""+consultazioneTarget+"\"]";
	var consultazioneNode = consultazioniFolderNode.childrenByXPath(consultazionePathQuery)[0];
	
	if(checkIsNotNull(dataSeduta)) {
		var dataSedutaSplitted = dataSeduta.split("-");
		var dataSedutaParsed = new Date(dataSedutaSplitted[0],dataSedutaSplitted[1]-1,dataSedutaSplitted[2]);	
		consultazioneNode.properties["crlatti:dataSedutaConsultazione"] = dataSedutaParsed;
	}else {
		consultazioneNode.properties["crlatti:dataSedutaConsultazione"] = null;
	}
	
	
	if(checkIsNotNull(dataConsultazione)) {
		var dataConsultazioneSplitted = dataConsultazione.split("-");
		var dataConsultazioneParsed = new Date(dataConsultazioneSplitted[0],dataConsultazioneSplitted[1]-1,dataConsultazioneSplitted[2]);
		consultazioneNode.properties["crlatti:dataConsultazione"] = dataConsultazioneParsed;
	}else {
		consultazioneNode.properties["crlatti:dataConsultazione"] = null;
	}
	
	consultazioneNode.properties["crlatti:previstaConsultazione"] = prevista;
	consultazioneNode.properties["crlatti:discussaConsultazione"] = discussa;
	consultazioneNode.properties["crlatti:noteConsultazione"] = note;

	consultazioneNode.save();
	
	
} else {
	status.code = 500;
	status.message = "id atto e organismo statutario sono obbligatori";
	status.redirect = true;
}