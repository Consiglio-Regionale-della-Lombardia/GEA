var legislatura = args.legislatura;
var firmatariStoriciPath = "/app:company_home" +
	"/cm:"+search.ISO9075Encode("CRL") +
	"/cm:"+search.ISO9075Encode("Gestione Atti") +
	"/cm:"+search.ISO9075Encode("Anagrafica") +
	"/cm:"+search.ISO9075Encode("ConsiglieriStorici");
var luceneQuery = "PATH:\""+firmatariStoriciPath+"/*\" AND @crlatti\\:legislaturaConsigliereAnagrafica:\""+legislatura+"\"";
var firmatariStoriciResults = search.luceneSearch(luceneQuery);
model.firmatari = firmatariStoriciResults;