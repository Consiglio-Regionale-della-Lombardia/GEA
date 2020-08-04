<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">


var seduta = json.get("seduta");
var id = seduta.get("idSeduta");

var atti = seduta.get("attiTrattati");
var attiSindacato = seduta.get("attiSindacato");
var consultazioniAtti = seduta.get("consultazioniAtti");
var audizioni = seduta.get("audizioni");


if(checkIsNotNull(id)){
	
	var sedutaFolderNode = utils.getNodeFromString(id);

	
	// Atti trattati
	
	var attiXPathQuery = "*[@cm:name='AttiTrattati']";
	var attiFolderNode = sedutaFolderNode.childrenByXPath(attiXPathQuery)[0];		
	
	var numeroAtti = atti.length();
	
	for (var j=0; j<numeroAtti; j++){
	
		var atto = atti.get(j).get("attoTrattato").get("atto").get("atto");
		
		var attoTrattato = atti.get(j).get("attoTrattato");
		
		var previstoAttoTrattato = filterParam(attoTrattato.get("previsto"));
		var discussoAttoTrattato = filterParam(attoTrattato.get("discusso"));
		var numeroOrdinamento = filterParam(attoTrattato.get("numeroOrdinamento"));
		
		var idAttoTrattato = filterParam(atto.get("id"));
			
		var attoTrattatoFolderNode = utils.getNodeFromString(idAttoTrattato);
		
		var tipoAttoTrattato = attoTrattatoFolderNode.typeShort.substring(12).toUpperCase();
		
		var nomeAttoTrattato = tipoAttoTrattato +"-"+attoTrattatoFolderNode.name
		
		//verifica l'esistenza dell'atto all'interno del folder AttiTrattati
		var existAttoTrattatoXPathQuery = "*[@cm:name=\""+nomeAttoTrattato+"\"]";
		var attoTrattatoEsistenteResults = attiFolderNode.childrenByXPath(existAttoTrattatoXPathQuery);
		
		var attoTrattatoNode = null;
		
		var creaAssociazione = true;
		
		if(attoTrattatoEsistenteResults!=null && attoTrattatoEsistenteResults.length>0){
			attoTrattatoNode = attoTrattatoEsistenteResults[0];
			creaAssociazione = false;
		} else {
			attoTrattatoNode = attiFolderNode.createNode(nomeAttoTrattato,"crlatti:attoTrattatoODG");
			attoTrattatoNode.content = nomeAttoTrattato;
		}
	
		if(creaAssociazione){
			attoTrattatoNode.createAssociation(attoTrattatoFolderNode,"crlatti:attoTrattatoSedutaODG");
		}
		
		attoTrattatoNode.properties["crlatti:previstoAttoTrattatoODG"] = previstoAttoTrattato;
		attoTrattatoNode.properties["crlatti:discussoAttoTrattatoODG"] = discussoAttoTrattato;
		attoTrattatoNode.properties["crlatti:numeroAttoTrattatoODG"] = attoTrattatoFolderNode.name;
		attoTrattatoNode.properties["crlatti:tipoAttoTrattatoODG"] = tipoAttoTrattato;
		attoTrattatoNode.properties["crlatti:numeroOrdinamento"] = numeroOrdinamento;
		
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
			var atto = atti.get(q).get("attoTrattato").get("atto").get("atto");
			var attoTrattato = utils.getNodeFromString(atto.get("id"));
			
			var attoTrattatoTipo = attoTrattato.typeShort.substring(12).toUpperCase();
			
			if(""+attoTrattatoTipo+"-"+attoTrattato.name+""==""+attoTrattatoNelRepository.name+""){
				trovato = true;
				break
			}
		}
		if(!trovato){
			attoTrattatoNelRepository.remove();
		}
	}
	
	
	// Atti sindacato
	
	var attiSindacatoXPathQuery = "*[@cm:name='AttiSindacato']";
	var attiSindacatoFolderNode = sedutaFolderNode.childrenByXPath(attiSindacatoXPathQuery)[0];		
	
	var numeroAttiSindacato = attiSindacato.length();
	
	for (var j=0; j<numeroAttiSindacato; j++){
	
		var attoSindacato = attiSindacato.get(j).get("collegamentoAttiSindacato");
		
		var idAttoTrattato = filterParam(attoSindacato.get("idAtto"));
		var tipoAtto = filterParam(attoSindacato.get("tipoAtto"));
		var numeroAtto = filterParam(attoSindacato.get("numeroAtto"));
		var oggettoAtto = filterParam(attoSindacato.get("oggettoAtto"));
		var numeroOrdinamento = filterParam(attoSindacato.get("numeroOrdinamento"));
			
		var attoIndirizzoTrattatoFolderNode = utils.getNodeFromString(idAttoTrattato);
		
		//verifica l'esistenza dell'atto all'interno del folder AttiTrattati
		var existAttoIndirizzoTrattatoXPathQuery = "*[@cm:name=\""+attoIndirizzoTrattatoFolderNode.name+"\"]";
		var attoIndirizzoTrattatoEsistenteResults = attiSindacatoFolderNode.childrenByXPath(existAttoIndirizzoTrattatoXPathQuery);
		
		var attoIndirizzoTrattatoNode = null;
		
		var creaAssociazione = true;
		
		if(attoIndirizzoTrattatoEsistenteResults!=null && attoIndirizzoTrattatoEsistenteResults.length>0){
			attoIndirizzoTrattatoNode = attoIndirizzoTrattatoEsistenteResults[0];
			creaAssociazione = false;
		} else {
			attoIndirizzoTrattatoNode = attiSindacatoFolderNode.createNode(attoIndirizzoTrattatoFolderNode.name,"crlatti:attoIndirizzoTrattatoODG");
			attoIndirizzoTrattatoNode.content = attoIndirizzoTrattatoFolderNode.name;
		}
	
		if(creaAssociazione){
			attoIndirizzoTrattatoNode.createAssociation(attoIndirizzoTrattatoFolderNode,"crlatti:attoIndirizzoTrattatoSedutaODG");
		}
		
		attoIndirizzoTrattatoNode.properties["crlatti:numeroOrdinamento"] = numeroOrdinamento;
		
		attoIndirizzoTrattatoNode.save();
		
	}	
	
	
	//verifica atti sindacato trattati da cancellare
	var attiSindacatoTrattatiNelRepository = attiSindacatoFolderNode.getChildAssocsByType("crlatti:attoIndirizzoTrattatoODG");
	
	//query nel repository per capire se bisogna cancellare alcuni link
	for(var z=0; z<attiSindacatoTrattatiNelRepository.length; z++){
		var trovato = false;
		var attoSindacatoTrattatoNelRepository = attiSindacatoTrattatiNelRepository[z];
		
		//cerco il nome dell'atto trattato nel repo all'interno del json
		for (var q=0; q<attiSindacato.length(); q++){
			var attoSindacato = attiSindacato.get(q).get("collegamentoAttiSindacato");
			var attoSindacatoTrattato = utils.getNodeFromString(attoSindacato.get("idAtto"));
			
			if(""+attoSindacatoTrattato.name+""==""+attoSindacatoTrattatoNelRepository.name+""){
				trovato = true;
				break
			}
		}
		if(!trovato){
			attoSindacatoTrattatoNelRepository.remove();
		}
	}
	
	
	// consultazioni
	
	var numeroConsultazioni = consultazioniAtti.length();
	
	for (var j=0; j<numeroConsultazioni; j++){
		
		var consultazione = consultazioniAtti.get(j).get("consultazione");
	
		var idAtto = filterParam(consultazione.get("idAtto"));
		var nomeConsultazione = filterParam(consultazione.get("descrizione"));
		var previstaConsultazione =  filterParam(consultazione.get("prevista"));
		var discussaConsultazione =  filterParam(consultazione.get("discussa"));
		
		var attoFolderNode = utils.getNodeFromString(idAtto);
		
		
		var consultazioniXPathQuery = "*[@cm:name='Consultazioni']";
		var consultazioniFolderNode = attoFolderNode.childrenByXPath(consultazioniXPathQuery)[0];
		
		
		//verifica l'esistenza della consultazione
		var existConsultazioneXPathQuery = "*[@cm:name=\""+nomeConsultazione+"\"]";
		var consultazioneEsistenteResults = consultazioniFolderNode.childrenByXPath(existConsultazioneXPathQuery);
	
	
		
		if(consultazioneEsistenteResults!=null && consultazioneEsistenteResults.length>0){
			consultazioneNode = consultazioneEsistenteResults[0];
			
			consultazioneNode.properties["crlatti:previstaConsultazione"] = previstaConsultazione;
			consultazioneNode.properties["crlatti:discussaConsultazione"] = discussaConsultazione;
			
			consultazioneNode.save();
		
		} else {
			status.code = 500;
			status.message = "consultazione non esistente";
			status.redirect = true;
		}
	
		
	}	
	
	
	// audizioni
	
	var audizioniXPathQuery = "*[@cm:name='Audizioni']";
	var audizioniFolderNode = sedutaFolderNode.childrenByXPath(audizioniXPathQuery)[0];		
	
	var numeroAudizioni = audizioni.length();
	
	for (var j=0; j<numeroAudizioni; j++){
		
		var audizione = audizioni.get(j).get("audizione");
	
		var soggettoPartecipante = filterParam(audizione.get("soggettoPartecipante"));
		var previstoAudizione = filterParam(audizione.get("previsto"));
		var discussoAudizione = filterParam(audizione.get("discusso"));
		
		//verifica l'esistenza dell'audizione all'interno del folder Audizioni
		var existAudizioneXPathQuery = "*[@cm:name=\""+soggettoPartecipante+"\"]";
		var audizioneEsistenteResults = audizioniFolderNode.childrenByXPath(existAudizioneXPathQuery);
		
		var audizioneNode = null;
				
		if(audizioneEsistenteResults!=null && audizioneEsistenteResults.length>0){
			audizioneNode = audizioneEsistenteResults[0];
		} else {
			audizioneNode = audizioniFolderNode.createNode(soggettoPartecipante,"crlatti:audizione");
		}
	
		audizioneNode.properties["crlatti:previstoAudizione"] = previstoAudizione; 
		audizioneNode.properties["crlatti:discussoAudizione"] = discussoAudizione; 
		
		audizioneNode.save();
		
	}	
	
	
	//verifica audizioni da cancellare
	var audizioniNelRepository = audizioniFolderNode.getChildAssocsByType("crlatti:audizione");
	
	//query nel repository per capire se bisogna cancellare alcune audizioni
	for(var z=0; z<audizioniNelRepository.length; z++){
		var trovato = false;
		var audizioneNelRepository = audizioniNelRepository[z];
		
		//cerco il nome dell'audizione nel repo all'interno del json
		for (var q=0; q<audizioni.length(); q++){
			var audizione = audizioni.get(q).get("audizione");
			if(""+audizione.get("soggettoPartecipante")+""==""+audizioneNelRepository.name+""){
				trovato = true;
				break
			}
		}
		if(!trovato){
			audizioneNelRepository.remove();
		}
	}
	
	
	
}else{
	
	status.code = 400;
	status.message = "id seduta non valorizzato";
	status.redirect = true;
}
		
