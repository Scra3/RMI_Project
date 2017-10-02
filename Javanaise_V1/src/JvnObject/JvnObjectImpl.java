package JvnObject;

import Server.Interfaces.JvnRemoteServer;
import java.io.Serializable;
import java.util.ArrayList;
import jvn.JvnException;

public class JvnObjectImpl implements JvnObject.Interfaces.JvnObject {

    Serializable objectRemote;
    int id;
    Lock state;
    public ArrayList<JvnRemoteServer> servers;

    public JvnObjectImpl() {
        servers = new ArrayList<>();
    }

    public JvnObjectImpl(Serializable objectRemote, int id, Lock lock) {
        this.objectRemote = objectRemote;
        this.id = id;
        this.state = lock;
        this.servers = new ArrayList();
    }

    @Override
    public void jvnLockRead() throws JvnException {
        state = Lock.RLT;
    }

    @Override
    public void jvnLockWrite() throws JvnException {
        state = Lock.WLT;
    }

    @Override
    public void jvnUnLock() throws JvnException {
        if (state == Lock.WLT || state == Lock.RLT_WLC) {
            state = Lock.WLC;
        } else if (state == Lock.RLT) {
            state = Lock.RLC;
        }
    }

    @Override
    public int jvnGetObjectId() throws JvnException {
        return id;
    }

    @Override
    public Serializable jvnGetObjectState() throws JvnException {
        return objectRemote;
    }

    @Override
    public void jvnInvalidateReader() throws JvnException {
        
    }

    @Override
    public Serializable jvnInvalidateWriter() throws JvnException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Serializable jvnInvalidateWriterForReader() throws JvnException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Serializable getObjectRemote() {
        return objectRemote;
    }

    public void setObjectRemote(Serializable objectRemote) {
        this.objectRemote = objectRemote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Lock getLock() {
        return state;
    }

    public void setLock(Lock lock) {
        this.state = lock;
    }

    public ArrayList<JvnRemoteServer> getServers() {
        return servers;
    }

    public void setServers(ArrayList<JvnRemoteServer> servers) {
        this.servers = servers;
    }

}
