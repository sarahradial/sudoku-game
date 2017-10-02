/* CS 230 Final Project
 * Primary Contributors: Barakah Quader, Emily Wang, Sarah Yan
 * 
 * Notes:
 * numberplacetester.java contains the frame to set the game up
 */


import javax.swing.JFrame;

public class NumberPlaceTester{
  
  public static void main (String[] args) {
    /* creates and shows a JFrame 
     * and sets a default close operator
     */
    JFrame frame = new JFrame("Number Place");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
    /* creates a Home panel
     * and adds it to the frame
     */
    Home panel = new Home();
    frame.getContentPane().add(panel);

    /* sets JFrame's properties
     */
    frame.setSize(500,500);
    frame.setResizable(false);
    frame.setVisible(true);

  }
}


