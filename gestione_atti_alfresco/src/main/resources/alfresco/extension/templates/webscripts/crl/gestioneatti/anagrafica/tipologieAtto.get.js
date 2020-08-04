<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var tipoAtto = args.tipoAtto;
var tipologieAtto = null;

if(checkIsNotNull(tipoAtto)){
	
	var tipologieAttoPath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Anagrafica") +
	"/cm:"+search.ISO9075Encode("Tipi atto") + 
	"/cm:"+search.ISO9075Encode(tipoAtto) +
	"/*";
	
	var luceneQuery = "PATH:\""+tipologieAttoPath+"\"";
	tipologieAtto = search.luceneSearch(luceneQuery);
	
} else {
	status.code = 400;
	status.message = "tipoAtto non valorizzato";
	status.redirect = true;
}

model.tipologieAtto = tipologieAtto;