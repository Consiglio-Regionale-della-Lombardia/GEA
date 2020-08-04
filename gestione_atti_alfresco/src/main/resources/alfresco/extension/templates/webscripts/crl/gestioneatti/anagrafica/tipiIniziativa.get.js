var tipiIniziativePath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Anagrafica") +
	"/cm:"+search.ISO9075Encode("Tipi iniziative") + "/*";
var luceneQuery = "PATH:\""+tipiIniziativePath+"\"";
var tipiIniziativeResults = search.luceneSearch(luceneQuery);
model.tipiIniziative = tipiIniziativeResults;