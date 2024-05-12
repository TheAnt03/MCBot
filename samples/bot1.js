function onMessageSend(bot, message) {
	var content = message.getContent();
	
	if(content.equals("!test")) {
		bot.sendMessage("Hello");
	}
}