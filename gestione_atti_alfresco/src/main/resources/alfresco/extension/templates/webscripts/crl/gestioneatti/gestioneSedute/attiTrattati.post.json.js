<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">


var seduta = json.get("seduta");
var id = seduta.get("idSeduta");

var atti = seduta.get("attiTrattati");


if(checkIsNotNull(id)){
	
	var sedutaFolderNode = utils.getNodeFromString(id);

	var attiXPathQuery = "*[@cm:name='AttiTrattati']";
	var attiFolderNode = sedutaFolderNode.childrenByXPath(attiXPathQuery)[0];		
	
	var numeroAtti = atti.length();
	
	for (var j=0; j<numeroAtti; j++){
	
		var atto = atti.get(j).get("atto");
	
		var idAttoTrattato = filterParam(atto.get("id"));
		
		var attoTrattatoFolderNode = utils.getNodeFromString(idAttoTrattato);
		
		//verifica l'esistenza dell'atto all'interno del folder AttiTrattati
		var existAttoTrattatoXPathQuery = "*[@cm:name='"+attoTrattatoFolderNode.name+"']";
		var attoTrattatoEsistenteResults = attiFolderNode.childrenByXPath(existAttoTrattatoXPathQuery);
		
		var attoTrattatoNode = null;
		
		var creaAssociazione = true;
		
		if(attoTrattatoEsistenteResults!=null && attoTrattatoEsistenteResults.length>0){
			attoTrattatoNode = attoTrattatoEsistenteResults[0];
			creaAssociazione = false;
		} else {
			attoTrattatoNode = attiFolderNode.createNode(attoTrattatoFolderNode.name,"crlatti:attoTrattatoODG");
			attoTrattatoNode.content = attoTrattatoFolderNode.name;
			attoTrattatoNode.save();
		}
	
		

		if(creaAssociazione){
			attoTrattatoNode.createAssociation(attoTrattatoFolderNode,"crlatti:attoTrattatoSedutaODG");
		}
		
		attoTrattatoNode.save();
		
	}	
	
	
	//verifica atti trattati da cancellare
	var attiTrattatiNelRepository = attiFolderNode.getChildAssocsByType("crlatti:attoTrattatoODG");
	
	//query nel repository per capire se bisogna cancellare alcuni link
	for(var z=0; z<attiTrattatiNelRepository.length; z++){
		var trovato = false;
		var attoTrattatoNelRepository = attiTrattatiNelRepository[z];
		
		//cerco il nome dell'atto trattato nel repo all'interno del json
		for (var q=0; q<atti.length(); q++){
			var atto = atti.get(q).get("atto");
			var attoTrattato = utils.getNodeFromString(atto.get("id"));
			if(""+attoTrattato.name+""==""+attoTrattatoNelRepository.name+""){
				trovato = true;
				break
			}
		}
		if(!trovato){
			attoTrattatoNelRepository.remove();
		}
	}
	
	
}else{
	
	status.code = 400;
	status.message = "id seduta non valorizzato";
	status.redirect = true;
}
		
