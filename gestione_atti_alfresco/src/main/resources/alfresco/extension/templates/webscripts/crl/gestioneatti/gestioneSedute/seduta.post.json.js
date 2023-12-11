<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">


var seduta = json.get("seduta").get("seduta");
var dataSeduta = filterParam(seduta.get("dataSeduta"));
var dalleOre = filterParam(seduta.get("dalleOre"));
var alleOre = filterParam(seduta.get("alleOre"));
var numVerbale = filterParam(seduta.get("numVerbale"));
var note = filterParam(seduta.get("note"));
var links = filterParam(seduta.get("links"));
var legislatura = seduta.get("legislatura");

var provenienza = json.get("target").get("target").get("provenienza");


var gestioneSedutePath = "/app:company_home"+
"/cm:"+search.ISO9075Encode("CRL")+
"/cm:"+search.ISO9075Encode("Gestione Atti")+
"/cm:"+search.ISO9075Encode("Sedute");


if(checkIsNotNull(provenienza)){
	
	var dataSedutaSplitted = dataSeduta.split("-");

	var anno = dataSedutaSplitted[0]
	var mese = dataSedutaSplitted[1];


	//creazione spazio per la provenienza (commissione o aula)
	var provenienzaPath = gestioneSedutePath + "/cm:"+search.ISO9075Encode(provenienza);
	var provenienzaLuceneQuery = "PATH:\""+provenienzaPath+"\"";
	var provenienzaResults = search.luceneSearch(provenienzaLuceneQuery);

	var provenienzaFolderNode = null;
	if(provenienzaResults!=null && provenienzaResults.length>0){
		provenienzaFolderNode = provenienzaResults[0];
	} else {
		var gestioneSeduteLuceneQuery = "PATH:\""+gestioneSedutePath+"\"";
		var gestioneSeduteFolderNode = search.luceneSearch(gestioneSeduteLuceneQuery)[0];
		provenienzaFolderNode = gestioneSeduteFolderNode.createFolder(provenienza);
		
		// setting dei permessi sul nodo in base alla provenienza
		var commissioniAnagraficaQuery = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:Commissioni/*\"";
		logger.log("query commissioni -> " + commissioniAnagraficaQuery);
		var commissioniAnagraficaResponse = search.luceneSearch(commissioniAnagraficaQuery);

		if (commissioniAnagraficaResponse != null && commissioniAnagraficaResponse.length > 0) {
			for (var j = 0; j < commissioniAnagraficaResponse.length; j++) {
				if (commissioniAnagraficaResponse[j].properties["cm:title"] == provenienza) {
					var permissionGroup = "GROUP_" + getLegislaturaCorrente() + "_" + commissioniAnagraficaResponse[j].properties["cm:name"];
					logger.log("setPermission " + permissionGroup + " on " + provenienzaFolderNode.nodeRef);
					provenienzaFolderNode.setPermission("Coordinator", permissionGroup);
					break;
				}
			}
		}

	}

	//creazione spazio anno
	var annoPath = provenienzaPath + "/cm:" + search.ISO9075Encode(anno);
	var annoLuceneQuery = "PATH:\""+annoPath+"\"";
	var annoResults = search.luceneSearch(annoLuceneQuery);
	var annoFolderNode = null;
	if(annoResults!=null && annoResults.length>0){
		annoFolderNode = annoResults[0];
	} else {
		annoFolderNode = provenienzaFolderNode.createFolder(anno);
	}

	//creazione spazio mese
	var mesePath = annoPath + "/cm:" + search.ISO9075Encode(mese);
	var meseLuceneQuery = "PATH:\""+mesePath+"\"";
	var meseResults = search.luceneSearch(meseLuceneQuery);
	var meseFolderNode = null;
	if(meseResults!=null && meseResults.length>0){
		meseFolderNode = meseResults[0];
	} else {
		meseFolderNode = annoFolderNode.createFolder(mese);
	}


	//verifica esistenza del folder della seduta
	var sedutaPath = mesePath + "/cm:" + search.ISO9075Encode(dataSeduta);
	var sedutaLuceneQuery = "PATH:\""+sedutaPath+"\"";
	var sedutaResults = search.luceneSearch(sedutaLuceneQuery);
	var checkLegislaturaCorrente = false;
	
	// controlla l'esecuzione dell'update
	var eseguiUpdate = false;
	
	if(sedutaResults!=null && sedutaResults.length>0){
//		//seduta presente
//		status.code = 500;
//		status.message = "seduta in data "+dataSeduta+" per l'organo "+provenienza+" gia' presente nel repository";
//		status.redirect = true;
		sedutaFolderNode = sedutaResults[0];
		checkLegislaturaCorrente = true;
		
	} else {
		//creazione del nodo
		var sedutaSpaceTemplateQuery = "PATH:\"/app:company_home/app:dictionary/app:space_templates/cm:Seduta\"";
		var sedutaSpaceTemplateNode = search.luceneSearch(sedutaSpaceTemplateQuery)[0];
		var sedutaFolderNode = sedutaSpaceTemplateNode.copy(meseFolderNode,true);
		sedutaFolderNode.name = dataSeduta;
		checkLegislaturaCorrente = false;
		eseguiUpdate=true;
	}
	
	if(checkLegislaturaCorrente){
		//verifica se la seduta appartiene ad una legislatura corrente
		var legislaturaSedutaODG = sedutaFolderNode.properties["crlatti:legislaturaSedutaODG"];
		
		if(legislaturaSedutaODG!=null && legislaturaSedutaODG!=""){
			var luceneQueryLegislaturaAnagrafica = 
				"PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti/cm:Anagrafica/cm:Legislature/*\" " +
				"AND TYPE:\"crlatti:legislaturaAnagrafica\" " +
				"AND @cm\\:name:\""+legislaturaSedutaODG+"\"";
			
			var legislatureResults = search.luceneSearch(luceneQueryLegislaturaAnagrafica);
			
			if(legislatureResults!=null && legislatureResults.length>0){
				var legislaturaAnagraficaNode = legislatureResults[0];
				var isCorrenteLegislatura = legislaturaAnagraficaNode.properties["crlatti:correnteLegislatura"];
				if(isCorrenteLegislatura){
					eseguiUpdate = true;
				}
			}
		}
	}
	
	if(eseguiUpdate){
	
		var dataSedutaSplitted = dataSeduta.split("-");
		var dataSedutaParsed = new Date(dataSedutaSplitted[0],dataSedutaSplitted[1]-1,dataSedutaSplitted[2]);
		
		sedutaFolderNode.properties["crlatti:dataSedutaSedutaODG"] = dataSedutaParsed;
		
		sedutaFolderNode.properties["crlatti:dalleOreSedutaODG"] = dalleOre;
		sedutaFolderNode.properties["crlatti:alleOreSedutaODG"] = alleOre;
	
		
		sedutaFolderNode.properties["crlatti:numVerbaleSedutaODG"] = numVerbale;
		sedutaFolderNode.properties["crlatti:noteSedutaODG"] = note;
	        
	    sedutaFolderNode.properties["crlatti:legislaturaSedutaODG"] = legislatura;
		
		sedutaFolderNode.save();
		
		
		// ricerco le consultazioni della legislatura corrente e inserisco eventuali atti trattati
		
		var gestioneAttiPath = "/app:company_home"+
		"/cm:"+search.ISO9075Encode("CRL")+
		"/cm:"+search.ISO9075Encode("Gestione Atti");
		
		
		var legislaturaPath = gestioneAttiPath + "/cm:"+search.ISO9075Encode(getLegislaturaCorrente());
		var consultazioniLuceneQuery = "PATH:\""+legislaturaPath+"//*\" AND TYPE:\"crlatti:consultazione\" ";
		consultazioniLuceneQuery += "AND @crlatti\\:commissioneConsultazione:\""+provenienza+"\" ";
		consultazioniLuceneQuery += "AND @crlatti\\:dataSedutaConsultazione:\""+dataSeduta+"T00:00:00.000Z\" ";
	
		
		var consultazioniResults = search.luceneSearch(consultazioniLuceneQuery);
		
		var attiXPathQuery = "*[@cm:name='AttiTrattati']";
		var attiFolderNode = sedutaFolderNode.childrenByXPath(attiXPathQuery)[0];
		
		for(var k=0; k<consultazioniResults.length;k++){
			
				
				var atto = consultazioniResults[k].parent.parent;
						
				var tipoAttoTrattato = atto.typeShort.substring(12).toUpperCase();
				var nomeAttoTrattato = tipoAttoTrattato +"-"+atto.name
				
				if (attiFolderNode.childByNamePath(nomeAttoTrattato) == null){			
					var attoTrattatoNode = attiFolderNode.createNode(nomeAttoTrattato,"crlatti:attoTrattatoODG");
					attoTrattatoNode.content = nomeAttoTrattato;
					
					attoTrattatoNode.createAssociation(atto,"crlatti:attoTrattatoSedutaODG");
				
					attoTrattatoNode.properties["crlatti:numeroAttoTrattatoODG"] = atto.name;
					attoTrattatoNode.properties["crlatti:tipoAttoTrattatoODG"] = tipoAttoTrattato;
					
					attoTrattatoNode.save();
				}
				
		}
	
	}
	
		
		
		
		//links
		var linksFolderXPathQuery = "*[@cm:name='Links']";
		var linksFolderNode = sedutaFolderNode.childrenByXPath(linksFolderXPathQuery)[0];
		
		for (var i=0; i<links.length(); i++){
			var link = links.get(i).get("link");
			var descrizione = link.get("descrizione");
			var indirizzo = link.get("indirizzo");
			
			var existLinkXPathQuery = "*[@cm:name=\""+descrizione+"\"]";
			var existLinkResults = linksFolderNode.childrenByXPath(existLinkXPathQuery);
			
			var linkNode = null;
			if(existLinkResults!=null && existLinkResults.length>0){
				linkNode = existLinkResults[0];
			} else {
				linkNode = linksFolderNode.createNode(descrizione,"crlatti:link");
			}
			
			linkNode.properties["crlatti:indirizzoCollegamento"] = indirizzo;
			linkNode.save();
		}
		
		model.seduta = sedutaFolderNode;
			
	
	
}else{
	
	status.code = 400;
	status.message = "id seduta e provenienza non valorizzati";
	status.redirect = true;
}
		
