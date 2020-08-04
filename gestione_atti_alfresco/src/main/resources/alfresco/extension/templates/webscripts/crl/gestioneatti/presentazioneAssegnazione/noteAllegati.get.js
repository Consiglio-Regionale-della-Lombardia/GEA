<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">
var id = args.id;
var attoNode = null;
var links = null;
var noteNode = null;
if(checkIsNotNull(id)){
	var attoNode = utils.getNodeFromString(id);
	
	//lettura delle note generali
	var noteXPathQuery = "*[@cm:name='Note Generali.txt']";
	var noteResults = attoNode.childrenByXPath(noteXPathQuery);
	if(noteResults!=null && noteResults.length>0){
		noteNode = noteResults[0];
	}
	
	//lettura links
	var linksFolderXPathQuery = "*[@cm:name='Links']";
	var linksFolderNode = attoNode.childrenByXPath(linksFolderXPathQuery)[0];
	links = linksFolderNode.getChildAssocsByType("crlatti:link");
} else {
	status.code = 400;
	status.message = "id atto non valorizzato";
	status.redirect = true;
}
model.atto = attoNode;
model.links = links;
model.note = noteNode;