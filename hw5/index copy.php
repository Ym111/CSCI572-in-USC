<?php


// make sure browsers see this page as utf-8 encoded HTML
header('Content-Type: text/html; charset=utf-8');
ini_set('memory_limit',-1);
ini_set('max_execution_time', 300);

$limit = 10;
$query = isset($_REQUEST['q']) ? $_REQUEST['q'] : false;
$system = isset($_REQUEST['system']) ? $_REQUEST['system'] : "Lucene";
$results = false;
$rows =  array_map('str_getcsv', file('./URLtoHTML_latimes_news.csv'));
$isCorrect = true;
if ($query)
{
	require_once('Apache/Solr/Service.php');
	$solr = new Apache_Solr_Service('localhost', 8983, '/solr/myexample/');
	if (get_magic_quotes_gpc() == 1)
	{
		$query = stripslashes($query);
	}
	try
	{
		// handle query
		if (isset($system) && $system=="Lucene"){
			$results = $solr->search($query, 0, $limit);
		}else{
			$additionalParameters = array(
				'sort' => 'pageRankFile desc'
			);
			$results = $solr->search($query, 0, $limit, $additionalParameters);
		}

		// handle spelling correction
		$words = explode(" ",$query);
		$correctQuery = "";
		include 'SpellCorrector.php';

		for( $i =0;$i<sizeof($words);$i++){
			$word = $words[$i];
			// check the correct in spellcorrector 
			$correct = SpellCorrector::correct($word);
			$correctQuery .= $correct;
			$correctQuery .= " ";
		}
		$correctQuery = rtrim($correctQuery);
		// echo("<script>console.log('".$correctQuery."');</script>");
		// echo("<script>console.log('".$query."');</script>");
		if(strcmp($correctQuery ,$query)==0 ){
			$isCorrect = true;
		}else{
			$isCorrect = false; 
		}

		 
	}
	catch (Exception $e)
	{
		die("<html><head><title>SEARCH EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
	}
}
?>
<html>
<head>
<title>PHP Solr Client Example</title>
<style>
.searchpart{
	text-align: center
}
.card{
	width:90%;
	margin: auto;
	margin-bottom :10px
}
.radio-bt{
	display:flexbox;
}
</style>
</head>
<body >
<div class= "searchpart">
<h1>CSCI 572</h1>
<form accept-charset="utf-8" method="get">
<label for="q">Search:</label>
<input id="q" name="q" type="text" value="<?php echo htmlspecialchars($query, ENT_QUOTES, 'utf-8'); ?>"/>

<div class= "radio-bt">
	<?php if($system == "Lucene"){
		?>
<input type="radio" id="Lucene" name="system" value="Lucene" checked = "true">
  <label for="Lucene">Lucene</label>
<input type="radio" id="Pagerank" name="system" value="Pagerank">
  <label for="Pagerank">PageRank</label>
		<?php
	}else{
		?>
		<input type="radio" id="Lucene" name="system" value="Lucene" >
  <label for="Lucene">Lucene</label>
<input type="radio" id="Pagerank" name="system" value="Pagerank" checked = "true">
  <label for="Pagerank">PageRank</label>
		<?php
	}
	 ?>

</div>
<input type="submit"/>
</form>
<?php 
	if($isCorrect){
	}else{
		?>
			<div>Do you meaning: <a href="?q=<?php echo $correctQuery ?>&system=<?php echo $system ?>"> <?php echo $correctQuery ?> </a> </div>
		<?php
	}
?>
</div>
<?php
// display results
if ($results)
{
	$total = (int) $results->response->numFound;
	$start = min(1, $total);
	$end = min($limit, $total);
	?>
	<div>
		<div style = "margin-left:70px"><?php echo $system ?> Results:</div>
		<?php
		// iterate result documents
		foreach ($results->response->docs as $doc)
		{  
			$id = $doc->id;
			$title = $doc->title;
			$description = $doc->description;
			$path_id = str_replace("/Users/Bruce/Desktop/20spring/572/CSCI572-in-USC/hw4/solr-7.7.2/crawl_data/latimes/","",$id);//
			?>
			<div class="card">
			<?php
			foreach($rows as $row)
			  if($path_id==$row[0])
			  {
				$map_url = $row[1];
				break;
			  }
			unset($row);
			echo "
			  <div style='border-style: solid; margin-top:10px; padding-left:15px; padding-right:15px;'>
				<p><b>Title : $title</b> </br>
				URL : <a href='$map_url'>$map_url</a> </br>
				ID :$path_id </br>
				Description : $description</br>
				</p>
			  </div>";
			  ?>
			  </div>
				  <?php
		}
		  
			
	
}
?>
</div>
</body>
</html>
