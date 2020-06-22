package me.skiincraft.api.paladins.exceptions;

public class NoMatchQueueException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5758961246899999315L;
	private String message;
	
	
	public NoMatchQueueException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}
