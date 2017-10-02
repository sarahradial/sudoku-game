//cs 230 final project
/* Home class: Contains the main menu and controls the various buttons & panels
 * Primary contributers: Barakah Quader, Emily Wang, and Sarah Yan
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.net.URL.*;
import java.net.MalformedURLException.*;


public class Home extends JPanel{
  
  /*create labels, instructions buttons, and sets up home with panels
   */
  private JLabel titleLabel; 
  private JButton instructions; 
  private JButton playGame; 
  private JButton backToHome; 
  private ImageIcon xImg; 
  private JPanel centerPanel; 
  private JPanel northPanel; 
  private JPanel southPanel;
  private JLabel colorMenuLabel;
  
  /*creates buttons for levels 
   * creates instance boolean and integer variables 
   * creates an object of NumberPlace and ColorPlace
   */
  private JButton easyLevel; 
  private JButton medLevel;  
  private JButton hardLevel; 
  private JButton regularMode; 
  private JButton colorMode; 
  private boolean choseRegularMode; 
  private boolean choseColorMode; 
  private int levelReveal; 
  private JButton undo; 
  private JButton redo; 
  private NumberPlace numPlace; 
  private ColorPlace colorPlace; 
  
  /* Constructor for Home class and sets up the home page
   * Initialize instance variables
   */
  public Home() {
    
    levelReveal = 0; 
    
    setLayout(new BorderLayout(0,0)); 
    
    xImg = createImageIcon("https://7585b97a-a-b6cc57a5-s-sites.googlegroups.com/a/wellesley.edu/temp-for-cs/home/why.gif?attachauth=ANoY7coBk2mBAFUxWpktkiv3JmwD1-YH-8xlyZDNfCH2G67dDP9sSWcLLS3p0Ea0GUlL8sCDczs7ZM9VrfpneNUt2ld542Fk0wfefYhA_pZpmp3Uxm3kU6HsHmfjV1dy1kAygZJLvakdGPsCo-AZQHaDmKIGRixkDottMCrrRHG9zV07jATEcMZTvDvbRw5wh3zpqTzUQAOclyOjfDKQomz9bKvtatw7wQ%3D%3D&attredirects=0", "an X image"); //using .png, create an ImageIcon object to put in jLabel
    titleLabel = new JLabel("", xImg, JLabel.CENTER); 
    titleLabel.setIcon(xImg); 
    
    setBackground(Color.black); 
    centerPanel = makeTitlePanel(); 
    add(centerPanel, BorderLayout.CENTER); 
    northPanel = makeNorthPanel(); 
    add(northPanel, BorderLayout.NORTH); 
    southPanel = makeSouthPanel();
    
  }
  
  /* Private helper method to create south panel (with color menu)
   * ONLY called with ColorPlace (not regular NumberPlace)
   */
  private JPanel makeSouthPanel() {
    
    JPanel southPanel = new JPanel(); 
    southPanel.setBackground(Color.black); 
    ImageIcon instructImg = createImageIcon("https://7585b97a-a-b6cc57a5-s-sites.googlegroups.com/a/wellesley.edu/temp-for-cs/home/panel.gif?attachauth=ANoY7coucGxaRWoq9hcQPM0h_lqdtZPFzN3g0zPHsmHo7nUfWCW2dFGCJwUN6CKbgu0kTQhCoZNbgJTuBJoVQAHClAKsmcwtdlE7e2hZ4dnElRiFgWRdPHUJ3Ta6JA0sD5fXxVEGDl8GFg9fsd1UTIelDs5sSeHLo46ADVusw9zF5WHAutJSxR3Vr2R_o53GV9e-rFX_pPB5H1m5G1UdhypelAl7ZHU6Hg%3D%3D&attredirects=0", "");
    colorMenuLabel = new JLabel("", instructImg, JLabel.CENTER);
    colorMenuLabel.setForeground(Color.white);
    southPanel.add(colorMenuLabel);    
    
    return southPanel;
  }
  
  /*creates a remove SouthPanel to hide the color panel at the bottom of the screen 
   */
  private void removeSouthPanel() {
    southPanel.remove(colorMenuLabel);
  }
  
  /* Private helper method to create north panel (with play game & instructions button) 
   */
  private JPanel makeNorthPanel() {

    JPanel northPanel = new JPanel();
    northPanel.setBackground(Color.black);
    

    instructions = new JButton("Instructions");
    instructions.setFont(new Font("Serif", Font.ITALIC, 20));
    

    playGame = new JButton("Play Game");
    playGame.setFont(new Font("Serif", Font.ITALIC, 20));
    
    
    

    playGame.addActionListener(new ButtonListener());
    instructions.addActionListener(new ButtonListener());
    northPanel.add(playGame);
    northPanel.add(instructions);
    
    return northPanel;
  }
  
  /* Private helper method to create center panel with title image
   */
  private JPanel makeTitlePanel () {

    JPanel titlePanel = new JPanel();
    titlePanel.setBackground(Color.black);
    titlePanel.add(titleLabel); 
    
    return titlePanel;
  }
  
  /* Private helper method to create ImageIcon with the title image
   * Taken from TicTacToePanel lab solutions
   */
  private static ImageIcon createImageIcon(String path, String description) {
    try {
      URL img = new URL(path);
      java.net.URL imgURL = img;
      if (imgURL != null) 
        return new ImageIcon(imgURL, description);
      
      
    } catch (MalformedURLException ex){
      System.out.println("URL error");
    }
    return null;
    
  }
  
  /* Private helper method to switch between title sub-panel and number/color place board sub-panel in the center panel
   * Takes in the new JPanel as a parameter
   */
  private void switchCenterPanel(JPanel panel)
  {
    remove(centerPanel); 
    centerPanel = panel; 
    add(panel, BorderLayout.CENTER); 
    revalidate(); 
  }
  
  /* Private helper method to respond to button push events
   */
  private class ButtonListener implements ActionListener {
    
    public void actionPerformed (ActionEvent event) {
      if (event.getSource() == playGame) { 
        switchCenterPanel(makeModePanel());
        removeSouthPanel();
      }
      if (event.getSource() == instructions) 
      {  
        JOptionPane.showMessageDialog(null, "NumberPlace Instructions\n1. find an empty cell\n2. look at all the numbers in that box, row, and column\n3. select a number 1- 9 that is not in the same box, row, and column\n4. while picking the number, take into consideration where other numbers in that box. row, and column could potentially go\n5. if the box turns red, that means that the number you entered is repeated in one of the three places and you need to check over the past work that you have done.\n    You can click the undo button to help you backtrack and redo if you've gone too far back.\n6. You win the game once all the cells are filled and there are no numbers repeated in any box, row, and column\n\bColorPlace Instructions\n"
+ "Follow the same rules as NumberPlace. The exception is that instead of 9 distinct numbers, a user uses 9 distinct colors.\nWhen a user enters a color that is repeated in that box, row, or column, \nthe number that is correlated with the color that they entered will appear in the recently added box to notify them to recheck their board.\nSimilar to NumberPlace, the user can undo and redo their moves as needed in order to help them backtrack and solve the problem. ", "Instructions", 1);
      }
      if (event.getSource() == backToHome) 
        switchCenterPanel(backToMenuPanel()); 
      
      if (event.getSource() == regularMode) { 
        choseRegularMode = true;
        choseColorMode = false;
        switchCenterPanel(makeLevelPanel());
      }
      if (event.getSource() == colorMode) { 
        choseColorMode = true;
        choseRegularMode = false;
        switchCenterPanel(makeLevelPanel());
      }
      if (choseRegularMode) { 
        if (event.getSource() == easyLevel) {
          levelReveal = 30;
          numPlace = new NumberPlace(levelReveal);
          switchCenterPanel(numPlace.getNumberPlace()); 
          makeUndoButton();
          makeRedoButton();
        }
        if (event.getSource() == medLevel) {
          levelReveal = 25;
          numPlace = new NumberPlace(levelReveal);
          switchCenterPanel(numPlace.getNumberPlace());
          makeUndoButton();
          makeRedoButton();
          
        }
        if (event.getSource() == hardLevel) {
          levelReveal = 15; 
          numPlace = new NumberPlace(levelReveal);
          switchCenterPanel(numPlace.getNumberPlace());
          makeUndoButton();
          makeRedoButton();
        }
      }
      else if (choseColorMode) { 
        if (event.getSource() == easyLevel) {
          levelReveal = 30;
          colorPlace = new ColorPlace(levelReveal);
          switchCenterPanel(colorPlace.getColorPlace()); 
          southPanel = makeSouthPanel();
          add(southPanel, BorderLayout.SOUTH); 
          makeUndoButton();
          makeRedoButton();
          
        }
        if (event.getSource() == medLevel) {
          levelReveal = 25;
          colorPlace = new ColorPlace(levelReveal);
          switchCenterPanel(colorPlace.getColorPlace()); 
          southPanel = makeSouthPanel();
          add(southPanel, BorderLayout.SOUTH); 
          makeUndoButton();
          makeRedoButton();
          
        }
        if (event.getSource() == hardLevel) {
          levelReveal = 15; 
          colorPlace = new ColorPlace(levelReveal);
          switchCenterPanel(colorPlace.getColorPlace()); 
          southPanel = makeSouthPanel();
          add(southPanel, BorderLayout.SOUTH); 
          makeUndoButton();
          makeRedoButton();
        }
        
      }
      if(event.getSource() == undo){
        if (numPlace != null)
          numPlace.Undo(); 
        else
          colorPlace.Undo();
      }
      
      if(event.getSource() == redo){
        numPlace.Redo();
      }
      
    }
    
    /*private method to make a displaylevel panel*/
    private JPanel makeDisplayLevelPanel() {
      
      JPanel displayLevelPanel = new JPanel();
      displayLevelPanel.setBackground(Color.black);
      JLabel displayText = new JLabel(getLevelDifficulty());
      
      displayText.setForeground(Color.white);
      
      displayLevelPanel.add(displayText);
      
      return displayLevelPanel; 
    }
    
    /*private method to return to title panel (calls centerPanel)*/
    private JPanel backToMenuPanel()
    {
      return makeTitlePanel();
    }
    
    /*creates the panel that displays the buttons with the levels*/
    private JPanel makeLevelPanel(){
      JPanel levelPanel = new JPanel(); 
      levelPanel.setBackground(Color.black);
      JLabel levelText = new JLabel("Choose a level!\n");
      
      levelText.setForeground(Color.white);
      levelPanel.add(levelText);
      
      easyLevel = new JButton("Easy?");
      easyLevel.setFont(new Font("Serif", Font.ITALIC, 16));
      easyLevel.addActionListener(new ButtonListener());
      levelPanel.add(easyLevel);
      
      medLevel = new JButton("Medium?");
      medLevel.setFont(new Font("Serif", Font.ITALIC, 16));
      medLevel.addActionListener(new ButtonListener());
      levelPanel.add(medLevel);
      
      hardLevel = new JButton("Hard?");
      hardLevel.setFont(new Font("Serif", Font.ITALIC, 16));
      hardLevel.addActionListener(new ButtonListener());
      levelPanel.add(hardLevel);
      
      
      return levelPanel;
    }
  }
  
  /*to choose between NumberPlace and ColorNumberPlace*/
  private JPanel makeModePanel() {
    JPanel modePanel = new JPanel();
    modePanel.setBackground(Color.black);
    JLabel modeText = new JLabel("Choose a mode!\n");
    
    modeText.setForeground(Color.white);
    modePanel.add(modeText);
    
    regularMode = new JButton("Regular Number Place");
    regularMode.setFont(new Font("Serif", Font.ITALIC, 16));
    regularMode.addActionListener(new ButtonListener());
    modePanel.add(regularMode);
    
    colorMode = new JButton("Color Number Place");
    colorMode.setFont(new Font("Serif", Font.ITALIC, 16));
    colorMode.addActionListener(new ButtonListener());
    modePanel.add(colorMode);
    
    return modePanel;
  }
  
  /*create the undo button that will be placed on north sub-panel*/
  private void makeUndoButton() {
    undo = new JButton("Undo");
    undo.setFont(new Font("Serif", Font.ITALIC, 20));
    undo.addActionListener(new ButtonListener()); 
    northPanel.add(undo); 
  }
    
  /*create the redo button that will be placed on north sub-panel*/
  private void makeRedoButton() {
    redo = new JButton("Redo");
    redo.setFont(new Font("Serif", Font.ITALIC, 20));
    redo.addActionListener(new ButtonListener()); 
    northPanel.add(redo); 
  }
  
  /*returns the level difficulty chosen by the player*/
  public String getLevelDifficulty(){
    return Integer.toString(levelReveal);
  }
}
















