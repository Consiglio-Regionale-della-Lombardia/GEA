var commissione1 = "GROUP_X_I Commissione - Programmazione e bilancio";
var commissione1Label = "I Commissione - Programmazione e bilancio";

var commissione1New = "GROUP_X_I Commissione permanente - Programmazione e bilancio";
var commissione1NewLabel = "I Commissione permanente - Programmazione e bilancio";

var commissione2 = "GROUP_X_II Commissione - Affari istituzionali";
var commissione2Label = "II Commissione - Affari istituzionali";

var commissione2New = "GROUP_X_II Commissione permanente - Affari istituzionali";
var commissione2NewLabel = "II Commissione permanente - Affari istituzionali";

var commissione3 = "GROUP_X_III Commissione - Sanità e politiche sociali";
var commissione3Label = "III Commissione - Sanità e politiche sociali";

var commissione3New = "GROUP_X_III Commissione permanente - Sanità e politiche sociali";
var commissione3NewLabel = "III Commissione permanente - Sanità e politiche sociali";

var commissione4 = "GROUP_X_IV Commissione - Attività produttive e occupazione";
var commissione4Label = "IV Commissione - Attività produttive e occupazione";

var commissione4New = "GROUP_X_IV Commissione permanente - Attività produttive e occupazione";
var commissione4NewLabel = "IV Commissione permanente - Attività produttive e occupazione";

var commissione5 = "GROUP_X_V Commissione - Territorio e infrastrutture";
var commissione5Label = "V Commissione - Territorio e infrastrutture";

var commissione5New = "GROUP_X_V Commissione permanente - Territorio e infrastrutture";
var commissione5NewLabel = "V Commissione permanente - Territorio e infrastrutture";

var commissione6 = "GROUP_X_VI Commissione - Ambiente e protezione civile";
var commissione6Label = "VI Commissione - Ambiente e protezione civile";

var commissione6New = "GROUP_X_VI Commissione permanente - Ambiente e protezione civile";
var commissione6NewLabel = "VI Commissione permanente - Ambiente e protezione civile";

var commissione7 = "GROUP_X_VII Commissione - Cultura, istruzione, formazione, comunicazione e sport";
var commissione7Label = "VII Commissione - Cultura, istruzione, formazione, comunicazione e sport";

var commissione7New = "GROUP_X_VII Commissione permanente - Cultura, istruzione, formazione, comunicazione e sport";
var commissione7NewLabel = "VII Commissione permanente - Cultura, istruzione, formazione, comunicazione e sport";

var commissione8 = "GROUP_X_VIII Commissione - Agricoltura, montagna, foreste e parchi";
var commissione8Label = "VIII Commissione - Agricoltura, montagna, foreste e parchi";

var commissione8New = "GROUP_X_VIII Commissione permanente - Agricoltura, montagna, foreste e parchi";
var commissione8NewLabel = "VIII Commissione permanente - Agricoltura, montagna, foreste e parchi";

var coordinatorRole = "Coordinator";

var luceneQueryPda = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti//*\" AND TYPE:\"crlatti:attoPda\"";
var luceneQueryPdl = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti//*\" AND TYPE:\"crlatti:attoPdl\"";
var luceneQueryDoc = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti//*\" AND TYPE:\"crlatti:attoDoc\"";
var luceneQueryInp = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti//*\" AND TYPE:\"crlatti:attoInp\"";
var luceneQueryPar = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti//*\" AND TYPE:\"crlatti:attoPar\"";
var luceneQueryPlp = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti//*\" AND TYPE:\"crlatti:attoPlp\"";
var luceneQueryPre = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti//*\" AND TYPE:\"crlatti:attoPre\"";
var luceneQueryOrg = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti//*\" AND TYPE:\"crlatti:attoOrg\"";
var luceneQueryRef = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti//*\" AND TYPE:\"crlatti:attoRef\"";
var luceneQueryRel = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti//*\" AND TYPE:\"crlatti:attoRel\"";
var luceneQueryEac = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti//*\" AND TYPE:\"crlatti:attoEac\"";
var luceneQueryMis = "PATH:\"/app:company_home/cm:CRL/cm:Gestione_x0020_Atti//*\" AND TYPE:\"crlatti:attoMis\"";

