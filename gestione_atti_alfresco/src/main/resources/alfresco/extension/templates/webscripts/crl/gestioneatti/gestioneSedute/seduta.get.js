<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var provenienza = args.provenienza;
var dataSeduta = args.dataSeduta;
var legislatura = args.legislatura;

var gestioneSedutePath = "/app:company_home"+
"/cm:"+search.ISO9075Encode("CRL")+
"/cm:"+search.ISO9075Encode("Gestione Atti")+
"/cm:"+search.ISO9075Encode("Sedute")+
"/cm:"+search.ISO9075Encode(provenienza);

var luceneQuery = "PATH:\""+gestioneSedutePath+"//*\" "+
	    "AND TYPE:\"crlatti:sedutaODG\" " +
		"AND @crlatti\\:dataSedutaSedutaODG:\""+dataSeduta+"T00:00:00\" " +
		"AND @crlatti\\:legislaturaSedutaODG:\""+legislatura+"\"";

var sedutaResults = search.luceneSearch(luceneQuery);
var seduta = null;

if(sedutaResults!=null && sedutaResults.length>0){
	seduta = sedutaResults[0];
	model.seduta = seduta;
} else {
	status.code = 404;
	status.message = "Seduta non trovata con provenienza "+provenienza+" | Data "+dataSeduta+" | Legislatura "+legislatura;
	status.redirect = true;
}