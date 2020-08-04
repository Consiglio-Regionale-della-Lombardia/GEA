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
		var id = currentForm.id.value;
		var numeroAtto = currentForm.numeroAtto.value;
		var dataAtto = currentForm.dataAtto.value;
		var tipoAtto = currentForm.tipoAtto.options[currentForm.tipoAtto.selectedIndex].value;
		var note = currentForm.note.value;
		
		var attoCustom = {
		"atto" : {
			"id" : ""+id+"",
			"numeroAtto" : ""+numeroAtto+"",
			"tipoAtto" : ""+tipoAtto+"",
			"dataAtto" : ""+dataAtto+"",
			"note" : ""+note+"",
			"legislatura" : "",
			"tipologia" : ""
			}
		};
		
		creaAtto(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test creazione atto EAC</h1>
	</div>
	
	<form action="javascript:creaAttoCustom()">
	<table>	
		<tr>
			<td>Id:</td>
			<td>
				<input type="text" name="id">
			</td>
		</tr>
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
				
					<option value="EAC" selected="selected">EAC</option>
				
				</select>
			</td>
		</tr>
		<tr>
			<td>Data atto:</td>
			<td><input type="text" name="dataAtto" value="2012-10-11"></td>
		</tr>
		<tr>
			<td>Note:</td>
			<td><input type="text" name="note" value="prova note"></td>
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

