package com;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import org.json.JSONException;

public interface FASensor extends Remote {
	public String getAlarms() throws IOException,JSONException;
	public boolean authenticateUser(String email,String password) throws IOException,RemoteException;
	public boolean addFAListener(FAListener faListener) throws RemoteException;
	public boolean removeFAListener(FAListener faListener) throws RemoteException;
}
