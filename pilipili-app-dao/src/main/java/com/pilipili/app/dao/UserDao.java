package com.pilipili.app.dao;

import com.alibaba.fastjson.JSONObject;
import com.pilipili.app.domain.User;
import com.pilipili.app.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;


// mapper与mybatis产生关联，mybatic把xml和实体类进行关，实体类的方法会自动映射到mybatis的xml中
//api->service->dao，最终影响database, dao->service->api->前端
@Mapper
public interface UserDao {
    User getUserByPhone(String phone);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);

    User getUserById(Long id);

    UserInfo getUserInfoByUserId(Long userId);

    Integer updateUsers(User user);

    Integer updateUserInfos(UserInfo userInfo);

    User getUserByPhoneOrEmail(String phone, String email);

    List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList);

    Integer pageCountUserInfos(Map<String, Object> params);

    List<UserInfo> pageListUserInfos(JSONObject params);
}
