//Date
public class notGates {
  private int length;
  private int height;
  private int output;
  private boolean isOppF;
  private static int numGates = 0;
  private final int GATE_ID;

  public notGates(boolean iof) {
    isOppF = iof;
    GATE_ID = numGates;
    numGates++;
  }

  public notGates() {
    isOppF = false;
    GATE_ID = numGates;
    numGates++;
  }

  public int setOutput(int i1) {
    if (isOppF == false) {
      if (i1 == 1) {
        output = 1;
      } else {
        output = 0;
      }

    } else {
      if (i1 == 0) {
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

  public static int getNumGates() {
    return numGates;
  }

}
