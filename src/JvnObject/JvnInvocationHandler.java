/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JvnObject;

import JvnObject.Interfaces.JvnObject;
import Server.JvnServerImpl;
import static Server.JvnServerImpl.js;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import jvn.JvnException;

/**
 *
 * @author scra
 */
public class JvnInvocationHandler implements InvocationHandler {

    JvnObject jo;

    public JvnInvocationHandler(Serializable object, String name) {
        JvnServerImpl.jvnGetServer();
        jo = js.jvnLookupObject(name);
        if (jo == null) {
            try {
                jo = js.jvnCreateObject(object);
                js.jvnRegisterObject(name, jo);
                jo.jvnUnLock();
            } catch (JvnException ex) {
                System.out.println(ex);
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = new Object();

        if (method.isAnnotationPresent(MethodAnnotation.class)) {
            String annot = method.getAnnotation(MethodAnnotation.class).type();
            switch (annot) {
                case "write":
                    jo.jvnLockWrite();
                    break;
                case "read":
                    jo.jvnLockRead();
                    break;
                default:
                    throw new JvnException("Method does not exist");
            }

            result = method.invoke(jo.jvnGetObjectState(), args);
            jo.jvnUnLock();
        }
        return result;
    }
}
