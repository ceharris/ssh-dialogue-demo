dialogue.expect(">");
dialogue.send("term length 0\r");
dialogue.expect(">");
dialogue.send("show version\r");
var ctx = dialogue.expect("Cisco IOS Software, .*, Version (.*),");
versionService.storeVersion(dialogue.name, ctx.matchResult.group(1));
dialogue.expect(">");
dialogue.send("exit\r");
