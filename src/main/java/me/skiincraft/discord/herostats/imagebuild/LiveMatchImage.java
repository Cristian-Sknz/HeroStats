package me.skiincraft.discord.herostats.imagebuild;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.match.LiveMatch;
import me.skiincraft.api.paladins.entity.match.LivePlayer;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.discord.core.textfont.CustomFont;
import me.skiincraft.discord.core.utils.ImageBuilder;
import me.skiincraft.discord.herostats.assets.PaladinsImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class LiveMatchImage {

    public static InputStream drawImagePartida(String mapname, LiveMatch match) throws IOException {
        List<LivePlayer> team1 = match.getTeamBlue();
        List<LivePlayer> team2 = match.getTeamRed();

        ImageBuilder principal = new ImageBuilder("partida", 863, 313);
        principal.getGraphic().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);



        //MAPA
        principal.drawImage(ImageIO.read(Objects.requireNonNull(PaladinsImage.getMap((mapname == null) ? "default_map" : mapname))), 863 / 2, 313 / 2,
                new Dimension(870, 490), ImageBuilder.Alignment.Center);

        //FADE
        principal.drawImage(ImageIO.read(Objects.requireNonNull(PaladinsImage.getAssetsImage("TeamFade"))), 863 / 2, 313 / 2, principal.getSize(),
                ImageBuilder.Alignment.Center);
		Font font = new CustomFont().getFont("gabriola", Font.BOLD, 22);
        addTime1(principal, team1, font);
		addTime2(principal, team2, font);

        return principal.buildInput();
    }

	private static void addTime2(ImageBuilder principal, List<LivePlayer> team2, Font font) throws IOException{
		int y = 62;
		for (LivePlayer p : team2) {
			ImageBuilder player = new ImageBuilder("champs2", 478, 56);
			Champion champion = p.getChampion(Language.Portuguese).get();
			player.getGraphic().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// TIER
			player.drawImage(ImageIO.read(Objects.requireNonNull(PaladinsImage.getTier(p.getTier().getTier()))), 472, 32, new Dimension(37, 37), ImageBuilder.Alignment.Right);
			// AVATAR
			player.drawImage(ImageIO.read(Objects.requireNonNull(PaladinsImage.getAvatar(champion))), 432, 28, new Dimension(41, 41), ImageBuilder.Alignment.Right);

			// NAME
			String playername = (p.getPlayerName().length() < 3) ? "[Perfil Privado]" : p.getPlayerName();
			player.addRightStringY(playername, 379, 21, font);

			// Cargo
			player.addRightStringY(champion.getRole().split(" ")[1], 361, 37, font);

			principal.drawImage(player.buildImage(), 863, y, player.getSize(), ImageBuilder.Alignment.Right);
			y += 50;
		}
	}

    private static void addTime1(ImageBuilder principal, List<LivePlayer> team1, Font font) throws IOException{
		int y = 62;
		for (LivePlayer p : team1) {
			ImageBuilder player = new ImageBuilder("champs", 478, 56);
			Champion champion = p.getChampion(Language.Portuguese).get();
			player.getGraphic().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// TIER
			player.drawImage(ImageIO.read(Objects.requireNonNull(PaladinsImage.getTier(p.getTier().getTier()))), 6, 32, new Dimension(37, 37), ImageBuilder.Alignment.Left);
			// AVATAR
			player.drawImage(ImageIO.read(Objects.requireNonNull(PaladinsImage.getAvatar(champion))), 46, 28, new Dimension(41, 41), ImageBuilder.Alignment.Left);

			// NAME
			String playername = (p.getPlayerName().length() < 3) ? "[Perfil Privado]" : p.getPlayerName();
			player.addCentralizedStringY(playername, 98, 21, font);

			// Cargo
			player.addCentralizedStringY(champion.getRole().split(" ")[1], 111, 37, font);

			principal.drawImage(player.buildImage(), 0, y, player.getSize(), ImageBuilder.Alignment.Left);
			y += 50;
		}
	}

}
