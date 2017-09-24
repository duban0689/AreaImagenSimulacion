
package areaimagen;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.SwingUtilities;
  

public class AreaImagen extends JFrame {
    
    private static final long serialVersionUID = 8937119421588637012L;
    private SeleccionAreaImagen selector;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                AreaImagen frame = new AreaImagen();
                frame.setVisible(true);
            }
        });
    }

    public AreaImagen() {
        super();
        initGUI();
    }
    
    private void initGUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        selector = new SeleccionAreaImagen();
        try {
            selector.setImagen(ImageIO.read(getClass().getClassLoader().getResource("imagen/Imagen.png")));
        } catch (IOException ex) {
            Logger.getLogger(AreaImagen.class.getName()).log(Level.SEVERE, null, ex);
        }
        add(selector, BorderLayout.CENTER);
        selector.addSelectorListener(new SeleccionAreaListener() {

            public void areaSeleccionada(BufferedImage imagen) {
                SeleccionAreaImagen s = new SeleccionAreaImagen(imagen);
                JDialog d = new JDialog(AreaImagen.this);
                d.add(s);
                d.pack();
                d.setVisible(true);
            }
        });
        setTitle("Demo " + selector.getClass().getName());
        pack();
        setResizable(false);
    }
    
}
