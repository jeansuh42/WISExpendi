<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>
	<title>Home</title>
</head>
<body>
    <button onclick='javascript:postJson()'>JSON POST</button>
</body>

<script src="https://code.jquery.com/jquery-2.2.1.js"></script>
<script type="text/javascript">

var obj = {
    "paymentBasis": "개인경비",
    "name": "서은빈",
    "mailAdd": "jeansuh97@gmail.com",
    "inputDate": "2022-12-01",
    "cellList":
    [{"date": "2022-12-01",
     "abs": "test",
     "expendDepartment": "샀어요",
     "budgetAmt": "123",
     "paymentAmt": "123",
     "projectNm": "OK저축은행",
     "etc": "test"},
    {"date": "test",
     "abs": "test",
     "expendDepartment": "test",
     "budgetAmt": "123",
     "paymentAmt": "123",
     "projectNm": "test",
     "etc": "test"},
     {"date": "test",
      "abs": "test",
      "expendDepartment": "test",
      "budgetAmt": "123",
      "paymentAmt": "123",
      "projectNm": "test",
      "etc": "test"},
    {"date": "test",
    "abs": "test",
    "expendDepartment": "test",
    "budgetAmt": "123",
    "paymentAmt": "123",
    "projectNm": "test",
    "etc": "test"}
    ]

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

    xhr.open('POST', "/mail", true);
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

    xhr.send(JSON.stringify(obj));
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
