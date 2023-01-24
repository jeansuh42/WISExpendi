package com.wiselab.WISExpendi.service;

import com.wiselab.WISExpendi.DTO.ExpenditureCell;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface CommonService {

    public void insertRecipt(ExpenditureCell dto) throws Exception;


}
