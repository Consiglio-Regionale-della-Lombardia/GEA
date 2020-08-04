<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">
var tipoTemplate = args.tipoTemplate;

if(checkIsNotNull(tipoTemplate)){
	
	var templatesPath = "/app:company_home"+
	"/cm:"+search.ISO9075Encode("CRL")+
	"/cm:"+search.ISO9075Encode("Gestione Atti")+
	"/cm:"+search.ISO9075Encode("Templates");
	
	var luceneQuery = "PATH:\""+templatesPath+"//*\" AND TYPE:\""+tipoTemplate+"\"";
	var templateResults = search.luceneSearch(luceneQuery);
	
	var templateNode = templateResults[0];
	model.lettera = templateNode;
	
} else {
	status.code = 400;
	status.message = "parametro tipoTemplate non valorizzato";
	status.redirect = true;
}
