
package areaimagen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RescaleOp;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;


public class SeleccionAreaImagen extends JPanel {

    private static final long serialVersionUID = 1934268343709701737L;
    private Point puntoInicial;
    private Point puntoFinal;
    private int x;
    private int y;
    private int ancho;
    private int alto;
    private BufferedImage imagen;
    private BufferedImageOp op = new RescaleOp(new float[]{0.5f, 0.5f, 0.5f}, new float[]{0f, 0f, 0f}, null);
    private boolean cuadrada = true;
    private Set<SeleccionAreaListener> ls;

    public SeleccionAreaImagen() {
        ls = new HashSet<SeleccionAreaListener>(5);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                puntoInicial = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                notificarSeleccion(getImagenSeleccionada());
                puntoInicial = null;
                puntoFinal = null;
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                puntoFinal = e.getPoint();
                repaint();
            }
        });
    }

    public SeleccionAreaImagen(BufferedImage imagen) {
        this();
        setImagen(imagen);
    }

    public final void setImagen(BufferedImage imagen) {
        this.imagen = imagen;
        if (imagen != null) {
            setPreferredSize(new Dimension(imagen.getWidth(), imagen.getHeight()));
        }
    }

    public void setCuadrada(boolean cuadrada) {
        this.cuadrada = cuadrada;
    }

    public void addSelectorListener(SeleccionAreaListener listener) {
        if (listener == null) {
            return;
        }
        ls.add(listener);
    }

    public void removeSelectorListener(SeleccionAreaListener listener) {
        if (listener == null) {
            return;
        }
        ls.remove(listener);
    }

    @Override
    public void paintComponent(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        super.paintComponent(g);
        if (imagen == null) {
            return;
        }
        if (puntoInicial != null && puntoFinal != null) {
            g.drawImage(imagen, op, 0, 0);
            g.drawImage(getImagenSeleccionada(), x, y, this);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setColor(Color.WHITE);
            g.drawRect(x, y, ancho, alto);
            g.drawString("Área: " + ancho + "x" + alto, 10, 20);
        } else {
            g.drawImage(imagen, 0, 0, this);
        }
    }

    private void notificarSeleccion(BufferedImage seleccion) {
        for (SeleccionAreaListener l : ls) {
            l.areaSeleccionada(seleccion);
        }
    }

    private void calcularPuntos() {
        if (puntoInicial == null || puntoFinal == null) {
            return;
        }
        int xi = puntoInicial.x;
        int yi = puntoInicial.y;
        int xf = puntoFinal.x;
        int yf = puntoFinal.y;
        int iw = 0;
        int ih = 0;
        if (imagen != null) {
            iw = imagen.getWidth();
            ih = imagen.getHeight();
            if (xf > iw) {
                xf = iw;
            }
            if (yf > ih) {
                yf = ih;
            }
        }
        if (xf < 0) {
            xf = 0;
        }
        if (yf < 0) {
            yf = 0;
        }
        ancho = Math.abs(xi - xf);
        alto = Math.abs(yi - yf);
        ancho = ancho == 0 ? 1 : ancho;
        alto = alto == 0 ? 1 : alto;
        if (cuadrada) {
            ancho = Math.min(ancho, alto);
            alto = ancho;
        }
        x = xi < xf ? xi : xi - ancho;
        y = yi < yf ? yi : yi - alto;
    }

    private BufferedImage getImagenSeleccionada() {
        if (imagen == null) {
            return null;
        }
        calcularPuntos();
        return imagen.getSubimage(x, y, ancho, alto);
    }
}


