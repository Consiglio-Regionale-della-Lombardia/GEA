/*************************************************
 * @fileCommons per commissioni e legislatura *
 *************************************************/

/*************************************************
 * Paths e Variabili *
 *************************************************/
var commissioniPath = "//app:company_home" +
    "/cm:" + search.ISO9075Encode("CRL") +
    "/cm:" + search.ISO9075Encode("Gestione Atti") +
    "/cm:" + search.ISO9075Encode("Anagrafica") +
    "/cm:" + search.ISO9075Encode("Commissioni") + "/*";
var legislaturePath = "/app:company_home" +
    "/cm:" + search.ISO9075Encode("CRL") +
    "/cm:" + search.ISO9075Encode("Gestione Atti") +
    "/cm:" + search.ISO9075Encode("Anagrafica") +
    "/cm:" + search.ISO9075Encode("Legislature") + "//*\"";
var attiPath = "/app:company_home" +
    "/cm:" + search.ISO9075Encode("CRL") +
    "/cm:" + search.ISO9075Encode("Gestione Atti") + "//*\"";
var gestioneAttiPath = "/app:company_home"+
    "/cm:"+search.ISO9075Encode("CRL")+
    "/cm:"+search.ISO9075Encode("Gestione Atti");

/*************************************************
 * Commissioni *
 *************************************************/
function getTitleDellaCommissioni() {
    var luceneQuery = "PATH:\"" + commissioniPath + "\"";
    var nodeType = "@crlatti:numeroOrdinamentoCommissioneAnagrafica";
    // faccio ricerca per LuceneSearch
    var tmpRes = search.luceneSearch(luceneQuery, nodeType, true);
    var commissioniResults = luceneSearchFile(tmpRes);
    return commissioniResults;
}

function luceneSearchFile(query) {
    var foundObjNode = new Object();
    var response = new Array();
    // ciclo per la ricerca Lucene
    for (var i = 0; i < query.length; i++) {
        // trovo il nodeRef del elemento
        var nodeRef = query[i].nodeRef.toString();
        // faccio ricerca per NodeRef Search
        var foundNode = search.findNode(nodeRef);
        // vedo che non ci sia nullo
        if (checkIsNotNull(foundNode.properties.title)) {
            foundObjNode.title = foundNode.properties.title;
            response.push(foundObjNode.title);
        } else if (checkIsNull(foundNode.properties.title)) {
            foundObjNode.title = foundNode.properties.name;
            response.push(foundObjNode.title);
        }
    }
    return response;
}

/*************************************************
 * Atti - Legislatura *
 *************************************************/
function getAttiDellaLegislaturaPerChiusura(dataFineLegislatura) {
    var legislaturaAttiResults = new Array();
    var luceneQuery = "PATH:\"" + attiPath + " AND TYPE:\"crlatti:atto\"";
    // faccio ricerca per LuceneSearch
    var attiList = search.luceneSearch(luceneQuery);
    legislaturaAttiResults = ricercaLegislaturaAtti(attiList, dataFineLegislatura);
    return legislaturaAttiResults;
}

function ricercaLegislaturaAtti(atti, dataFine) {
    var luceneQueryLegislatura = "PATH:\"" + legislaturePath;
    var legislaturaCorrente = search.luceneSearch(luceneQueryLegislatura);
    var attiResults = new Array();
    var legislatura = false;
    var nodoLegislatura = null;

    // faccio una ricerca della legislatura corrente
    for (var i = 0; i < legislaturaCorrente.length; i++) {
        var nodeRefLegislatura = legislaturaCorrente[i].nodeRef.toString();
        // faccio ricerca per NodeRef Search
        var foundLegislaturaNode = search.findNode(nodeRefLegislatura);
        if (foundLegislaturaNode.properties["crlatti:correnteLegislatura"] == true) {
            legislatura = true;
            nodoLegislatura = foundLegislaturaNode;
            break;
        }
    }
    // faccio una ricerca di tutti gli atti
    for (var i = 0; i < atti.length; i++) {
        // vedo che non ci sia nullo e che la legislatura sia la corrente
        if (checkIsNotNull(atti[i]) && legislatura == true && atti[i].properties["crlatti:statoAtto"] != "Chiuso") {
            atti[i].properties["crlatti:statoAtto"] = "Chiuso";
            atti[i].properties["crlatti:noteChiusura"] = "Per decadenza (fine legislatura)";
            atti[i].save();
            attiResults.push(atti[i]);
        }
    }
    // chiude la legislatura corrente cerco la cartella della legislatura corrente
    if (legislatura == true) {
        nodoLegislatura.properties["crlatti:dataFineLegislatura"] = dataFine; // passato per parametro
        nodoLegislatura.properties["crlatti:chiusuraLegislatura"] = true;
        nodoLegislatura.properties["crlatti:correnteLegislatura"] = false;
        nodoLegislatura.properties["crlatti:statoLegislatura"] = "Chiuso";
        nodoLegislatura.save();
    }
    return attiResults;
}

