<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/commonMigrazione.js">

var atto = json.get("atto").get("atto");
var id = atto.get("id");
var statoAtto = atto.get("stato");
var passaggio = json.get("target").get("target").get("passaggio");

// selezione del passaggio corrente
var passaggioTarget = getPassaggioTarget(json, passaggio);

var dataSedutaRinvio = filterParam(passaggioTarget.get("aula").get("dataSedutaRinvio"));
var dataTermineMassimo = filterParam(passaggioTarget.get("aula").get("dataTermineMassimo"));
var motivazioneRinvio = filterParam(passaggioTarget.get("aula").get("motivazioneRinvio"));
var rinvioCommBilancio = filterParam(passaggioTarget.get("aula").get("rinvioCommBilancio"));


if(checkIsNotNull(id)
		&& checkIsNotNull(passaggio)){
	var attoNode = utils.getNodeFromString(id);
	
	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='"+passaggio+"']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
	
	var aulaXPathQuery = "*[@cm:name='Aula']";
	var aulaFolderNode = passaggioFolderNode.childrenByXPath(aulaXPathQuery)[0];
	
	aulaFolderNode.properties["crlatti:rinvioCommBilancioAula"] = rinvioCommBilancio;
	aulaFolderNode.properties["crlatti:motivazioneRinvioAula"] = motivazioneRinvio;
	aulaFolderNode.properties["crlatti:rinviato"] = true;
		
	if(checkIsNotNull(dataSedutaRinvio)) {
		var dataSedutaRinvioSplitted = dataSedutaRinvio.split("-");
		var dataSedutaRinvioParsed = new Date(dataSedutaRinvioSplitted[0],dataSedutaRinvioSplitted[1]-1,dataSedutaRinvioSplitted[2]);
		aulaFolderNode.properties["crlatti:dataSedutaRinvioAula"] = dataSedutaRinvioParsed;
	}
	
	if(checkIsNotNull(dataTermineMassimo)) {
		var dataTermineMassimoSplitted = dataTermineMassimo.split("-");
		var dataTermineMassimoParsed = new Date(dataTermineMassimoSplitted[0],dataTermineMassimoSplitted[1]-1,dataTermineMassimoSplitted[2]);
		aulaFolderNode.properties["crlatti:dataTermineMassimoAula"] = dataTermineMassimoParsed;
	}
			
	aulaFolderNode.save();
	
	
	// creazione del nuovo passaggio
	// si potrebbe prendere semplicemente il passaggio corrente e incrementare di uno 
	// ma la soluzione più robusta è effettuare comunque dei controlli su tutti i passaggi creati interrogando il repository

	
	var indiceUltimoPassaggio = 0;
	
	// set del nome passaggio
	for(var i=0; i<passaggiFolderNode.children.length; i++){
		var indiceCorrente = passaggiFolderNode.children[i].name.substring(9);		
		if(indiceCorrente > indiceUltimoPassaggio){
			indiceUltimoPassaggio = indiceCorrente;
		}
	}
	
	var indiceNuovoPassaggio = parseInt(indiceUltimoPassaggio) + 1;
	
	var passaggioSpaceTemplateQuery = "PATH:\"/app:company_home/app:dictionary/app:space_templates/cm:Passaggio\"";
	var passaggioSpaceTemplateNode = search.luceneSearch(passaggioSpaceTemplateQuery)[0];
	var nuovoPassaggioFolderNode = passaggioSpaceTemplateNode.copy(passaggiFolderNode, true); // deep copy
	
	nuovoPassaggioFolderNode.name = "Passaggio"+indiceNuovoPassaggio;
	nuovoPassaggioFolderNode.save();
	
	// creo le commissioni e popolo i metadati già decisi in presentazione e assegnazione (prendo i dati del passaggio corrente)
	
	var nuoveCommissioniXPathQuery = "*[@cm:name='Commissioni']";
	var nuoveCommissioniFolderNode = nuovoPassaggioFolderNode.childrenByXPath(nuoveCommissioniXPathQuery)[0];

	// commissioni del passaggio corrente
	var commissioniXPathQuery = "*[@cm:name='Commissioni']";
	var commissioniFolderNode = passaggioFolderNode.childrenByXPath(commissioniXPathQuery)[0];

	
	var commissioneNodeList = commissioniFolderNode.getChildAssocsByType("crlatti:commissione");
	
	
	for(var i=0; i<commissioneNodeList.length; i++){
			
		var commissioneSpaceTemplateQuery = "PATH:\"/app:company_home/app:dictionary/app:space_templates/cm:Commissione\"";
		var commissioneSpaceTemplateNode = search.luceneSearch(commissioneSpaceTemplateQuery)[0];
		
		// deep copy da space template
		commissioneSpaceTemplateNode.copy(nuoveCommissioniFolderNode, true);
		
		var commissioneXPathQuery = "*[@cm:name='Commissione']";
		var commissioneTempNode = nuoveCommissioniFolderNode.childrenByXPath(commissioneXPathQuery)[0];
		
		// setting dei valori iniziali della commissione a partire dai valori assegnati alla commissione nel passaggio corrente
		commissioneTempNode.name = commissioneNodeList[i].properties["cm:name"];
		commissioneTempNode.properties["crlatti:dataAssegnazioneCommissione"] = commissioneNodeList[i].properties["crlatti:dataAssegnazioneCommissione"];
		commissioneTempNode.properties["crlatti:dataAnnulloCommissione"] = commissioneNodeList[i].properties["crlatti:dataAnnulloCommissione"];
		commissioneTempNode.properties["crlatti:dataPropostaCommissione"] = commissioneNodeList[i].properties["crlatti:dataPropostaCommissione"];
		commissioneTempNode.properties["crlatti:ruoloCommissione"] = commissioneNodeList[i].properties["crlatti:ruoloCommissione"];
		commissioneTempNode.save();
		
	}
		
	attoNode.properties["crlatti:statoAtto"] = statoAtto;
	attoNode.save();

	model.atto = attoNode;
	model.nuovoPassaggio = nuovoPassaggioFolderNode;
	
} else {
	status.code = 400;
	status.message = "id e pasaggio atto non valorizzato";
	status.redirect = true;
}