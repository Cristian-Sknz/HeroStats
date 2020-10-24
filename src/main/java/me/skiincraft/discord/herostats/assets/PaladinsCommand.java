package me.skiincraft.discord.herostats.assets;

import me.skiincraft.api.paladins.common.EndPoint;
import me.skiincraft.api.paladins.common.Request;
import me.skiincraft.api.paladins.common.Session;
import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.champions.Champions;
import me.skiincraft.api.paladins.entity.leaderboard.LeaderBoard;
import me.skiincraft.api.paladins.entity.match.LiveMatch;
import me.skiincraft.api.paladins.entity.player.Player;
import me.skiincraft.api.paladins.entity.player.PlayerChampion;
import me.skiincraft.api.paladins.entity.player.objects.PlayerChampions;
import me.skiincraft.api.paladins.entity.player.objects.SearchResults;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.enums.PlayerStatus;
import me.skiincraft.api.paladins.enums.Tier;
import me.skiincraft.api.paladins.objects.Place;
import me.skiincraft.api.paladins.objects.SearchPlayer;
import me.skiincraft.discord.core.command.Command;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.exceptions.UnavailableAPIException;
import me.skiincraft.discord.herostats.utils.HeroUtils;

import java.lang.reflect.Field;
import java.util.List;

public abstract class PaladinsCommand extends Command {

    public static final int SEASON = 3;

    public PaladinsCommand(String name, List<String> aliases, String usage) {
        super(name, aliases, usage);
    }


    public EndPoint endpoint(){
        List<Session> sessions = HeroStatsBot.getPaladins().getSessions();
        if (sessions.size() == 0){
            throw new UnavailableAPIException("API indisponivel no momento.");
        }

        return sessions.get(0).getEndPoint();
    }

    protected List<SearchPlayer> searchPlayers(String name, Platform platform){
        List<Session> sessions = HeroStatsBot.getPaladins().getSessions();
        if (sessions.size() == 0){
            throw new UnavailableAPIException("API indisponivel no momento.");
        }

        EndPoint endpoint = sessions.get(0).getEndPoint();
        Request<SearchResults> request = endpoint.searchPlayer(name, platform);
        return request.get().getAsList();
    }

    protected SearchPlayer searchPlayer(String name, Platform platform){
        return searchPlayers(name, platform).get(0);
    }

    protected SearchPlayer searchPlayer(String name){
        return searchPlayers(name, null).get(0);
    }

    protected List<SearchPlayer> searchPlayers(String name){
        return searchPlayers(name, null);
    }

    public Player player(String name){
        List<Session> sessions = HeroStatsBot.getPaladins().getSessions();
        if (sessions.size() == 0){
            throw new UnavailableAPIException("API indisponivel no momento.");
        }

        EndPoint endpoint = sessions.get(0).getEndPoint();
        return endpoint.getPlayer(name).get();
    }

    public Player player(long name){
        List<Session> sessions = HeroStatsBot.getPaladins().getSessions();
        if (sessions.size() == 0){
            throw new UnavailableAPIException("API indisponivel no momento.");
        }

        EndPoint endpoint = sessions.get(0).getEndPoint();
        Request<Player> request = endpoint.getPlayer(name);

        return request.get();
    }

    public PlayerChampions playerChampions(long id){
        List<Session> sessions = HeroStatsBot.getPaladins().getSessions();
        if (sessions.size() == 0){
            throw new UnavailableAPIException("API indisponivel no momento.");
        }

        EndPoint endpoint = sessions.get(0).getEndPoint();
        return endpoint.getPlayerChampions(id).get();
    }

    public Champions champions(Language language) {
        List<Session> sessions = HeroStatsBot.getPaladins().getSessions();
        if (sessions.size() == 0){
            throw new UnavailableAPIException("API indisponivel no momento.");
        }

        EndPoint endpoint = sessions.get(0).getEndPoint();
        return endpoint.getChampions(language).get();
    }

    public Champion champion(long championId, Language language){
        return champions(language).getById(championId);
    }

    public PlayerChampion playerChampion(long playerId, long championId){
        return playerChampions(playerId).getById(championId);
    }

    public LeaderBoard leaderBoard(Tier tier){
        List<Session> sessions = HeroStatsBot.getPaladins().getSessions();
        if (sessions.size() == 0){
            throw new UnavailableAPIException("API indisponivel no momento.");
        }

        EndPoint endpoint = sessions.get(0).getEndPoint();
        return endpoint.getLeaderboard(tier, SEASON).get();
    }

    public PlayerStatus playerStatus(String player) {
        List<Session> sessions = HeroStatsBot.getPaladins().getSessions();
        if (sessions.size() == 0){
            throw new UnavailableAPIException("API indisponivel no momento.");
        }

        EndPoint endpoint = sessions.get(0).getEndPoint();
        return endpoint.getPlayerStatus(player).get();
    }

    public PlayerStatus playerStatus(long playerId) {
        return playerStatus(String.valueOf(playerId));
    }

    public LiveMatch liveMatch(long matchId) {
        List<Session> sessions = HeroStatsBot.getPaladins().getSessions();
        if (sessions.size() == 0){
            throw new UnavailableAPIException("API indisponivel no momento.");
        }

        EndPoint endpoint = sessions.get(0).getEndPoint();
        return endpoint.getMatchPlayerDetails(matchId).get();
    }

    public LiveMatch liveMatch(PlayerStatus status){
        return this.liveMatch(status.getMatchId());
    }

    public EndPoint endpont(){
        List<Session> sessions = HeroStatsBot.getPaladins().getSessions();
        if (sessions.size() == 0){
            throw new UnavailableAPIException("API indisponivel no momento.");
        }

        return sessions.get(0).getEndPoint();
    }

    public Place leaderboardPlace(Player player){
        LeaderBoard board = leaderBoard(player.getTier());
        if (player.getTier() == Tier.Master){
            Place place = board.getById(player.getId());
            if (place != null){
                if (HeroUtils.isGrandMaster(board.getAsList(), player)) {
                    try {
                        Field field = place.getClass().getDeclaredField("tier");
                        field.setAccessible(true);
                        field.set(place, Tier.Grandmaster);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return board.getById(player.getId());
    }

}
