<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">
var idAtto = args.idAtto;
var idAttoAbbinato = args.idAttoAbbinato;
var passaggio = args.passaggio;

if(checkIsNotNull(idAtto)
		&& checkIsNotNull(idAttoAbbinato)
		&& checkIsNotNull(passaggio)){
	
	var attoNode = utils.getNodeFromString(idAtto);
	var attoAbbinatoNode = utils.getNodeFromString(idAttoAbbinato);
	
	
	// gestione passaggi
	var passaggiXPathQuery = "*[@cm:name='Passaggi']";
	var passaggiFolderNode = attoNode.childrenByXPath(passaggiXPathQuery)[0];
	
	var passaggioXPathQuery = "*[@cm:name='"+passaggio+"']";
	var passaggioFolderNode = passaggiFolderNode.childrenByXPath(passaggioXPathQuery)[0];
	
	var abbinamentiFolderXpathQuery = "*[@cm:name='Abbinamenti']";
	var abbinamentiFolderNode = passaggioFolderNode.childrenByXPath(abbinamentiFolderXpathQuery)[0];

	var abbinamentiFolderXpathQuery = "*[@cm:name='Abbinamenti']";
	var abbinamentiFolderNode = passaggioFolderNode.childrenByXPath(abbinamentiFolderXpathQuery)[0];
	
	var nomeNodoAbbinamento = attoAbbinatoNode.typeShort.substring(12) + " " + attoAbbinatoNode.name;
	
	var abbinamentoXpathQuery = "*[@cm:name='"+nomeNodoAbbinamento+"']";
	var abbinamentoNode = abbinamentiFolderNode.childrenByXPath(abbinamentoXpathQuery)[0];
	
	if(abbinamentoNode!=null){
			
		var passaggioAttoAbbinatoFolderNode = getLastPassaggio(attoAbbinatoNode)
		
		var xPathQueryAbbinamentiAttoAbbinato = "*[@cm:name='Abbinamenti']";
		var abbinamentiAttoAbbinatoFolderNode = passaggioAttoAbbinatoFolderNode.childrenByXPath(xPathQueryAbbinamentiAttoAbbinato)[0];
		
		//verifica esistenza abbinamento da cancellare
		var nomeNodoAbbinato = attoNode.typeShort.substring(12) + " " + attoNode.name;
		
		var abbinamentoAttoAbbinatoXPathQuery = "*[@cm:name='"+nomeNodoAbbinato+"']";
		var abbinamentoAttoAbbinatoNode = abbinamentiAttoAbbinatoFolderNode.childrenByXPath(abbinamentoAttoAbbinatoXPathQuery)[0];
		
		if(abbinamentoAttoAbbinatoNode!=null){
			abbinamentoAttoAbbinatoNode.remove();
		}
		
		abbinamentoNode.remove();
	}
	
	
} else {
	status.code = 400;
	status.message = "id atto, id atto abbinato e passaggio non valorizzati";
	status.redirect = true;
}