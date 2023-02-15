package com.wiselab.WISExpendi.Controller;

import java.io.*;
import javax.servlet.http.HttpServletResponse;

import com.wiselab.WISExpendi.DTO.ExpenditureSheet;
import com.wiselab.WISExpendi.Common.MailService;
import com.wiselab.WISExpendi.Service.CommonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PoiController {
	@Autowired
	CommonService commService;

	@Autowired
	MailService mailSender;

	@GetMapping(value = "/")
	public String getExcelTest() throws Exception{
		return "home";
	}

	@PostMapping(value = "/sendMail")
	public ResponseEntity<InputStreamResource> downloadExcel(HttpServletResponse response, @RequestBody ExpenditureSheet sheetData) throws IOException {

		try {
			InputStream res = commService.makeExcelFile(sheetData);
			mailSender.sendMailAttachment(convertInputStreamToFile(res),sheetData );

			return ResponseEntity.ok()
					.header("Content-Disposition", "attachment;filename=지출경비.xlsx") //
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(new InputStreamResource(res));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static File convertInputStreamToFile(InputStream in) throws IOException {

		File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
		tempFile.deleteOnExit();

		copyInputStreamToFile(in, tempFile);

		return tempFile;
	}

	private static void copyInputStreamToFile(InputStream inputStream, File file) throws FileNotFoundException {

		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			int read;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


}
