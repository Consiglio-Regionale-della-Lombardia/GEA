<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");

var consultazioni = atto.get("consultazioni");



if(checkIsNotNull(id)){
	
	var attoNode = utils.getNodeFromString(id);
	
	var consultazioniXPathQuery = "*[@cm:name='Consultazioni']";
	var consultazioniFolderNode = attoNode.childrenByXPath(consultazioniXPathQuery)[0];
	
	var numeroConsultazioni = consultazioni.length();
	
	for (var i=0; i<numeroConsultazioni; i++){
	
		var consultazione = consultazioni.get(i).get("consultazione");
	
		var descrizione = filterParam(consultazione.get("descrizione")).trim();
		var dataSeduta = filterParam(consultazione.get("dataSeduta"));
		var dataConsultazione =  filterParam(consultazione.get("dataConsultazione"));
		var prevista =  filterParam(consultazione.get("prevista"));
		var discussa =  filterParam(consultazione.get("discussa"));
		var note =  filterParam(consultazione.get("note"));
		
		var commissione =  filterParam(consultazione.get("commissione"));
		
			
		
			
		//verifica l'esistenza della consultazione all'interno del folder Consultazioni
		var existConsultazioneXPathQuery = "*[@cm:name=\""+descrizione+"\"]";
		var consultazioneEsistenteResults = consultazioniFolderNode.childrenByXPath(existConsultazioneXPathQuery);
		var consultazioneNode = null;
		if(consultazioneEsistenteResults!=null && consultazioneEsistenteResults.length>0){
			consultazioneNode = consultazioneEsistenteResults[0];
		} else {
			// copia la cartella consultazione dagli space template e la rinomina
			
			var consSpaceTemplateQuery = "PATH:\"/app:company_home/app:dictionary/app:space_templates/cm:Consultazione\"";
			var consSpaceTemplateNode = search.luceneSearch(consSpaceTemplateQuery)[0];
			
			
			// deep copy con secondo argomento = true
			consSpaceTemplateNode.copy(consultazioniFolderNode, true);
			
			var consXPathQuery = "*[@cm:name='Consultazione']";
			consultazioneNode = consultazioniFolderNode.childrenByXPath(consXPathQuery)[0];
			consultazioneNode.name = descrizione;
			

			
			// aggiungo l'atto alla seduta solo se esiste una seduta in quella data
			
			
			
			var seduteCommissionePath = "/app:company_home"+
			"/cm:"+search.ISO9075Encode("CRL")+
			"/cm:"+search.ISO9075Encode("Gestione Atti")+
			"/cm:"+search.ISO9075Encode("Sedute")+
			"/cm:"+search.ISO9075Encode(commissione);
			
			
			
			var sedutaLuceneQuery = "PATH:\""+seduteCommissionePath+"//*\" AND TYPE:\"crlatti:sedutaODG\" ";
			sedutaLuceneQuery += "AND @crlatti\\:dataSedutaSedutaODG:\""+dataSeduta+"T00:00:00.000Z\" ";

			
			var sedutaResults = search.luceneSearch(sedutaLuceneQuery);
			
			if(sedutaResults.length>0){
				
				var seduta = sedutaResults[0];
				
				var attiXPathQuery = "*[@cm:name='AttiTrattati']";
				var attiFolderNode = seduta.childrenByXPath(attiXPathQuery)[0];
				
				var tipoAttoTrattato = attoNode.typeShort.substring(12).toUpperCase();
				var nomeAttoTrattato = tipoAttoTrattato +"-"+attoNode.name
				
				// verifico che l'atto non sia già tra gli atti trattati nella seduta
				
				var attoTrattatoXPathQuery = "*[@cm:name=\""+nomeAttoTrattato+"\"]";
				var attoTrattatoEsistenteResults = attiFolderNode.childrenByXPath(attoTrattatoXPathQuery);
				
				if(attoTrattatoEsistenteResults!=null && attoTrattatoEsistenteResults.length>0){
					// do nothing
				} else {
					var attoTrattatoNode = attiFolderNode.createNode(nomeAttoTrattato,"crlatti:attoTrattatoODG");
					attoTrattatoNode.content = nomeAttoTrattato;
					
					attoTrattatoNode.createAssociation(attoNode,"crlatti:attoTrattatoSedutaODG");
				
					attoTrattatoNode.properties["crlatti:numeroAttoTrattatoODG"] = attoNode.name;
					attoTrattatoNode.properties["crlatti:tipoAttoTrattatoODG"] = tipoAttoTrattato;
					
					attoTrattatoNode.save();
				}
				
							
				
	
			}
			
			
	}
	
		
		
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
		consultazioneNode.properties["crlatti:commissioneConsultazione"] = commissione;
		
		
		consultazioneNode.save();
		
		var soggettiInvitati = consultazione.get("soggettiInvitati");
		
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
		
		
		
	}
	
	
	//verifica consultazioni da cancellare
	var consultazioniNelRepository = consultazioniFolderNode.getChildAssocsByType("crlatti:consultazione");
	
		
	//query nel repository per capire se bisogna cancellare alcune consultazioni
	for(var z=0; z<consultazioniNelRepository.length; z++){
		var trovato = false;
		var consultazioneNelRepository = consultazioniNelRepository[z];
		
		//cerco il nome della consultazione nel repo all'interno del json
		for (var q=0; q<consultazioni.length(); q++){
			var consult = consultazioni.get(q).get("consultazione");
			var descrizione = filterParam(consult.get("descrizione")).trim();
			if(""+descrizione+""==""+consultazioneNelRepository.name+""){
				trovato = true;
				break
			}
		}
		if(!trovato){
			consultazioneNelRepository.remove();			
			
		}
	}
	
	
} else {
	status.code = 400;
	status.message = "id Atto è obbligatorio";
	status.redirect = true;
}