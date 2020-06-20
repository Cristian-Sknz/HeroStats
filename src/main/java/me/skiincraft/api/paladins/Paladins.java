package me.skiincraft.api.paladins;

import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Logger;

import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.api.paladins.entity.Session;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;

public class Paladins {
	
	public static final String CHAMPIONS_PATH = "assets/champions/";
    private String PATH = "http://api.paladins.com/paladinsapi.svc";
    private int DEVID;
    private String AUTHKEY;

	private Logger simplelog;
    private SimpleDateFormat StampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private String sessionId;
    
    public String getPATH() {
		return PATH;
	}

	public int getDEVID() {
		return DEVID;
	}

	public String getAUTHKEY() {
		return AUTHKEY;
	}

	public String getSessionId() {
		return sessionId;
	}

    public Paladins(int devid, String token) {
    	this.DEVID = devid;
    	this.AUTHKEY = token;
    	
    	if (sessionId == "" || sessionId == null) {
    		try {
    			if (simplelog == null) {
    				simplelog = Logger.getLogger("[Paladins-API]");
    			}
    			simplelog.config("Criando nova session");
    			
				Session session = createSession();
				sessionId = session.getSession();
				
				simplelog.info("Uma nova sessão foi criada: " + sessionId);
			} catch (HttpRequestException | MalformedURLException e) {
				e.printStackTrace();
			}
    	}
    }
    
    public Paladins(int devid, String token, String sessionid) {
    	DEVID = devid;
    	AUTHKEY = token;
    	sessionId = sessionid;
    	
		if (simplelog == null) {
			simplelog = Logger.getLogger("[Paladins-API]");
		}
    	simplelog.config("Tentando assumir a ultima sessão.");
    	try {
			testSession(sessionid);
		} catch (HttpRequestException | MalformedURLException e) {
			e.printStackTrace();
		}
    }
    
    public synchronized Session createSession() throws HttpRequestException, MalformedURLException {
    	String metodo = "createsession";
    	String formato = "Json";
    	
    	String requestUrl = PATH +"/"+ complete(metodo + formato, DEVID+"", getSignature(metodo), getTimeStamp());
    	
    	HttpRequest request = HttpRequest.get(new URL(requestUrl));
    	String body = request.body();
    	
    	System.out.println(body);
    	
    	JsonObject jo = new JsonParser().parse(body).getAsJsonObject();
	    String sessionId = jo.get("session_id").getAsString();
	    
	    
	    this.sessionId = sessionId;
    	return new Session() {
			
			@Override
			public String requestMessage() {
				return body;
			}
			
			@Override
			public String getSession() {
				return sessionId;
			}
		};
    }
    
    public synchronized Session testSession(String sessionId) throws HttpRequestException, MalformedURLException {
    	String metodo = "testsession";
    	String formato = "Json";
    	
    	String requestUrl = PATH +"/"+ complete(metodo + formato, DEVID+"", getSignature(metodo), sessionId, getTimeStamp());
    	
    	HttpRequest request = HttpRequest.get(new URL(requestUrl));
    	String body = request.body();
    	System.out.println(requestUrl);
    	System.out.println(body);
    	
    	if (body.toLowerCase().contains("Invalid session id.".toLowerCase())) {
    		simplelog.config("A sessão inserida esta invalida.");
    		return null;
    	}
    	
	    //testsession[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}
	    

	    this.sessionId = sessionId;
    	return new Session() {
			
			@Override
			public String requestMessage() {
				return body;
			}
			
			@Override
			public String getSession() {
				return sessionId;
			}
		};
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
    
    public String complete(String...strings) {
    	StringBuffer buffer = new StringBuffer();
    	int lenght = strings.length;
    	for (String s : strings) {
    		if (s != strings[lenght-1]) {
    			buffer.append(s.replace(" ", "_") + (s.contains("/") ? "" : "/"));    			
    		} else {
    			buffer.append(s.replace(" ", "_"));
    		}
    	}
    	
    	return buffer.toString();
    }
    
    public Queue getQueue() {
    	return new Queue(this);
    }
}