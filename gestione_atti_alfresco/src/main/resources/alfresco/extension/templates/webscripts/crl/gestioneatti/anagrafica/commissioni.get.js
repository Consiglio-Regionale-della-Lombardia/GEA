var commissioniPath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Anagrafica") +
	"/cm:"+search.ISO9075Encode("Commissioni") + "/*";
var luceneQuery = "PATH:\""+commissioniPath+"\"";
var commissioniResults = search.luceneSearch(luceneQuery, "@crlatti:numeroOrdinamentoCommissioneAnagrafica", true);



model.commissioni = commissioniResults;