<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <title>경비 영수증 청구</title>
</head>
<body>
    <div class="container">
    <h1 class="mt-3 mb-3">경비 영수증 청구</h1>
    <form id="expenseForm">

        <div id="formatInfo">

          <div class="row mb-3">
          <label for="paymentBasis" class="col-sm-2 col-form-label">경비 기준</label>
          <div class="col-sm-10">
            <select class="form-select" id="paymentBasis" name="paymentBasis">
              <option value="개인경비">개인경비</option>
              <option value="법인경비">법인경비</option>
            </select>

          </div>
        </div>
        <div class="row mb-3">
          <label for="name" class="col-sm-2 col-form-label">이름</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" id="name" name="name" value="서은빈"/>
          </div>
        </div>
        <div class="row mb-3">
          <label for="mailAdd" class="col-sm-2 col-form-label">이메일 주소</label>
          <div class="col-sm-10">
            <input type="email" class="form-control" id="mailAdd" name="mailAdd" value="jeansuh97@gmail.com" />
          </div>
        </div>
        <div class="row mb-3">
          <label for="inputDate" class="col-sm-2 col-form-label">기준 일자</label>
          <div class="col-sm-10">
            <input type="date" class="form-control" id="inputDate" name="inputDate" value="2022-12-01"/>
          </div>
        </div>

      </div>


        <hr/>

        <div class="col">
          <div class="list-title d-flex flex-row justify-content-between">
            <h3 class="mb-3 d-inline-block">청구 영수증 목록</h3>
            <div>
                <button type="button" class="btn btn-outline-dark btn-sm mb-1" onclick="addCell()">+</button>
                <button type="button" class="btn btn-outline-success btn-sm mb-1" onclick="postJson()">전송</button>
            </div>
          </div>
          <table class="table table-striped" id="cellTable">
            <thead>
              <tr>
                <th>일자</th>
                <th>적요</th>
                <th>지불처</th>
                <th>예산액</th>
                <th>지불액</th>
                <th>프로젝트명</th>
                <th>주석</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td><input type="date" class="form-control" name="date"></td>
                <td><input type="text" class="form-control" name="abs"></td>
                <td><input type="text" class="form-control" name="expendDepartment"></td>
                <td><input type="number" class="form-control" name="budgetAmt"></td>
                <td><input type="number" class="form-control" name="paymentAmt"></td>
                <td><input type="text" class="form-control" name="projectNm"></td>
                <td><input type="text" class="form-control" name="etc"></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </form>
    </div>
</body>

<script src="https://code.jquery.com/jquery-2.2.1.js"></script>
<script type="text/javascript">

  function addCell() {
    const tbody = document.querySelector("#cellTable tbody");
    const tr = tbody.querySelector("tr");
    const clone = tr.cloneNode(true);

    const inputList = clone.querySelectorAll('td input');
    inputList.forEach( el => { if (el.name !== 'projectNm') { el.value = "" } });

    tbody.appendChild(clone);
  }

  function formToJson() {

    let data = {};
    let receiptArr = [];

    const formatInfo = document.querySelector('#formatInfo');

    const paymentBasisData = document.querySelector('#paymentBasis');
    data[paymentBasisData.name] = paymentBasisData.value;

    const inputs = formatInfo.querySelectorAll('input');

    for (let input of inputs) {
      data[input.name] = input.value;
    }

    const table = document.getElementById("cellTable");
    const tbody = table.getElementsByTagName("tbody")[0];
    const rows = tbody.getElementsByTagName("tr");


    for (let i = 0; i < rows.length; i++) {
      const row = rows[i];
      const cells = row.getElementsByTagName("td");

      const rowData = {};

      for (let j = 0; j < cells.length; j++) {
        const cell = cells[j].firstChild;
        rowData[cell.name] = cell.value
      }

      receiptArr.push(rowData);
    }

    data["cellList"] = receiptArr;

    let jsonData = JSON.stringify(data);
    console.log(jsonData);

    return jsonData
  }


function poiExcel() {
  var formObj = $('#ExcelForm');

  formObj.attr('action', '/poiExcel');
  formObj.attr('method', 'post');
  formObj.submit();

}

function postJson() {
    let blobUrl = "";
    let xhr = new XMLHttpRequest();

    xhr.open('POST', "/sendMail", true);
    xhr.setRequestHeader('content-type', 'application/json');
    xhr.responseType = 'blob';
    xhr.onloadstart = function(e){
        // 파일 생성 대기 메세지
    };
    xhr.onload = function(e) {
        let resp = xhr.response;
        if(resp.type !== "application/octet-stream" || resp.size === 0){
            // 에러 메세지
        } else{
            let contentDispo = xhr.getResponseHeader('Content-Disposition');
            let fileName = contentDispo.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)[1];
            saveOrOpenBlob(resp, fileName, blobUrl);
        }
    };
    xhr.onloadend = function(e){
        // 파일 생성 대기 해제
        // 파일 다운로드 종료 후 Blob url 해제
        window.URL.revokeObjectURL(blobUrl);
    };

    xhr.send(formToJson());
  }

  function saveOrOpenBlob(blob, fileName, blobUrl) {
      let a = document.createElement('a');
      blobUrl = window.URL.createObjectURL(blob);
      a.href = blobUrl;
      a.download = fileName;
      a.dispatchEvent(new MouseEvent('click'));
  }

</script>
</html>