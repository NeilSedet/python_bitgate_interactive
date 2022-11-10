//Date
public class orGates {
  private int length;
  private int height;
  private int output;
  private boolean isOppF;
  private final int GATE_ID;
  private static int numGates = 0;

  public orGates(boolean iof) {
    isOppF = iof;
    GATE_ID = numGates;
    numGates++;
  }

  public orGates() {
    isOppF = false;
    GATE_ID = numGates;
    numGates++;
  }

  public int setOutput(int i1, int i2) {
    if (isOppF == false) {
      if (i1 == 1 || i2 == 1) {
        output = 1;
      } else {
        output = 0;
      }

    } else {
      if (i1 == 0 || i2 == 0) {
        output = 1;
      } else {
        output = 0;
      }

    }
    return output;
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
