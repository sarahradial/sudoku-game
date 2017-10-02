/* CS 230 Final Project
 * Primary Contributors: Barakah Quader, Emily Wang, Sarah Yan
 * 
 * Notes:
 * Move.java contains the Move objects for any user moves 
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import javafoundations.LinkedStack;

public class Move {
  //private instance vars
  private final int [] coords;
  private final String newValue;
  private final Color newColor;
  
  //constructor
  public Move(int [] c, String newVal, Color newC){
    coords = c;
    newValue = newVal;
    newColor = newC;
  }
  
  //getter methods
  public int [] getCoords(){
    return coords;
  }
  public String getValue(){
    return newValue;
  }
  public Color getColor(){
    return newColor;
  }
}