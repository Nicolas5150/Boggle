// packages and imports
package userInterface;

import java.awt.*;
import javax.swing.*;
import core.*;
import java.util.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;


/**
 *
 * @author Nicolas
 */

// extending and creating boggle with JFrame
public class BoggleUi extends JFrame {
    
    // private JFrame frame;
    private JMenuBar menuBar;
    private JMenu boggleMenu;
    private JMenuItem newGame;
    private JMenuItem exitGame;
    //extra- add high score to game
    private JMenuItem highScore;
    //extra - style for the color of the JOptionPane pop-up box
    //ImageIcon icon = new ImageIcon(getClass().getResource("JOptionPaneImage.jpg"));
    //ImageIcon icon = new ImageIcon("JOptionPaneImage.png");
    //ImageIcon logo;
    //logo = createImageIcon("JOptionPaneImage.jpg");
    UIManager UI=new UIManager();
    
    // boggle Board and the button 2D array
    private final Board boggleBoard;
    private final ArrayList dictionaryData;
    private JPanel bogglePanel;
    private final ArrayList diceButtons;
    private JButton[][] dieButton;
    
    // enter the words found
    private static JPanel wordsPanel;
    private JScrollPane scrollPane;
    private JTextPane textArea;
    private static JPanel shakeDicePanel;
    private JPanel timerPanel;
    private JLabel timeLabel;
    private JButton shakeDice;
    
    //assign3- new JPanel to show current word
    private static JPanel currentPanel;
    private JButton submitWord;
    private JLabel score;
    private JLabel current;
    //assign3- variables for keeping tack
    int scoreValue = 0;
    
    //assign3- timeCounter variables 
    private static int sec = 59;
    private static int min = 2;
    Timer timeCounter;
    private JButton altNewGame = new JButton("New Game");
      
    //assign3 - create the variables to string together letters chosen 
    String currentLetters = "";
    String newCurrentLetter = "";
    int rows;
    int cols;
    private boolean[][] dieButtonCheck;
    
    //assign4- arrayList for checking repeated words 
    ArrayList<String> addedWords = new ArrayList<>();
    
    //assign4 - variables needed
    ArrayList<String> addedWordsShuffled = new ArrayList<>();
    ArrayList<String> addedWordsCPU = new ArrayList<>();
    int scoreValueCPU = 0;
    
    //assign4 - style for strikethrough variables and its font
    Style primaryStyle;
    Style secondaryStyle;
    StyledDocument styleDocument; 
    Font textAreaFont;
    
    //extra- adding arraylist for value stored in file with highscore
    private final ArrayList scoreData;
    int highScoreValue = 0;
    //int highScoreValue = Integer.parseInt(scoreData.toString());
    //int highScoreValue = Integer.parseInt(scoreData);
    
    //popup icon
     ImageIcon logo;

    
    // pass in the board and go to the initComponents method
    public BoggleUi(Board inBoard, ArrayList dictionaryData, ArrayList scoreData)
    { 
       diceButtons = new ArrayList();       
       this.boggleBoard = inBoard;
       this.dictionaryData = dictionaryData;
       this.scoreData = scoreData;
       initComponents();
       //System.out.println(scoreData);
    }
    
    // method to create whole board
    private void initComponents()
    {     
    // layout manager of Border Layout     
    // initalization of the JFrame from this object BoggleUi
    this.setTitle("Boggle");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(800, 750);
    this.setMinimumSize(new Dimension(800, 750));
    //this.setMaximumSize(new Dimension(800, 750));
    this.setPreferredSize(new Dimension(800, 750));
    this.setLayout(new BorderLayout());
    logo = createImageIcon("JOptionPaneImage.jpg");
    
    //extra- causes all JOptionPane itmes to this color since declared here
    UI.put("OptionPane.background", (Color.decode("#9fe0dc")));
    UI.put("Panel.background", (Color.decode("#9fe0dc")));
    
    // initalization of the JMenuBar to add to the JFrame
    createMenu();
    // initalization of the Jpanel for the Boggle Dice
    setupBogglePanel();
    // initalization of the JPanel for the word 
    setupWordPanel();    
    //assign3 - initalization of the JPanel for the current aspects 
    setupCurrentPanel();
    
    // add items to the JFrame
    // frame.setJMenuBar(menuBar);
    this.add(bogglePanel, BorderLayout.WEST);
    this.add(wordsPanel, BorderLayout.CENTER);   
    //assign3- current pannel
    this.add(currentPanel, BorderLayout.SOUTH); 
    // need this to diplay all Ui text or nothing will show
    this.setVisible(true); 
    
    //assign3- calls the argument for miliseconds and an action listner 
    timeCounter= new Timer(1000, new timeCounter());
    }

