import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class Bus {

    public static String unicodeToASCII(String s) {
        String s1 = Normalizer.normalize(s, Normalizer.Form.NFKD);
        String regex =
        "[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+";
        String s2 = null;
        try {
            s2 = new String(s1.replaceAll(regex, "").getBytes("ascii"),
            "ascii");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
            return s2;
    }

    public static Map<String, String> LoadFavDictionary() throws ParserConfigurationException, SAXException, IOException{
        return Data.readXMLFile("files/Fav.xml");
    }

    public static Map<String, String> LoadEngVieDictionary() throws ParserConfigurationException, SAXException, IOException{
        return Data.readXMLFile("files/Anh_Viet.xml");
    }

    public static Map<String, String> LoadVieEngDictionary() throws ParserConfigurationException, SAXException, IOException{
        return Data.readXMLFile("files/Viet_Anh.xml");
    }


    public static void setWordList(JList<String> myList, Map<String, String> myDic) throws ParserConfigurationException, SAXException, IOException{
        DefaultListModel<String> listModel = new DefaultListModel<>();

        String[] listWords = myDic.keySet().toArray(new String[0]);
        Arrays.sort(listWords);

        for (String word : listWords) {
            listModel.addElement(word);
        }

        myList.setModel(listModel);
    }

    public static void updateSearchList(String Keyword, JList<String> SearchDic, Map<String, String> myDic) throws ParserConfigurationException, SAXException, IOException{
        DefaultListModel listModel = new DefaultListModel<>();
        String[] listWords = {};

        if (Keyword.equals("")){
            setWordList(SearchDic, myDic);
        } 

        else {
            for (String key : myDic.keySet()){
                if (key.toLowerCase().contains(Keyword)){
                    listWords = Arrays.copyOf(listWords, listWords.length + 1);
                    listWords[listWords.length - 1] = key;
                }
            }
            
            Arrays.sort(listWords);

            for (String word : listWords) {
                listModel.addElement(word);
            }
            
            SearchDic.setModel(listModel);
        }
    }

    public static void addNewWord(JFrame frame, Map<String, String> myDic, JList<String> wordsList, String DicMode){

        // 1. Show Dialog

        // Create Dialog
        JDialog dialog = new JDialog (frame, "Add new word", true);

        // Add components to the dialog
        JPanel panel = new JPanel(new GridLayout(2, 2));

        JLabel wordLabel = new JLabel("Word: ");
        wordLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panel.add(wordLabel);

        JTextField wordField = new JTextField();
        panel.add(wordField);
    
        JLabel meaningLabel = new JLabel("Meaning: ");
        meaningLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panel.add(meaningLabel);

        JTextArea meaningField = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(meaningField);
        meaningField.setLineWrap(true);
        panel.add(scrollPane);

        dialog.add(panel, BorderLayout.CENTER);

        // Add an "OK" button to the dialog
        JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordField.getText();
                String existedWord = "";
                String meaning = meaningField.getText();
                Boolean isAddable = true;
                
                for (String key : myDic.keySet()){
                    if (key.toLowerCase().equals(word.toLowerCase())){
                        isAddable = false;
                        existedWord = key;
                        break;
                    }
                }

                if (!isAddable){ // Word has been existed

                    // Show dialog to ask for user confirmation

                    int confirmation = JOptionPane.showConfirmDialog(frame, "The word \"" + existedWord + "\"has been existed. \n Are you sure to add new word? (Old word will be deleted)", "Confirm", JOptionPane.OK_CANCEL_OPTION);
        
                    if (confirmation == JOptionPane.OK_OPTION) {
                        // User still want to add
                        
                        // Delete old word
                        myDic.remove(existedWord);

                        // Add to Map 
                        if (!word.equals("") && !meaning.equals("")){
                            myDic.put(word,meaning);
                        }
 
                        // update UI to show the change
                        try {
                            setWordList(wordsList, myDic);
                        } catch (ParserConfigurationException | SAXException | IOException e1) {e1.printStackTrace();}

                        // 3. Save change to File

                        try {
                            Data.writeXMLFile(myDic,DicMode);
                        } catch (ParserConfigurationException | TransformerException e1) {e1.printStackTrace();}
                        
                        
                        dialog.dispose(); // Close the dialog

                        // Show a message to inform the user that the word has been deleted
                        JOptionPane.showMessageDialog(frame, "The word \"" + word + "\" has been added.", "Word Added", JOptionPane.INFORMATION_MESSAGE);
                    }

                    else {
                        // JOptionPane.CANCEL_OPTION
                        dialog.dispose(); // Close the dialog
                    }
                }
            }
        });
        dialog.add(okButton, BorderLayout.SOUTH);


        // Set the dialog size and show it
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    public static void deleteWord(JFrame frame, String[] DeleteWords, Map<String, String> myDic, JList<String> myList, String DicMode) throws ParserConfigurationException, SAXException, IOException{
        
        int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure to delete this word?", "Confirm Deletion", JOptionPane.OK_CANCEL_OPTION);
        
        if (confirmation == JOptionPane.OK_OPTION) {

            // Remove word from Map
            for (String word : DeleteWords){
                myDic.remove(word);
            }
            
            // Update JList to show the change 
            setWordList(myList, myDic);
            
            // Show a message to inform the user that the word has been deleted
            JOptionPane.showMessageDialog(frame, "The word(s) have been deleted.", "Word Deleted", JOptionPane.INFORMATION_MESSAGE);
        
            // Save change to File
            try {
                Data.writeXMLFile(myDic,DicMode);
            } catch (ParserConfigurationException | TransformerException e1) {e1.printStackTrace();}
        }

        else {
            // JOptionPane.CANCEL_OPTION: Do nothing
        }
    }

    public static void addToFavorite(JFrame frame, String[] words, Map<String, String> sourceDic, Map<String, String> myDic){

        String meaning = "";

        // Add the word to favorite list
        for(String word : words){
            meaning = sourceDic.get(word);
            myDic.put(word, meaning);
        }

        // Show a message to inform the user that the word has been added
        JOptionPane.showMessageDialog(frame, "The word(s) have been added to Favorite list.", "Word Added", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showStatistics(JFrame frame) throws IOException, ParseException{
        // Create table model
        DefaultTableModel model = new DefaultTableModel();

        // Create a dialog
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setTitle("Statistics");

        // Create a top section for dialog
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel fromLabel = new JLabel("From date:");
        JLabel toLabel = new JLabel("To date:");

        // Create 1st datePicker
        SpinnerDateModel fromModel = new SpinnerDateModel();
        JSpinner fromDatePicker = new JSpinner(fromModel);

        // Create 2nd datePicker 
        SpinnerDateModel toModel = new SpinnerDateModel();
        JSpinner toDatePicker = new JSpinner(toModel);

        fromDatePicker.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    getModel(fromDatePicker, toDatePicker, model);
                } catch (ParseException | IOException e1) {e1.printStackTrace();}
            }
            
        });

        JSpinner.DateEditor fromEditor = new JSpinner.DateEditor(fromDatePicker, "dd-MM-yyyy");
        fromDatePicker.setEditor(fromEditor);

        
        toDatePicker.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    getModel(fromDatePicker, toDatePicker, model);
                } catch (ParseException | IOException e1) { e1.printStackTrace();}
            }

        });

        JSpinner.DateEditor toEditor = new JSpinner.DateEditor(toDatePicker, "dd-MM-yyyy");
        toDatePicker.setEditor(toEditor);
        
        // Add component to Layout
        topPanel.add(fromLabel);
        topPanel.add(fromDatePicker);
        topPanel.add(toLabel);
        topPanel.add(toDatePicker);
        dialog.add(topPanel, BorderLayout.NORTH);

        // Create main section for dialog
        JPanel mainPanel = new JPanel();
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);

        

        // Add column headers
        model.addColumn("Words");
        model.addColumn("Times");

        // Load data to model
        
        getModel(fromDatePicker, toDatePicker, model);

        // Add model to table
        table.setModel(model);
        
        // Add table to Layout
        mainPanel.add(scrollPane);
        dialog.add(mainPanel, BorderLayout.CENTER);

        // Create buttons
        JPanel bottomPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton clearButton = new JButton("Clear history");

        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }

        });
        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Data.clearTxtFile("files/log.txt");
                // Show a message to inform the user that the word has been deleted
                JOptionPane.showMessageDialog(frame, "The history has been clear.", "History Clear", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            }
        });

        bottomPanel.add(okButton);
        bottomPanel.add(clearButton);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        // Set the dialog size and show it
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    public static void saveHistory(String word) throws IOException{
        if (!word.equals("null")){
            Date today = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formatDate =  dateFormat.format(today);
            
            String msg = formatDate + "?" + word + "\n";
            Data.writeToTxtFile(msg, "files/log.txt");
        }
    }

    public static DefaultTableModel getModel(JSpinner fromDatePicker, JSpinner toDatePicker, DefaultTableModel model) throws ParseException, IOException{
        model.setRowCount(0);

        // Load data to model
        String[] lines;
        String[] dates = new String[100];
        String[] words = new String[100];
        Map<String, Integer> data = new HashMap<>();
        
        // Get date from spinner mm-dd-yyyy then convert back to dd-mm-yyyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date fromDate = (Date) fromDatePicker.getValue();
        String formattedDate = dateFormat.format(fromDate);
        Date realFromDate = dateFormat.parse(formattedDate);

        Date toDate = (Date) toDatePicker.getValue();
        String formattedDate2 = dateFormat.format(toDate);
        Date realToDate = dateFormat.parse(formattedDate2);

        lines = Data.readTxtFile("files/log.txt");

        // From String[] lines, load data to specific variable
        for (int i = 0 ; i < lines.length && lines[i] != null; i++){

            if (i == dates.length || i == words.length){
                dates = Arrays.copyOf(dates, dates.length * 2); // Double the size of the array if it gets full
                words = Arrays.copyOf(words, words.length * 2); // Double the size of the array if it gets full
            }

            String parts[] = lines[i].split("\\?");

            if (parts.length == 2){
                dates[i] = parts[0];
                words[i] = parts[1];
            }
        }

        // Create HashMap
        for (int i = 0; i < dates.length && dates[i] != null; i++){
            Boolean isExisted = false;
            Date date = dateFormat.parse(dates[i]);
            
            if (date.compareTo(realFromDate) >= 0 && date.compareTo(realToDate) <=0 ){

                for (String key : data.keySet()){
                    if (words[i].equals(key)){
                        isExisted = true;
                        break;
                    }
                }

                if (!isExisted){
                    data.put(words[i], 1);
                }else{
                    Integer count = data.get(words[i]);
                    count++;
                    data.put(words[i], count);
                }
            }
        }

        // Put data to model
        for (String key : data.keySet()){
            model.addRow(new Object[] {key, data.get(key)});
        }

        return model;
    }
}
