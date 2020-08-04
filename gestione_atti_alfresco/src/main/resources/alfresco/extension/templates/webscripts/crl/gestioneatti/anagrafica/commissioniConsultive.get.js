var commissioniConsultivePath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Anagrafica") +
	"/cm:"+search.ISO9075Encode("Commissioni") + "/*";
var luceneQuery = "PATH:\""+commissioniConsultivePath+"\"";
var commissioniConsultiveResults = search.luceneSearch(luceneQuery, "@crlatti:numeroOrdinamentoCommissioneAnagrafica", true);
model.commissioniConsultive = commissioniConsultiveResults;