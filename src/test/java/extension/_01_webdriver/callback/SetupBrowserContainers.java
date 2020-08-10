package extension._01_webdriver.callback;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Testcontainers
@Execution(CONCURRENT)
@ExtendWith(SetupBaseTestExtension.class)
public @interface SetupBrowserContainers {
}
