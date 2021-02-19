package ru.geebrains.online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        startTextClient();
    }

    private static void startTextClient() {

        try (Socket socket = new Socket("localhost", 8194);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream());
             Scanner sc = new Scanner(System.in)) {
            String myMessage = "";


            Thread serverReader = new Thread(() -> {
                String serverMessage = "";
                try {
                    while (!socket.isClosed()) {
                        serverMessage = in.readLine();
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            serverReader.start();
            while (!myMessage.equalsIgnoreCase("stop")){
                myMessage = sc.nextLine();
                out.println(myMessage);
                out.flush();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