    // method to create menu bar
    private void createMenu() 
    {
        // create Ui for the Menu Bar 
        menuBar = new JMenuBar();
        // use this line to set the background color to a hex value
        //http://www.javascripter.net/faq/rgbtohex.htm
        menuBar.setOpaque(true);
        menuBar.setBackground(Color.decode("#9fe0dc"));
        boggleMenu = new JMenu("Boggle");
        boggleMenu.setBackground(Color.decode("#9fe0dc"));
        boggleMenu.setMnemonic('B');
        menuBar.add(boggleMenu);
        newGame = new JMenuItem("New Game");
        exitGame = new JMenuItem("Exit");
        newGame.setMnemonic('N');
        exitGame.setMnemonic('E');
        //extra- add a high score menu item
        highScore = new JMenuItem("High Score");
        highScore.setMnemonic('H');
        boggleMenu.add(newGame);
        boggleMenu.add(highScore);
        boggleMenu.add(exitGame);
        //assign3 - add action listner to quit game when option is selected
        newGame.addActionListener(new newGame());
        highScore.addActionListener(new highScore());
        exitGame.addActionListener(new exitGame());
        this.setJMenuBar(menuBar);    
    }

    private void setupBogglePanel() 
    {
        // create the Ui for Boggle Board Pannel
        bogglePanel = new JPanel(new GridLayout(4, 4));
        bogglePanel.setMinimumSize(new Dimension(400, 400));
        //bogglePanel.setMaximumSize(new Dimension(400, 400));
        bogglePanel.setPreferredSize(new Dimension(400, 400));
        bogglePanel.setBorder(BorderFactory.createTitledBorder("Boggle Board"));
        //extra- use this line to set the background color to a hex value
        //http://www.javascripter.net/faq/rgbtohex.htm
        bogglePanel.setBackground(Color.decode("#dcc0cf"));
        
       //initalization of assignDieButtons getting array positions of dieButtons
        assignDieButtons();
        //extra- initalization to disable all die buttons at 0:00
        lockDieButtons();
    }

