package me.skiincraft.api.paladins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import me.skiincraft.api.paladins.builder.PaladinsPlayerBuilder;
import me.skiincraft.api.paladins.common.Champion;
import me.skiincraft.api.paladins.entity.PaladinsPlayer;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.objects.Card;
import me.skiincraft.api.paladins.parser.JsonChampionCards;
import me.skiincraft.api.paladins.parser.JsonChampions;

public class Queue {
	
	private Paladins paladinsapi;
	private List<Champion> loadedchampions = new ArrayList<Champion>();

	public Paladins getPaladins() {
		return paladinsapi;
	}

	public Queue(Paladins paladinsapi) {
		this.paladinsapi = paladinsapi;
	}
	
	private synchronized String makeSimpleUrl(String method, Language language) {
		method = method.toLowerCase();
		String format = "Json";
		System.out.println(paladinsapi.getSessionId());
		String url = getPaladins().getPATH()+ "/" + method+format+
				"/"+ paladinsapi.complete(paladinsapi.getDEVID()+"",
						paladinsapi.getSignature(method),
						paladinsapi.getSessionId(),
						paladinsapi.getTimeStamp(), language.getLanguagecode()+"");
		
		return url;
	}
	
	public List<Champion> refreshChampions() {
		try {
			File filePT = new File(Paladins.CHAMPIONS_PATH + "championsrawPt.json");
			File fileEN = new File(Paladins.CHAMPIONS_PATH + "championsrawEn.json");
			String bodyPT = null;
			String bodyEN = null;
			if (!filePT.exists() && !fileEN.exists()) {
			HttpRequest request = HttpRequest.get(new URL(makeSimpleUrl("getchampions", Language.Portuguese)));
			HttpRequest request2 = HttpRequest.get(new URL(makeSimpleUrl("getchampions", Language.English)));
			
			JsonElement json1 = new JsonParser().parse(bodyPT);
			JsonElement json2 = new JsonParser().parse(bodyEN);
			
			bodyPT = request.body();
			bodyEN = request2.body();
			
			try {
				filePT.createNewFile();
				fileEN.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			writeGson(filePT, json1);
			writeGson(fileEN, json2);
			}
			List<Champion> a = new JsonChampions(bodyPT, bodyEN, this).refreshchampions();
			loadedchampions = a;
			return a;

		} catch (HttpRequestException | MalformedURLException e) {
			e.printStackTrace();
		}
		return loadedchampions;
	}
	
	public List<Card> getChampionsCards(int championId, Language lang) {
		//getchampioncards[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{championId}/{languageCode}
		String method = "getchampioncards";
		String responseformat = "Json";
		String request = method + responseformat + "/";

		String url;
		int language = (lang == null) ? Language.Portuguese.getLanguagecode() : lang.getLanguagecode();

		url = getPaladins().getPATH() + "/" + request
				+ paladinsapi.complete(paladinsapi.getDEVID() + "", paladinsapi.getSignature(method),
						paladinsapi.getSessionId(), paladinsapi.getTimeStamp(), championId + "",
						"" + language);

		HttpRequest requester;
		try {
			requester = HttpRequest.get(new URL(url));
			String body = requester.body();
			System.out.println(body);
			return new JsonChampionCards(body).cardJsonParse();
		} catch (HttpRequestException | MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void getChampionsSkins(int championId, Language lang) {
		//getchampionskins[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{godId}/{languageCode}
		String method = "getchampionskins";
		String responseformat = "Json";
		String request = method + responseformat + "/";

		String url;
		
		int language = (lang == null) ? Language.Portuguese.getLanguagecode() : lang.getLanguagecode();
		
		url = getPaladins().getPATH() + "/" + request
				+ paladinsapi.complete(paladinsapi.getDEVID() + "", paladinsapi.getSignature(method),
						paladinsapi.getSessionId(), paladinsapi.getTimeStamp(), championId + "",
						"" + language);

		HttpRequest requester;
		try {
			requester = HttpRequest.get(new URL(url));
			String body = requester.body();
			System.out.println(body);
		} catch (HttpRequestException | MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public PaladinsPlayer getPlayer(String playername, Platform platform) {
		//getplayer[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{player}/{portalId}
		//Returns league and other high level data for a particular player.
		
		String method = "getplayer";
		String responseformat = "Json";
		String request = method + responseformat + "/";
		
		String url;
		try {
		url = getPaladins().getPATH() + "/" + request +
				paladinsapi.complete(
						paladinsapi.getDEVID()+"",
						paladinsapi.getSignature(method),
						paladinsapi.getSessionId(),
						paladinsapi.getTimeStamp(), playername);
		HttpRequest requester;
			requester = HttpRequest.get(new URL(url));

		String body = requester.body();
		
		System.out.println(body);
		
		return new PaladinsPlayerBuilder(body);
		} catch (HttpRequestException | MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeGson(File file, Object data) {
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(new GsonBuilder().setPrettyPrinting().create().toJson(data));
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	public Paladins getPaladinsAPI() {
		return paladinsapi;
	}

	public List<Champion> getLoadedchampions() {
		return loadedchampions;
	}
}
