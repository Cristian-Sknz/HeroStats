package me.skiincraft.api.paladins;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.api.paladins.entity.Session;
import me.skiincraft.api.paladins.hirez.HirezStatus;

import java.security.MessageDigest;

public class Paladins {

	private String PATH = "http://api.paladins.com/paladinsapi.svc";
	private int DEVID;
	private String AUTHKEY;

	private Logger simplelog = Logger.getLogger("[Paladins-API]");
	private SimpleDateFormat StampFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private List<Session> sessionsCache = new ArrayList<Session>();

	public String getPATH() {
		return PATH;
	}

	public int getDEVID() {
		return DEVID;
	}

	public String getAUTHKEY() {
		return AUTHKEY;
	}

	public Paladins(int devid, String token) {
		this.DEVID = devid;
		this.AUTHKEY = token;

		if (sessionsCache.size() == 0) {
			try {
				simplelog.config("Criando nova session");
				
				Session session = createSession();
				
				simplelog.info("Uma nova sessão foi criada: " + session.getSessionId());
				System.out.println(session.getSessionId());
			} catch (HttpRequestException e) {
				e.printStackTrace();
			}
		}
	}

	public Paladins(int devid, String token, String sessionid) {
		DEVID = devid;
		AUTHKEY = token;

		System.out.println("Tentando assumir a ultima sessão.");
		boolean exists = false;

		for (Session sessions : sessionsCache) {
			if (sessions.getSessionId() == sessionid) {
				if (!hasValidSession(sessionid)) {
					sessionsCache.remove(sessions);
					System.out.println("Sessão reassumida esta invalida, criando nova sessão.");
					exists = false;
				} else {
					exists = true;
				}
				break;
			}
		}
		
		if (!exists) {
			if (!hasValidSession(sessionid)) {
				System.out.println("Sessão reassumida esta invalida, criando nova sessão.");
				createSession();
			} else {
				sessionsCache.add(new Session(sessionid, null, null, "", this));
				System.out.println("A sessão foi assumida com sucesso!");
			}
		}
	}

	public synchronized Session createSession() {
		String method = "createsession";
		String responseFormat = "Json";
		String developerId = String.valueOf(getDEVID());
		String signature = getSignature(method);
		String timeStamp = getTimeStamp();
		String url = getPATH() + "/" + complete(method + responseFormat, developerId, signature, timeStamp);

		HttpRequest request = HttpRequest.get(url);

		String body = request.body();

		System.out.println(body);

		JsonObject jo = new JsonParser().parse(body).getAsJsonObject();
		String sessionId = jo.get("session_id").getAsString();

		Session session = new Session(sessionId, signature, timeStamp, body, this);
		sessionsCache.add(session);
		return session;
	}
	
	public boolean hasValidSession(String sessionId) {
		// testsession[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}
		String method = "testsession";
		String responseFormat = "Json";
		String developerId = String.valueOf(getDEVID());
		String signature = getSignature(method);
		String timeStamp = getTimeStamp();

		String url = getPATH() + "/" + complete(method + responseFormat, developerId, signature, sessionId, timeStamp);

		HttpRequest request;
		request = HttpRequest.get(url);

		String body = request.body();

		if (body.contains("Invalid session id")) {
			System.out.println("A sessão inserida não é valida.");
			return false;
		}

		return true;
	}
	
	public boolean hasValidSession(Session session) {
		return hasValidSession(session.getSessionId());
	}

	public synchronized Map<String, String> getDataUsed(String sessionId) {
		// getdataused[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}
		String method = "getdataused";
		String responseFormat = "Json";
		String developerId = String.valueOf(getDEVID());
		String signature = getSignature(method);
		String timeStamp = getTimeStamp();

		String url = getPATH() + "/" + complete(method + responseFormat, developerId, signature, sessionId, timeStamp);

		HttpRequest requester = HttpRequest.get(url);
		JsonElement json = new JsonParser().parse(requester.body());
		JsonObject object = json.getAsJsonArray().get(0).getAsJsonObject();
		Map<String, String> map = new HashMap<>();

		map.put("Active_Sessions", object.get("Active_Sessions").getAsString());
		map.put("Concurrent_Sessions", object.get("Concurrent_Sessions").getAsString());
		map.put("Request_Limit_Daily", object.get("Request_Limit_Daily").getAsString());
		map.put("Session_Cap", object.get("Session_Cap").getAsString());
		map.put("Session_Time_Limit", object.get("Session_Time_Limit").getAsString());
		map.put("Total_Requests_Today", object.get("Total_Requests_Today").getAsString());
		map.put("Total_Sessions_Today", object.get("Total_Sessions_Today").getAsString());
		map.put("ret_msg", object.get("ret_msg").toString());

		return map;
	}

	public synchronized Map<String, String> getDataUsed(Session session) {
		return getDataUsed(session.getSessionId());
	}

	public synchronized List<HirezStatus> getHirezServerStatus(String sessionId) {
		// gethirezserverstatus[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}
		String method = "gethirezserverstatus";
		String responseFormat = "Json";
		String developerId = String.valueOf(getDEVID());
		String signature = getSignature(method);
		String timeStamp = getTimeStamp();

		String url = getPATH() + "/" + complete(method + responseFormat, developerId, signature, timeStamp);

		HttpRequest requester = HttpRequest.get(url);
		JsonElement json = new JsonParser().parse(requester.body());
		JsonArray array = json.getAsJsonArray();
		List<HirezStatus> list = new ArrayList<HirezStatus>();

		for (JsonElement ele : array) {
			JsonObject object = ele.getAsJsonObject();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
			try {
				date = df.parse(object.get("entry_datetime").getAsString());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			list.add(new HirezStatus(date, object.get("environment").getAsString(),
					object.get("limited_access").getAsBoolean(), object.get("platform").getAsString(),
					object.get("status").getAsString(), object.get("version").getAsString(),
					object.get("ret_msg").getAsString()));
		}

		return list;
	}

	public String getTimeStamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp.getTime());
		calendar.add(Calendar.HOUR, 3);
		timestamp = new Timestamp(calendar.getTime().getTime());
		return StampFormat.format(timestamp);
	}

	public String getSignature(String metodo) {
		try {
			String signature = DEVID + metodo + AUTHKEY + getTimeStamp();
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(signature.getBytes());
			byte[] bytedigest = digest.digest();

			StringBuffer buffer = new StringBuffer();
			for (byte b : bytedigest) {
				buffer.append(String.format("%02x", b & 0xff));
			}

			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String complete(String... strings) {
		StringBuffer buffer = new StringBuffer();
		int lenght = strings.length;
		for (String s : strings) {
			if (s != strings[lenght - 1]) {
				buffer.append(s.replace(" ", "_") + (s.contains("/") ? "" : "/"));
			} else {
				buffer.append(s.replace(" ", "_"));
			}
		}

		return buffer.toString();
	}

	public List<Session> getSessionsCache() {
		return sessionsCache;
	}
}