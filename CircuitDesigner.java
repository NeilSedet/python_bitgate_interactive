// https://www.daniweb.com/programming/software-development/threads/419761/logic-circuit-sim-drag-drop
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/* Runnable demo of Java techniques for an electrical circuit design application
 * has NAND gates with connections etc and a drag'n'drop GUI
 * Main classes:
 * Component - abstract class for gates etc. Has inputs & outputs and a drawing method 
 *   NANDGate - concrete Component - you can create others for NOR etc
 * Connector - abstract class for inputs and outputs
 *   Input, Output - concrete Connectors
 * Connection - joins two Connectors (joins one Input to one Output)
 * 
 * These classes have the logic for GUI interaction (dragging, selecting Connectors etc)
 * but also have the potential for simulating the actual circuit behaviour.
 * 
 */

public class CircuitDesigner {

   // NAND gate drag'n'drop demo with connections etc

   JFrame frame = new JFrame("Logic Gate Simulator :D");

   ArrayList<Component> components = new ArrayList<Component>();
   ArrayList<Connection> lines = new ArrayList<Connection>();

   String defaultLabel = "Welcome. Add a component to start";
   JLabel prompt = new JLabel(defaultLabel);
   DrawPanel drawPanel = new DrawPanel();

   //boolean showInputs = true, showOutputs = true;
   boolean addingComponent = false, selectingOutput = false, selectingInput = false;
   //This is the holder of user selection
   Component selectedInput = null;
		   
		   
   int gateChoice = 0;
   //boolean iNode, oNode = true; // this is a bit trippy but the iNode is for the output gate and the oNode is
                                // for the input gate (think about it for a bit and it'll make sense)
   
   static Color TRUE_COLOR = Color.green;
   static Color FALSE_COLOR = Color.red;
   static Color HIGHTLIGHT_COLOR = Color.yellow;
   static Color UNDECIDED_COLOR = Color.lightGray;
   static int INIT_WIDTH = 70, INIT_HEIGHT = 30;
   
