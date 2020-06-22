package me.skiincraft.api.paladins;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.api.paladins.common.ChampionRank;
import me.skiincraft.api.paladins.builder.PaladinsMatchBuilder;
import me.skiincraft.api.paladins.builder.PaladinsPlayerBuilder;
import me.skiincraft.api.paladins.common.Champion;
import me.skiincraft.api.paladins.common.ChampionLoadouts;
import me.skiincraft.api.paladins.common.ChampionSkin;
import me.skiincraft.api.paladins.entity.LiveMatch;
import me.skiincraft.api.paladins.entity.PaladinsFriend;
import me.skiincraft.api.paladins.entity.PaladinsMatch;
import me.skiincraft.api.paladins.entity.PaladinsPlayer;
import me.skiincraft.api.paladins.entity.Session;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.PaladinsQueue;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.enums.PlayerStatus;
import me.skiincraft.api.paladins.enums.PlayerStatus.Status;
import me.skiincraft.api.paladins.enums.Tier;
import me.skiincraft.api.paladins.exceptions.NoMatchQueueException;
import me.skiincraft.api.paladins.exceptions.PlayerNotFoundException;
import me.skiincraft.api.paladins.matches.LiveMatchChampion;
import me.skiincraft.api.paladins.matches.LivePlayer;
import me.skiincraft.api.paladins.objects.Card;
import me.skiincraft.api.paladins.objects.LeagueSeason;
import me.skiincraft.api.paladins.parser.JsonChampionCards;
import me.skiincraft.api.paladins.parser.JsonChampionRanks;
import me.skiincraft.api.paladins.parser.JsonChampionSkins;
import me.skiincraft.api.paladins.parser.JsonChampions;
import me.skiincraft.api.paladins.parser.JsonPaladinsFriends;
import me.skiincraft.api.paladins.parser.JsonPaladinsLoadouts;
public class Queue {
	
	private Paladins paladins;
	private Session session;
	private List<Champion> loadedchampions = new ArrayList<Champion>();
	
	public Queue(Session session) {
		this.paladins = session.getPaladins();
		this.session = session;
	}
	
	private String makeAUrl(String method, String[] args) {
		method = method.toLowerCase();
		String responseFormat = "Json";
		String devId = String.valueOf(paladins.getDEVID());
		String signature = paladins.getSignature(method);
		String sessionId = getSession().getSessionId();
		String timeStamp = paladins.getTimeStamp();
		
		String url = paladins.getPATH() + "/" + method+responseFormat + "/" +
				paladins.complete(devId, signature, sessionId,
						timeStamp) + ((args == null || args.length == 0) ? "" : "/");
		
		StringBuffer buffer = new StringBuffer();
		
		int i = args.length -1;
		if (args.length != 0) {
			for (String string : args) {
				buffer.append((string != args[i]) ? string + "/" : string);
			}
		}
		
		return url + buffer.toString();
	}
	
	public List<Champion> refreshChampions() {
			HttpRequest request = HttpRequest.get(makeAUrl("getchampions", new String[] {Language.Portuguese.getLanguagecode()+""}));
			HttpRequest request2 = HttpRequest.get(makeAUrl("getchampions", new String[] {Language.Portuguese.getLanguagecode()+""}));
			
			String bodyPT = request.body();
			String bodyEN = request2.body();
			
			List<Champion> champions = new JsonChampions(bodyPT, bodyEN, this).refreshchampions();
			loadedchampions = champions;
		return champions;
	}
	
	public List<Card> getChampionsCards(int championId, Language lang) {
		// getchampioncards[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{championId}/{languageCode}
		int language = (lang == null) ? Language.Portuguese.getLanguagecode() : lang.getLanguagecode();
		String url = makeAUrl("getchampioncards", new String[] { championId + "", language + "" });

		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();
		return new JsonChampionCards(body).cardJsonParse();
	}
	
