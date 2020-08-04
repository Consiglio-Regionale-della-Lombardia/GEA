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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/ammissibilita?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function aggiornaDatiCustom(){
		var currentForm = document.forms[0];
		var id = currentForm.id.options[currentForm.id.selectedIndex].value;
		var valutazioneAmmissibilita = currentForm.valutazioneAmmissibilita.value;
		var dataRichiestaInformazioni = currentForm.dataRichiestaInfo.value;
		var dataRicevimentoInformazioni = currentForm.dataRicevimentoInfo.value;
		var aiutiStato = currentForm.aiutiStato.value;
		var normaFinanziaria = currentForm.normaFinanziaria.value;
		var richiestaUrgenza = currentForm.richiestaUrgenza.value;
		var votazioneUrgenza = currentForm.votazioneUrgenza.value;
		var dataVotazioneUrgenza = currentForm.dataVotazioneUrgenza.value;
		var noteAmmissibilita = currentForm.noteAmmissibilita.value;

		var attoCustom = {
		"atto" : {
			"id" : ""+id+"",
			"valutazioneAmmissibilita" : ""+valutazioneAmmissibilita+"",
			"dataRichiestaInformazioni" : ""+dataRichiestaInformazioni+"",
			"dataRicevimentoInformazioni" : ""+dataRicevimentoInformazioni+"",
			"aiutiStato" : ""+aiutiStato+"",
			"normaFinanziaria" : ""+normaFinanziaria+"",
			"richiestaUrgenza" : ""+richiestaUrgenza+"",
			"votazioneUrgenza" : ""+votazioneUrgenza+"",
			"dataVotazioneUrgenza" : ""+dataVotazioneUrgenza+"",
			"noteAmmissibilita" : ""+noteAmmissibilita+""
			}
		};
		
		creaDatiAtto(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test modifica ammissibilit&agrave; atto</h1>
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
			<td>Valutazione:</td>
			<td><input type="text" name="valutazioneAmmissibilita" value=""></td>
		</tr>
		<tr>
			<td>Data richiesta Info:</td>
			<td><input type="text" name="dataRichiestaInfo" value="2012-08-01"></td>
		</tr>
		<tr>
			<td>Data ricevimento informazioni:</td>
			<td><input type="text" name="dataRicevimentoInfo" value="2012-08-01"></td>
		</tr>
		<tr>
			<td>Aiuti stato:</td>
			<td><input type="text" name="aiutiStato" value="false"></td>
		</tr>
		<tr>
			<td>Norma finanziaria:</td>
			<td><input type="text" name="normaFinanziaria" value="true"></td>
		</tr>
		<tr>
			<td>Richiesta urgenza:</td>
			<td><input type="text" name="richiestaUrgenza" value="true"></td>
		</tr>
		<tr>
			<td>Votazione urgenza:</td>
			<td><input type="text" name="votazioneUrgenza" value="false"></td>
		</tr>
		<tr>
			<td>Data votazione urgenza:</td>
			<td><input type="text" name="dataVotazioneUrgenza" value="2012-08-01"></td>
		</tr>
		<tr>
			<td>Note:</td>
			<td><input type="text" name="noteAmmissibilita" value="La nota di ammissibilita"></td>
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

