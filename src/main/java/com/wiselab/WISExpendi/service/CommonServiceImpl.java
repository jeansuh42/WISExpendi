package com.wiselab.WISExpendi.service;


import com.wiselab.WISExpendi.DAO.CommonDAO;
import com.wiselab.WISExpendi.DTO.ExpenditureCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    CommonDAO commonDao;

    @Override
    public void insertRecipt(ExpenditureCell dto) throws Exception {
        System.out.println(dto);
        commonDao.insertRecipt(dto);
    }
}
