var organismiStatutariPath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Anagrafica") +
	"/cm:"+search.ISO9075Encode("OrganismiStatutari") + "/*";
var luceneQuery = "PATH:\""+organismiStatutariPath+"\"";
var organismiStatutariResults = search.luceneSearch(luceneQuery);
model.organismiStatutari = organismiStatutariResults;