<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var statoAtto = atto.get("stato");
var commissioneUtente = json.get("target").get("commissione");
var passaggio = json.get("target").get("passaggio");

//selezione della commissione e del passaggio corrente
var commissioneTarget = getCommissioneTarget(json, passaggio, commissioneUtente);


var relatori = filterParam(commissioneTarget.get("relatori"));
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
	
	var relatoriXPathQuery = "*[@cm:name='Relatori']";
	var relatoriFolderNode = commissioneFolderNode.childrenByXPath(relatoriXPathQuery)[0];
	
	var numeroRelatori = relatori.length();
	//relatori
	
	for(var k=0; k<numeroRelatori; k++) {
		
		var relatore = relatori.get(k);
	
		var descrizione = filterParam(relatore.get("descrizione"));
		var dataNomina = filterParam(relatore.get("dataNomina"));
		var dataUscita = filterParam(relatore.get("dataUscita"));
		
		//verifica l'esistenza di un relatore nel repository
		var existRelatoreXPathQuery = "*[@cm:name=\""+descrizione+"\"]";
		var relatoreEsistenteResults = relatoriFolderNode.childrenByXPath(existRelatoreXPathQuery);
		var relatoreNode = null;
		if(relatoreEsistenteResults!=null && relatoreEsistenteResults.length>0){
			relatoreNode = relatoreEsistenteResults[0];
		} else {
			relatoreNode = relatoriFolderNode.createNode(descrizione,"crlatti:relatore");
			relatoreNode.content = descrizione;
		}
		
		var dataNominaParsed = null;
		if(checkIsNotNull(dataNomina)){
			var dataNominaSplitted = dataNomina.split("-");
			dataNominaParsed = new Date(dataNominaSplitted[0],dataNominaSplitted[1]-1,dataNominaSplitted[2]);
		}
		
		var dataUscitaParsed = null;
		if(checkIsNotNull(dataUscita)){
			var dataUscitaSplitted = dataUscita.split("-");
			dataUscitaParsed = new Date(dataUscitaSplitted[0],dataUscitaSplitted[1]-1,dataUscitaSplitted[2]);
		}
		
		relatoreNode.properties["crlatti:dataNominaRelatore"] = dataNominaParsed;
		relatoreNode.properties["crlatti:dataUscitaRelatore"] = dataUscitaParsed;
		

		// occorre ridondare alcuni metadati dell'atto e della commissione per permettere le ricerche lucene (report) sui relatori
		var tipoAttoRelatore = attoNode.typeShort.substring(12);
		var numeroAttoRelatore = attoNode.name;
		var commissioneRelatore = commissioneFolderNode.name;

		relatoreNode.properties["crlatti:tipoAttoRelatore"] = tipoAttoRelatore;
		relatoreNode.properties["crlatti:numeroAttoRelatore"] = numeroAttoRelatore;
		relatoreNode.properties["crlatti:commissioneRelatore"] = commissioneRelatore;
		
		relatoreNode.save();
		
	}	
	
	//verifica relatori da cancellare
	var relatoriNelRepository = relatoriFolderNode.getChildAssocsByType("crlatti:relatore");
	
		
	//query nel repository per capire se bisogna cancellare alcuni relatori
	for(var z=0; z<relatoriNelRepository.length; z++){
		var trovato = false;
		var relatoreNelRepository = relatoriNelRepository[z];
		
		//cerco il nome del relatore nel repo all'interno del json
		for (var q=0; q<relatori.length(); q++){
			var relatore = relatori.get(q);
			var descrizione = filterParam(relatore.get("descrizione"));
			if(""+descrizione+""==""+relatoreNelRepository.name+""){
				trovato = true;
				break
			}
		}
		if(!trovato){
			var relatoriSpace = relatoreNelRepository.parent;
			relatoreNelRepository.remove();
			
			
		}
	}
	
	
	/*
	 * in eliminazione devono essere gestite manualmente 
	 * a causa di un bug di Alfresco risolto nella 4.1.1 riguardo le regole in outbound:
	 * 
	 * https://issues.alfresco.com/jira/browse/ALF-12711
	 * 
	*/
	
	// modifico la proprietà relatori dell'atto riciclando tutte le commissioni e i relativi relatori
	
	var commissioniNodes = commissioniFolderNode.getChildAssocsByType("crlatti:commissione");
	var numeroCommissioni = commissioniNodes.length;
	
	var commRelatoriCommissioni = new Array();
	
	for(var i=0; i<numeroCommissioni; i++) {
		
		var commissioneTempNode = commissioniNodes[i];
		var relatoriCommissioneXPathQuery = "*[@cm:name='Relatori']";
		var relatoriCommissioneFolderNode = commissioneTempNode.childrenByXPath(relatoriCommissioneXPathQuery)[0];
		
		var relatoriNode = relatoriCommissioneFolderNode.getChildAssocsByType("crlatti:relatore");
		
		// considero i relatori di ogni commissione
		var numeroRelatori = relatoriNode.length;
		for(var j=0; j<numeroRelatori; j++) {
			commRelatoriCommissioni.push(relatoriNode[j].properties["cm:name"]);	
		}
	}

	attoNode.properties["crlatti:relatori"] = commRelatoriCommissioni;
	
	
	// passaggio di stato per l'atto in caso di commissione Referente
	if(canChangeStatoAtto(ruoloCommissione)) {
		attoNode.properties["crlatti:statoAtto"] = statoAtto;
		attoNode.save();
	}
	
	
	model.atto = attoNode;
	model.commissione = commissioneFolderNode;
	model.relatori = relatoriFolderNode.getChildAssocsByType("crlatti:relatore");
	
} else {
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
}