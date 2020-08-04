<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var username = person.properties.userName;
if (username == "protocollo" || username == "admin") {

    var importProtocolloPath =
        "/app:company_home" +
        "/cm:" + search.ISO9075Encode("Import") +
        "/cm:" + search.ISO9075Encode("Gestione Atti") +
        "/cm:" + search.ISO9075Encode("Protocollo") +
        "/cm:" + search.ISO9075Encode("Atti");

    var importProtocolloLuceneQuery = "PATH:\"" + importProtocolloPath + "\"";
    var importProtocolloFolderNode = search.luceneSearch(importProtocolloLuceneQuery)[0];

    var atto = json.get("atto");
    var tipoAtto = atto.get("tipoAtto");
    var idProtocollo = atto.get("idProtocollo");
    var numeroAtto = atto.get("numeroAtto");
    var estensioneAtto = "";

    // Da eliminare quando il protocollo supporterà il campo estensioneAtto
    try {
        if (atto.get("estensioneAtto")) {
            estensioneAtto = atto.get("estensioneAtto");
        }

    } catch (err) {
        // do nothing
    }

    if (checkIsNotNull(tipoAtto)) {

        if (tipoAtto == "EAC") {
            // atto EAC non importato da protocollo
            status.code = 400;
            status.message = "Tipo atto " + tipoAtto + " non gestito";
            protocolloLogger.error(status.message);
            status.redirect = true;

        } else if (tipoAtto == "MIS") {
            // atto MIS non importato da protocollo
            status.code = 400;
            status.message = "Tipo atto " + tipoAtto + " non gestito";
            protocolloLogger.error(status.message);
            status.redirect = true;

        } else {

            var legislatura = atto.get("legislatura");
            var tipologia = atto.get("tipologia");

            var numeroProtocollo = atto.get("numeroProtocollo");
            var numeroRepertorio = atto.get("numeroRepertorio");
            var dataRepertorio = atto.get("dataRepertorio");
            var urlFascicolo = atto.get("urlFascicolo");
            var classificazione = atto.get("classificazione");
            var oggetto = atto.get("oggetto");

            var tipoIniziativa = atto.get("tipoIniziativa");
            var dataIniziativa = atto.get("dataIniziativa");
            var descrizioneIniziativa = atto.get("descrizioneIniziativa");

            var numeroDgr = atto.get("numeroDgr");
            var dataDgr = atto.get("dataDgr");

            var assegnazione = atto.get("assegnazione");
            var esibenteMittente = atto.get("esibenteMittente");

            var dataImportazione = new Date();
            var mese = dataImportazione.getMonth() + 1;
            var anno = dataImportazione.getFullYear();


            if (checkIsNotNull(legislatura)
                && checkIsNotNull(numeroAtto)
                && checkIsNotNull(tipoAtto)
                && checkIsNotNull(idProtocollo)) {

                //gestione dei tipi atto
                var nodeType = "crlatti:atto";
                if (tipoAtto == "PDL") {
                    nodeType = "crlatti:attoPdl";
                } else if (tipoAtto == "DOC") {
                    nodeType = "crlatti:attoDoc";
                } else if (tipoAtto == "INP") {
                    nodeType = "crlatti:attoInp";
                } else if (tipoAtto == "PAR") {
                    nodeType = "crlatti:attoPar";
                } else if (tipoAtto == "PDA") {
                    nodeType = "crlatti:attoPda";
                } else if (tipoAtto == "PLP") {
                    nodeType = "crlatti:attoPlp";
                } else if (tipoAtto == "PRE") {
                    nodeType = "crlatti:attoPre";
                } else if (tipoAtto == "REF") {
                    nodeType = "crlatti:attoRef";
                } else if (tipoAtto == "REL") {
                    nodeType = "crlatti:attoRel";
                } else if (tipoAtto == "ORG") {
                    nodeType = "crlatti:attoOrg";
                }

                if (nodeType != "crlatti:atto") {


                    var attoFolderNode = null;
                    var update = false;

                    //creazione del nodo del nuovo atto
                    var attoSpaceTemplateQuery = "PATH:\"/app:company_home/app:dictionary/app:space_templates/cm:Atto\"";
                    var attoSpaceTemplateNode = search.luceneSearch(attoSpaceTemplateQuery)[0];
                    attoFolderNode = attoSpaceTemplateNode.copy(importProtocolloFolderNode, true);


                    // FIX PROTOCOLLO
                    attoFolderNode.name = numeroAtto + estensioneAtto + "_" + makeTimestamp();
                    attoFolderNode.specializeType(nodeType);
                    attoFolderNode.properties["crlatti:legislatura"] = legislatura;
                    attoFolderNode.properties["crlatti:numeroAtto"] = numeroAtto;
                    attoFolderNode.properties["crlatti:estensioneAtto"] = estensioneAtto;
                    attoFolderNode.properties["crlatti:tipologia"] = tipologia;
                    attoFolderNode.properties["crlatti:anno"] = anno;
                    attoFolderNode.properties["crlatti:idProtocollo"] = idProtocollo;
                    attoFolderNode.properties["crlatti:numeroProtocollo"] = numeroProtocollo;
                    attoFolderNode.properties["crlatti:numeroRepertorio"] = numeroRepertorio;
                    attoFolderNode.properties["crlatti:classificazione"] = classificazione;
                    attoFolderNode.properties["crlatti:oggetto"] = oggetto;
                    attoFolderNode.properties["crlatti:tipoIniziativa"] = tipoIniziativa;
                    attoFolderNode.properties["crlatti:descrizioneIniziativa"] = descrizioneIniziativa;
                    attoFolderNode.properties["crlatti:assegnazione"] = assegnazione;
                    attoFolderNode.properties["crlatti:pubblico"] = true;
                    attoFolderNode.properties["crlatti:urlFascicolo"] = urlFascicolo;

                    attoFolderNode.properties["crlatti:statoAtto"] = "Protocollato";


                    //dataRepertorio
                    if (checkIsNotNull(dataRepertorio)) {
                        var dataRepertorioSplitted = dataRepertorio.split("-");
                        var dataRepertorioParsed = new Date(dataRepertorioSplitted[0], dataRepertorioSplitted[1] - 1, dataRepertorioSplitted[2]);
                        attoFolderNode.properties["crlatti:dataRepertorio"] = dataRepertorioParsed;
                    }

                    //dataIniziativa
                    if (checkIsNotNull(dataIniziativa)) {
                        var dataIniziativaSplitted = dataIniziativa.split("-");
                        var dataIniziativaParsed = new Date(dataIniziativaSplitted[0], dataIniziativaSplitted[1] - 1, dataIniziativaSplitted[2]);
                        attoFolderNode.properties["crlatti:dataIniziativa"] = dataIniziativaParsed;
                    }

                    //aspect firmatari
                    if (attoFolderNode.hasAspect("crlatti:firmatariAspect")) {
                        var firmatariSpaceTemplateQuery = "PATH:\"/app:company_home/app:dictionary/app:space_templates/cm:Firmatari\"";
                        var firmatariSpaceTemplateNode = search.luceneSearch(firmatariSpaceTemplateQuery)[0];
                        firmatariSpaceTemplateNode.copy(attoFolderNode);
                    }

                    //aspect rlatoriAtto: alcuni atti (es:ORG) hanno i relatori direttamente collegati ad atto e non a commissione
                    if (attoFolderNode.hasAspect("crlatti:relatoriAttoAspect")) {
                        var relatoriSpaceTemplateQuery = "PATH:\"/app:company_home/app:dictionary/app:space_templates/cm:RelatoriAtto\"";
                        var relatoriSpaceTemplateNode = search.luceneSearch(relatoriSpaceTemplateQuery)[0];
                        relatoriSpaceTemplateNode.copy(attoFolderNode);
                    }


                    var firmatariPath = "/app:company_home" +
                        "/cm:" + search.ISO9075Encode("CRL") +
                        "/cm:" + search.ISO9075Encode("Gestione Atti") +
                        "/cm:" + search.ISO9075Encode("Anagrafica") +
                        "/cm:" + search.ISO9075Encode("ConsiglieriAttivi") + "/*";

                    //gestione tipo iniziativa
                    if ((!update) && checkIsNotNull(esibenteMittente)) {
                        var firmatariArray = new Array();
                        if (tipoIniziativa == "01_ATTO DI INIZIATIVA CONSILIARE") {

                            protocolloLogger.info("esibenteMittente [" + tipoAtto + " " + numeroAtto + estensioneAtto + "]: " + esibenteMittente);

                            var prefix = "CONSIGLIERI REGIONALI:";

                            if (esibenteMittente.indexOf(prefix) != -1) {

                                var esibenteMittenteFirmatari = esibenteMittente.split(prefix)[1];

                                var firmatariNomeCognomeSplitted = new Array();

                                if (esibenteMittenteFirmatari.indexOf("-") == -1) {
                                    firmatariNomeCognomeSplitted.push(esibenteMittenteFirmatari);
                                } else {
                                    firmatariNomeCognomeSplitted = esibenteMittenteFirmatari.split("-");
                                }

                                var firmatariSplittedTemp = null;
                                var firmatariSplitted = new Array();

                                for (var k = 0; k < firmatariNomeCognomeSplitted.length; k++) {

                                    firmatariSplittedTemp = firmatariNomeCognomeSplitted[k].split(",");
                                    for (var j = 0; j < firmatariSplittedTemp.length; j++) {
                                        var firmatarioTemp = null;
                                        if (firmatariSplittedTemp[j].indexOf("(") == -1) {
                                            firmatarioTemp = firmatariSplittedTemp[j];
                                        } else {
                                            firmatarioTemp = firmatariSplittedTemp[j].split("\\(")[0];
                                        }

                                        firmatariSplitted.push(firmatarioTemp);
                                    }

                                }

                                var childrenXPathQuery = "*[@cm:name='Firmatari']";
                                var firmatariFolderNode = attoFolderNode.childrenByXPath(childrenXPathQuery)[0];

                                // elimino i firmatari eventualmente presenti per l'update
                                if (update && firmatariSplitted.length > 0) {
                                    var firmatari = firmatariFolderNode.children;
                                    for (var f = 0; f < firmatari.length; f++) {
                                        if (firmatari[f].isDocument) {
                                            firmatari[f].remove();
                                        }
                                    }
                                }

                                //for firmatari splitted
                                //modifica per gli atti di tipo PAR e PRE - non hanno firmatari
                                if (tipoAtto != "PAR" && tipoAtto != "PRE") {
                                    for (var i = 0; i < firmatariSplitted.length; i++) {
                                        if (firmatariSplitted[i].indexOf("(") == -1) {
                                            var firmatario = firmatariSplitted[i].trim();

                                            //cerca il consigliere exact match - difficile
                                            var consigliereLuceneQuery = "PATH:\"" + firmatariPath + "\" AND TYPE:\"crlatti:consigliereAnagrafica\" AND @cm\\:name:\"*" + firmatario + "\"";
                                            var consigliereResults = search.luceneSearch(consigliereLuceneQuery);
                                            var consigliereAnagraficaNode = null;
                                            if (consigliereResults != null && consigliereResults.length == 1) {
                                                consigliereAnagraficaNode = consigliereResults[0];
                                            }

                                            if (consigliereAnagraficaNode != null) {
                                                var nomeConsigliere = consigliereAnagraficaNode.properties["crlatti:nomeConsigliereAnagrafica"];
                                                var cognomeConsigliere = consigliereAnagraficaNode.properties["crlatti:cognomeConsigliereAnagrafica"];
                                                var gruppoConsigliere = consigliereAnagraficaNode.properties["crlatti:gruppoConsigliereAnagrafica"];
                                                if (checkIsNotNull(nomeConsigliere) && checkIsNotNull(cognomeConsigliere)) {
                                                    var nomeCompletoConsigliere = nomeConsigliere + " " + cognomeConsigliere;

                                                    protocolloLogger.info("firmatario: [" + tipoAtto + " " + numeroAtto + estensioneAtto + "]: " + nomeCompletoConsigliere);

                                                    firmatariArray.push(nomeCompletoConsigliere);

                                                    // creo i nodi di tipo firmatario

                                                    firmatarioNode = firmatariFolderNode.createNode(nomeCompletoConsigliere, "crlatti:firmatario");

                                                    // inserimento proprietà per l'ordinamento 01,02,03 ecc...
                                                    if (i < 10) {
                                                        firmatarioNode.properties["crlatti:numeroOrdinamento"] = "0" + i + "";
                                                    } else {
                                                        firmatarioNode.properties["crlatti:numeroOrdinamento"] = "" + i + "";
                                                    }

                                                    firmatarioNode.properties["crlatti:nomeFirmatario"] = "" + nomeCompletoConsigliere + "";
                                                    firmatarioNode.properties["crlatti:gruppoConsiliare"] = "" + gruppoConsigliere + "";

                                                    if (i == 0) {
                                                        firmatarioNode.properties["crlatti:isPrimoFirmatario"] = true;
                                                    }
                                                    firmatarioNode.content = nomeCompletoConsigliere;
                                                    firmatarioNode.save();


                                                }
                                            }
                                        }
                                    } //for firmatari splitted
                                } // check PAR e PRE
                            }
                        }

                    }

                    // creazione passaggio1

                    // creazione del primo passaggio per le commissioni e l'aula
                    var passaggiXPathQuery = "*[@cm:name='Passaggi']";
                    var passaggiFolderNode = attoFolderNode.childrenByXPath(passaggiXPathQuery)[0];

                    // Creo il primo passaggio solo durante la creazione atto
                    if (!update) {
                        var passaggioSpaceTemplateQuery = "PATH:\"/app:company_home/app:dictionary/app:space_templates/cm:Passaggio\"";
                        var passaggioSpaceTemplateNode = search.luceneSearch(passaggioSpaceTemplateQuery)[0];
                        var passaggioFolderNode = passaggioSpaceTemplateNode.copy(passaggiFolderNode, true); // deep copy
                        passaggioFolderNode.name = "Passaggio1";
                        passaggioFolderNode.save();
                    }

                    //aspect Dgr
                    if (attoFolderNode.hasAspect("crlatti:dgr")) {

                        //dataDgr
                        if (checkIsNotNull(dataDgr)) {
                            var dataDgrSplitted = dataDgr.split("-");
                            var dataDgrParsed = new Date(dataDgrSplitted[0], dataDgrSplitted[1] - 1, dataDgrSplitted[2]);
                            attoFolderNode.properties["crlatti:dataDgr"] = dataDgrParsed;
                        }

                        attoFolderNode.properties["crlatti:numeroDgr"] = numeroDgr;
                    }

                    attoFolderNode.save();
                    attoFolderNode.addAspect("crlatti:importatoDaProtocollo");
                    model.atto = attoFolderNode;

                    protocolloLogger.info("Atto trasferito correttamente dal sistema di protocollo. Atto numero:" + numeroAtto + " estensione:" + estensioneAtto + " tipo:" + tipoAtto + " idProtocollo:" + idProtocollo);

                } else {
                    status.code = 400;
                    status.message = "Tipo atto " + tipoAtto + " non gestito";
                    protocolloLogger.error(status.message);
                    status.redirect = true;

                }

            } else if (checkIsNull(numeroAtto)) {
                status.code = 400;
                status.message = "numero atto non valorizzato";
                protocolloLogger.error(status.message);
                status.redirect = true;

            } else if (checkIsNull(tipoAtto)) {
                status.code = 400;
                status.message = "tipoAtto per atto " + numeroAtto + " non valorizzato";
                protocolloLogger.error(status.message);
                status.redirect = true;

            } else if (checkIsNull(legislatura)) {
                status.code = 400;
                status.message = "Legislatura per atto " + numeroAtto + " non valorizzata";
                protocolloLogger.error(status.message);
                status.redirect = true;

            } else if (checkIsNull(idProtocollo)) {
                status.code = 400;
                status.message = "idProtocollo non valorizzato";
                protocolloLogger.error(status.message);
                status.redirect = true;

            }

        }


    } else {
        status.code = 400;
        status.message = "Tipo atto non valorizzato";
        protocolloLogger.error(status.message);
        status.redirect = true;
    }
} else {
    status.code = 401;
    status.message = "utenza non abilitata ad accedere a questo servizio";
    protocolloLogger.error(status.message);
    status.redirect = true;
}