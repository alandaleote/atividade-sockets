package cliente;

import java.io.*;
import java.net.*;

public class Client extends Thread {

    public static final String MCAST_ADDR = "230.1.1.1";
    public static final int MCAST_PORT = 4000;
    public static final int DGRAM_BUF_LEN = 2048;
    Window v = new Window(0);

    public void run() {
        InetAddress group = null;
        try {
            group = InetAddress.getByName(MCAST_ADDR);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        boolean exit = true;

        try {
            MulticastSocket socket = new MulticastSocket(MCAST_PORT);
            socket.joinGroup(group);
            DatagramPacket contact = new DatagramPacket(("<inicio>" + v.getName()).getBytes(), ("<inicio>" + v.getName()).length(), group, MCAST_PORT);
            socket.send(contact);
            while (exit) {
                //System.out.println("input");
                if (v.getStatus() == 0) {
                    //System.out.println("input read");
                    socket.setSoTimeout(100);
                    try {
                        byte[] buffer = new byte[DGRAM_BUF_LEN];
                        DatagramPacket receive = new DatagramPacket(buffer, buffer.length);
                        socket.receive(receive);
                        byte[] data = receive.getData();
                        String message = new String(data);
                        System.out.println("Dados recebidos: " + message);
                        v.setNewMessage(message);
                    } catch (Exception e) {
                    }
                } else if (v.getStatus() == 1) {
                    //System.out.println("input write");
                    String message = "";
                    
                    if(v.getOutNum() == 1){
                        message = "<output>" + v.getName();
                    }else{
                        if(v.getActiveTab() != 0){
                            message = "C<msg><privado><" + v.getName() + "><" + v.getChatContacts(v.getActiveTab()) + ">" + v.getActiveMessage();
                        }else if(v.getActiveTab() == 0){
                            message = "C<msg><" + v.getName() + ">" + v.getActiveMessage();
                        }
                    }
                    DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), group, MCAST_PORT);
                    System.out.println("Enviando: " + message + " com um TTL= " + socket.getTimeToLive());
                    socket.send(packet);
                    v.setStatus(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }

    }//run

    public static void main(String[] args) {

        try {
            Client client = new Client();
            client.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }//main
}//class
