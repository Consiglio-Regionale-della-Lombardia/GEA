<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var noteCollegamenti = atto.get("noteCollegamenti");

var collegamenti = atto.get("collegamenti");

if(checkIsNotNull(id)){
	
	var attoFolderNode = utils.getNodeFromString(id);
        
        attoFolderNode.properties["crlatti:noteCollegamenti"] = noteCollegamenti;
        attoFolderNode.save();
	
	var collegamentiXPathQuery = "*[@cm:name='Collegamenti']";
	var collegamentiFolderNode = attoFolderNode.childrenByXPath(collegamentiXPathQuery)[0];
	
	var interniXPathQuery = "*[@cm:name='Interni']";
	var interniFolderNode = collegamentiFolderNode.childrenByXPath(interniXPathQuery)[0];
	
	
	var numeroCollegamenti = collegamenti.length();
	
	for (var j=0; j<numeroCollegamenti; j++){
	
		var collegamento = collegamenti.get(j).get("collegamento");
	
		var idAttoCollegato = filterParam(collegamento.get("idAttoCollegato"));
		var note = filterParam(collegamento.get("note"));
			
		var attoCollegatoFolderNode = utils.getNodeFromString(idAttoCollegato);
		
		
		//verifica l'esistenza del collegamento all'interno del folder Collegamenti/Interni
		var existCollegamentoXPathQuery = "*[@cm:name='"+attoCollegatoFolderNode.typeShort.substring(12) + " " + attoCollegatoFolderNode.name+"']";
		var collegamentoEsistenteResults = interniFolderNode.childrenByXPath(existCollegamentoXPathQuery);
		var collegamentoNode = null;
		
		var creaAssociazione = true;
		
		if(collegamentoEsistenteResults!=null && collegamentoEsistenteResults.length>0){
			collegamentoNode = collegamentoEsistenteResults[0];
			creaAssociazione = false;
		} else {
			var nomeCollegamento = attoCollegatoFolderNode.typeShort.substring(12) + " " + attoCollegatoFolderNode.name;
			collegamentoNode = interniFolderNode.createNode(nomeCollegamento,"crlatti:collegamento");
			collegamentoNode.content = nomeCollegamento;
		}
	
		//aggiornamento dei metadati
		
		collegamentoNode.properties["crlatti:noteCollegamento"] = note;


		if(creaAssociazione){
			collegamentoNode.createAssociation(attoCollegatoFolderNode,"crlatti:attoAssociatoCollegamento");
		}
		
		collegamentoNode.save();
		
		
		
		
		// Creo collegamento nell'atto collegato. Il collegamento è bidirezionale
		
		var collegamentiAttoCollegatoXPathQuery = "*[@cm:name='Collegamenti']";
		var collegamentiAttoCollegatoFolderNode = attoCollegatoFolderNode.childrenByXPath(collegamentiAttoCollegatoXPathQuery)[0];
		
		var interniAttoCollegatoXPathQuery = "*[@cm:name='Interni']";
		var interniAttoCollegatoFolderNode = collegamentiAttoCollegatoFolderNode.childrenByXPath(interniAttoCollegatoXPathQuery)[0];
		
		
		//verifica esistenza collegamento preesistente
		var esisteCollegamentoAttoCollegatoXPathQuery = "*[@cm:name='"+attoFolderNode.typeShort.substring(12) + " " + attoFolderNode.name+"']";
		var collegamentoAttoCollegatoEsistenteResults = interniAttoCollegatoFolderNode.childrenByXPath(esisteCollegamentoAttoCollegatoXPathQuery);
		
		var creaAssociazioneAttoCollegato = true;
		if(collegamentoAttoCollegatoEsistenteResults!=null && collegamentoAttoCollegatoEsistenteResults.length>0){
			collegamentoAttoCollegatoNode = collegamentoAttoCollegatoEsistenteResults[0];
			creaAssociazioneAttoCollegato = false;
		} else {
			var nomeCollegamentoAttoCollegato = attoFolderNode.typeShort.substring(12) + " " + attoFolderNode.name;
			collegamentoAttoCollegatoNode = interniAttoCollegatoFolderNode.createNode(nomeCollegamentoAttoCollegato,"crlatti:collegamento");
			collegamentoAttoCollegatoNode.content = nomeCollegamentoAttoCollegato;
		}
		
		
		collegamentoAttoCollegatoNode.properties["crlatti:noteCollegamento"] = note;
		collegamentoAttoCollegatoNode.save();
		
		if(creaAssociazioneAttoCollegato){
			collegamentoAttoCollegatoNode.createAssociation(attoFolderNode,"crlatti:attoAssociatoCollegamento");
		}
		
		
	}
	
	
	
	//verifica dei collegamenti da cancellare
	var collegamentiNelRepository = interniFolderNode.getChildAssocsByType("crlatti:collegamento");
			
	//query nel repository per capire se bisogna cancellare alcuni collegamenti
	for(var z=0; z<collegamentiNelRepository.length; z++){
		var trovato = false;
		var collegamentoNelRepository = collegamentiNelRepository[z];
		
		//cerco il nome del collegamento nel repo all'interno del json
		for (var q=0; q<collegamenti.length(); q++){
			var collegamento = collegamenti.get(q).get("collegamento");
			var idAttoCollegamento = filterParam(collegamento.get("idAttoCollegato"));
			var attoCollegamento = utils.getNodeFromString(idAttoCollegamento);
			var nomeAttoCollegamento = attoCollegamento.typeShort.substring(12) + " " + attoCollegamento.name;
			
			if(""+nomeAttoCollegamento+""==""+collegamentoNelRepository.name+""){
				trovato = true;
				break
			}
		}
		if(!trovato){
			
			// cancello il riferimento anche nell'atto collegato. 
			// Anche la cancellazione del collegamento è bidirezionale
			
			var attoCollegatoNode = collegamentoNelRepository.assocs["crlatti:attoAssociatoCollegamento"][0];
			
			var collegamentiAttoCollegatoXPathQuery = "*[@cm:name='Collegamenti']";
			var collegamentiAttoCollegatoFolderNode = attoCollegatoNode.childrenByXPath(collegamentiAttoCollegatoXPathQuery)[0];
			
			var interniAttoCollegatoXPathQuery = "*[@cm:name='Interni']";
			var interniAttoCollegatoFolderNode = collegamentiAttoCollegatoFolderNode.childrenByXPath(interniAttoCollegatoXPathQuery)[0];
			
			var nomeCollegamentoAttoCollegato = attoFolderNode.typeShort.substring(12) + " " + attoFolderNode.name;
			
			var collegamentoAttoCollegatoXPathQuery = "*[@cm:name='"+nomeCollegamentoAttoCollegato+"']";
			var collegamentoAttoCollegatoResults = interniAttoCollegatoFolderNode.childrenByXPath(collegamentoAttoCollegatoXPathQuery);
			
			collegamentoAttoCollegatoResults[0].remove();
			
			
			collegamentoNelRepository.remove();
		}
	}
	
	
} else {
	status.code = 400;
	status.message = "collegamento non valorizzato correttamente: idAtto e idAttoCollegamento per ogni atto da collegare, sono obbligatori";
	status.redirect = true;
}
