<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var nodeRefAtto = "";
var tipologia = "";
var tipologiaTesto = "";
var pubblico = false;
var pubblicoOpendata = false;
var filename = "";
var provenienza = "";
var passaggio ="";
var content = null;

for each (field in formdata.fields) {
  if (field.name == "id") {
    nodeRefAtto = field.value;
  } else if(field.name == "tipologia"){
	tipologia = field.value;
  } else if(field.name == "tipologiaTesto"){
	tipologiaTesto = field.value;
  } else if(field.name == "pubblico"){
	pubblico = field.value;
  } else if(field.name == "pubblicoOpendata"){
	pubblicoOpendata = field.value;
  } else if(field.name == "commissione"){
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
	
	var attoRecordNode = null;
	var testiXpathQuery = "*[@cm:name='Testi']";
	var testiFolderNode = commissioneUtenteFolderNode.childrenByXPath(testiXpathQuery)[0];
		
	var testoXpathQuery = "*[@cm:name='"+filename+"']";
	var attoResults = testiFolderNode.childrenByXPath(testoXpathQuery);
	
	if(attoResults!=null && attoResults.length>0){
		
		var attoEsistente = attoResults[0];
		attoEsistente.properties.content.write(content);
		attoEsistente.properties.content.setEncoding("UTF-8");
		attoEsistente.properties.content.guessMimetype(filename);
		attoEsistente.save();
		attoRecordNode = attoEsistente;
	
	} 
	
	if (attoRecordNode == null){
	
		
		
		// creazione binario
		attoRecordNode = testiFolderNode.createFile(filename);
		attoRecordNode.specializeType("crlatti:testo");
		attoRecordNode.properties["crlatti:tipologia"] = tipologia;
		attoRecordNode.properties["crlatti:tipologiaTesto"] = tipologiaTesto;
		attoRecordNode.properties["crlatti:pubblico"] = pubblico;
		if (pubblicoOpendata) {
			var attiPubbliciOpendata=attoNode.childrenByXPath("*//*[@crlatti:pubblicoOpendata='true']");
			for each (attoPubblicoOpendata in attiPubbliciOpendata) {
				if (attoPubblicoOpendata.properties["crlatti:tipologiaTesto"] != null && attoPubblicoOpendata.properties["crlatti:tipologiaTesto"] == tipologiaTesto) {
					attoPubblicoOpendata.properties["crlatti:pubblicoOpendata"]=false;
					attoPubblicoOpendata.save();
				}
			}
			attoRecordNode.properties["crlatti:pubblicoOpendata"] = pubblicoOpendata;
		}
		attoRecordNode.properties["crlatti:provenienza"] = provenienza;
		attoRecordNode.properties.content.write(content);
		attoRecordNode.properties.content.setEncoding("UTF-8");
		attoRecordNode.properties.content.guessMimetype(filename);
		attoRecordNode.save();
		
		
	}
	model.attoRecord = attoRecordNode;	
}