package views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import controllers.IdController;
import controllers.MessageController;
import controllers.TransactionController;
import models.Id;
import models.Message;
import youareell.YouAreEll;

// Simple Shell is a Console view for youareell.YouAreEll.
public class SimpleShell {

    public static void prettyPrint(String output) {
        // yep, make an effort to format things nicely, eh?
        System.out.println(output);
    }

    public static void main(String[] args) throws IOException {

        YouAreEll urll = new YouAreEll(new MessageController(), new IdController());

        String commandLine;
        BufferedReader console = new BufferedReader
                (new InputStreamReader(System.in));

        ProcessBuilder pb = new ProcessBuilder();
        List<String> history = new ArrayList<String>();
        int index = 0;
        //we break out with <ctrl c>
        while (true) {
            //read what the user enters
            System.out.println("cmd? ");
            commandLine = console.readLine();

            //input parsed into array of strings(command and arguments)
            String[] commands = commandLine.split(" ");
            List<String> list = new ArrayList<String>();

            //if the user entered a return, just loop again
            if (commandLine.equals(""))
                continue;
            if (commandLine.equals("exit")) {
                System.out.println("bye!");
                break;
            }

            //loop through to see if parsing worked
            for (int i = 0; i < commands.length; i++) {
                //System.out.println(commands[i]); //***check to see if parsing/split worked***
                list.add(commands[i]);

            }
            System.out.print(list); //***check to see if list was added correctly***
            history.addAll(list);
            try {
                //display history of shell with index
                if (list.get(list.size() - 1).equals("history")) {
                    for (String s : history)
                        System.out.println((index++) + " " + s);
                    continue;
                }

                // Specific Commands.

                // ids
                if (list.contains("ids")) {
                    String results = urll.get_ids();
                    SimpleShell.prettyPrint(results);
                    urll.postID("sugarpoppy hiep", "hiepnguyen90");
                    continue;
                }

                // messages
                if (list.contains("messages")) {
                    String results = urll.get_messages();
                    SimpleShell.prettyPrint(results);

                    continue;
                }

                if (list.contains("send")) {
                    String sender = null;
                    String results = "";
                    // Intrinsic sender, insert into list
                    if (sender != null) {
                        list.add(1, sender);
                    }
                    // if list has 3 words
                    if (list.size() >= 3) {
                        sender = list.get(1);
                        String receiver = list.get(list.size() - 1);
                        String msg = "";
                        // If sender id is invalid, prompt for valid id
                        // If sender is a valid name, set sender to their github
                        if (urll.getGitHubFromName(sender) != null) {
                            sender = urll.getGitHubFromName(sender);
                        }
                        // If sender is a valid github, proceed
                        if (urll.getIdFromGitHub(sender) == null) {
                            results = "Please use a valid sender GitHub ID.";
                        } else if (urll.getIdFromGitHub(receiver) == null || !list.get(list.size() - 2).equals("to")) {
                            // Else if last word is invalid receiver id or the key word "to" was not including,
                            // assume that sender wants to send globally
                            // Convert list to string
                            for (int i = 2; i < list.size(); i++){
                                msg += list.get(i) + " ";
                            }
                            msg =  msg.substring(0, msg.length() -  1); // Delete extra space
                            results = urll.send_messages(urll.getIdFromGitHub(sender), msg);
                        } else {
                            // Else both ids are valid, send message to specific receiver
                            // Convert list to string, excluding the words "to receiver"
                            for (int i = 2; i < list.size() - 2; i++){
                                msg += list.get(i) + " ";
                            }
                            msg =  msg.substring(0, msg.length() -  1); // Delete extra space
                            results = urll.send_messages(urll.getIdFromGitHub(sender), urll.getIdFromGitHub(receiver), msg);
                        }
                    } else {
                        results = "Please include your GitHub ID and a message to send.";
                    }
                    SimpleShell.prettyPrint(results);
                    continue;
                }
                    //!! command returns the last command in history
                    if (list.get(list.size() - 1).equals("!!")) {
                        pb.command(history.get(history.size() - 2));

                    }//!<integer value i> command
                    else if (list.get(list.size() - 1).charAt(0) == '!') {
                        int b = Character.getNumericValue(list.get(list.size() - 1).charAt(1));
                        if (b <= history.size())//check if integer entered isn't bigger than history size
                            pb.command(history.get(b));
                    } else {
                        pb.command(list);
                    }

                    // wait, wait, what curiousness is this?
                    Process process = pb.start();

                    //obtain the input stream
                    InputStream is = process.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);

                    //read output of the process
                    String line;
                    while ((line = br.readLine()) != null)
                        System.out.println(line);
                    br.close();



            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


        }
    }
}


// So what, do you suppose, is the meaning of this comment?
/** The steps are:
 * 1. parse the input to obtain the command and any parameters
 * 2. create a ProcessBuilder object
 * 3. start the process
 * 4. obtain the output stream
 * 5. output the contents returned by the command
 */