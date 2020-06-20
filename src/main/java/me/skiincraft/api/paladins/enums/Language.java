package me.skiincraft.api.paladins.enums;

public enum Language {
	English(1), German(2), French(3), Chinese(5), Spanish(7), Portuguese(10), Russian(11), Polish(12), Turkish(13);
	
	private int languagecode;

	Language(int languagecode) {
		this.setLanguagecode(languagecode);
	}

	public int getLanguagecode() {
		return languagecode;
	}

	private void setLanguagecode(int languagecode) {
		this.languagecode = languagecode;
	}
	
}
