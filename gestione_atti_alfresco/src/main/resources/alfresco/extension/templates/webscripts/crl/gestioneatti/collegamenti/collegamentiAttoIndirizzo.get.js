<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">
var idAtto = args.id;

var collegamenti = null;
if(checkIsNotNull(idAtto)){
	var attoNode = utils.getNodeFromString(idAtto);

	var collegamentiXPathQuery = "*[@cm:name='Collegamenti']";
	var collegamentiFolderNode = attoNode.childrenByXPath(collegamentiXPathQuery)[0];
	
	var attoIndirizzoXPathQuery = "*[@cm:name='AttiIndirizzoSindacatoIspettivo']";
	var attoIndirizzoFolderNode = collegamentiFolderNode.childrenByXPath(attoIndirizzoXPathQuery)[0];
	
	collegamenti = attoIndirizzoFolderNode.getChildAssocsByType("crlatti:collegamentoAttoIndirizzo");
} else {
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
}
model.collegamenti = collegamenti;