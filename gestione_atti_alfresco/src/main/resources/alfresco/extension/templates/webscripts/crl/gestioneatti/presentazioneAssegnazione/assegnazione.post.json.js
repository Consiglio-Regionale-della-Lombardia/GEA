<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var statoAtto = atto.get("stato");
// suppongo che ci sia solo un passaggio essendo la fase di assegnazione antecedente all'esame commissioni e all'esame aula
var commissioni = atto.get("passaggi").get(0).get("passaggio").get("commissioni");
var organismiStatutari = atto.get("organismiStatutari");

if(checkIsNotNull(id)){
	var attoNode = utils.getNodeFromString(id);
	
	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='Passaggio1']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
	
	//gestione commissioni e ridondanza (in eliminazione) di commissioni referenti e consultive
	var commissioniXPathQuery = "*[@cm:name='Commissioni']";
	var commissioniFolderNode = passaggioFolderNode.childrenByXPath(commissioniXPathQuery)[0];
	
	var commissioniAnnullateXPathQuery = "*[@cm:name='CommissioniAnnullate']";
	var commissioniAnnullateFolderNode = passaggioFolderNode.childrenByXPath(commissioniAnnullateXPathQuery)[0];
	
	var commissioniAnnullateResults = commissioniAnnullateFolderNode.getChildAssocsByType("crlatti:commissione")
	
	var numeroCommissioni = commissioni.length();
	for (var j=0; j<numeroCommissioni; j++){
		var commissione = commissioni.get(j).get("commissione");
		var descrizione = filterParam(commissione.get("descrizione"));
		var dataProposta = filterParam(commissione.get("dataProposta"));
		var dataAssegnazione = filterParam(commissione.get("dataAssegnazione"));
		var ruolo = filterParam(commissione.get("ruolo"));
		var stato = filterParam(commissione.get("stato"));
		var dataAnnullo = filterParam(commissione.get("dataAnnullo"));
		
		var dataPropostaParsed = null;
		if(checkIsNotNull(dataProposta)){
			var dataPropostaSplitted = dataProposta.split("-");
			dataPropostaParsed = new Date(dataPropostaSplitted[0],dataPropostaSplitted[1]-1,dataPropostaSplitted[2]);
		}
		
		var dataAssegnazioneParsed = null;
		if(checkIsNotNull(dataAssegnazione)){
			var dataAssegnazioneSplitted = dataAssegnazione.split("-");
			dataAssegnazioneParsed = new Date(dataAssegnazioneSplitted[0],dataAssegnazioneSplitted[1]-1,dataAssegnazioneSplitted[2]);
		}
		
		var dataAnnulloParsed = null;
		if(checkIsNotNull(dataAnnullo)){
			var dataAnnulloSplitted = dataAnnullo.split("-");
			dataAnnulloParsed = new Date(dataAnnulloSplitted[0],dataAnnulloSplitted[1]-1,dataAnnulloSplitted[2]);
		}
		
		
		//verifica l'esistenza della commissione all'interno del folder Commissioni
		var existCommissioneXPathQuery = "*[@cm:name=\""+descrizione+"\"]";
		var commissioneEsistenteResults = commissioniFolderNode.childrenByXPath(existCommissioneXPathQuery);
		var commissioneNode = null;
		
		
		// se lo stato della commissione e' Annullato allora la commissione esiste gia' nel repository
		if(stato == "Annullato"){
			
			
			// Gestione delle commissioni annullate. 
			// Le commissioni annullate vengono spostate nella cartella commissioniAnnullate 
			// in fase di update viene effettuato un controllo su alcune proprieta' per capire se le commmissioni sono state gia' annullate 
			// (cioe' sono gia' presenti nella cartella commissioniAnnullate) o devono essere annullate per la prima volta
			
			var check = false;
			
			for(var k=0; k<commissioniAnnullateResults.length; k++) {
				
				commissioneAnnullata = commissioniAnnullateResults[k];
				var nomeCommissioneAnnullata = commissioneAnnullata.properties["cm:name"];
				
				// controllo su nomecommissione, dataAssegnazione, dataAnnullo, ruolo
				if(nomeCommissioneAnnullata.indexOf(descrizione) == 0 && 
                                        ((dataAssegnazioneParsed == null && commissioneAnnullata.properties["crlatti:dataAssegnazioneCommissione"] == null) || 
                                        (dataAssegnazioneParsed !=null && commissioneAnnullata.properties["crlatti:dataAssegnazioneCommissione"] != null && 
                                        dataAssegnazioneParsed.getTime() == commissioneAnnullata.properties["crlatti:dataAssegnazioneCommissione"].getTime())) &&
                                        ((dataAnnulloParsed == null && commissioneAnnullata.properties["crlatti:dataAnnulloCommissione"] == null) || 
                                        (dataAnnulloParsed != null && commissioneAnnullata.properties["crlatti:dataAnnulloCommissione"] != null && 
                                        dataAnnulloParsed.getTime() == commissioneAnnullata.properties["crlatti:dataAnnulloCommissione"].getTime())) &&
					ruolo == commissioneAnnullata.properties["crlatti:ruoloCommissione"]){
					
					// prendo il nodo relativo alla commissione precedentemente annullata
					commissioneNode = commissioneAnnullata;
					check = true;
					break;
				}
			}
			
			
			if(!check){
				commissioneNode = commissioneEsistenteResults[0];
				commissioneNode.move(commissioniAnnullateFolderNode);
				var timestamp = new Date().getTime();
				commissioneNode.name = descrizione+"#"+timestamp;
				commissioneNode.save();
			}
			
			
		
		}else{
			
			
			if(commissioneEsistenteResults!=null && commissioneEsistenteResults.length>0){
				commissioneNode = commissioneEsistenteResults[0];
			} else {
				

				//commissioneNode = commissioniFolderNode.createNode(descrizione,"crlatti:commissione");
				
				// copia la cartella commissione dagli space template e la rinomina
				
				var commissioneSpaceTemplateQuery = "PATH:\"/app:company_home/app:dictionary/app:space_templates/cm:Commissione\"";
				var commissioneSpaceTemplateNode = search.luceneSearch(commissioneSpaceTemplateQuery)[0];			
				
				// deep copy con secondo argomento = true
				commissioneSpaceTemplateNode.copy(commissioniFolderNode, true);
				
				var commissioneXPathQuery = "*[@cm:name='Commissione']";
				commissioneNode = commissioniFolderNode.childrenByXPath(commissioneXPathQuery)[0];
				commissioneNode.name = descrizione;
			}
			
			// setting dei permessi sul nodo in base alla descrizione
			var commissioniAnagraficaQuery = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:Commissioni/*\"";
			logger.log("query commissioni -> " + commissioniAnagraficaQuery + " , descrizione -> " + descrizione);
			var commissioniAnagraficaResponse = search.luceneSearch(commissioniAnagraficaQuery);
	
			if (commissioniAnagraficaResponse != null && commissioniAnagraficaResponse.length > 0) {
				for (var i = 0; i < commissioniAnagraficaResponse.length; i++) {
					if (commissioniAnagraficaResponse[i].properties["cm:title"] == descrizione) {
						var permissionGroup = "GROUP_" + getLegislaturaCorrente() + "_" + commissioniAnagraficaResponse[i].properties["cm:name"];
						logger.log("setPermission " + permissionGroup + " on " + attoNode.nodeRef);
						attoNode.setPermission("Coordinator", permissionGroup);
						break;
					}
				}
			}
			
		}
		
	
		
		commissioneNode.properties["crlatti:dataAssegnazioneCommissione"] = dataAssegnazioneParsed;
		commissioneNode.properties["crlatti:dataAnnulloCommissione"] = dataAnnulloParsed;
		commissioneNode.properties["crlatti:dataPropostaCommissione"] = dataPropostaParsed;
		commissioneNode.properties["crlatti:ruoloCommissione"] = ruolo;
		commissioneNode.properties["crlatti:statoCommissione"] = stato;
		
		commissioneNode.save();
		

	}
	
	//verifica commissioni da cancellare
	var commissioniNelRepository = commissioniFolderNode.getChildAssocsByType("crlatti:commissione");
	
	//query nel repository per capire se bisogna cancellare delle commissioni
	for(var z=0; z<commissioniNelRepository.length; z++){
		var trovato = false;
		var commissioneNelRepository = commissioniNelRepository[z];
		
		//cerco il nome della commissione nel repo all'interno del json
		for (var q=0; q<numeroCommissioni; q++){
			var commissione = commissioni.get(q).get("commissione");
			var descrizione = filterParam(commissione.get("descrizione"));
			var stato = filterParam(commissione.get("stato"));
			if(""+descrizione+""==""+commissioneNelRepository.name+"" && ""+stato+""!="Annullato"){
				trovato = true;
				break;
			}
		}
		if(!trovato){
			var commissioniSpace = commissioneNelRepository.parent;
			commissioneNelRepository.remove();
			/*
			 * in eliminazione le commissioni devono essere gestite manualmente 
			 * a causa di un bug di Alfresco risolto nella 4.1.1 riguardo le regole in outbound:
			 * 
			 * https://issues.alfresco.com/jira/browse/ALF-12711
			 * 
			*/
			
			//Commissioni referenti
			var commissioniReferentiLuceneQuery = 
				"PARENT:\""+commissioniSpace.nodeRef+"\" AND @crlatti\\:ruoloCommissione:\"Referente\"";
			
		    var commissioniReferentiResults = search.luceneSearch(commissioniReferentiLuceneQuery);		
			var commissioniReferentiAtto = new Array(commissioniReferentiResults.length);
			for (var r=0; r<commissioniReferentiResults.length; r++) {
				commissioniReferentiAtto[r] = commissioniReferentiResults[r].name;
			}
			
			//Commissioni consultive
			var commissioniConsultiveLuceneQuery = 
				"PARENT:\""+commissioniSpace.nodeRef+"\" AND @crlatti\\:ruoloCommissione:\"Consultiva\"";
			
		    var commissioniConsultiveResults = search.luceneSearch(commissioniConsultiveLuceneQuery);		
			var commissioniConsultiveAtto = new Array(commissioniConsultiveResults.length);
			for (var r=0; r<commissioniConsultiveResults.length; r++) {
				commissioniConsultiveAtto[r] = commissioniConsultiveResults[r].name;
			}
			
			attoNode.properties["crlatti:commReferente"] = commissioniReferentiAtto;
			attoNode.properties["crlatti:commConsultiva"] = commissioniConsultiveAtto;
			attoNode.save();
		}
	}
	
	//gestione pareri e ridondanza (in eliminazione) di organismiStatutari
	var pareriXPathQuery = "*[@cm:name='Pareri']";
	var pareriFolderNode = attoNode.childrenByXPath(pareriXPathQuery)[0];
	
	var numeroOrganismi = organismiStatutari.length();
	for (var j=0; j<numeroOrganismi; j++){
		var organismoStatutario = organismiStatutari.get(j).get("organismoStatutario");
		var descrizione = filterParam(organismoStatutario.get("descrizione"));
		var dataAssegnazione = filterParam(organismoStatutario.get("dataAssegnazione"));
		var dataAnnullo = filterParam(organismoStatutario.get("dataAnnullo"));
		var obbligatorio = filterParam(organismoStatutario.get("obbligatorio"));
		
		//verifica l'esistenza del parere all'interno del folder Pareri
		var existParereXPathQuery = "*[@cm:name='"+descrizione+"']";
		var parereEsistenteResults = pareriFolderNode.childrenByXPath(existParereXPathQuery);
		var parereNode = null;
		if(parereEsistenteResults!=null && parereEsistenteResults.length>0){
			parereNode = parereEsistenteResults[0];
		} else {
			
			// copia la cartella parere dagli space template e la rinomina
			
			var parereSpaceTemplateQuery = "PATH:\"/app:company_home/app:dictionary/app:space_templates/cm:Parere\"";
			var parereSpaceTemplateNode = search.luceneSearch(parereSpaceTemplateQuery)[0];
			
			// deep copy con secondo argomento = true
			parereSpaceTemplateNode.copy(pareriFolderNode, true);
			
			var parereXPathQuery = "*[@cm:name='Parere']";
			parereNode = pareriFolderNode.childrenByXPath(parereXPathQuery)[0];
			parereNode.name = descrizione;
			
		}
		
		var dataAssegnazioneParsed = null;
		if(checkIsNotNull(dataAssegnazione)){
			var dataAssegnazioneSplitted = dataAssegnazione.split("-");
			dataAssegnazioneParsed = new Date(dataAssegnazioneSplitted[0],dataAssegnazioneSplitted[1]-1,dataAssegnazioneSplitted[2]);
		}
		
		var dataAnnulloParsed = null;
		if(checkIsNotNull(dataAnnullo)){
			var dataAnnulloSplitted = dataAnnullo.split("-");
			dataAnnulloParsed = new Date(dataAnnulloSplitted[0],dataAnnulloSplitted[1]-1,dataAnnulloSplitted[2]);
		}
		
		parereNode.properties["crlatti:organismoStatutarioParere"] = descrizione;
		parereNode.properties["crlatti:dataAssegnazioneParere"] = dataAssegnazioneParsed;
		parereNode.properties["crlatti:dataAnnulloParere"] = dataAnnulloParsed;
		parereNode.properties["crlatti:obbligatorio"] = obbligatorio;
		
		var tipoAttoRelatore = attoNode.typeShort.substring(12);
		var numeroAttoRelatore = attoNode.name;
		
		// proprieta' necessarie alla reportistica
		parereNode.properties["crlatti:tipoAttoParere"] = tipoAttoRelatore;
		parereNode.properties["crlatti:numeroAttoParere"] = numeroAttoRelatore;
		
		parereNode.save();
	}
	
	//verifica pareri da cancellare
	var pareriNelRepository = pareriFolderNode.getChildAssocsByType("crlatti:parere");
	
	//query nel repository per capire se bisogna cancellare dei pareri
	for(var z=0; z<pareriNelRepository.length; z++){
		var trovato = false;
		var parereNelRepository = pareriNelRepository[z];
		
		//cerco il nome del parere nel repo all'interno del json
		for (var q=0; q<numeroOrganismi; q++){
			var organismoStatutario = organismiStatutari.get(q).get("organismoStatutario");
			var descrizione = filterParam(organismoStatutario.get("descrizione"));
			if(""+descrizione+""==""+parereNelRepository.name+""){
				trovato = true;
				break;
			}
		}
		if(!trovato){
			var pareriSpace = parereNelRepository.parent;
			parereNelRepository.remove();
			/*
			 * in eliminazione le commissioni devono essere gestite manualmente 
			 * a causa di un bug di Alfresco risolto nella 4.1.1 riguardo le regole in outbound:
			 * 
			 * https://issues.alfresco.com/jira/browse/ALF-12711
			 * 
			*/
			
			var pareriLuceneQuery = 
				"PARENT:\""+pareriSpace.nodeRef+"\" AND TYPE:\"crlatti:parere\"";
			
		    var pareriResults = search.luceneSearch(pareriLuceneQuery);		
			var pareriAtto = new Array(pareriResults.length);
			for (var r=0; r<pareriResults.length; r++) {
				pareriAtto[r] = pareriResults[r].name;
			}
			
			attoNode.properties["crlatti:organismiStatutari"] = pareriAtto;
			attoNode.save();
		}
	}
	
	
	attoNode.properties["crlatti:statoAtto"] = statoAtto;
	attoNode.save();
	
	model.atto = attoNode;
	
} else {
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
}