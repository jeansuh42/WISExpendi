package com.wiselab.WISExpendi.DTO;

import lombok.Data;
import java.util.List;
@Data
public class MonthReceipts {

    private String month;
    private List<ReceiptData> receiptData;

}



