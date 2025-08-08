
package sistemacine.modelo;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;

public class JPanelImage extends JLabel{
    private final int x;
    private final int y;
    private final String path;
    
    public JPanelImage(JDesktopPane panel, String path){
        this.path = path;
        this.x = panel.getWidth();
        this.y = panel.getHeight();
       this.setSize(x, y); 
      //  this.setSize(panel.getSize()); 
       // this.setLayout(new java.awt.BorderLayout());
        
        
    }
    @Override
    public void paint (Graphics g){
       ImageIcon img = new ImageIcon(getClass().getResource(path));
       g.drawImage(img.getImage(), 0, 0, x, y, null);  
}
 
}
