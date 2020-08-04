<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">


var idSeduta = args.idSeduta;

if(checkIsNotNull(idSeduta)){
	
	var seduta = utils.getNodeFromString(idSeduta);
	seduta.remove();
	
}else{
	
	status.code = 400;
	status.message = "id seduta non valorizzato";
	status.redirect = true;
}
		