/*************************************************
 * Aprire Cartella Legislatura *
 *************************************************/
function aprireLegislaturaCorrente(data, legislaturaNome) {
    var path = "/app:company_home" +
                "/cm:" + search.ISO9075Encode("CRL") +
                "/cm:" + search.ISO9075Encode("Gestione Atti") +
                "/cm:" + search.ISO9075Encode("Anagrafica") +
                "/cm:" + search.ISO9075Encode("Legislature") + "//*\"";
    var luceneQueryLegislatura = "PATH:\"" + path ;
    var results = search.luceneSearch(luceneQueryLegislatura);

    var foundAttoNode = null;
    var idAnagrafica = null;
    for (var i = 0; i < results.length; i++) {
        var nodeRef = results[i].nodeRef.toString();
        // faccio ricerca per NodeRef Search
        foundAttoNode = search.findNode(nodeRef);
        if (foundAttoNode.name == legislaturaNome) {
            logger.system.out("Legislatura " + foundAttoNode.name + " esiste, non vado a crearla");
        }
        if (foundAttoNode.name == "XI" && checkIsNotNull(foundAttoNode.properties["crlatti:idAnagrafica"])) {
            idAnagrafica = foundAttoNode.properties["crlatti:idAnagrafica"];
            logger.system.out("Legislatura " + foundAttoNode.name + " con idAnagrafica " + foundAttoNode.properties["crlatti:idAnagrafica"]);
        }
    }
    //creazione dello spazio legislatura
	var legislaturaPath = gestioneAttiPath + "/cm:" + search.ISO9075Encode(foundAttoNode);
	var legislaturaLuceneQuery = "PATH:\""+legislaturaPath+"\"";
	var legislaturaResults = search.luceneSearch(legislaturaLuceneQuery);
	var legislaturaFolderNode = null;
	if(legislaturaResults!=null && legislaturaResults.length>0){
		legislaturaFolderNode = legislaturaResults[0];
	} else {
		var gestioneAttiLuceneQuery = "PATH:\"" + gestioneAttiPath + "\"";
		var gestioneAttiFolderNode = search.luceneSearch(gestioneAttiLuceneQuery)[0];
        if(checkIsNotNull(legislaturaNome) && checkIsNotNull(data)){
		    legislaturaFolderNode = gestioneAttiFolderNode.createFolder(legislaturaNome);
            var dataSplitted = data.split("-");
            var valueDate = new Date(dataSplitted[0], dataSplitted[1]-1, dataSplitted[2]);
            legislaturaFolderNode.properties["crlatti:dataInizioLegislatura"] = valueDate;
            legislaturaFolderNode.properties["crlatti:dataFineLegislatura"] = null;
            legislaturaFolderNode.properties["crlatti:chiusuraLegislatura"] = false;
            legislaturaFolderNode.properties["crlatti:correnteLegislatura"] = true;
            legislaturaFolderNode.properties["crlatti:idAnagrafica"] = idAnagrafica + 1;
        }
        legislaturaFolderNode.save();
        legislaturaFolderNode.addAspect("crlatti:importatoDaAnagrafica");
    }
    // logger.system.out("Legislatura " + legislaturaFolderNode + " creata");
    return legislaturaFolderNode;
}

