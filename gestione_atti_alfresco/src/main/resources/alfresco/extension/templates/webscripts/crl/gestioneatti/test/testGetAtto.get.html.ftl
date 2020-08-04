<html>
<head>
<script type="text/javascript">
	function getAtto(atto) {
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
		xmlhttp.open("GET", "http://localhost:8080/alfresco/service/crl/atto?id="+atto+"&alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function getAttoCustom(){
		var currentForm = document.forms[0];
		var atto = currentForm.atto.options[currentForm.atto.selectedIndex].value;
		getAtto(atto);

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test lettura dettagli atto</h1>
	</div>
	
	<form action="javascript:getAttoCustom()">
	<table>
		<tr>
			<td>Atto:</td>
			<td>
				<select name="atto">
				<#list atti as atto>
					<option value="${atto.nodeRef}">${atto.name}</option>
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

