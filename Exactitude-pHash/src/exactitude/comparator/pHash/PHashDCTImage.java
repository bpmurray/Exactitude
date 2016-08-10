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

import org.phash.DCTImageHash;
import org.phash.ImageHash;

/**
 * Handles DCT image comparisons.
 * 
 * @author bpmurray
 */
public class PHashDCTImage extends IpHashImage {
	
	/* (non-Javadoc)
	 * @see exactitude.core.comparator.IpHashImage#getHash(java.lang.String)
	 */
	@Override
	public ImageHash getHash(String fileName) throws FileNotFoundException {
		DCTImageHash hash = phash.getDctImageHash(fileName);
		if (null == hash) {
			throw new FileNotFoundException("Can't generate hash for " + fileName);
		}
		return hash;
	}
	
	/* (non-Javadoc)
	 * @see exactitude.comparator.pHash.IpHashGeneric#getWeightedValue(double)
	 */
	public double getWeightedValue(double val) {
		return val / 50.0;
	}
	
	/* (non-Javadoc)
	 * @see exactitude.core.comparator.IComparator#getThreshold()
	 */
	public double getThreshold() {
		return 0.5;
	}

}
