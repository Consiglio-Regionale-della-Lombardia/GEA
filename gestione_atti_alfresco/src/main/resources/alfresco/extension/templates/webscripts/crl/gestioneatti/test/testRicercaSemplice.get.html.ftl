<html>
<head>
<script type="text/javascript">
	
	function ricerca(atto) {
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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/ricerca/semplice?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function ricercaSemplice(){
		var currentForm = document.forms[0];
		var numeroAtto = currentForm.numeroAtto.value;
		var stato = currentForm.stato.value;
		var tipoAtto = currentForm.tipoAtto.options[currentForm.tipoAtto.selectedIndex].value;
		var legislatura = currentForm.legislatura.options[currentForm.legislatura.selectedIndex].value;
		var numeroProtocollo = currentForm.numeroProtocollo.value;
		var numeroDcr = currentForm.numeroDcr.value;
		var oggetto = currentForm.oggetto.value;
		var dataIniziativaDa = currentForm.dataIniziativaDa.value;
		var dataIniziativaA = currentForm.dataIniziativaA.value;
		var tipoIniziativa = currentForm.tipoIniziativa.value;
		var primoFirmatario = currentForm.primoFirmatario.value;
		var firmatario = currentForm.firmatario.value;
		
		var jsonRicerca = {
		"atto" : {
			"numeroAtto" : ""+numeroAtto+"",
			"tipoAtto" : ""+tipoAtto+"",
			"stato" : ""+stato+"",
			"oggetto" : ""+oggetto+"",
			"legislatura" : ""+legislatura+"",
			"numeroProtocollo" : ""+numeroProtocollo+"",
			"tipoIniziativa" : ""+tipoIniziativa+"",
			"numeroDcr" : ""+numeroDcr+"",
			"primoFirmatario" : ""+primoFirmatario+"",
			"firmatario" : ""+firmatario+"",
			"dataIniziativaDa" : ""+dataIniziativaDa+"",
			"dataIniziativaA" : ""+dataIniziativaA+""
			}
		};
		
		ricerca(JSON.stringify(jsonRicerca));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test ricerca semplice atti</h1>
	</div>
	
	<form action="javascript:ricercaSemplice()">
	<table>
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
					<option value="" selected="selected"></option>
				<#list tipiAttoResults as tipoAtto>
					<option value="${tipoAtto.name}">${tipoAtto.name} - ${tipoAtto.properties.title}</option>
				</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Legislatura:</td>
			<td>
				<select name="legislatura">
					<option value="" selected="selected"></option>
				<#list legislatureResults as legislatura>
					<option value="${legislatura.name}">${legislatura.name}</option>
				</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Stato:</td>
			<td><input type="text" name="stato"></td>
		</tr>
		<tr>
			<td>Numero protocollo:</td>
			<td><input type="text" name="numeroProtocollo"></td>
		</tr>
		<tr>
			<td>Numero DCR:</td>
			<td><input type="text" name="numeroDcr"></td>
		</tr>
		<tr>
			<td>Oggetto:</td>
			<td><input type="text" name="oggetto"></td>
		</tr>
		<tr>
			<td>Data iniziativa Da:</td>
			<td><input type="text" name="dataIniziativaDa"></td>
		</tr>
		<tr>
			<td>Data iniziativa A:</td>
			<td><input type="text" name="dataIniziativaA"></td>
		</tr>
		<tr>
			<td>Tipo iniziativa:</td>
			<td><input type="text" name="tipoIniziativa"></td>
		</tr>
		<tr>
			<td>Primo firmatario:</td>
			<td><input type="text" name="primoFirmatario"></td>
		</tr>
		<tr>
			<td>Firmatario:</td>
			<td><input type="text" name="firmatario"></td>
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

