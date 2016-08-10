/**
 * 
 */
package exactitude.gui;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JFileChooser;

/**
 * @author bpmurray
 *
 */
public class Actioneer implements Action {
	private FrontEnd       frontEnd;
	private AbstractButton component;

	// Constructor
	public Actioneer(FrontEnd fe, AbstractButton comp) {
		this.frontEnd = fe;
		this.component = comp;
	}
	/* (non-Javadoc)
	 * @see javax.swing.Action#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#getValue(java.lang.String)
	 */
	@Override
	public Object getValue(String key) {
		if (key != null) {
			if (key.equals(MNEMONIC_KEY)) {
				return component.getMnemonic();
			} else if (key.equals(NAME)) {
				return component.getText();
			}
			else if (key.equals(ACTION_COMMAND_KEY)) {
				return component.getActionCommand();
			}
			else if (key.equals(SHORT_DESCRIPTION)) {
				return component.getToolTipText();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return component.isEnabled();
	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#putValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void putValue(String key, Object value) {
		if (key != null) {
			System.out.println("putValue: " + key);
		}

	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean b) {
		component.setEnabled(b);

	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// OK, who did it?
		Object source = e.getSource();
		BufferedImage sourceImage = null;
		//Handle open button/menu action.
		if (source == frontEnd.getJButtonOpen() || source == frontEnd.getJMenuFileOpen()) {
			JFileChooser fc = frontEnd.getJFileChoose();
			int returnVal = fc.showOpenDialog(frontEnd);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				frontEnd.setSourceFile(file.getPath());
				frontEnd.getJTextFilename().setText(file.getPath());
				try {
					sourceImage = ImageIO.read(file);
				} catch (Exception ex) {
					System.out.println("Can't load " + file.getPath() + ": " + ex.getLocalizedMessage());
				}
				

				frontEnd.getImageLeft().setImage(sourceImage);
				frontEnd.getImageLeft().repaint();
				frontEnd.getImageRight().setImage(null);
				frontEnd.getImageRight().repaint();

			}
		}
		else if (source == frontEnd.getJButtonExit() || source == frontEnd.getJMenuExit()) {
			frontEnd.exitProgram();
		}
	}


}
