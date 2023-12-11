<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/anagrafica/Commons.js">

var attiResults = new Array();
var data = args.data;

var dataSplitted = data.split("-");
var valueDate = new Date(dataSplitted[0], dataSplitted[1]-1, dataSplitted[2]);

attiResults = getAttiDellaLegislaturaPerChiusura(valueDate);

model.atto = attiResults;