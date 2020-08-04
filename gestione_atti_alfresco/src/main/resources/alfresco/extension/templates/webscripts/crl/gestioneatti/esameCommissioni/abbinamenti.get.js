<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">
var idAtto = args.id;
var passaggio = args.passaggio;

var abbinamenti = null;
if(checkIsNotNull(idAtto)
		&& checkIsNotNull(passaggio)){
	var attoNode = utils.getNodeFromString(idAtto);
	
	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='"+passaggio+"']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
	
	var abbinamentiFolderXpathQuery = "*[@cm:name='Abbinamenti']";
	var abbinamentiFolderNode = passaggioFolderNode.childrenByXPath(abbinamentiFolderXpathQuery)[0];
	abbinamenti = abbinamentiFolderNode.getChildAssocsByType("crlatti:abbinamento");
} else {
	status.code = 400;
	status.message = "id atto e pasaggio non valorizzati";
	status.redirect = true;
}
model.abbinamenti = abbinamenti;
model.atto = attoNode;