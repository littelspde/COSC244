/**
 * This class performs LZW compression on data read from stdin.  It
 * sends the compressed output to stdout using a BitOutputStream which
 * allows a variable width bit field to be used.
 */
public class Compress {

   // 8 bits for character set + 1 bit for growcode
   private static int bitFieldSize = 9;
   private static BitOutputStream outstream =
         new BitOutputStream(System.out, bitFieldSize);
   private static LookUpTable table = new LookUpTable();
   private static boolean DEBUG = false;

   /**
    *  The entry point of the program.  Performs LZW compression on input
    *  read from stdin.
    *
    * @param  args if -d is passed as a command line argument then
    *             debugging output is sent to stderr.
    * @exception  Exception  Throws all exceptions (no error handling done)
    */
   public static void main(String[] args) throws Exception {
      if (args.length > 0 && args[0].equals("-d")) {
         DEBUG = true;
      }
      String w = "";
      int k;
      while ((k = System.in.read()) != -1) {
         String joined = w + (char) k;
         if (table.hasString(joined)) {
            w = joined;
         } else {
            table.add(joined);
            output(w);
            w = "" + (char) k;
         }
      }
      output(w);
      outstream.close();
   }

   /**
    * Output the code corresponding to the given string to stdout using
    * the variable width BitOutputStream.  If the code for the given
    * string is too large to fit in the current bit-field size then
    * the GROW_CODE is output, the bit-field size is increased and then
    * the code for the given string is output.
    *
    * @param w the string to output the code for.
    */
   private static void output(String w) throws Exception {
      if (!w.equals("")) {
         int code = table.getCode(w);
         if (DEBUG) {
            System.err.println("\n" + code + ":'" + w + "'");
         }
         try {
            outstream.write(code);
         } catch (IllegalArgumentException e) {
            // The code is too big to fit into bitFieldSize so we output the
            // growcode, increase the bitFieldSize, then write the code out.
            outstream.write(LookUpTable.GROW_CODE);
            outstream.setBitFieldSize(++bitFieldSize);
            outstream.write(code);
         }
      }
   }
}

