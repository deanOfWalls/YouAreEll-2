package controllers;

import models.Id;

import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class TransactionController {
    private String rootURL = "http://zipcode.rocks:8085";

    private MessageController msgCtrl;
    private IdController idCtrl;
    List<JSONObject> user = new ArrayList<>();
    List<JSONObject> messages = new ArrayList<>();
    private StringBuilder informationStringUsers;
    private StringBuilder informationStringMessages;
    StringBuilder informationString;
    URL urlID = new URL(rootURL + "/ids");
    URL urlMessages = new URL("http://zipcode.rocks:8085/messages");

    {
        try {
            URL url = urlID;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if(responseCode != 200){
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                informationString = new StringBuilder();

                informationStringUsers = new StringBuilder();

                informationStringMessages = new StringBuilder();

                Scanner scanner = new Scanner(urlID.openStream());

                while(scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }

                scanner.close();
                scanner = new Scanner(urlMessages.openStream());

                while(scanner.hasNext()){
                    informationStringMessages.append(scanner.nextLine());
                }
                scanner.close();

                JSONParser parse = new JSONParser();
                JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(informationString));

                JSONArray dataObject2 = (JSONArray) parse.parse(String.valueOf(informationStringMessages));

                for(Object o : dataObject) {
                    user.add((JSONObject) o);
                }
                for(Object i : dataObject2) {
                    messages.add((JSONObject) i);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TransactionController(MessageController m, IdController j) throws MalformedURLException {
    }

    public List<Id> getIds() {
        return null;
    }

    public String postId(String idtoRegister, String githubName) {
        Id tid = new Id(idtoRegister, githubName);
        tid = idCtrl.postId(tid);
        return ("Id registered.");
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
}
