<html>
<head>
<script type="text/javascript">
	function creaEmendamentiClausole(atto) {
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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/esamecommissioni/emendamenticlausole?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	


	function creaEmendamentiClausoleCommissione(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
		var passaggio = currentForm.passaggio.value;
		var commissione = currentForm.commissione.value;
		
		
		var numEmendPresentatiMaggiorEsameCommissioni = currentForm.numEmendPresentatiMaggiorEsameCommissioni.value;
		var numEmendPresentatiMinorEsameCommissioni = currentForm.numEmendPresentatiMinorEsameCommissioni.value;
		var numEmendPresentatiGiuntaEsameCommissioni = currentForm.numEmendPresentatiGiuntaEsameCommissioni.value;
		var numEmendPresentatiMistoEsameCommissioni = currentForm.numEmendPresentatiMistoEsameCommissioni.value;

		var numEmendApprovatiMaggiorEsameCommissioni = currentForm.numEmendApprovatiMaggiorEsameCommissioni.value;
		var numEmendApprovatiMinorEsameCommissioni = currentForm.numEmendApprovatiMinorEsameCommissioni.value;
		var numEmendApprovatiGiuntaEsameCommissioni = currentForm.numEmendApprovatiGiuntaEsameCommissioni.value;
		var numEmendApprovatiMistoEsameCommissioni = currentForm.numEmendApprovatiMistoEsameCommissioni.value;

		var nonAmmissibiliEsameCommissioni = currentForm.nonAmmissibiliEsameCommissioni.value;
		var decadutiEsameCommissioni = currentForm.decadutiEsameCommissioni.value;
		var ritiratiEsameCommissioni = currentForm.ritiratiEsameCommissioni.value;
		var respintiEsameCommissioni = currentForm.respintiEsameCommissioni.value;
		var noteEmendamentiEsameCommissioni = currentForm.noteEmendamentiEsameCommissioni.value;
		
		var dataPresaInCaricoProposta = currentForm.dataPresaInCaricoProposta.value;
		var dataIntesa = currentForm.dataIntesa.value;
		var esitoVotazioneIntesa = currentForm.esitoVotazioneIntesa.value;
		var noteClausolaValutativa = currentForm.noteClausolaValutativa.value;
		
		var attoCustom = {	
			"target":{
				"commissione": ""+commissione+"",
				"passaggio": ""+passaggio+""
			},
			"atto": {
				"id": ""+idAtto+"",
				"stato": "emendamenti e clausole",
				"passaggi": [
					{
						
							"nome" : "Passaggio1",
							"commissioni": [
								{
							 			"descrizione": ""+commissione+"",
										"stato": "fine lavori comitato",
										"ruolo": "Referente",
										"numEmendPresentatiMaggiorEsameCommissioni": ""+numEmendPresentatiMaggiorEsameCommissioni+"",
										"numEmendPresentatiMinorEsameCommissioni": ""+numEmendPresentatiMinorEsameCommissioni+"",
										"numEmendPresentatiGiuntaEsameCommissioni": ""+numEmendPresentatiGiuntaEsameCommissioni+"",
										"numEmendPresentatiMistoEsameCommissioni": ""+numEmendPresentatiMistoEsameCommissioni+"",
										
										"numEmendApprovatiMaggiorEsameCommissioni": ""+numEmendApprovatiMaggiorEsameCommissioni+"",
										"numEmendApprovatiMinorEsameCommissioni": ""+numEmendApprovatiMinorEsameCommissioni+"",
										"numEmendApprovatiGiuntaEsameCommissioni": ""+numEmendApprovatiGiuntaEsameCommissioni+"",
										"numEmendApprovatiMistoEsameCommissioni": ""+numEmendApprovatiMistoEsameCommissioni+"",
										
										"nonAmmissibiliEsameCommissioni": ""+nonAmmissibiliEsameCommissioni+"",
										"decadutiEsameCommissioni": ""+decadutiEsameCommissioni+"",
										"ritiratiEsameCommissioni": ""+ritiratiEsameCommissioni+"",
										"respintiEsameCommissioni": ""+respintiEsameCommissioni+"",
										"noteEmendamentiEsameCommissioni": ""+noteEmendamentiEsameCommissioni+"",
										
										"dataPresaInCaricoProposta": ""+dataPresaInCaricoProposta+"",
										"dataIntesa": ""+dataIntesa+"",
										"esitoVotazioneIntesa": ""+esitoVotazioneIntesa+"",
										"noteClausolaValutativa": ""+noteClausolaValutativa+""
									
								},
								{
							 			"descrizione": "commissioneStaticaTest"
										
									
								}
							]
							
						
					}
				]
		
			}
		};
		
	
		
		creaEmendamentiClausole(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test emendamenti clausole atto</h1>
	</div>
	
	<form action="javascript:creaEmendamentiClausoleCommissione()">
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
			<td>Commissione</td>
			<td>
				<input type="text" name="commissione"/>
			</td>
		</tr>
		<tr>
			<td>Passaggio</td>
			<td>
				<input type="text" name="passaggio"/>
			</td>
		</tr>
			
		<tr>
			<td>Num Emend Presentati Maggior</td>
			<td>
				<input type="text" name="numEmendPresentatiMaggiorEsameCommissioni"/>
			</td>
		</tr>
		<tr>
			<td>Num Emend Presentati Minor</td>
			<td>
				<input type="text" name="numEmendPresentatiMinorEsameCommissioni"/>
			</td>
		</tr>
		<tr>
			<td>Num Emend Presentati Giunta</td>
			<td>
				<input type="text" name="numEmendPresentatiGiuntaEsameCommissioni"/>
			</td>
		</tr>
		<tr>
			<td>Num Emend Presentati Misto</td>
			<td>
				<input type="text" name="numEmendPresentatiMistoEsameCommissioni"/>
			</td>
		</tr>
		<tr>
			<td>Num Emend Approvati Maggior</td>
			<td>
				<input type="text" name="numEmendApprovatiMaggiorEsameCommissioni"/>
			</td>
		</tr>
		<tr>
			<td>Num Emend Approvati Minor</td>
			<td>
				<input type="text" name="numEmendApprovatiMinorEsameCommissioni"/>
			</td>
		</tr>
		<tr>
			<td>Num Emend Approvati Giunta</td>
			<td>
				<input type="text" name="numEmendApprovatiGiuntaEsameCommissioni"/>
			</td>
		</tr>
		<tr>
			<td>Num Emend Approvati Misto</td>
			<td>
				<input type="text" name="numEmendApprovatiMistoEsameCommissioni"/>
			</td>
		</tr>
		<tr>
			<td>Non ammissibili</td>
			<td>
				<input type="text" name="nonAmmissibiliEsameCommissioni"/>
			</td>
		</tr>
		<tr>
			<td>decaduti</td>
			<td>
				<input type="text" name="decadutiEsameCommissioni"/>
			</td>
		</tr>
		<tr>
			<td>ritirati</td>
			<td>
				<input type="text" name="ritiratiEsameCommissioni"/>
			</td>
		</tr>
		<tr>
			<td>respinti</td>
			<td>
				<input type="text" name="respintiEsameCommissioni"/>
			</td>
		</tr>
		<tr>
			<td>note emendamenti</td>
			<td>
				<input type="text" name="noteEmendamentiEsameCommissioni"/>
			</td>
		</tr>
		<tr>
			<td>Data presa in carico</td>
			<td>
				<input type="text" name="dataPresaInCaricoProposta"/>
			</td>
		</tr>
		<tr>
			<td>Data intesa</td>
			<td>
				<input type="text" name="dataIntesa"/>
			</td>
		</tr>
		<tr>
			<td>Esito votazione intesa</td>
			<td>
				<input type="text" name="esitoVotazioneIntesa"/>
			</td>
		</tr>
		<tr>
			<td>Note Clausola Valutativa</td>
			<td>
				<input type="text" name="noteClausolaValutativa"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="emendamenti e clausole"></td>
		</tr>
	</table>
	
	
		
	</form>
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

