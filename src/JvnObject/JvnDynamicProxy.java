package JvnObject;

import java.io.Serializable;
import java.lang.reflect.Proxy;

/**
 *
 * @author scra
 */
public class JvnDynamicProxy {

    public static Object intitialyze(Serializable object, String name) {
        return Proxy.newProxyInstance(
                object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                new JvnInvocationHandler(object, name)
        );
    }
}
