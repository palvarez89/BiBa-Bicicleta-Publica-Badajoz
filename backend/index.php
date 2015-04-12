<?php
 error_reporting (5); 


$homepage = file_get_contents('http://www.bicibadajoz.es/estado/EstadoactualBis.asp');

	$json = array();

   $DOM = new DOMDocument;
   $DOM->loadHTML($homepage);
   // echo $DOM->saveHTML();
   //get all H1
   $items = $DOM->getElementsByTagName('tr');
   $i = 4;
   
   $lat =  array(38.8787,38.8769,38.8753,38.8760,38.8730,38.8706,38.8707,38.8662,38.8669,38.8737,38.8792,38.8788,38.8951,38.8902,38.8856,38.8851,38.8855,38.8866,38.8829,38.8830,38.8855,38.8437,38.8621);
	$long = array(-6.9698,-6.9712,-6.9740,-6.9769,-6.9743,-6.9822,-6.9888,-6.9845,-6.9741,-6.9647,-6.9611,-6.9556,-6.9695,-6.9822,-6.9799,-6.9919,-6.9942,-6.9994,-7.0004,-7.0052,-7.0110,-6.9684,-7.0039);
 
      $json = array_merge($json,array(array('num' => '0', 'nombre' => 'ACTUALIZAR APLICACIÓN','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
    $json = array_merge($json,array(array('num' => '0', 'nombre' => 'ENTRE EN GOOGLE PLAY','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	 $json = array_merge($json,array(array('num' => '0', 'nombre' => 'PARA PODER REALIZAR','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	  $json = array_merge($json,array(array('num' => '0', 'nombre' => 'LA ACTUALIZACIÓN','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	  
	    $json = array_merge($json,array(array('num' => '0', 'nombre' => 'ACTUALIZAR APLICACIÓN','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
    $json = array_merge($json,array(array('num' => '0', 'nombre' => 'ENTRE EN GOOGLE PLAY','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	 $json = array_merge($json,array(array('num' => '0', 'nombre' => 'PARA PODER REALIZAR','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	  $json = array_merge($json,array(array('num' => '0', 'nombre' => 'LA ACTUALIZACIÓN','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	  
	    $json = array_merge($json,array(array('num' => '0', 'nombre' => 'ACTUALIZAR APLICACIÓN','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
    $json = array_merge($json,array(array('num' => '0', 'nombre' => 'ENTRE EN GOOGLE PLAY','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	 $json = array_merge($json,array(array('num' => '0', 'nombre' => 'PARA PODER REALIZAR','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	  $json = array_merge($json,array(array('num' => '0', 'nombre' => 'LA ACTUALIZACIÓN','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	  
	    $json = array_merge($json,array(array('num' => '0', 'nombre' => 'ACTUALIZAR APLICACIÓN','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
    $json = array_merge($json,array(array('num' => '0', 'nombre' => 'ENTRE EN GOOGLE PLAY','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	 $json = array_merge($json,array(array('num' => '0', 'nombre' => 'PARA PODER REALIZAR','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	  $json = array_merge($json,array(array('num' => '0', 'nombre' => 'LA ACTUALIZACIÓN','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	  
	    $json = array_merge($json,array(array('num' => '0', 'nombre' => 'ACTUALIZAR APLICACIÓN','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
    $json = array_merge($json,array(array('num' => '0', 'nombre' => 'ENTRE EN GOOGLE PLAY','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	 $json = array_merge($json,array(array('num' => '0', 'nombre' => 'PARA PODER REALIZAR','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	  $json = array_merge($json,array(array('num' => '0', 'nombre' => 'LA ACTUALIZACIÓN','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	  
	    $json = array_merge($json,array(array('num' => '0', 'nombre' => 'ACTUALIZAR APLICACIÓN','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
    $json = array_merge($json,array(array('num' => '0', 'nombre' => 'ENTRE EN GOOGLE PLAY','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));
	 $json = array_merge($json,array(array('num' => '0', 'nombre' => 'PARA PODER REALIZAR','lat'=> $lat[0],'long'=> $long[0], 'estado' => "FUERA DE LINEA", 'disp' => '0', 'cap' => '0' )));

	  
	  
	echo json_encode($json);
?>
