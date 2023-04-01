import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


public class DictionaryApp extends JPanel implements ListSelectionListener, ItemListener, DocumentListener, ActionListener{

    // Left pannel components
    private JTextField searchBox;
    private JList<String> wordList;
    private JButton addWordButton;
    private JButton deleteWordButton;

    // Right pannel components
    private JComboBox languageCombobox;
    private JTextArea meaningArea;
    private JButton addToFavoriteButton;

    // Variable
    private static Map<String, String> EngVieDic;
    private static Map<String, String> VieEngDic;
    private static Map<String, String> FavDic;


    public DictionaryApp() throws ParserConfigurationException, SAXException, IOException{
        super();
        setPreferredSize(new Dimension(1000,600));
        setLayout(new BorderLayout());

        // Create Left panel
        JPanel leftPanel = createLeftPanel();
        add(leftPanel,BorderLayout.WEST);

        // Create Right panel
        JPanel rightPanel = createRightPanel();
        add(rightPanel,BorderLayout.CENTER);

        // Set WordList
        Bus.setWordList(wordList, EngVieDic);
    }

    // Create Left panel
    public JPanel createLeftPanel() throws ParserConfigurationException, SAXException, IOException{

        // Create and Set size
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(400,600));

        // Create top section of Left panel
        JPanel topPanel = new JPanel(new BorderLayout());
        leftPanel.add(topPanel, BorderLayout.NORTH);

