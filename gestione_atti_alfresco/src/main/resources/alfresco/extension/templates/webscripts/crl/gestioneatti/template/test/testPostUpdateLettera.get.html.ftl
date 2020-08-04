<html>
<head>
<script type="text/javascript">
	
	function creaDatiLettera(atto) {
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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/template/lettere/dati?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function aggiornaDatiCustom(){
		var currentForm = document.forms[0];
		var tipoTemplate = currentForm.tipoTemplate.value;
		var firmatario = currentForm.firmatario.value;
		var ufficio = currentForm.ufficio.value;
		var direzione = currentForm.direzione.value;

		var letteraCustom = {
		"lettera" : {
			"tipoTemplate" : ""+tipoTemplate+"",
			"firmatario" : ""+firmatario+"",
			"ufficio" : ""+ufficio+"",
			"direzione" : ""+direzione+""
			}
		};
		
		creaDatiLettera(JSON.stringify(letteraCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test edit dati lettara</h1>
	</div>
	
	<form action="javascript:aggiornaDatiCustom()">
	<table>
		<tr>
			<td>Tipo Template:</td>
			<td>
				<input type="text" name="tipoTemplate" value="crltemplate:letteraTrasmissioneLCRAula">
			</td>
		</tr>
		<tr>
			<td>Firmatario:</td>
			<td>
				<input type="text" name="firmatario" value="Mario Rossi">
			</td>
		</tr>
		<tr>
			<td>Ufficio</td>
			<td><input type="text" name="ufficio" value="AE/888/RC"></td>
		</tr>
		<tr>
			<td>Direzione</td>
			<td><input type="text" name="direzione" value="Direzione di test"></td>
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

