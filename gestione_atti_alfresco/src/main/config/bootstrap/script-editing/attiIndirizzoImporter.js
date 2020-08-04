<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

//Script per l'importazione degli atti verso il sistema di Gestione Atti di Indirizzo


attiIndirizzoImportLogger.info("Importazione Atti da Sistema di Gestione Atti Indirizzo avviato..."); 

var importFolderXPathQuery = "*[@cm:name='Import']";
var importFolderNode = companyhome.childrenByXPath(importFolderXPathQuery)[0];

var gestioneAttiFolderXPathQuery = "*[@cm:name='Gestione Atti']";
var gestioneAttiFolderNode = importFolderNode.childrenByXPath(gestioneAttiFolderXPathQuery)[0];

//var attiIndirizzoFolderXPathQuery = "*[@cm:name='AttiIndirizzo']";
//var attiIndirizzoFolderNode = gestioneAttiFolderNode.childrenByXPath(attiIndirizzoFolderXPathQuery)[0];

var attiParsedXPathQuery = "*[@cm:name='AttiIndirizzoParsed']";
var attiParsedFolderNode = gestioneAttiFolderNode.childrenByXPath(attiParsedXPathQuery)[0];


var gestioneAttiIndirizzoPath = "/app:company_home"+
"/cm:"+search.ISO9075Encode("Import")+
"/cm:"+search.ISO9075Encode("Gestione Atti")+
"/cm:"+search.ISO9075Encode("AttiIndirizzo");

var gestioneAttiIndirizzoQuery = "PATH:\""+gestioneAttiIndirizzoPath+"/*\" AND TYPE:\"cm:content\"";
var xmlFileList = search.luceneSearch(gestioneAttiIndirizzoQuery, "cm:name", true);


attiIndirizzoImportLogger.info("Inizio importazione atti. Elementi da importare: "+xmlFileList.length);

var count = 0;

