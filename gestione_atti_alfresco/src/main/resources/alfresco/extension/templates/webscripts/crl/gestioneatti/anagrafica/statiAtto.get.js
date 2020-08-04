var statiAttoPath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Anagrafica") +
	"/cm:"+search.ISO9075Encode("Stati atto") + "/*";
var luceneQuery = "PATH:\""+statiAttoPath+"\"";
var statiAttoResults = search.luceneSearch(luceneQuery);
model.statiAtto = statiAttoResults;