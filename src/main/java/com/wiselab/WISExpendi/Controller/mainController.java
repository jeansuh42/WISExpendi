package com.wiselab.WISExpendi.Controller;


import com.wiselab.WISExpendi.DTO.*;
import com.wiselab.WISExpendi.Service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class mainController {

	@Autowired
	CommonService commonService;

	@ResponseBody
	@PostMapping(value = "/test")
	public String test(@RequestBody List<ReceiptData> list) {
		System.out.println(list);
		return "forward:/poiExcel";
	}

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

	@PostMapping(value = "/receipt")
	public @ResponseBody ResponseEntity<String> addRecipt(@RequestBody ReceiptData recipt) throws Exception{
		ReceiptData savedReceipt = commonService.insertRecipt(recipt);
		return ResponseEntity.ok("OK");
	}

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