package com.wiselab.WISExpendi.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ExpenditureSheet {

    private String paymentBasis;
    private String name;
    private String inputDate;
    private String mailAdd;
    private List<ExpenditureCell> cellList;

}
