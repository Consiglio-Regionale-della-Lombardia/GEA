<html>
<head>
<script type="text/javascript">
	function eliminaFirmatario(firmatario) {
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
		xmlhttp.open("DELETE", "http://localhost:8080/alfresco/service/crl/atto/firmatario?id="+firmatario+"&alf_method=delete&alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(firmatario);
	}
	
	function eliminaFirmatarioCustom(){
		var currentForm = document.forms[0];
		var firmatario = currentForm.firmatario.options[currentForm.firmatario.selectedIndex].value;
		var firmatarioCustom = firmatario;
		eliminaFirmatario(firmatarioCustom);

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test eliminazione firmatario</h1>
	</div>
	
	<form action="javascript:eliminaFirmatarioCustom()">
	<table>
		<tr>
			<td>Firmatario:</td>
			<td>
				<select name="firmatario">
				<#list firmatari as firmatario>
					<option value="${firmatario.nodeRef}">${firmatario.name} - ${firmatario.parent.parent.name}</option>
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

