
import JvnObject.Interfaces.JvnObject;
import Server.JvnServerImpl;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import jvn.JvnException;
import java.lang.reflect.Proxy;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author shannix
 */
public class InvocationProxy implements InvocationHandler {

    JvnObject ob;
    JvnServerImpl js;

    public InvocationProxy(String jon, Serializable object) throws JvnException {
        js = JvnServerImpl.jvnGetServer();
        ob = js.jvnLookupObject(jon);
        if (ob == null) {
            ob = js.jvnCreateObject(object);
            ob.jvnUnLock();
            js.jvnRegisterObject(jon, ob);
        }

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("Method invoke " + method.getName());

        Object result = null;
        if (method.isAnnotationPresent(MethodAnnotation.class)) {
            String annotation = method.getAnnotation(MethodAnnotation.class).type();

            switch (annotation) {
                case "write":
                    ob.jvnLockWrite();
                    result = method.invoke(ob.jvnGetObjectState(), args);
                    ob.jvnUnLock();
                    break;
                case "read":
                    ob.jvnLockRead();
                    result = method.invoke(ob.jvnGetObjectState(), args);
                    ob.jvnUnLock();
                    break;
            }

        }
        return result;
    }

    public static Object newInstance(Serializable obj, String jon) throws JvnException {
        return Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new InvocationProxy(jon, obj));

    }
}
