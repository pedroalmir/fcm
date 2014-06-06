<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
    	
    	<meta name="author" content="Pedro Almir">
    	<meta name="description" content="Fuzzy C-Means Algorithm">
    	<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico">
    	
		<!-- Bootstrap core CSS -->
	    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
	    <!-- Custom styles for this template -->
	    <link href="${pageContext.request.contextPath}/css/sticky-footer-navbar.css" rel="stylesheet">
		<link href="${pageContext.request.contextPath}/css/vis.css" rel="stylesheet" type="text/css" />
	
	    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	    <!--[if lt IE 9]>
	      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
	    <![endif]-->
	    
	    <!-- ========================================================== -->
		<!-- Placed at the end of the document so the pages load faster -->
	    <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	    <script src="${pageContext.request.contextPath}/js/docs.min.js"></script>
	    
	    <script src="${pageContext.request.contextPath}/js/main.js"></script>
	    <script src="${pageContext.request.contextPath}/js/highcharts.js"></script>
	    <script src="${pageContext.request.contextPath}/js/exporting.js"></script>
	</head>
	<body>
		<!-- Fixed navbar -->
    	<div class="navbar navbar-default navbar-fixed-top">
      		<div class="container">
        		<div class="navbar-header">
          			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
	           			<span class="sr-only">Toggle navigation</span>
	            		<span class="icon-bar"></span>
	            		<span class="icon-bar"></span>
	            		<span class="icon-bar"></span>
          			</button>
          			<a class="navbar-brand" href="${pageContext.request.contextPath}/">Fuzzy C-Means Algorithm</a>
        		</div>
        		<div class="collapse navbar-collapse">
          			<ul class="nav navbar-nav">
            			<li class="active"><a href="${pageContext.request.contextPath}/">Home</a></li>
            			<li><a href="http://github.com/pedroalmir/fcm">Git Repository</a></li>
            			<li><a href="mailto:petrus.cc@gmail.com">Contact</a></li>
          			</ul>
        		</div><!--/.nav-collapse -->
      		</div>
    	</div>
    	<!-- Begin page content -->
    	<div class="container">
      		<div class="page-header">
        		<h2>Fuzzy C-Means Algorithm</h2>
      		</div>
      		<p class="lead">Fuzzy clustering is a class of algorithms for cluster analysis in which the allocation of data points to clusters is not <code>hard</code> (all-or-nothing) but <code>fuzzy</code> in the same sense as fuzzy logic.</p>
      		<div class="row">
      			<div class="col-md-5">
      				<div class="panel panel-default">
						<div class="panel-heading">
					    	<h3 class="panel-title">Clustering algorithm</h3>
					  	</div>
					  	<div class="panel-body">
					  		<form action="">
					  			<div class="col-sm-12">
						  			<div class="form-group">
								    	<label for="matrix">Data samples definition (Use JSON pattern)</label>
							    		<textarea class="form-control" rows="5" id="matrix" placeholder="Define here the matrix of problem">[[1, 3], [1.5, 3.2], [1.3, 2.8], [3, 1]]
							    		</textarea>
								  	</div>
							  	</div>
							  	<div class="col-sm-6">
							  		<div class="form-group">
								    	<label for="fuzziness">Fuzziness value (m)</label>
								    	<input type="text" class="form-control" id="fuzziness" placeholder="Fuzziness value" value="2.0">
							    	</div>
							    </div>
							    <div class="col-sm-6">
							    	<div class="form-group">
								    	<label for="tolerance">Tolerance level (e)</label>
								    	<input type="text" class="form-control" id="tolerance" placeholder="Tolerance level" value="0.0">
							    	</div>
							  	</div>
							  	<div class="col-sm-12">
							    	<div class="form-group">
								    	<label for="nbClusters">Number of clusters</label>
								    	<input type="text" class="form-control" id="nbClusters" placeholder="Number of clusters" value="2">
							    	</div>
							  	</div>
					  		</form>
					  	</div>
					</div>
					<div class="panel panel-default">
						<div id="labelControl" class="panel-heading">
					    	<h3 id="labelControl" class="panel-title">Controls</h3>
					  	</div>
					  	<div class="panel-body">
					  		<input id="actualIteration" type="hidden" value="0">
							<div class="col-sm-12">
							    <div class="form-group">
									<button id="runFCM" type="button" class="btn btn-primary btn-lg" style="width: 100%;">
										Run Fuzzy C-Means <span class="glyphicon glyphicon-play"></span>
									</button>
								</div>
							</div>
							
							<div class="col-sm-12">
							    <div class="form-group">
									<button id="finish" type="button" class="btn btn-success btn-lg" disabled="disabled" style="width: 100%;">
										Finish <span class="glyphicon glyphicon-fast-forward"></span>
									</button>
								</div>
							</div>
					  	</div>
					</div>
      			</div>
      			<div class="col-md-7">
      				<div class="panel panel-primary" style="height: 580px;">
						<div class="panel-heading">
							<h3 id="visualizationPanel" class="panel-title">Visualization</h3>
					  	</div>
					  	<div class="panel-body text-center">
					  		<div id="container" style="min-width: 310px; height: 515px; max-width: 700px; margin: 0 auto"></div>
					  	</div>
					</div>
      			</div>
      		</div>
    	</div>
    	<div id="footer">
      		<div class="container">
        		<p class="text-muted">Developed by <a href="http://pedroalmir.com">Pedro Almir</a> © 2014</p>
      		</div>
	    </div>
	</body>
</html>