package com.pilipili.app.dao;

import com.pilipili.app.domain.User;
import com.pilipili.app.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
// mapper与mybatis产生关联，mybatic把xml和实体类进行关，实体类的方法会自动映射到mybatis的xml中
//api->service->dao，最终影响database, dao->service->api->前端
public interface UserDao {
    User getUserByPhone(String phone);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);

    User getUserById(Long id);

    UserInfo getUserInfoByUserId(Long userId);

    Integer updateUsers(User user);

    User getUserByPhoneOrEmail(String phone, String email);
}