	public List<ChampionSkin> getChampionsSkins(int championId, Language lang) {
		// getchampionskins[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{godId}/{languageCode}
		int language = (lang == null) ? Language.Portuguese.getLanguagecode() : lang.getLanguagecode();
		String url = makeAUrl("getchampionskins", new String[] { championId + "", language + "" });

		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();
		return new JsonChampionSkins(body, this).skinJsonParser();
	}
	
	public PaladinsPlayer getPlayer(String player, Platform platform) throws PlayerNotFoundException {
		// getplayer[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{player}/{portalId}
		String url = makeAUrl("getplayer", new String[] { player });

		url += (platform == null) ? "" : "/" + platform.getPortalId();

		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();

		if (body.equalsIgnoreCase("[]")) {
			throw new PlayerNotFoundException("O Jogador solicitado não existe ou esta com perfil privado.");
		}

		return new PaladinsPlayerBuilder(body);
	}
	
	public PaladinsPlayer getPlayerBySearch(String searchplayer, Platform platform) throws PlayerNotFoundException {
		// getplayer[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{player}/{portalId}
		List<SearchPlayer> searchlist = searchPlayer(searchplayer);
		int firstId = 0;
		for (SearchPlayer search : searchlist) {
			if (platform != null && platform.getPortalId() == search.getPortalId()) {
				firstId = search.getUserId();
				break;
			}
		}
		
		String url = makeAUrl("getplayer", new String[] { (firstId != 0) ?firstId+"":searchlist.get(0).getUserId()+""});

		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();

		if (searchlist.size() == 0 || body.equalsIgnoreCase("[]")) {
			throw new PlayerNotFoundException("O Jogador solicitado não existe ou esta com perfil privado.");
		}

		return new PaladinsPlayerBuilder(body);
	}
	
	public List<PaladinsPlayer> getPlayerBatch(Integer...playersids) throws PlayerNotFoundException{
		///getplayerbatch[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{playerId,playerId,playerId,...playerId}
		String method = "getplayerbatch";
		StringBuffer ids = new StringBuffer();
		
		int ultimovalor = playersids[playersids.length-1];
		for (Integer i : playersids) {
			if (i != ultimovalor) {
				ids.append(i + ",");				
			} else {
				ids.append(i);
			}
		}
		
		String url = makeAUrl(method, new String[] {ids.toString()});
		HttpRequest requester = HttpRequest.get(url);

		String body = requester.body();
		List<PaladinsPlayer> players = new ArrayList<PaladinsPlayer>();
		if (body.equalsIgnoreCase("[]")) {
			throw new PlayerNotFoundException("O Jogador solicitado não existe ou esta com perfil privado.");
		}
		
		for (JsonElement ele : new JsonParser().parse(body).getAsJsonArray()) {
			JsonObject object = ele.getAsJsonObject();
			players.add(new PaladinsPlayerBuilder(object.toString()));
		}
		
		return players;
	}
	
	public List<ChampionRank> getChampionRanks(int userId) {
		//getchampionranks[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{playerId}
		String url = makeAUrl("getchampionranks", new String[] {userId+""});
		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();
			
		return new JsonChampionRanks(body, this).ranksJsonParser();
	}
	
	public List<PaladinsFriend> getFriends(int userid) {
		//getfriends[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{playerId}
		String url = makeAUrl("getfriends", new String[] {userid+""});
		
		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();
		return new JsonPaladinsFriends(body, this).friendJsonParser();
	}
	
	public List<ChampionLoadouts> getChampionsLoadouts(int userId, Language lang){
		//getplayerloadouts[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/playerId}/{languageCode}
		int language = (lang == null) ? Language.Portuguese.getLanguagecode() : lang.getLanguagecode();
		String url = makeAUrl("getplayerloadouts", new String[] {userId+"", language+""});
		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();
		return new JsonPaladinsLoadouts(body, this).loadoutsJsonParser();		
	}
	
	public PaladinsMatch getMatchDetails(int matchId) {
		//getmatchdetails[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{match_id}
		String url = makeAUrl("getmatchdetails", new String[] {matchId+""});
		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();
		return new PaladinsMatchBuilder(body, getSession());
	}
	
