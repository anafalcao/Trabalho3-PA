var medidoresData = [];
var medidoresToSave = [];

function getMedidores() {
    var url = "medidorcontroller";
    pedidoGet(url)
        .then(function (ajaxRequest) {
            var data = ajaxRequest.response.data;
            console.log("data: ", data);
            if (data) {
                medidoresData = data;
                var tBody = document.getElementById("content");

                while (tBody.firstChild) {
                    tBody.removeChild(tBody.firstChild);
                }

                data.map((row) => {
                    console.log("row", row);
                    var newRow = document.createElement("tr");
                    var tdNome = document.createElement("td");
                    tdNome.appendChild(document.createTextNode(row.nome));
                    tdNome.setAttribute("id", row.serial);
                    tdNome.setAttribute("contenteditable", true);
                    tdNome.addEventListener("input", medidorChangeListener);
                    newRow.appendChild(tdNome);

                    var tdTabela = document.createElement("td");
                    tdTabela.appendChild(document.createTextNode(row.tabela));
                    newRow.appendChild(tdTabela);

                    tBody.appendChild(newRow);
                });
            }
        })
        .catch(function (error) {
            console.error(error);
        });
}

function medidorChangeListener(event) {
    console.log("medidorChangeListener");
    var alreadySavedMedidorIndex = medidoresToSave.findIndex((medidor) => {
        return medidor.serial == event.target.id;
    });
    if (alreadySavedMedidorIndex >= 0){
        medidoresToSave[alreadySavedMedidorIndex].nome = event.target.innerText;
        return;
    }
    var changedMedidor = medidoresData.find((medidor) => medidor.serial == event.target.id);
    if (changedMedidor) {
        changedMedidor.nome = event.target.innerText;
        medidoresToSave.push(changedMedidor);
    }
    console.log("medidoresToSave",medidoresToSave);
}

function saveEdits() {
    var url = "medidorcontroller";

    pedidoPost(medidoresToSave, url)
        .then(function (ajaxRequest) {
            console.log(ajaxRequest.response.data);
        })
        .catch(function (error) {
            console.error(error);
        });
}

function addRow() {

    var table = document.getElementById("myTable");

    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);

    var nomeCell = row.insertCell(0);
    var nomeInput = document.createElement("td");
    nomeInput.setAttribute("contenteditable", true);
    nomeInput.id = rowCount;
    nomeInput.addEventListener("input", medidorChangeListener);
    nomeCell.appendChild(nomeInput);

    var tabelaCell = row.insertCell(1);
    tabelaCell.name = "tabela";
    tabelaCell.innerHTML = "medidor00" + rowCount;
    
    medidoresData.push({ serial: rowCount, nome: "", tabela: "medidor00" + rowCount });
}


