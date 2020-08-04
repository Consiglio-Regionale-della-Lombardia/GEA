<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var nodeRefAtto = "";
var tipologia = "";
var pubblico = false;
var filename = "";
var provenienza = "";
var passaggio ="";
var content = null;

for each (field in formdata.fields) {
  if (field.name == "id") {
    nodeRefAtto = field.value;
  } else if(field.name == "tipologia"){
	tipologia = field.value;
  } else if(field.name == "pubblico"){
	pubblico = field.value;
  } else if(field.name == "provenienza"){
	provenienza = field.value;
  } else if(field.name == "passaggio"){
	passaggio = field.value;
  } else if (field.name == "file" && field.isFile) {
    filename = field.filename;
    content = field.content;
  }
}

if(nodeRefAtto == ""){
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
} else {
	var attoNode = utils.getNodeFromString(nodeRefAtto);
	
	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='"+passaggio+"']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
	
	//cerco la commissione di riferimento dell'utente corrente
	var commissioniXPathQuery = "*[@cm:name='Commissioni']";
	var commissioniFolderNode = passaggioFolderNode.childrenByXPath(commissioniXPathQuery)[0];

	var commissioneUtenteXPathQuery = "*[@cm:name=\""+provenienza+"\"]";
	var commissioneUtenteFolderNode = commissioniFolderNode.childrenByXPath(commissioneUtenteXPathQuery)[0];
	
	var allegatoNode = null;
	var allegatiXpathQuery = "*[@cm:name='Allegati']";
	var allegatiSpace = commissioneUtenteFolderNode.childrenByXPath(allegatiXpathQuery)[0];
		
	var allegatoXpathQuery = "*[@cm:name='"+filename+"']";
	var allegatoResults = allegatiSpace.childrenByXPath(allegatoXpathQuery);
	
	if(allegatoResults!=null && allegatoResults.length>0){
		var allegatoEsistente = allegatoResults[0];
		allegatoEsistente.properties.content.write(content);
		allegatoEsistente.properties.content.setEncoding("UTF-8");
		allegatoEsistente.properties.content.guessMimetype(filename);
		allegatoEsistente.save();
		allegatoNode = allegatoEsistente;
	}

	if(allegatoNode == null) {
		// creazione binario
		allegatoNode = allegatiSpace.createFile(filename);
		allegatoNode.specializeType("crlatti:allegato");
		allegatoNode.properties["crlatti:tipologia"] = tipologia;
		allegatoNode.properties["crlatti:pubblico"] = pubblico;
		allegatoNode.properties["crlatti:provenienza"] = provenienza;
		
		allegatoNode.properties.content.write(content);
		allegatoNode.properties.content.setEncoding("UTF-8");
		allegatoNode.properties.content.guessMimetype(filename);
		allegatoNode.save();
	}
	
	model.allegato = allegatoNode;	
}