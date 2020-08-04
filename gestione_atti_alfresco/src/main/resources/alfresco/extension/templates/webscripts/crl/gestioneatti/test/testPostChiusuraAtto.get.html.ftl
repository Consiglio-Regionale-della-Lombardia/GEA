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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/chiusuraatto?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function aggiornaDatiCustom(){
		var currentForm = document.forms[0];
		var id = currentForm.id.options[currentForm.id.selectedIndex].value;
		var stato = currentForm.stato.value;		
		var dataChiusura = currentForm.dataChiusura.value;
		var tipoChiusura = currentForm.tipoChiusura.value;
		var noteChiusuraIter = currentForm.noteChiusuraIter.value;
		var numeroLr = currentForm.numeroLr.value;
		var dataLr = currentForm.dataLr.value;
		var numeroPubblicazioneBURL = currentForm.numeroPubblicazioneBURL.value;
		var dataPubblicazioneBURL = currentForm.dataPubblicazioneBURL.value;

		var attoCustom = {
		"atto" : {
			"id" : ""+id+"",
			"stato" : ""+stato+"",
			"dataChiusura" : ""+dataChiusura+"",
			"tipoChiusura" : ""+tipoChiusura+"",
			"noteChiusuraIter" : ""+noteChiusuraIter+"",
			"numeroLr" : ""+numeroLr+"",
			"dataLR" : ""+dataLr+"",
			"numeroPubblicazioneBURL" : ""+numeroPubblicazioneBURL+"",
			"dataPubblicazioneBURL" : ""+dataPubblicazioneBURL+""
			}
		};
		
		creaDatiAtto(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test chiusura atto</h1>
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
			<td>Stato:</td>
			<td><input type="text" name="stato" value="Lo stato custom"></td>
		</tr>
		<tr>
			<td>Data chiusura:</td>
			<td><input type="text" name="dataChiusura" value="2012-08-01"></td>
		</tr>
		<tr>
			<td>Tipo chiusura:</td>
			<td><input type="text" name="tipoChiusura" value="tipo1"></td>
		</tr>
		<tr>
			<td>Note chiusura:</td>
			<td><input type="text" name="noteChiusuraIter" value="note chiusura"></td>
		</tr>
		
		<tr>
			<td>Numero Lr:</td>
			<td><input type="text" name="numeroLr" value="numero lr"></td>
		</tr>
		<tr>
			<td>Data Lr:</td>
			<td><input type="text" name="dataLr" value="2012-09-05"></td>
		</tr>
		<tr>
			<td>Numero BURL:</td>
			<td><input type="text" name="numeroPubblicazioneBURL" value="numero burl"></td>
		</tr>
		<tr>
			<td>Data BURL:</td>
			<td><input type="text" name="dataPubblicazioneBURL" value="2012-09-08"></td>
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

