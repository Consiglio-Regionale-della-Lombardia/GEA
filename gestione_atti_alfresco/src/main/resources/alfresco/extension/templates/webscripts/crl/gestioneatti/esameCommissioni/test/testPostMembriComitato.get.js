var luceneQueryAtti = "TYPE:\"crlatti:atto\"";
var attiResults = search.luceneSearch(luceneQueryAtti);
model.atti = attiResults;
	


var membriComitatoPath = "/app:company_home" +
"/cm:"+search.ISO9075Encode("CRL") +
"/cm:"+search.ISO9075Encode("Gestione Atti") +
"/cm:"+search.ISO9075Encode("Anagrafica") +
"/cm:"+search.ISO9075Encode("MembriComitato") + "/*";
var luceneQuery = "PATH:\""+membriComitatoPath+"\"";
var membriResults = search.luceneSearch(luceneQuery);
model.membri = membriResults;