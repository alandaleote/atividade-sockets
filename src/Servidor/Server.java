package Servidor;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server extends Thread {

    public static final String MCAST_ADDR = "230.1.1.1";
    public static final int MCAST_PORT = 4000;
    public static final int DGRAM_BUF_LEN = 2048;
    private ArrayList<String> contacts;

    public void run() {
        contacts = new ArrayList();
        String msg = "";
        InetAddress group = null;
        try {
            group = InetAddress.getByName(MCAST_ADDR);	
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        for (;;) {
            try {
                MulticastSocket socket = new MulticastSocket(MCAST_PORT);
                socket.joinGroup(group);

                System.out.println("Aguardando conexão");
                
                byte[] buffer = new byte[DGRAM_BUF_LEN];
                DatagramPacket receive = new DatagramPacket(buffer,buffer.length);
                socket.receive(receive);
                byte [] data = receive.getData();
                msg = new String(data);
                System.out.println("Dados recebidos: " + msg);
                
                if(msg.contains("<inicio>")){
                    msg = msg.substring(8);
                    String name = "";
                    int i = 0;
                    while(Character.isLetter(msg.charAt(i))){
                        name = name + msg.charAt(i);
                        i++;
                    }
                    contacts.add(name);
                    String cont = "<contatos>" + contacts.toString();
                    System.out.println("Enviando: " + cont);
                    DatagramPacket packet = new DatagramPacket( cont.getBytes(), cont.length(), group, MCAST_PORT);
                    System.out.println("Enviando: " + msg + "  com um TTL= " + socket.getTimeToLive());
                    socket.send(packet);
                    socket.close();
                }else if(msg.contains("C<msg>")){
                    msg = msg.substring(1);
                    msg = "S" + msg;
                    DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), group, MCAST_PORT);
                    System.out.println("Enviando: " + msg.toString() + "   com um TTL= " + socket.getTimeToLive());
                    socket.send(packet);
                    socket.close();
                }else if(msg.contains("<output>")){
                    String output = "";
                    int i = 8;
                    while(Character.isLetter(msg.charAt(i))){
                        output = output + msg.charAt(i);
                        i++;
                    }
                    contacts.remove(output);
                    String cont = "<contatos>" + contacts.toString();
                    System.out.println("Enviando: " + cont);
                    DatagramPacket packet = new DatagramPacket( cont.getBytes(), cont.length(), group, MCAST_PORT);
                    //System.out.println("Enviando: " + msg + "   com um TTL= " + socket.getTimeToLive());
                    socket.send(packet);
                    socket.close(); 
                }
                //Envia dados para todo grupo
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(2);
            }

            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException ie) {
            }
        }
    }

    public static void main(String[] args) {

        try {
            Server mc2 = new Server();
            mc2.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

