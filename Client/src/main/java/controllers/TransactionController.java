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

    public String getMessage(){
        return msgCtrl.getMessages().toString();

    }

    public String makecall(String s, String get, String s1) {
        String output = "";
        if(s.equals("/ids")){
            for(JSONObject z : user){
                output += z.get("userid") + "\n";
            }
        } else if(s.equals("/name")){
            for(JSONObject z : user){
                output += z.get("name") + "\n";
            }
        } else if (s.equals("/github")){
            for(JSONObject z : user){
                output += z.get("github") + "\n";
            }
        } else if (s.equals("/messages")){
            for(JSONObject z : messages){
                output += z.get("message") + "\n";
            }
        }
        return output;
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
}
