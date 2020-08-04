<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">


var idContent = args.idContent;

if(checkIsNotNull(idContent)){
	
	var content = utils.getNodeFromString(idContent);
	content.remove();
	
}else{
	
	status.code = 400;
	status.message = "id content non valorizzato";
	status.redirect = true;
}
		
