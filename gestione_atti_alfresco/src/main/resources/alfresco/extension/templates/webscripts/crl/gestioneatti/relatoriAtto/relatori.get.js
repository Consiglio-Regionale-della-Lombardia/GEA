<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var idAtto = args.idAtto;


if(checkIsNotNull(idAtto) && checkIsNotNull(commissione) && checkIsNotNull(passaggio)){
	var attoNode = utils.getNodeFromString(idAtto);
	
	// gestione passaggi
	var relatoriXPathQuery = "*[@cm:name='RelatoriAtto']";
	var relatoriFolderNode = attoNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var relatoriNode = relatoriCommissioneFolderNode.getChildAssocsByType("crlatti:relatore");
		
	model.atto = attoNode;
	model.relatori = relatoriNode;	
	
} else {
		
	status.code = 400;
	status.message = "parametro idAtto non valorizzato";
	status.redirect = true;	
}