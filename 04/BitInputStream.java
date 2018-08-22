import java.io.*;

/**
 * Implements an enhanced InputStream which allows you to read a
 * stream of bit fields ranging in size from 1 bit (a true bit
 * stream) to 32 bits (a stream of integers). The size of the current
 * bitfield can be changed at any point while reading the stream.
 */
public class BitInputStream extends FilterInputStream {
   /** 8 bits per byte */
   final static int EIGHT = 8;
   
   /** our byte bitstream read-ahead */
   protected int buffer;

   /** how many unread bits left in our byte */
   protected int bitsInCache;
   
   /** current size of bitstream read fields */
   protected int fieldSize;

   /**
    * Creates a BitInputStream with the given bitfield size and InputStream.
    *
    * @param  in the stream to read from.
    * @param  bitFieldSize  the initial size of the bitfield.
    */
   public BitInputStream(InputStream in, int bitFieldSize) {
      super(in);
      setBitFieldSize(bitFieldSize);
      bitsInCache = 0;
   }

   /**
    *  Sets the size of the bitfield to equal <code>bits</code>.
    *
    * @param bits The new bitfield size
    * @throws IllegalArgumentException if <code>bits</code> is not in
    * the range 1-32 inclusive.
    */
   public void setBitFieldSize(int bits) throws IllegalArgumentException {
      if (bits > 32 || bits < 1) {
         throw new IllegalArgumentException
               ("BitField size (" + bits +
                  ") no good. Has to be between 1 and 32.");
      }
      this.fieldSize = bits;
   }

   /**
    *  Returns the current bitfield size.
    *
    * @return The bitfield size.
    */
   public int getBitFieldSize() {
      return this.fieldSize;
   }

   /**
    *  Read a bitfield from the input stream. The number of bits read is the
    *  current bitfield length. The bitfield can be on arbitrary bit
    *  boundaries.
    *
    * @return the bitfield read as an integer, or -1 if the end of the
    *         stream is reached.
    * @throws IOException if an I/O error occurs. 
    */
   public int read() throws IOException {

      int bitField = 0;    // what we're going to return to caller
      int bitsToRead;  // remaining bits to assemble into BF
      int availableNumberOfBits;
      int OR_position;
      int rightAlignedBFPartial;
      bitsToRead = fieldSize;
      OR_position = fieldSize;

      while (bitsToRead > 0) {
         if (bitsInCache == 0) {
            if ((buffer = in.read()) == -1) {
               return -1;
               // reached EOF
            }
            bitsInCache = EIGHT;
            // we've got a full byte again
         }
         availableNumberOfBits = Math.min(bitsToRead, bitsInCache);
         rightAlignedBFPartial = buffer >> (EIGHT - availableNumberOfBits);
         // always keep next partial left aligned and clean
         buffer <<= availableNumberOfBits;
         buffer &= 255;
         OR_position -= availableNumberOfBits;
         // add bitfield subfield
         bitField |= rightAlignedBFPartial << OR_position;
         // track # of cached bits
         // track how much left to do

         bitsInCache -= availableNumberOfBits;
         bitsToRead -= availableNumberOfBits;
      }
      return bitField;
   }

}
