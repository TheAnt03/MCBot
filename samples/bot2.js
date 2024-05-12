function getPlayersMessage(players) {
	var message = "The current players on the server are:\n";

	for(var i = 0; i < players.length; i++) {
		var playerName = players[i].getName();

		message += playerName + "\n";
		message += playerName + "\n";
		message += playerName + "\n";
	}

	return message;
}

function onMessageSend(bot, message) {
	var content = message.getContent();
	var player = message.getPlayer();

	switch(content) {
		case "!test2":
			bot.sendMessage("Hello " + player.getName());

			break;
		case "!getActivePlayers":
			var server = bot.getServer();
			var players = server.getOnlinePlayers();
			var message = getPlayersMessage(players);
			bot.sendMessage(message);

			break;
		case "!getRandomWord":
			var response = bot.get("https://random-word-api.herokuapp.com/word");
			var responseJSON = JSON.parse(response);

			bot.sendMessage(responseJSON);
			break;
	}
}

function onPlayerJoin(bot, player) {
	var message = "";

	if(player.hasPlayedBefore()) {
		message = "Welcome back " + player.getName();
	} else {
		message = "Welcome " + player.getName();
	}

	bot.sendMessage(message);
}