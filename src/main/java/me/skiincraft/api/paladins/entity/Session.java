package me.skiincraft.api.paladins.entity;

import java.util.Timer;
import java.util.TimerTask;

import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.Queue;

public class Session {
	
	public int timerId;
	
	private String sessionId;
	private String signature;
	private String timestamp;
	private String requestMsg;
	private Queue requester;
	private Paladins paladins;
	private Timer timer;
	
	public Session(String sessionId, String signature, String timestamp, String requestMsg,
			Paladins paladins) {
		this.sessionId = sessionId;
		this.signature = signature;
		this.timestamp = timestamp;
		this.requestMsg = requestMsg;
		this.paladins = paladins;
		this.requester = new Queue(this);
		autoReconnect();
	}
	
	private void autoReconnect() {
		timerId = requester.getPaladins().getSessionsCache().size();
		System.out.println("[Session-Worker-"+ timerId + "] Rodando...");
		timer = new Timer("Session-Worker-"+ timerId);
		
		Session thissession = this;
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("[REQUEST] Running session validation.");
				if (!getPaladins().hasValidSession(thissession)) {
					System.out.println("Sessão está invalida, recriando outra sessão.");
					
					getPaladins().createSession();
					getPaladins().getSessionsCache().remove(thissession);
					cancel();
				} else {
					System.out.println("[Session-Worker-"+ timerId +"]"+ "Sessão-" + sessionId + " ainda é valida.");
				}
			}
		}, 8*(1000*60), 8*(1000*60));
	}
	
	public String getSessionId() {
		return sessionId;
	}
	public String getRequestMsg() {
		return requestMsg;
	}
	
	public boolean hasValidSession() {
		return getPaladins().hasValidSession(this);
	}
	
	public Paladins getPaladins() {
		return paladins;
	}
	public String getSignature() {
		return signature;
	}
	public String getTimestamp() {
		return timestamp;
	}

	public Queue getRequester() {
		return requester;
	}
	
}
