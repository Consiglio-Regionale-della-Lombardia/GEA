<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">
var id = args.id;
var attoNode = null;
var firmatari = null;
if(checkIsNotNull(id)){
	var attoNode = utils.getNodeFromString(id);
	var firmatariXPathQuery = "*[@cm:name='Firmatari']";
	var firmatariFolderNode = attoNode.childrenByXPath(firmatariXPathQuery)[0];
	firmatari = firmatariFolderNode.getChildAssocsByType("crlatti:firmatario");
} else {
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
}
model.atto = attoNode;
model.firmatari = firmatari;