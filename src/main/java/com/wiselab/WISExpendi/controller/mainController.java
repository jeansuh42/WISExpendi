package com.wiselab.WISExpendi.controller;

import com.wiselab.WISExpendi.DTO.ExpenditureCell;
import com.wiselab.WISExpendi.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class mainController {

	@Autowired
	CommonService commonService;

	@ResponseBody
	@PostMapping(value = "/test")
	public String test(@RequestBody List<ExpenditureCell> list) {
		System.out.println(list);
		return "forward:/poiExcel";
	}


	@PostMapping(value = "/recipt")
	public @ResponseBody ResponseEntity<String> addRecipt(@RequestBody ExpenditureCell cell) throws Exception{
		System.out.println(cell);
		commonService.insertRecipt(cell);
		return ResponseEntity.ok("OK");
	}

//	@PutMapping(value = "/recipt")
//	public String updRecipt(@RequestBody ExpenditureCell cell) {
//		return "forward:/poiExcel";
//	}
//
//	@PostMapping(value = "/recipt")
//	public String delRecipt(@RequestParam int idx) {
//		System.out.println(idx);
//		return "forward:/poiExcel";
//	}


}