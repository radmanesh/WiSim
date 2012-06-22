package org.frankStyle.wiSim.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.frankStyle.wiSim.WSNeditor;

/**
 * @author Arman
 * 
 */
public class ClassChooserDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -529545053793696599L;
	private final JPanel contentPanel = new JPanel();
	private String parentClassName;
	private Class<?> parentClass;
	DefaultListModel listModel;// = new DefaultListModel();
	JList list;
	String choosedClass;

	/**
	 * Create the dialog.
	 */
	public ClassChooserDialog(String filterClass, String title,
			Window parentWindow) {
		super(parentWindow, title, ModalityType.DOCUMENT_MODAL);
		this.parentClassName = filterClass;
		try {
			parentClass = Class.forName(parentClassName);
		} catch (Exception ex) {
			EditorWindow.showWarning("error in class finding");
		}
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane);
			{
				listModel = new DefaultListModel();
				list = new JList(listModel);
				scrollPane.setViewportView(list);
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						choosedClass = (String) list.getSelectedValue();
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						choosedClass = null;
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		initList();

	}

	void initList() {
		Properties p = System.getProperties();
		String strPath = p.getProperty("java.class.path");
		String strPathSeparator = p.getProperty("path.separator");
		StringTokenizer st = new StringTokenizer(strPath, strPathSeparator);
		while (st.hasMoreTokens()) {
			String strFile = st.nextToken();
			File file = new File(strFile);
			if (file.isDirectory()) {
				processDirectory(file, file);
			} else if (file.isFile()) {
				String strFilename = file.getName();
				if (isJarFile(strFilename)) {
					try {
						JarInputStream is = new JarInputStream(new FileInputStream(file));

						JarEntry entry;
						while ((entry = is.getNextJarEntry()) != null) {
							// java.util.jar.
							if (entry.getName().endsWith(".class")) {
								extractClassFile(entry.getName());
					                
							}
						}
					} catch (Exception e) {
						WSNeditor.handleLog("error loading a jar entry");
					}
				} else if (strFilename.endsWith(".class")) {
					extractClassFile(file, file);
				}
			}
		}
	}


	LinkedList<File> listDirectory(File file) {
		if (file == null) {
			return null;
		}
		String[] filenames = file.list();
		if (filenames == null) {
			return null; // permission denied
		}
		String Path = file.getAbsolutePath();
		LinkedList<File> listFiles = new LinkedList<File>();
		for (int i = 0; i < filenames.length; i++) {
			if ((filenames[i].length() > 0 && filenames[i].charAt(0) == '.')
					|| (filenames[i].indexOf('$') != -1)) // invalid
				// class/Package
				// names
				continue;
			listFiles.add(new File(Path, filenames[i]));
		}
		return listFiles;
	}


	void extractClassFile(File file, File base) {
		try {
			String strFilename = file.getCanonicalPath();
			Properties p = System.getProperties();
			String strFileSeparator = p.getProperty("file.separator");
			String strBaseFilename = base.getCanonicalPath() + strFileSeparator;
			if (strFilename.endsWith(".class") && (!isInnerClass(strFilename))
					&& strFilename.startsWith(strBaseFilename)) {
				// addClassToList(filenameToClassName(strFilename.substring(strBaseFilename.length())));
				String className = filenameToClassName(strFilename
						.substring(strBaseFilename.length()));
				// System.out.println("*******class*******>>>>>>>>>>>>>>" +
				// strFilename);


				try {
					if (className.endsWith("ObjectDuplicable"))
						System.out.println(">>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<");
					Class<?> cl = this.getClass().getClassLoader().loadClass(className);
					if (parentClass.isAssignableFrom(cl)) {
						// System.out.println("    YES!!!!!   *************");
						// System.out.println(className);
						listModel.addElement(className);

					}
				} catch (Exception e) {
					System.out.println("\n\n\n\n\n\n\n\n" + className);
					e.printStackTrace();
				}
 catch (Error er) {
					System.out.println("EROR****************");
					er.printStackTrace();
				}
			}
		} catch (Exception e) {
		}
	}

	void extractClassFile(String strFilename) {
		try {

			if (!isInnerClass(strFilename)) {

				// addClassToList(filenameToClassName(strFilename.substring(strBaseFilename.length())));
				String className = filenameToClassName(strFilename);
				// className = className.substring(className.lastIndexOf('.') +
				// 1);
				// System.out.print(className);
				Class<?> cl, parent;
				try {
					cl = Class.forName(className);
					parent = Class.forName(parentClassName);
					if (parent.isAssignableFrom(cl)) {
						// System.out.println("    YES!!!!!   *************");
						// System.out.println(className);
						listModel.addElement(className);

					}
				} catch (Exception exception) {
					WSNeditor.handleLog(Level.WARNING, exception.getMessage());
				} catch (Error er) {
					WSNeditor.handleLog(Level.WARNING, er.getMessage());
				}
			}
		} catch (Exception ex) {
			WSNeditor.handleLog(Level.WARNING, ex.getMessage());
		}
	}

	void processDirectory(File file, File base) {
		LinkedList<File> listFiles = listDirectory(file);
		if (listFiles == null)
			return;
		ListIterator<File> iter = listFiles.listIterator();
		while (iter.hasNext()) {
			File fileNext = (File) iter.next();
			if (fileNext.isDirectory()) {
				processDirectory(fileNext, base);
			} else if (fileNext.isFile()) {
				extractClassFile(fileNext, base);
			}
		}
	}

	boolean isJarFile(String filename) {
		int start = filename.length() - 4;
		if (start <= 0)
			return false;
		String suffix = filename.substring(start);
		if (suffix.equalsIgnoreCase(".zip") || suffix.equalsIgnoreCase(".jar"))
			return true;
		return false;
	}

	String filenameToClassName(String s) {
		String className = s.replace('/', '.').substring(0, s.length() - 6);
		className = className.replace('\\', '.');
		return className;
	}

	boolean isInnerClass(String filename) {
		if (filename.indexOf('$') == -1) {
			return false;
		}
		return true;
	}

}
