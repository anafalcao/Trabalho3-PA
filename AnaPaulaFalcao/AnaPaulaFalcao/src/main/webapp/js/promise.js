function fazerPedidoPostAJAXcomPromise(metodo, sendData, destino){
    return new Promise(function (resolve,reject) {
        var data = JSON.stringify(sendData);
        var xhr = new XMLHttpRequest();
        xhr.open(metodo, destino);
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhr.responseType = 'json';
        xhr.onload = function(){
            if (xhr.readyState===4 && xhr.status === 200) {
                resolve(xhr);
            } else {
                reject(xhr);
            }
        };
        // Handle network errors
        xhr.onerror = function() {
            reject(xhr);
        };
        xhr.send(data);
    });
}

function pedidoGet(destino) {
    return fazerPedidoPostAJAXcomPromise('GET', {}, destino);
}

function pedidoPost(data, destino) {
    return fazerPedidoPostAJAXcomPromise('POST', data, destino);
}