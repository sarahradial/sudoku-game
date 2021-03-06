/* CS 230 Final Project
 * Primary Contributors: Barakah Quader, Emily Wang, Sarah Yan
 * 
 * Notes:
 * colorplace.java contains the mechanics (methods and others) of the color place
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import javafoundations.LinkedStack;

public class ColorPlace extends JPanel{
  /* private variables for general setup
   * colorPlace to edit
   * numberLevel aka the difficulty level
   */
  private JPanel colorPlace;
  private int numberLevel;
  
  /* private 2D array of textFields for userinput
   */
  private JTextField [][] textFieldCells;
  
  /* private variables to set up the grid properties
   * GRID_SIZE = size of board
   * SUBGRID_SIZE = size of subgrid
   */
  public static final int GRID_SIZE = 9;  
  public static final int SUBGRID_SIZE = 3;
  
  
  /* private variables to set up the UI in terms of sizes, colors, fonts, etc
   * CELL_SIZE is the cell width/height in pixels
   * CANVAS_WIDTH determines the board width in pixels 
   * CANVAS_HEIGHT determines the board height in pixels
   */
  public static final int CELL_SIZE = 60; 
  public static final int CANVAS_WIDTH = CELL_SIZE * GRID_SIZE;
  public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
  
  /* public variables for the nested InputListener to use
   * np_inputs stores all the inputs in a stack
   * np_index stores all the index values in an int array stack
   */
  public LinkedStack<Color> cp_inputs; 
  public LinkedStack<int[]> cp_index; 
  
  /*array of Colors to hold Color values (and corresponding index values)
   */
  public Color[] colors;
  
  /* constructor that sets up..
   * the linkedstacks so that we can use it in other methods
   * numberLevel for determining number of autofilled JButtons
   * colorrPlace so that we can put Layout and textfields on it
   * grid layout (9 by 9) so that we can line up each textfieldCell
   * textFieldCells (9 by 9) for each cell in grid
   * among other set ups including individual textfield cells, auto filled cells
   */
  public ColorPlace(int numLevel) {
    
    cp_inputs = new LinkedStack<Color>();
    cp_index = new LinkedStack<int[]>(); 
    colors = new Color[] {Color.red, Color.pink, Color.orange, Color.yellow, Color.white, Color.green, Color.cyan, Color.blue, Color.magenta};
    
    numberLevel = numLevel;

    colorPlace = new JPanel();
    
    GridLayout grid = new GridLayout(GRID_SIZE, GRID_SIZE, 1, 1);
    colorPlace.setLayout(grid);
    
    textFieldCells = new JTextField[GRID_SIZE][GRID_SIZE];

    
    /* sets up each JButton within textFieldCell
     * by adding an InputListener with specified features
     */
    for (int i = 0; i < GRID_SIZE; i++) {
      for (int j = 0; j < GRID_SIZE; j++) {
        textFieldCells[i][j] = new JTextField("");
        textFieldCells[i][j].addActionListener(new InputListener());
        textFieldCells[i][j].setBackground(Color.lightGray);
        textFieldCells[i][j].setForeground(Color.black);
        textFieldCells[i][j].setHorizontalAlignment(JTextField.CENTER);
        colorPlace.add(textFieldCells[i][j]);
      }
    } 
    
    /* generates the auto filled cells via random number algorithm
     * checks if it conflicts with other auto filled cells
     * if it does conflict, then choose again (same loc, diff int)
     * set these cells as uneditable
     */
    for (int i = 0; i < numberLevel; i++) { 
      int randVal = (int)(Math.random()*9);
      Color tempColor = colors[randVal];
      int randRowNum = (int)(Math.random()*9);
      int randColNum = (int)(Math.random()*9);
      
      while (!textFieldCells[randRowNum][randColNum].getBackground().equals(Color.lightGray)) 
      {
        randRowNum = (int)(Math.random()*9);
        randColNum = (int)(Math.random()*9);
      }
      
      textFieldCells[randRowNum][randColNum].setBackground(tempColor);
      while (!check(randRowNum, randColNum)) { 
        randVal = (int)(Math.random()*9);
        tempColor = colors[randVal];
        textFieldCells[randRowNum][randColNum].setBackground(tempColor);
      }
      textFieldCells[randRowNum][randColNum].setEditable(false); 
    }
  }
  
  /* method to get the specific board
   * returns JPanel 
   */
  public JPanel getColorPlace() {
    return colorPlace;
  }
  
  /* method to check if current location's value does not conflict with any other values 
   * within its column, row, and 3 by 3 box
   * returns a boolean (true if no conflicts, false if there are conflicts)
   */
  private boolean check(int r, int c) {
    /* intializes three arrays to store and check values 
     */
    Color[] row = new Color[9];
    Color[] col = new Color[9];
    Color[] box = new Color[9];
    
    for (int i = 0; i < 9; i++) {
      //add values to ROW array
      row[i] = textFieldCells[r][i].getBackground();
      //add value to COL
      col[i] = textFieldCells[i][c].getBackground();
    }
    
    if (checkForRepeats(row)) { //if there is a repeat in the array of rows
      return false;
    }
    if (checkForRepeats(col)) { //if there is a repeat in the array of columns
      return false;
    }
    
    //get values within the 3x3 square
    int boxRowNum = (r-(r%3))/3;
    int boxColNum = (c-(c%3))/3;
    int startRow = boxRowNum*3;
    int startCol = boxColNum*3;
    
    int arrayCounter = 0;
    for (int j = startRow; j < startRow + 3; j++) {
      for (int k = startCol; k < startCol + 3; k++) {
        //if value exists in the space (not "")
        if (!textFieldCells[j][k].getBackground().equals(Color.lightGray)) {
          //add to box array
          box[arrayCounter] = textFieldCells[j][k].getBackground();
          arrayCounter++;
        }
      }
    }
    if (checkForRepeats(box)) {
      return false;
    }
    return true;
  }
  
  //private helper method to check for repeating values in an array
  //return true if there is a repeat
  private boolean checkForRepeats(Color[] array) { 
    for (int i = 0; i < array.length-1; i++) {
      for (int j = i+1; j < array.length; j++) {
        //empty values are represented with 0 (default value for array) so need to skip 0 values
        if (array[i] != null && array[j] != null && !array[i].equals(Color.lightGray) && !array[j].equals(Color.lightGray) && array[i].equals(array[j])) {
          return true;
        }
      }
    }
    return false;
  }
  
  /* helper method to check if user can even undo
   * returns boolean
   * - true if cp_inputs is not empty
   * - false if cp_inputs is empty 
   */
  public boolean canUndo(){
    return !cp_inputs.isEmpty();  
  }
  /* void Undo method allows the user to backtrace their movements 
   * accesses numberplace's two linkedstacks: cp_index, cp_inputs, cp_colors
   * first checks if we can undo 
   * - (since these linkedstacks should have the same size, we only need to check if one of them has size > 0)
   * then evaluates based on size of linkedstacks
   */
  public void Undo(){
    
    if(!canUndo()){
      System.out.println("There are no more moves to undo..."); 
    }
    else {
      /* first get the next data in the three linkedstacks
       * then set the text of the previously edited cell to blank
       * then set the background of the previously edited cell back to previous color
       */
      Color in_temp = cp_inputs.pop();
      int [] a_temp = cp_index.pop();
      
      textFieldCells[a_temp[0]][a_temp[1]].setBackground(Color.lightGray);
      textFieldCells[a_temp[0]][a_temp[1]].setText("");
      
      /* if there np_inputs still has data in it
       * then check if the next data has the same indexes,
       * if so, set cell's value to the next undo
       */
      if(cp_inputs.size() > 0) {
        int [] p_temp = cp_index.peek();
        Color next_color = cp_inputs.peek();
        
        if (p_temp[0] == a_temp[0] && p_temp[1] == a_temp[1]) {
          textFieldCells[a_temp[0]][a_temp[1]].setBackground(next_color);
        }
      }
      
      /* update the cell
       */
      revalidate();
    }
  }
  
  public int[] getIndex(String x){
    int[] ind = new int[2]; 
    for(int i = 0; i < 9; i++){
      for(int j = 0; j < 9; j++){
        System.out.println("i = " + i + " j = " + j);
        if(textFieldCells[i][j] == new JTextField(x))
          ind[0] = i;
          ind[1] = j;
      }
    }
    System.out.println("x: " + x + " line 198: " + ind[0] + ", " + ind[1]);
    return ind;
  }
  
  private class InputListener implements ActionListener {
    public void actionPerformed (ActionEvent event) {
      //to get the source (aka the specific Button)
      int rSelected = -1;
      int cSelected = -1;
      
      JTextField sourceTextField = (JTextField)event.getSource();
      
      //scan JFields for all r and c, to match with the source object
      boolean found = false;
      for (int r = 0; r < GRID_SIZE && !found; ++r) {
        for (int c = 0; c < GRID_SIZE && !found; ++c) {
          if (textFieldCells[r][c] == sourceTextField) {
            rSelected = r;
            cSelected = c;
            int[] temp = new int[2];
            temp[0] = r;
            temp[1] = c; 
            try {
              cp_index.push(temp); 
            } catch (NullPointerException e)
            {
              System.out.println(e);
            }
            found = true;
          }
        }
      }
      
      if (found) {
        //get text from user input
        String text = textFieldCells[rSelected][cSelected].getText();
        
        //parse the String text into int, for record keeping
        int num = Integer.parseInt(text);
        //set the text into stuff
        
        textFieldCells[rSelected][cSelected].setText(text);
        //set the background to the corresponding color from color array 
        textFieldCells[rSelected][cSelected].setBackground(colors[num-1]); //must be 0-9 or will throw error

        cp_inputs.push(colors[num-1]); 
        
        //call checker method
        boolean checkResult = check(rSelected,cSelected);
        if (checkResult) {
          textFieldCells[rSelected][cSelected].setText("");
        }
        
        if (cp_index.size() == 81) {
          boolean won = true;
          for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
              if (textFieldCells[i][j].getForeground().equals(Color.red))
              {
                System.out.println("You have not yet won");
                won = false;
              }
            }
          }
          if (won == true)
            System.out.println("Congrats, you won!");
        }
      }
    }
  }
}


