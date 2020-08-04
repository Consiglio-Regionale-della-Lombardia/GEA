var commissioniReferentiPath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Anagrafica") +
	"/cm:"+search.ISO9075Encode("Commissioni") + "/*";
var luceneQuery = "PATH:\""+commissioniReferentiPath+"\"";
var commissioniReferentiResults = search.luceneSearch(luceneQuery, "@crlatti:numeroOrdinamentoCommissioneAnagrafica", true);
model.commissioniReferenti = commissioniReferentiResults;