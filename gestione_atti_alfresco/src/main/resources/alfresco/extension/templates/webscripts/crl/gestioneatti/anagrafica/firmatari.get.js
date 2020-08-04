var firmatariPath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Anagrafica") +
	"/cm:"+search.ISO9075Encode("ConsiglieriAttivi") + "/*";
var luceneQuery = "PATH:\""+firmatariPath+"\"";
var firmatariResults = search.luceneSearch(luceneQuery);
model.firmatari = firmatariResults;