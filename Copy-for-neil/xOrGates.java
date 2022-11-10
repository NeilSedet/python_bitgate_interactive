//Date
public class xOrGates {
  private int height;
  private int length;
  private int output;
  private boolean isOppF;
  private final int GATE_ID;
  private static int numGates = 0;

  public xOrGates(boolean iof) {
    isOppF = iof;
    GATE_ID = numGates;
    numGates++;
  }

  public xOrGates() {
    isOppF = false;
    GATE_ID = numGates;
    numGates++;
  }

  public int setOutput(int i1, int i2) {
    if (i1 == 1 && i2 == 1) {
      return 0;
    } else if (i1 == 0 && i2 == 0) {
      return 0;
    } else {
      return 1;
    }
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
