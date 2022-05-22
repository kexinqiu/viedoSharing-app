package com.pilipili.app.api.support;

import com.pilipili.app.domain.exception.ConditionException;
import com.pilipili.app.service.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

//将token放入前端请求头中，通过usersupport从请求中拿到用户信息

@Component
public class UserSupport {

    public Long getCurrentUserId() {
        //获取token
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        Long userId = TokenUtil.verifyToken(token);
        if(userId < 0) {
            throw new ConditionException("非法用户");
        }
        //this.verifyRefreshToken(userId);
        return userId;
    }
}
