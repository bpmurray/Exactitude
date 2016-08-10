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

package exactitude.impl.converter;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import exactitude.core.converter.IConverter;

/**
 * @author Brendan Murray
 *
 * This is the converter that uses the JDK's <code>ImageIO</code> library, along
 * with the <code>JAI</code> Java Advanced Imaging library, to convert images. * 
 */
public class ImageIOConverter extends IConverter {

   /**
    * Find an <code>ImageWriter</code> that supports the provided format,
    * and that has a class that starts with the provides basename.
    * 
    * @param format - the format for which we want a writer
    * @param baseName - the initial name of the class (null means the first available)
    * @return - the first <code>ImageWriter</code> that matches the criteria.
    */
   public static ImageWriter findWriter(String format, String baseName) {
      ImageWriter iWriter = null;
      Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(format);
      if (null == writers || !writers.hasNext()) {
         writers = ImageIO.getImageWritersBySuffix(format);
      }

      if (null != writers) {
         while (writers.hasNext()) {
            iWriter = (ImageWriter) writers.next();
            if (null == baseName || iWriter.getClass().getName().startsWith(baseName)) {
               break; // Found - we don't need to continue
            }
         }
      }
      if (null == iWriter) {
         System.out.println("Error: Cannot find writer for '" + format + "'");
      }
      return iWriter;
   }

   /**
    * Find an <code>ImageReader</code> that supports the provided format,
    * and that has a class that starts with the provides basename.
    * 
    * @param format - the format for which we want a reader
    * @param baseName - the initial name of the class (null means the first available)
    * @return - the first <code>ImageReader</code> that matches the criteria.
    */
   public static ImageReader findReader(String format, String baseName) {
      ImageReader iReader = null;
      Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(format);
      if (null == readers || !readers.hasNext()) {
         readers = ImageIO.getImageReadersBySuffix(format);
      }

      if (null != readers) {
         while (readers.hasNext()) {
            iReader = (ImageReader)readers.next();
            if (null == baseName || iReader.getClass().getName().startsWith(baseName)) {
               break; // Found - we don't need to continue
            }
         }
      }
      if (null == iReader) {
         System.out.println("Error: Cannot find reader for '" + format + "'");
      }
      return iReader;
   }

   /* (non-Javadoc)
    * @see exactitude.core.converter.IConverter#convert(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   public void convert(String inName, String inType, String outName, String outType) throws IOException {
      ImageInputStream inStream = ImageIO.createImageInputStream(new File(inName));
      if (null == inStream) {
         throw new IOException("Cannot create input stream for " + inName);
      }

      ImageReader iReader = findReader(inType, "com.sun.media");

      if (null != iReader) {
         iReader.setInput(inStream, false, false);
         ImageIOMetadataConverter imeta = (ImageIOMetadataConverter) getMetaConverter();


         int nImages = iReader.getNumImages(true);
         if (nImages > 0) {
            ImageOutputStream outStream = ImageIO.createImageOutputStream(new File(outName));
            if (null == outStream) {
               throw new IOException("Cannot create output stream for " + outName);
            }

            ImageWriter iWriter = ImageIOConverter.findWriter(outType, "com.sun.media");
            if (null != iWriter) {
               iWriter.setOutput(outStream);

               // Process the stream metadata
               IIOMetadata outStreamMetadata = imeta.convertStreamMetadata(iReader, iWriter, iReader.getStreamMetadata());

               // Process the images
               IIOMetadata outputMetadata = null;
               IIOImage theImage;

               if (iWriter.canWriteSequence()) {
                  iWriter.prepareWriteSequence(outStreamMetadata);
               }

               for (int iX = 0; iX<nImages; iX++) {
                  theImage = iReader.readAll(iX, null);
                  RenderedImage rImage = theImage.getRenderedImage();

                  outputMetadata = imeta.convertMetadata(iReader, iWriter, new ImageTypeSpecifier(rImage), theImage.getMetadata());
                  IIOImage outImage = new IIOImage(rImage, null, outputMetadata);

                  if (iWriter.canWriteSequence()) {
                     iWriter.writeToSequence(outImage, null);
                  } else {
                     iWriter.write(outImage);
                  }
               }

               if (iWriter.canWriteSequence()) {
                  iWriter.endWriteSequence();
               }

               iWriter.dispose();
               outStream.close();
            }
         }
         iReader.dispose();
         inStream.close();
      }
   }

}
