<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">


crlUtility.sudo(importaAtto, "admin");


function importaAtto(){

	//Regole da impostare con uno scope attivo solo su crlatti:atto

	var legislatura = document.properties["crlatti:legislatura"];
	var numeroAtto = document.properties["crlatti:numeroAtto"];
	var estensioneAtto = document.properties["crlatti:estensioneAtto"];
	var idProtocollo = document.properties["crlatti:idProtocollo"];
	
	var tipologia = document.properties["crlatti:tipologia"];
	var numeroProtocollo = document.properties["crlatti:numeroProtocollo"];
	var numeroRepertorio = document.properties["crlatti:numeroRepertorio"];
	var dataRepertorio = document.properties["crlatti:dataRepertorio"];
	var classificazione = document.properties["crlatti:classificazione"];
	var oggetto = document.properties["crlatti:oggetto"];
	
	var tipoIniziativa = document.properties["crlatti:tipoIniziativa"];
	var dataIniziativa = document.properties["crlatti:dataIniziativa"];
	var descrizioneIniziativa = document.properties["crlatti:descrizioneIniziativa"];
	
	var numeroDgr = document.properties["crlatti:numeroDgr"];
	var dataDgr = document.properties["crlatti:dataDgr"];
	
	var assegnazione = document.properties["crlatti:assegnazione"];
	var firmatari = document.properties["crlatti:firmatari"];
	
	var urlFascicolo = document.properties["crlatti:urlFascicolo"];

    //MG (rimuovere dopo che è stata confermata la risoluzione atti duplicati)
    //   var dataImportazione = new Date(2015,6,1);
	var dataImportazione = new Date();
	var mese = dataImportazione.getMonth() + 1;
	var anno = dataImportazione.getFullYear();
	
	var type = document.typeShort;
	
	var tipoAtto = type.substring(12).toUpperCase();
	
	var gestioneAttiPath = "/app:company_home"+
	"/cm:"+search.ISO9075Encode("CRL")+
	"/cm:"+search.ISO9075Encode("Gestione Atti");
	
	if(type=="crlatti:attoEac"){
		
	
	} else if (type=="crlatti:attoMis"){
		
		
		
	} else if(checkIsNotNull(legislatura)
			&& checkIsNotNull(numeroAtto)
			&& checkIsNotNull(idProtocollo)){
		
		//creazione dello spazio legislatura
		var legislaturaPath = gestioneAttiPath + "/cm:"+search.ISO9075Encode(legislatura);
		var legislaturaLuceneQuery = "PATH:\""+legislaturaPath+"\"";
		var legislaturaResults = search.luceneSearch(legislaturaLuceneQuery);
		
		var legislaturaFolderNode = null;
		if(legislaturaResults!=null && legislaturaResults.length>0){
			legislaturaFolderNode = legislaturaResults[0];
		} else {
			var gestioneAttiLuceneQuery = "PATH:\""+gestioneAttiPath+"\"";
			var gestioneAttiFolderNode = search.luceneSearch(gestioneAttiLuceneQuery)[0];
			legislaturaFolderNode = gestioneAttiFolderNode.createFolder(legislatura);
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
		
	
		// creazione spazio "tipo" con copia dello space template ContenitoreAtto. 
		// Nello space sono presenti le regole necessarie alla creazione degli xml di export
		// verso il sistema di gestione Atti Indirizzo
		var tipoPath = mesePath + "/cm:" + search.ISO9075Encode(tipoAtto);
		var tipoLuceneQuery = "PATH:\""+tipoPath+"\"";
		var tipoResults = search.luceneSearch(tipoLuceneQuery);
		var tipoFolderNode = null;
		if(tipoResults!=null && tipoResults.length>0){
			tipoFolderNode = tipoResults[0];
		} else {
			var contenitoreAttoSpaceTemplateQuery = "PATH:\"/app:company_home/app:dictionary/app:space_templates/cm:ContenitoreAtto\"";
			var contenitoreAttoSpaceTemplateNode = search.luceneSearch(contenitoreAttoSpaceTemplateQuery)[0];
			tipoFolderNode = contenitoreAttoSpaceTemplateNode.copy(meseFolderNode,true);
			tipoFolderNode.name = tipoAtto;
			tipoFolderNode.save();
		}
		
		//verifica esistenza del folder dell'atto
		var attoPath = tipoPath + "/cm:" + search.ISO9075Encode(numeroAtto+""+estensioneAtto);
		//var attoLuceneQuery = "PATH:\""+attoPath+"\""; // TYPE:"crlatti:attoPdl" AND @crlatti\:numeroAtto:"238" "
        var attoLuceneQuery = ' +PATH:"'+legislaturaPath+'//*" +TYPE:"'+document.typeShort+'" +@crlAtti\\:numeroAtto:"'+numeroAtto+'" +@crlAtti\\:estensioneAtto:"'+estensioneAtto+'" ';
        var attoResults = search.luceneSearch(attoLuceneQuery);

		//var esisteAttoLuceneQuery = "TYPE:\"crlatti:atto\" AND @crlatti\\:idProtocollo:\""+idProtocollo+"\" AND @cm\\:name:\""+numeroAtto+"\"";
		
		var attoFolderNode = null;
		if(attoResults!=null && attoResults.length>0){
			//atto presente
			attoFolderNode = attoResults[0];
			attoFolderNode.properties["crlatti:legislatura"] = legislatura;
			attoFolderNode.properties["crlatti:numeroAtto"] = numeroAtto;
			attoFolderNode.properties["crlatti:tipologia"] = tipologia;
			attoFolderNode.properties["crlatti:anno"] = anno;
			attoFolderNode.properties["crlatti:idProtocollo"] = idProtocollo;
			attoFolderNode.properties["crlatti:numeroProtocollo"] = numeroProtocollo;
			attoFolderNode.properties["crlatti:numeroRepertorio"] = numeroRepertorio;
			attoFolderNode.properties["crlatti:classificazione"] = classificazione;
			attoFolderNode.properties["crlatti:oggetto"] = oggetto;
			attoFolderNode.properties["crlatti:tipoIniziativa"] = tipoIniziativa;
			attoFolderNode.properties["crlatti:descrizioneIniziativa"] = descrizioneIniziativa;
			attoFolderNode.properties["crlatti:assegnazione"] = assegnazione;
			attoFolderNode.properties["crlatti:dataIniziativa"] = dataIniziativa;
			attoFolderNode.properties["crlatti:dataRepertorio"] = dataRepertorio;
			attoFolderNode.properties["crlatti:urlFascicolo"] = urlFascicolo;
			
			if(attoFolderNode.hasAspect("crlatti:dgr")){
				attoFolderNode.properties["crlatti:numeroDgr"] = numeroDgr;
				attoFolderNode.properties["crlatti:dataDgr"] = dataDgr;
			}
			
			if(attoFolderNode.hasAspect("crlatti:firmatariAspect")){
				// i firmatari verranno infasati con la rule sulla cartella firmatari
				// attoFolderNode.properties["crlatti:firmatari"] = document.properties["crlatti:firmatari"];
	
				var childrenXPathQuery = "*[@cm:name='Firmatari']";
				var firmatariFolderNode = attoFolderNode.childrenByXPath(childrenXPathQuery)[0];
				
				// elimino i firmatari esistenti
				for each (firmatarioEsistente in firmatariFolderNode.children){
                                    if(firmatarioEsistente.isDocument) {
					firmatarioEsistente.remove();
                                    }
				}
	
				var listaFirmatari = firmatari;
				
				var childrenXPathQuery = "*[@cm:name=\""+nomeFirmatario+"\"]";
				var firmatarioNode = firmatariFolderNode.childrenByXPath(childrenXPathQuery);
				
				// inserisco i nuovi firmatari
				if(listaFirmatari!=null) {
					for(var k=0; k < listaFirmatari.length; k++) {
						
						var nomeFirmatario = listaFirmatari[k];
						
						firmatarioNode = firmatariFolderNode.createNode(nomeFirmatario,"crlatti:firmatario");	
		
						// inserimento proprietà per l'ordinamento 01,02,03 ecc...
						if(k<10) {
							firmatarioNode.properties["crlatti:numeroOrdinamento"] = "0"+k+"";
						}else{
							firmatarioNode.properties["crlatti:numeroOrdinamento"] = ""+k+"";
						}
						
						// il primo firmatario della lista è contrassegnato come primo firmatario
						if(k==0){
							firmatarioNode.properties["crlatti:isPrimoFirmatario"] = true;
						}
						
						// se non setto la proprietà content Share da un errore nella visualizzazione
						firmatarioNode.content = "";
							
						firmatarioNode.save();
						
					}
				}
	
			}
			
			attoFolderNode.save();
			
			if(attoFolderNode.hasAspect("crlatti:importatoDaProtocollo")){
				attoFolderNode.removeAspect("crlatti:importatoDaProtocollo");
			}
			
				
			document.remove();
			
			protocolloLogger.info("Atto importato correttamente. Operazione: Modifica atto esistente - Atto numero:"+numeroAtto+" idProtocollo:"+idProtocollo);	
			
		} else {
			//creazione del nodo del nuovo atto
			document.move(tipoFolderNode);
                        
                        // FIX PROTOCOLLO
                        var attoFolderName = document.name;
                        document.name = attoFolderName.substring(0, attoFolderName.lastIndexOf("_"));
                        document.save();
                        
			if(document.hasAspect("crlatti:importatoDaProtocollo")){
				document.removeAspect("crlatti:importatoDaProtocollo");
			}
			

			document.addAspect("crlatti:attiIndirizzoAspect");
			
			
			protocolloLogger.info("Atto importato correttamente. Operazione: Creazione nuovo atto - Atto numero:"+numeroAtto+" idProtocollo:"+idProtocollo);	
		}
		
		
	
	
	}
}