/** Data definition */
var chart = null; var model = null;
    		
$(document).ready(function() {
	var defaultDataSample = [[1, 3], [1.5, 3.2], [1.3, 2.8], [3, 1]];
	graph = plotGraph(createDefaultSeries(defaultDataSample), false);
	
	$('#runFCM').click(function(){
		graph.destroy();
		var dataSample = parseJSON($('#matrix').val());
		graph = plotGraph(createDefaultSeries(dataSample), false);
		sendRequest(prepareData(model, false));
	});
	
	$('#finish').click(function(){
		if(!model.finish){
			graph.destroy();
			var dataSample = parseJSON($('#matrix').val());
			graph = plotGraph(createDefaultSeries(dataSample), false);
			sendRequest(prepareData(model, true));
		}else{
			/* Atualizando o label do panel de controle */
			$('#labelControl').text('Controls - [Iteration: ' + model.actualIteration + ', Error: ' + model.error + ', Finish: ' + model.finish + ']');
			redraw(model);
		}
	});
});

/** Prepare data to send */
function prepareDataToSend(runAllIterations){
	var json = {
		dataSample: parseJSON($('#matrix').val()),
		fuzziness: parseJSON($('#fuzziness').val()),
		tolerance: parseJSON($('#tolerance').val()),
		nbClusters: parseJSON($('#nbClusters').val()),
		actualIteration: parseJSON($('#actualIteration').val()),
		matrix: null,
		clusters: null,
		finish: false,
		error: Number.MAX_VALUE
	}
	json = JSON.stringify(json);
	return {json: json, runAll: runAllIterations};
}

/** Prepare data to send */
function prepareData(model, runAllIterations){
	if(model != null){
		var json = {
			dataSample: model.dataSample,
			fuzziness: model.fuzziness,
			tolerance: model.tolerance,
			nbClusters: model.nbClusters,
			actualIteration: model.actualIteration,
			matrix: model.matrix,
			clusters: model.clusters,
			finish: model.finish,
			error: model.error
		}
		json = JSON.stringify(json);
		return {json: json, runAll: runAllIterations};
	}else{
		return prepareDataToSend(runAllIterations);
	}
}

function sendRequest(dataToSend){
	$.ajax({
		url: '/executeFCM',
		type: 'POST',
		data: dataToSend,
		beforeSend: function(){
			/* Active the loader */
		}, success: function(data, textStatus, jqXHR){
			/* Parse result to JSON */
			var result = parseJSON(data);
			model = result;
			console.log(result);
			
			if(result != 'finish' && !result.finish){
				/* Atualizando botão 'Run Fuzzy C-Means' to 'Next Iteration' */
				$('#runFCM').html('Next Iteration <span class="glyphicon glyphicon-step-forward"></span>');
				$('#finish').removeAttr('disabled');
			}else{
				/* Atualizando botão 'Next Iteration' to 'Run Fuzzy C-Means' */
				$('#runFCM').html('Run Fuzzy C-Means <span class="glyphicon glyphicon-play"></span>');
				/* Desabilitando o botão para execução do algoritmo */
				$('#runFCM').attr('disabled', 'disabled');
				/* Atualizando botão 'Run Fuzzy C-Means' to 'Result' */
				$('#finish').html('Result <span class="glyphicon glyphicon-ok"></span>');
			}
			/* Atualizando o label do panel de controle */
			$('#labelControl').text('Controls - [Iteration: ' + model.actualIteration + ', Error: ' + model.error + ']');
			redraw(model);
		}, error: function(jqXHR, textStatus, errorThrown){
			console.log(textStatus);
		}, complete: function(jqXHR, textStatus){
			/* Disable the loader*/
		}
	});
}

/** Print the result of FCM */
function redraw(model){
	var data = [{name: 'Cluster Centroids', color: 'rgba(223, 83, 83, .5)', data: model.clusters},
		{name: 'Data samples', data: model.dataSample}];
	graph = plotGraph(data, true);
}

/* Create default series */
function createDefaultSeries(dataSamples){
	return [{name: 'Data samples', data: dataSamples}];
}

/** Plot graph function */
function plotGraph(listOfPoints, removeClickEvents){
	var evts = {};
	if(!removeClickEvents){
		evts = {
            click: function(e) {
                /* find the clicked values and the series */
                var x = e.xAxis[0].value, y = e.yAxis[0].value, series = this.series[0];
                /* Add it */
                series.addPoint([x, y]);
            }
        }
	}
	
	return new Highcharts.Chart({
        chart: {
        	renderTo: 'container',
            type: 'scatter',
            zoomType: 'xy',
            margin: [70, 50, 60, 80],
            events: evts
        },
        title: {
            text: ''
        },
        subtitle: {
            text: 'Click the plot area to add a point. Click a point to remove it.'
        },
        xAxis: {
        	title: {
                text: 'Value (x)'
            },
            gridLineWidth: 1,
            minPadding: 0.2,
            maxPadding: 0.2,
            minRange: 1
        },
        yAxis: {
            title: {
                text: 'Value (y)'
            },
            minPadding: 0.2,
            maxPadding: 0.2,
            minRange: 1
        },
        legend: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        plotOptions: {
        	scatter:{
        		marker: {
        			radius: 5,
                    states: {
                        hover: {
                            enabled: true,
                            lineColor: 'rgb(100,100,100)'
                        }
                    }
        		},
        		tooltip: {
                    headerFormat: '<b>{series.name}</b><br>',
                    pointFormat: '[x: {point.x}, y: {point.y}]'
                }
        	},
            series: {
                point: {
                    events: {
                        'click': function() {
                            if (this.series.data.length > 1) this.remove();
                        }
                    }
                }
            }
        },
        series: listOfPoints
    });
}

/** Parse data to JSON */
function parseJSON(data) {
    return window.JSON && window.JSON.parse ? window.JSON.parse(data) : (new Function("return " + data))(); 
}