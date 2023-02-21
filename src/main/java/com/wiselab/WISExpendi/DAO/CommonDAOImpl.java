package com.wiselab.WISExpendi.DAO;
import com.wiselab.WISExpendi.DTO.MonthReceipts;
import com.wiselab.WISExpendi.DTO.ReceiptData;
import com.wiselab.WISExpendi.DTO.YearReceipts;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommonDAOImpl implements CommonDAO {

    @Autowired
    private SqlSession sql;

    private static final String NS = "com.WISExpendi.wiselab.commonMapper";

    @Override
    public ReceiptData insertRecipt(ReceiptData dto) throws Exception {
        System.out.println(dto);
        sql.insert(NS + ".insertRecipt", dto);
        return dto;
    }

    @Override
    public ReceiptData updateRecipt(int id, ReceiptData dto) throws Exception {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("id", id);
        inputParam.put("receiptData", dto);

        sql.update(NS + ".updateReceipt", inputParam);

        ReceiptData result = findByRcptNo(id);
        return result;
    }

    @Override
    public int deleteRecipt(int id) throws Exception {
        int result = sql.delete(NS + ".deleteReceipt", id);
        return result;
    }

    @Override
    public ReceiptData findByRcptNo(int rcptNo) throws Exception {
        return sql.selectOne(NS + ".findByRcptNo", rcptNo);
    }

    @Override
    public List<YearReceipts> getUserReceipts(String uuid) throws Exception {
        return sql.selectList(NS + ".selectReciptsData", uuid);
    }

    @Override
    public List<ReceiptData> getUserMonthReceipts(String uuid, String yearMonth) throws Exception {


        String[] parts = yearMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        Map<String, Object> params = new HashMap<>();
        params.put("uuid", uuid);

        LocalDate date = LocalDate.of(year, month, 1);
        params.put("startOfMonth", date);
        params.put("endOfMonth", date.withDayOfMonth(date.lengthOfMonth()));

        return sql.selectList(NS + ".selectMonthReciptsData", params);
    }


}