<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">
var idAtto = args.id;
var commissione = args.commissione;
var passaggio = args.passaggio;


var records = null;
if(checkIsNotNull(idAtto)
		&& checkIsNotNull(commissione)
		&& checkIsNotNull(passaggio)){
	
	var attoFolderNode = utils.getNodeFromString(idAtto);
	
	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoFolderNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='"+passaggio+"']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
		
	//cerco la commissione di riferimento dell'utente corrente
	var commissioniXPathQuery = "*[@cm:name='Commissioni']";
	var commissioniFolderNode = passaggioFolderNode.childrenByXPath(commissioniXPathQuery)[0];

	var commissioneUtenteXPathQuery = "*[@cm:name=\""+commissione+"\"]";
	var commissioneFolderNode = commissioniFolderNode.childrenByXPath(commissioneUtenteXPathQuery)[0];
	
	
	var testiFolderNode = commissioneFolderNode.childrenByXPath("*[@cm:name='Testi']")[0];
	var records = testiFolderNode.getChildAssocsByType("crlatti:testo");

} else {
	status.code = 400;
	status.message = "id atto, passaggio o commissione non valorizzato";
	status.redirect = true;
}
model.records = records;