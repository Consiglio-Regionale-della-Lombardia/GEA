<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">
var idAtto = args.idAtto;
var tipologiaAllegati = args.tipologiaAllegati;
var allegati = null;
var consultazione= args.consultazione;


if(checkIsNotNull(idAtto)){
	var attoFolderNode = utils.getNodeFromString(idAtto);

	var consultazioniXPathQuery = "*[@cm:name='Consultazioni']";
	var consultazioniFolderNode = attoFolderNode.childrenByXPath(consultazioniXPathQuery)[0];
	
	var consultazioneXPathQuery = "*[@cm:name='"+consultazione+"']";
	var consultazioneNode = consultazioniFolderNode.childrenByXPath(consultazioneXPathQuery)[0];


	var allegatiFolderXpathQuery = "*[@cm:name='Allegati']";
	var allegatiFolderNode = consultazioneNode.childrenByXPath(allegatiFolderXpathQuery)[0];
	
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