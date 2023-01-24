package com.wiselab.WISExpendi.DTO;

import lombok.Data;

@Data
public class ExpenditureCell {

    // 일자 적요 지불처 예산액 지불액 누계 프로젝트 비고

    private String date;                   // 일자
    private String abs;                    // 적요
    private String expendDepartment;       // 지불처
    private int budgetAmt;                 // 예산액
    private int paymentAmt;                // 지불액
    //private int sum;                     // 누계
    private String projectNm;              // 프로젝트
    private String etc;                    // 비고
    private String regID;                  // 등록인

}
