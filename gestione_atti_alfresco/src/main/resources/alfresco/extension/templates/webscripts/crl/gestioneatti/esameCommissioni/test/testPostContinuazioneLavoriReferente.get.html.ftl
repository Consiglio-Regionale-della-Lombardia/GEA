<html>
<head>
<script type="text/javascript">
	function creaContinuazioneReferente(atto) {
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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/esamecommissioni/continuazionereferente?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	


	function creaContinuazioneReferenteCommissione(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
		var commissione = currentForm.commissione.value;
		var passaggio = currentForm.passaggio.value;
		var dataSedutaContinuazioneInReferente = currentForm.dataSedutaContinuazioneInReferente.value;
		var motivazioniContinuazioneInReferente = currentForm.motivazioniContinuazioneInReferente.value;
	
		
		var attoCustom = {
			"target":{
				"commissione": ""+commissione+"",
				"passaggio": ""+passaggio+""
			},
			"atto": {
				"id": ""+idAtto+"",
				"stato": "continuazione referente",
				"passaggi": [
					{
						
							"nome" : "Passaggio1",
							"commissioni" : [
								{
							 			"descrizione": ""+commissione+"",
							 			"motivazioniContinuazioneInReferente": ""+motivazioniContinuazioneInReferente+"",
										"dataSedutaContinuazioneInReferente": ""+dataSedutaContinuazioneInReferente+"",
										"stato": "continuazione referente",
										"ruolo": "Referente"
									
								},
								{
							 			"descrizione": "commissioneStaticaTest",
										"motivazioniContinuazioneInReferente": "Motivazioni per il passaggio",
										"dataSedutaContinuazioneInReferente": "2012-09-08",
										"stato": "continuazione referente",
										"ruolo": "Referente"
									
								}
							]
						
					}
				
				]
				 
			}
		};
		
	
		
		creaContinuazioneReferente(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test continazione referente commissione atto</h1>
	</div>
	
	<form action="javascript:creaContinuazioneReferenteCommissione()">
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
			<td>Commissione:</td>
			<td>
				<input type="text" name="commissione"/>
			</td>
		</tr>
		<tr>
			<td>Passaggio:</td>
			<td>
				<input type="text" name="passaggio"/>
			</td>
		</tr>
		<tr>
			<td>Data Seduta Continuazione In Referente</td>
			<td>
				<input type="text" name="dataSedutaContinuazioneInReferente"/>
			</td>
		</tr>
		<tr>
			<td>Motivazioni Continuazione In Referente</td>
			<td>
				<input type="text" name="motivazioniContinuazioneInReferente"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="continuazione refrente"></td>
		</tr>
	</table>
	
	
		
	</form>
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

