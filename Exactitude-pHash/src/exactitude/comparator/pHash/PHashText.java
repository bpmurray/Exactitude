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
import org.phash.TextHash;

/**
 * @author bpmurray
 *
 */
public class PHashText extends IpHashGeneric {

	/* (non-Javadoc)
	 * @see exactitude.core.comparator.IpHashGeneric#compareFiles(org.phash.Hash, org.phash.Hash)
	 */
	@Override
	public double compareFiles(Hash hash1, Hash hash2) {
		TextHash tHash1 = (TextHash) hash1;
		TextHash tHash2 = (TextHash) hash2;

		int limit1 = tHash1.hash.length;
		int limit2 = tHash2.hash.length;

		int nFound = 0;
		for (int iX=0; iX<limit1; iX++){
			for (int iY=0; iY<limit2 && iX<limit1; iY++){
				if (tHash1.hash[iX] == tHash2.hash[iY]) {
					nFound++;
					break;
				}
			}
		}

		return (double) (limit1 - nFound) / limit1;
		//return phash.getTextDistance(tHash1, tHash2);
	}

	/* (non-Javadoc)
	 * @see exactitude.core.comparator.IpHashGeneric#getHash(java.lang.String)
	 */
	@Override
	public Hash getHash(String fileName) throws FileNotFoundException {
		TextHash hash = phash.getTextHash(fileName);
		if (null == hash) {
			throw new FileNotFoundException("Can't generate text hash for " + fileName);
		}
		return hash;
	}

}