	public List<PaladinsMatch> getMatchDetailsBatch(Integer...matchids) {
		//getmatchdetailsbatch[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{match_id,match_id,match_id,...match_id}
		StringBuffer ids = new StringBuffer();
		int ultimovalor = matchids[matchids.length-1];
		for (Integer i : matchids) {
			if (i != ultimovalor) {
				ids.append(i + ",");				
			} else {
				ids.append(i + "");
			}
		}
		String url = makeAUrl("getmatchdetailsbatch", new String[] {ids.toString()});
		
		List<PaladinsMatch> matchs = new ArrayList<PaladinsMatch>();
		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();
		JsonArray arrays = new JsonParser().parse(body).getAsJsonArray();
		
		List<JsonArray> jsons = new ArrayList<JsonArray>();
		for (int i = 0; i < 5; i++) {
			JsonArray a = new JsonArray();
			int num = 0;
			if (i == 0)
				num = 0;
			if (i == 1)
				num = 10;
			if (i == 2)
				num = 20;
			if (i == 3)
				num = 30;
			if (i == 4)
				num = 40;
			for (int o = num; o < num + 10; o++) {
				a.add(arrays.get(o).getAsJsonObject());
			}
			jsons.add(a);
		}
		for (JsonArray jsonarray:jsons) {
			matchs.add(new PaladinsMatchBuilder(jsonarray.toString(), getSession()));
		}
		return matchs;
	}
	
	public List<PaladinsMatch> getMatchHistory(int playerId) {
		//getmatchhistory[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{playerId}
		String url = makeAUrl("getmatchhistory", new String[] {playerId+""});		
		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();
		List<Integer> ids = new ArrayList<>();
		for (JsonElement ele : new JsonParser().parse(body).getAsJsonArray()) {
			JsonObject object = ele.getAsJsonObject();
			ids.add(object.get("Match").getAsInt());
			if (ids.size() == 5) {
				break;
			}
		}
		
		Integer[] id = new Integer[ids.size()];
		ids.toArray(id);
		
		return getMatchDetailsBatch(id);
	}
	
	public LeagueLeaderboard getLeagueLeaderboard(PaladinsQueue queue, Tier tier, int season) {
		//getleagueleaderboard[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{queue}/{tier}/{round}
		String url = makeAUrl("getLeagueLeaderboard", new String[] {queue.getQueueId()+"", tier.getRankId()+"", season+""});
		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();
		List<LeaderboardPlaces> places = new ArrayList<LeaderboardPlaces>();
		JsonArray array = new JsonParser().parse(body).getAsJsonArray();
		for (JsonElement ele : array) {
			JsonObject object = ele.getAsJsonObject();
			places.add(new LeaderboardPlaces(object.get("Name").getAsString(),
					object.get("Wins").getAsInt(),
					object.get("Losses").getAsInt(),
					object.get("Leaves").getAsInt(),
					object.get("Points").getAsInt(),
					object.get("Season").getAsInt(),
					tier,
					object.get("player_id").getAsInt(),
					object.get("Trend").getAsInt()));
		}
		
		return new LeagueLeaderboard(places, tier, queue);
	}

	public List<SearchPlayer> searchPlayer(String player, Platform platform) {
		//searchplayers[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{searchPlayer}
		String url = makeAUrl("searchplayers", new String[] {player});
		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();
		JsonArray array = new JsonParser().parse(body).getAsJsonArray();
		List<SearchPlayer> search = new ArrayList<>();
		for (JsonElement ele : array) {
			JsonObject object = ele.getAsJsonObject();
			search.add(new SearchPlayer(object.get("Name").getAsString(),
					object.get("player_id").getAsInt(),
					object.get("hz_player_name").toString(),
					object.get("portal_id").getAsInt(),
					(object.get("privacy_flag").getAsString().equalsIgnoreCase("y")) ?true:false));
		}
		if (platform != null) {
		search.forEach(s ->{
			if (s.getPortalId() == platform.getPortalId()) {
				search.remove(s);
			}});
		}
		return search;
	}
	
