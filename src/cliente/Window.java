package cliente;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Window implements ActionListener{
    public static String Name;
    public  int statusOp;
    public int outNum;
    private JFrame window;
    private JTabbedPane chats;
    private JPanel panel;
    private ArrayList<JTextArea> chatPerson;
    private ArrayList<String> contacts;
    private ArrayList<String> contactsChat;
    private ArrayList<JTextField> sendText;
    private ArrayList<JButton> sendButtons;
    
    public Window(int operation){
        statusOp = operation;
        outNum = 0;
        Name = JOptionPane.showInputDialog("Insira seu nome: ");
        while(Name.isEmpty()){
            Name = JOptionPane.showInputDialog("Insira seu nome: ");
        }
        window = new JFrame();
        
        window.setSize(500, 500);
        window.setTitle(Name + ": Chat multicast");
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(3);
        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                statusOp = 1;
                outNum = 1;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        chatPerson = new ArrayList();
        contactsChat = new ArrayList();
        contacts = new ArrayList();
        sendText = new ArrayList();
        sendButtons = new ArrayList();
        
        startComponents();
        window.setVisible(true);
    }
    
    private void startComponents(){
        panel = new JPanel();
        JLabel title = new JLabel("Seu chats: " + Name);
        
        panel.setLayout(null);
        window.getContentPane().add(panel);
        title.setBounds(10, 10, 180, 30);
        panel.add(title);
        
        setButtons();
        setGeneral();
    }
    
    private void setButtons(){
        JButton newChat = new JButton();
        newChat.addActionListener(this);
        newChat.setText("Chat privado");
        newChat.setBounds(290, 10, 200, 30);
        panel.add(newChat);
    }
    
    private void setGeneral(){
        chats = new JTabbedPane();
        chats.setBounds(10, 50, 480, 400);
        newChat("Geral");
        panel.add(chats);
    }
    
    private void newChat(String name){
        JPanel newPanel = new JPanel();
        newPanel.setLayout(null);
        chats.addTab(name, newPanel);
        
        JTextArea chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setForeground(Color.BLACK);
        chatPerson.add(chatBox);
        JScrollPane scroll = new JScrollPane(chatPerson.get(chatPerson.size() - 1));
        scroll.setBounds(10,10,455,300);
        newPanel.add(scroll);
        
        JTextField text = new JTextField();
        sendText.add(text);
        sendText.get(sendText.size() - 1).setBounds(10, 320, 350, 39);
        newPanel.add(sendText.get(sendText.size() - 1));
        
        JButton send = new JButton("Enviar");
        sendButtons.add(send);
        sendButtons.get(sendButtons.size() - 1).setBounds(370, 320, 95, 38);
        sendButtons.get(sendButtons.size() - 1).setText("Enviar");
        sendButtons.get(sendButtons.size() - 1).addActionListener(this);
        String buttonName = "Enviar" + (sendButtons.size() - 1);
        System.out.println(buttonName);
        sendButtons.get(sendButtons.size() - 1).setName(buttonName);
        newPanel.add(sendButtons.get(sendButtons.size() - 1));
        
        contactsChat.add(name);
    }
    
    public int getStatus(){
        return statusOp;
    }
    
    public int getOutNum(){
        return outNum;
    }
    
    public void setStatus(int newStatus){
        statusOp = newStatus;
    }
    
    public String getActiveMessage(){
        int selectedIndex = chats.getSelectedIndex();
        String text = sendText.get(selectedIndex).getText();
        sendText.get(selectedIndex).setText("");
        return text;
    }
    
    public int getActiveTab(){
        
        return chats.getSelectedIndex();
    }
    
    public String getChatContacts(int index){
        return contactsChat.get(index);
    }
    
    public void setNewMessage(String message){
        if(message.contains("<contatos>")){
            String contact = "";
            contacts.clear();
            for(int i = 12; i < message.length(); i++){
                System.out.println("|" + message.charAt(i) + "|");
                //Thread.sleep(1000);
                if(Character.isLetter(message.charAt(i))){
                    contact += message.charAt(i);
                }else if(message.charAt(i) == ','){
                    contacts.add(contact);
                    contact = "";
                }else{
                    //break;
                }
            }
            contacts.add(contact);
            System.out.println(contacts);
            //contacts.remove(Name);
        }else if(message.startsWith("S<msg>")){
            String receiver = "";
            message = message.substring(6);
            
            //char feliz = (char) "\u1F600";
            
            message = message.replace(":)", "\uD83D\uDE04");
            message = message.replace(":D", "\uD83D\uDE03");
            message = message.replace(":3", "\uD83D\uDE0A");
            message = message.replace(":P", "\uD83D\uDE1C");
            message = message.replace(":(", "\uD83D\uDE14");
            message = message.replace(":'(", "\uD83D\uDE22");
            message = message.replace("D:", "\uD83D\uDE29");
            message = message.replace(">:c", "\uD83D\uDE21");
            
            
            if(message.contains("<privado>")){
                String sendTo = "";
                int i = 1;
                
                message = message.substring(9);
                while(Character.isLetter(message.charAt(i))){
                    receiver = receiver + message.charAt(i);
                    i++;
                }
                message = message.substring(i + 1);
                
                i = 1;
                System.out.println(message);
                while(Character.isLetter(message.charAt(i))){
                    sendTo = sendTo + message.charAt(i);
                    i++;
                }
                message = message.substring(i + 1);
                System.out.println(message);
                System.out.println("De: " + receiver + " para: " + sendTo);
                
                if(Name.equals(sendTo)){
                    if(contactsChat.contains(receiver)){
                        int selectedIndex = contactsChat.indexOf(receiver);
                        sendText.get(selectedIndex).setText("");
                        chatPerson.get(selectedIndex).setText(chatPerson.get(selectedIndex).getText() + "\n" + receiver + ":" + message);
                    }else{
                        newChat(receiver);
                        chats.setSelectedIndex(contactsChat.indexOf(receiver));
                        int selectedIndex = chats.getSelectedIndex();
                        sendText.get(selectedIndex).setText("");
                        chatPerson.get(selectedIndex).setText(chatPerson.get(selectedIndex).getText() + "\n" + receiver + ":" + message);
                    }
                }else if(Name.equals(receiver)){
                    int selectedIndex = chats.getSelectedIndex();
                    sendText.get(selectedIndex).setText("");
                    chatPerson.get(selectedIndex).setText(chatPerson.get(selectedIndex).getText() + "\n" + receiver + ":" + message);
                }
            }else{
                int selectedIndex = 0;
                sendText.get(selectedIndex).setText("");
                chatPerson.get(selectedIndex).setText(chatPerson.get(selectedIndex).getText() + "\n" + message);
            }
        }
    }
    
    public String getName(){
        return Name;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        
        JButton button = (JButton) ae.getSource();
        
        if(button.getActionCommand().equals("Chat privado")){
            if(contacts.toArray().length == 0){
                JOptionPane.showMessageDialog(null, "Não há contatos disponíveis", "Contatos", JOptionPane.ERROR_MESSAGE);
            }else{
                ArrayList<String> showContacts = contacts;
                showContacts.remove(Name);
                String selection = (String) JOptionPane.showInputDialog(null, "Novo chat", "Selecione usuário para enviar mensagem privadas", JOptionPane.INFORMATION_MESSAGE, null, showContacts.toArray(), showContacts.toArray()[0]);
                if(contactsChat.contains(selection)){
                    chats.setSelectedIndex(contactsChat.indexOf(selection));
                }else{
                    newChat(selection);
                    chats.setSelectedIndex(contactsChat.indexOf(selection));
                }
                statusOp = 0;
            }
        }else if(button.getActionCommand().equals("Enviar")){
            statusOp = 1;
        }
    }
}
