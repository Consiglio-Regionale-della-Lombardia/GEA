<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var noteGenerali = atto.get("notePresentazioneAssegnazione");
var links = atto.get("linksPresentazioneAssegnazione");


if(checkIsNotNull(id)){
	var attoNode = utils.getNodeFromString(id);
	
	//note generali
	var noteGeneraliFilename = "Note Generali.txt";
	var noteGeneraliXPathQuery = "*[@cm:name='"+noteGeneraliFilename+"']";
	var noteGeneraliResults = attoNode.childrenByXPath(noteGeneraliXPathQuery);
	
	var noteGeneraliNode = null;
	if(noteGeneraliResults!=null && noteGeneraliResults.length>0){
		noteGeneraliNode = noteGeneraliResults[0];	
	} else {
		noteGeneraliNode = attoNode.createFile(noteGeneraliFilename);
		noteGeneraliNode.properties.content.setEncoding("UTF-8");
	}
	
	noteGeneraliNode.content = noteGenerali;
	noteGeneraliNode.save();

	//links
	var linksFolderXPathQuery = "*[@cm:name='Links']";
	var linksFolderNode = attoNode.childrenByXPath(linksFolderXPathQuery)[0];
	
	for (var i=0; i<links.length(); i++){
		var link = links.get(i).get("link");
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
			var descrizione = filterParam(link.get("link").get("descrizione"));
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
	status.message = "id atto non valorizzato";
	status.redirect = true;
}