for(var i=0; i<xmlFileList.length; i++){
	
	var xmlFile =  xmlFileList[i];
	
	// parsing xml content
	var xmlObject = new XML(xmlFile.content);
	
	var operazione = xmlObject.@operazione;
	var id_atto = xmlObject.@id_atto.toString();
	var id_legislatura = xmlObject.@id_legislatura.toString();
	var tipo_atto = xmlObject.@tipo_atto.toString();
	var numero_atto = xmlObject.@numero_atto.toString();
	var oggetto_atto = xmlObject.@oggetto_atto.toString();
	
	var gestioneAttiPath = "/app:company_home"+
	"/cm:"+search.ISO9075Encode("CRL")+
	"/cm:"+search.ISO9075Encode("Gestione Atti");
	
	var dataImportazione = new Date();
	var mese = dataImportazione.getMonth() + 1;
	var anno = dataImportazione.getFullYear();
	
	var numero_legislatura = getNumeroLegislaturaByIdAnagrafica(id_legislatura);
	
	
	if(operazione == "CREATE"){
		
		//creazione dello spazio legislatura
		var legislaturaPath = gestioneAttiPath + "/cm:"+search.ISO9075Encode(numero_legislatura);
		var legislaturaLuceneQuery = "PATH:\""+legislaturaPath+"\"";
		var legislaturaResults = search.luceneSearch(legislaturaLuceneQuery);
		
		var legislaturaFolderNode = null;
		if(legislaturaResults!=null && legislaturaResults.length>0){
			legislaturaFolderNode = legislaturaResults[0];
		} else {
			var gestioneAttiLuceneQuery = "PATH:\""+gestioneAttiPath+"\"";
			var gestioneAttiFolderNode = search.luceneSearch(gestioneAttiLuceneQuery)[0];
			legislaturaFolderNode = gestioneAttiFolderNode.createFolder(numero_legislatura);
		}
		
		//creazione spazio anno
		var annoPath = legislaturaPath + "/cm:" + search.ISO9075Encode(anno);
		var annoLuceneQuery = "PATH:\""+annoPath+"\"";
		var annoResults = search.luceneSearch(annoLuceneQuery);
		var annoFolderNode = null;
		if(annoResults!=null && annoResults.length>0){
			annoFolderNode = annoResults[0];
		} else {
			annoFolderNode = legislaturaFolderNode.createFolder(anno);
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
		
		//creazione spazio attiIndirizzo
		var attoIndirizzoPath = mesePath + "/cm:" + search.ISO9075Encode("AttiIndirizzo");
		var attoIndirizzoQuery = "PATH:\""+attoIndirizzoPath+"\"";
		var attoIndirizzoResults = search.luceneSearch(attoIndirizzoQuery);
		var attoIndirizzoFolderNode = null;
		if(attoIndirizzoResults!=null && attoIndirizzoResults.length>0){
			attoIndirizzoFolderNode = attoIndirizzoResults[0];
		} else {
			attoIndirizzoFolderNode = meseFolderNode.createFolder("AttiIndirizzo");
		}
		
		// creazione spazio "tipo"
		var tipoPath = attoIndirizzoPath + "/cm:" + search.ISO9075Encode(tipo_atto);
		var tipoLuceneQuery = "PATH:\""+tipoPath+"\"";
		var tipoResults = search.luceneSearch(tipoLuceneQuery);
		var tipoFolderNode = null;
		if(tipoResults!=null && tipoResults.length>0){
			tipoFolderNode = tipoResults[0];
		} else {
			tipoFolderNode = attoIndirizzoFolderNode.createFolder(tipo_atto);
		}
		
		var attoPath = tipoPath + "/cm:" + search.ISO9075Encode(id_atto);
		var attoLuceneQuery = "PATH:\""+attoPath+"\"";
		var attoResults = search.luceneSearch(attoLuceneQuery);
		if(attoResults!=null && attoResults.length>0){
			attiIndirizzoImportLogger.error("Richiesta operazione di CREATE per atto con id: "+id_atto+" e tipo atto: "+tipo_atto+". L'atto e' gia' presente nel repository");			
		} else {
			//creazione del nodo AttoIndirizzo
			var attoSpaceTemplateQuery = "PATH:\"/app:company_home/app:dictionary/app:space_templates/cm:AttoIndirizzo\"";
			var attoSpaceTemplateNode = search.luceneSearch(attoSpaceTemplateQuery)[0];
			var attoFolderNode = attoSpaceTemplateNode.copy(tipoFolderNode,true);
			attoFolderNode.name = id_atto;
			attoFolderNode.properties["crlatti:IdAttoIndirizzo"] = id_atto;
			attoFolderNode.properties["crlatti:IdLegislaturaAttoIndirizzo"] = id_legislatura;
			attoFolderNode.properties["crlatti:tipoAttoIndirizzo"] = tipo_atto;
			attoFolderNode.properties["crlatti:numeroAttoIndirizzo"] = numero_atto;
			attoFolderNode.properties["crlatti:oggettoAttoIndirizzo"] = oggetto_atto;
			attoFolderNode.save();
		
		}
			
		xmlFile.move(attiParsedFolderNode);
		attiIndirizzoImportLogger.info("Parsing eseguito per il file "+xmlFile.name+": operazione "+operazione+" su atto "+tipo_atto+" "+numero_atto);
		count++;
		
		
	}else if (operazione == "UPDATE"){
		
		var legislaturaRepositoryPath = gestioneAttiPath + "/cm:"+search.ISO9075Encode(numero_legislatura);
		var attoIndirizzoLuceneQuery = "PATH:\""+legislaturaRepositoryPath+"//*\"";
		attoIndirizzoLuceneQuery += " AND TYPE:\"crlatti:attoIndirizzo\"";
		attoIndirizzoLuceneQuery += " AND @crlatti\\:IdAttoIndirizzo:\""+id_atto+"\"";
		
		var attoIndirizzoResults = search.luceneSearch(attoIndirizzoLuceneQuery);
		
		if(attoIndirizzoResults!=null && attoIndirizzoResults.length>0){
			
			//modifica del nodo AttoIndirizzo
			var attoFolderNode = attoIndirizzoResults[0];
			
			attoFolderNode.properties["crlatti:IdAttoIndirizzo"] = id_atto;
			attoFolderNode.properties["crlatti:IdLegislaturaAttoIndirizzo"] = id_legislatura;
			attoFolderNode.properties["crlatti:tipoAttoIndirizzo"] = tipo_atto;
			attoFolderNode.properties["crlatti:numeroAttoIndirizzo"] = numero_atto;
			attoFolderNode.properties["crlatti:oggettoAttoIndirizzo"] = oggetto_atto;
			attoFolderNode.save();
		
			//creazione o modifica dei firmatari
			
			var firmatariFolderXPathQuery = "*[@cm:name='Firmatari']";
			var firmatariFolderNode = attoFolderNode.childrenByXPath(firmatariFolderXPathQuery)[0];
			
			for each( var firmatario in xmlObject.firmatari.firmatario ) {
				
				// controllo sulla presenza del firmatario
				var nome_firmatario= firmatario.@nome_firmatario.toString();
				var cognome_firmatario = firmatario.@cognome_firmatario.toString();
				var firmatario_nome_cognome = nome_firmatario +" "+cognome_firmatario;
				
				//verifica l'esistenza del firmatrio
				var existFirmatarioXPathQuery = "*[@cm:name=\""+firmatario_nome_cognome+"\"]";
				var firmatarioEsistenteResults = firmatariFolderNode.childrenByXPath(existFirmatarioXPathQuery);
				var firmatarioNode = null;
				if(firmatarioEsistenteResults!=null && firmatarioEsistenteResults.length>0){
					firmatarioNode = firmatarioEsistenteResults[0];
				} else {
					firmatarioNode = firmatariFolderNode.createNode(firmatario_nome_cognome,"crlatti:firmatarioAttoIndirizzo");
					firmatarioNode.content = firmatario;
				}
				
				firmatarioNode.properties["crlatti:IdFirmatarioAttoIndirizzo"] = firmatario.@id_firmatario.toString();
				firmatarioNode.properties["crlatti:idGruppoFirmatarioAttoIndirizzo"] = firmatario.@id_gruppo_firmatario.toString();
				firmatarioNode.properties["crlatti:nomeFirmatarioAttoIndirizzo"] = nome_firmatario;
				firmatarioNode.properties["crlatti:cognomeFirmatarioAttoIndirizzo"] = cognome_firmatario;
				firmatarioNode.properties["crlatti:gruppoFirmatarioAttoIndirizzo"] = firmatario.@gruppo_firmatario.toString();
				
				firmatarioNode.save();
			}
		
		
			//verifica firmatari da cancellare
			var firmatariNelRepository = firmatariFolderNode.getChildAssocsByType("crlatti:firmatarioAttoIndirizzo");
				
			//query nel repository per capire se bisogna cancellare alcuni firmatari
			for(var z=0; z<firmatariNelRepository.length; z++){
				var trovato = false;
				var firmatarioNelRepository = firmatariNelRepository[z];
				
				for each( var firmatario in xmlObject.firmatari.firmatario ) {
					
					// controllo sulla presenza del firmatario
					var nome_firmatario= firmatario.@nome_firmatario.toString();
					var cognome_firmatario = firmatario.@cognome_firmatario.toString();
					var firmatario = nome_firmatario +" "+cognome_firmatario;
					
					if(""+firmatario+""==""+firmatarioNelRepository.name+""){
						trovato = true;
						break
					}
					
				}
				
				
				if(!trovato){
					firmatarioNelRepository.remove();
				}
			}
			

			// creazione o modifica dei collegamenti
		
			var collegamentiFolderXPathQuery = "*[@cm:name='Collegamenti']";
			var collegamentiFolderNode = attoFolderNode.childrenByXPath(collegamentiFolderXPathQuery)[0];
	
			for each( var collegamento in xmlObject.collegamenti.collegamento ) {
				
				var numero_atto_collegamento = collegamento.@numero_atto.toString();
				var tipo_atto_collegamento= collegamento.@tipo_atto.toString();
				var nome_collegamento = tipo_atto_collegamento + " " + numero_atto_collegamento;
				
				var legislaturaRepositoryPath = gestioneAttiPath + "/cm:"+search.ISO9075Encode(numero_legislatura);
				var collegamentoLuceneQuery = "PATH:\""+legislaturaRepositoryPath+"//*\"";
				
				if(checkIsNotNull(tipo_atto_collegamento)){
					if(tipo_atto_collegamento == "PDL") {
						type = "crlatti:attoPdl";	
					} else if(tipo_atto_collegamento == "INP") {
						type = "crlatti:attoInp";	
					} else if(tipo_atto_collegamento == "PAR") {
						type = "crlatti:attoPar";	
					} else if(tipo_atto_collegamento == "PDA") {
						type = "crlatti:attoPda";	
					} else if(tipo_atto_collegamento == "PLP") {
						type = "crlatti:attoPlp";	
					} else if(tipo_atto_collegamento == "PRE") {
						type = "crlatti:attoPre";	
					} else if(tipo_atto_collegamento == "REF") {
						type = "crlatti:attoRef";	
					} else if(tipo_atto_collegamento == "REL") {
						type = "crlatti:attoRel";	
					} else if(tipo_atto_collegamento == "EAC") {
						type = "crlatti:attoEac";	
					} else if(tipo_atto_collegamento == "REF") {
						type = "crlatti:attoRef";	
					} else if(tipo_atto_collegamento == "DOC") {
						type = "crlatti:attoDoc";	
					} else if(tipo_atto_collegamento == "ORG") {
						type = "crlatti:attoOrg";	
					}
				}
	
				collegamentoLuceneQuery += " AND TYPE:\""+type+"\"";
				collegamentoLuceneQuery += " AND @cm\\:name:\""+numero_atto_collegamento+"\"";
				
				
				var attoCollegatoResults = search.luceneSearch(collegamentoLuceneQuery);
				
				if(attoCollegatoResults!=null && attoCollegatoResults.length>0){
					
					var attoCollegato = attoCollegatoResults[0];
			
					//verifica l'esistenza del collegamento
					var existCollegamentoXPathQuery = "*[@cm:name='"+nome_collegamento+"']";
					var collegamentoEsistenteResults = collegamentiFolderNode.childrenByXPath(existCollegamentoXPathQuery);
					var collegamentoNode = null;
					if(collegamentoEsistenteResults!=null && collegamentoEsistenteResults.length>0){
						collegamentoNode = collegamentoEsistenteResults[0];
						creaAssociazione = false;
					} else {
						collegamentoNode = collegamentiFolderNode.createNode(nome_collegamento,"crlatti:collegamento");
						collegamentoNode.createAssociation(attoCollegato,"crlatti:attoAssociatoCollegamento");
						
						// creo collegamento nella direzione opposta: atto -> attoIndirizzo
						
						var collegamentiAttoXPathQuery = "*[@cm:name='Collegamenti']";
						var collegamentiAttoFolderNode = attoCollegato.childrenByXPath(collegamentiAttoXPathQuery)[0];
						
						var attiIndirizzoXPathQuery = "*[@cm:name='AttiIndirizzoSindacatoIspettivo']";
						var attiIndirizzoFolderNode = collegamentiAttoFolderNode.childrenByXPath(attiIndirizzoXPathQuery)[0];
						
						
						//verifica l'esistenza del collegamento all'interno del folder Collegamenti/Interni
						var existCollegamentoXPathQuery = "*[@cm:name='"+id_atto+"']";
						var collegamentoEsistenteResults = attiIndirizzoFolderNode.childrenByXPath(existCollegamentoXPathQuery);
						var collegamentoNode = null;
						
						if(collegamentoEsistenteResults!=null && collegamentoEsistenteResults.length>0){
							collegamentoNode = collegamentoEsistenteResults[0];
							
						} else {
							collegamentoNode = attiIndirizzoFolderNode.createNode(id_atto,"crlatti:collegamentoAttoIndirizzo");
							collegamentoNode.createAssociation(attoFolderNode,"crlatti:attoAssociatoCollegamentoAttiIndirizzo");
						}
					
						
					}
					
					collegamentoNode.save();
					
				
					
				}else{
					attiIndirizzoImportLogger.error("Richiesta operazione di CREATE per atto:"+tipo_atto+" "+numero_atto+". Non e' possibile creare il collegamento, atto "+tipo_atto_collegamento+" "+numero_atto_collegamento+" non presente presente nel repository");				
				}	
				
			}
			
			
			//verifica collegamenti da cancellare
			var collegamentiNelRepository = collegamentiFolderNode.getChildAssocsByType("crlatti:collegamento");
				
			//query nel repository per capire se bisogna cancellare alcuni firmatari
			for(var z=0; z<collegamentiNelRepository.length; z++){
				var trovato = false;
				var collegamentoNelRepository = collegamentiNelRepository[z];
				
				for each( var collegamento in xmlObject.collegamenti.collegamento ) {
					
					var numero_atto_collegamento = collegamento.@numero_atto.toString();
					var tipo_atto_collegamento= collegamento.@tipo_atto.toString();
					var nome_collegamento = tipo_atto_collegamento + " " + numero_atto_collegamento;
					
					if(""+nome_collegamento+""==""+collegamentoNelRepository.name+""){
						trovato = true;
						break
					}
					
				}
				
				
				if(!trovato){
					
					// rimuovo il collegamento atto -> atto indirizzo
					var attoCollegatoDaScollegare = collegamentoNelRepository.assocs["crlatti:attoAssociatoCollegamento"][0];
					
					var collegamentiAttoXPathQuery = "*[@cm:name='Collegamenti']";
					var collegamentiAttoFolderNode = attoCollegatoDaScollegare.childrenByXPath(collegamentiAttoXPathQuery)[0];
					
					var attiIndirizzoXPathQuery = "*[@cm:name='AttiIndirizzoSindacatoIspettivo']";
					var attiIndirizzoFolderNode = collegamentiAttoFolderNode.childrenByXPath(attiIndirizzoXPathQuery)[0];
					
					var existCollegamentoXPathQuery = "*[@cm:name='"+id_atto+"']";
					var collegamentoEsistenteResults = attiIndirizzoFolderNode.childrenByXPath(existCollegamentoXPathQuery);
					var collegamentoNode = null;
					
					if(collegamentoEsistenteResults!=null && collegamentoEsistenteResults.length>0){
						collegamentoNode = collegamentoEsistenteResults[0];
						collegamentoNode.remove();
						
					} 
					
					collegamentoNelRepository.remove();
				}
			}
			
		
			xmlFile.move(attiParsedFolderNode);
			attiIndirizzoImportLogger.info("Parsing eseguito per il file "+xmlFile.name+": operazione "+operazione+" su atto "+tipo_atto+" "+numero_atto);
			count++;
						
		} else {
			attiIndirizzoImportLogger.error("Richiesta operazione di UPDATE per atto con id: "+id_atto+" e tipo atto: "+tipo_atto+". L'atto non e' presente nel repository");
		}
		
	}else if (operazione == "DELETE"){
		
		var legislaturaRepositoryPath = gestioneAttiPath + "/cm:"+search.ISO9075Encode(numero_legislatura);
		var attoIndirizzoLuceneQuery = "PATH:\""+legislaturaRepositoryPath+"//*\"";
		attoIndirizzoLuceneQuery += " AND TYPE:\"crlatti:attoIndirizzo\"";
		attoIndirizzoLuceneQuery += " AND @crlatti\\:IdAttoIndirizzo:\""+id_atto+"\"";
		
				
		var attoResults = search.luceneSearch(attoIndirizzoLuceneQuery);
		
		if(attoResults!=null && attoResults.length>0){
                    
                    var attoFolderNode = attoResults[0];
			
                    // rimuovere i collegamenti negli atti

                    // ricerca di tutti i crlatti:collegamentoAttoIndirizzo con id_atto corrente

                    var collegamentiLegislaturaRepositoryPath = gestioneAttiPath + "/cm:"+search.ISO9075Encode(numero_legislatura);
                    var collAttoIndirizzoLuceneQuery = "PATH:\""+collegamentiLegislaturaRepositoryPath+"//*\"";
                    collAttoIndirizzoLuceneQuery += " AND TYPE:\"crlatti:collegamentoAttoIndirizzo\"";
                    collAttoIndirizzoLuceneQuery += " AND @cm\\:name:\""+id_atto+"\"";

                    var collAttoResults = search.luceneSearch(collAttoIndirizzoLuceneQuery);

                    for(var j=0; j < collAttoResults.length; j++){

                            collAttoResults[j].remove();

                    }


                    // rimozione dell'atto dal repository

                    attoFolderNode.remove();
                        
		} else {
		
                    attiIndirizzoImportLogger.error("Richiesta operazione di DELETE per atto con id: "+id_atto+" e tipo atto: "+tipo_atto+". L'atto non e' presente nel repository");			
	
		}
		
		xmlFile.move(attiParsedFolderNode);
		attiIndirizzoImportLogger.info("Parsing eseguito per il file "+xmlFile.name+": operazione "+operazione+" su atto "+tipo_atto+" "+numero_atto);
		count++;
		
	}
	
	
	
	

			

	
}


attiIndirizzoImportLogger.info("Importazione Atti terminata. Elementi importati: "+count);









