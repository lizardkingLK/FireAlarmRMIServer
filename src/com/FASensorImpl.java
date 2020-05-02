package com;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FASensorImpl extends UnicastRemoteObject implements FASensor,Serializable,Runnable {
	private static final long serialVersionUID = 1L;
	private OkHttpClient httpClient = new OkHttpClient();
	private ArrayList<FAListener> listeners = new ArrayList<>();
	volatile JSONArray alarms = null;

	protected FASensorImpl() throws RemoteException {
		super();
	}

	@Override
	public boolean authenticateUser(String email, String password) throws IOException,RemoteException {
		String json = new StringBuilder()
		.append("{"
			+ "\"email\":\""+email+"\","
			+ "\"password\":\""+password+"\""
			+ "}").toString();

		RequestBody requestBody = RequestBody.create (
			MediaType.parse("application/json; charset=UTF-8"), json
		);

		Request request = new Request.Builder()
		.url("http://localhost:8080/FireAlarmRest/rest/UserService/authenticateUser")
		.post(requestBody)
		.build();

		try (Response response = httpClient.newCall(request).execute()) {
			int code = response.code();

			if(code == 201)
				return true;
			else
				return false;
		}
	}

	@Override
	public String getAlarms() throws IOException, JSONException {
		Request request = new Request.Builder()
        .url("http://localhost:8080/FireAlarmRest/rest/AlarmService/getAlarms")
        .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful())
            	throw new IOException("AlarmService not responding" + response);

            String resBody = response.body().string();
            alarms = new JSONArray(resBody);
        }

		return alarms.toString();
	}

	@Override
	public boolean addFAListener(FAListener faListener) {
		if(listeners.add(faListener))
			return true;
		else
			return false;
	}

	@Override
	public boolean removeFAListener(FAListener faListener) {
		if(listeners.remove(faListener))
			return true;
		else
			return false;
	}

	@Override
	public boolean addLocation(String f, String r, String lid) throws IOException,RemoteException {
		String json = new StringBuilder()
		.append("{"
			+ "\"lid\":\""+lid+"\","
			+ "\"floorNo\":\""+f+"\","
			+ "\"roomNo\":\""+r+"\""
			+ "}").toString();

		RequestBody requestBody = RequestBody.create (
			MediaType.parse("application/json; charset=UTF-8"), json
		);

		Request request = new Request.Builder()
		.url("http://localhost:8080/FireAlarmRest/rest/LocationService/addLocation")
		.post(requestBody)
		.build();

		try (Response response = httpClient.newCall(request).execute()) {
			int code = response.code();

			if(code == 201)
				return true;
			else
				return false;
		}
	}

	@Override
	public String getLocations() throws IOException, JSONException {
		JSONArray locations = null;

		Request request = new Request.Builder()
        .url("http://localhost:8080/FireAlarmRest/rest/LocationService/getLocations")
        .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful())
            	throw new IOException("LocationService not responding" + response);

            String resBody = response.body().string();
            locations = new JSONArray(resBody);
        }

		return locations.toString();
	}

	@Override
	public String getNewAlarmID() throws IOException, JSONException {
		JSONObject newAid = null;

		Request request = new Request.Builder()
        .url("http://localhost:8080/FireAlarmRest/rest/AlarmService/getNewAlarmId")
        .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful())
            	throw new IOException("AlarmService not responding" + response);

            String resBody = response.body().string();
            newAid = new JSONObject(resBody);
        }

		return newAid.toString();
	}

	@Override
	public boolean addAlarm(String aid, String handler, String lid, int smoke, int co2, int activeState, int workingState) throws IOException, RemoteException {
		String json = new StringBuilder()
		.append("{"
			+ "\"aid\":\""+aid+"\","
			+ "\"email\":\""+handler+"\","
			+ "\"lid\":\""+lid+"\","
			+ "\"smokeLevel\":\""+smoke+"\","
			+ "\"co2Level\":\""+co2+"\","
			+ "\"isActive\":\""+activeState+"\","
			+ "\"isWorking\":\""+workingState+"\""
			+ "}").toString();

		RequestBody requestBody = RequestBody.create (
			MediaType.parse("application/json; charset=UTF-8"), json
		);

		Request request = new Request.Builder()
		.url("http://localhost:8080/FireAlarmRest/rest/AlarmService/addAlarm")
		.post(requestBody)
		.build();

		try (Response response = httpClient.newCall(request).execute()) {
			int code = response.code();

			if(code == 201)
				return true;
			else
				return false;
		}
	}

	@Override
	public boolean updateAlarm(String aid, int smoke, int co2, String lid, int workingState, String handler, int activeState) throws IOException, RemoteException {
		String json = new StringBuilder()
		.append("{"
			+ "\"aid\":\""+aid+"\","
			+ "\"email\":\""+handler+"\","
			+ "\"lid\":\""+lid+"\","
			+ "\"smokeLevel\":\""+smoke+"\","
			+ "\"co2Level\":\""+co2+"\","
			+ "\"isActive\":\""+activeState+"\","
			+ "\"isWorking\":\""+workingState+"\""
			+ "}").toString();

		RequestBody requestBody = RequestBody.create (
			MediaType.parse("application/json; charset=UTF-8"), json
		);

		Request request = new Request.Builder()
		.url("http://localhost:8080/FireAlarmRest/rest/AlarmService/updateAlarm")
		.put(requestBody)
		.build();

		try (Response response = httpClient.newCall(request).execute()) {
			int code = response.code();

			if(code == 201)
				return true;
			else
				return false;
		}
	}

	public void run() {
		for(;;) {
			try {
				Thread.sleep(15000);
			}
			catch (InterruptedException ie) {
				System.out.println(ie);
			}

			try {
				getAlarms();
			} catch (IOException | JSONException e1) {
				e1.printStackTrace();
			}

			try {
				notifyOthers();
			} catch (IOException | JSONException e2) {
				e2.printStackTrace();
			}
		}
	}

	public void notifyOthers() throws IOException, JSONException {
		for(FAListener listener: listeners) {
			try {
				listener.alarmsChanged(alarms.toString());
			}
			catch(RemoteException re) {
				System.out.println(re);
			}
		}
	}

}

