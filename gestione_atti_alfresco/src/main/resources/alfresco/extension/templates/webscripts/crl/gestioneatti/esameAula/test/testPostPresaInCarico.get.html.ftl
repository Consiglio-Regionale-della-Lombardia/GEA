<html>
<head>
<script type="text/javascript">
	function creaEsameAssemblea(atto) {
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				document.getElementById("myDiv").innerHTML = "status: "+xmlhttp.status;
			}
		}
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/esameaula/presaincarico?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function creaEsameAssembleaAula(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
		var passaggio = currentForm.passaggio.value;
		var esitoVotoAula = currentForm.esitoVotoAula.value;
		var tipologiaVotazione = currentForm.tipologiaVotazione.value;
		var dataSedutaAula = currentForm.dataSedutaAula.value;
		var numeroDcr = currentForm.numeroDcr.value;
		var numeroLcr = currentForm.numeroLcr.value;
		var noteVotazione = currentForm.noteVotazione.value;
		var emendato = currentForm.emendato.options[currentForm.emendato.selectedIndex].value;
		
		var attoCustom = {
			"target":{
				"passaggio": ""+passaggio+""
			},
			"atto": {
				"id": ""+idAtto+"",
				"stato": "Votato in aula",
				"passaggi": [
					{
						
							"nome" : "Passaggio1",
							"aula" : 
								{
						 			"esitoVotoAula": ""+esitoVotoAula+"",
						 			"tipologiaVotazione": ""+tipologiaVotazione+"",
						 			"dataSedutaAula": ""+dataSedutaAula+"",
						 			"numeroDcr": ""+numeroDcr+"",
						 			"numeroLcr": ""+numeroLcr+"",
						 			"emendato": ""+emendato+"",
						 			"noteVotazione": ""+noteVotazione+""
						 			
								}
						
					}
				]
				 
			}
		};
		
	
		
		creaEsameAssemblea(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test presa in carico aula atto</h1>
	</div>
	
	<form action="javascript:creaEsameAssembleaAula()">
	<table>
		<tr>
			<td>
				Atto:
			</td>
			<td>
				<select name="idAtto">
					<#list atti as atto>
						<option value="${atto.nodeRef}">${atto.name}</option>
					</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Tipologia Votazione:</td>
			<td>
				<input type="text" name="tipologiaVotazione"/>
			</td>
		</tr>
		var dataSedutaAula = currentForm.dataSedutaAula.value;
		var numeroDcr = currentForm.numeroDcr.value;
		var numeroLcr = currentForm.numeroLcr.value;
		var noteVotazione = currentForm.noteVotazione.value;
		var emendato = currentForm.emendato.options[currentForm.emendato.selectedIndex].value;
		<tr>
			<td>Esito Voto Aula</td>
			<td>
				<input type="text" name="esitoVotoAula"/>
			</td>
		</tr>
		<tr>
			<td><Data Seduta Aula</td>
			<td>
				<input type="text" name="dataSedutaAula"/>
			</td>
		</tr>
		<tr>
			<td>Relazione scritta:</td>
			<td>
				<input type="text" name="relazioneScritta"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="presa in carico"></td>
		</tr>
	</table>
	
	
		
	</form>
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

