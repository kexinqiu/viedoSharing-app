package com.pilipili.app.api.aspect;


import com.pilipili.app.api.support.UserSupport;
import com.pilipili.app.domain.UserMoment;
import com.pilipili.app.domain.auth.UserRole;
import com.pilipili.app.domain.constant.AuthRoleConstant;
import com.pilipili.app.domain.exception.ConditionException;
import com.pilipili.app.service.UserRoleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//优先级
@Order(1)
@Component
@Aspect
public class DataLimitedAspect {
    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserRoleService userRoleService;

    //切点, 在注解执行到时进行切入
    @Pointcut("@annotation(com.pilipili.app.domain.annotation.DataLimited)")
    public void check(){
    }

    //切入后，前置通知
    @Before("check()")
    public void doBefore(JoinPoint joinPoint){
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        Set<String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        //获取接口参数
        Object[] args = joinPoint.getArgs();
        for(Object arg : args){
            if(arg instanceof UserMoment){
                UserMoment userMoment = (UserMoment)arg;
                String type = userMoment.getType();
                if(roleCodeSet.contains(AuthRoleConstant.ROLE_LV1) && !"0".equals(type)){
                    throw new ConditionException("参数异常");
                }
            }
        }
    }
}