protocolloLogger.info("------- Processamento batch iniziato -------");

//PDA
var attiPda = search.luceneSearch(luceneQueryPda);
for(var i=0; i<attiPda.length; i++){
	var attoPda = attiPda[i];
	protocolloLogger.info("Processamento atto PDA: "+attoPda.name);
	aggiungiNuovaCommissione(attoPda);
}


//PDL
var attiPdl = search.luceneSearch(luceneQueryPdl);
for(var i=0; i<attiPdl.length; i++){
	var attoPdl = attiPdl[i];
	protocolloLogger.info("Processamento atto PDL: "+attoPdl.name);
	aggiungiNuovaCommissione(attoPdl);
}

//DOC
var attiDoc = search.luceneSearch(luceneQueryDoc);
for(var i=0; i<attiDoc.length; i++){
	var attoDoc = attiDoc[i];
	protocolloLogger.info("Processamento atto DOC: "+attoDoc.name);
	aggiungiNuovaCommissione(attoDoc);
}

//INP
var attiInp = search.luceneSearch(luceneQueryInp);
for(var i=0; i<attiInp.length; i++){
	var attoInp = attiInp[i];
	protocolloLogger.info("Processamento atto INP: "+attoInp.name);
	aggiungiNuovaCommissione(attoInp);
}

//PAR
var attiPar = search.luceneSearch(luceneQueryPar);
for(var i=0; i<attiPar.length; i++){
	var attoPar = attiPar[i];
	protocolloLogger.info("Processamento atto PAR: "+attoPar.name);
	aggiungiNuovaCommissione(attoPar);
}

//PLP
var attiPlp = search.luceneSearch(luceneQueryPlp);
for(var i=0; i<attiPlp.length; i++){
	var attoPlp = attiPlp[i];
	protocolloLogger.info("Processamento atto PLP: "+attoPlp.name);
	aggiungiNuovaCommissione(attoPlp);
}

//PRE
var attiPre = search.luceneSearch(luceneQueryPre);
for(var i=0; i<attiPre.length; i++){
	var attoPre = attiPre[i];
	protocolloLogger.info("Processamento atto PRE: "+attoPre.name);
	aggiungiNuovaCommissione(attoPre);
}

//ORG
var attiOrg = search.luceneSearch(luceneQueryOrg);
for(var i=0; i<attiOrg.length; i++){
	var attoOrg = attiOrg[i];
	protocolloLogger.info("Processamento atto ORG: "+attoOrg.name);
	aggiungiNuovaCommissione(attoOrg);
}

//REF
var attiRef = search.luceneSearch(luceneQueryRef);
for(var i=0; i<attiRef.length; i++){
	var attoRef = attiRef[i];
	protocolloLogger.info("Processamento atto REF: "+attoRef.name);
	aggiungiNuovaCommissione(attoRef);
}

//REL
var attiRel = search.luceneSearch(luceneQueryRel);
for(var i=0; i<attiRel.length; i++){
	var attoRel = attiRel[i];
	protocolloLogger.info("Processamento atto REL: "+attoRel.name);
	aggiungiNuovaCommissione(attoRel);
}

//EAC
var attiEac = search.luceneSearch(luceneQueryEac);
for(var i=0; i<attiEac.length; i++){
	var attoEac = attiEac[i];
	protocolloLogger.info("Processamento atto EAC: "+attoEac.name);
	aggiungiNuovaCommissione(attoEac);
}

//MIS
var attiMis = search.luceneSearch(luceneQueryMis);
for(var i=0; i<attiMis.length; i++){
	var attoMis = attiMis[i];
	protocolloLogger.info("Processamento atto MIS: "+attoMis.name);
	aggiungiNuovaCommissione(attoMis);
}

protocolloLogger.info("------- Processamento batch concluso con successo -------");


