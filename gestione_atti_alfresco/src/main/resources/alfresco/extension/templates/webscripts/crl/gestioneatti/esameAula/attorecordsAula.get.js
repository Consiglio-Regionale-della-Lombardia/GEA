<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">
var idAtto = args.id;
var commissione = args.commissione;
var passaggio = args.passaggio;


var records = null;
if(checkIsNotNull(idAtto)
		&& checkIsNotNull(passaggio)){
	
	var attoFolderNode = utils.getNodeFromString(idAtto);
	
	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoFolderNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='"+passaggio+"']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
		
	var aulaXPathQuery = "*[@cm:name='Aula']";
	var aulaFolderNode = passaggioFolderNode.childrenByXPath(aulaFolderNode)[0];

		
	var testiFolderNode = aulaFolderNode.childrenByXPath("*[@cm:name='Testi']")[0];
	var records = testiFolderNode.getChildAssocsByType("crlatti:testo");
	
} else {
	status.code = 400;
	status.message = "id atto, passaggio non valorizzato";
	status.redirect = true;
}
model.records = records;