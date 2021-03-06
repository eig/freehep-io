// Copyright 2002-2009, FreeHEP.
package org.freehep.util.io.test;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;

import junit.framework.AssertionFailedError;

import org.freehep.util.io.BitInputStream;

/**
 * Test for Bit Input Stream
 * 
 * @author Mark Donszelmann
 */
public class BitInputStreamTest extends AbstractStreamTest {

	/**
	 * Test method for 'org.freehep.util.io.RunLengthInputStream.read()'
	 * 
	 * @throws Exception
	 *             if ref file cannot be found
	 */
	public void testRead() throws Exception {
		File testFile = new File(testDir, "Quote.bit");
		File refFile = new File(refDir, "Quote.txt");

		BitInputStream in = new BitInputStream(new FileInputStream(testFile));
		FileInputStream ref = new FileInputStream(refFile);

		int i = 0;
		try {
			while (true) {
				int n = (int) in.readUBits(3) + 1;
				long b = in.readUBits(n);
				int r = ref.read();
				i++;
				if (r != b) {
					throw new AssertionFailedError(refFile
							+ ": comparison failed at offset " + (i - 1));
				}
			}
		} catch (EOFException e) {
			// ignored
		}

		in.close();
		ref.close();
	}
}
