function genTable(domElem, data){
    let tbl = document.createElement("table");
    tbl.appendChild(genTableHead());
    tbl.appendChild(genTableBody(data));
    domElem.appendChild(tbl)
}

function genDomChild(name, value){
    let d = document.createElement(name);
    d.appendChild(document.createTextNode(value));
    return d
}

function genTableHead(){
    let head = document.createElement("thead");
    let tr = document.createElement("tr");
    tr.appendChild(genDomChild("th", "Day"));
    tr.appendChild(genDomChild("th", "Start Time"));
    tr.appendChild(genDomChild("th", "End Time"));
    tr.appendChild(genDomChild("th", "Room"));
    tr.appendChild(genDomChild("th", "Event"));
    head.appendChild(tr);
    return head
}

function genTableBody(data){
    let tbody = document.createElement("tbody");
    data.forEach(function(e) {
        tbody.appendChild(genTableRow(e))
    })
    return tbody
}

function genTableRow(elem){
    let row = document.createElement("tr");
    row.appendChild(genDomChild("td", elem.day));
    row.appendChild(genDomChild("td", formatTime(elem.startHour, elem.startMinute)));
    row.appendChild(genDomChild("td", formatTime(elem.endHour, elem.endMinute)));
    row.appendChild(genDomChild("td", elem.room));
    row.appendChild(genDomChild("td", elem.event));

    return row
}

function formatNum(num){
    return ("0" + num).slice(-2)
}

function formatTime(hr, min){
    return (formatNum(hr) + ":" + formatNum(min))
}