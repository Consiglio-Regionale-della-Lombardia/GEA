<html>
<head>
<script type="text/javascript">
	function creaStralcio(atto) {
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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/esameaula/stralcio?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function creaStralcioAula(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
		var passaggio = currentForm.passaggio.value;
		
		var dataSedutaStralcio = currentForm.dataSedutaStralcio.value;
		var dataStralcio = currentForm.dataStralcio.value;
		var dataIniziativaStralcio = currentForm.dataIniziativaStralcio.value;
		var articoli = currentForm.articoli.value;
		var noteStralcio = currentForm.noteStralcio.value;
		var quorumEsameAula = currentForm.quorumEsameAula.value;

		
		
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
						 			"dataSedutaStralcio": ""+dataSedutaStralcio+"",
						 			"dataStralcio": ""+dataStralcio+"",
						 			"dataIniziativaStralcio": ""+dataIniziativaStralcio+"",
						 			"articoli": ""+articoli+"",
						 			"noteStralcio": ""+noteStralcio+"",
						 			"quorumEsameAula": ""+quorumEsameAula+""
								}
						
					}
				]
				 
			}
		};
		
	
		
		creaStralcio(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test stralcio aula atto</h1>
	</div>
	
	<form action="javascript:creaStralcioAula()">
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
			<td>Data Seduta Stralcio</td>
			<td>
				<input type="text" name="dataSedutaStralcio"/>
			</td>
		</tr>
		<tr>
			<td>Data Stralcio</td>
			<td>
				<input type="text" name="dataStralcio"/>
			</td>
		</tr>
		<tr>
			<td>Data Iniziativa Stralcio</td>
			<td>
				<input type="text" name="dataIniziativaStralcio"/>
			</td>
		</tr>
			<tr>
			<td>Articoli</td>
			<td>
				<input type="text" name="articoli"/>
			</td>
		</tr>
		<tr>
			<td>Note Stralcio</td>
			<td>
				<input type="text" name="noteStralcio"/>
			</td>
		</tr>
		<tr>
			<td>Quorum Esame Aula</td>
			<td>
				<input type="text" name="quorumEsameAula"/>
			</td>
		</tr>
		
	
	
		<tr>
			<td></td>
			<td><input type="submit" value="stralcio"></td>
		</tr>
	</table>
	
	
		
	</form>
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