        JLabel searchLabel = new JLabel("Search");
        searchLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 7, 10));
        topPanel.add(searchLabel, BorderLayout.WEST);

        searchBox = new JTextField(20);
        searchBox.getDocument().addDocumentListener(this);
        topPanel.add(searchBox,BorderLayout.CENTER);

        // Create middle section of Left panel
        JScrollPane scrollPane = new JScrollPane();
        wordList = new JList<>();
        wordList.addListSelectionListener(this);
        scrollPane.setViewportView(wordList);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Create bottom section of Left panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        leftPanel.add(bottomPanel,BorderLayout.SOUTH);

        addWordButton = new JButton("Add new word");
        addWordButton.addActionListener(this);
        bottomPanel.add(addWordButton);

        deleteWordButton = new JButton("Delete word");
        deleteWordButton.addActionListener(this);
        bottomPanel.add(deleteWordButton);

        return leftPanel;
    }

    // Create Right panel
    public JPanel createRightPanel(){

        JPanel rightPanel = new JPanel(new BorderLayout());

        // Create top section of right panel
        JPanel topPanel = new JPanel(new BorderLayout());
        rightPanel.add(topPanel,BorderLayout.NORTH);

        JLabel selectLabel = new JLabel("Choose mode:");
        selectLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        topPanel.add(selectLabel, BorderLayout.WEST);

        languageCombobox = new JComboBox<>(new String[] {"English to Vietnamese", "Vietnamese to English", "Favorite words", "Statistics"});
        languageCombobox.addItemListener(this);
        topPanel.add(languageCombobox, BorderLayout.CENTER);

        // Create the middle section of right panel
        meaningArea = new JTextArea(20, 40);
        meaningArea.setEditable(false);
        meaningArea.setLineWrap(true); // enable line wrap
        meaningArea.setWrapStyleWord(true); // wrap on word boundary
        JScrollPane meaningScrollPane = new JScrollPane(meaningArea);
        rightPanel.add(meaningScrollPane, BorderLayout.CENTER);

        // Create the bottom section of right panel
        JPanel bottomPanel = new JPanel(new FlowLayout());
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        addToFavoriteButton = new JButton("Add to favorite list");
        addToFavoriteButton.addActionListener(this);
        bottomPanel.add(addToFavoriteButton);

        return rightPanel;
    }

    // WordList Event
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()){

            String selectedLanguage = (String) languageCombobox.getSelectedItem(); 
            String selectedWord = wordList.getSelectedValue();
            String meaning = "";

            if (selectedLanguage.equals("English to Vietnamese")){
                meaning = EngVieDic.get(selectedWord);
            } 

            else if (selectedLanguage.equals("Vietnamese to English")){
                meaning = VieEngDic.get(selectedWord);
            } 

            else if (selectedLanguage.equals("Favorite words")){
                meaning = FavDic.get(selectedWord);
            }

            meaningArea.setText(meaning);

            // Save the word in history to make statistic
            try {
                if (wordList.getSelectedValue() != null){
                    Bus.saveHistory(selectedWord);
                }
            } catch (IOException e1) {}
        }
    }

    // ComboBox event
    @Override
    public void itemStateChanged(ItemEvent e) {

        if (e.getStateChange() == ItemEvent.SELECTED){

            String selectedLanguage = (String) languageCombobox.getSelectedItem();

            if (selectedLanguage.equals("English to Vietnamese")) {
                try {
                    Bus.setWordList(wordList, EngVieDic);
                } catch (ParserConfigurationException | SAXException | IOException e1) {e1.printStackTrace();}

                addToFavoriteButton.setEnabled(true);
            } 

            else if (selectedLanguage.equals("Vietnamese to English")) {
                try {
                    Bus.setWordList(wordList, VieEngDic);
                } catch (ParserConfigurationException | SAXException | IOException e1) { e1.printStackTrace();}

                addToFavoriteButton.setEnabled(true);
            } 

            else if (selectedLanguage.equals("Favorite words")) {
                try {
                    Bus.setWordList(wordList, FavDic);
                } catch (ParserConfigurationException | SAXException | IOException e1) {e1.printStackTrace();}

                // Disable AddToFavButton
                addToFavoriteButton.setEnabled(false);
            }

            else if (selectedLanguage.equals("Statistics")){
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(languageCombobox);
                
                try {
                    Bus.showStatistics(frame);
                    languageCombobox.setSelectedIndex(0); // advoid bug
                } catch (IOException | ParseException e1) {e1.printStackTrace();}
            }

            searchBox.setText("");
        }
    }

    // Searchbox event
    @Override
    public void insertUpdate(DocumentEvent e) {

        String searchText = searchBox.getText().toLowerCase();
        String selectedLanguage = (String) languageCombobox.getSelectedItem();
        
        if (selectedLanguage.equals("English to Vietnamese")) {
            try {
                Bus.updateSearchList(searchText, wordList, EngVieDic);
            } catch (ParserConfigurationException | SAXException | IOException e1) {e1.printStackTrace();}
        } 

        else if (selectedLanguage.equals("Vietnamese to English")) {
            try {
                Bus.updateSearchList(searchText, wordList, VieEngDic);
            } catch (ParserConfigurationException | SAXException | IOException e1) {e1.printStackTrace();}
        } 
        
        else if (selectedLanguage.equals("Favorite words")) {
            try {
                Bus.updateSearchList(searchText, wordList, FavDic);
            } catch (ParserConfigurationException | SAXException | IOException e1) {e1.printStackTrace();}
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {

        String searchText = searchBox.getText().toLowerCase();
        String selectedLanguage = (String) languageCombobox.getSelectedItem();
        
        if (selectedLanguage.equals("English to Vietnamese")) {
            try {
                Bus.updateSearchList(searchText, wordList, EngVieDic);
            } catch (ParserConfigurationException | SAXException | IOException e1) {e1.printStackTrace();}
        } 
        
        else if (selectedLanguage.equals("Vietnamese to English")) {
            try {
                Bus.updateSearchList(searchText, wordList, VieEngDic);
            } catch (ParserConfigurationException | SAXException | IOException e1) {e1.printStackTrace();}
        } 
        
        else if (selectedLanguage.equals("Favorite words")) {
            try {
                Bus.updateSearchList(searchText, wordList, FavDic);
            } catch (ParserConfigurationException | SAXException | IOException e1) { e1.printStackTrace();}
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

        String searchText = searchBox.getText().toLowerCase();
        String selectedLanguage = (String) languageCombobox.getSelectedItem();
        
        if (selectedLanguage.equals("English to Vietnamese")) {
            try {
                Bus.updateSearchList(searchText, wordList, EngVieDic);
            } catch (ParserConfigurationException | SAXException | IOException e1) { e1.printStackTrace();}
        } 
        
        else if (selectedLanguage.equals("Vietnamese to English")) {
            try {
                Bus.updateSearchList(searchText, wordList, VieEngDic);
            } catch (ParserConfigurationException | SAXException | IOException e1) {e1.printStackTrace();}
        } 
        
        else if (selectedLanguage.equals("Favorite words")) {
            try {
                Bus.updateSearchList(searchText, wordList, FavDic);
            } catch (ParserConfigurationException | SAXException | IOException e1) {e1.printStackTrace();}
        }
    }

    // Buttons event
    @Override
    public void actionPerformed(ActionEvent e) {

        // AddWordButton event
        if (addWordButton == e.getSource()){

            String selectedLanguage = (String) languageCombobox.getSelectedItem();
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(addWordButton);

            if (selectedLanguage.equals("English to Vietnamese")){
                Bus.addNewWord(currentFrame, EngVieDic, wordList, selectedLanguage);
            } 
            
            else if (selectedLanguage.equals("Vietnamese to English")){
                Bus.addNewWord(currentFrame, VieEngDic, wordList, selectedLanguage);
            } 
            
            else if (selectedLanguage.equals("Favorite words")){
                Bus.addNewWord(currentFrame, FavDic, wordList, selectedLanguage);
            }
        }
    
        // DeleteWordButton event
        if (deleteWordButton == e.getSource()){

            int selectedIndex = wordList.getSelectedIndex();

            if (selectedIndex != -1){

                String selectedLanguage = (String) languageCombobox.getSelectedItem();
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(deleteWordButton);
                String [] selectedWords = wordList.getSelectedValuesList().toArray(new String[0]);
                

                if (selectedLanguage.equals("English to Vietnamese")){
                    try {
                        Bus.deleteWord(currentFrame, selectedWords, EngVieDic, wordList, selectedLanguage);
                    } catch (ParserConfigurationException | SAXException | IOException e1) {e1.printStackTrace();}
                } 
                
                else if (selectedLanguage.equals("Vietnamese to English")){
                    try {
                        Bus.deleteWord(currentFrame, selectedWords, VieEngDic, wordList, selectedLanguage);
                    } catch (ParserConfigurationException | SAXException | IOException e1) {e1.printStackTrace();}
                } 
                
                else if (selectedLanguage.equals("Favorite words")){
                    try {
                        Bus.deleteWord(currentFrame, selectedWords, FavDic, wordList, selectedLanguage);
                    } catch (ParserConfigurationException | SAXException | IOException e1) {e1.printStackTrace();}
                }
            }
        }

        // AddToFavoriteButton event
        if (addToFavoriteButton == e.getSource()){
            
            int selectedIndex = wordList.getSelectedIndex();

            if (selectedIndex != -1){

                String selectedLanguage = (String) languageCombobox.getSelectedItem();
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(addToFavoriteButton);
                String [] selectedWords = wordList.getSelectedValuesList().toArray(new String[0]);

                if (selectedLanguage.equals("English to Vietnamese")){
                    Bus.addToFavorite(currentFrame, selectedWords, EngVieDic, FavDic);
                } 
    
                else if (selectedLanguage.equals("Vietnamese to English")){
                    Bus.addToFavorite(currentFrame, selectedWords, VieEngDic, FavDic);
                } 
            }
        }
    }

    private static void createAndShowGUI() throws ParserConfigurationException, SAXException, IOException{
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Dictionary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DictionaryApp newContentPane = new DictionaryApp();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        EngVieDic = Bus.LoadEngVieDictionary();
        VieEngDic = Bus.LoadVieEngDictionary();
        FavDic = Bus.LoadFavDictionary();
        createAndShowGUI();
    }
}
