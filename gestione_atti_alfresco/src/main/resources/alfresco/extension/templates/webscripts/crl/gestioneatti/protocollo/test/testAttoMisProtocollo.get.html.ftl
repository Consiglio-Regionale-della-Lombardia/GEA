<html>
<head>
<script type="text/javascript">
	
	function creaAtto(atto) {
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				document.getElementById("myDiv").innerHTML = xmlhttp.responseText;
			}
		}
		xmlhttp.open("POST", "/alfresco/service/crl/protocollo/synch/atto?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function creaAttoCustom(){
		var currentForm = document.forms[0];
		var numeroAtto = currentForm.numeroAtto.value;
		var tipoAtto = currentForm.tipoAtto.options[currentForm.tipoAtto.selectedIndex].value;
		var idProtocollo = currentForm.idProtocollo.value;
		
		var note = currentForm.note.value;
		var dataIniziativaComitato = currentForm.dataIniziativaComitato.value;
		var dataPropostaCommissione = currentForm.dataPropostaCommissione.value;
		var commissioneCompetente = currentForm.commissioneCompetente.value;
		var esitoVotoIntesa = currentForm.esitoVotoIntesa.value;
		var dataIntesa = currentForm.dataIntesa.value;
		var dataRispostaComitato = currentForm.dataRispostaComitato.value;
		var dataApprovazioneProgetto = currentForm.dataApprovazioneProgetto.value;
		var dataApprovazioneUdp = currentForm.dataApprovazioneUdp.value;
		var numeroAttoUdp = currentForm.numeroAttoUdp.value;
		var istitutoIncaricato = currentForm.istitutoIncaricato.value;
		var scadenzaMv = currentForm.scadenzaMv.value;
		var dataEsameRapportoFinale = currentForm.dataEsameRapportoFinale.value;
		var dataTrasmissioneACommissioni = currentForm.dataTrasmissioneACommissioni.value;
		
		var attoCustom = {
		"atto" : {
			"numeroAtto" : ""+numeroAtto+"",
			"tipoAtto" : "MIS",
			"idProtocollo" : ""+idProtocollo+"",
			"dataIniziativaComitato" : ""+dataIniziativaComitato+"",
			"dataPropostaCommissione" : ""+dataPropostaCommissione+"",
			"commissioneCompetente" : ""+commissioneCompetente+"",
			"esitoVotoIntesa" : ""+esitoVotoIntesa+"",
			"dataIntesa" : ""+dataIntesa+"",
			"dataRispostaComitato" : ""+dataRispostaComitato+"",
			"dataApprovazioneProgetto" : ""+dataApprovazioneProgetto+"",
			"dataApprovazioneUdp" : ""+dataApprovazioneUdp+"",
			"numeroAttoUdp" : ""+numeroAttoUdp+"",
			"istitutoIncaricato" : ""+istitutoIncaricato+"",
			"scadenzaMv" : ""+scadenzaMv+"",
			"dataEsameRapportoFinale" : ""+dataEsameRapportoFinale+"",
			"dataTrasmissioneACommissioni" : ""+dataTrasmissioneACommissioni+"",
			"note" : ""+note+"",
			"legislatura" : "",
			"tipologia" : ""
			}
		};
		
		creaAtto(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Protocollo - Test creazione atto MIS</h1>
	</div>
	
	<form action="javascript:creaAttoCustom()">
	<table>
		<tr>
			<td>ID protocollo:</td>
			<td>
				<input type="text" name="idProtocollo">
			</td>
		</tr>
		<tr>
			<td>Numero atto:</td>
			<td>
				<input type="text" name="numeroAtto">
			</td>
		</tr>
		<tr>
			<td>Tipo:</td>
			<td>
				<select name="tipoAtto">
				
					<option value="MIS" selected="selected">MIS</option>
				
				</select>
			</td>
		</tr>
		<tr>
			<td>Data iniziativa comitato:</td>
			<td><input type="text" name="dataIniziativaComitato" value="2012-10-11"></td>
		</tr>
		<tr>
			<td>Data proposta commissione:</td>
			<td><input type="text" name="dataPropostaCommissione" value="2012-10-11"></td>
		</tr>
		<tr>
			<td>Commissione competente:</td>
			<td><input type="text" name="commissioneCompetente" value="Commissione1"></td>
		</tr>
		<tr>
			<td>Esito voto intesa:</td>
			<td><input type="text" name="esitoVotoIntesa" value="Approvato"></td>
		</tr>
		<tr>
			<td>Data intesa:</td>
			<td><input type="text" name="dataIntesa" value="2012-10-11"></td>
		</tr>
		<tr>
			<td>Data risposta comitato:</td>
			<td><input type="text" name="dataRispostaComitato" value="2012-10-11"></td>
		</tr>
		<tr>
			<td>Data approvazione progetto:</td>
			<td><input type="text" name="dataApprovazioneProgetto" value="2012-10-11"></td>
		</tr>
		<tr>
			<td>Data approvazione UDP:</td>
			<td><input type="text" name="dataApprovazioneUdp" value="2012-10-11"></td>
		</tr>
		<tr>
			<td>Numero atto UDP:</td>
			<td><input type="text" name="numeroAttoUdp" value="12345"></td>
		</tr>
		<tr>
			<td>Istituto incaricato:</td>
			<td><input type="text" name="istitutoIncaricato" value="Consiglio Regionale"></td>
		</tr>
		<tr>
			<td>Scadenza MV:</td>
			<td><input type="text" name="scadenzaMv" value="2012-10-11"></td>
		</tr>
		<tr>
			<td>Data esame rapporto finale:</td>
			<td><input type="text" name="dataEsameRapportoFinale" value="2012-10-11"></td>
		</tr>
		<tr>
			<td>Data trasmissione a commissioni:</td>
			<td><input type="text" name="dataTrasmissioneACommissioni" value="2012-10-11"></td>
		</tr>
		<tr>
			<td>Note:</td>
			<td><input type="text" name="note" value="prova note"></td>
		</tr>
		<tr>
		<td></td>
		<td><input type="submit"></td>
		</tr>
	</table>
	
	
		
	</form>
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

