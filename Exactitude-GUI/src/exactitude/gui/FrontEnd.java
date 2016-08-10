/**
 * Software License Agreement (BSD License)
 *
 * Copyright 2010 Brendan Murray. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY BRENDAN MURRAY ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BRENDAN MURRAY OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied.
 *
 */
package exactitude.gui;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

/**
 * @author bpmurray
 *
 */
public class FrontEnd extends JFrame {

	// Generated serialization version ID
	private static final long serialVersionUID = 3098895797497306674L;

	// Locations/borders
	private static final int  SIZE_LEFT   = 10;
	//	private static final int  SIZE_RIGHT  = 10;
	private static final int  SIZE_TOP    = 10;
	// private static final int  SIZE_BOTTOM = 10;

	// Globals to hold the various Swing GUI elements
	private JSplitPane  jSplitMain = null;
	private JSplitPane  jSplitTop = null;
	private ImageBox    imageLeft = null;
	private ImageBox    imageRight = null;
	private JPanel      jPanelBottom = null;

	private JMenuBar    jMenuBar = null;
	private JMenu       jMenu = null;
	private JMenuItem   jMenuFileOpen = null;
	private JMenuItem   jMenuConvComp = null;
	private JMenuItem   jMenuCompOnly = null;
	private JMenuItem   jMenuReload = null;
	private JMenuItem   jMenuExit = null;
	private JButton     jButtonOpen = null;
	private JTextField  jTextFilename = null;
	private JButton     jButtonExit = null;
	private JButton     jButtonConvert = null;
	private JButton     jButtonCompare = null;
	private JButton     jButtonReload = null;

	//Create a file chooser
	private JFileChooser jFileChoose = null;
	private String       sourceFile = null;
	private String       targetFile = null; 
	
	
	/**
	 * Constructor
	 */
	public FrontEnd() {
		super();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}

