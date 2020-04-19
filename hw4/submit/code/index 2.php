<?php
// make sure browsers see this page as utf-8 encoded HTML
header('Content-Type: text/html; charset=utf-8');
$limit = 10;
$query = isset($_REQUEST['q']) ? $_REQUEST['q'] : false;
$results = false;
if ($query)
{
	// The Apache Solr Client library should be on the include path
	// which is usually most easily accomplished by placing in the
	// same directory as this script ( . or current directory is a default
	// php include path entry in the php.ini)
	require_once('Apache/Solr/Service.php');
	// create a new solr service instance - host, port, and corename
	// path (all defaults in this example)
	$solr = new Apache_Solr_Service('localhost', 8983, '/solr/myexample/');
	// if magic quotes is enabled then stripslashes will be needed
	if (get_magic_quotes_gpc() == 1)
	{
		$query = stripslashes($query);
	}
	// in production code you'll always want to use a try /catch for any
	// possible exceptions emitted by searching (i.e. connection
	// problems or a query parsing error)
	try
	{
		$additionalParameters = array(
			'sort' => 'pageRankFile desc'
		);
		$results2 = $solr->search($query, 0, $limit, $additionalParameters);

		$results = $solr->search($query, 0, $limit);
		//  $additionalParameters = array(
		//  	'fq' => 'a filtering query',
		// 	'facet' => 'true',
		// 	// notice I use an array for a multi-valued parameter
		// 	'facet.field' => array(
		// 	'id',
		// 	'field_2'
		// 	)
		//    );
		//    $results = $solr->search($query, 0, $limit, $additionalParameters);
	}
	catch (Exception $e)
	{
		// in production you'd probably log or email this error to an admin
		// and then show a special message to the user but for this example
		// we're going to show the full exception
		die("<html><head><title>SEARCH EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
	}
}
?>
<html>
<head>
<title>PHP Solr Client Example</title>
</head>
<body>
<form accept-charset="utf-8" method="get">
<label for="q">Search:</label>
<input id="q" name="q" type="text" value="<?php echo htmlspecialchars($query, ENT_QUOTES, 'utf-8'); ?>"/>
<input type="submit"/>
</form>
<?php
// display results
$cnt =0;
$r1 = [];
// $csvdoc = fopen("/Users/Bruce/Desktop/20spring/572/CSCI572-in-USC/hw4/solr-7.7.2/crawl_data/URLtoHTML_latimes_news.csv", 'r');
// $csvar = [];

if ($results)
{
	$total = (int) $results->response->numFound;
	$start = min(1, $total);
	$end = min($limit, $total);
	?>
		<div>Results <?php echo $start; ?> - <?php echo $end;?> of <?php echo $total; ?>:</div>
		<ol>
		<?php
		// iterate result documents
		foreach ($results->response->docs as $doc)
		{
			?>
				<li>
				
				<?php
				// iterate document fields / values
				foreach ($doc as $field => $value)
				{
					if($field =="og_url"){
						?>
						
						<a href=" <?php echo htmlspecialchars($value, ENT_NOQUOTES, 'utf-8'); ?> "><?php echo htmlspecialchars($value, ENT_NOQUOTES, 'utf-8'); ?></a> 
						
						<?php
						array_push($r1,$value);
					}
					
				}
			?>
				
				</li>
				<?php
		}
	?>
		</ol>
		<?php
}

if ($results2)
{
	$total = (int) $results2->response->numFound;
	$start = min(1, $total);
	$end = min($limit, $total);
	?>
		<div>Results for PageRank<?php echo $start; ?> - <?php echo $end;?> of <?php echo $total; ?>:</div>
		<ol>
		<?php
		// iterate result documents
		foreach ($results2->response->docs as $doc)
		{
			?>
				<li>
				
				<?php
				// iterate document fields / values
				$fg = 0;
				foreach ($doc as $field => $value)
				{
					
					if($field =="og_url"){
						if($value ==null || $value == ""){
						$fg=1;
						}else{
							$fg=0;
						}
						
						?>
						
						
						<a href=" <?php echo htmlspecialchars($value, ENT_NOQUOTES, 'utf-8'); ?> "><?php echo htmlspecialchars($value, ENT_NOQUOTES, 'utf-8'); ?></a> 
						
						<?php
						
						if (in_array($value,$r1)){
							$cnt++;
						}
					}
					
				}
			?>
				
				</li>
				<?php
		}
	?>
		</ol>
		<?php
}
?>
<p>The Amount of Overlap: <?php 
 echo $cnt
?></p>

</body>
</html>
