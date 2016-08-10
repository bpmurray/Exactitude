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

package exactitude.test;

import java.io.File;
import java.io.PrintStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.Node;

import exactitude.impl.converter.ImageIOConverter;
import exactitude.impl.utils.ExifUtil;

public class DumpBoth {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		(new DumpBoth()).doit();
	}

	private void dumpIt(String fName, String fType, boolean process)  {
		try {
			PrintStream dump = new PrintStream(new File(fName + ".xml"));

			ImageInputStream inStream = null;

			inStream = ImageIO.createImageInputStream(new File(fName));
			if (null == inStream) {
				System.out.println("Cannot create input stream for " + fName);
			}
			ImageReader iReader = ImageIOConverter.findReader(fType, "com.sun.media");
			iReader.setInput(inStream, false, false);

			IIOMetadata metadata = null; 
			try {
				metadata = iReader.getStreamMetadata();
			} catch (Exception ex) {
				System.out.println("Error " + ex.getLocalizedMessage() + " with " + fName);
				ex.printStackTrace();
			}
			//if (null != metadata) {
			//	Node node = metadata.getAsTree(metadata.getNativeMetadataFormatName());
			//	exactitude.core.util.XmlUtil.displayNodes("Stream metadata", node, dump);
			//}

			int nImages = iReader.getNumImages(true);

			for (int iX=0; iX<nImages; iX++) {
				try {
					metadata = iReader.getImageMetadata(iX);
				} catch (Exception ex) {
					System.out.println("Error " + ex.getLocalizedMessage() + " with " + fName);
					ex.printStackTrace();
				}
				if (null == metadata) {
					System.out.println("Cannot read image metadata for " + fName);
				} else {
					Node node = metadata.getAsTree(metadata.getNativeMetadataFormatName());
					ExifUtil.displayNodes("Image " + iX + " metadata", node, dump);
				}
			}
			dump.close();
		} catch (Exception ex) {
			System.out.println("Error " + ex.getLocalizedMessage() + " with " + fName);
			ex.printStackTrace();
		}

	}

	private void doit() {
		dumpIt("spider.jpg", "jpg", true);
		dumpIt("spider.tif", "tif", true);
		dumpIt("out.jpg", "jpg", true);
		dumpIt("out.tif", "tif", true);
	}
}
