/*
<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var legislaturaCorrente = getLegislaturaCorrente();

var gestioneAttiPath = "/app:company_home"+
"/cm:"+search.ISO9075Encode("CRL")+
"/cm:"+search.ISO9075Encode("Gestione Atti");

var legislaturaRepositoryPath = gestioneAttiPath + "/cm:"+search.ISO9075Encode(legislaturaCorrente);
var attoIndirizzoLuceneQuery = "PATH:\""+legislaturaRepositoryPath+"//*\"";
attoIndirizzoLuceneQuery += " AND TYPE:\"crlatti:attoIndirizzo\"";

var attoIndirizzoResults = search.luceneSearch(attoIndirizzoLuceneQuery);

var listaTipiAtto = new Array();


for(var i=0; i<attoIndirizzoResults.length; i++){
	
	var tipo = attoIndirizzoResults[i].properties["crlatti:tipoAttoIndirizzo"];
	
	if(!contains(listaTipiAtto, tipo)){
		listaTipiAtto.push(tipo);
	}
	
	
}


model.tipiAtto = listaTipiAtto;


function contains(a, obj) {
    for (var i = 0; i < a.length; i++) {
        if (a[i] === obj) {
            return true;
        }
    }
    return false;
}
*/

model.tipiAtto = ["ITR", "IQT", "ITL", "MOZ", "ODG", "RIS"].sort();