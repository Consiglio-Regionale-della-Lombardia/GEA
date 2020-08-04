<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var id = args.id;
var attoFolderNode = null;
var notePresentazioneAssegnazioneNode = null;
var pareri = null;
var consultazioni = null;
var links = null;
var relatoriAtto = null;

if(checkIsNotNull(id)){
	attoFolderNode = utils.getNodeFromString(id);
	
	//lettura delle note generali
	var noteXPathQuery = "*[@cm:name='Note Generali.txt']";
	var noteResults = attoFolderNode.childrenByXPath(noteXPathQuery);
	if(noteResults!=null && noteResults.length>0){
		notePresentazioneAssegnazioneNode = noteResults[0];
	}
	
	//lettura links
	var linksFolderXPathQuery = "*[@cm:name='Links']";
	var linksFolderNode = attoFolderNode.childrenByXPath(linksFolderXPathQuery)[0];
	if(linksFolderNode!=null && linksFolderNode!=undefined){
		links = linksFolderNode.getChildAssocsByType("crlatti:link");
	}
	
	//lettura pareri
	var pareriFolderXPathQuery = "*[@cm:name='Pareri']";
	var pareriFolderNode = attoFolderNode.childrenByXPath(pareriFolderXPathQuery)[0];
	if(pareriFolderNode!=null && pareriFolderNode!=undefined){
		pareri = pareriFolderNode.getChildAssocsByType("crlatti:parere");
	}
	
	
	//lettura pareri
	var consultazioniFolderXPathQuery = "*[@cm:name='Consultazioni']";
	var consultazioniFolderNode = attoFolderNode.childrenByXPath(consultazioniFolderXPathQuery)[0];
	if(consultazioniFolderNode!=null && consultazioniFolderNode!=undefined){
		consultazioni = consultazioniFolderNode.getChildAssocsByType("crlatti:consultazione");
	}
	

	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoFolderNode.childrenByXPath(passaggiXPathQuery)[0];
	var passaggi = null;
	if(passaggiFolderNode!=null && passaggiFolderNode!=undefined){
		passaggi = passaggiFolderNode.getChildAssocsByType("cm:folder");
	}
	
	// gestione relatori atto ORG
	var relatoriAttoXPathQuery = "*[@cm:name='RelatoriAtto']";
	var relatoriAttoFolderNode = attoFolderNode.childrenByXPath(relatoriAttoXPathQuery)[0];
	
	if(relatoriAttoFolderNode!=null && relatoriAttoFolderNode!=undefined){
		relatoriAtto = relatoriAttoFolderNode.getChildAssocsByType("crlatti:relatore");
	}
	
	
	var tipoAtto = attoFolderNode.typeShort.substring(12).toUpperCase();
	var numeroAtto = attoFolderNode.name;
	var legislatura = attoFolderNode.properties["crlatti:legislatura"];
	
	
	var sedutePath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Sedute") + "//*";

	var luceneQuery = "PATH:\""+sedutePath+"\" AND TYPE:\"crlatti:attoTrattatoODG\" AND @crlatti\\:numeroAttoTrattatoODG:\""+numeroAtto+"\" AND @crlatti\\:tipoAttoTrattatoODG:\""+tipoAtto+"\"";
	var seduteResults = search.luceneSearch(luceneQuery);
	
	var seduteResultsObj = new Array();

	for(var i=0; i< seduteResults.length; i++){
		
		var sedutaResult = seduteResults[i].parent.parent;
		var sedutaResultObj = new Object();
                
                if(legislatura == sedutaResult.properties["crlatti:legislaturaSedutaODG"]) {
		
                    sedutaResultObj.idSeduta = sedutaResult.nodeRef.toString();

                    // nome della commissione o dell'aula
                    sedutaResultObj.nomeOrgano = sedutaResult.parent.parent.parent.name;
                    sedutaResultObj.dataSeduta = sedutaResult.properties["crlatti:dataSedutaSedutaODG"];
                    sedutaResultObj.numVerbale = sedutaResult.properties["crlatti:numVerbaleSedutaODG"];


                    var linksFolderXpathQuery = "*[@cm:name='Links']";
                    var linksFolderNode = sedutaResult.childrenByXPath(linksFolderXpathQuery)[0];
                    sedutaResultObj.links = linksFolderNode.getChildAssocsByType("crlatti:link");

                   sedutaResultObj.discusso = seduteResults[i].properties["crlatti:discussoAttoTrattatoODG"];

                    var verbaliFolderXpathQuery = "*[@cm:name='Verbali']";
                    var verbaliFolderNode = sedutaResult.childrenByXPath(verbaliFolderXpathQuery)[0];
                    var verbali = verbaliFolderNode.getChildAssocsByType("crlatti:allegato");

                    if(verbali != null && verbali.length > 0) {

                        var verbale = verbali[0];

                        sedutaResultObj.idVerbale = verbale.nodeRef.toString();
                        sedutaResultObj.nomeVerbale = verbale.name;
                        sedutaResultObj.mimetypeVerbale = verbale.mimetype;
                        sedutaResultObj.pubblico = verbale.properties["crlatti:pubblico"];
                        sedutaResultObj.downloadUrl = verbale.downloadUrl;  

                    }

                    var tipoAtto = attoFolderNode.typeShort;
                    if(!tipoAtto.endsWith("Mis")){
	                    var passaggioFolderNode = getLastPassaggio(attoFolderNode);
	
	                    if(sedutaResultObj.nomeOrgano!="Aula" && sedutaResultObj.nomeOrgano!="ServizioCommissioni"){
	
	
	                            var commissioniFolderXpathQuery = "*[@cm:name='Commissioni']";
	                            var commissioniFolderNode = passaggioFolderNode.childrenByXPath(commissioniFolderXpathQuery)[0];
	
	                            var commissioneFolderXpathQuery = "*[@cm:name=\""+sedutaResultObj.nomeOrgano+"\"]";
	                            var commissioneFolderNode = commissioniFolderNode.childrenByXPath(commissioneFolderXpathQuery)[0];
	
	                            // Workaround nel caso in cui la commissione aggiunga all'odg un atto per cui non ha nessun ruolo
	                            if(commissioneFolderNode!=null){
	                                    sedutaResultObj.nomeOrgano = commissioneFolderNode.properties["crlatti:ruoloCommissione"] + " - " + sedutaResultObj.nomeOrgano;
	                            }
	                    }

	                    seduteResultsObj.push(sedutaResultObj);

                        //scrl-407
                        if (sedutaResultObj.nomeOrgano=="Aula"){
                            var aulaFolderXpathQuery = "*[@cm:name='Aula']";
                            var aulaFolderNode = passaggioFolderNode.childrenByXPath(aulaFolderXpathQuery)[0];

                            var allegatoAulaQuery = "*[@cm:name='Allegati']/*[@crlatti:tipologia='allegato_aula']"
                            var allegatoAula = aulaFolderNode.childrenByXPath(allegatoAulaQuery)[0];
                            if (allegatoAula){
                                sedutaResultObj.idVerbale = allegatoAula.nodeRef.toString();
                                sedutaResultObj.nomeVerbale = allegatoAula.name;
                                sedutaResultObj.mimetypeVerbale = allegatoAula.mimetype;
                                sedutaResultObj.pubblico = allegatoAula.properties["crlatti:pubblico"];
                                sedutaResultObj.downloadUrl = allegatoAula.downloadUrl;
                            }
                        }
                    }
                }
	}
	
	
	// TODO ordinare seduteResultsObj in ordine crescente
	
	
} else {
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
}

//reperimento tipoAtto
var typeQName = attoFolderNode.typeShort;
var tipoAtto = typeQName.substring(12).toUpperCase();

model.notePresentazioneAssegnazione = notePresentazioneAssegnazioneNode;
model.links = links;
model.organismiStatutari = pareri;
model.consultazioni = consultazioni;
model.passaggi = passaggi;
model.atto = attoFolderNode;
model.tipoAtto = tipoAtto;
model.relatoriAtto = relatoriAtto;
model.seduteAtto = seduteResultsObj;