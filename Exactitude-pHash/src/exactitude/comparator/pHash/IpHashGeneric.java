/**
 * Software License Agreement (BSD License)
 *
 * Copyright 2010 Brendan Murray. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY BRENDAN MURRAY ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BRENDAN MURRAY OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied.
 *
 */
package exactitude.comparator.pHash;

import java.io.FileNotFoundException;

import org.phash.Hash;
import org.phash.pHash;

import exactitude.core.comparator.IComparator;

/**
 * This is the abstract class from which all the hashing methods descend.
 * 
 * @author bpmurray
 */
abstract class IpHashGeneric extends IComparator {
	public static final pHash phash = new pHash();
	
	Hash  hash1;
	Hash  hash2;
	
	abstract public Hash getHash(String fileName) throws FileNotFoundException;
	abstract public double compareFiles(Hash hash1, Hash hash2);	
	
	/* (non-Javadoc)
	 * @see exactitude.core.comparator.IComparator#compareFiles(java.lang.String, java.lang.String)
	 */
	public double compareFiles(String file1, String file2) throws FileNotFoundException {
		hash1 = getHash(file1);
		if (null == hash1) {
			throw new FileNotFoundException("Can't generate hash for " + file1);
		}
		hash2 = getHash(file2);
		if (null == hash1) {
			throw new FileNotFoundException("Can't generate hash for " + file2);
		}
		return compareFiles(hash1, hash2);
	}
	
	/**
	 * This is used by the distance functions to weight a particular method, in
	 * order to ensure that all of the methods return similar values.
	 *  
	 * @return - The weighting value, defaults to 1.0 in the abstract class.
	 */
	public double getWeightedValue(double val) {
		return val;
	}
}