function aggiornaMetadatoCommissione(atto, commissioneLabel, qname){
	if(commissioneLabel!=null && commissioneLabel!=""){
		
		if(commissioneLabel==commissione1Label){
			atto.properties[qname] = commissione1NewLabel;
		} else if(commissioneLabel==commissione2Label){
			atto.properties[qname] = commissione2NewLabel;
		} else if(commissioneLabel==commissione3Label){
			atto.properties[qname] = commissione3NewLabel;
		} else if(commissioneLabel==commissione4Label){
			atto.properties[qname] = commissione4NewLabel;
		} else if(commissioneLabel==commissione5Label){
			atto.properties[qname] = commissione5NewLabel;
		} else if(commissioneLabel==commissione6Label){
			atto.properties[qname] = commissione6NewLabel;
		} else if(commissioneLabel==commissione7Label){
			atto.properties[qname] = commissione7NewLabel;
		} else if(commissioneLabel==commissione8Label){
			atto.properties[qname] = commissione8NewLabel;
		}

	}
}

function verificaEaggiornaMetadatiCommissioniSuAtto(atto){
//	var commReferente = atto.properties["crlatti:commReferente"];
//	var commCoreferente = atto.properties["crlatti:commCoreferente"];
//	var commConsultiva = atto.properties["crlatti:commConsultiva"];
//	var commRedigente = atto.properties["crlatti:commRedigente"];
//	var commDeliberante = atto.properties["crlatti:commDeliberante"];
//	var commCompetenteMis = atto.properties["crlatti:commissioneCompetenteMis"];
	
//	aggiornaMetadatoCommissione(atto,commReferente,"crlatti:commReferente");
//	aggiornaMetadatoCommissione(atto,commCoreferente,"crlatti:commCoreferente");
//	aggiornaMetadatoCommissione(atto,commConsultiva, "crlatti:commConsultiva");
//	aggiornaMetadatoCommissione(atto,commRedigente, "crlatti:commRedigente");
//	aggiornaMetadatoCommissione(atto,commDeliberante, "crlatti:commDeliberante");
//	aggiornaMetadatoCommissione(atto,commCompetenteMis, "crlatti:commissioneCompetenteMis");
	
	//atto.save();
	
	var passaggiNode = atto.childByNamePath("/Passaggi");
	if(passaggiNode!=null) {
		var passaggi = passaggiNode.getChildAssocsByType("cm:folder");
		if(passaggi!=null){
			for(var z=0; z<passaggi.length; z++){
				var passaggio = passaggi[z];
				protocolloLogger.info("Processamento atto "+atto.name+" | passaggio: "+passaggio.name);
				var commissioniFolderNode = passaggio.childByNamePath("/Commissioni");
				var commissioni = commissioniFolderNode.getChildAssocsByType("crlatti:commissione");
				if(commissioni!=null){
					for(var k=0; k<commissioni.length; k++){
						var commissione = commissioni[k];
						protocolloLogger.info("Processamento atto "+atto.name+" | passaggio: "+passaggio.name+" | commissione: "+commissione.name);
						if(commissione.name==commissione1Label){
							commissione.name=commissione1NewLabel;
						} else if(commissione.name==commissione2Label){
							commissione.name=commissione2NewLabel;
						} else if(commissione.name==commissione3Label){
							commissione.name=commissione3NewLabel;
						} else if(commissione.name==commissione4Label){
							commissione.name=commissione4NewLabel;
						} else if(commissione.name==commissione5Label){
							commissione.name=commissione5NewLabel;
						} else if(commissione.name==commissione6Label){
							commissione.name=commissione6NewLabel;
						} else if(commissione.name==commissione7Label){
							commissione.name=commissione7NewLabel;
						} else if(commissione.name==commissione8Label){
							commissione.name=commissione8NewLabel;
						}
						commissione.save();
						protocolloLogger.info("Fine processamento atto "+atto.name+" | passaggio: "+passaggio.name+" | commissione: "+commissione.name);
					}
				}
				
				
				//Commissioni Annullate
				var commissioniAnnullateFolderNode = passaggio.childByNamePath("/CommissioniAnnullate");
				var commissioniAnnullate = commissioniAnnullateFolderNode.getChildAssocsByType("crlatti:commissione");
				if(commissioniAnnullate!=null){
					for(var k=0; k<commissioniAnnullate.length; k++){
						var commissioneAnnullata = commissioniAnnullate[k];
						var commissioneAnnullataName = commissioneAnnullata.name;
						
						protocolloLogger.info("Processamento atto "+atto.name+" | passaggio: "+passaggio.name+" | commissione annullata: "+commissioneAnnullataName);
						
						var commissioneAnnullataSplitted = commissioneAnnullataName.split("#");
						var commissioneAnnullataTerm = commissioneAnnullataSplitted[0];
						var newName = commissioneAnnullataTerm;
						if(commissioneAnnullataTerm==commissione1Label){
							newName = commissione1NewLabel+"#"+commissioneAnnullataSplitted[1];
							commissioneAnnullata.name=newName;
						} else if(commissioneAnnullataTerm==commissione2Label){
							newName = commissione2NewLabel+"#"+commissioneAnnullataSplitted[1];
							commissioneAnnullata.name=newName;
						} else if(commissioneAnnullataTerm==commissione3Label){
							newName = commissione3NewLabel+"#"+commissioneAnnullataSplitted[1];
							commissioneAnnullata.name=newName;
						} else if(commissioneAnnullataTerm==commissione4Label){
							newName = commissione4NewLabel+"#"+commissioneAnnullataSplitted[1];
							commissioneAnnullata.name=newName;
						} else if(commissioneAnnullataTerm==commissione5Label){
							newName = commissione5NewLabel+"#"+commissioneAnnullataSplitted[1];
							commissioneAnnullata.name=newName;
						} else if(commissioneAnnullataTerm==commissione6Label){
							newName = commissione6NewLabel+"#"+commissioneAnnullataSplitted[1];
							commissioneAnnullata.name=newName;
						} else if(commissioneAnnullataTerm==commissione7Label){
							newName = commissione7NewLabel+"#"+commissioneAnnullataSplitted[1];
							commissioneAnnullata.name=newName;
						} else if(commissioneAnnullataTerm==commissione8Label){
							newName = commissione8NewLabel+"#"+commissioneAnnullataSplitted[1];
							commissioneAnnullata.name=newName;
						}
						
						commissioneAnnullata.save();
						
						protocolloLogger.info("Fine processamento  atto "+atto.name+" | passaggio: "+passaggio.name+" | commissione annullata: "+commissioneAnnullataName);
						
					}
				}
				protocolloLogger.info("Fine processamento atto "+atto.name+" passaggio: "+passaggio.name);
			}
		}
	}
	protocolloLogger.info("Fine processamento atto: "+atto.name);
}

