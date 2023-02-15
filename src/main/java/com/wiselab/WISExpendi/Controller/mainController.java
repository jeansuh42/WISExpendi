package com.wiselab.WISExpendi.Controller;


import com.wiselab.WISExpendi.DTO.*;
import com.wiselab.WISExpendi.Service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
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

	@GetMapping("/recipts/abstract")
	public ResponseEntity<List<YearReceipts>> getAbstractData(User user) {
		try {
			List<YearReceipts> data = commonService.getUserReceipts(user.getUuid());
			return ResponseEntity.ok(data);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/recipts")
	public ResponseEntity<MonthReceipts> getMonthData(User user, String dateStr) {

		try {
			// dateStr = "2023-01"
			MonthReceipts data = commonService.getUserMonthReceipts(user.getUuid(), dateStr);
			return ResponseEntity.ok(data);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping(value = "/recipt")
	public @ResponseBody ResponseEntity<String> addRecipt(@RequestBody ReceiptData recipt) throws Exception{
		ReceiptData savedReceipt = commonService.insertRecipt(recipt);
		return ResponseEntity.ok("OK");
	}

	@PutMapping("/receipt/{id}")
	public ResponseEntity<ReceiptData> updateReceipt(@PathVariable("rcptNo") int rcptNo, @RequestBody ReceiptData data) throws Exception {

		Optional<ReceiptData> optionalRec = Optional.ofNullable(commonService.findByRcptNo(rcptNo));

		if(optionalRec.isPresent()) {
			ReceiptData updatedReceipt = commonService.updateRecipt(rcptNo, data);
			return ResponseEntity.ok(updatedReceipt);
		}else {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/receipt/{id}")
	public ResponseEntity<ReceiptData> deleteReceipt(@PathVariable("id") int id) throws Exception {
		int result = commonService.deleteRecipt(id);
		if (result == 1) {
			return ResponseEntity.status(200).build();
		}else {
			return ResponseEntity.badRequest().build();
		}

	}

}