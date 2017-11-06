/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coordinator;

import JvnObject.JvnObjectImpl;
import Server.Interfaces.JvnRemoteServer;
import java.util.ArrayList;

/**
 *
 * @author scra
 */
public class ObjectManager {

    JvnObjectImpl jvnObjectImpl;
    ArrayList<JvnRemoteServer> readerServers;
    JvnRemoteServer writerServer;
    int utilizationDegree;

    public ObjectManager(JvnObjectImpl jvnObjectImpl, JvnRemoteServer writerServer) {
        this.jvnObjectImpl = jvnObjectImpl;
        this.readerServers = new ArrayList();
        this.writerServer = writerServer;
        this.utilizationDegree = 1;
    }

    public JvnObjectImpl getJvnObjectImpl() {
        return jvnObjectImpl;
    }

    public void setJvnObjectImpl(JvnObjectImpl jvnObjectImpl) {
        this.jvnObjectImpl = jvnObjectImpl;
    }

    public ArrayList<JvnRemoteServer> getReaderServers() {
        this.utilizationDegree++;
        return readerServers;
    }

    public void setReaderServers(ArrayList<JvnRemoteServer> readerServers) {
        this.readerServers = readerServers;
    }

    public JvnRemoteServer getWriterServer() {
        return writerServer;
    }

    public void setWriterServer(JvnRemoteServer writerServer) {
        this.writerServer = writerServer;
        this.utilizationDegree++;
    }

    public void removeReaderServers() {
        readerServers.clear();
    }

    public void removeWriterServer() {
        writerServer = null;
    }

    public int getUtilizationDegree() {
        return utilizationDegree;
    }

    public void setUtilizationDegree(int utilizationDegree) {
        this.utilizationDegree = utilizationDegree;
    }
    
    
}
