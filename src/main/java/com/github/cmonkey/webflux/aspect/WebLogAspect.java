package com.github.cmonkey.webflux.aspect;

import com.github.cmonkey.webflux.utils.FastJsonUtils;
import com.github.cmonkey.webflux.utils.IPThreadLocal;
import com.github.cmonkey.webflux.utils.RemoteIpHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//@Order(-1)
//@Aspect
//@Component
public class WebLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.github.cmonkey.webflux.controller.*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("URL = {}" ,request.getRequestURL().toString());
        logger.info("HTTP_METHOD = {} " , request.getMethod());
        logger.info("remoteIP = {} " , RemoteIpHelper.getRemoteIpFrom(request));
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        Object[] objects = joinPoint.getArgs();

        List<Object> list = Arrays.stream(objects).filter(o -> {
            if(o instanceof HttpServletRequest){
                return false;
            }else {
                return !(o instanceof HttpServletResponse);
            }
        }).collect(Collectors.toList());

        logger.info("REQUEST : " + FastJsonUtils.toJSONString(list));

        IPThreadLocal.setRemoteIp(RemoteIpHelper.getRemoteIpFrom(request));
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("RESPONSE : " + FastJsonUtils.toJSONString(ret));
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()) / 1000 + "(s)");
    }
}
