
//Date
public class andGates {
  private int length;
  private int height;
  private int output;
  private int input1;
  private int input2;
  private boolean isOppF;
  private final int GATE_ID;
  private static int numGates = 0;

  public andGates(boolean iof) {
    isOppF = iof;
    GATE_ID = numGates;
    numGates++;
  }

  public andGates() {
    isOppF = false;
    GATE_ID = numGates;
    numGates++;
  }

  public int setOutput(int i1, int i2) {
    if (isOppF == false) {
      if (i1 == i2 && i1 == 1) {
        output = 1;
      } else {
        output = 0;
      }
    } else {
      if (i1 == i2 && i1 == 1) {
        output = 0;
      } else {
        output = 1;
      }
    }
    return output;
  }

  public void setInput1(int input1) {
    this.input1 = input1;
  }

  public void setInput2(int input2) {
    this.input2 = input2;
  }

  public int getInput1() {
    return input1;
  }

  public int getInput2() {
    return input2;
  }

  public void setOppF() {
    if(isOppF=true){
      isOppF=false;
    }
    else{
      isOppF=true;
    };
  }

}
