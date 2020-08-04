var tipiChiusuraPath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Anagrafica") +
	"/cm:"+search.ISO9075Encode("Tipi chiusura") + "/*";
var luceneQuery = "PATH:\""+tipiChiusuraPath+"\"";
var tipiChiusuraResults = search.luceneSearch(luceneQuery);
model.tipiChiusura = tipiChiusuraResults;