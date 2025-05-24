package com.makarova.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodParamsLoggingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(MethodParamsLoggingAspect.class);

    @Pointcut("@annotation(Loggable)")
    public void loggableMethods() {}

    @Around("loggableMethods()")
    public Object logMethodParameters(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = signature.getDeclaringType().getSimpleName();
        Object[] args = joinPoint.getArgs();

        LOG.info("Method {}.{} called with parameters: {}", className, methodName, java.util.Arrays.toString(args));

        return joinPoint.proceed();
    }
}