   /**
   * 
   */
   CircuitDesigner() {
	  components.add(new IN(30, 70));
      frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // https://stackoverflow.com/questions/46804305/jframe-in-full-screen-without-undecorated
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      frame.add(prompt, BorderLayout.PAGE_START);
      drawPanel.setLayout(null);
      frame.add(drawPanel);
      drawPanel.add(components.get(0));
      prompt.setText("Use mouse to drag components");
      drawPanel.repaint();

      // Variables (I think)
      JPanel buttonPanel = new JPanel();
      frame.add(buttonPanel, BorderLayout.PAGE_END);
      
     

      // JButton newComponentButton = new JButton("Add a Component");
      // buttonPanel.add(newComponentButton);
      // newComponentButton.addActionListener(new ActionListener() {

      // @Override
      // public void actionPerformed(ActionEvent arg0) {
      // addNewComponent();

      // }
      // });

      // Button to enable gate connecting
      JButton newConnectionButton = new JButton("Add a Connection");
      buttonPanel.add(newConnectionButton);
      newConnectionButton.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent arg0) {
            addNewConnection();
         }
      });

      // Button to spawn gates
      JButton andButton = new JButton("And Gate"), nandButton = new JButton("Nand Gate"),
            orButton = new JButton("Or Gate"), norButton = new JButton("Nor Gate"), xorButton = new JButton("Xor Gate"),
            xnorButton = new JButton("Xnor Gate"), notButton = new JButton("Not Gate"), IN = new JButton("INPUT");
            
      buttonPanel.add(andButton);
      buttonPanel.add(nandButton);
      buttonPanel.add(orButton);
      buttonPanel.add(norButton);
      buttonPanel.add(xorButton);
      buttonPanel.add(xnorButton);
      buttonPanel.add(notButton);
      buttonPanel.add(IN);
      
      andButton.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent arg0) {
            addNewComponent();
            gateChoice = 1;
         }
      });
      nandButton.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent arg0) {
            addNewComponent();
            gateChoice = 2;
         }
      });
      orButton.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent arg0) {
            addNewComponent();
            gateChoice = 3;
         }
      });
      norButton.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent arg0) {
            addNewComponent();
            gateChoice = 4;
         }
      });
      xorButton.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent arg0) {
            addNewComponent();
            gateChoice = 5;
         }
      });
      xnorButton.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent arg0) {
            addNewComponent();
            gateChoice = 6;
         }
      });
      notButton.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent arg0) {
            addNewComponent();
            gateChoice = 7;
         }
      });
      IN.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent arg0) {
            addNewComponent();
            gateChoice = 8;
         }
      });      

      frame.setVisible(true);
   }

   void addNewComponent() {
      prompt.setText("Click to add component");
      addingComponent = true;
   }

   void addNewConnection() {
      prompt.setText("Click the component to output...");
      selectingOutput = false;
      selectingInput = true;
      drawPanel.repaint();
   }

   abstract class Component extends JLabel implements MouseListener, MouseMotionListener {

      // A component is a movable draggable object with inputs and outputs

      ArrayList<Input> inputs = new ArrayList<Input>();
      ArrayList<Output> outputs = new ArrayList<Output>();
      boolean outputValue;
      

      public Component(String text, int x, int y) {
         super(text);
         addMouseListener(this);
         addMouseMotionListener(this);
      }

      protected void addInput(Input i)
      {
    	  inputs.add(i);
    	  updateConnectorPosition();
      }
      public abstract void updateInput();
      public void updateOutput()
      {
			for (Output outputConnector : outputs)
			{
				outputConnector.connector_value = outputValue;
				if (!outputConnector.isAvailable())
				{
					Input input= outputConnector.connection.input;
					input.connector_value = outputConnector.connector_value;
					Component comm = outputConnector.connection.input.component;
					comm.updateInput();
				}
			}
      }
      
      protected boolean getOutputValue()
      {
    	  return outputValue;
      }
      protected Output addOutput()
      {
    	  Output out = new Output(this);
    	  outputs.add(out);
    	  updateConnectorPosition();
    	  return out;
      }
      protected boolean allowMultipleInput()
      {
    	  return true;
      }
      protected boolean allowMultipleOutput()
      {
    	  return true;
      }
      //This method check the existence of a connection 
      protected boolean connectionExist(Component c)
      {
    	  for (Output output : outputs)
    	  {
    		  if (output.connection.input.component.equals(c))
    			  return true;
    	  }
    	  return false;
      }
      
      protected void updateConnectorPosition() {
    	  //Depending on the number of connectors, adjust the component height, rearrange the connectors
    	  int maxOfConnectors = Math.max(inputs.size(), outputs.size()) - 1;
    	  this.setBounds(getX(), getY(), INIT_WIDTH , INIT_HEIGHT + Connector.h * maxOfConnectors);
    	  int inputSectionHeight = this.getHeight() / 2;
    	  int outputSectionHeight = this.getHeight() / 2;
    	  if (inputs.size() != 0)
    		  inputSectionHeight = this.getHeight() / inputs.size() / 2;
    	  if (outputs.size() != 0)
    		  outputSectionHeight = this.getHeight() / outputs.size() / 2;
    	  
    	  for (int i = 1; i <= inputs.size(); i++)
    	  {
	    		 Input input = inputs.get(i-1);
	    		 Polygon p = new Polygon();
				 p.addPoint(Connector.w, inputSectionHeight * i - Connector.h / 2);
				 p.addPoint(Connector.w, inputSectionHeight * i + Connector.h / 2);
				 p.addPoint(0, inputSectionHeight * i);
				 input.shape = p; 
				 input.y = inputSectionHeight * i;
    	  }
    	  for (int i = 1; i <= outputs.size(); i++)
    	  {
	    		 Output output = outputs.get(i-1);
	    		 Polygon p = new Polygon();
				 p.addPoint(INIT_WIDTH - Connector.w, outputSectionHeight * i - Connector.h / 2);
				 p.addPoint(INIT_WIDTH - Connector.w, outputSectionHeight * i + Connector.h / 2);
				 p.addPoint(INIT_WIDTH, outputSectionHeight * i);
				 output.shape = p;
				 output.y = outputSectionHeight * i;
    	  }
      }

      @Override
      public void paintComponent(Graphics g) {
         // can do custom drawing here, but just show JLabel text for now...
         super.paintComponent(g);
         Graphics2D g2d = (Graphics2D) g;
         for (Input in : inputs) {
        	 in.paintConnector(g2d);           
         }
         for (Output out:outputs) {
        	 out.paintConnector(g2d);
         }
                
      }

      int startDragX, startDragY;
      boolean inDrag = false;

      @Override
      public void mouseEntered(MouseEvent e) {
         // not interested
      }

      @Override
      public void mouseExited(MouseEvent e) {
         // not interested
      }

      @Override
      public void mousePressed(MouseEvent e) {
         startDragX = e.getX();
         startDragY = e.getY();
      }

      @Override
      public void mouseReleased(MouseEvent e) {
         if (inDrag) {
            System.out.println("\"" + getText().trim() + "\" dragged to " + getX() + ", " + getY());
            inDrag = false;
         }
      }

      @Override
      public void mouseClicked(MouseEvent e) {
         if (selectingInput) {
        	 //This will select a component as an input. Highlight the component in yellow. Set it to SelectedInput.
            System.out.println("Click on input component of \"" + getText().trim() + "\"");
        	if (!allowMultipleOutput()) {
        		System.out.println("Error: component not allowed to output");
          	  	return;
        	}
        	setBackground(Color.yellow);
        	this.setOpaque(true);
            selectedInput = this;
            drawPanel.repaint();
            selectingInput = false;
            selectingOutput = true;
            prompt.setText("Click the component to receive the output...");
            frame.repaint();
            return;
         }
         if (selectingOutput) {
        	 //User click a component to act as a receiver of the input component
        	 // 1. Find the component
        	 // 2. Create output from input Component
        	 // 3. Create connection for those two connectors, ripple down to update all the components
        	 // 4. clear the highlight of the input component
             System.out.println("Click on the component to be connected \"" + getText().trim() + "\"");
             if (this.equals(selectedInput))
             {
            	 System.out.println("Error: Can't create self connection.");
            	 selectingOutput = false;
                 selectingInput = false;
                 selectedInput.setBackground(Color.white);
                 selectedInput.setOpaque(false);
                 prompt.setText(defaultLabel);
                 frame.repaint();
	           	 return;
             }
             if (selectedInput.connectionExist(this))
             {
            	 System.out.println("Error: There is already a connection.");
	           	  return;
             }
             if (!allowMultipleInput())
             {
	           	  System.out.println("Error: component is not allowed to accept input");
	           	  return;
             }
             
             Output comOutput = selectedInput.addOutput();
             Input comInput = new Input(this);
             addInput(comInput);
             Connection conn = new Connection(comOutput, comInput);
             lines.add(conn);
             selectingOutput = false;
             selectingInput = false;
             selectedInput.setBackground(Color.white);
             selectedInput.setOpaque(false);
             prompt.setText(defaultLabel);
             frame.repaint();
             return;
         }
         else
         {
        	 if (getClass().getName().contains("OUT"))
             {
	           	  System.out.println("Click on the component to flip output value\"" + getText().trim() + "\"");
	           	  this.outputValue = !outputValue;
	           	  updateOutput();
	           	  frame.repaint();
	           	  return;
            }
         }
      }

      @Override
      public void mouseDragged(MouseEvent e) {
         int newX = getX() + (e.getX() - startDragX);
         int newY = getY() + (e.getY() - startDragY);
         setLocation(newX, newY);
         inDrag = true;
         frame.repaint();
      }

      @Override
      public void mouseMoved(MouseEvent arg0) {
         // not interested
      }      
      
      @Override
      public boolean equals(Object o) {
         if (o == null)
        	 return false;
         if (o instanceof Component)
         {
        	 Component c = (Component) o;
        	 return this.getText().trim().equals(c.getText().trim());
         }
         return false;
      } 

   }

   class NANDgate extends Component {

      NANDgate(int x, int y) {
         super("    NAND " + (components.size() + 1), x, y);

         setVerticalAlignment(SwingConstants.CENTER);
         setBounds(x, y, INIT_WIDTH, INIT_HEIGHT);
         setBorder(new LineBorder(Color.black, 1));    
         
      }

	@Override
	public void addInput(CircuitDesigner.Input i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateInput() {
		// TODO Auto-generated method stub
		
	}
	
   }

   class ANDgate extends Component {

      ANDgate(int x, int y) {
         super("    AND " + (components.size() + 1), x, y);

         setVerticalAlignment(SwingConstants.CENTER);
         setBounds(x, y, INIT_WIDTH, INIT_HEIGHT);
         setBorder(new LineBorder(Color.black, 1));
         
      }

	@Override
	public void updateInput() {
		boolean result = true;
		for (Input inputConnector : inputs)
		{
			if (!inputConnector.isAvailable())
			{
				result = result && inputConnector.connector_value;
			}
		}
		this.outputValue = result;
		updateOutput();
	}
   }

   class ORgate extends Component {

      ORgate(int x, int y) {
         super("    OR " + (components.size() + 1), x, y);

         setVerticalAlignment(SwingConstants.CENTER);
         setBounds(x, y, INIT_WIDTH, INIT_HEIGHT);
         setBorder(new LineBorder(Color.black, 1));
         
      }

	@Override
	public void addInput(CircuitDesigner.Input i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateInput() {
		// TODO Auto-generated method stub
		
	}
   }

   class NORgate extends Component {

      NORgate(int x, int y) {
         super("    NOR " + (components.size() + 1), x, y);

         setVerticalAlignment(SwingConstants.CENTER);
         setBounds(x, y, INIT_WIDTH, INIT_HEIGHT);
         setBorder(new LineBorder(Color.black, 1));
         
      }

	@Override
	public void addInput(CircuitDesigner.Input i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateInput() {
		// TODO Auto-generated method stub
		
	}
   }

   class XORgate extends Component {

      XORgate(int x, int y) {
         super("    XOR " + (components.size() + 1), x, y);

         setVerticalAlignment(SwingConstants.CENTER);
         setBounds(x, y, INIT_WIDTH, INIT_HEIGHT);
         setBorder(new LineBorder(Color.black, 1));
         
      }

	@Override
	public void addInput(CircuitDesigner.Input i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateInput() {
		// TODO Auto-generated method stub
		
	}
   }

   class XNORgate extends Component {

      XNORgate(int x, int y) {
         super("    XNOR " + (components.size() + 1), x, y);

         setVerticalAlignment(SwingConstants.CENTER);
         setBounds(x, y, INIT_WIDTH, INIT_HEIGHT);
         setBorder(new LineBorder(Color.black, 1));
         
      }

	@Override
	public void addInput(CircuitDesigner.Input i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateInput() {
		// TODO Auto-generated method stub
		
	}
   }

   class NOTgate extends Component {

      NOTgate(int x, int y) {
         super("    NOT " + (components.size() + 1), x, y);

         setVerticalAlignment(SwingConstants.CENTER);
         setBounds(x, y, INIT_WIDTH, INIT_HEIGHT);
         setBorder(new LineBorder(Color.black, 1));

      }

	@Override
	protected boolean allowMultipleInput()
    {
  	  	return inputs.isEmpty();
    }
	
	
	@Override
	public void updateInput() {
		// TODO Auto-generated method stub
		
	}
   }

   //This is a component without input. 
   //User can click it to change the output value
   class OUT extends Component {

      OUT(int x, int y, boolean value) {
         super(" INPUT " + (components.size() + 1), x, y);

         setVerticalAlignment(SwingConstants.CENTER);
         setBounds(x, y, INIT_WIDTH, INIT_HEIGHT);
         setBorder(new LineBorder(Color.red, 2));
         
         this.outputValue = value;        
         
      }

	@Override
	protected void addInput(Input in)
    {
  	  	//Do nothing on the signal input component
		return;
    }
	
	@Override
	protected boolean allowMultipleInput()
    {
  	  return false;
    }
	
	@Override
    public void paintComponent(Graphics g) {
       //Final result component will have background based on the result boolean value
       super.paintComponent(g); 
       if (outputValue)
       {
    	   setBorder(new LineBorder(TRUE_COLOR, 2));
       }
       else
    	   setBorder(new LineBorder(FALSE_COLOR, 2));
    }

	@Override
	public void updateInput() {
		updateOutput();		
	}
   }
   
 //This is the final component showing the output. 
  
   class IN extends Component {

      IN(int x, int y) {
         super("  OUTPUT " + (components.size() + 1), x, y);

         setVerticalAlignment(SwingConstants.CENTER);
         setBounds(x, y, INIT_WIDTH, INIT_HEIGHT);
         setBorder(new LineBorder(UNDECIDED_COLOR, 5));
                 
      }

	@Override
    public void paintComponent(Graphics g) {
       //Final result component will have background based on the result boolean value
       super.paintComponent(g);
       if (outputValue)
       {
    	   setBorder(new LineBorder(TRUE_COLOR, 5));;
       }
       else
    	   setBorder(new LineBorder(FALSE_COLOR, 5));;
    }

	@Override
	public void updateInput() {
		outputValue = inputs.get(0).connector_value;		
	}
	
	@Override
	protected boolean allowMultipleOutput()
    {
  	  return false;
    }

   }

   abstract class Connector { // subclasses: Input, Output

      Component component;
      Connection connection;
      
      //This is the default value of the connector.
      boolean connector_value = false;

      int x, y; // x,y is the point where lines connect
      static int w = 10, h = 10; // overall width & height of Connector
      Shape shape;
      

      Connector(Component c) {
         this.component = c;         
      }

      //This is the method of checking whether the connector is has connection
      boolean isAvailable() {
    	  return connection == null;
      }

    //This is the abstract method of adding connection to connector
      abstract void addConnection(Connection con); 

      public int getX() {
         return component.getX() + x;
      }

      public int getY() {
         return component.getY() + y;
      }

      public void paintConnector(Graphics2D g2d) {    	  
         if (isAvailable())
            g2d.setColor(UNDECIDED_COLOR);
         else
         {
        	if (connector_value) 
        	{
        		g2d.setColor(TRUE_COLOR);
        	}
        	else
        		g2d.setColor(FALSE_COLOR);
         }
            
         g2d.fill(shape);
      }

   }

   class Output extends Connector {

      // Triangle, left facing, at RHS of component (points away from component)

      Output(Component owner) {
         super(owner);
         this.x = owner.getWidth(); // RHS of Component
         this.y = owner.getHeight() / 2;
         Polygon p = new Polygon();
         p.addPoint(x - w, this.y - h / 2);
         p.addPoint(x - w, this.y + h / 2);
         p.addPoint(x, this.y);
         shape = p;   
         //Set the boolean value to the component's output value
         this.connector_value = owner.outputValue;
      }
      public void paintConnector(Graphics2D g2d) {
    	  if (component.outputValue)
    	  {
    		  g2d.setColor(TRUE_COLOR);
    	  }
    	  else
    		  g2d.setColor(FALSE_COLOR);
          g2d.fill(shape);
       }
      
	@Override
	void addConnection(CircuitDesigner.Connection con) {
		// Output connector add connection:
		// Check the availability
		if (isAvailable())
		{
			connection = con;			
		}
	}

   }

   class Input extends Connector {

      // Triangle, left facing, at LHS of component (points into the component)

      Input(Component owner) {
  
         super(owner);
         this.x = 0; // LHS of Component
         this.y = owner.getHeight() / 2;
         Polygon p = new Polygon();
         p.addPoint(w, this.y - h / 2);
         p.addPoint(w, this.y + h / 2);
         p.addPoint(0, this.y);
         shape = p;         
      }

      @Override
      void addConnection(Connection con) {
		// Input connector add connection:
		// 1. Check the availability
		// 2. Set the connector boolean value to the output connector of the connection
		// 3. Update the component output value, that will ripple further to the next component till the end.
		if (isAvailable())
		{
			connection = con;
			this.connector_value = con.output.connector_value;
			component.updateInput();
		}
		
	}
     

   }

   class Connection {

      // a Connection connects an Output component to an Input component.
	  // This is the connector of the output component  
      Output output;
      //This is the connector of the input component
      Input input;
      

      public Connection(Output output, Input input) {
         this.output = output;
         this.input = input;
         output.addConnection(this);
         input.addConnection(this);
         input.connector_value = output.connector_value;
      }

      public void paintConnection(Graphics2D g2d) {
         g2d.drawLine(output.getX(), output.getY(), input.getX(), input.getY());
      }

   }

   class DrawPanel extends JPanel {

      // contains Components, draws lines (connecting pairs of components)

      DrawPanel() {
         addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
               if (addingComponent && gateChoice == 1) {
                  Component c = new ANDgate(e.getX(), e.getY());
                  components.add(c);
                  drawPanel.add(c);
                  addingComponent = false;
                  prompt.setText("Use mouse to drag components");
                  drawPanel.repaint();
               }
               if (addingComponent && gateChoice == 2) {
                  Component c = new NANDgate(e.getX(), e.getY());
                  components.add(c);
                  drawPanel.add(c);
                  addingComponent = false;
                  prompt.setText("Use mouse to drag components");
                  drawPanel.repaint();
               }
               if (addingComponent && gateChoice == 3) {
                  Component c = new ORgate(e.getX(), e.getY());
                  components.add(c);
                  drawPanel.add(c);
                  addingComponent = false;
                  prompt.setText("Use mouse to drag components");
                  drawPanel.repaint();
               }
               if (addingComponent && gateChoice == 4) {
                  Component c = new NORgate(e.getX(), e.getY());
                  components.add(c);
                  drawPanel.add(c);
                  addingComponent = false;
                  prompt.setText("Use mouse to drag components");
                  drawPanel.repaint();
               }
               if (addingComponent && gateChoice == 5) {
                  Component c = new XORgate(e.getX(), e.getY());
                  components.add(c);
                  drawPanel.add(c);
                  addingComponent = false;
                  prompt.setText("Use mouse to drag components");
                  drawPanel.repaint();
               }
               if (addingComponent && gateChoice == 6) {
                  Component c = new XNORgate(e.getX(), e.getY());
                  components.add(c);
                  drawPanel.add(c);
                  addingComponent = false;
                  prompt.setText("Use mouse to drag components");
                  drawPanel.repaint();
               }
               if (addingComponent && gateChoice == 7) {
                  Component c = new NOTgate(e.getX(), e.getY());
                  components.add(c);
                  drawPanel.add(c);
                  addingComponent = false;
                  prompt.setText("Use mouse to drag components");
                  drawPanel.repaint();
               }
               if (addingComponent && gateChoice == 8) {
                  Component c = new OUT(e.getX(), e.getY(), false);
                  components.add(c);
                  drawPanel.add(c);
                  addingComponent = false;
                  prompt.setText("Use mouse to drag components");
                  drawPanel.repaint();
               }
               else {
                  prompt.setText("Please select a gate first");
               }
            }
         });         
      }

      @Override
      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         Graphics2D g2d = (Graphics2D) g;
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         for (Connection line : lines) {
            line.paintConnection(g2d);
         }
      }

   }

   public static void main(String[] args) {
      new CircuitDesigner();
   }

}
