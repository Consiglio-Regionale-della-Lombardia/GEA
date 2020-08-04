var gruppiPath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Anagrafica") +
	"/cm:"+search.ISO9075Encode("GruppiConsiliari") + "/*";
var luceneQuery = "PATH:\""+gruppiPath+"\"";
var gruppiResults = search.luceneSearch(luceneQuery);
model.gruppi = gruppiResults;