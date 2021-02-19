package ru.geebrains.online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        startTextServer();
    }

    private static void startTextServer() {
        try(ServerSocket serverSocket = new ServerSocket(8194)){
            System.out.println("Server is listening");
            while(true) {
                Socket socket = serverSocket.accept();
                ServerThread thread = new ServerThread(socket);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerThread extends Thread {
    private Socket socket = null;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run(){
        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            Scanner sc = new Scanner(System.in)){
            System.out.println("Client is connected");
            out.println("Hello client");
            out.flush();
            String serverMessage = "";
            Thread serverReader = new Thread(()-> {
                String userMessage = "";
                try {
                    do {
                        userMessage = in.readLine();
                        System.out.println(userMessage);
                    } while (!userMessage.equalsIgnoreCase("stop"));
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
            serverReader.start();
            while (!socket.isClosed()){
                serverMessage = sc.nextLine();
                out.println(serverMessage);
                out.flush();
            }
            socket.close();
            System.out.println("Client leaved");

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
                System.out.println("Client leaved");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
