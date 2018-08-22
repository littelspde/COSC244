import java.io.*;

/**
 * Implements an enhanced OutputStream which allows you to write a
 * stream of bit fields ranging in size from 1 bit (a true bit
 * stream) to 32 bits (a stream of integers). The size of the current
 * bitfield can be changed at any point while writing the stream.
 */
public class BitOutputStream extends FilterOutputStream {

   /** 8 bits per byte */
   private final static int EIGHT = 8;

   /** our byte bitstream write buffer */
   protected int buffer;

   /** how many cached bits in our byte */
   protected int bitsInCache;

   /** current size of bitstream fields */
   protected int fieldSize;

   /** max value that fits bitfield */
   protected long maxFieldValue;

   /**
    * Creates a BitOutputStream with the given bitfield size which writes
    * to the given OutputStream.
    *
    *@param out the stream to write to.
    *@param bitFieldSize the initial size of the bitfield.
    */
   public BitOutputStream(OutputStream out, int bitFieldSize) {
      super(out);// call FilterOutputStream constr.
      setBitFieldSize(bitFieldSize);
      bitsInCache = 0;// we haven't got any cached bits
      buffer = 0;// start with clean buffer (for ORs !)
   }

   /**
    *  Sets the bitfield size to <code>bits</code>.
    *
    *@param bits   The new bitFieldSize value
    *@throws IllegalArgumentException if <code>bits</code> is less than 1
    * or greater than 32.
    */
   public void setBitFieldSize(int bits) throws IllegalArgumentException {
      if (bits > 32 || bits < 1) {
         throw new IllegalArgumentException
               ("BitField size (" + bits +
                ") no good. Has to be between 1 and 32.");
      }
      this.fieldSize = bits;
      this.maxFieldValue = (1L << bits) - 1;// precalc max bf value
   }

   /**
    *  Gets the current bitfield size.
    *
    *@return The bitfield size.
    */
   public int getBitFieldSize() {
      return this.fieldSize;
   }

   /**
    * Write a bitfield to the output stream. The number of bits written is
    * the current bitfield length. Bitfield can be on arbitrary bit boundaries.
    *
    *@param  bf the value to write.
    *@throws  IOException if an I/O exception occurs
    */
   public void write(int bf) throws IOException {
      int bitsToWrite;// how many bits left to write
      int capacity;// how many bits fit in write buffer
      int partial;// how many bits fit in write buffer
      int partialSize;// partial bitfield and its size in bits
      int bfExtractPos;// bitfield extract position (bit number)

      if (bf > maxFieldValue) {// check bf fits in current bitfield size
         throw new IllegalArgumentException
               ("Can not pack bitfield " + bf + " in " + fieldSize +
                " bits.");
      }
      bitsToWrite = fieldSize;
      bfExtractPos = fieldSize;
      // a single bitfield might have to be written out in several
      // passes since the lot has to pass through the single byte
      // write buffer. This inefficient situation is a result of
      // the complex aligning required to append any bitfield to
      // the currently written stream.
      while (bitsToWrite > 0) {
         if (bitsInCache != EIGHT) {// if capacity left
            capacity = EIGHT - bitsInCache;// in write buffer...

            partialSize = Math.min(bitsToWrite, capacity);
            bfExtractPos -= partialSize;

            partial = extract(bf, partialSize, bfExtractPos);
            buffer |= partial << (capacity - partialSize);
            bitsToWrite -= partialSize;
            bitsInCache += partialSize;
         }
         if (bitsInCache == EIGHT) {// if write buffer full,
            out.write(buffer);// send it on its way
            bitsInCache = 0;// and continue with
            buffer = 0;// clean buffer
         }
      }
   }

   /**
    * Extract a bitfield of length 'bits' from an integer source.
    * bitfield starts at bit 'pos' and is returned right-aligned to bitpos 0
    *
    *@param  source  the integer containing the bitfield to be extracted
    *@param  bits    number of bits to be extracted
    *@param  pos     postion of the bitfield in source (from right side)
    *@return         the extracted bitfield
    */
   private int extract(int source, int bits, int pos) {
      source = source >> pos;// align bitfield to bit 0
      int mask = ~((-1) << bits);// create a mask to get clean bitfld
      return source & mask;// return bitfield (0 bits padded)
   }

   /**
    * Override close() method to correctly flush any remaining bitfields
    * in write buffer before closing output chain.
    *
    *@throws  IOException  if an I/O exception occurs.
    */
   public void close() throws IOException {
      if (bitsInCache != 0) {
         out.write(buffer);
      }
      out.flush();
      out.close();
   }
}