function verificaEaggiungiCommissione(atto, gruppo, commissionePregresso,commissioneNuova, commissioneNuovaLabel){
	if(gruppo==commissionePregresso){
		atto.setPermission(coordinatorRole,commissioneNuova);
	}

}

function aggiungiNuovaCommissione(atto){

	//leggere i gruppi
	//var permessi = atto.getPermissions();
	
//	for(var j=0; j<permessi.length; j++){
		
		//verificare l'esistenza di una commissione da aggiungere
//		var permesso = permessi[j];
//		var permessoArray = permesso.split(";");
//		var gruppo = permessoArray[1];
		
//		verificaEaggiungiCommissione(atto, gruppo, commissione1, commissione1New, commissione1NewLabel);
//		verificaEaggiungiCommissione(atto, gruppo, commissione2, commissione2New, commissione2NewLabel);
//		verificaEaggiungiCommissione(atto, gruppo, commissione3, commissione3New, commissione3NewLabel);
//		verificaEaggiungiCommissione(atto, gruppo, commissione4, commissione4New, commissione4NewLabel);
//		verificaEaggiungiCommissione(atto, gruppo, commissione5, commissione5New, commissione5NewLabel);
//		verificaEaggiungiCommissione(atto, gruppo, commissione6, commissione6New, commissione6NewLabel);
//		verificaEaggiungiCommissione(atto, gruppo, commissione7, commissione7New, commissione7NewLabel);
//		verificaEaggiungiCommissione(atto, gruppo, commissione8, commissione8New, commissione8NewLabel);
			
		
		
		//}
	
	verificaEaggiornaMetadatiCommissioniSuAtto(atto);
		
	}