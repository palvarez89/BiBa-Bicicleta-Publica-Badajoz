<?php
 error_reporting (5); 


$homepage = file_get_contents('http://www.bicibadajoz.es/estado/EstadoactualBis.asp');

	$json = array();
	$lat =  array(38.8787,38.8769,38.8753,38.8760,38.8730,38.8706,38.8707,38.8662,38.8669,38.8737,38.8792,38.8788,38.8951,38.8902,38.8856,38.8851,38.8855,38.8866,38.8829,38.8830,38.8855,38.8437,38.8621,38.8836);
	$long = array(-6.9698,-6.9712,-6.9740,-6.9769,-6.9743,-6.9822,-6.9888,-6.9845,-6.9741,-6.9647,-6.9611,-6.9556,-6.9695,-6.9822,-6.9799,-6.9919,-6.9942,-6.9994,-7.0004,-7.0052,-7.0110,-6.9684,-7.0039,-7.0227);
   $DOM = new DOMDocument;
   $DOM->loadHTML($homepage);
   // echo $DOM->saveHTML();
   //get all H1
   $items = $DOM->getElementsByTagName('tr');
   $i = 4;
   while ($i < $items->length) {
   
		//Estación
		$obj = $items->item($i);
		$xml = utf8_decode($obj->ownerDocument->saveXML($obj));
		
		$dom2 = new DOMDocument;
		$dom2->loadHTML($xml);
		$cols = $dom2->getElementsByTagName('td');

		
		list ($numero, $nombre, $estado) = explode(" - ", $cols->item(1)->nodeValue);
		$i++;
		
		
		
		
		//Estado
		
		$obj = $items->item($i);
		$xml = $obj->ownerDocument->saveXML($obj);
        $cadena = $items->item($i)->nodeValue;
		
		list ($basura, $cantidad) = explode(" - ", utf8_decode( $cadena ));
		list ($disp, $capac) = explode("/", $cantidad);
		
		$i = $i+2;
		
		
		$json = array_merge($json,array(array('num' => $numero, 'nombre' => $nombre,'lat'=> $lat[$numero-1],'long'=> $long[$numero-1], 'estado' => $estado, 'disp' => filter_var($disp, FILTER_SANITIZE_NUMBER_INT), 'cap' => filter_var($capac, FILTER_SANITIZE_NUMBER_INT))));

		
		
	}
	echo json_encode($json);
?>
