var wsUri = "ws://127.0.0.1:8080/trabalho3/valoresSocket";
var tBody;
var websocket;

window.onload = function() {
    tBody = document.getElementById("content");
};

function adicionaLinha(row) {
    console.log("row", row);
    var newRow = document.createElement("tr");
    var tdDataHora = document.createElement("td");
    tdDataHora.appendChild(document.createTextNode(row.datahora));
    newRow.appendChild(tdDataHora);

    var tdUmidade = document.createElement("td");
    tdUmidade.appendChild(document.createTextNode(Number.parseFloat(row.umidade).toFixed(2)));
    newRow.appendChild(tdUmidade);

    var tdTemperatura = document.createElement("td");
    tdTemperatura.appendChild(document.createTextNode(Number.parseFloat(row.temperatura).toFixed(2)));
    newRow.appendChild(tdTemperatura);

    tBody.prepend(newRow);
}

function getDadosMedidores() {
    var url = "controller?" +
         
              "medidor=" + document.getElementById("medidor").value +
              "&periodo=" + document.getElementById("periodo").value +
              "&graficotabela=" + document.getElementById("graficotabela").value +
              "&data=" + document.getElementById("data").value;
    
    pedidoGet(url)
        .then(function(ajaxRequest) {
            var data = ajaxRequest.response.data;
            console.log("response: ", data);
            if (data) {
                while (tBody.firstChild) {
                    tBody.removeChild(tBody.firstChild);
                }

                data.map(adicionaLinha);
            }
        })
        .catch(function(error) {
            console.error(error)
        });
}

function ligaManual () {
    console.log('Mudar para manual');
    
    if (websocket !== undefined) {
        websocket.close();
        websocket = undefined;
    }
}

function ligaAutomatico () {
    console.log('Mudar para automatico');

    if (websocket === undefined) {
        try {
            websocket = new WebSocket(wsUri);
        } catch (erro) {
            console.log(erro);
            return;
        }

        websocket.onopen = function(ev){
            console.log('=== Conectou: ');
        };

        websocket.binaryType = "arraybuffer";

        websocket.onmessage = function (evt) {
            var json = JSON.parse(evt.data);
            console.log(json);
            adicionaLinha(json);
        };

        websocket.onerror = function (evt) {
            console.log(evt);
        };
    }
}