// make some sort of gui
// gates logic 



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;

public class GUI extends JFrame implements ActionListener {
  private JTextField andGatesInfo;
  private JTextField xOrGatesInfo;
  private JTextField orGatesInfo;
  private JTextField notGatesInfo;
  private JTextField nandGatesInfo;
  private JTextField norGatesInfo;
  private JTextField xnOrGatesInfo;
  private boolean mouseHolding;

  private JTextField field;
  private JButton notGate;
  private JButton andGate;
  private JButton orGate;
  private JButton xOrGate;
  private JButton nandGate;
  private JButton norGate;
  private JButton xnOrGate;

  private notGates[] notGateArr;
  private andGates[] andGateArr;
  private orGates[] orGateArr;
  private xOrGates[] xOrGateArr;
  private andGates[] nandGateArr;
  private orGates[] norGateArr;
  private xOrGates[] xnOrGateArr;
  private JButton[] notButtArr;
  private JButton[] andButtArr;
  private JButton[] orButtArr;
  private JButton[] xOrButtArr;
  private JButton[] nandButtArr;
  private JButton[] norButtArr;
  private JButton[] xnOrButtArr;

  public GUI() throws Exception {
    super("Computing");
    Icon notGateIcon = new ImageIcon("notGate.png");
    Icon andGateIcon = new ImageIcon("andGate.png");
    Icon orGateIcon = new ImageIcon("orGate.png");
    Icon xOrGateIcon = new ImageIcon("xOrGate.png");
    Icon nandGateIcon = new ImageIcon("nandGate.png");
    Icon norGateIcon = new ImageIcon("norGate.png");
    Icon xnOrGateIcon = new ImageIcon("xnorGate.png");
    setBounds(0, 0, 500, 500);// creates actual window
    setLayout(null);
    setVisible(true);
    field = new JTextField();
    notGate = new JButton(notGateIcon);
    andGate = new JButton(andGateIcon);
    orGate = new JButton(orGateIcon);
    xOrGate = new JButton(xOrGateIcon);
    field.setBounds(400, 100, 100, 300);
    field.setVisible(true);
    this.add(field);
    field.setBackground(new Color(0, 0, 0, 0));
    notGate.setBounds(425, 125, 25, 25);
    notGate.setVisible(true);
    this.add(notGate);
    andGate.setBounds(425, 175, 25, 25);
    andGate.setVisible(true);
    this.add(andGate);
    orGate.setBounds(425, 225, 25, 25);
    orGate.setVisible(true);
    this.add(orGate);
    xOrGate.setBounds(425, 275, 25, 25);
    xOrGate.setVisible(true);
    this.add(xOrGate);

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == notGate) {
      notGates[] copy = new notGates[notGateArr.length + 1];
      System.arraycopy(notGateArr, 0, copy, 0, notGateArr.length);
      notGateArr = copy;
      notGateArr[notGateArr.length] = new notGates();
      JButton[] copyDos = new JButton[notButtArr.length +1];
      System.arraycopy(notButtArr,0,copyDos,0,notButtArr.length);
      int length= notButtArr.length;
      notButtArr[length]= new JButton();
      notButtArr[length].setBounds(245, 255,10,10);
      notButtArr[length].setVisible(true);
      notButtArr[length].addMouseListener(notMousePresser);
      this.add(notButtArr[length]);
    }
  if (e.getSource() == andGate) {
      andGates[] copy = new andGates[andGateArr.length + 1];
      System.arraycopy(andGateArr, 0, copy, 0, andGateArr.length);
      andGateArr = copy;
      andGateArr[andGateArr.length] = new andGates();
      JButton[] copyDos = new JButton[andButtArr.length +1];
      System.arraycopy(andButtArr,0,copyDos,0,andButtArr.length);
      int length= andButtArr.length;
      andButtArr[length]= new JButton();
      andButtArr[length].setBounds(245, 255,10,10);
      andButtArr[length].setVisible(true);
      this.add(notButtArr[length]);
    }
    if (e.getSource() == notGate) {
      notGates[] copy = new notGates[notGateArr.length + 1];
      System.arraycopy(notGateArr, 0, copy, 0, notGateArr.length);
      notGateArr = copy;
      notGateArr[notGateArr.length] = new notGates();
    }
    if (e.getSource() == notGate) {
      notGates[] copy = new notGates[notGateArr.length + 1];
      System.arraycopy(notGateArr, 0, copy, 0, notGateArr.length);
      notGateArr = copy;
      notGateArr[notGateArr.length] = new notGates();
    }
    if (e.getSource() instanceof notGates) {
      notGates gate = (notGates) e.getSource();
      // get gate id
    }
  }

  MouseListener notMousePresser= new MouseListener(){
    public void mousePressed(MouseEvent e) {
        mouseHolding = true;
        while (mouseHolding = true) {
        int i= notButtArr.indexOf(e.getSource());
        e.getSource().setLocation(MouseInfo.getPointerInfo().getLocation());
        }
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        mouseHolding = false;
      }
  }

  public void mouseHover() {
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {

      }

      @Override
      

      @Override
      public void mouseEntered(MouseEvent e) {
        if (e.getSource().equals(notGate)) {
          notGatesInfo.setVisible(true);
        }
        if (e.getSource().equals(xOrGate)) {
          xOrGatesInfo.setVisible(true);
        }
        if (e.getSource().equals(andGate)) {
          andGatesInfo.setVisible(true);
        }
        if (e.getSource().equals(orGate)) {
          orGatesInfo.setVisible(true);
        }

      }

      @Override
      public void mouseExited(MouseEvent e) {
        if (e.getSource().equals(notGate)) {
          notGatesInfo.setVisible(false);
        }
        if (e.getSource().equals(xOrGate)) {
          xOrGatesInfo.setVisible(false);
        }
        if (e.getSource().equals(andGate)) {
          andGatesInfo.setVisible(false);
        }
        if (e.getSource().equals(orGate)) {
          orGatesInfo.setVisible(false);
        }
      }
    });
  }
}