    // method for setting up the text box area, time box, and shake dice button
    private void setupWordPanel() 
    {
        // create Ui for the Words Pannel portion
        wordsPanel = new JPanel();
        wordsPanel.setMinimumSize(new Dimension(400, 400));
        wordsPanel.setPreferredSize(new Dimension(400, 400));
        wordsPanel.setBorder(BorderFactory.createTitledBorder
                                                         ("Enter Words Found"));
        wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.Y_AXIS));
        StyleContext sc = new StyleContext();
        final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
        // assign the textarea's min,max,pref so that it doesnt start moving
        // pass in the doc for the JTextPane's text area
        textArea = new JTextPane(doc);
        textAreaFont = new Font("Times New Roman", Font.PLAIN, 15);
        textArea.setFont(textAreaFont);
        textArea.setMinimumSize(new Dimension(400, 238));
        textArea.setMaximumSize(new Dimension(400, 238));
        textArea.setPreferredSize(new Dimension(400, 238));
        //textArea.setLineWrap(true);
        //extra- cause the textbox area to be a readonly area to the player
        textArea.setEditable(false);
        // assign4- create the fonts and strikethrough portion
        styleDocument =  textArea.getStyledDocument();
        primaryStyle = styleDocument.addStyle("Primary", null);
        secondaryStyle = styleDocument.addStyle("Secondary", primaryStyle);
        StyleConstants.setFontFamily(primaryStyle, "Times New Roman");
        StyleConstants.setFontSize(primaryStyle, 15);
        //assign4- secondary text for Strike Through
        StyleConstants.setStrikeThrough(secondaryStyle, true);

        // assign the scrollpane's min,max,pref so that it doesnt start moving
        scrollPane = new JScrollPane(textArea);
        scrollPane.setMinimumSize(new Dimension(400, 238));
        //scrollPane.setMaximumSize(new Dimension(400, 238));
        scrollPane.setPreferredSize(new Dimension(400, 238));
        scrollPane.setHorizontalScrollBarPolicy
                                       (JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy
                                     (JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        wordsPanel.add(scrollPane);
        wordsPanel.setBackground(Color.decode("#fdd8ac"));       
        
        // create Ui for timer and the panel
        timerPanel = new JPanel();
        timerPanel.setMinimumSize(new Dimension(400, 105));
        //timerPanel.setMaximumSize(new Dimension(400, 105));
        timerPanel.setPreferredSize(new Dimension(400, 105));
        timerPanel.setBorder(BorderFactory.createTitledBorder("Time Left"));
        timeLabel = new JLabel ("3:00");
        Font fontOne = new Font ("Times New Roman" , Font.PLAIN, 48);
        timeLabel.setFont(fontOne);
        timerPanel.add(timeLabel);
        timerPanel.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        ///timerPanel.setPreferredSize(new Dimension(75, 75));
        timerPanel.setBackground(Color.decode("#f8d3c7"));
                
        // create Ui for Shake dice
        shakeDicePanel = new JPanel();
        shakeDicePanel.setMinimumSize(new Dimension(1, 1));
        //shakeDicePanel.setMaximumSize(new Dimension(1, 1));
        shakeDicePanel.setPreferredSize(new Dimension(100, 100));
        shakeDicePanel.setBackground(Color.decode("#f8d3c7"));
        shakeDice = new JButton("Shake Dice");
        shakeDice.setMinimumSize(new Dimension(388, 132));
        //shakeDice.setMaximumSize(new Dimension(388, 132));
        shakeDice.setPreferredSize(new Dimension(388, 132));
        shakeDicePanel.add(shakeDice);
        shakeDicePanel.add(shakeDice, BorderLayout.SOUTH);
        shakeDice.addActionListener(new shakeDice());
        
        // add items to the pannel
        wordsPanel.add(scrollPane);
        wordsPanel.add(timerPanel);
        wordsPanel.add(shakeDicePanel); 
    }
    
    //assign3 - method to create current items
    private void setupCurrentPanel()
    {
       //assign3- create Ui for JPanel containing submit, score, and word
       currentPanel = new JPanel();
       currentPanel.setBorder(BorderFactory.createTitledBorder("Current Word"));
       currentPanel.setMinimumSize(new Dimension(180, 120));
       currentPanel.setPreferredSize(new Dimension(180,120));
       currentPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
       currentPanel.setBackground(Color.decode("#9fe0dc"));
        
       //assign3- create Ui for current word   
       current = new JLabel ("Word is: ");
       current.setBorder(BorderFactory.createTitledBorder("Current Word"));
       current.setMinimumSize(new Dimension(250, 90));
       current.setPreferredSize(new Dimension(250,90));
       Font fontTwo = new Font ("Times New Roman" , Font.PLAIN, 15);
       current.setFont(fontTwo);
        
       //assign3- create Ui button for submit word 
       // disable submitWord button so the user cant add words prior to shake
       submitWord = new JButton("Submit Word");
       submitWord.setPreferredSize(new Dimension(194, 52));
       submitWord.addActionListener(new submitWord());
       submitWord.setEnabled(false);      
        
       //assign3- create Ui for the current score
       score = new JLabel ("Score is: "+scoreValue);
       score.setBorder(BorderFactory.createTitledBorder("Current Score"));
       score.setMinimumSize(new Dimension(250, 90));
       score.setPreferredSize(new Dimension(250,90));
       Font fontThree = new Font ("Times New Roman" , Font.PLAIN, 15);
       score.setFont(fontThree);
        
       //assign3-  add items to the pannel
       currentPanel.add(current);
       currentPanel.add(submitWord);
       currentPanel.add(score);
    }
    
        //assign4- method to compare words the CPU "finds", figures out score,
        // and assign strike through
    private void wordsCompareCPU()
    {
        @SuppressWarnings("UnusedAssignment")
        int i=0;
        // check to make sure a word was actually found or the game will error
        //  a word needs to be found for the random generator to have a value
        if(addedWords.size() > 0)
        {
        // reset text area so strike through list and CPU words found are shown
        textArea.setText("");
        // get a random number to use to get the amount of words the CPU "found"
        Random rand = new Random ();
        int RandomNumberCPU = rand.nextInt(addedWords.size());

        // create a copied string arrayList of the words the user found
        // this is so when shuffled the original arrayList doesnt change order
        for(i = 0; i < addedWords.size(); i++)
        {
            addedWordsShuffled.add(addedWords.get(i));
        }

        // shuffle the array list of words the user found 
        // randomizes the words the CPU "finds" from addedWord string ArrayList
        // and selects based on the random number
        Collections.shuffle(addedWordsShuffled);
        for(i=0; i < RandomNumberCPU; i++)
        {
            // this will add the words to the CPU's "found" array list
            addedWordsCPU.add(addedWordsShuffled.get(i));
        }
        
        // print the words the CPU "found"
        try {
            styleDocument.insertString(styleDocument.getLength(),
                                            "Words CPU Found:\n", primaryStyle);
            } 
        catch (BadLocationException ex) 
            {
             //Logger.getLogger(JTextPaneTest.class.getName()).log
                                                     //(Level.SEVERE, null, ex);
            } 
        //textArea.setText(textArea.getText() +("Words CPU Found:\n"));
        for(i=0; i< addedWordsCPU.size(); i++)
        {
           textArea.setText(textArea.getText() +(addedWordsCPU.get(i)+"\n"));
        }
        // print the static title for users list of words
        try {
            styleDocument.insertString(styleDocument.getLength(),
                                            "Words You Found:\n", primaryStyle);
            } 
        catch (BadLocationException ex) 
            {
             //Logger.getLogger(JTextPaneTest.class.getName()).log
                                                     //(Level.SEVERE, null, ex);
            } 
        //textArea.setText(textArea.getText() +("Words You Found:\n"));
        // reset score value vor CPU so it doesnt keep ading each time
        scoreValueCPU=0;
        // compare the words the CPU "found" to the words the user found
        for(i=0; i< addedWords.size(); i++)
        {
            // comparing the addedWords string array obtained from the CPU and
            // comparing it to the CPU's (which is smaller) leading to the else
            // the CPU "found" and had stored in its array string
            if(addedWordsCPU.contains(addedWords.get(i)))
            {
                //Strike through the text
                try {
                     styleDocument.insertString(styleDocument.getLength(),
                                        addedWords.get(i)+"\n", secondaryStyle);
                }
                catch (BadLocationException ex) {
                    //Logger.getLogger(JTextPaneTest.class.getName()).log
                                                     //(Level.SEVERE, null, ex);
                } 
                // add length of the string arrayList at that specific index 
                // to a new local int variable
                int wordLength = addedWords.get(i).length();
                if(wordLength <= 4)
                
                {
                    scoreValueCPU += 1;
                }
                    else if(wordLength == 5)
                {
                    scoreValueCPU += 2;
                }
                    else if(wordLength == 6)
                {
                    scoreValueCPU += 3;
                }
                    else if(wordLength == 7)
                {
                    scoreValueCPU += 5;
                }
                    else if(wordLength >= 8)
                {
                    scoreValueCPU += 11;
                }
             }
            // add words that are not in CPU's array list (not stuck through)
            else {
                try {
                    styleDocument.insertString(styleDocument.getLength(),
                                          addedWords.get(i)+"\n", primaryStyle);
                }
                catch (BadLocationException ex) {
                    //Logger.getLogger(JTextPaneTest.class.getName()).log
                                                     //(Level.SEVERE, null, ex);
                } 
            }
        }
        // update users score (after subtracting CPU's score
        scoreValue -= scoreValueCPU;
        score.setText("Score is: "+scoreValue);
        // initalization high score value which will be 0 to start then 1st game
        computeHighScore();
        }
    }
    
    //assign4- method to disable all die buttons at start, newGame, and 0:00
    private void lockDieButtons()
    {
        for(int i=0; i<4; i++)
        {
          for(int j=0; j<4; j++)
          {
            dieButton[i][j].setEnabled(false);
          }
        }
    }
    
    //method assignDieButtons to gather the array positions of the die buttons 
    private void assignDieButtons()
    {        
        // use the array list of the method shake dice
        ArrayList<Die> dice = boggleBoard.shakeDice();
        Collections.shuffle(dice);
                
        //assign3- create a 2D array to hold button positions and the deslecter
        dieButton = new JButton[4][4];
        dieButtonCheck= new boolean[4][4];
        //assign3- loop through all dice to add to the array and its position
        //assign3- set dieButtons false on setup so just shakeDice button works
        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
              String dieLetter = dice.get(4*i +j).getLetter();
              dieButton[i][j] = new JButton(dieLetter); 
              bogglePanel.add(dieButton[i][j]);
              //assign3 - action listner 
              dieButton[i][j].addActionListener(new dieButton());
              dieButtonCheck[i][j] = false;
            }
        }
    }   
    
    //extra- method to keep a value for the high score which can be called from
    // the JMenuBar item 
    private void computeHighScore()
    {
        if(highScoreValue < scoreValue)
        {
            highScoreValue = scoreValue;
            //scoreData.write(Integer.toString(highScoreValue));
        }
    }
       
            public ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
        System.err.println("Couldn't find file: " + path);
        return null;
        }
        }
  
    //assign3 - action lister exitApp to exit app on button press causing popup
     class exitGame implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {

        //JOptionPane.showMessageDialog(null, "Time's Up", "title", JOptionPane.INFORMATION_MESSAGE, logo);
          int response = JOptionPane.showConfirmDialog(null, "Confirm to exit?", 
              "Exit?", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION, logo);
            
          if (response == JOptionPane.YES_OPTION)
              System.exit(0);	
        }
    }
     
    //assign3 - action listner newGame to start new game
    // this will remove text from textbox, reset timer, and enable Shake Dice
    class newGame implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            //assign3- stop the timer
            timeCounter.stop();
            //assign3- reset the static timer and code back to the 3 minute mark
            timeLabel.setText("3:00");
            Font fontOne = new Font ("Times New Roman" , Font.PLAIN, 48);
            timeLabel.setFont(fontOne);
            min=2;
            sec=59;          
            //assign3- reenable the shakeDice button but not submitWord button
            shakeDice.setEnabled(true);
            submitWord.setEnabled(false);
            //extra- set the altNewGame button to false to remove it from panel
            altNewGame.setVisible(false);
            //assign4- disable all die buttons at 0:00
            lockDieButtons();
            //assign4- method to clear the addedWords and CPU's string arraylist 
            addedWords.clear();
            addedWordsShuffled.clear(); 
            addedWordsCPU.clear();
        }
    }
    
    //extra- action lister highScore (coming from menu item High Score
    // will display the best score since launch of the game
     class highScore implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
           JOptionPane.showMessageDialog
                (null,"High Score: "+highScoreValue, 
                           "High Score", JOptionPane.INFORMATION_MESSAGE, logo);
                           
        }
    }
     //assign3 - action lister shakeDice 
     // randomizes dice, resets score
    class shakeDice implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
        
        //assign3- redo the Ui on the boggle pannel (words with buttons)
        bogglePanel.removeAll();
        bogglePanel.revalidate();
        
        //assign3- diable shakeDice button and reset the submit word button
        // reset the score back to 0 and the found word text
        // call the dieButtons to reset
        textArea.setText(""); 
        shakeDice.setEnabled(false);
        submitWord.setEnabled(true);
        timeCounter.start();
        scoreValue = 0;
        scoreValueCPU = 0;
        score.setText("Score is: "+scoreValue);
        currentLetters = "";
        current.setText("Word is: "+currentLetters);
        assignDieButtons();
        
    }
    }   
    //assign3 - timeCounter for 3 min
    // count down from 3 min, when it stops "Game Over" appears in timeLabel
    // disable the submit button until shakeDice button is clicked
    class timeCounter implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            //assign3 - 3 min counter and its printing statment
            String s = String.format(" "+min+ ":" + "%02d", sec);
            timeLabel.setText(s);
            sec -=1;
            if(sec < 0)
            {
                sec=59;
                if(min == 0)
                {
                    //assign3 - stop time, GAME OVER, disable sumbitWord and die
                    timeCounter.stop();
                    JOptionPane.showMessageDialog
                           (null,"<html>THE GAME IS OVER<br "
                                                  + "/>CHECKING WORDS!</html>");
                    timeLabel.setText("THE GAME IS OVER\n");
                    Font fontFour = 
                                  new Font ("Times New Roman" , Font.PLAIN, 24);
                    timeLabel.setFont(fontFour);
                    submitWord.setEnabled(false);
                    //assign4- disable all die buttons at 0:00
                    lockDieButtons();
                    
                    //extra- create new JButton for an alt way to get new game
                     altNewGame = new JButton("New Game");
                     altNewGame.setMinimumSize(new Dimension(375, 80));
                     altNewGame.setPreferredSize(new Dimension(375, 80));
                     Font fontThree = 
                                  new Font ("Times New Roman" , Font.PLAIN, 15);
                     altNewGame.setFont(fontThree);
                     timerPanel.add(altNewGame); 
                     altNewGame.addActionListener(new newGame()); 
                     wordsCompareCPU();
             
                }
            min--;
            }              
        }
    }
    
    //assign3 - action lister submitWord
    // validate word from dict, clear old word and add new to currentWord JLabel
    // assign any points and clear the current word for new input
    class submitWord implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            int flag = 0;
            if(currentLetters.equals(""))
            {
                flag = 2;
            }
            for(int i=0; i< dictionaryData.size(); i++)
            {
                //assign3- if the word matches assign points accordingly
                if(currentLetters.equalsIgnoreCase
                                  (dictionaryData.get(i).toString()))
                {
                    flag = 1;
                    int wordLength = currentLetters.length();
                    //assign4- check for words that are repeated
                    if(addedWords.contains(currentLetters))
                    {
                    JOptionPane.showMessageDialog
                        (null,"Word was found \nResetting current word!", 
                            "Repeated Word", JOptionPane.INFORMATION_MESSAGE, logo);
                    //System.out.print
                    //("Word was found already ... Resetting current word!\n");
                   break;
                    }
                    if(wordLength <= 2)
                        {
                            JOptionPane.showMessageDialog
                            (null,"Word is too short \nResetting current word!", 
                            "Too Short", JOptionPane.INFORMATION_MESSAGE, logo);
                            //System.out.print
                         //("Word is too short ... Resetting current word!\n");
                        }
                    else if(wordLength <= 4)
                    {
                            scoreValue += 1;
                            textArea.setText(textArea.getText() +
                                                      (""+currentLetters+"\n"));
                            addedWords.add(currentLetters);
                    }
                    else if(wordLength == 5)
                    {
                            scoreValue += 2;
                            textArea.setText(textArea.getText() +
                                                      (""+currentLetters+"\n"));
                            addedWords.add(currentLetters);
                    }
                    else if(wordLength == 6)
                    {
                            scoreValue += 3;
                            textArea.setText(textArea.getText() +
                                                      (""+currentLetters+"\n"));
                            addedWords.add(currentLetters);
                    }
                    else if(wordLength == 7)
                    {
                            scoreValue += 5;
                            textArea.setText(textArea.getText() +
                                                      (""+currentLetters+"\n"));
                            addedWords.add(currentLetters);
                    }
                    else if(wordLength >= 8)
                    {
                            scoreValue += 11;
                            textArea.setText(textArea.getText() +
                                                      (""+currentLetters+"\n"));
                            addedWords.add(currentLetters);
                    }
                    break;
                }
            }  
            //assign3- if word isnt found b/c flag isnt changed word is invalid
            if(flag == 0)
            {
                JOptionPane.showMessageDialog
                       (null,"Your word is invalid \nResetting current word!", 
                              "Invalid", JOptionPane.INFORMATION_MESSAGE, logo);
              //System.out.print
                          //("Your word is invalid \nResetting current word!");
            }
            if(flag == 2)
            {
                JOptionPane.showMessageDialog
                    (null,"No letters were selected!", "No Selection Made", 
                                         JOptionPane.INFORMATION_MESSAGE, logo);
                //System.out.print("No letters were selected");
            }
            //assign3- nonetheless current word resets and possible points added
            currentLetters = "";
            current.setText("Word is: "+currentLetters);
            score.setText("Score is: "+scoreValue);
            
            //assign3- loop resets the dieButtons for interactivity again
            for(int i = 0; i<4; i++ )
            {
                for(int j = 0; j<4; j++)
                {
                    dieButton[i][j].setEnabled(true);
                    dieButtonCheck[i][j] = false;
                }
            }
        }
    }
    
    //assign3 - action lister dieButton
    // update current word JLabel of current word concatenating to exsisting txt
    // cause only the selcted leter and its surrounding letters to be selected
    class dieButton implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            JButton b;
            //assign3- cause dieButton presed to be assigned to temp variable
            newCurrentLetter = e.getActionCommand();
            b = (JButton)e.getSource();
            // add selected letter to the current letter string
            currentLetters += newCurrentLetter;
            current.setText("Word is: "+currentLetters);
            
            //assign3- loops to cause slected button to become deselected
            for(int i = 0; i<4; i++ )
            {
                for(int j = 0; j<4; j++)
                {
                    dieButton[i][j].setEnabled(false);
                    if(b == dieButton[i][j]) 
                    {
                        rows = i;
                        cols = j;
                        dieButtonCheck[i][j] = true;
                    }
                }
            }
            for(int i = rows -1; i <= rows +1; i++ )
            {
                for(int j = cols -1; j <= cols +1; j++ )
                {
                    if( i>=0 && i<4 && j>=0 && j<4) 
                    {
                        dieButton[i][j].setEnabled(true);
                        if(dieButtonCheck[i][j])
                            dieButton[i][j].setEnabled(false);
                    }
                }
            }
        }
    }
// end of code
}