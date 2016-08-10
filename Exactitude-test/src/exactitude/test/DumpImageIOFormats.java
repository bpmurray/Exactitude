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

import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTranscoder;
import javax.imageio.ImageWriter;

/**
 * @author bpmurray
 *
 */
public class DumpImageIOFormats {

	public void doDumpAll() {
		listReaderFormats();
		listWriterFormats();
		listTranscoders();		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		(new DumpImageIOFormats()).doDumpAll();
	}

	public void listReaderFormats() {
		String[] readerFormats = ImageIO.getReaderFormatNames();
		System.out.println("\nImageIO Reader Formats: ");
		for (int ix = 0; ix < readerFormats.length; ix++) {
			System.out.println(readerFormats[ix]);
		}

	}

	public void listWriterFormats() {

		String[] writerFormats = ImageIO.getWriterFormatNames();
		System.out.println("\nImageIO Writer Formats: ");
		for (int ix = 0; ix < writerFormats.length; ix++) {
			System.out.println(writerFormats[ix]);
		}
	}

	public void listTranscoders() {

		/* Initialize the following two variables to the desired input and output formats. */
		String inputFormat = "tif";
		String outputFormat = "jpg";
		Iterator<ImageReader> inputReaders = ImageIO.getImageReadersByFormatName(inputFormat);
		while (inputReaders.hasNext()) {
			ImageReader reader = (ImageReader) inputReaders.next();

			Iterator<ImageWriter> outputWriters = ImageIO.getImageWritersByFormatName(outputFormat);
			while (outputWriters.hasNext()) {
				ImageWriter writer = (ImageWriter) outputWriters.next();

				Iterator<ImageTranscoder> transcoders = ImageIO.getImageTranscoders(reader, writer);
				System.out.println("\n" + inputFormat + "(" 
						                + reader.getOriginatingProvider().getNativeImageMetadataFormatName() + "\n                   /"
						                + reader.getOriginatingProvider().getNativeStreamMetadataFormatName() + "\n                   /"
						                + reader.getOriginatingProvider().getPluginClassName()
						                + ")\n  to " + outputFormat + "("
						                + writer.getOriginatingProvider().getNativeImageMetadataFormatName() + "\n                   /"
						                + writer.getOriginatingProvider().getNativeStreamMetadataFormatName() + "\n                   /"
						                + writer.getOriginatingProvider().getPluginClassName()
						                + ") transcoders:");
				while (transcoders.hasNext()) {
					ImageTranscoder transcoder = transcoders.next();
					System.out.println(" " + transcoder.toString());
				}
			}
		}

	}
}
