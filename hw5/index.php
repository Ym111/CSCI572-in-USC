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
<!-- <script src="https://cdn.jsdelivr.net/npm/@tarekraafat/autocomplete.js@7.2.0/dist/js/autoComplete.min.js"></script> -->
<link rel="stylesheet"
  href="https://code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

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
#q{
	font-size: 20px;
}
.autocomplete {
  /*when navigating through the items using the arrow keys:*/
  background-color: DodgerBlue !important;
  color: #ffffff;
}
</style>
</head>
<body >
<div class= "searchpart">
<h1>CSCI 572</h1>
<form accept-charset="utf-8" method="get">
<label for="q">Search:</label>
<input list ="qu"id="q" name="q" autocomplete="off" type="text" onkeyup="searchsuggest(this.value,event)" value="<?php echo htmlspecialchars($query, ENT_QUOTES, 'utf-8'); ?>"/>
	
<br/>
<br/>
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
			<div>Do you mean: <a href="?q=<?php echo $correctQuery ?>&system=<?php echo $system ?>"> <?php echo $correctQuery ?> </a> </div>
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

<script>

	function searchsuggest(str,e){
		//console.log(str);
		if(str.length==0 || str==""){
			return;
		}
		// select input 
		if(e.keyCode == 37 || e.keyCode == 38||e.keyCode == 39||e.keyCode == 40){ // 
			return;
		}
		term = str.split(" ")[str.split(" ").length-1];
		// search it 
		preterm = str.replace(term,"");
		console.log(term);
		var xmlhttp = new XMLHttpRequest();
		xmlhttp.onreadystatechange = function() {
		//console.log(this.readyState);
        if (this.readyState == 4 && this.status == 200) {
            //query is changed
            if(document.getElementById("q").value != str){
              return;
			}
			//parse responde
			//console.log(this.responseText);
			var response = JSON.parse(this.responseText);
			var suggestions = response["suggest"]["suggest"][term]["suggestions"];
			
            var suggestion_list = [];
            for(var i = 0;i<suggestions.length;++i){
              suggestion_list.push(preterm + suggestions[i]["term"]);
            }
			console.log(suggestion_list);
            $("#q").autocomplete({
              source: function( request, response ) {
                      response(suggestion_list);
                  }
            });
          }
	  };
		xmlhttp.open("GET", "searchsuggest.php?q=" + term, true);
      	xmlhttp.send();
		
	  //console.log("localhost:8983/solr/myexample/suggest?q=" + str);


	}

</script>
</body>
</html>
