package org.frankStyle.wiSim;

import java.awt.EventQueue;
import java.io.File;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;

import org.frankStyle.wiSim.view.EditorWindow;

/**
 * @author Arman
 * 
 */
public class WSNeditor {
	public static File ImageDir = null;
	public static String ImageDirPrefix = null;
	public static String FrameImageDirPrefix = null;
	protected static File projectFile;
	public static String projectPath;
	private static Logger logger = Logger.getLogger("org.frankStyle.wiSim");
	private static FileHandler logFileHandler;

	private static void createAndShowGUI(final String args[]) {
		Locale.setDefault(Locale.US);
		try {
			logFileHandler = new FileHandler("wiSim.log");
			logger.addHandler(logFileHandler);
			logger.setLevel(Level.ALL);
		} catch (Exception e) {
			logger.setLevel(Level.ALL);
			logger.log(Level.WARNING, "trouble logging to file", e);
		}

		WSNeditor.projectFile = new File(WSNeditor.class.getResource(
				"WSNeditor.class").getPath());

		WSNeditor.ImageDir = new File(projectFile.getParentFile()
				.getParentFile().getParentFile().getParentFile()
				.getParentFile(), "icons");
		WSNeditor.projectPath = projectFile.getParentFile().getParentFile()
				.getParentFile().getParentFile().getPath();
		try {
			WSNeditor.ImageDirPrefix = WSNeditor.ImageDir.getCanonicalPath()
					+ System.getProperty("file.separator");
		} catch (Exception io) {
			logger.log(Level.SEVERE,
					"Problem with the canonical path of the image directory",
					io);
			System.out
					.println("Problem with the canonical path of the image directory");
		}
		WSNeditor.FrameImageDirPrefix = WSNeditor.ImageDirPrefix
				+ "frame_images" + System.getProperty("file.separator");
		if (WSNeditor.ImageDir.exists() == false) // $codepro.audit.disable
		// equalityTestWithBooleanLiteral
		{
			handleLog("Cannot access "
					+ WSNeditor.ImageDir.getAbsolutePath());
			// return;
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) // java.lang.ClassNotFoundException e)
		{
			// Can't change look and feel
			logger.log(Level.WARNING, "Can not change look and feel", e);
			System.out.println("Can not change look and feel");
		}
		if (args.length > 1) {
			handleLog("usage: java gEditor [xml file]");
			System.out.println("usage: java gEditor [xml file]");
			return;
		}

		EditorWindow m_frame = new EditorWindow(args);
		m_frame.pack();
		m_frame.setVisible(true);
		m_frame.validate();
		m_frame.setResizable(false);

	}

	public static void handleLog(Level logLevel, String msg, Throwable occuredException) {
		logger.log(logLevel, msg, occuredException);
	}
	public static void handleLog(Level logLevel, String msg) {
		logger.log(logLevel, msg);
	}
	public static void handleLog(String msg) {
		System.out.println("*********>>>>>" + msg);
		logger.log(Level.INFO, msg);
	}

	public static void main(final String args[]) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					WSNeditor.createAndShowGUI(args);
				} catch (Exception e) {
					logger.log(Level.SEVERE, e.getMessage());
				} catch (Error er) {
					logger.log(Level.SEVERE, er.getMessage());
				}
			}
		});
	}
}

