<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">


var atto = json.get("atto");
var id = atto.get("id");
var commissioneUtente = json.get("target").get("commissione");
var passaggio = json.get("target").get("passaggio");

//selezione della commissione e del passaggio corrente
var commissioneTarget = getCommissioneTarget(json, passaggio, commissioneUtente);


var note = filterParam(commissioneTarget.get("noteGeneraliEsameCommissione"));
var links = filterParam(commissioneTarget.get("linksNoteEsameCommissione"));


if(checkIsNotNull(id)
		&& checkIsNotNull(commissioneUtente)){
	
	var attoNode = utils.getNodeFromString(id);
	
	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='"+passaggio+"']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
	
	// cerco la cartella commissioni dell'atto
	var commissioniXPathQuery = "*[@cm:name='Commissioni']";
	var commissioniFolderNode = passaggioFolderNode.childrenByXPath(commissioniXPathQuery)[0];
	
	var commissioneXPathQuery = "*[@cm:name=\""+commissioneUtente+"\"]";
	var commissioneFolder = commissioniFolderNode.childrenByXPath(commissioneXPathQuery)[0];
	
	
	//note generali
	var noteGeneraliFilename = "Note Generali.txt";
	var noteGeneraliXPathQuery = "*[@cm:name='"+noteGeneraliFilename+"']";

	var noteGeneraliResults = commissioneFolder.childrenByXPath(noteGeneraliXPathQuery);
	
	var noteGeneraliNode = null;
	if(noteGeneraliResults!=null && noteGeneraliResults.length>0){
		noteGeneraliNode = noteGeneraliResults[0];	
	} else {
		noteGeneraliNode = commissioneFolder.createFile(noteGeneraliFilename);
		noteGeneraliNode.properties.content.setEncoding("UTF-8");
	}
	
	noteGeneraliNode.content = note;
	noteGeneraliNode.save();

	//links
	var linksFolderXPathQuery = "*[@cm:name='Links']";
	var linksFolderNode = commissioneFolder.childrenByXPath(linksFolderXPathQuery)[0];
	
	for (var i=0; i<links.length(); i++){
		var link = links.get(i);
		var descrizione = link.get("descrizione");
		var indirizzo = link.get("indirizzo");
		var pubblico = link.get("pubblico");
		
		var existLinkXPathQuery = "*[@cm:name='"+descrizione+"']";
		var existLinkResults = linksFolderNode.childrenByXPath(existLinkXPathQuery);
		
		var linkNode = null;
		if(existLinkResults!=null && existLinkResults.length>0){
			linkNode = existLinkResults[0];
		} else {
			linkNode = linksFolderNode.createNode(descrizione,"crlatti:link");
		}
		
		linkNode.properties["crlatti:indirizzoCollegamento"] = indirizzo;
		linkNode.properties["crlatti:pubblico"] = pubblico;
		linkNode.save();
	}
	
	
	
	//verifica link da cancellare
	var linksNelRepository = linksFolderNode.getChildAssocsByType("crlatti:link");
	
	//query nel repository per capire se bisogna cancellare alcuni link
	for(var z=0; z<linksNelRepository.length; z++){
		var trovato = false;
		var linkNelRepository = linksNelRepository[z];
		
		//cerco il nome del link nel repo all'interno del json
		for (var q=0; q<links.length(); q++){
			var link = links.get(q);
			var descrizione = filterParam(link.get("descrizione"));
			if(""+descrizione+""==""+linkNelRepository.name+""){
				trovato = true;
				break
			}
		}
		if(!trovato){
			linkNelRepository.remove();
		}
	}
	
	
} else {
	status.code = 400;
	status.message = "id atto e commissione target non valorizzati";
	status.redirect = true;
}