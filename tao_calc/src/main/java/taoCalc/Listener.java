/*
 * Copyright 2016 John Grosh <john.a.grosh@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package taoCalc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import taoCalc.db.Sqlite;
import taoCalc.dto.Member;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class Listener extends ListenerAdapter {
	private final Bot bot;

	public Listener(Bot bot) {
		this.bot = bot;
	}

	/**
	 * Botが起動したときのイベント
	 */
	@Override
	public void onReady(ReadyEvent event) {
		bot.getThreadpool().scheduleWithFixedDelay(() -> {
			for (Guild guild : event.getJDA().getGuilds()) {
				ArrayList<HashMap<String, String>> serverInfo = Sqlite.getMapExecuteSql(guild.getId(),
						"select * from server_info");
				if (!serverInfo.isEmpty() && !serverInfo.get(0).get("exp_info_channel").isEmpty()) {
					String expInfoChannel = serverInfo.get(0).get("exp_info_channel");

					System.out.println("ギルドID：" + guild.getId());
					System.out.println("ギルド名：" + guild.getName());
					EmbedBuilder eb = new EmbedBuilder();
					eb.setTitle("保有経験値");
					List<Member> memberList = Sqlite.selectMemberOrderByExpDesc(guild.getId());

					int count = 1;
					for (Member member : memberList) {
						if (count % 20 == 0) {
							guild.getTextChannelById(expInfoChannel).sendMessage(eb.build()).queue();
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							eb.clear();
							eb.setTitle("保有経験値");
						}
						eb.appendDescription(
								String.format("> %s ： <@%s> | 最終更新 ： %s \n", member.getFormatExp(), member.getId(),
										member.getUpdateDate()));
						count++;
					}
					guild.getTextChannelById(expInfoChannel).sendMessage(eb.build()).queue();
				}
			}
		}, 0, 60, TimeUnit.MINUTES);
		
		bot.getThreadpool().scheduleWithFixedDelay(() -> {
			for (Guild guild : event.getJDA().getGuilds()) {
				if(guild.getId().equals("823574484660518932")) {
					TextChannel channnel = guild.getTextChannelById("833683675345977416");
					ChannelManager channelManager = ChannelManager.getINSTANCE();
					Calendar calendar = Calendar.getInstance();
					Date date = channelManager.getDate("833683675345977416");
					if(date == null) {
						return;
					}
					calendar.setTime(date);
					calendar.add(Calendar.MINUTE, 3);
					
					Date now = new Date();
					if(now.after(calendar.getTime())) {
						channnel.sendMessage("<@&850863982465384459> 回せ！").queue();
					}
				}
			}
		}, 0, 1, TimeUnit.MINUTES);
	}

	@Override
	public void onShutdown(ShutdownEvent event) {
		bot.shutdown();
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		Guild guild = event.getGuild();
		PlayerManager Playermanager = PlayerManager.getINSTANCE();

		if (Playermanager.isJoin(guild.getId())) {
			if (Playermanager.getUserId(guild.getId()).equals(event.getMember().getId())) {
				AudioManager manager = guild.getAudioManager();
				manager.closeAudioConnection();
				Playermanager.setJoin(guild.getId(), "");
			}
		}
	}
}
