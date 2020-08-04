<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var id = args.id;
var firmatari = null;

if(checkIsNotNull(id)){
	var attoFolderNode = utils.getNodeFromString(id);
	if(attoFolderNode.hasAspect("crlatti:firmatariAspect")){
		var childrenXPathQuery = "*[@cm:name='Firmatari']";
		var firmatariFolderNode = attoFolderNode.childrenByXPath(childrenXPathQuery)[0];
		firmatari = firmatariFolderNode.getChildAssocsByType("crlatti:firmatario");
	}
} else {
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
}
model.firmatari = firmatari;