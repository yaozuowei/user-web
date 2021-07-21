package com.yzw.web.aop;

import com.yzw.web.common.util.LoginUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author YaoZuoWei
 * @date 2021/1/19 10:51
 * @Description:
 */
@Aspect
@Component
@Slf4j
public class WebControllerAop {
    /**
     * 指定切点
     * 匹配 com.example.demo.controller包及其子包下的所有类的所有方法
     */
    @Pointcut("execution(public * com.yzw.web.core.controller.*.*(..))")
    public void  optlog(){
    }

    /**
     * 前置通知，方法调用前被调用
     * @param joinPoint
     */
    @Before("optlog()")
    public void doBefore(JoinPoint joinPoint){
        //获取目标方法的参数信息
        Object[] obj = joinPoint.getArgs();

        Signature signature = joinPoint.getSignature();
        //代理的是哪一个方法
        String methodName = signature.getName();

        //AOP代理类的名字
        String className = signature.getDeclaringTypeName();

        MethodSignature methodSignature = (MethodSignature) signature;
        String[] strings = methodSignature.getParameterNames();
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = attributes.getRequest();
        StringBuffer content = new StringBuffer();
        content.append("开始时间："+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        content.append(" url="+req.getRequestURI());
        content.append(" ,paramName=").append(Arrays.toString(strings));
        content.append(" ,paramValue=").append( Arrays.toString(joinPoint.getArgs()));
        content.append( ", clientIp=").append( req.getRemoteAddr());
        content.append(" ,HTTP_METHOD=").append(req.getMethod());
        content.append(" ,methodName=").append(methodName);
        content.append(" ,methodClassName=").append(className);
        log.info("LOG--start："+content);
        //设置请求开始时间
        req.setAttribute("STARTTIME2", System.currentTimeMillis());
    }

    /**
     * 处理完请求返回内容
     * @param ret
     * @throws Throwable
     */

    @AfterReturning(returning = "ret", pointcut = "optlog()")
    public void doAfterReturning(Object ret){
        // 处理完请求，返回内容
        //log.info("方法的返回值 : " + ret);
    }

    /**
     * 后置异常通知
     * @param joinPoint
     */

    @AfterThrowing(value = "optlog()",throwing = "ex")
    public void AfterThrowing(JoinPoint joinPoint,Throwable ex){
        Signature signature = joinPoint.getSignature();
        //代理的是哪一个方法
        String methodName = signature.getName();

        //AOP代理类的名字
        String className = signature.getDeclaringTypeName();

        StringBuffer content = new StringBuffer();
        content.append(" methodName=").append(methodName);
        content.append(" ,methodClassName=").append(className);
        content.append(" 方法异常时执行.....");
        log.error(content.toString());
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw,true));
        log.error(sw.getBuffer().toString());
    }

    /**
     * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
     * @param joinPoint
     */
    @After("optlog()")
    public void after(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = attributes.getRequest();
        //当前时间
        long currentTime = System.currentTimeMillis();
        long  startTime = Long.valueOf(req.getAttribute("STARTTIME2").toString());

        Signature signature = joinPoint.getSignature();
        //代理的是哪一个方法
        String methodName = signature.getName();

        //AOP代理类的名字
        String className = signature.getDeclaringTypeName();
        log.info("LOG--end: "+"结束时间："+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" "+className+"."+methodName+"请求耗时："+(currentTime - startTime)+"ms 操作人---"+ LoginUtil.getLoginName());
    }
}
