<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");

var consultazioneTarget = json.get("target").get("consultazione");

var consultazioni = atto.get("consultazioni");

var consultazione = null;
//selezione della consultazione
for(var i=0; i<consultazioni.length(); i++) {
	var consTemp = consultazioni.get(i).get("consultazione");
	if(""+consTemp.get("descrizione") == ""+consultazioneTarget+"") {
		consultazione = consTemp;		
	}	
}

var soggettiInvitati = consultazione.get("soggettiInvitati");


if(checkIsNotNull(id)
		&& checkIsNotNull(consultazioneTarget)){
	
	var attoFolderNode = utils.getNodeFromString(id);
	
	var consultazioniXPathQuery = "*[@cm:name='Consultazioni']";
	var consultazioniFolderNode = attoFolderNode.childrenByXPath(consultazioniXPathQuery)[0];
	
	var consultazionePathQuery = "*[@cm:name=\""+consultazioneTarget+"\"]";
	var consultazioneNode = consultazioniFolderNode.childrenByXPath(consultazionePathQuery)[0];
	
	var soggettiInvitatiPathQuery = "*[@cm:name='SoggettiInvitati']";
	var soggettiInvitatiFolderNode = consultazioneNode.childrenByXPath(soggettiInvitatiPathQuery)[0];
	
	var numeroSoggettiInvitati = soggettiInvitati.length();
	

	
	for (var j=0; j<numeroSoggettiInvitati; j++){
	
		var soggettoInvitato = soggettiInvitati.get(j).get("soggettoInvitato");
	
		var descrizione = filterParam(soggettoInvitato.get("descrizione")).trim();
		var intervenuto = filterParam(soggettoInvitato.get("intervenuto"));
			
		//verifica l'esistenza della consultazione all'interno del folder Consultazioni
		var existSogettoXPathQuery = "*[@cm:name=\""+descrizione+"\"]";
		var soggettoNodeEsistenteResults = soggettiInvitatiFolderNode.childrenByXPath(existSogettoXPathQuery);
		var soggettoNode = null;
		if(soggettoNodeEsistenteResults!=null && soggettoNodeEsistenteResults.length>0){
			soggettoNode = soggettoNodeEsistenteResults[0];
		} else {
			soggettoNode = soggettiInvitatiFolderNode.createNode(descrizione,"crlatti:soggettoInvitato");
		}
	
		soggettoNode.properties["crlatti:descrizioneSoggettoInvitato"] = descrizione;
		soggettoNode.properties["crlatti:intervenutoSoggettoInvitato"] = intervenuto;
		soggettoNode.save();
		
	}
	
	
	//verifica soggetti da cancellare
	var soggettiNelRepository = soggettiInvitatiFolderNode.getChildAssocsByType("crlatti:soggettoInvitato");
	
		
	//query nel repository per capire se bisogna cancellare alcuni soggetti
	for(var z=0; z<soggettiNelRepository.length; z++){
		var trovato = false;
		var soggettoNelRepository = soggettiNelRepository[z];
		
		//cerco il nome del soggetto nel repo all'interno del json
		for (var q=0; q<soggettiInvitati.length(); q++){
			var soggetto = soggettiInvitati.get(q).get("soggettoInvitato");
			var descrizione = filterParam(soggetto.get("descrizione")).trim();
			if(""+descrizione+""==""+soggettoNelRepository.name+""){
				trovato = true;
				break
			}
		}
		if(!trovato){
			soggettoNelRepository.remove();
		}
	}
	
	
} else {
	status.code = 400;
	status.message = "id Atto e consultazione sono obbligatori";
	status.redirect = true;
}