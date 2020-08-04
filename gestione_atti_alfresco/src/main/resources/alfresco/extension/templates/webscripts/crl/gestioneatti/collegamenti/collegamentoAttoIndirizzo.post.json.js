<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");

var collegamenti = atto.get("collegamentiAttiSindacato");

if(checkIsNotNull(id)){
	
	var attoFolderNode = utils.getNodeFromString(id);
	
	var collegamentiXPathQuery = "*[@cm:name='Collegamenti']";
	var collegamentiFolderNode = attoFolderNode.childrenByXPath(collegamentiXPathQuery)[0];
	
	var attoIndirizzoXPathQuery = "*[@cm:name='AttiIndirizzoSindacatoIspettivo']";
	var attoIndirizzoFolderNode = collegamentiFolderNode.childrenByXPath(attoIndirizzoXPathQuery)[0];
	
	
	var numeroCollegamenti = collegamenti.length();
	
	for (var j=0; j<numeroCollegamenti; j++){
	
		var collegamento = collegamenti.get(j).get("collegamentoAttiSindacato");
	
		var idAttoCollegato = filterParam(collegamento.get("idAtto"));
		var note = filterParam(collegamento.get("descrizione"));
			
		var attoCollegatoFolderNode = utils.getNodeFromString(idAttoCollegato);
		
		
		//verifica l'esistenza del collegamento all'interno del folder Collegamenti/AttiIndirizzoSindacatoIspettivo
		var existCollegamentoXPathQuery = "*[@cm:name='"+attoCollegatoFolderNode.name+"']";
		var collegamentoEsistenteResults = attoIndirizzoFolderNode.childrenByXPath(existCollegamentoXPathQuery);
		var collegamentoNode = null;
		
		var creaAssociazione = true;
		
		if(collegamentoEsistenteResults!=null && collegamentoEsistenteResults.length>0){
			collegamentoNode = collegamentoEsistenteResults[0];
			creaAssociazione = false;
		} else {
			collegamentoNode = attoIndirizzoFolderNode.createNode(attoCollegatoFolderNode.name,"crlatti:collegamentoAttoIndirizzo");
		}
	
		//aggiornamento dei metadati
		
		collegamentoNode.properties["crlatti:descrizioneCollegamentoAttiIndirizzo"] = note;
		collegamentoNode.save();
									
		if(creaAssociazione){
			collegamentoNode.createAssociation(attoCollegatoFolderNode,"crlatti:attoAssociatoCollegamentoAttiIndirizzo");
		}
		
		collegamentoNode.save();
	
	}
	
	
	//verifica dei collegamenti da cancellare
	var collegamentiNelRepository = attoIndirizzoFolderNode.getChildAssocsByType("crlatti:collegamentoAttoIndirizzo");
			
	//query nel repository per capire se bisogna cancellare alcuni collegamenti
	for(var z=0; z<collegamentiNelRepository.length; z++){
		var trovato = false;
		var collegamentoNelRepository = collegamentiNelRepository[z];
		
		//cerco il nome del collegamento nel repo all'interno del json
		for (var q=0; q<collegamenti.length(); q++){
			var collegamento = collegamenti.get(q).get("collegamentoAttiSindacato");
			var idAttoCollegamento = filterParam(collegamento.get("idAtto"));
			var attoCollegatoFolderNode = utils.getNodeFromString(idAttoCollegamento);
			
			if(""+attoCollegatoFolderNode.name+""==""+collegamentoNelRepository.name+""){
				trovato = true;
				break
			}
		}
		if(!trovato){
			
			collegamentoNelRepository.remove();
		}
	}
	
	// salvo il nodo atto modificando una proprietà non definita nel modello
	// al solo scopo di innescare la regola che produce il file XML per l'export
	// verso il sistema di gestione Atti Indirizzo
//	attoFolderNode.properties["crlatti:exportCollegamentiProp"] = new Date().getTime();
//	attoFolderNode.save();
	
	// modifico la property per innescare la creazione del file xml di export per Atti Indirizzo
	attoFolderNode.properties["crlatti:statoExportAttiIndirizzo"]="UPDATE";	
	attoFolderNode.save();
	
	
} else {
	status.code = 400;
	status.message = "collegamento non valorizzato correttamente: idAtto e idAttoCollegamento per ogni atto da collegare, sono obbligatori";
	status.redirect = true;
}
