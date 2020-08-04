<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var nodeRefAllegato = args.id;

if(checkIsNull(nodeRefAllegato)){
	status.code = 400;
	status.message = "id allegato non valorizzato";
	status.redirect = true;
} else {
	
	var allegatoNode = utils.getNodeFromString(nodeRefAllegato);
	var allegatiFolderNode = allegatoNode.parent;
	var attoFolderNode = allegatiFolderNode.parent;
	
	var testiXPathQuery = "*[@cm:name='Testi']";
	var filename = allegatoNode.name;
	var recordXPathQuery = "*[@cm:name='"+filename+"']";
	
	var testiFolderNode = attoFolderNode.childrenByXPath(testiXPathQuery)[0];
	var allegatoResults = testiFolderNode.childrenByXPath(recordXPathQuery);
	
	var attoRecordNode = null;
	
	//verifica dell'esistenza di un testo con lo stesso nome dell'allegato
	if(allegatoResults!=null && allegatoResults.length>0){
		//esiste un testo con lo stesso nome dell'allegato che si sta per spostare: aggiornamento del solo contenuto
		attoRecordNode = allegatoResults[0];
	} else {
		//non esiste alcun testo con lo stesso nome dell'allegato
		// creazione binario
		attoRecordNode = testiFolderNode.createFile(filename);
		attoRecordNode.specializeType("crlatti:testo");
	}
	
	//prelevamento properties dall'allegato
	var tipologia = allegatoNode.properties["crlatti:tipologia"];
	var pubblico = allegatoNode.properties["crlatti:pubblico"];
	var provenienza = allegatoNode.properties["crlatti:provenienza"];
	var content = allegatoNode.properties.content;
	
	//scrittura properties all'interno del nodo testo
	attoRecordNode.properties["crlatti:tipologia"] = "testo_atto";
	attoRecordNode.properties["crlatti:pubblico"] = pubblico;
	attoRecordNode.properties["crlatti:provenienza"] = provenienza;
	attoRecordNode.properties.content.write(content);
	attoRecordNode.properties.content.setEncoding("UTF-8");
	attoRecordNode.properties.content.guessMimetype(filename);
	attoRecordNode.save();
	
	//rimozione del vecchio allegato che adesso �� presente come testo dell'atto
	allegatoNode.remove();

	model.attoRecord = attoRecordNode;
}