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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/abbinamento?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function aggiornaDatiCustom(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
		var idAttoAbbinato = currentForm.idAttoAbbinato.options[currentForm.idAttoAbbinato.selectedIndex].value;
		var dataAbbinamento = currentForm.dataAbbinamento.value;
		var dataDisabbinamento = currentForm.dataDisabbinamento.value;
		var tipoTesto = currentForm.tipoTesto.value;
		var passaggio = currentForm.passaggio.value;

		var attoCustom = {
		"target" : {
			"passaggio" : ""+passaggio+""
		},
		"abbinamento" : {
			"idAtto" : ""+idAtto+"",
			"idAttoAbbinato" : ""+idAttoAbbinato+"",
			"tipoTesto" : ""+tipoTesto+"",
			"dataAbbinamento" : ""+dataAbbinamento+"",
			"dataDisabbinamento" : ""+dataDisabbinamento+""
			}
		};
		
		creaDatiAtto(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test post abbinamento atto</h1>
	</div>
	
	<form action="javascript:aggiornaDatiCustom()">
	<table>
		<tr>
			<td>
				Atto padre:
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
			<td>
				Atto da abbinare:
			</td>
			<td>
				<select name="idAttoAbbinato">
					<#list atti as atto>
						<option value="${atto.nodeRef}">${atto.name}</option>
					</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Data abbinamento:</td>
			<td>
				<input name="dataAbbinamento" value="2012-06-01"/>
			</td>
		</tr>
		<tr>
			<td>Data disabbinamento:</td>
			<td>
				<input name="dataDisabbinamento" value="2012-06-11"/>
			</td>
		</tr>
		<tr>
			<td>Tipo testo:</td>
			<td>
				<input name="tipoTesto" value="referenza"/>
			</td>
		</tr>
		<tr>
			<td>Passaggio:</td>
			<td>
				<input name="passaggio" />
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Post abbinamento Atto"></td>
		</tr>
		
	</table>
	</form>
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>