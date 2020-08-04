<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/common.js">

var atto = json.get("atto");
var id = atto.get("id");
var stato = atto.get("stato");

var dataChiusura = filterParam(atto.get("dataChiusura"));
var tipoChiusura = filterParam(atto.get("tipoChiusura"));
var noteChiusuraIter = filterParam(atto.get("noteChiusuraIter"));
var numeroLr = filterParam(atto.get("numeroLr"));
var dataLr = filterParam(atto.get("dataLR"));
var numeroPubblicazioneBURL = filterParam(atto.get("numeroPubblicazioneBURL"));
var dataPubblicazioneBURL = filterParam(atto.get("dataPubblicazioneBURL"));

var numeroDgrSeguito = filterParam(atto.get("numeroDgrSeguito"));
var dataDgrSeguito = filterParam(atto.get("dataDgrSeguito"));
var numRegolamento = filterParam(atto.get("numRegolamento"));
var dataRegolamento = filterParam(atto.get("dataRegolamento"));

//var numeroDcr = filterParam(atto.get("numeroDcr"));


if(atto!=null
		&& id!=null
		&& stato!=null) {
	
	var attoFolderNode = utils.getNodeFromString(id);
	
        if(checkIsNotNull(dataChiusura)) {
            var dataChiusuraSplitted = dataChiusura.split("-");
            var dataChiusuraParsed = new Date(dataChiusuraSplitted[0],dataChiusuraSplitted[1]-1,dataChiusuraSplitted[2]);
            attoFolderNode.properties["crlatti:dataChiusura"] = dataChiusuraParsed;
        } else {
            attoFolderNode.properties["crlatti:dataChiusura"] = null;
        }
	
	attoFolderNode.properties["crlatti:tipoChiusura"] = tipoChiusura;
	attoFolderNode.properties["crlatti:noteChiusura"] = noteChiusuraIter;
	
	attoFolderNode.properties["crlatti:numeroLr"] = numeroLr;
	//attoFolderNode.properties["crlatti:numeroDcr"] = numeroDcr;
	
	if(checkIsNotNull(dataLr)) {
		var dataLrSplitted = dataLr.split("-");
		var dataLrParsed = new Date(dataLrSplitted[0],dataLrSplitted[1]-1,dataLrSplitted[2]);
		attoFolderNode.properties["crlatti:dataLr"] = dataLrParsed;
	} else {
            attoFolderNode.properties["crlatti:dataLr"] = null;
        }
	
	attoFolderNode.properties["crlatti:numeroPubblicazioneBURL"] = numeroPubblicazioneBURL;
	
	if(checkIsNotNull(dataPubblicazioneBURL)) {
		var dataPubblicazioneBURLSplitted = dataPubblicazioneBURL.split("-");
		var dataPubblicazioneBURLParsed = new Date(dataPubblicazioneBURLSplitted[0],dataPubblicazioneBURLSplitted[1]-1,dataPubblicazioneBURLSplitted[2]);
		attoFolderNode.properties["crlatti:dataPubblicazioneBURL"] = dataPubblicazioneBURLParsed;
	} else {
            attoFolderNode.properties["crlatti:dataPubblicazioneBURL"] = null;
        }
	
	attoFolderNode.properties["crlatti:numeroDgrSeguito"] = numeroDgrSeguito;
	
	if(checkIsNotNull(dataDgrSeguito)) {
		var dataDgrSeguitoSplitted = dataDgrSeguito.split("-");
		var dataDgrSeguitoParsed = new Date(dataDgrSeguitoSplitted[0],dataDgrSeguitoSplitted[1]-1,dataDgrSeguitoSplitted[2]);
		attoFolderNode.properties["crlatti:dataDgrSeguito"] = dataDgrSeguitoParsed;
	}else{
		attoFolderNode.properties["crlatti:dataDgrSeguito"] = null;
	}
        
        attoFolderNode.properties["crlatti:numRegolamento"] = numRegolamento;
	
	if(checkIsNotNull(dataRegolamento)) {
		var dataRegolamentoSplitted = dataRegolamento.split("-");
		var dataRegolamentoParsed = new Date(dataRegolamentoSplitted[0],dataRegolamentoSplitted[1]-1,dataRegolamentoSplitted[2]);
		attoFolderNode.properties["crlatti:dataRegolamento"] = dataRegolamentoParsed;
	}else{
		attoFolderNode.properties["crlatti:dataRegolamento"] = null;
	}

	attoFolderNode.properties["crlatti:statoAtto"] = stato;
	attoFolderNode.save();
	
} else {
	status.code = 500;
	status.message = "atto, id, stato sono obbligatori";
	status.redirect = true;
}