import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

/**
  *
  * Beschreibung
  *
  * @version 1.0 vom 22.03.2012
  * @author
  */

public class gui extends JFrame {
  // Anfang Attribute
    private profile dialog;
    private String[] jList1Data = {"Entry 1","Entry 2","Entry 3","Entry 4",};
  private ArrayList<String> test = new ArrayList<String>();
  private JList jList1 = new JList(jList1Data);
  private JCheckBox actParser = new JCheckBox();
  private JButton Del = new JButton();
  private JButton jButton1 = new JButton();
  private JTextField parsed = new JTextField();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private String[] jcProfileData = {"Profile 1"};
  private JComboBox jcProfile = new JComboBox(jcProfileData);
  private JButton jbPlus = new JButton();
  private JButton jbMinus = new JButton();
  // Ende Attribute

  public gui(String title) {
    // Frame-Initialisierung
    super(title);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    int frameWidth = 239;
    int frameHeight = 508;
    setSize(frameWidth, frameHeight);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (d.width - getSize().width) / 2;
    int y = (d.height - getSize().height) / 2;
    setLocation(x, y);
    Container cp = getContentPane();
    cp.setLayout(null);
    // Anfang Komponenten

    jList1.setBounds(16, 32, 185, 225);
    cp.add(jList1);
    actParser.setBounds(16, 368, 17, 25);
    actParser.setText("");
    cp.add(actParser);
    Del.setBounds(168, 264, 35, 25);
    Del.setText("Del");
    Del.setMargin(new Insets(2, 2, 2, 2));
    Del.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Del_ActionPerformed(evt);
      }
    });
    cp.add(Del);
    jButton1.setBounds(40, 368, 99, 25);
    jButton1.setText("Parser");
    jButton1.setMargin(new Insets(2, 2, 2, 2));
    jButton1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
               new parser("Parser");
        }
    });
    cp.add(jButton1);
    parsed.setBounds(16, 432, 185, 24);
    parsed.setText("Text");
    cp.add(parsed);
    jLabel1.setBounds(16, 8, 114, 16);
    jLabel1.setText("Clipboard History");
    jLabel1.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
    cp.add(jLabel1);
    jLabel2.setBounds(16, 408, 66, 16);
    jLabel2.setText("parsed text");
    jLabel2.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
    cp.add(jLabel2);
    jcProfile.setBounds(16, 328, 121, 24);
    cp.add(jcProfile);
    jbPlus.setBounds(144, 328, 27, 25);
    jbPlus.setText("+");
    jbPlus.setMargin(new Insets(2, 2, 2, 2));
    jbPlus.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        jbPlus_ActionPerformed(evt);
      }
    });
    cp.add(jbPlus);
    jbMinus.setBounds(176, 328, 27, 25);
    jbMinus.setText("-");
    jbMinus.setMargin(new Insets(2, 2, 2, 2));
    jbMinus.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        jbMinus_ActionPerformed(evt);
      }
    });
    cp.add(jbMinus);
    // Ende Komponenten

    setResizable(false);
    setVisible(true);
  }
  
  // Fügt ein neues Profil hinzu
  public void addNewProfile(String n) {
         jcProfile.insertItemAt(n, 0);
         jcProfile.setSelectedIndex(0);
  }

  // Anfang Methoden
  public void Del_ActionPerformed(ActionEvent evt) {
    // TODO hier Quelltext einfügen
  }


  public void jbMinus_ActionPerformed(ActionEvent evt) {
         jcProfile.removeItemAt(jcProfile.getSelectedIndex());
  }

  public void jbPlus_ActionPerformed(ActionEvent evt) {
         dialog = new profile(this,"New parser",true);
         addNewProfile(dialog.getInput());
  }

  // Ende Methoden

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    new gui("Clipboard");
  }
}
