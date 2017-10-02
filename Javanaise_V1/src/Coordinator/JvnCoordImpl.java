/**
 * *
 * JAVANAISE Implementation JvnServerImpl class Contact:
 *
 * Authors:
 */
package Coordinator;

import Configs.Config;
import Coordinator.Interfaces.JvnRemoteCoord;
import JvnObject.Interfaces.JvnObject;
import JvnObject.JvnObjectImpl;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;
import jvn.JvnException;
import Server.Interfaces.JvnRemoteServer;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JvnCoordImpl
        extends UnicastRemoteObject implements JvnRemoteCoord {

    public JvnRemoteServer look_up;
    public Map<String, JvnObjectImpl> store;
    public ArrayList<JvnRemoteServer> servers;
    public AtomicInteger counter_object;
    public JvnObjectImpl jvnObj;

    /**
     * Default constructor
     *
     * @throws JvnException
     *
     */
    private JvnCoordImpl() throws Exception {
        store = new HashMap();
        servers = new ArrayList();
        counter_object = new AtomicInteger(0);
    }

    /**
     * Allocate a NEW JVN object id (usually allocated to a newly created JVN
     * object)
     *
     * @return
     * @throws java.rmi.RemoteException,JvnException
     * @throws jvn.JvnException
     *
     */
    @Override
    public synchronized int jvnGetObjectId() throws java.rmi.RemoteException, jvn.JvnException {
        jvnObj = new JvnObjectImpl();
        return counter_object.getAndIncrement();
    }

    /**
     * Associate a symbolic name with a JVN object counter_object
     *
     * @param jon : the JVN object name
     * @param jo : the JVN object
     * @param joi : the JVN object identification
     * @param js : the remote reference of the JVNServer
     * @throws java.rmi.RemoteException,JvnException
     *
     */
    @Override
    public synchronized void jvnRegisterObject(String jon, JvnObject jo, JvnRemoteServer js) throws java.rmi.RemoteException, jvn.JvnException {
        jvnObj.setObjectRemote(jo);
        jvnObj.getServers().add(js);
        store.put(jon, jvnObj);
    }

    /**
     * Get the reference of a JVN object managed by a given JVN server
     *
     * @param jon : the JVN object name
     * @param js : the remote reference of the JVNServer
     * @return
     * @throws java.rmi.RemoteException,JvnException
     *
     */
    @Override
    public synchronized JvnObject jvnLookupObject(String jon, JvnRemoteServer js) throws java.rmi.RemoteException, jvn.JvnException {
        JvnObjectImpl jvnObjImpl = store.get(jon);
        jvnObjImpl.getServers().add(js);
        return jvnObjImpl;
    }

    /**
     * Get a Read lock on a JVN object managed by a given JVN server
     *
     * @param joi : the JVN object identification
     * @param js : the remote reference of the server
     * @return the current JVN object state
     * @throws java.rmi.RemoteException, JvnException
     *
     */
    public synchronized Serializable jvnLockRead(int joi, JvnRemoteServer js) throws java.rmi.RemoteException, JvnException {
        JvnObjectImpl jo = geJvnObjImplById(joi);
        //TODO
        jo.jvnLockRead();
        return jo;
    }

    /**
     * Get a Write lock on a JVN object managed by a given JVN server
     *
     * @param joi : the JVN object identification
     * @param js : the remote reference of the server
     * @return the current JVN object state
     * @throws java.rmi.RemoteException, JvnException
     *
     */
    @Override
    public synchronized Serializable jvnLockWrite(int joi, JvnRemoteServer js) throws java.rmi.RemoteException, JvnException {
        JvnObjectImpl jo = geJvnObjImplById(joi);
        //TODO
        jo.jvnLockWrite();
        return jo;
    }

    /**
     * A JVN server terminates
     *
     * @param js : the remote reference of the server
     * @throws java.rmi.RemoteException, JvnException
     * @throws jvn.JvnException
     *
     */
    @Override
    public synchronized void jvnTerminate(JvnRemoteServer js) throws java.rmi.RemoteException, JvnException {
        servers.remove(js);
        store.entrySet().stream()
                .map(keyValue -> keyValue.getValue()
                        .getServers().remove(js));
    }

    private JvnObjectImpl geJvnObjImplById(int objId) {
        return store.entrySet().stream()
                .filter(keyValue -> keyValue.getValue().getId() == objId)
                .findFirst().get().getValue();
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(Config.coordinatorHostPort);
            Naming.rebind(Config.coordinatorHost, new JvnCoordImpl());
            System.err.println("Coordinator ready on " + Config.coordinatorHost);
        } catch (RemoteException e) {
            System.err.println("Server exception: " + e.toString());

        } catch (Exception ex) {
            Logger.getLogger(JvnCoordImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }
}
