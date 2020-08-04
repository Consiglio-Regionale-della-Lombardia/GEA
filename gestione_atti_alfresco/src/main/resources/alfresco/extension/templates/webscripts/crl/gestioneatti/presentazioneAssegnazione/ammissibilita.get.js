<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">
var id = args.id;
var attoNode = null;
if(checkIsNotNull(id)){
	var attoNode = utils.getNodeFromString(id);
} else {
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
}
model.atto = attoNode;