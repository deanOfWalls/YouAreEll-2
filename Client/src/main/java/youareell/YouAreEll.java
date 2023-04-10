package youareell;

import controllers.*;
import models.Id;
import models.Message;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

public class YouAreEll {

    TransactionController tt;

    public YouAreEll (TransactionController t) {
        this.tt = t;
    }

    public YouAreEll (MessageController m, IdController j) throws MalformedURLException {
        this.tt = new TransactionController(m, j);
    }

    private String MakeURLCall(String s, String get, String s1) {
        return null;
    }

    public static void main(String[] args) throws MalformedURLException {
        // hmm: is this Dependency Injection?
        YouAreEll urlhandler = new YouAreEll(
            new TransactionController(
                new MessageController(), new IdController()
        ));
        System.out.println(urlhandler.MakeURLCall("/ids", "GET", ""));
        System.out.println(urlhandler.MakeURLCall("/messages", "GET", ""));
    }

    public String get_ids() throws IOException, ParseException { //
        return tt.makeCall("/ids", "GET", "");
    }

    public String get_messages() throws IOException, ParseException {
//        return MakeURLCall("/messages", "GET", "");
        URL url = new URL("http://zipcode.rocks:8085/%22");
        return tt.getMessages();
    }

    public String postID(String idtoRegister, String githubName){
        return tt.postId(idtoRegister, githubName);
    }

    public String messageID(Id myId, Id toId, Message msg){return tt.postMessage(myId, toId, msg);}

    public String putId(Id id){
        return tt.putId(id);
    }

    public String send_messages(Id fromid, Id toid, String msg) {
        return tt.sendMessage(fromid, toid, msg);
    }
    public String send_messages(Id fromid, String msg) {
        return tt.sendMessage(fromid, msg);
    }

    public Id getIdFromGitHub(String github) throws IOException, ParseException {
        return tt.getIdFromGitHub(github);
    }

    public String getGitHubFromName(String name) throws IOException, ParseException {
        return tt.getGitHubFromName(name);
    }
    public String post_id(String name, String github) {
        return tt.postId(name, github);
    }

    public String put_id(String name, String github) {
        return tt.putId(name, github);
    }
}