	public List<SearchPlayer> searchPlayer(String player) {
		return searchPlayer(player, null);
	}
	
	//getplayerstatusJson/developerId/signature/sessionId/timestamp/player
	public PlayerStatus getPlayerStatus(String player) {
		String url = makeAUrl("getplayerstatus", new String[] {player});
		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();
		JsonObject object = new JsonParser().parse(body)
				.getAsJsonArray().get(0).getAsJsonObject();
		
		System.out.println(object);
		return new PlayerStatus(object.get("Match").getAsInt(),
				Status.getStatusById(object.get("status").getAsInt()),
				PaladinsQueue.getQueueById(object.get("match_queue_id").getAsInt()));	
	}
	
	public PlayerStatus getPlayerStatusBySearch(String player, Platform platform) {
		String url = makeAUrl("getplayerstatus", new String[] {""+searchPlayer(player, platform).get(0).getUserId()});
		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();
		JsonObject object = new JsonParser().parse(body)
				.getAsJsonArray().get(0).getAsJsonObject();
		
		System.out.println(object);
		return new PlayerStatus(object.get("Match").getAsInt(),
				Status.getStatusById(object.get("status").getAsInt()),
				PaladinsQueue.getQueueById(object.get("match_queue_id").getAsInt()));	
	}
	
	public PlayerStatus getPlayerStatusBySearch(String player) {
		return getPlayerStatusBySearch(player, null);
	}
	
	//getmatchplayerdetails[ResponseFormat]/{developerId}/{signature}/{session}/{timestamp}/{match_id}
	
	public LiveMatch getMatchPlayerDetails(int matchId) throws NoMatchQueueException {
		String url = makeAUrl("getmatchplayerdetails", new String[] {""+matchId});
		HttpRequest requester = HttpRequest.get(url);
		String body = requester.body();
		JsonArray array = new JsonParser().parse(body).getAsJsonArray();
		
		JsonObject inicialobj = array.get(0).getAsJsonObject();
		JsonElement ret_msg = inicialobj.get("ret_msg");
		if (ret_msg.toString() != "\"null\"") {
			if (ret_msg.getAsString().contains("No match_queue returned.")) {
					throw new NoMatchQueueException(ret_msg.getAsString());
			}
		}
		
		List<LivePlayer> players = new ArrayList<LivePlayer>();
		for (JsonElement ele : array) {
			JsonObject object = ele.getAsJsonObject();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aa");
			LiveMatchChampion champ = null;
			for (Champion champion : getLoadedchampions()) {
				if (champion.getChampionName().equalsIgnoreCase(object.get("ChampionName").getAsString())) {
					champ = new LiveMatchChampion(champion,
							object.get("ChampionId").getAsInt(),
							object.get("Skin").getAsString(),
							object.get("SkinId").getAsInt(),
							session);
					break;
				}
			}
			
			try {
				players.add(new LivePlayer(champ, object.get("Account_Champions_Played").getAsInt(),
						object.get("Account_Level").getAsInt(),
						(object.get("playerCreated").getAsString() != null) 
						? df.parse(object.get("playerCreated").getAsString()) : null,
						object.get("playerId").getAsInt(),
						object.get("playerName").getAsString(),
						object.get("playerRegion").getAsString(),
						new LeagueSeason(object.get("tierWins").getAsInt(),
								object.get("tierLosses").getAsInt(),
								0,
								Tier.getTierById(object.get("Tier").getAsInt()))));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return new LiveMatch(players, matchId, PaladinsQueue.getQueueById(inicialobj.get("Queue").getAsInt()),
				inicialobj.get("mapGame").getAsString());

	}
	
	public List<Champion> getLoadedchampions() {
		if (loadedchampions == null || loadedchampions.size() == 0) {
			loadedchampions = refreshChampions();
		}
		return loadedchampions;
	}

	public Paladins getPaladins() {
		return paladins;
	}
	
	public Session getSession() {
		return session;
	}
}
