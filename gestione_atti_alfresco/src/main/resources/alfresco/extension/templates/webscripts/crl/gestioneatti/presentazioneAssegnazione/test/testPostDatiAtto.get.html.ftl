<html>
<head>
<script type="text/javascript">
	
	function creaDatiAtto(atto) {
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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/dati?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function aggiornaDatiCustom(){
		var currentForm = document.forms[0];
		var id = currentForm.id.options[currentForm.id.selectedIndex].value;
		var classificazione = currentForm.classificazione.value;
		var oggetto = currentForm.oggetto.value;
		var numeroRepertorio = currentForm.numeroRepertorio.value;
		var dataRepertorio = currentForm.dataRepertorio.value;
		var tipoIniziativa = currentForm.tipoIniziativa.value;
		var dataIniziativa = currentForm.dataIniziativa.value;
		var descrizioneIniziativa = currentForm.descrizioneIniziativa.value;
		var assegnazione = currentForm.assegnazione.value;
		var numeroDgr = currentForm.numeroDgr.value;
		var dataDgr = currentForm.dataDgr.value;

		var attoCustom = {
		"atto" : {
			"id" : ""+id+"",
			"classificazione" : ""+classificazione+"",
			"oggetto" : ""+oggetto+"",
			"numeroRepertorio" : ""+numeroRepertorio+"",
			"dataRepertorio" : ""+dataRepertorio+"",
			"tipoIniziativa" : ""+tipoIniziativa+"",
			"dataIniziativa" : ""+dataIniziativa+"",
			"descrizioneIniziativa" : ""+descrizioneIniziativa+"",
			"assegnazione" : ""+assegnazione+"",
			"numeroDgr" : ""+numeroDgr+"",
			"dataDgr" : ""+dataDgr+"",
			"firmatari" : [
				{
					"firmatario":{
						"descrizione":"Mario Rossi",
						"gruppoConsiliare":"PD",
						"dataFirma":"2009-09-08",
						"dataRitiro":"",
						"primoFirmatario":false
					}
				},
				{
					"firmatario":{
						"descrizione":"Carlo Verdi",
						"gruppoConsiliare":"IDV",
						"dataFirma":"2009-09-08",
						"dataRitiro":"",
						"primoFirmatario":true
					}
				}
			]
			}
		};
		
		creaDatiAtto(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test modifica dati atto</h1>
	</div>
	
	<form action="javascript:aggiornaDatiCustom()">
	<table>
		<tr>
			<td>Id:</td>
			<td>
				<select name="id">
				<#list atti as atto>
					<option value="${atto.nodeRef}">${atto.name}</option>
				</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Classificazione:</td>
			<td><input type="text" name="classificazione"></td>
		</tr>
		<tr>
			<td>Oggetto:</td>
			<td><input type="text" name="oggetto"></td>
		</tr>
		<tr>
			<td>Numero repertorio:</td>
			<td><input type="text" name="numeroRepertorio"></td>
		</tr>
		<tr>
			<td>Data repertorio:</td>
			<td><input type="text" name="dataRepertorio"></td>
		</tr>
		<tr>
			<td>Tipo iniziativa:</td>
			<td><input type="text" name="tipoIniziativa"></td>
		</tr>
		<tr>
			<td>Data iniziativa:</td>
			<td><input type="text" name="dataIniziativa"></td>
		</tr>
		<tr>
			<td>Descrizione iniziativa:</td>
			<td><input type="text" name="descrizioneIniziativa"></td>
		</tr>
		<tr>
			<td>Assegnazione:</td>
			<td><input type="text" name="assegnazione"></td>
		</tr>
		<tr>
			<td>Numero DGR:</td>
			<td><input type="text" name="numeroDgr"></td>
		</tr>
		<tr>
			<td>Data DGR:</td>
			<td><input type="text" name="dataDgr"></td>
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

