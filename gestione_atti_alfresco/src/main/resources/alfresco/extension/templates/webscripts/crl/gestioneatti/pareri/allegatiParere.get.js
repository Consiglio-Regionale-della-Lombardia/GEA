<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">
var idAtto = args.idAtto;
var tipologiaAllegati = args.tipologiaAllegati;
var allegati = null;
var organismoStatutario = args.organismoStatutario;

if(checkIsNotNull(idAtto)){
	var attoFolderNode = utils.getNodeFromString(idAtto);

	var pareriXPathQuery = "*[@cm:name='Pareri']";
	var pareriFolderNode = attoFolderNode.childrenByXPath(pareriXPathQuery)[0];
	
	var organismoStatutarioXPathQuery = "*[@cm:name='"+organismoStatutario+"']";
	var organismoStatutarioNode = pareriFolderNode.childrenByXPath(organismoStatutarioXPathQuery)[0];


	var allegatiFolderXpathQuery = "*[@cm:name='Allegati']";
	var allegatiFolderNode = organismoStatutarioNode.childrenByXPath(allegatiFolderXpathQuery)[0];
	
	if(checkIsNotNull(tipologiaAllegati)){
		var allegatiDiUnaTipologiaXPathQuery = "*[@crlatti:tipologia='"+tipologiaAllegati+"']";
		allegati = allegatiFolderNode.childrenByXPath(allegatiDiUnaTipologiaXPathQuery);
	} else {
		allegati = allegatiFolderNode.getChildAssocsByType("crlatti:allegato");
	}
} else {
	status.code = 400;
	status.message = "id atto o tipologiaAllegati non valorizzato";
	status.redirect = true;
}
model.allegati = allegati;