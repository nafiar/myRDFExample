<?php

function prefix($word, $prefix){
	if( strlen($word) < strlen($prefix)){
		return 0;
	}
	else{
		$prefixWord = substr($word, 0, strlen($prefix));
		if($prefix == $prefixWord){
			$suffixWord = substr($word, strlen($prefix), strlen($word));
			return $suffixWord;
		}
		else{
			return 0;
		}
	}
}

$filePredicate = "/home/nafiar/NetBeansProjects/myRDFExample/test/listPredicate.txt";
$prefixes = array('0' => ['prefix' => 'rdf',
						  'isi' => 'http://www.w3.org/1999/02/22-rdf-syntax-ns#'],
				  '1' => ['prefix' => 'rdfs',
				  		  'isi' => 'http://www.w3.org/2000/01/rdf-schema#'],
				  '2' => ['prefix' => 'owl',
				  		  'isi' => 'http://www.w3.org/2002/07/owl#'],
				  '3' => ['prefix' => 'dbpedia-owl',
				  		  'isi' => 'http://dbpedia.org/ontology/'],
				  '4' => ['prefix' => 'dcterms',
				  		  'isi' => 'http://purl.org/dc/terms/'],
				  '5' => ['prefix' => 'foaf',
				  		  'isi' => 'http://xmlns.com/foaf/0.1/'],
				  '6' => ['prefix' => 'dbpprop-id',
				  		  'isi' => 'http://id.dbpedia.org/property/'],
				  '7' => ['prefix' => 'ns7',
				  		  'isi' => 'http://www.w3.org/ns/prov#']);
$myFile = fopen($filePredicate, "r");

$predicates[] = array();
$i = 0;
while(!feof($myFile)){
	$predicates[$i] = fgets($myFile);
	$i++;
}

$indexedPredicate[] = array();
foreach($predicates as $key => $predicate){
	if(isset($indexedPredicate[$predicate])){
	}
	else{
		$indexedPredicate[$predicate] = 1;
		foreach ($prefixes as $prefix) {
			$word=prefix($predicate, $prefix['isi']);
			//echo $word."\n";
			if($word){
				//echo $word;
				$string = $prefix['prefix'].":".$word;
				echo $string;
			}
		}
	}
}
fclose($myFile);
?>