package service;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

public class Ideas {
	
	 public static BufferedImage iconToImage(Icon icon) {
         int w = icon.getIconWidth();
         int h = icon.getIconHeight();
         GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
         GraphicsDevice gd = ge.getDefaultScreenDevice();
         GraphicsConfiguration gc = gd.getDefaultConfiguration();
         BufferedImage image = gc.createCompatibleImage(w, h);
         Graphics2D g = image.createGraphics();
         icon.paintIcon(null, g, 0, 0);
         g.dispose();
         return image;
	 }
     
}
