package controllers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.util.JSONPObject;
import models.Id;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class IdController {
    private HashMap<String, Id> allIds;

    Id myId;
    private String rootURL = "http://zipcode.rocks:8085";

    private MessageController msgCtrl;
    private IdController idCtrl;
    ArrayList<Id> user = new ArrayList<>();
    private StringBuilder informationStringUsers;

    StringBuilder informationString;


    public ArrayList<Id> getIds() throws IOException, ParseException {
        // IDs
        final ArrayList<Id> Ids = new ArrayList<>();
        final StringBuilder informationStringIds;
        // Create connection
        final URL urlIds = new URL(rootURL + "/ids");
        final HttpURLConnection conn;
        conn = (HttpURLConnection) urlIds.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.connect();
        // Get Server Status
        int responseCode = conn.getResponseCode();
        // Check if server is online
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {
            // Get IDs
            informationStringIds = new StringBuilder();
            Scanner scan = new Scanner(urlIds.openStream());
            while (scan.hasNext()) {
                informationStringIds.append(scan.nextLine());
            }
            scan.close();
        }
        // Parse Messages from JSON Data
        JSONParser parse = new JSONParser();
        JSONArray dataObjectIds = null; // Ids
        try {
            dataObjectIds = (JSONArray) parse.parse(String.valueOf(informationStringIds));
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }
        // Add all Ids data into ArrayList
        for (Object o : dataObjectIds) {
            String uid = (String) ((JSONObject) o).get("userid");
            String name = (String) ((JSONObject) o).get("name");
            String github = (String) ((JSONObject) o).get("github");
            Id id = new Id(uid, name, github);
            Ids.add(id);
        }
        conn.disconnect();
        return Ids;
    }

        public String postId (String idtoRegister, String githubName){
            Id tid = new Id(idtoRegister, githubName);
            tid = idCtrl.postId(tid);
            return ("Id registered.");
        }



    public Id postId(Id id) {
        JSONObject Id = new JSONObject();
        Id.put("userid", id.getUid());
        Id.put("name", id.getName());
        Id.put("github", id.getGithub());

        try {
            URL urlID = new URL(rootURL + "/ids");

            URL url = urlID;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("User-Agent", "");

            try(DataOutputStream dos = new DataOutputStream(conn.getOutputStream())){
                dos.writeBytes(Id.toString());
            }

            try (BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = bf.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return id;
    }

    public Id putId(Id id) {

        String userid = id.getUid();
        String github = id.getGithub();

        try {
            ArrayList<Id> ids = getIds();
            for (Id s : ids) {
                if (s.getGithub().equals(github)) {
                    userid = s.getUid();
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JSONObject ids = new JSONObject();
        ids.put("userid", userid);
        ids.put("github", id.getGithub());
        ids.put("name", id.getName());

        if (userid.equals("")) {
            postId(id);
        } else {
            try {
                URL urlID = new URL(rootURL + "/ids");

                URL url = urlID;
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("User-Agent", "");

                try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                    dos.writeBytes(ids.toString());
                }

                try (BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = bf.readLine()) != null) {
                        System.out.println(line);
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            return id;
        } return null;
    }

    public Id getIdFromGitHub(String github) throws IOException, ParseException {
        ArrayList<Id> ids = getIds();
        for (Id iD : ids) {
            if (iD.getGithub().equals(github)) {
                return iD;
            }
        }
        return null;
    }
    public String getGitHubFromName(String name) throws IOException, ParseException {
        ArrayList<Id> ids = getIds();
        for (Id iD : ids) {
            if (iD.getGithub().equals(name)) {
                return iD.getGithub();
            }

        }

        return null;
    }
}