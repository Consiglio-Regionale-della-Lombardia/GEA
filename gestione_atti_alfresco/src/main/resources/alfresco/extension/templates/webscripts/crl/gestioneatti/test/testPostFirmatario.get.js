var luceneQueryAtti = "TYPE:\"crlatti:atto\"";
var atti = search.luceneSearch(luceneQueryAtti);
model.atti = atti;

var firmatariPath = "/app:company_home" +
"/cm:"+search.ISO9075Encode("CRL") +
"/cm:"+search.ISO9075Encode("Gestione Atti") +
"/cm:"+search.ISO9075Encode("Anagrafica") +
"/cm:"+search.ISO9075Encode("Firmatari") + "/*";
var luceneQuery = "PATH:\""+firmatariPath+"\"";
var firmatariResults = search.luceneSearch(luceneQuery);
model.firmatari = firmatariResults;