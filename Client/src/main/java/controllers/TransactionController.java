package controllers;

import models.Id;

import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import models.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class TransactionController {
    private String rootURL = "http://zipcode.rocks:8085";

    private MessageController msgCtrl = new MessageController();
    private IdController idCtrl =  new IdController();
    List<JSONObject> user = new ArrayList<>();
    List<JSONObject> messages = new ArrayList<>();
    private StringBuilder informationStringUsers;
    private StringBuilder informationStringMessages;
    StringBuilder informationString;


    public TransactionController(MessageController m, IdController j) throws MalformedURLException {
    }

    public TransactionController() {

    }

    public List<Id> getIds() {
        return null;
    }

    public String postId(String idtoRegister, String githubName) {
        Id tid = new Id(idtoRegister, githubName);
        tid = idCtrl.postId(tid);
        return ("Id registered.");

    }

    public String postMessage(Id myId, Id toId, Message msg){
        return msgCtrl.postMessage(myId, toId, msg.getMessage()).getMessage();
    }

    public String putId(Id id){
        idCtrl.putId(id);
        return ("Put the ID");

    }

    public String getMessages() throws IOException, ParseException {
        // Last 20 messages
        ArrayList<Message> messages = msgCtrl.getMessages();
        StringBuilder output = new StringBuilder();
        int num = 19;
        if (num > messages.size()) {
            num = messages.size();
        }
        for (int i = num; i >= 0; i--) {
            output.append(messages.get(i)).append("\n");
        }
        return output.substring(0, output.length() - 1);
    }

    public String makeCall(String s, String get, String s1) throws IOException, ParseException {
        ArrayList<Id> ids = idCtrl.getIds();
        StringBuilder output = new StringBuilder();
        if(s.equals("/ids")) {
            for (Id id : ids) {
                output.append(id.getGithub()).append("\n");
            }
        } else if (s.equals("/name")) {
            for (Id id : ids) {
                output.append(id.getName()).append("\n");
            }
        } else if (s.equals("/messages")) {
            output.append(getMessages());
        } else {
            System.err.print("lol"); System.exit(0);
        }
        return output.substring(0, output.length() - 1);
    }

    public String sendMessage(Id myId, Id toId, String msgs) {
        return msgCtrl.postMessage(myId, toId, msgs).toString();
    }

    public String sendMessage(Id myId, String msg) {
        return msgCtrl.postMessage(myId, null, msg).toString();
    }
    public Id getIdFromGitHub(String github) throws IOException, ParseException {
        return idCtrl.getIdFromGitHub(github);
    }

    public String getGitHubFromName(String name) throws IOException, ParseException {
        return idCtrl.getGitHubFromName(name);
    }

    public String putId(String name, String githubName) {
        Id id = new Id(name, githubName);
        idCtrl.putId(id);
        return ("Id Put. Random id generated.");
    }
    public String putId(String idtoRegister, String name, String githubName) {
        Id id = new Id(idtoRegister, name, githubName);
        idCtrl.putId(id);
        return ("Id Put.");
    }
    public String getMessages(Id id) throws IOException, ParseException {
        // Last 20 messages
        ArrayList<Message> messages = msgCtrl.getMessagesForId(id);
        StringBuilder output = new StringBuilder();
        int num = 19;
        if (num > messages.size()) {
            num = messages.size() - 1;
        }
        for (int i = num; i >= 0; i--) {
            output.append(messages.get(i)).append("\n");
        }
        return output.substring(0, output.length() - 1);
    }
}
