package controllers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import models.Id;
import models.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MessageController {
    StringBuilder informationStringMessages;
    ArrayList<Message> messages = new ArrayList<>();

    private HashSet<Message> messagesSeen;
    // why a HashSet??

    public ArrayList<Message> getMessages() {
        int i = 0;
        try {
            URL urlMessages = new URL("http://zipcode.rocks:8085/messages");

            HttpURLConnection conn = (HttpURLConnection) urlMessages.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();


            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                informationStringMessages = new StringBuilder();
                Scanner scanner = new Scanner(urlMessages.openStream());
                while (scanner.hasNext()) {
                    informationStringMessages.append(scanner.nextLine());
                }

                scanner.close();

                JSONParser parse = new JSONParser();
                JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(informationStringMessages));


                for (Object o : dataObject) {
                    String msg = (String) ((JSONObject) o).get("message");
                    String fromid = (String) ((JSONObject) o).get("fromid");
                    String toid = (String) ((JSONObject) o).get("toid");

                    Message message = new Message(msg, fromid, toid);

                    messages.add(message);

                }
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    public ArrayList<Message> getMessagesForId(Id Id) {
        ArrayList<Message> msg1 = getMessages();
        ArrayList<Message> msg2 = new ArrayList<>();
        for (Message s : msg1) {
            if (s.getToId().equals(Id.getGithub())) {
                msg2.add(s);
            }
        }
        return msg2;
    }

    public Message getMessageForSequence(String seq) {
        ArrayList<Message> seq1 = getMessages();
        for (Message s : seq1) {
            if (s.getSeqId().equals(seq)) {
                return s;
            }
        }
        return null;
    }

    public ArrayList<Message> getMessagesFromFriend(Id myId, Id friendId) {
        ArrayList<Message> msgFrd = getMessages();
        ArrayList<Message> msgFrd2 = new ArrayList<>();
        for (Message s : msgFrd) {
            if (s.getFromId().equals(myId) && s.getToId().equals(friendId)) {
                msgFrd2.add(s);
            }
        }
        return msgFrd2;
    }

    public Message postMessage(Id myId, Id toId, String msg) {
        // Finals
        final String toID;
        // Timestamp

        // Create Json
        JSONObject message = new JSONObject();
        message.put("sequence", "-");
        message.put("timestamp", "2023-04-09T23:15:27.096349871Z");
        message.put("fromid", myId.getGithub());
        if (toId == null) {
            toID = "";
        } else {
            toID = toId.getGithub();
        }
        message.put("toid", toID);
        message.put("message", msg);
        // Post Message (note to self: posting to id is different whyyy)
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://zipcode.rocks:8085/ids/" + myId.getGithub() + "/messages")) // Why was this info nowhere
                    .method("POST", HttpRequest.BodyPublishers.ofString(message.toString()))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return new Message(msg, myId.getGithub(), toID);
    }
}
 
