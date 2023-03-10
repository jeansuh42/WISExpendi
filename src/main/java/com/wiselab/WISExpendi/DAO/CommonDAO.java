package com.wiselab.WISExpendi.DAO;

import com.wiselab.WISExpendi.DTO.MonthReceipts;
import com.wiselab.WISExpendi.DTO.ReceiptData;
import com.wiselab.WISExpendi.DTO.YearReceipts;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CommonDAO {

    public ReceiptData insertRecipt(ReceiptData dto) throws Exception;

    public ReceiptData updateRecipt(int id, ReceiptData dto) throws Exception;

    public int deleteRecipt(int id) throws Exception;

    public ReceiptData findByRcptNo(int rcptNo) throws Exception;

    public List<YearReceipts> getUserReceipts(String uuid) throws Exception;

    public List<ReceiptData> getUserMonthReceipts(String uuid, String month) throws Exception;

}