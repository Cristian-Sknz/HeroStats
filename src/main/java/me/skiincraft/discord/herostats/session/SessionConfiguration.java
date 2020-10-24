package me.skiincraft.discord.herostats.session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.common.Session;
import me.skiincraft.api.paladins.exceptions.RequestException;
import me.skiincraft.discord.herostats.HeroStatsBot;

public class SessionConfiguration {

	private final Paladins paladins;
	private final String SESSIONS_PATH = "PaladinsApi/Sessions/";

	public SessionConfiguration(Paladins paladins){
		this.paladins = paladins;
		new File(SESSIONS_PATH).mkdirs();
	}

	public void resumeSessions() throws IOException {
		List<File> sessions = new ArrayList<>();
		for (Path path : Files.newDirectoryStream(Paths.get(SESSIONS_PATH))){
			File file = path.toFile();
			System.out.println(file.getName());
			if (file.getName().endsWith(".paladins")){
				sessions.add(file);
			}
		}

		if (sessions.size() == 0){
			createSession();
			return;
		}

		List<File> toRemove = new ArrayList<>();

		for (File file : sessions){
			String id = file.getName().replace(".paladins", "");
			try {
				paladins.testSession(id).get();
			} catch (RequestException e) {
				file.delete();
				toRemove.add(file);
			}
		}

		sessions.removeAll(toRemove);

		if (sessions.size() == 0){
			createSession();
			return;
		}
		for (File file: sessions) {
			Session session = paladins.resumeSession(file.getName().replace(".paladins", "")).get();
			session.setOnValidating(null);
		}
	}

	public void createSession() {
		Session session = paladins.createSession().get();
		File file = new File(SESSIONS_PATH + session.getSessionId() + ".paladins");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
