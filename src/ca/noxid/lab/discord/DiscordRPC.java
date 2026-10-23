package ca.noxid.lab.discord;

import fun.crashsystem.jdrpc.DiscordIPC;
import fun.crashsystem.jdrpc.activity.Activity;
import fun.crashsystem.jdrpc.activity.ActivityType;

public class DiscordRPC {
	public static void main(String[] args) {
		long clientId = 1563050087897104404L;

		try (DiscordIPC discord = DiscordIPC.create(clientId)) {
			discord.connect();

			Activity activity = new Activity.Builder()
					.setType(ActivityType.PLAYING)
					.setState("Main Menu")
					.setDetails("Building Discord rich presence")
					.setLargeImage("game-logo", "jDRPC")
					.setStartTimestamp(System.currentTimeMillis() / 1000L)
					.addButton("GitHub", "https://github.com/")
					.build();

			discord.setActivity(activity);

			System.out.println("Connected: " + discord.isConnected());
			System.out.println("State: " + discord.status());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}