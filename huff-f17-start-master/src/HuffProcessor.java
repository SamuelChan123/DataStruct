import java.util.*;

import javax.swing.tree.TreeNode;

/**
 *	Interface that all compression suites must implement. That is they must be
 *	able to compress a file and also reverse/decompress that process.
 * 
 *	@author Brian Lavallee
 *	@since 5 November 2015
 *  @author Owen Atrachan
 *  @since December 1, 2016
 */
public class HuffProcessor {

	public static final int BITS_PER_WORD = 8;
	public static final int BITS_PER_INT = 32;
	public static final int ALPH_SIZE = (1 << BITS_PER_WORD); // or 256
	public static final int PSEUDO_EOF = ALPH_SIZE;
	public static final int HUFF_NUMBER = 0xface8200;
	public static final int HUFF_TREE  = HUFF_NUMBER | 1;
	public static final int HUFF_COUNTS = HUFF_NUMBER | 2;
	private String[] theString = new String[257] ;


	
	
	public enum Header{TREE_HEADER, COUNT_HEADER};
	public Header myHeader = Header.TREE_HEADER;
	
	/**
	 * Compresses a file. Process must be reversible and loss-less.
	 *
	 * @param in
	 *            Buffered bit stream of the file to be compressed.
	 * @param out
	 *            Buffered bit stream writing to the output file.
	 */

	public void compress(BitInputStream in, BitOutputStream out){
	    int[] counts = readForCounts(in);
	    HuffNode root = makeTreeFromCounts(counts);
	    makeCodingsFromTree(root, "");
	    writeHeader(root, out);
	 
	    in.reset();
	    writeCompressedBits(in, theString, out);
	}

	
	public int[] readForCounts(BitInputStream in) {
		int[] ret = new int[256];
		while (true) {
			int val = in.readBits(BITS_PER_WORD);
			if (val == -1) break;
			ret[val]++;
		}
		return ret;
	}
	
	public HuffNode makeTreeFromCounts(int[] ret) {
		PriorityQueue<HuffNode> pq = new PriorityQueue<>();

		for (int i = 0; i < ret.length; i++) {
			if(ret[i] >= 1) {
				pq.add(new HuffNode(i, ret[i]));
			}
		}
		pq.add(new HuffNode(PSEUDO_EOF, 1));

		int counter = 0;
		while (pq.size() > 1) {
		    HuffNode left = pq.remove();
		    HuffNode right = pq.remove();
		    HuffNode t = new HuffNode(-1, left.weight() + right.weight(), left,right);
		    pq.add(t);
		    counter ++;
		}
		System.out.println("Alphabet size: " + counter);
		HuffNode root = pq.remove();	
		return root;
	}
	
	private void makeCodingsFromTree(HuffNode node, String s){
		HuffNode current = node ;
		
		if(current.right() == null && current.left() == null){
			theString[current.value()] = s ;
			return;
		}
		
		makeCodingsFromTree(current.left(), s + 0) ;
		makeCodingsFromTree(current.right(), s + 1) ;
	}
	
	public void writeHeader(HuffNode root, BitOutputStream out) {
		out.writeBits(32, HUFF_TREE);
		writeHeaderHelper(root, out);
		
	}

	
	public void writeHeaderHelper(HuffNode root, BitOutputStream out) {
		HuffNode current = root ;
		if( current.left() == null && current.right() == null){
			out.writeBits(1, 1);
			out.writeBits(9, current.value());
			return ;
		}
		out.writeBits(1, 0);
		writeHeaderHelper(current.left(), out);
		writeHeaderHelper(current.right(), out);

	}
	
	public void writeCompressedBits(BitInputStream in, String[] encodings, BitOutputStream out) {
		while(true) {
			int val = in.readBits(BITS_PER_WORD);
			if (val == -1) {
				String encode = encodings[PSEUDO_EOF] ;
				out.writeBits(encode.length(), Integer.parseInt(encode,2));	
				break;
			}
			String encode = encodings[val];
			out.writeBits(encode.length(), Integer.parseInt(encode,2));
		}

	}


	

	/**
	 * Decompresses a file. Output file must be identical bit-by-bit to the
	 * original.
	 *
	 * @param in
	 *            Buffered bit stream of the file to be decompressed.
	 * @param out
	 *            Buffered bit stream writing to the output file.
	 */
	public void decompress(BitInputStream in, BitOutputStream out){

		int id = in.readBits(BITS_PER_INT);
		
		if (id != HUFF_TREE) throw new HuffException("Error!");

		HuffNode root = readTreeHeader(in);
		
		readCompressedBits(root,in,out);

	}
	
	public HuffNode readTreeHeader(BitInputStream in) {
		
		int bit = in.readBits(1);
				
		if (bit == 0){
		    HuffNode left = readTreeHeader(in);
		    HuffNode right = readTreeHeader(in);
		    return new HuffNode(0, 0, left, right);
		}
		if (bit == 1) {
			return new HuffNode(in.readBits(BITS_PER_WORD+1), 0);
		}
		
		return null;
		
	}
	
	public void readCompressedBits(HuffNode h, BitInputStream in, BitOutputStream out) {

		int bits;
		HuffNode current = h;   // root of tree, constructed from header data

		while (true) {

			bits = in.readBits(1);
			if (bits == -1) {
				throw new HuffException("Bad input, no PSEUDO_EOF");
			}
			else { 

				if (bits == 0) current = current.left(); // read a 0, go left
				if(bits == 1) current = current.right(); // read a 1, go right

				if (current.left() == null && current.right() == null) { // at leaf!
					if (current.value() == PSEUDO_EOF)
						break;   // out of loop
					else {
						out.writeBits(BITS_PER_WORD, current.value());
						current = h; // start back after leaf
					}
				}
			}
			
		}


	}

	public void setHeader(Header header) {
        myHeader = header;
        System.out.println("header set to "+myHeader);
    }
}