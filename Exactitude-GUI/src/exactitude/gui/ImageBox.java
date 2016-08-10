/**
 * 
 */
package exactitude.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * @author bpmurray
 *
 */
public class ImageBox extends JPanel {
	private static final long serialVersionUID = 5988301900259286783L;
	
	private BufferedImage image;

	public void paintComponent(Graphics g)	{
		super.paintComponent(g);
		
		if(image != null) {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}
	}

	/**
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}
}
