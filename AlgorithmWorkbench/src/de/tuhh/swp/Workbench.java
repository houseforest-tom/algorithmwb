/**
 * <=========================================================================================>
 * File: IPrintable.java
 * Created: 16.12.2015
 * Author: HAUSWALD, Tom.
 * <=========================================================================================>
 */
package de.tuhh.swp;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * TODO: Document this type.
 */
public class Workbench
{

	private JFrame frame;
	private JButton loadLabelsButton;
	private JButton loadImagesButton;
	private JButton loadButton;
	private String labelsFilePath;
	private String imagesFilePath;

	/**
	 * Launch the application.
	 */
	public static void main( String[] args )
	{
		EventQueue.invokeLater( new Runnable()
		{
			public void run()
			{
				try
				{
					Workbench window = new Workbench();
					window.frame.setVisible( true );
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
			}
		} );
	}

	/**
	 * Create the application.
	 */
	public Workbench()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		int width = screenWidth / 2;
		int height = (int) ( width / 16.0f * 9.0f );

		frame = new JFrame();
		frame.setBounds( ( screenWidth - width ) / 2, ( screenHeight - height ) / 2, width, height );
		frame.setLayout( new FlowLayout() );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		frame.add( new JLabel( "Load Training Data:" ) );

		// Load labels file.
		loadLabelsButton = new JButton( "Select Labels" );
		loadLabelsButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				JFileChooser chooser = new JFileChooser( new File( "./" ) );
				chooser.setVisible( true );
				if( chooser.showOpenDialog( frame ) == JFileChooser.APPROVE_OPTION )
				{
					labelsFilePath = chooser.getSelectedFile().getAbsolutePath();
				}
			}
		} );
		setComponentSize( loadLabelsButton, 140, 32 );
		frame.add( loadLabelsButton );

		// Load images file.
		loadImagesButton = new JButton( "Select Images" );
		loadImagesButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				JFileChooser chooser = new JFileChooser( new File( "./" ) );
				chooser.setVisible( true );
				if( chooser.showOpenDialog( frame ) == JFileChooser.APPROVE_OPTION )
				{
					imagesFilePath = chooser.getSelectedFile().getAbsolutePath();
				}
			}
		} );
		setComponentSize( loadImagesButton, 140, 32 );
		frame.add( loadImagesButton );

		// Load the training samples using the specified labels and images files.
		loadButton = new JButton( "Load" );
		loadButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent event )
			{
				try
				{
					byte[] labelFile = Files.readAllBytes( Paths.get( labelsFilePath ) );
					byte[] labels = new LabelConverter().toInternal( labelFile );
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
			}
		} );
		setComponentSize( loadButton, 80, 32 );
		frame.add( loadButton );
	}

	// ===========================================================
	// Constants
	// ===========================================================

	;;

	// ===========================================================
	// Fields
	// ===========================================================

	;;

	// ===========================================================
	// Constructors
	// ===========================================================

	;;

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	;;

	// ===========================================================
	// Override Methods
	// ===========================================================

	;;

	// ===========================================================
	// Methods
	// ===========================================================

	public void setComponentSize( JComponent component, int width, int height )
	{
		Dimension dim = new Dimension( width, height );
		component.setPreferredSize( dim );
		component.setSize( dim );
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	;;

}
