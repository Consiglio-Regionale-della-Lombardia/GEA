<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">
var id = args.id;
var records = null;
if(checkIsNotNull(id)){
	var attoFolderNode = utils.getNodeFromString(id);
	var testiFolderNode = attoFolderNode.childrenByXPath("*[@cm:name='Testi']")[0];
	var records = testiFolderNode.getChildAssocsByType("crlatti:testo");
} else {
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
}
model.records = records;