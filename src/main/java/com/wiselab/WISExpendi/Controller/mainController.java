package com.wiselab.WISExpendi.Controller;


import com.wiselab.WISExpendi.DTO.*;
import com.wiselab.WISExpendi.Service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = {"WiseExpendi RestAPI"})
public class mainController {

	@Autowired
	CommonService commonService;

	@ResponseBody
	@PostMapping(value = "/test")
	public String test(@RequestBody List<ReceiptData> list) {
		System.out.println(list);
		return "forward:/poiExcel";
	}


	@ApiOperation(value = "영수증 개요 조회", notes = "사용자 영수증 데이터 개요를 조회합니다.")
	@ApiImplicitParam(name = "user", value = "사용자", dataType = "string", required = true)
	@GetMapping("/recipts/{user}/abstract")
	public ResponseEntity<List<YearReceipts>> getAbstractData(@PathVariable("user") String userId) {
		// TODO 인증 방법 변경 예정 : User 객체 or Token -> UUID 조회해 오는 방식
		try {
			// User getUser = getUser();
			//List<YearReceipts> data = commonService.getUserReceipts(user.getUuid());
			List<YearReceipts> data = commonService.getUserReceipts(userId);
			return ResponseEntity.ok(data);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@ApiOperation(value = "영수증 상세 조회", notes = "특정 연월 간의 사용자 영수증 데이터를 조회합니다.")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "user", value = "사용자", dataType = "string", required = true),
		@ApiImplicitParam(name = "dateStr", value = "특정 연월", example = "2023-01", dataType = "string", required = true)
	})
	@GetMapping("/recipts/{user}/{dateStr}")
	public ResponseEntity<MonthReceipts> getMonthData(@PathVariable("user") String userId, @PathVariable("dateStr") String dateStr) {
		// TODO 인증 방법 변경 예정 : User 객체 or Token -> UUID 조회해 오는 방식
		try {
			// dateStr = "2023-01"
			//User getUser = getUser();
			//MonthReceipts data = commonService.getUserMonthReceipts(getUser.getUuid(), dateStr);
			MonthReceipts data = commonService.getUserMonthReceipts(userId, dateStr);
			return ResponseEntity.ok(data);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@ApiOperation(value = "영수증 등록", notes = "영수증을 등록합니다.")
	@ApiImplicitParam(name = "recipt", value = "등록 영수증 데이터", paramType="body", dataType = "ReceiptData", required = true,
			defaultValue = "{\"date\": \"지출날짜\", \"abs\": \"적요\", \"expendDepartment\": \"지출처\", \"budgetAmt\": \"예산액\", \"paymentAmt\": \"지불액\", \"projectNm\": \"프로젝트명\", \"etc\": \"주석\", \"regID\": \"등록아이디\"}"
	)
	@PostMapping(value = "/receipt")
	public @ResponseBody ResponseEntity<String> addRecipt(@RequestBody ReceiptData recipt) throws Exception{
		ReceiptData savedReceipt = commonService.insertRecipt(recipt);
		return ResponseEntity.ok("OK");
	}

	@ApiOperation(value = "영수증 수정", notes = "영수증을 수정합니다.")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "rcptNo", value = "수정 영수증 번호", dataType = "int", required = true),
			@ApiImplicitParam(name = "data", value = "수정 영수증 데이터", paramType="body", dataType = "ReceiptData", required = true,
					defaultValue = "{\"date\": \"지출날짜\", \"abs\": \"적요\", \"expendDepartment\": \"지출처\", \"budgetAmt\": \"예산액\", \"paymentAmt\": \"지불액\", \"projectNm\": \"프로젝트명\", \"etc\": \"주석\", \"regID\": \"등록아이디\"}"
			)
	})
	@PutMapping("/receipt/{rcptNo}")
	public ResponseEntity<ReceiptData> updateReceipt(@PathVariable("rcptNo") int rcptNo, @RequestBody ReceiptData data) throws Exception {

		Optional<ReceiptData> optionalRec = Optional.ofNullable(commonService.findByRcptNo(rcptNo));

		if(optionalRec.isPresent()) {
			ReceiptData updatedReceipt = commonService.updateRecipt(rcptNo, data);
			return ResponseEntity.ok(updatedReceipt);
		}else {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "영수증 삭제", notes = "rcptNo 영수증 데이터를 삭제합니다.")
	@ApiImplicitParam(name = "rcptNo", value = "삭제 영수증 번호", dataType = "int", required = true)
	@DeleteMapping("/receipt/{rcptNo}")
	public ResponseEntity<ReceiptData> deleteReceipt(@PathVariable("rcptNo") int rcptNo) throws Exception {
		int result = commonService.deleteRecipt(rcptNo);
		if (result == 1) {
			return ResponseEntity.status(200).build();
		}else {
			return ResponseEntity.badRequest().build();
		}

	}

}