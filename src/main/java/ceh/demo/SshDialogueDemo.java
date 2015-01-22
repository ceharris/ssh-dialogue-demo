package ceh.demo;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.ualerts.expect4j.Dialogue;
import org.ualerts.expect4j.DialogueFactory;
import org.ualerts.expect4j.script.URLScript;

public class SshDialogueDemo {

  public static void main(String[] args) throws Exception {
    Map<String, Object> props = new HashMap<String, Object>();
    props.put("host", "r14-c50-01.cns.vt.edu");
    props.put("username", System.getProperty("ssh.username"));
    props.put("password", System.getProperty("ssh.password"));
    Dialogue dialogue = DialogueFactory.getInstance()
        .newDialogue("ssh", "javascript", props);

    URL url = Thread.currentThread().getContextClassLoader()
        .getResource("show-version.js");
    
    dialogue.setScript(new URLScript(url));
    dialogue.setLogWriter(new OutputStreamWriter(System.out));
    dialogue.setLoggingEnabled(true);  // could also do this in the script itself
    
    dialogue.call();
  }
  
}
