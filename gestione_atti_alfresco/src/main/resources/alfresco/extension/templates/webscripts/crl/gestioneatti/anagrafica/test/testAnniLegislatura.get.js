var legislaturePath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Anagrafica") +
	"/cm:"+search.ISO9075Encode("Legislature") + "/*";

var luceneQuery = "PATH:\""+legislaturePath+"\"";
var legislatureResults = search.luceneSearch(luceneQuery);
model.legislatureResults = legislatureResults;