package JvnObject;

/**
 *
 * @author scra
 */
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)

@Target(ElementType.METHOD)
public @interface MethodAnnotation {
    String type();
}
