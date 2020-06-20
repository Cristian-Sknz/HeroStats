package me.skiincraft.discord.herostats;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;

import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.Queue;
import me.skiincraft.api.paladins.common.Champion;

public class HeroStatsBot {
	
	public static int i = 1;
	
	public static void main(String[] args) throws Exception {
		//, "787F29C26DB64C1EA252576855F028D4");
		//paladins.getQueue().refreshChampions();
		Timer timer = new Timer();
		Paladins paladins = new Paladins(0, "");
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("Rodando request agora.[" + i + "]");
				try {
					Queue queue = paladins.getQueue();
					
					List<Champion> champs = queue.refreshChampions();
					
					System.out.println(champs.size());
					System.out.println(champs.get(5).getEnglishTitle());
					
				} catch (HttpRequestException e) {
					e.printStackTrace();
				}
				i++;
			}
		}, 200, TimeUnit.MINUTES.toMillis(5));
		
	}

}
