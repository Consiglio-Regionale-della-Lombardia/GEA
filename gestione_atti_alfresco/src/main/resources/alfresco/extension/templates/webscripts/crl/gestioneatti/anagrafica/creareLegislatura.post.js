<import resource="classpath:alfresco/extension/templates/webscripts/crl/gestioneatti/anagrafica/Commons.js">


var dataLegislatura = args.data;

var nomeLegislatura = romanize(args.legislatura);

var legislaturaResults = aprireLegislaturaCorrente(dataLegislatura, nomeLegislatura);

model.legislaturaAnagrafica = legislaturaResults;
