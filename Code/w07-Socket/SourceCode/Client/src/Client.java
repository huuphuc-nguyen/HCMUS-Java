import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.*;

public class Client {
      static  Socket clientSocket;
     public static void main(String[] args) {

        boolean connected = false;

        while (!connected)
        {
            try {
                clientSocket = new Socket("localhost", 1235); // Replace "localhost" with the server's IP address if necessary

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                
                Thread receivingThread = new Thread(() -> {
                    try {
                        while(true){
                            // Read a line from the server
                        String serverMessage;

                        if((serverMessage = in.readLine()) != null){
                            System.out.println("Received from server: " + serverMessage);
                            
                            
                            if (serverMessage.equals("Send Root Folder"))
                            {
                                out.println("Root Dir");

                                 // Get the root folders
                                List<String> rootFolders = new ArrayList<>();
                                File[] roots = File.listRoots();
                                for (File root : roots) {
                                    rootFolders.add(root.getPath());
                                }

                                // Send the root folder list to the client
                                for (String folder : rootFolders) {
                                    out.println(folder);
                                }

                                out.println("Over");
                            }
                            else if (serverMessage.equals("Send Sub Folder"))
                            {
                                // Get Path
                                String path = in.readLine();
                                File folder = new File(path);

                                out.println("Sub Dir");


                                // Get the sub folders
                                File[] subDirectories = folder.listFiles(File::isDirectory);
                    
                                // Send the root folder list to the client

                                if (subDirectories != null) {
                                    for (File directory : subDirectories) {
                                        out.println(directory.getAbsolutePath());
                                    }
                                }

                                out.println("Over");
                            }
                            else if (serverMessage.equals("Send Back Folder"))
                            {
                                // Get Path
                                String path = in.readLine();

                                if (path.length() <= 3) {

                                    out.println("Back Dir");
                                    out.println("0");

                                        // Get the root folders
                                        List<String> rootFolders = new ArrayList<>();
                                        File[] roots = File.listRoots();
                                        for (File root : roots) {
                                            rootFolders.add(root.getPath());
                                        }

                                        // Send the root folder list to the client
                                        for (String _folder : rootFolders) {
                                            out.println(_folder);
                                        }

                                        out.println("Over");
                                }
                                else{
                                    File folder = new File(path).getParentFile();

                                    // if (folder.getAbsolutePath().length() <= 3){
                                    //     out.println("Back Dir");
                                    //     out.println("0");

                                    //     // Get the root folders
                                    //     List<String> rootFolders = new ArrayList<>();
                                    //     File[] roots = File.listRoots();
                                    //     for (File root : roots) {
                                    //         rootFolders.add(root.getPath());
                                    //     }

                                    //     // Send the root folder list to the client
                                    //     for (String _folder : rootFolders) {
                                    //         out.println(_folder);
                                    //     }

                                    //     out.println("Over");
                                    // }
                                    //else {
                                        out.println("Back Dir");
                                        out.println(folder.getAbsolutePath());

                                        // Get the sub folders
                                        File[] subDirectories = folder.listFiles(File::isDirectory);
                            
                                        // Send the root folder list to the client

                                        if (subDirectories != null) {
                                            for (File directory : subDirectories) {
                                                out.println(directory.getAbsolutePath());
                                            }
                                        }

                                        out.println("Over");
                                    //}
                                }
                            }
                            else if (serverMessage.equals("Send Spy"))
                            {   
                                
                                // Define the directory to watch
                                String directoryPath = in.readLine();

                                // Create a new thread for each client
                                WatchThread w = new WatchThread(directoryPath);
                                Thread t = new Thread(w);
                                t.start();
                            }
                        }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                receivingThread.start();

                // Close the connection
                // in.close();
                // out.close();
                //clientSocket.close();

                connected = true;
            } catch (IOException e) {
                System.out.println("Can't connect to server. Retrying in 1 hour...");
                try {
                    Thread.sleep(3600000); // Delay for 1 hour
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

                // Keep the client alive
                while (connected) {
                    // Perform any additional actions or communication with the server here
        
                    // Add a delay to prevent the program from exiting immediately
                    try {
                        Thread.sleep(1000); // Delay for 1 second
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
    }


    static class WatchThread implements Runnable {
        private String path;

        public WatchThread(String path) {
            this.path = path;
        }

        public void run() {
            try {
               // Create a WatchService
               WatchService watchService = FileSystems.getDefault().newWatchService();

               // Register the directory with the WatchService for specific events
               Path directory = Paths.get(path);
               directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                       StandardWatchEventKinds.ENTRY_MODIFY);

               System.out.println("Watching directory: " + directory);

               
                   // Start watching for events
                   while (true) {
                       WatchKey key;
                       try {
                           // Retrieve the next available WatchKey
                           key = watchService.take();
                       } catch (InterruptedException e) {
                           System.out.println("Interrupted while waiting for file events.");
                           return;
                       }
               
                       // Process the events associated with the WatchKey
                       for (WatchEvent<?> event : key.pollEvents()) {
                           WatchEvent.Kind<?> kind = event.kind();
                           Path eventPath = (Path) event.context();
               
                           // Log the event information
                           System.out.println(kind + ": " + eventPath);
                        
                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                           out.println("Send Log");
                           out.println(kind + ": " + eventPath);
                       }
               
                       // Reset the WatchKey
                       boolean valid = key.reset();
                       if (!valid) {
                           System.out.println("WatchKey no longer valid.");
                           break;
                       }
                   }

               // Close the WatchService
               watchService.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
