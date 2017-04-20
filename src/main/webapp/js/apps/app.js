define(
  [ 'jquery', 
    'tesys',  
    'model', 
    'view', 
    'recomendationview',
    'bar',
    'radar',
    'parser'
  ], 
  function($, 
    tesys, 
    model, 
    view,
    recomendationView,
    bar,
    radar
  ) {
	
	/* Main function */
  var start = function() {
    //TODO arreglar el sig bug. Cuand se elije un nuevo issue para graficar, 
    //desaparece el grafico que esta en el panel inactivo. Esto se debe a 
    //que AmCharts no puede graficar sobre un tab inactivo.
    var metricsToPlot = { array:[] };
    var skillsToPlot = { array:[] };
    
    // Definicion de objetos encargados de graficar sobre la UI (plotters)
    var metricsPlotter = new bar("metricChart");
    var skillPlotter = new radar("skillChart");

    // Definicion de Modelos.
    var developers = new model.DeveloperCollection();
    var issues = new model.RecommendationCollection();
    var metrics = new model.MetricCollection() ;
    var skills = new model.MetricCollection() ;

    /**
    * Modelo de Recomendacion
    *
    **/

    var metricsRecommendation = new model.MetricCollection();

    // Definicion de Vistas.
    var devListView = new view.DeveloperCollectionView(
        { el: $('#developers-issues'),
          collection: developers, 
          plotter: [metricsPlotter, skillPlotter],
          attrToPlot: ['metrics', 'skills']
        }
    );
    
    var recommendation = new view.IssueRecommendationView(
            { collection: issues,
              el: $('#developers-reco')
            }
    );       
   
    var metricsView = new view.MetricCollectionView(
          { collection: metrics, 
            el: $('#metrics'), 
            metricsToPlot: metricsToPlot,
            plotter: metricsPlotter,
            type: 'metrics'
          }
    );

    var mview = new view.MetricSelectView(
          { collection: metrics,
            el: $('#submitMetricSelect') 
          }
    );

    var skillsView = new view.MetricCollectionView(
          { collection: skills, 
            el: $('#skills'), 
            metricsToPlot: skillsToPlot,
            plotter: skillPlotter,
            type: 'skills'
    });

    // array of {metricKey: floatValue}
    var metricsValues = {};

    //Selector de las metricas
    var metricRecomendationView = new view.MetricSelectView(
      { collection: metrics,
        el: $('#recomendationMetricSelect') 
      }
    );

    var estimationSelectedSkills = { array: [] };
    
    // Select de Skill para las estimaciones
    var estimationSkillsSelectorView = new recomendationView.SkillCollectionView(
      {
        collection: skills,
        el: $('#estimationsSkillSelector'),
        selectedSkills: estimationSelectedSkills
      }
    );
    
    // Selects para las recomendaciones
    var recomendationMetricsView = new view.MetricSelectView(
      { collection: metricsRecommendation,
        el: $('#recomendationMetric') 
      });
    
    $('#estimationInsertBtn').click(function(){
      var metricValue = parseFloat($('#estimationMetricInput').val());
      if (!isNaN(metricValue) && isFinite(metricValue)) {
        var metricId = $('#recomendationMetricSelect').val() ;
        //inserto o reemplazo la metrica en map metricsValues
        metricsValues[metricId] = metricValue;
        //inserto o reemplazo la metrica en la vista
        var metricList = $('#metricList') ;
        var metricListElement = $('#'+metricId, metricList) ;
        if (metricListElement) {
          metricListElement.remove();
        }
        var li = $('<li class="list-group-item" id="'+metricId+'">').text(metricId+"= "+metricValue) ;
        metricList.append(li);

        //Event listener para eliminar metrica si le hago doble click
        li.on('dblclick', function(){
          delete metricsValues[this.id] ;
          $(this).remove();
        });
        console.log(metricsValues) ;
      } else {
        alert("Invalid Input");
      }
    });
    
    /* Predictions */
    var metricsPredicted = new model.MetricCollection() ;

    var metricsPredToPlot = { array:[] };

    var predPlotter = new bar("predBar") ;

    var metricsPredView = new recomendationView.MetricPredictionCollectionView(
          { collection: metricsPredicted, 
            el: $('#metricsPred'), 
            metricsToPlot: metricsPredToPlot,
            plotter: predPlotter,
            type: 'metrics'
          }
    );

    var devPred = new model.DeveloperPredictionCollection();
    var devPredListView = new recomendationView.DeveloperPredictionCollectionView(
        { el: $('#developers-predictions'),
          collection: devPred, 
          plotter: [predPlotter, new radar("predRadar")],
          attrToPlot: ['metrics', 'skills']
        }
    );

    //Metricas de recomendacion
    var metricsValuesRecomendation = {};
    
    //Vistas de la recomendacion. Seleccion de las metricas.
    var recomendationsMetricsView = new view.MetricSelectView(
      {
        collection: metricsRecommendation,
        el: $('#recomendationMetricSelect2')
      });
    
    //Skills para la recomendacion
    var recommendationSelectedSkills = { array: [] };
    
    //Recomendacion de los contenedores.
    var recomendationsSkillSelectorView = new recomendationView.SkillCollectionView(
      {
       collection: skills,
       el: $('#recomendationsSkillSelector'), 
       selectedSkills:recommendationSelectedSkills
      });
    
    /**
     * Elimina repetidos en un array
     * 
     * @return {Array} Arreglo sin repetidos
     */
    Array.prototype.unique = function() {
      var a = this.concat();
      for(var i=0; i<a.length; ++i) {
          for(var j=i+1; j<a.length; ++j) {
              if(a[i] === a[j])
                  a.splice(j--, 1);
          }
      }
      return a;
    };

    /**
     * Devuelve las metricas sin repetidos
     *
     * @param  {Array of String} allMetrics Es un json que representa un 
     *   conjunto de issues, ejemplo, predicciones.
     *    
     * @return {Array of string} Arreglo de metricas sin repetidos
     */
    function metricSetFromPredictions( allMetrics ){
      var metricSet = [] ;
      if (allMetrics) {
        for (var i = 0; i < allMetrics.length; i++) {
          // Estoy recorriendo un arreglo de developers
          for (var j = 0; j < allMetrics[i].issues.length; j++) {
            // Ahora recorro un arreglo de issues
            var metricSubset = [] ;
            for (var metric in allMetrics[i].issues[j].metrics) {
              metricSubset.push(metric);
            }  
            metricSet = metricSet.concat(metricSubset).unique();          
          }
        }
      }
      return metricSet ;
    }

    /**
     * Dado un arreglo de metricas (contiene las key de las metricas)
     * y una colecion de metricas (una coleccion de backbone) crea una
     * coleccion con la interseccion entre los dos conjuntos.
     * 
     * @param  {Backbone.Collection of metrics} metriccollection Coleccion de
     *   entrada.
     * 
     * @param  {Array of string} metricArray Metricas que queremos tener.
     * 
     * @return {Backbone.Collection of metrics} Coleccion resultado de la
     *   interseccion .
     */
    function selectMetricsFromModel(metricCollection, metricArray){
      var result = new model.MetricCollection() ; 
      for( var id in metricArray ) {
          result.push(metricCollection.get(metricArray[id])) ;
      }        
      return result;
    }

    var predictions = []; //todos los developer con las predicciones¿
    function addPredictions (data) {
      console.log("estimation data "+data);
      if (predictions === undefined || predictions.length === 0) {
        predictions = data ;
      } else {
        for (i=0; i<data.length; i++) {
          var j = 0;
          //busco si el developer se encontraba en la prediccion
          while (j<predictions.length && data[i].name!=predictions[j].name) {
            j++;
          }
          if(j>=predictions.length) { 
            // Si el developer no se encontro agrego nuevo developer a la predicicon
            predictions.push(data[i]); 
          } else { 
            //En caso de haber repetidos, piso las metricas y desviaciones viejas por las nuevas.
            for (var metric in data[i].issues[0].metrics) {
              predictions[j].issues[0].metrics[metric] = data[i].issues[0].metrics[metric] ;
              predictions[j].issues[0].deviations[metric] = data[i].issues[0].deviations[metric] ;
            }
          }
        }
      }
      console.log("estimacion"+predictions);
      devPred.reset(predictions);
      
      metricsPredicted.reset(
        selectMetricsFromModel(
          metrics, 
          metricSetFromPredictions(predictions)
        ).models
      );
    }
    
    $('#estimationBtn').click(function(){
      // metricsValues
      // estimationSelectedSkills.array 
      predictions = [] ;
      var minCorrelation = parseFloat($('#estimationCorrelation').val()) ;
      if (minCorrelation <= 1.0) { 
        for (var m in metricsValues) {
          tesys.getPredictions(m, metricsValues[m], minCorrelation, estimationSelectedSkills.array, addPredictions); 
        }
      }
    });

    /*** fin recomendaciones */

    /**
    * Pestaña de RECOMENDACION DE DESARROLLADORES
    **/

    // Modelo de las metricas
    var metricsRecommendationPredicted = new model.MetricCollection();
    // Modelo para la recomendacion.
    var devRecom = new model.DeveloperPredictionCollection();
    var devRecomListView = new recomendationView.DeveloperRecommendationCollectionView(
      { 
        el: $('#developers-predictions-recommendation'),
        collection: devRecom
        //plotter: [predPlotter, new radar("predRadar")],
        //attrToPlot: ['metrics', 'skills']
      });

    //Hacer lo mismo que el insert de estimacion pero para la pestaña de recomendacion
    $('#recomendationInsertBtn').click(function(){
      var metricValue = parseFloat($('#recomendationMetricInput').val());
       if (!isNaN(metricValue) && isFinite(metricValue)) {
          var metricId = $('#recomendationMetricSelect2').val();
          //inserto o reemplazo la metrica en map metricsValues
          metricsValuesRecomendation[metricId] = metricValue;
          //inserto o reemplazo la metrica en la vista
          var metricList = $('#metricListRecommendation');
          var metricListElement = $('#'+metricId, metricList) ;
          if (metricListElement) {
            metricListElement.remove();
        }
        var li = $('<li class="list-group-item" id="'+metricId+'">').text(metricId+"= "+metricValue) ;
        metricList.append(li);
        //Event listener para eliminar metrica si le hago doble click
        li.on('dblclick', function(){
          delete metricsValuesRecomendation[this.id] ;
          $(this).remove();
        });
        console.log(metricsValuesRecomendation);
      } else {
        alert("Invalid Input");
      }
    });

    //Funcion para la prediccion de Recomendacion.
    var predictionsRecommendation = []; //Developers a partir de las metricas seleccionadas.

    function addPredictionsRecommendations (data) {
      //console.log("dentro de la funcion, dta "+data);
      if (predictionsRecommendation === undefined || predictionsRecommendation.length === 0) {
        predictionsRecommendation = data ;
      } else {
        for (i=0; i<data.length; i++) {
          var j = 0;
          //busco si el developer se encontraba en la prediccion
          while (j<predictionsRecommendation.length && data[i].name!=predictionsRecommendation[j].name) {
            j++;
          }
          if(j>=predictionsRecommendation.length) { 
            // Si el developer no se encontro agrego nuevo developer a la predicicon
            predictionsRecommendation.push(data[i]); 
          } else { 
            //En caso de haber repetidos, piso las metricas y desviaciones viejas por las nuevas.
            for (var metric in data[i].issues[0].metrics) {
              predictionsRecommendation[j].issues[0].metrics[metric] = data[i].issues[0].metrics[metric] ;
              predictionsRecommendation[j].issues[0].deviations[metric] = data[i].issues[0].deviations[metric] ;
            }
          }
        }
      }
      console.log("Recomendacion "+predictionsRecommendation);
      devRecom.reset(predictionsRecommendation);
    /*  
      metricsRecommendationPredicted.reset(
          selectMetricsFromModel(
          metricsRecommendation, 
          metricSetFromPredictions(predictionsRecommendation)
        ).models
      );*/
    }

      //Boton de recomendar
      $('#RecommendDeveloperbyIssue').click(function(){
      //alert("hice click en recomendar");
      predictionsRecommendation = [] ;
      var minCorrelation = parseFloat($('#recommendationCorrelation').val());
      if (minCorrelation <= 1.0) {  
        alert("antes del for");
       for (var m in metricsValuesRecomendation) {
         // Cambiar el 0.5 , 0.5 por una variable
          alert("metrica "+m);
          alert("valor "+metricsValuesRecomendation[m]);
          tesys.getDevRecommendationbyIssue(0.5, 0.5,m,metricsValuesRecomendation[m],metricsValuesRecomendation, addPredictionsRecommendations);
        }         
       }
      });

      /**
      * Fin de pestaña de RECOMENDACIONES
      **/


    // Extraccion de los datos desde Tesys al modelo de la UI
    tesys.getAnalysis(function(data){
      developers.reset(data);
      //developerRecommendation.reset(data);
    });

    tesys.getMetrics(function(data){
      metrics.reset(data);
      //Metricas de la recomendacion 
      metricsRecommendation.reset(data);
    });


    tesys.getSkills(function(data){

      //adapt skills to metrics format
      var adaptedData = [];
      $.each(data, function(index, el) {
        adaptedData.push({'key': el.skillName, 'nombre': el.skillName});
      });
      skills.reset(adaptedData);
    });

    tesys.getIssues(function(data){
      issues.reset(data);
    })
    
    //$('#myTab a[href="#metricPane"]').on('shown.bs.tab', function (e) {
    //  predPlotter.build(metricsPredToPlot.array);
    //  predPlotter.build();
    //  $.each(recomendationView.issuesViewsToPlot.array, function(i, item){
    //    item.plot();
    //  });
    //});
    
    // On click tab for metrics then replot chart
    $('#myTab a[href="#metricPane"]').on('shown.bs.tab', function (e) {
      console.log(metricsToPlot.array);
      metricsPlotter.build(metricsToPlot.array);
      predPlotter.build();
      $.each(view.issuesViewsToPlot.array, function(i, item){
        item.plot();
      });
    });

    $('#myTab a[href="#skillPane"]').on('shown.bs.tab', function (e) {
      skillPlotter.build(skillsToPlot.array);
      $.each(view.issuesViewsToPlot.array, function(i, item){
        item.plot();
      });
    });


    // Punctuation Form
    // Hacer que '#puntuador' extraiga los users del Jira

    $('#submitPunctuation').click(function() {
      tesys.score(
        $('#puntuador').val(), 
        $('#puntuado').val(),
        $('#issues').val(),
        $('#puntuacion').val()
      ); 
    });

    // Complex metrics form

  $('#submitMetricBtnAddMetric').click(function() {
    // Appends metric into complex metric function
    $('#submitMetricFunction').val($('#submitMetricFunction').val() + " " + $('#submitMetricSelect').find('option:selected').val()) ;
  });

  $('#submitMetricBtnSend').click(function () { 
    $("#submitMetricSpan").empty();
    try {
      //TODO el parser no acepta asociatividad con parentesis
      var metricFormula = parser.parse($("#submitMetricFunction").val());
      tesys.storeMetric(
        $("#submitMetricName").val(), 
        $("#submitMetricDescription").val(),
        metricFormula,
        function (data) {
            markers = JSON.stringify(data);
            $("#submitMetricSpan").html(markers);
        });       
    } catch (e) {
      $("#submitMetricSpan").html(String(e));
    }
  });

  //Sonar Analysis Submit
    $('#submitAnalysisBtnSend').click(function(event) {
      tesys.storeAnalysis(
        $('#submitAnalysisUrl').val(), 
        $('#submitAnalysisUser').val(),
        $('#submitAnalysisPass').val(),
        $('#submitAnalysisRepo').val(),
        $('#submitAnalysisRev').val(),
        $('#submitAnalysisKey').val()
      ); 
    });
  };
  
  return { 
    'start': start 
  };
	
});