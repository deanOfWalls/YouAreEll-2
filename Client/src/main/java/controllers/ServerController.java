package controllers;

//import spiffyUrlManipulator

import models.Id;

import javax.json.JsonString;
import java.net.HttpURLConnection;

public class ServerController {
    private String rootURL = "http://zipcode.rocks:8085/ids";

    private ServerController svr = new ServerController();

    private ServerController() {

    }

    public ServerController shared() {
        return svr;
    }

    public JsonString idGet() {
        // url -> /ids/
        // send the server a get with url
        // return json from server
        return null;
    }
    public JsonString idPost(Id input) {
        // url -> /ids/
        // create json from Id
        // request
        // reply
        // return json
        return null;
    }
    public JsonString idPut(Id input) {
        // url -> /ids/
        return null;
    }


}

// ServerController.shared.doGet()