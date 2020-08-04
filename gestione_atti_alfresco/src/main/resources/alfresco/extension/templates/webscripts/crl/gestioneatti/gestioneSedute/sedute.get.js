<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var provenienza = args.provenienza;
var legislatura = args.legislatura;
var sedute = null;

var gestioneSedutePath = "/app:company_home"+
"/cm:"+search.ISO9075Encode("CRL")+
"/cm:"+search.ISO9075Encode("Gestione Atti")+
"/cm:"+search.ISO9075Encode("Sedute")+
"/cm:"+search.ISO9075Encode(provenienza);

var luceneQuery = "PATH:\""+gestioneSedutePath+"//*\"" + " AND TYPE:\"crlatti:sedutaODG\" AND @crlatti\\:legislaturaSedutaODG:\""+legislatura+"\"";


var seduteResults = search.luceneSearch(luceneQuery);

model.sedute = seduteResults;