/*************************************************
 * Ripristinare Atti Per Legislatura *
 *************************************************
function getRipristinareAttiPerLegislatura(uuidDaRipristinare) {
    var attiChiuse = new Array();
    // recupero il nodeRef del nodo
    var nodeRefAttoRipristinato = aprireAttiChiusi(uuidDaRipristinare);
    // faccio ricerca per NodeRef Search
    var foundAttoNode = search.findNode(nodeRefAttoRipristinato);
    if (checkIsNotNull(nodeRefAttoRipristinato) && legislaturaCorrente == true) {
        if (nodeRefAttoRipristinato.properties["crlatti:statoAtto"] != "Chiuso") {
            attiChiuse.push(foundAttoNode.properties["crlatti:statoAtto"]);
            logger.system.out("Atto numero: " + nodeRefAttoRipristinato.properties["crlatti:numeroAtto"] +
                "\ndella legislatura: " + nodeRefAttoRipristinato.properties["crlatti:legislatura"] +
                " " + nodeRefAttoRipristinato.properties["crlatti:statoAtto"]);
        }
    }
    logger.system.out("Atto ripristinato a stato NON chiuso: " + attiNonChiuse.length);
    return nodeRefAttoRipristinato;
}

function aprireAttiChiusi(uuidAtto) {
    var nodeRef = costruireNodeRef(uuidAtto);
    // faccio ricerca per NodeRef Search
    var foundAttoNode = search.findNode(nodeRef);
    // vedo che non ci sia nullo e che la legislatura sia XI
    if (foundAttoNode.properties["crlatti:statoAtto"] == "Chiuso" && legislaturaCorrente == true) {
        logger.system.out("Atto pronto per aprirlo: " + foundAttoNode.properties["crlatti:statoAtto"]);
        foundAttoNode.properties["crlatti:statoAtto"] = "RIPRISTINATO L'ATTO'";
        foundAttoNode.properties["crlatti:noteChiusura"] = "Ripristino delL'Atto della legislatura " + foundAttoNode.properties["crlatti:legislatura"];
        foundAttoNode.save();
        logger.system.out("Atto numero: " + foundAttoNode.properties["crlatti:numeroAtto"] +
            "\ndella legislatura: " + foundAttoNode.properties["crlatti:legislatura"] +
            " ha stato " + foundAttoNode.properties["crlatti:statoAtto"] +
            "\nnote: " + foundAttoNode.properties["crlatti:noteChiusura"]);
        return foundAttoNode;
    } else {
        logger.system.out("Legislatura chiusa è chiusa: " + foundAttoNode.properties["crlatti:statoAtto"]);
    }
}



/*************************************************
 * Funzioni Generali *
 *************************************************/

/**
function prendereUUID(uuid) {
    var uuidPulito = uuid.slice(24);
    return uuidPulito;
}

function costruireNodeRef(uuid) {
    const ref = "workspace://SpacesStore/";
    var nodeRef = ref + uuid;
    return nodeRef;
}

function prendereLegislaturaCorrente() {
    var luceneQueryLegislatura = "PATH:\"" + legislaturePath;
    var legislaturaCorrente = search.luceneSearch(luceneQueryLegislatura);
    // faccio una ricerca di tutti gli atti che non hanno
    for (var i = 0; i < legislaturaCorrente.length; i++) {
        var nodeRefLegislatura = legislaturaCorrente[i].nodeRef.toString();
        // faccio ricerca per NodeRef Search
        var foundLegislaturaNode = search.findNode(nodeRefLegislatura);
        if (foundLegislaturaNode.properties["crlatti:correnteLegislatura"] == true) {
            return true;
        }
    }
}
*/

function checkIsNull(parameterValue) {
    if (parameterValue == undefined || parameterValue == null || parameterValue == "" || parameterValue == "null") return true;
    else return false;
}

function checkIsNotNull(parameterValue) {
    if (parameterValue != null && parameterValue != undefined && parameterValue != "" && parameterValue != "null") return true;
    else return false;
}

function romanize (num) {
	if (!+num)
		return false;
	var	digits = String(+num).split(""),
		key = ["","C","CC","CCC","CD","D","DC","DCC","DCCC","CM",
		       "","X","XX","XXX","XL","L","LX","LXX","LXXX","XC",
		       "","I","II","III","IV","V","VI","VII","VIII","IX"],
		roman = "",
		i = 3;
	while (i--)
		roman = (key[+digits.pop() + (i * 10)] || "") + roman;
	return Array(+digits.join("") + 1).join("M") + roman;
}

function deromanize (str) {
	var	str = str.toUpperCase(),
		validator = /^M*(?:D?C{0,3}|C[MD])(?:L?X{0,3}|X[CL])(?:V?I{0,3}|I[XV])$/,
		token = /[MDLV]|C[MD]?|X[CL]?|I[XV]?/g,
		key = {M:1000,CM:900,D:500,CD:400,C:100,XC:90,L:50,XL:40,X:10,IX:9,V:5,IV:4,I:1},
		num = 0, m;
	if (!(str && validator.test(str)))
		return false;
	while (m = token.exec(str))
		num += key[m[0]];
	return num;
}