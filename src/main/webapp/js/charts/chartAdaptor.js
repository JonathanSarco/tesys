define(function () {
    /************************/
    /** Char Adaptor Class **/
    /************************/
    /*
        Adapta los datos a mostrar con la interfaz de amCharts 
    */

    function ChartAdaptor() {
    }

    /** functions for normalize data **/
    ChartAdaptor.prototype.generateDataProvider = function (set) {

        var dataProvider = [] ;
        for (i = 0; i < set.length; i++) {
            dataProvider.push( { "skill": set[i] } ) ;
        }    
        return dataProvider;
    };

    /**
    devName: String
    devJson: JSON
    dataProvider: array of JSON
    **/
    ChartAdaptor.prototype.addDataToProvider = function (devName, devJson, dataProvider) {
        for (i = 0; i < dataProvider.length; i++) {
            if (devJson[dataProvider[i]["skill"]] ) {
                dataProvider[i][devName] = devJson[dataProvider[i]["skill"]] ;
            } else {
               dataProvider[i][devName] = null ; 
            }
        } 
    };

    /*
        Como entrada un JSON " [{"key":key, developer:value }, ...] " 
        donde "value" es un numero absoluto que se convertira en porcentaje,
        en relacion a los demas. 
        Como salida el mismo JSON pero con valores entre 0 y 1.

    _dataProvider: JSON 
    return dataProvider: JSON
    */
    ChartAdaptor.prototype.normalizeDataProvider = function (_dataProvider) {

        dataProvider = JSON.parse(JSON.stringify(_dataProvider)) ;
        for (i = 0; i < dataProvider.length; i++) {
            dataProvider[i]["total"] = 0 ;
            acum = 0; 
            for (var key in dataProvider[i]) {
                if (dataProvider[i][key] && !isNaN(dataProvider[i][key] )) {
                    acum+=dataProvider[i][key] ;
                }
            }

            for (var key in dataProvider[i]) {
                if (dataProvider[i][key] && !isNaN(dataProvider[i][key])  ) {
                    dataProvider[i][key]/=acum ;
                    dataProvider[i][key]*=100 ;
                } 
            }
            
            dataProvider[i]["total"] = acum ;
        }

        return dataProvider ;
    };

    return ChartAdaptor ;
});
