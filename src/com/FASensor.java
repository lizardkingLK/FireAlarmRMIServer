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
	public boolean addLocation(String f, String l, String lid) throws IOException,RemoteException;
	public String getLocations() throws IOException,JSONException;
	public String getNewAlarmID() throws IOException,JSONException;
	public boolean addAlarm(String aid, String handler, String lid, int smoke, int co2, int activeState, int workingState) throws IOException,RemoteException;
	public boolean updateAlarm(String aid, int smoke, int co2, String lid, int workingState, String handler, int activeState) throws IOException,RemoteException;
}