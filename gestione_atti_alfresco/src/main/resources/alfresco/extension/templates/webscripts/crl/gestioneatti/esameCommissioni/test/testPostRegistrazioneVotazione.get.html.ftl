<html>
<head>
<script type="text/javascript">
	function creaRegistraVotazione(atto) {
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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/esamecommissioni/registravotazione?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function creaRegistraVotazioneCommissione(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
		var commissione = currentForm.commissione.value;
		var passaggio = currentForm.passaggio.value;
		var tipoVotazione = currentForm.tipoVotazione.value;
		var esitoVotazione = currentForm.esitoVotazione.value;
		var dataVotazione = currentForm.dataVotazione.value;
	
		
		var attoCustom = {
			"target":{
				"commissione": ""+commissione+"",
				"passaggio": ""+passaggio+""
			},
			"atto": {
				"id": ""+idAtto+"",
				"stato": "votazione comissione",
				"passaggi": [
					{
						
							"nome" : "Passaggio1",
							"commissioni": [
								{
							 			"descrizione": ""+commissione+"",
							 			"quorumEsameCommissioni": ""+tipoVotazione+"",
							 			"esitoVotazione": ""+esitoVotazione+"",
										"dataSedutaCommissione": ""+dataVotazione+"",
										"stato": "votazione comissione",
										"ruolo": "Referente"
									
								},
								{
							 			"descrizione": "commissioneStaticaTest",
										"quorumEsameCommissioni": "tipo",
							 			"esitoVotazione": "esito",
										"dataSedutaCommissione": "2012-09-09",
										"stato": "votazione comissione",
										"ruolo": "Referente"
									
								}
				
							]
						
					}
				]
				 
			}
		};
		
	
		
		creaRegistraVotazione(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test registrazione votazione commissione atto</h1>
	</div>
	
	<form action="javascript:creaRegistraVotazioneCommissione()">
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
			<td>Tipo votazione</td>
			<td>
				<input type="text" name="tipoVotazione"/>
			</td>
		</tr>
		<tr>
			<td>Esito votazione</td>
			<td>
				<input type="text" name="esitoVotazione"/>
			</td>
		</tr>
		<tr>
			<td>Data votazione</td>
			<td>
				<input type="text" name="dataVotazione"/>
			</td>
		</tr>
	
		<tr>
			<td></td>
			<td><input type="submit" value="registra votazione"></td>
		</tr>
	</table>
	
	
		
	</form>
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

