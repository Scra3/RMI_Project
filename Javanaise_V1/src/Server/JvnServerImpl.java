package Server;

/**
 * *
 * JAVANAISE Implementation JvnServerImpl class Contact:
 *
 * Authors:
 */
import Configs.Config;
import Coordinator.Interfaces.JvnRemoteCoord;
import Coordinator.JvnCoordImpl;
import JvnObject.Interfaces.JvnObject;
import JvnObject.JvnObjectImpl;
import Server.Interfaces.JvnLocalServer;
import Server.Interfaces.JvnRemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;
import jvn.JvnException;

public class JvnServerImpl extends UnicastRemoteObject
        implements JvnLocalServer, JvnRemoteServer {

    // A JVN server is managed as a singleton 
    public static JvnServerImpl js = null;
    public JvnRemoteCoord look_up;
    public Collection<JvnObjectImpl> cache;

    /**
     * Default constructor
     *
     * @throws JvnException
     *
     */
    private JvnServerImpl() throws Exception {
        super();
        look_up = (JvnRemoteCoord) Naming.lookup(Config.coordinatorHost);
        cache = new ArrayList<>();
    }

    /**
     * Static method allowing an application to get a reference to a JVN server
     * instance
     *
     * @return
     *
     */
    public static JvnServerImpl jvnGetServer() {
        if (js == null) {
            try {
                js = new JvnServerImpl();
            } catch (Exception e) {
                return null;
            }
        }
        return js;
    }

    /**
     * The JVN service is not used anymore
     *
     * @throws JvnException
     *
     */
    @Override
    public void jvnTerminate() throws jvn.JvnException {
        try {
            look_up.jvnTerminate(js);
        } catch (RemoteException ex) {
            Logger.getLogger(JvnServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * creation of a JVN object
     *
     * @param o : the JVN object state
     * @throws JvnException
     *
     */
    @Override
    public JvnObject jvnCreateObject(Serializable o) throws jvn.JvnException {
        JvnObject jvnObject = null;
        try {
            jvnObject = new JvnObjectImpl(o, look_up.jvnGetObjectId(), JvnObject.Lock.WLT);
        } catch (RemoteException ex) {
            Logger.getLogger(JvnServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jvnObject;
    }

    /**
     * Associate a symbolic name with a JVN object
     *
     * @param jon : the JVN object name
     * @param jo : the JVN object
     * @throws JvnException
     *
     */
    @Override
    public void jvnRegisterObject(String jon, JvnObject jo) throws jvn.JvnException {
        try {
            cache.add((JvnObjectImpl) jvnCreateObject(jo));
            look_up.jvnRegisterObject(jon, jo, js);
        } catch (RemoteException ex) {
            Logger.getLogger(JvnServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Provide the reference of a JVN object beeing given its symbolic name
     *
     * @param jon : the JVN object name
     * @return the JVN object
     * @throws JvnException
     *
     */
    @Override
    public JvnObject jvnLookupObject(String jon) throws jvn.JvnException {
        JvnObject jvnObject = null;
        try {
            jvnObject = look_up.jvnLookupObject(jon, js);
        } catch (RemoteException ex) {
            Logger.getLogger(JvnServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jvnObject;
    }

    /**
     * Get a Read lock on a JVN object
     *
     * @param joi : the JVN object identification
     * @return the current JVN object state
     * @throws JvnException
     *
     */
    @Override
    public Serializable jvnLockRead(int joi) throws JvnException {
        Serializable jvnObject = null;
        try {
            jvnObject = look_up.jvnLockRead(joi, js);
        } catch (RemoteException ex) {
            Logger.getLogger(JvnServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jvnObject;
    }

    /**
     * Get a Write lock on a JVN object
     *
     * @param joi : the JVN object identification
     * @return the current JVN object state
     * @throws JvnException
     *
     */
    @Override
    public Serializable jvnLockWrite(int joi) throws JvnException {
        Serializable jvnObject = null;
        try {
            jvnObject = look_up.jvnLockWrite(joi, js);
        } catch (RemoteException ex) {
            Logger.getLogger(JvnServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jvnObject;
    }

    /**
     * ******************************* CALL BY COORD
     * *************************************
     */
    /**
     * Invalidate the Read lock of the JVN object identified by id called by the
     * JvnCoord
     *
     * @param joi : the JVN object id
     * @return void
     * @throws java.rmi.RemoteException,JvnException
     * @throws jvn.JvnException
     *
     */
    @Override
    public void jvnInvalidateReader(int joi) {
        JvnObject jvnObject = getJvnObjectByIdInCache(joi);
        try {
            jvnObject.jvnInvalidateReader();
        } catch (JvnException ex) {
            Logger.getLogger(JvnServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Invalidate the Write lock of the JVN object identified by id
     *
     * @param joi : the JVN object id
     * @return the current JVN object state
     * @throws java.rmi.RemoteException,JvnException
     * @throws jvn.JvnException
     *
     */
    @Override
    public Serializable jvnInvalidateWriter(int joi) throws java.rmi.RemoteException, jvn.JvnException {
        JvnObject jvnObject = getJvnObjectByIdInCache(joi);
        try {
            jvnObject.jvnInvalidateWriter();
        } catch (JvnException ex) {
            Logger.getLogger(JvnServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jvnObject;
    }

    /**
     * Reduce the Write lock of the JVN object identified by id
     *
     * @param joi : the JVN object id
     * @return the current JVN object state
     * @throws java.rmi.RemoteException,JvnException
     * @throws jvn.JvnException
     *
     */
    @Override
    public Serializable jvnInvalidateWriterForReader(int joi) throws java.rmi.RemoteException, jvn.JvnException {
        JvnObject jvnObject = getJvnObjectByIdInCache(joi);
        try {
            jvnObject.jvnInvalidateWriterForReader();
        } catch (JvnException ex) {
            Logger.getLogger(JvnServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jvnObject;
    }

    private int getIdOfJvnObject(JvnObject jvnObj) {
        Integer jvnObjectId = null;
        try {
            jvnObjectId = jvnObj.jvnGetObjectId();
        } catch (JvnException ex) {
            Logger.getLogger(JvnServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jvnObjectId;
    }

    private JvnObject getJvnObjectByIdInCache(int id) {
        return cache.stream().filter(jvnObj -> getIdOfJvnObject(jvnObj) == id)
                .findFirst().get();
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(Config.clientServerHostPort_1);
            Naming.rebind(Config.clientServerHost_1, new JvnServerImpl());
            System.err.println("Server ready");

        } catch (Exception ex) {
            Logger.getLogger(JvnServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
