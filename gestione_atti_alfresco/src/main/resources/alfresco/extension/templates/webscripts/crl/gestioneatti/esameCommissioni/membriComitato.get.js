<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var idAtto = args.idAtto;
var commissione = args.commissione;
var passaggio = args.passaggio;

if(checkIsNotNull(idAtto) && checkIsNotNull(commissione)){
	var attoNode = utils.getNodeFromString(idAtto);
	
	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='"+passaggio+"']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
	var commissioneFolderNode = null;
	
	// cerco la cartella commissioni dell'atto
	var commissioniXPathQuery = "*[@cm:name='Commissioni']";
	var commissioniFolderNode = passaggioFolderNode.childrenByXPath(commissioniXPathQuery)[0];
	
	// cerco la commissione
	var commissioneXPathQuery = "*[@cm:name=\""+commissione+"\"]";
	var commissioneResults = commissioniFolderNode.childrenByXPath(commissioneXPathQuery);
	
	if(commissioneResults!=null && commissioneResults.length>0){
		commissioneFolderNode = commissioneResults[0];
		
		var comitatoXPathQuery = "*[@cm:name='ComitatoRistretto']";
		var comitatoCommissioneFolderNode = commissioneFolderNode.childrenByXPath(comitatoXPathQuery)[0];
		
		var membriComitatoNode = comitatoCommissioneFolderNode.getChildAssocsByType("crlatti:membroComitatoRistretto");
			
		model.atto = attoNode;
		model.commissione = commissioneFolderNode;
		model.passaggio = passaggioFolderNode;
		model.presenzaComitato = comitatoCommissioneFolderNode.properties["crlatti:presenzaCR"];
		model.dataIstituzioneComitato = comitatoCommissioneFolderNode.properties["crlatti:dataIstituzioneCR"];
                model.tipologia = comitatoCommissioneFolderNode.properties["crlatti:tipologiaCR"];
                
		model.membriComitato = membriComitatoNode;

			
	} else {
		status.code = 400;
		status.message = "commissione utente non valorizzata";
		status.redirect = true;
	}
	
	
} else {
		
	status.code = 400;
	status.message = "parametri idAtto, commissione, passaggio non valorizzati";
	status.redirect = true;	
}