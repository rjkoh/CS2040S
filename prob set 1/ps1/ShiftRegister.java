///////////////////////////////////
// This is the main shift register class.
// Notice that it implements the ILFShiftRegister interface.
// You will need to fill in the functionality.
///////////////////////////////////

/**
 * class ShiftRegister
 * @author
 * Description: implements the ILFShiftRegister interface.
 */
public class ShiftRegister implements ILFShiftRegister {
    ///////////////////////////////////
    // Create your class variables here
    ///////////////////////////////////
    // TODO:
    private int srsize;
    private int srtap;
    private int[] srseed;

    ///////////////////////////////////
    // Create your constructor here:
    ///////////////////////////////////
    ShiftRegister(int size, int tap) {
        // TODO:
        srsize = size;
        srtap = tap;
    }

    ///////////////////////////////////
    // Create your class methods here:
    ///////////////////////////////////
    /**
     * setSeed
     * @param seed
     * Description:
     */
    @Override
    public void setSeed(int[] seed) {
        // TODO:
        for (int i = 0; i < seed.length; i++) {
            if (seed[i] != 1 && seed[i] != 0) {
                throw new Error("non-binary seed");
            }
        }
        srseed = seed;
    }

    /**
     * shift
     * @return
     * Description:
     */
    @Override
    public int shift() {
        // TODO:
        int least = srseed[srsize - 1];
        int tapno = srseed[srtap];
        for (int i = srsize - 1; i >= 1; i--) {
            srseed[i] = srseed[i - 1];
        }
        srseed[0] = (least ^ tapno);
        return srseed[0];
    }

    /**
     * generate
     * @param k
     * @return
     * Description:
     */
    @Override
    public int generate(int k) {
        // TODO:
        int[] tobin = new int[k];
        for (int i = 0; i < k; i++) {
            tobin[i] = shift();
        }
        return toBinary(tobin);
    }

    /**
     * Returns the integer representation for a binary int array.
     * @param array
     * @return
     */
    private int toBinary(int[] array) {
        // TODO:
        String binstr = "";
        for (int i = 0; i < array.length; i++) {
            binstr += array[i];
        }
        return Integer.parseInt(binstr, 2);
    }

    public static void main(String[] args) {
        int[] array = new int[] {1};
        ShiftRegister shifter = new ShiftRegister(1, 0);
        shifter.setSeed(array);
        for (int i = 0; i < 10; i++) {
            int j = shifter.generate(3);
            System.out.println(j);
        }

    }
}


