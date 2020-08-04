<html>
<head>
<script type="text/javascript">
	function creaRinvio(atto) {
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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/esameaula/rinvio?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function creaRinvioAula(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
		var passaggio = currentForm.passaggio.value;
		
		var dataSedutaRinvio = currentForm.dataSedutaRinvio.value;
		var dataTermineMassimo = currentForm.dataTermineMassimo.value;
		var motivazioneRinvio = currentForm.motivazioneRinvio.value;
		
		
		var attoCustom = {
			"target":{
				"passaggio": ""+passaggio+""
			},
			"atto": {
				"id": ""+idAtto+"",
				"stato": "Preso in carico da Aula",
				"passaggi": [
					{
						
							"nome" : "Passaggio1",
							"aula" : 
								{
						 			"dataSedutaRinvio": ""+dataSedutaRinvio+"",
						 			"dataTermineMassimo": ""+dataTermineMassimo+"",
						 			"motivazioneRinvio": ""+motivazioneRinvio+""
								}
						
					}
				]
				 
			}
		};
		
	
		
		creaRinvio(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test rinvio aula atto</h1>
	</div>
	
	<form action="javascript:creaRinvioAula()">
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
			<td>Passaggio:</td>
			<td>
				<input type="text" name="passaggio"/>
			</td>
		</tr>
		<tr>
			<td>Data Seduta Rinvio</td>
			<td>
				<input type="text" name="dataSedutaRinvio"/>
			</td>
		</tr>
		<tr>
			<td>Data Termine Massimo</td>
			<td>
				<input type="text" name="dataTermineMassimo"/>
			</td>
		</tr>
		<tr>
			<td>Motivazione Rinvio</td>
			<td>
				<input type="text" name="motivazioneRinvio"/>
			</td>
		</tr>
		
	
		<tr>
			<td></td>
			<td><input type="submit" value="rinvio"></td>
		</tr>
	</table>
	
	
		
	</form>
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

