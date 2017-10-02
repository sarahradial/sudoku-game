/* CS 230 Final Project
 * Primary Contributors: Barakah Quader, Emily Wang, Sarah Yan
 * 
 * Notes:
 * numberplace.java contains the mechanics (methods and others) of the number place game
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.util.*;
import javafoundations.LinkedStack;

public class NumberPlace extends JPanel{
  /* private variables for general setup
   * numberPlace to edit
   * numberLevel aka the difficulty level
   */
  private JPanel numberPlace;
  private int numberLevel;
  private JOptionPane errorMessage;
  
  /* private 2D array of textFields for userinput
   */
  private JTextField [][] textFieldCells;
  
  /* private variables to set up the grid properties
   * GRID_SIZE = size of board
   * SUBGRID_SIZE = size of subgrid
   */
  private static final int GRID_SIZE = 9;  
  private static final int SUBGRID_SIZE = 3;
  
  /* private variables to set up the UI in terms of sizes, colors, fonts, etc
   * CELL_SIZE is the cell width/height in pixels
   * CANVAS_WIDTH determines the board width in pixels 
   * CANVAS_HEIGHT determines the board height in pixels
   */
  private static final int CELL_SIZE = 60; 
  private static final int CANVAS_WIDTH = CELL_SIZE * GRID_SIZE;
  private static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
  
  /* public variables for the nested InputListener to use for Undo
   * np_inputs stores all the inputs in a stack
   * np_index stores all the index values in an int array stack
   * np_colors stores all the corresponding background colors
   */
  public LinkedStack<String> np_inputs; 
  public LinkedStack<int[]> np_index; 
  public LinkedStack<Color> np_colors;
  
  /* public variables for the nested InputListener to use for Redo
   * np_savedInputs stores all the inputs in a stack
   * np_savedValues stores all the index values in an int array stack
   * np_savedColors stores all the corresponding background colors
   */
  public LinkedStack<String> np_savedInputs;
  public LinkedStack<int[]> np_savedIndex;
  public LinkedStack<Color> np_savedColors;
  
  /* Each Move object will contain the cell's colors, coords, and index
   * np_Moves is going to be used for Undo
   * np_savedMoves is going to be used for Redo
   */
  public LinkedStack<Move> np_moves;
  public LinkedStack<Move> np_savedMoves;
  
  /* constructor that sets up..
   * the linkedstacks so that we can use it in other methods
   * numberLevel for determining number of autofilled JButtons
   * numberPlace so that we can put Layout and textfields on it
   * grid layout (9 by 9) so that we can line up each textfieldCell
   * textFieldCells (9 by 9) for each cell in grid
   * among other set ups including individual textfield cells, auto filled cells
   */
  public NumberPlace(int numLevel) {
    
    np_inputs = new LinkedStack<String>();
    np_index = new LinkedStack<int[]>(); 
    np_colors = new LinkedStack<Color>();
    
    np_savedInputs = new LinkedStack<String>();
    np_savedIndex = new LinkedStack<int[]>(); 
    np_savedColors = new LinkedStack<Color>();
    
    np_moves = new LinkedStack<Move>();
    np_savedMoves = new LinkedStack<Move>();
    
    numberLevel = numLevel;
    
    numberPlace = new JPanel();
    
    GridLayout grid = new GridLayout(GRID_SIZE, GRID_SIZE, 1, 1);
    numberPlace.setLayout(grid);
    
    textFieldCells = new JTextField[GRID_SIZE][GRID_SIZE];
    
    /* sets up each JButton within textFieldCell
     * by adding an InputListener with specified features
     */
    for (int i = 0; i < GRID_SIZE; i++) {
      for (int j = 0; j < GRID_SIZE; j++) {
        textFieldCells[i][j] = new JTextField("");
        textFieldCells[i][j].addActionListener(new InputListener());
        textFieldCells[i][j].setForeground(Color.black);
        textFieldCells[i][j].setHorizontalAlignment(JTextField.CENTER);
        numberPlace.add(textFieldCells[i][j]);
      }
    } 
    
    /* sets up the color by grouping the 3 by 3 boxes
     * easier for the user to see
     */
    for (int i = 0; i < GRID_SIZE; i++) {
      for (int j = 0; j < GRID_SIZE; j++) {
        if ((i < 3 && (j < 3 || j > 5)) || (i > 5 && (j < 3 || j > 5)) || ( (i > 2 && i < 6) && (j > 2 && j < 6)))
          textFieldCells[i][j].setBackground(Color.lightGray);
        else 
          textFieldCells[i][j].setBackground(Color.white);
      }
    } 
    
    /* generates the auto filled cells via random number algorithm
     * checks if it conflicts with other auto filled cells
     * if it does conflict, then choose again (same loc, diff int)
     * set these cells as uneditable
     */
    for (int i = 0; i < numberLevel; i++) { 
      int randVal = (int)(Math.random()*9 + 1);
      int randRowNum = (int)(Math.random()*9);
      int randColNum = (int)(Math.random()*9);
      Integer randValInteger = new Integer(randVal);
      
      while (!textFieldCells[randRowNum][randColNum].getText().equals("")) {
        randRowNum = (int)(Math.random()*9);
        randColNum = (int)(Math.random()*9);
      }
      
      textFieldCells[randRowNum][randColNum].setText(randValInteger.toString());
      
      while (!check(randRowNum, randColNum)) { 
        randVal = (int)(Math.random()*9 + 1);
        randValInteger = new Integer(randVal);
        textFieldCells[randRowNum][randColNum].setText(randValInteger.toString());
      }
      
      textFieldCells[randRowNum][randColNum].setEditable(false); 
    }
  }
  
  /* method to get the specific board
   * returns JPanel 
   */
  public JPanel getNumberPlace() {
    return numberPlace;
  }
  
  public JOptionPane errorMessage(String msg) {
    errorMessage = new JOptionPane(msg);
    JDialog d = errorMessage.createDialog((JFrame) null, "Error Message");
    d.setLocation(50,50);
    d.setVisible(true);
    return errorMessage;
  }
  /* method to set the color of said location
   */
  private void setColors(int r, int c) {
    if (!check(r, c) && !textFieldCells[r][c].getText().equals("")) {
        textFieldCells[r][c].setBackground(Color.red);
        JOptionPane panel = errorMessage("Entered a conflicting number.\nPlease enter a different value in this cell.");
        
    } else {
        if ((r < 3 && (c < 3 || c > 5)) || 
            (r > 5 && (c < 3 || c > 5)) || 
            ( (r > 2 && r < 6) && (c > 2 && c < 6)))
          textFieldCells[r][c].setBackground(Color.lightGray);
        else 
          textFieldCells[r][c].setBackground(Color.white);
      } 
  }
  /* method to check if current location's value does not conflict with any other values 
   * within its column, row, and 3 by 3 box
   * returns a boolean (true if no conflicts, false if there are conflicts)
   */
  private boolean check(int r, int c) {
    /* intializes three arrays to store and check values 
     */
    int[] row = new int[9];
    int[] col = new int[9];
    int[] box = new int[9];
    
    /* 1st if statement 
     * - obtains values and stores into row array if values exist inside the cells
     * 2nd if statement
     * - obtains values and stores into col array if values exist inside the cells
     */
    for (int i = 0; i < 9; i++) {
      if (!textFieldCells[r][i].getText().equals("")) {
        row[i] = Integer.parseInt(textFieldCells[r][i].getText());
      }
      
      if (!textFieldCells[i][c].getText().equals("")) {
        col[i] = Integer.parseInt(textFieldCells[i][c].getText());
      }
    }
    
    /* to check if there is a value repeat in either array
     * - returns false if there is...
     * - continue if there isn't...
     */
    if (checkForRepeats(row) || checkForRepeats(col)) { 
      return false;
    }
    
    /* boundaries for the 3x3 square that the location is at
     */
    int boxRowNum = (r-(r%3))/3;
    int boxColNum = (c-(c%3))/3;
    int startRow = boxRowNum*3;
    int startCol = boxColNum*3;
    
    /* counter for the box array
     */
    int arrayCounter = 0;
    
    /* uses the boundaries to store values into box array
     */
    for (int j = startRow; j < startRow + 3; j++) {
      for (int k = startCol; k < startCol + 3; k++) {
        if (!textFieldCells[j][k].getText().equals("")) {
          box[arrayCounter] = Integer.parseInt(textFieldCells[j][k].getText());
          arrayCounter++;
        }
      }
    }
    
    /* checks if there are repeated values in the box
     * - returns false if there is
     * - continues if there isn't
     */
    if (checkForRepeats(box))
      return false;
    
    /* after all the checking, method returns true
     */
    return true;
  }
  
  /* private helper method to check for repeating values in an array
   * returns boolean 
   * - true if there is a repeat
   * - false if there aren't repeats
   */ 
  private boolean checkForRepeats(int[] array) { 
    
    for (int i = 0; i < array.length-1; i++) {
      for (int j = i+1; j < array.length; j++) {
        /* empty values are represented with 0 (default value for array) 
         * so need to skip 0 values
         */
        if (array[i] != 0 && array[i] == array[j]) {
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean canRedo(){
    return !np_savedMoves.isEmpty();
  }
  
  public void Redo() {
    if(!canRedo()){
      JOptionPane panel = errorMessage("No moves to redo");
    }
    else {
      /* first get the saved moves data in saveedMoves
       * then set the text of the previously edited cell to blank
       * then set the background of the cell using the check method
       */
      Move temp = np_savedMoves.pop();
      int [] coords = temp.getCoords();
      Color color = temp.getColor();
      String value = temp.getValue();
      
      /* push temp move into Undo's linkedlist of moves
       */
      np_moves.push(temp);
      
      textFieldCells[coords[0]][coords[1]].setText(value);
      
      /*to set the cell into the correct color
       */
      setColors(coords[0], coords[1]); 
      
      /* update the cell
       */
      revalidate();
      System.out.println("line 284 end of redo\n");
    }
  }
  
  /* helper method to check if user can even undo
   * returns boolean
   * - true if np_inputs is not empty
   * - false if np_inputs is empty 
   */
  public boolean canUndo(){
    return !np_moves.isEmpty();  
  }
  
  /* void Undo method allows the user to backtrace their movements 
   * accesses numberplace's three linkedstacks: np_index, np_inputs
   * first checks if we can undo 
   * - (since these linkedstacks should have the same size, we only need to check if one of them has size > 0)
   * then evaluates based on size of linkedstacks
   */
  public void Undo(){
    
    if(!canUndo()){
      JOptionPane panel = errorMessage("Cannot undo");
    }
    else {
      
      /* first get the prev move data in np_move LinkedList
       * then set the text of the previously edited cell to blank
       * then set the background of the previously edited cell using check method
       */
      Move previous = np_moves.pop();
      int [] coords = previous.getCoords();
      Color prevColor = previous.getColor();
      String prevValue = previous.getValue();
      
      /* store previous Move into np_savedMoves
       * and then sets text to empty for now
       */
      np_savedMoves.push(previous);
     
      textFieldCells[coords[0]][coords[1]].setText("");
      
      /* if there np_inputs still has data in it
       * then check if the following datas has the same indexes,
       * if so, set cell's value to the next undo
       * HAS TO EDIT SO IT CORRECTLY UNDOS and ADDS COMPONENTS TO REDO
       */
       if(np_moves.size() > 0) {
        
        LinkedStack<Move> temp_moves = new LinkedStack<Move>(); 
        boolean prevIsFound = false;
        
        while ((np_moves.size() > 0) && !prevIsFound) {
          
          Move temp = np_moves.pop();
          int [] temp_coords = previous.getCoords();
          Color temp_color = previous.getColor();
          String temp_value = previous.getValue();
          
          if (temp_coords[0] == coords[0] && temp_coords[1] == coords[1] && temp_value != (textFieldCells[coords[0]][coords[1]].getText())) {
            System.out.println("once");
            np_savedMoves.push(temp);
            
            textFieldCells[temp_coords[0]][temp_coords[1]].setText(temp_value);
            
            prevIsFound = true;
          }
          temp_moves.push(temp);
        }
        np_moves = temp_moves;
        System.out.println("np_moves size: " + np_moves.size());
         /*to set the cell into the correct color after all the modifications
          */
      }
      
      setColors(coords[0], coords[1]);
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
    return ind;
  }
  
  /*nested InputListener class which implements ActionListener
   * has InputListener which checks if there is a textFieldCell JField from specified location
   * then stores the values into corresponding input and index LinkedStacks
   */
  public class InputListener  implements ActionListener {
    
    public void actionPerformed (ActionEvent event) {
      /* to get the source (aka the specific Button) by setting loc to (-1, -1) 
       * then find the sourceTextField location coords by comparing the cell to the source
       * if found, then push the location coords onto the np_index LinkedStack
       */
      int rSelected = -1;
      int cSelected = -1;
      
      JTextField sourceTextField = (JTextField)event.getSource();
      
      boolean found = false;
      for (int r = 0; r < GRID_SIZE && !found; ++r) {
        for (int c = 0; c < GRID_SIZE && !found; ++c) {
          if (textFieldCells[r][c] == sourceTextField) {
            rSelected = r;
            cSelected = c;
            int [] ind = {r, c};
            found = true;
          }
        }
      }
      
      /* if we found the source textfield location coords, 
       * obtain the user input and parse it into int
       * if value is valid (try catch the user input)
       * - then push value into np_inputs LinkedStack
       * - set textfield text as the value
       * - push cell background into np_colors LinkedStack
       */
      if (found) {
        
        String value = textFieldCells[rSelected][cSelected].getText();
        System.out.println("value: " + value);
        
        try {
          int num = Integer.parseInt(value);
          
          if (num > 9 || num < 1) {
            JOptionPane.showMessageDialog(null, "Enter a different value. Value must be between 1 and 9");
          
          } else {
            textFieldCells[rSelected][cSelected].setText(value);
            
            /* calls on setColors which will use a helper method to...
             * check if it conflicts with values in its row, column, and 3 by 3 box
             * if it does - set the background as red
             * if not, set the color to either lightgray or white (depending on its loc)
             */
            setColors(rSelected,cSelected);
            
            /* clears saved Moves for Redo when user puts in a new value
             */
            np_savedMoves = new LinkedStack<Move>();
            
            /*determine if user won by comparing the color of the boxes and if there is a value
             */ 
            if (np_index.size() == (81 - numberLevel)) {
              boolean won = true;
              for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                  if (!(textFieldCells[i][j].getBackground().equals(Color.white) ||textFieldCells[i][j].getBackground().equals(Color.lightGray)))
                    won = false;
                }
              }
              if (won == true)
                System.out.println("Congrats, you won!");
            }
          }
        } catch (NumberFormatException nfe){
          JOptionPane panel = errorMessage("This is not a value. Please try again.");
        }
        
        /* Creates a new object Move and puts it in the LinkedStack
         */
        int [] coords = new int [2];
        coords[0] = rSelected;
        coords[1] = cSelected;
        Move curr = new Move(coords, value, textFieldCells[rSelected][cSelected].getBackground());
        
        np_moves.push(curr);
        System.out.println("size of np_moves: " + np_moves.size());
        
        /* save the color background here in case it was modified 
         */
        Color temp = textFieldCells[rSelected][cSelected].getBackground();
        np_colors.push(temp);
        
      }
    }
  }
}
