package com;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class FASensorServer {
	private static FASensorServer faSensorServerInstance = null;
	private FASensorServer() {}

	public static FASensorServer getInstance() {
		if(faSensorServerInstance == null) {
			synchronized (FASensorServer.class) {
				faSensorServerInstance = new FASensorServer();
			}
		}

		return faSensorServerInstance;
	}

	public static void main(String[] args) throws RemoteException {
//	public void startServer() throws RemoteException {
		System.setProperty("java.security.policy", "file:allowall.policy");

		FASensor faSensor = (FASensor) new FASensorImpl();

		try {
			Naming.rebind("rmi://localhost:5500/faSensor", faSensor);

			Thread thread = new Thread((Runnable) faSensor);
			thread.start();

			System.out.println("FireAlarmSensorServer ONLINE...");
		}
		catch (RemoteException | MalformedURLException e) {
			System.out.println("FireAlarmSensorServer Failed");
			System.out.println(e);
		}
	//}
	}
}
