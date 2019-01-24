package com.springboot.utils.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 注解aspect
 * @Author: chenmingjian
 * @Date: 18-11-22 18:23
 */
@Aspect
@Component
@Slf4j
public class SystemLogAspect {

    private static ObjectMapper mapper = new ObjectMapper();

    private static final String DESCRIPTION = "description";

    private static final String PRINTINPUTPARAM = "printInputPram";


    @Pointcut("execution(* com.beeseven.wechat.controller..*.*(..))")
    public void controller() { }

    @Pointcut("execution(* com.beeseven.wechat.service.impl..*.*(..)) ")
    public void service() {}

    @Pointcut("execution(* com.beeseven.wechat.util..*.*(..))")
    public void utils() {}

    @Around("service() || utils()")
    public Object other(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        ServerDescription serverDescription = getServerDescription(joinPoint, request);

        Map<String, Object> map = getSystemLogAnnotationValue(joinPoint);
        if(map.containsKey(PRINTINPUTPARAM) && !(boolean) map.get(PRINTINPUTPARAM)){
            log.info("类型：入参, 请求方法: {}, 请求参数: 因为请求参数过大，不予输出, 服务器ip: {}, 服务器名: {}, 服务器端口: {}",serverDescription.getMothedName(), serverDescription.getIp(), serverDescription.getServerName(), serverDescription.getPort());
        }else {
            log.info("类型：入参, 请求方法: {}, 请求参数: {}, 服务器ip: {}, 服务器名: {}, 服务器端口: {}", serverDescription.getMothedName(), serverDescription.getRequestParam(), serverDescription.getIp(), serverDescription.getServerName(), serverDescription.getPort());
        }


        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        long totalTime = System.currentTimeMillis() - startTime;

        log.info("类型：出参, 请求方法: {}, 返回结果: {}, 服务器ip: {}, 服务器名: {}, 服务器端口: {}, 运行时间: {}", serverDescription.getMothedName(), result, serverDescription.getIp(), serverDescription.getServerName(), serverDescription.getPort(), totalTime);

        return result;
    }



    @Around("controller()")
    public Object controller(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        ServerDescription serverDescription = getServerDescription(joinPoint, request);


        Map<String, Object> map = getSystemLogAnnotationValue(joinPoint);
        if(map.containsKey(PRINTINPUTPARAM) && !(boolean) map.get(PRINTINPUTPARAM)){
            log.info("类型：入参, 请求url: {}, 请求方法: {}, 请求参数: 因为请求参数过大，不予输出, 服务器ip: {}, 服务器名: {}, 服务器端口: {}", serverDescription.getUrl(), serverDescription.getMothedName(),  serverDescription.getIp(), serverDescription.getServerName(), serverDescription.getPort());
        }else {
            log.info("类型：入参, 请求url: {}, 请求方法: {}, 请求参数: {}, 服务器ip: {}, 服务器名: {}, 服务器端口: {}",  serverDescription.getUrl(), serverDescription.getMothedName(), serverDescription.getRequestParam(), serverDescription.getIp(), serverDescription.getServerName(), serverDescription.getPort());
        }

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        long totalTime = System.currentTimeMillis() - startTime;

        log.info("类型：出参, 请求url: {}, 请求方法: {}, 返回结果: {}, 服务器ip: {}, 服务器名: {}, 服务器端口: {}, 运行时间: {}", serverDescription.getUrl(), serverDescription.getMothedName(), result, serverDescription.getIp(), serverDescription.getServerName(), serverDescription.getPort(), totalTime);

        return result;
    }

    /**
     * 获取@controllerLog 注解上信息
     *
     * @param joinPoint
     * @return map
     * @throws Exception
     */
    public Map<String, Object> getSystemLogAnnotationValue(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        Map<String, Object> map = new HashMap<>();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] classes = method.getParameterTypes();
                if (classes.length == arguments.length) {
                    //取入参数据
                    if(method.getAnnotation(SystemLog.class) == null){
                        break;
                    }

                    String description = method.getAnnotation(SystemLog.class).description();
                    boolean printInputParam = method.getAnnotation(SystemLog.class).printInputParam();
                    map.put(DESCRIPTION, description);
                    map.put(PRINTINPUTPARAM,printInputParam);
                    break;
                }
            }
        }

        return map;

    }


    private String toJson(Object obj)
    {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
//            log.error("IOException: ", e);
        }
        return null;
    }

    /**
     * 获得当前页面客户端的IP
     * @return 当前页面客户端的IP
     * */
    private String getIPAddress(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }


    /**
     * 获取请求参数信息
     */
    private ServerDescription getServerDescription(ProceedingJoinPoint joinPoint,HttpServletRequest request) {
        ServerDescription serverDescription = new ServerDescription();
        //服务器名
        serverDescription.setServerName(request.getServerName());

        //服务器ip
        serverDescription.setIp(getIPAddress(request));

        //服务器端口
        serverDescription.setPort(request.getServerPort());
        // url
        serverDescription.setUrl(request.getRequestURI());

        String mothedName = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
        serverDescription.setMothedName(mothedName);

        //获取用户请求方法的参数并序列化为JSON格式字符串
        StringBuilder requestParam = new StringBuilder();
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                Object arg = joinPoint.getArgs()[i];
                if (arg == null || arg instanceof HttpServletResponse || arg instanceof HttpServletRequest || arg instanceof MultipartFile) {
                    requestParam.append("null;");
                } else if (arg.getClass().isPrimitive() || arg.getClass() == String.class) {
                    requestParam.append(arg.toString());
                } else {
                    requestParam.append(toJson(arg)).append(";");
                }
            }
        }

        serverDescription.setRequestParam(requestParam.toString());

        return serverDescription;
    }


}