		this.setTitle("Exactitude - GUI demo interface");
		this.setJMenuBar(getJJMenuBar());
		this.setContentPane(getJSplitMain());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.pack();
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
	}

	/**
	 * This method initializes jSplitMain	
	 * 	
	 * @return javax.swing.jSplitMain	
	 */
	private JSplitPane getJSplitMain() {
		if (jSplitMain == null) {
			jSplitMain = new JSplitPane();
			jSplitMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitMain.setDividerSize(4);
			jSplitMain.setDividerLocation(400);
			jSplitMain.setBottomComponent(getJPanelBottom());
			jSplitMain.setTopComponent(getJSplitTop());
			jSplitMain.setDividerLocation(0.9);
			jSplitMain.setOneTouchExpandable(true);
			jSplitMain.setContinuousLayout(true);
			jSplitMain.setResizeWeight(0.9);
		}
		return jSplitMain;
	}

	/**
	 * This method initializes jMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getJJMenuBar() {
		if (jMenuBar == null) {
			jMenuBar = new JMenuBar();
			jMenuBar.add(getJMenu());
		}
		return jMenuBar;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu() {
		if (jMenu == null) {
			jMenu = new JMenu();
			jMenu.setText("Convert");
			jMenu.setMnemonic(KeyEvent.VK_C);
			jMenu.add(getJMenuFileOpen());
			jMenu.add(getJMenuConvComp());
			jMenu.add(getJMenuCompOnly());
			jMenu.add(getJMenuReload());
			jMenu.add(getJMenuExit());
		}
		return jMenu;
	}

	/**
	 * This method initializes jMenuFileOpen	
	 * 	
	 * @return javax.swing.jMenuFileOpen	
	 */
	public JMenuItem getJMenuFileOpen() {
		if (jMenuFileOpen == null) {
			jMenuFileOpen = new JMenuItem();
			jMenuFileOpen.setName("menuOpen");
			jMenuFileOpen.setToolTipText("Open an image file");
			jMenuFileOpen.setMnemonic(KeyEvent.VK_O);
			jMenuFileOpen.setText("Open");
			jMenuFileOpen.setAction(new Actioneer(this, jMenuFileOpen));
		}
		return jMenuFileOpen;
	}

	/**
	 * This method initializes jMenuConvComp	
	 * 	
	 * @return javax.swing.jMenuFileOpen	
	 */
	public JMenuItem getJMenuConvComp() {
		if (jMenuConvComp == null) {
			jMenuConvComp = new JMenuItem();
			jMenuConvComp.setText("Convert and Compare");
			jMenuConvComp.setMnemonic(KeyEvent.VK_T);
			jMenuConvComp.setName("menuConv");
			jMenuConvComp.setToolTipText("Convert the image and compare outputs");
			jMenuConvComp.setAction(new Actioneer(this, jMenuConvComp));
		}
		return jMenuConvComp;
	}

	/**
	 * This method initializes jMenuCompOnly	
	 * 	
	 * @return javax.swing.jMenuFileOpen	
	 */
	public JMenuItem getJMenuCompOnly() {
		if (jMenuCompOnly == null) {
			jMenuCompOnly = new JMenuItem();
			jMenuCompOnly.setText("Compare only");
			jMenuCompOnly.setToolTipText("Do not convert - compare only");
			jMenuCompOnly.setMnemonic(KeyEvent.VK_Y);
			jMenuCompOnly.setName("menuComp");
			jMenuCompOnly.setAction(new Actioneer(this, jMenuCompOnly));
		}
		return jMenuCompOnly;
	}

	/**
	 * This method initializes jMenuReload	
	 * 	
	 * @return javax.swing.jMenuFileOpen	
	 */
	public JMenuItem getJMenuReload() {
		if (jMenuReload == null) {
			jMenuReload = new JMenuItem();
			jMenuReload.setText("Reload");
			jMenuReload.setName("menuReload");
			jMenuReload.setMnemonic(KeyEvent.VK_R);
			jMenuReload.setToolTipText("Reload the image files");
			jMenuReload.setAction(new Actioneer(this, jMenuReload));
		}
		return jMenuReload;
	}

	/**
	 * This method initializes jMenuExit	
	 * 	
	 * @return javax.swing.jMenuFileOpen	
	 */
	public JMenuItem getJMenuExit() {
		if (jMenuExit == null) {
			jMenuExit = new JMenuItem();
			jMenuExit.setMnemonic(KeyEvent.VK_X);
			jMenuExit.setText("Exit");
			jMenuExit.setToolTipText("Close all files and exit");
			jMenuExit.setName("menuExit");
			jMenuExit.setAction(new Actioneer(this, jMenuExit));
		}
		return jMenuExit;
	}

	/**
	 * This method initializes jSplitTop	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitTop() {
		if (jSplitTop == null) {
			jSplitTop = new JSplitPane();
			jSplitTop.setDividerSize(4);
			jSplitTop.setDividerLocation(0.5);
			jSplitTop.setOneTouchExpandable(true);
			jSplitTop.setContinuousLayout(true);
			jSplitTop.setResizeWeight(0.5);
			jSplitTop.setLeftComponent(getImageLeft());
			jSplitTop.setRightComponent(getImageRight());
		}
		return jSplitTop;
	}

	/**
	 * This method initializes jScrollLeft	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	public ImageBox getImageLeft() {
		if (imageLeft == null) {
			imageLeft = new ImageBox();
			imageLeft.setOpaque(true);
		}
		return imageLeft;
	}

	/**
	 * This method initializes jScrollRight	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	public ImageBox getImageRight() {
		if (imageRight == null) {
			imageRight = new ImageBox();
			imageRight.setOpaque(true);
		}
		return imageRight;
	}

	/**
	 * This method initializes jPanelBottom	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
			jPanelBottom = new JPanel();
			jPanelBottom.setOpaque(true);
			SpringLayout layout = new SpringLayout();
			jPanelBottom.setLayout(layout);

			//Create and add the components.
			JButton jbuttonOpen = getJButtonOpen();
			jPanelBottom.add(jbuttonOpen, null);
			layout.putConstraint(SpringLayout.WEST, jbuttonOpen, SIZE_LEFT, SpringLayout.WEST, jPanelBottom);
			layout.putConstraint(SpringLayout.NORTH, jbuttonOpen, SIZE_TOP, SpringLayout.NORTH, jPanelBottom);

			JTextField jtext = getJTextFilename();
			jPanelBottom.add(jtext, null);
			layout.putConstraint(SpringLayout.WEST, jtext, SIZE_LEFT, SpringLayout.EAST, jbuttonOpen);
			layout.putConstraint(SpringLayout.NORTH, jtext, SIZE_TOP, SpringLayout.NORTH, jPanelBottom);
			
			JButton jbuttonConvert = getJButtonConvert();
			jPanelBottom.add(jbuttonConvert, null);
			layout.putConstraint(SpringLayout.WEST, jbuttonConvert, SIZE_LEFT, SpringLayout.EAST, jtext);
			layout.putConstraint(SpringLayout.NORTH, jbuttonConvert, SIZE_TOP, SpringLayout.NORTH, jPanelBottom);
			
			JButton jbuttonCompare = getJButtonCompare();
			jPanelBottom.add(jbuttonCompare, null);
			layout.putConstraint(SpringLayout.WEST, jbuttonCompare, SIZE_LEFT, SpringLayout.EAST, jbuttonConvert);
			layout.putConstraint(SpringLayout.NORTH, jbuttonCompare, SIZE_TOP, SpringLayout.NORTH, jPanelBottom);
			
			JButton jbuttonReload = getJButtonReload();
			jPanelBottom.add(jbuttonReload, null);
			layout.putConstraint(SpringLayout.WEST, jbuttonReload, SIZE_LEFT, SpringLayout.EAST, jbuttonCompare);
			layout.putConstraint(SpringLayout.NORTH, jbuttonReload, SIZE_TOP, SpringLayout.NORTH, jPanelBottom);
			
			JButton jbuttonExit = getJButtonExit();
			jPanelBottom.add(jbuttonExit, null);
			layout.putConstraint(SpringLayout.WEST, jbuttonExit, SIZE_LEFT, SpringLayout.EAST, jbuttonReload);
			layout.putConstraint(SpringLayout.NORTH, jbuttonExit, SIZE_TOP, SpringLayout.NORTH, jPanelBottom);
			
		}
		return jPanelBottom;
	}

	/**
	 * This method initializes jFileChoose	
	 * 	
	 * @return javax.swing.JFileChooser	
	 */
	public JFileChooser getJFileChoose() {
		if (jFileChoose == null) {
			jFileChoose = new JFileChooser();
		}
		return jFileChoose;
	}


	/**
	 * This method initializes jButtonOpen	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getJButtonOpen() {
		if (jButtonOpen == null) {
			jButtonOpen = new JButton();
			jButtonOpen.setName("jButtonOpen");
			jButtonOpen.setToolTipText("Open a graphics file");
			jButtonOpen.setRolloverEnabled(false);
			jButtonOpen.setText("Open");
			jButtonOpen.setAction(new Actioneer(this, jButtonOpen));  

		}
		return jButtonOpen;
	}

	/**
	 * This method initializes jTextFilename	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getJTextFilename() {
		if (jTextFilename == null) {
			jTextFilename = new JTextField();
			jTextFilename.setEditable(false);
			jTextFilename.setToolTipText("Currently selected file");
			jTextFilename.setPreferredSize(new Dimension(600, 28));
		}
		return jTextFilename;
	}
	
	/**
	 * This method initializes jButtonExit	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getJButtonExit() {
		if (jButtonExit == null) {
			jButtonExit = new JButton();
			jButtonExit.setName("jButtonExit");
			jButtonExit.setToolTipText("Exit the program");
			jButtonExit.setRolloverEnabled(false);
			jButtonExit.setText("Exit");
			jButtonExit.setAction(new Actioneer(this, jButtonExit));  

		}
		return jButtonExit;
	}


	/**
	 * This method initializes jButtonConvert	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getJButtonConvert() {
		if (jButtonConvert == null) {
			jButtonConvert = new JButton();
			jButtonConvert.setName("jButtonConvert");
			jButtonConvert.setToolTipText("Convert & compare the image");
			jButtonConvert.setRolloverEnabled(false);
			jButtonConvert.setText("Convert");
			jButtonConvert.setAction(new Actioneer(this, jButtonConvert));  

		}
		return jButtonConvert;
	}

	
	/**
	 * This method initializes jButtonCompare	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getJButtonCompare() {
		if (jButtonCompare == null) {
			jButtonCompare = new JButton();
			jButtonCompare.setName("jButtonCompare");
			jButtonCompare.setToolTipText("Compare the images");
			jButtonCompare.setRolloverEnabled(false);
			jButtonCompare.setText("Compare");
			jButtonCompare.setAction(new Actioneer(this, jButtonCompare));  

		}
		return jButtonCompare;
	}

	/**
	 * This method initializes jButtonReload	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getJButtonReload() {
		if (jButtonReload == null) {
			jButtonReload = new JButton();
			jButtonReload.setName("jButtonReload");
			jButtonReload.setToolTipText("Reload the images");
			jButtonReload.setRolloverEnabled(false);
			jButtonReload.setText("Reload");
			jButtonReload.setAction(new Actioneer(this, jButtonReload));  

		}
		return jButtonReload;
	}
	

	public void setSourceFile(String f) {
		this.sourceFile = f;
	}
	
	public String getSourceFile() {
		return this.sourceFile;
	}
	
	public void setTargetFile(String f) {
		this.targetFile = f;
	}
	
	public String getTargetFile() {
		return this.targetFile;
	}
		
	
	public void exitProgram() {
		this.processWindowEvent(
				new WindowEvent(
						this, WindowEvent.WINDOW_CLOSING));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				(new FrontEnd()).initialize();
			}
		});
	}


}  //  @jve:decl-index=0:visual-constraint="8,10"
