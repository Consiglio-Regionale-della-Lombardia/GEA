<html>
<head>
<script type="text/javascript">
	function creaPresaInCarico(atto) {
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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/esamecommissioni/presaincarico?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function creaPresaInCaricoCommissione(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
		var commissione = currentForm.commissione.value;
		var passaggio = currentForm.passaggio.value;
		var dataPresaInCarico = currentForm.dataPresaInCarico.value;
		var materia = currentForm.materia.value;
		var dataScadenza = currentForm.dataScadenza.value;
		
		var attoCustom = {
			"target":{
				"commissione": ""+commissione+"",
				"passaggio": ""+passaggio+""
			},
			"atto": {
				"id": ""+idAtto+"",
				"stato": "Preso in carico da Commissione",
				"passaggi": [
					{
						
							"nome" : "Passaggio1",
							"commissioni" : [
								{
						 			"descrizione": ""+commissione+"",
									"dataPresaInCarico": ""+dataPresaInCarico+"",
									"materia": ""+materia+"",
									"ruolo": "Referente",
									"dataScadenza": ""+dataScadenza+"",
									"stato": "In carico"
								},
								{
						 			"descrizione": "commissioneStaticaTest",
									"dataPresaInCarico": "2012-08-08",
									"materia": "materiaTest",
									"ruolo": "Consultiva",
									"dataScadenza": "2012-09-08",
									"stato": "In carico"
								}
							]
						
					}
				]
				 
			}
		};
		
	
		
		creaPresaInCarico(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test presa in carico commissione atto</h1>
	</div>
	
	<form action="javascript:creaPresaInCaricoCommissione()">
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
			<td>Data presa in carico</td>
			<td>
				<input type="text" name="dataPresaInCarico"/>
			</td>
		</tr>
		<tr>
			<td>Materia:</td>
			<td>
				<input type="text" name="materia"/>
			</td>
		</tr>
		<tr>
			<td>Data scadenza:</td>
			<td>
				<input type="text" name="dataScadenza"/>
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

