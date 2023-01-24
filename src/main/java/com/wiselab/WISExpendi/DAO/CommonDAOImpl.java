package com.wiselab.WISExpendi.DAO;
import com.wiselab.WISExpendi.DTO.ExpenditureCell;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommonDAOImpl implements CommonDAO {

    @Autowired
    private SqlSession sql;

    private static final String NS = "com.WISExpendi.wiselab.commonMapper";

    @Override
    public void insertRecipt(ExpenditureCell dto) throws Exception {
        System.out.println(dto);
        sql.insert(NS + ".insertRecipt", dto);
    }

}