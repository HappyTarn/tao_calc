package taoCalc;
/*
 * Copyright 2017 John Grosh (jagrosh).
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import taoCalc.commands.AddExpCommand;
import taoCalc.commands.CalcCommand;
import taoCalc.commands.ChangeExpCommand;
import taoCalc.commands.ChangeRateCommand;
import taoCalc.commands.DbCommand;
import taoCalc.commands.MtCommand;
import taoCalc.commands.MtoCommand;
import taoCalc.commands.PingCommand;
import taoCalc.commands.PlayCommand;
import taoCalc.commands.Private;
import taoCalc.commands.Rank;
import taoCalc.commands.SetExpInfoChannelCommand;
import taoCalc.commands.SetPrizeInfoChannelCommand;
import taoCalc.commands.SetRoleCommand;
import taoCalc.commands.ShowExpCommand;
import taoCalc.commands.ShowPetCommand;
import taoCalc.commands.ShowRateCommand;
import taoCalc.commands.ShutdownCommand;
import taoCalc.commands.StartCommand;
import taoCalc.commands.StopCommand;
import taoCalc.commands.TestCommand;
import taoCalc.commands.initCommand;
import taoCalc.event.MessageEvent;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class TaoCalc {
	private final EventWaiter eventWaiter = new EventWaiter();

	public static void main(String[] args) {
		// args[0] would be our Bot token
		try {
			new TaoCalc().start();
		} catch (LoginException | IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public void start() throws IOException, LoginException {
		// config.txt contains two lines
		List<String> list = Files.readAllLines(Paths.get("config.txt"));

		// the first is the bot token
		String token = list.get(0);

		// the second is the bot's owner's id
		String ownerId = list.get(1);

		Bot bot = new Bot(eventWaiter);

		// define a command client
		CommandClientBuilder client = new CommandClientBuilder();

		// The default is "Type !!help" (or whatver prefix you set)
		client.useDefaultGame();

		// sets the owner of the bot
		client.setOwnerId(ownerId);

		// sets emojis used throughout the bot on successes, warnings, and failures
		//        client.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");

		// sets the bot prefix
		client.setPrefix(list.get(2));

		// adds commands
		client.addCommands(
				// command to check bot latency
				new PingCommand(),

				// command to shut off the bot
				new ShutdownCommand(),

				new initCommand(),
				new ChangeRateCommand(),
				new ShowRateCommand(),
				new ShowExpCommand(),
				new ChangeExpCommand(),
				new DbCommand(),
				new SetRoleCommand(),
				new TestCommand(),
				new SetExpInfoChannelCommand(),
				new SetPrizeInfoChannelCommand(),
				new StartCommand(),
				new StopCommand(),
				new ShowPetCommand(),
				new PlayCommand(),
				new CalcCommand(),
				new MtCommand(),
				new MtoCommand(),
				new Private(),
				new Rank(),
				new AddExpCommand());

		// start getting a bot account set up
		JDABuilder.createDefault(token)

				// set the game for when the bot is loading
				.setStatus(OnlineStatus.DO_NOT_DISTURB)
				.setActivity(Activity.playing("loading..."))

				// add the listeners
				.addEventListeners(eventWaiter, client.build(), new MessageEvent(), new Listener(bot))

				.setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
				.setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
				.enableIntents(GatewayIntent.GUILD_MEMBERS)
				// start it up!
				.build();
	}

	public EventWaiter getEventWaiter() {
		return eventWaiter;
	}

}