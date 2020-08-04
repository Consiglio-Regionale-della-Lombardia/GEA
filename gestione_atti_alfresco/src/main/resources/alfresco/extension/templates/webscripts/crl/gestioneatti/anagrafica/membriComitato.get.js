var membriPath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Anagrafica") +
	"/cm:"+search.ISO9075Encode("MembriComitato") + "/*";
var luceneQuery = "PATH:\""+membriPath+"\"";
var membriResults = search.luceneSearch(luceneQuery);
model.membri = membriResults;