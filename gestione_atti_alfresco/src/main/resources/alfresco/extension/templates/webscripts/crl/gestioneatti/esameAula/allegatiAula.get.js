<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">
var idAtto = args.idAtto;
var tipologiaAllegati = args.tipologiaAllegati;
var passaggio = args.passaggio;

var allegati = null;
if(checkIsNotNull(idAtto)
		&& checkIsNotNull(passaggio)){
	
	var attoFolderNode = utils.getNodeFromString(idAtto);
	
	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoFolderNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='"+passaggio+"']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
		
	
	var aulaXPathQuery = "*[@cm:name='Aula']";
	var aulaFolderNode = passaggioFolderNode.childrenByXPath(aulaXPathQuery)[0];

	
	var allegatiFolderXpathQuery = "*[@cm:name='Allegati']";
	var allegatiFolderNode = aulaFolderNode.childrenByXPath(allegatiFolderXpathQuery)[0];
	
	if(checkIsNotNull(tipologiaAllegati)){
		var allegatiDiUnaTipologiaXPathQuery = "*[@crlatti:tipologia='"+tipologiaAllegati+"']";
		allegati = allegatiFolderNode.childrenByXPath(allegatiDiUnaTipologiaXPathQuery);
	} else {
		allegati = allegatiFolderNode.getChildAssocsByType("crlatti:allegato");
	}
} else {
	status.code = 400;
	status.message = "id atto, passaggio o tipologiaAllegati non valorizzato";
	status.redirect = true;
}
model.allegati = allegati;