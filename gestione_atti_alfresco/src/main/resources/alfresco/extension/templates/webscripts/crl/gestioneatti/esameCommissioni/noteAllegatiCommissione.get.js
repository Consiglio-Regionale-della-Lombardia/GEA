<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">
var id = args.id;
var commissione = args.commissione;
var passaggio = args.passaggio;
var attoNode = null;
var links = null;
var noteNode = null;

if(checkIsNotNull(id)
		&& checkIsNotNull(commissione)
		&& checkIsNotNull(passaggio)){
	
	var attoNode = utils.getNodeFromString(id);

	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='"+passaggio+"']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
	
	
	var commissioniXPathQuery = "*[@cm:name='Commissioni']";
	var commissioniFolderNode = passaggioFolderNode.childrenByXPath(commissioniXPathQuery)[0];
	
	var commissioneXPathQuery = "*[@cm:name=\""+commissione+"\"]";
	var commissioneFolder = commissioniFolderNode.childrenByXPath(commissioneXPathQuery)[0];
	
	
	//lettura delle note generali
	var noteXPathQuery = "*[@cm:name='Note Generali.txt']";
	var noteResults = commissioneFolder.childrenByXPath(noteXPathQuery);
	if(noteResults!=null && noteResults.length>0){
		noteNode = noteResults[0];
	}
	
	//lettura links
	var linksFolderXPathQuery = "*[@cm:name='Links']";
	var linksFolderNode = commissioneFolder.childrenByXPath(linksFolderXPathQuery)[0];
	links = linksFolderNode.getChildAssocsByType("crlatti:link");
} else {
	status.code = 400;
	status.message = "id atto, commissione e passaggio non valorizzati";
	status.redirect = true;
}
model.atto = attoNode;
model.links = links;
model.note = noteNode;
model.commissione = commissioneFolder;
model.passaggio = passaggioFolderNode;