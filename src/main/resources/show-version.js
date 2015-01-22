dialogue.expect(">");
dialogue.send("term length 0\r");
dialogue.expect(">");
dialogue.send("show version\r");
dialogue.expect(">");
dialogue.send("exit\r");
