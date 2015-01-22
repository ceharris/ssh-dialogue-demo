package ceh.demo;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.ualerts.expect4j.Dialogue;
import org.ualerts.expect4j.DialogueFactory;
import org.ualerts.expect4j.script.URLScript;
import org.ualerts.expect4j.service.DialogueCallback;
import org.ualerts.expect4j.service.DialogueService;
import org.ualerts.expect4j.service.ExecutorDialogueService;

public class SshDialogueServiceDemo {

  public static void main(String[] args) {
    VersionService versionService = new SimpleVersionService();
    
    DialogueService dialogueService = new ExecutorDialogueService(
        Executors.newCachedThreadPool());

    Dialogue dialogue = newDialogueWithHost("r14-c50-01.cns.vt.edu", 
        "store-version.js", new OutputStreamWriter(System.out), versionService);
    dialogueService.submit(dialogue, SimpleDialogueCallback.INSTANCE);
    
    dialogueService.shutdown();    
    versionService.reportVersions();
  }
  
  private static Dialogue newDialogueWithHost(String host, 
      String scriptResource, Writer output, VersionService versionService) {
    
    Map<String, Object> props = new HashMap<String, Object>();
    props.put("host", host);
    props.put("username", System.getProperty("ssh.username"));
    props.put("password", System.getProperty("ssh.password"));
    Dialogue dialogue = DialogueFactory.getInstance()
        .newDialogue("ssh", "javascript", props);

    URL url = Thread.currentThread().getContextClassLoader()
        .getResource(scriptResource);
    
    dialogue.setName(host);
    dialogue.setScript(new URLScript(url));
    dialogue.setLogWriter(output);
    dialogue.setLoggingEnabled(true);
    dialogue.setAttribute("versionService", versionService);
    return dialogue;
  }
  
  static class SimpleDialogueCallback implements DialogueCallback {

    public static final DialogueCallback INSTANCE = new SimpleDialogueCallback();
    
    public void onReturn(Dialogue dialogue, Object result) {
      System.out.format("\ndialogue %s finished successfully\n", 
          dialogue.getName());
    }

    public void onException(Dialogue dialogue, Exception exception) {
      System.err.format("dialogue %s finished with error: %s\n", 
          dialogue.getName(), exception.toString());
    }
    
  }
  
  public interface VersionService {
    
    void storeVersion(String host, String version);
    
    void reportVersions();
    
  }
  
  public static class SimpleVersionService implements VersionService {
    private final Map<String, String> versionMap = 
        new HashMap<String, String>();

    public void storeVersion(String host, String version) {
      versionMap.put(host, version);      
    }

    public void reportVersions() {
      System.out.println();
      System.out.format("%-30s %s\n", "Host", "Version");
      for (String key : versionMap.keySet()) {
        System.out.format("%-30s %s\n", key, versionMap.get(key));
      }
    }
  }

}
