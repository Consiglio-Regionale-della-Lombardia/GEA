<html>
<head>
<script type="text/javascript">
	
	function creaAtto(atto) {
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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function creaAttoCustom(){
		var currentForm = document.forms[0];
		var numeroAtto = currentForm.numeroAtto.value;
		var estensioneAtto = currentForm.estensioneAtto.value;
		var tipologia = currentForm.tipologia.value;
		var tipoAtto = currentForm.tipoAtto.options[currentForm.tipoAtto.selectedIndex].value;
		var legislatura = currentForm.legislatura.options[currentForm.legislatura.selectedIndex].value;
		
		var attoCustom = {
		"atto" : {
			"numeroAtto" : ""+numeroAtto+"",
			"estensioneAtto" : ""+estensioneAtto+"",
			"tipoAtto" : ""+tipoAtto+"",
			"tipologia" : ""+tipologia+"",
			"legislatura" : ""+legislatura+"",
			"stato" : "Protocollato"
			}
		};
		
		creaAtto(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test creazione atto</h1>
	</div>
	
	<form action="javascript:creaAttoCustom()">
	<table>
		<tr>
			<td>Numero atto:</td>
			<td>
				<input type="text" name="numeroAtto">
			</td>
		</tr>
		<tr>
			<td>Estensione atto:</td>
			<td>
				<input type="text" name="estensioneAtto">
			</td>
		</tr>
		<tr>
			<td>Tipo:</td>
			<td>
				<select name="tipoAtto">
				<#list tipiAttoResults as tipoAtto>
					<option value="${tipoAtto.name}">${tipoAtto.name} - ${tipoAtto.properties.title}</option>
				</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Tipologia:</td>
			<td><input type="text" name="tipologia"></td>
		</tr>
		<tr>
			<td>Legislatura:</td>
			<td>
				<select name="legislatura">
				<#list legislatureResults as legislatura>
					<option value="${legislatura.name}">${legislatura.name}</option>
				</#list>
				</select>
			</td>
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

