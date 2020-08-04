var file = companyhome.childByNamePath("backupFirmatari_test.json");

utils.disableRules();

function sortFirmatari(a, b){

    var indiceA = a["crlatti:numeroOrdinamento"];
    var indiceB = b["crlatti:numeroOrdinamento"];

    if (indiceA < indiceB) {
        return -1
    }
    if (indiceA > indiceB) {
        return 1
    }

    return 0
}


/*
 must create a javascript string to use eval, file.content is a java String and wouldn't work
 so here's a solution to take Alfresco out of the way
 */
var body = " " + file.content;
var data = eval(body);

var firmatariPath = "/app:company_home" +
    "/cm:"+search.ISO9075Encode("CRL") +
    "/cm:"+search.ISO9075Encode("Gestione Atti") +
    "/cm:"+search.ISO9075Encode("Anagrafica") +
    "/cm:"+search.ISO9075Encode("ConsiglieriAttivi") +"/*";

for (var i = 0; i < data.length; i++) {
    var firmatariOriginari = [];
    var firmatariCorrenti = [];
    var gruppoFirmatari = [];

    var atto = data[i];
    var attoPath = atto.atto.replace("/Company Home/", "");
    var attoNode = companyhome.childByNamePath(attoPath);
    if (!attoNode) {
        logger.log("WARNING: " + atto.atto + " not found!!!!");
        continue;
    }
    logger.log("processing " + atto.atto);
    var firmatari = atto.firmatari;
    firmatari.sort(sortFirmatari);


    var firmatariParentNode = attoNode.childByNamePath("Firmatari");
    if (!firmatariParentNode) {
        logger.log("WARNING: Firmatari folder not found!!!");
        continue;
    }

    for (var j = 0; j < firmatari.length; j++) {
        var firmatario = firmatari[j];
        var firmatarioNode = firmatariParentNode.childByNamePath(firmatario["cm:name"]);
        if (firmatarioNode) {
            logger.log("removing firmatario " + firmatarioNode.name);
            firmatarioNode.remove();
        }
        firmatarioNode = firmatariParentNode.createNode(firmatario["cm:name"], "crlatti:firmatario");

        var isModifiedInGea = false;
        if (firmatario["crlatti:dataFirma"]) {
            firmatarioNode.properties["crlatti:dataFirma"] = firmatario["crlatti:dataFirma"];
            isModifiedInGea = true;
        }
        if (firmatario["crlatti:dataRitiro"]) {
            firmatarioNode.properties["crlatti:dataRitiro"] = firmatario["crlatti:dataRitiro"];
            isModifiedInGea = true;
        }
        if (firmatario["crlatti:gruppoConsiliare"]) {
            firmatarioNode.properties["crlatti:gruppoConsiliare"] = firmatario["crlatti:gruppoConsiliare"];
            isModifiedInGea = true;
        }
        if (firmatario["crlatti:isPrimoFirmatario"]) {
            firmatarioNode.properties["crlatti:isPrimoFirmatario"] = true;

            if (!firmatario["crlatti:dataRitiro"]){
                attoNode.properties["crlatti:primoFirmatario"] = primoFirmatarioAtto = firmatario["cm:name"];
            }
            var consigliereLuceneQuery = "PATH:\""+firmatariPath+"\" AND TYPE:\"crlatti:consigliereAnagrafica\" AND @cm\\:name:\""+firmatario["cm:name"]+"\"";
            var consigliereResults = search.luceneSearch(consigliereLuceneQuery);
            if(consigliereResults!=null && consigliereResults.length==1){
                var consigliereAnagraficaNode = consigliereResults[0];
                var gruppoPrimoFirmatario = consigliereAnagraficaNode.properties["crlatti:codiceGruppoConsigliereAnagrafica"];
                attoNode.properties["crlatti:gruppoPrimoFirmatario"] = gruppoPrimoFirmatario;
            }
        } else {
            if (!firmatario["crlatti:dataRitiro"]){
                firmatariOriginari.push(firmatario["cm:name"]);
            }
        }
        if (firmatario["crlatti:numeroOrdinamento"]) {
            firmatarioNode.properties["crlatti:numeroOrdinamento"] = firmatario["crlatti:numeroOrdinamento"];
        }
        if (isModifiedInGea) {
            firmatarioNode.properties["crlatti:isfirmatarioModified"] = true;
        }
        firmatarioNode.save();

        if (!firmatario["crlatti:dataRitiro"]){
            firmatariCorrenti.push(firmatario["cm:name"]);

            var consigliereLuceneQuery = "PATH:\""+firmatariPath+"\" AND TYPE:\"crlatti:consigliereAnagrafica\" AND @cm\\:name:\""+firmatario["cm:name"]+"\"";
            var consigliereResults = search.luceneSearch(consigliereLuceneQuery);
            if(consigliereResults!=null && consigliereResults.length==1){
                consigliereAnagraficaNode = consigliereResults[0];
                gruppoFirmatari.push(consigliereAnagraficaNode.properties["crlatti:codiceGruppoConsigliereAnagrafica"]);
            }
        }
    }

    attoNode.properties["crlatti:firmatariOriginari"] = firmatariOriginari;
    attoNode.properties["crlatti:firmatari"] = firmatariCorrenti;
    attoNode.properties["crlatti:gruppoFirmatari"] = gruppoFirmatari;
    attoNode.save();
}