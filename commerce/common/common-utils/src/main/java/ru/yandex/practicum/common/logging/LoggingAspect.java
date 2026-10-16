package ru.yandex.practicum.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Around("@annotation(ru.yandex.practicum.common.logging.Loggable) " +
            "|| @within(ru.yandex.practicum.common.logging.Loggable)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("Entering method: {}.", joinPoint.getSignature());
        log.debug("Request parameters: {}.", joinPoint.getArgs());
        Object result = joinPoint.proceed();
        log.debug("Exiting method: {} - Response: {}.", joinPoint.getSignature(), result);
        return result;
    }
}
