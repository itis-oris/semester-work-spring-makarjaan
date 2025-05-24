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
public class ExceptionLoggingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionLoggingAspect.class);

    @Pointcut("@annotation(Loggable)")
    public void loggableMethods() {}

    @Around("loggableMethods()")
    public Object logExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String className = signature.getDeclaringType().getSimpleName();
            String methodName = signature.getName();

            LOG.error("Exception in {}.{}: {}", className, methodName, ex.getMessage(), ex);

            throw ex;
        }
    }
}

