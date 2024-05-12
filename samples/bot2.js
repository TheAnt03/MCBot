function getPlayersMessage(players) {
	var message = "The current players on the server ar2e:\n";
	
	for(var i = 0; i < players.length; i++) {
		var playerName = players[i].getName();

		message += playerName + "\n";
	}
	
	return message;
}

function onMessageSend(bot, message) {
	var content = message.getContent();
	var player = message.getPlayer();
	
	switch(content) {
		case "!hello":
			bot.sendMessage("Hello " + player.getName());
			
			break;
		case "!getActivePlayers":
			var server = bot.getServer();
			var players = server.getOnlinePlayers();
			var message = getPlayersMessage(players);

			bot.sendMessage(message);

			break;
	}
}
