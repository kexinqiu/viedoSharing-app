package com.pilipili.app.service;


import com.pilipili.app.dao.DemoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//启动时，将demoservice作为要发布的bean，加入到上下文中
@Service
public class DemoService {

    @Autowired
    private DemoDao demoDao;

    public Long query(Long id){
        return demoDao.query(id);
    }
}
