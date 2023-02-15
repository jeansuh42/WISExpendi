package com.wiselab.WISExpendi.Service;


import com.wiselab.WISExpendi.DTO.*;
import org.json.JSONObject;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CommonService {

    public InputStream makeExcelFile(ExpenditureSheet sheetData) throws Exception;

    public ReceiptData insertRecipt(ReceiptData dto) throws Exception;

    public ReceiptData updateRecipt(int id, ReceiptData dto) throws Exception;

    public int deleteRecipt(int id) throws Exception;

    public ReceiptData findByRcptNo(int rcptNo) throws Exception;

    public List<YearReceipts> getUserReceipts(String uuid) throws Exception;

    public MonthReceipts getUserMonthReceipts(String uuid, String month) throws Exception;
}
