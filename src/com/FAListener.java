package com;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.json.JSONException;

public interface FAListener extends Remote {
	public void alarmsChanged(String alarms) throws RemoteException, JSONException;
}
