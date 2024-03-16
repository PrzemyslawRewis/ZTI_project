package pl.przemyslawrewis.server.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Aspekt odpowiedzialny za logowanie zdarzeń w aplikacji.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final String apiPointcut = "execution(* pl.przemyslawrewis.server.controllers.*.*(..))";
    private final String exceptionPointcut = "execution(* pl.przemyslawrewis.server.*.*.*(..))";

    /**
     * Pointcut dla metod API.
     */
    @Pointcut(apiPointcut)
    public void APIPointCut() {
    }

    /**
     * Metoda wywoływana przed wykonaniem metody API, loguje rozpoczęcie wykonywania metody.
     *
     * @param joinPoint JoinPoint metody
     */
    @Before("APIPointCut()")
    public void logEnteringAPI(JoinPoint joinPoint) {
        log.info("Aspect logger: The API method will executing {}", joinPoint.getSignature().getName());
    }

    /**
     * Metoda wywoływana po wykonaniu metody API, loguje zakończenie wykonywania metody.
     *
     * @param joinPoint JoinPoint metody
     */
    @After("APIPointCut()")
    public void logExitingAPI(JoinPoint joinPoint) {
        log.info("Aspect logger: The API method finished execute {}", joinPoint.getSignature().getName());
    }

    /**
     * Metoda wywoływana po wystąpieniu wyjątku w dowolnym miejscu w aplikacji, loguje informacje o wyjątku.
     *
     * @param joinPoint JoinPoint metody
     * @param exception Rzucony wyjątek
     */
    @AfterThrowing(value = exceptionPointcut, throwing = "exception")
    public void logsExceptionsFromAnyLocations(JoinPoint joinPoint, Throwable exception) {
        log.error("Aspect logger: We have error in this method {}", joinPoint.getSignature().getName());
        log.error("Aspect logger: The exception message: ".concat(exception.getMessage()));
    }
}

