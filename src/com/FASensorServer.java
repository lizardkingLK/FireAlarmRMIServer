package com;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class FASensorServer {
	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		System.setProperty("java.security.policy", "file:allowall.policy");
		
		FASensor faSensor = new FASensorImpl();
		
		try {
			Naming.rebind("rmi://localhost/faSensor", faSensor);
			
			Thread thread = new Thread((Runnable) faSensor);
			thread.start();
			
			System.out.println("FireAlarmSensorServer ONLINE...");
		} 
		catch (RemoteException | MalformedURLException e) {
			System.out.println("FireAlarmSensorServer Failed");
			System.out.println(e);
		}
	}
}
