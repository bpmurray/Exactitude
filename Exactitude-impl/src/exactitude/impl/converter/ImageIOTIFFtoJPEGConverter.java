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

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.w3c.dom.NodeList;

import exactitude.core.converter.IConverter;
import exactitude.impl.utils.ExifUtil;

/**
 * @author Brendan Murray
 *
 * This class converts TIFF files to JPEG
 */
public class ImageIOTIFFtoJPEGConverter extends IConverter {

   /* (non-Javadoc)
    * @see exactitude.core.converter.IConverter#convert(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
    */
   @Override
   public void convert(String inName, String inType, String outName, String outType) throws IOException {
      //ImageIOMetadataConverter imeta = getMetaConverter(); // Should be null!

      // Set up the reader
      ImageReader iReader = ImageIOConverter.findReader(inType, "com.sun.media");
      if (null == iReader) {
         throw new IOException("Cannot find reader for " + inType);
      }
      ImageInputStream inStream = ImageIO.createImageInputStream(new File(inName));
      if (null == inStream) {
         throw new IOException("Cannot create input stream for " + inName);
      }
      iReader.setInput(inStream, false, false);

      // Set up the writer
      ImageWriter iWriter = ImageIOConverter.findWriter(outType, "com.sun.imageio");
      if (null == iWriter) {
         throw new IOException("Cannot find writer for " + outType);
      }
      File tmpFile = new File(outName + ".tmp");
      tmpFile.deleteOnExit(); // It's only a temporary file
      ImageOutputStream outStream = ImageIO.createImageOutputStream(tmpFile);
      if (null == outStream) {
         throw new IOException("Cannot create output stream for " + tmpFile.getName());
      }
      iWriter.setOutput(outStream);

      IIOImage    theImage     = iReader.readAll(0, null);
      IIOMetadata tiffMetadata = iReader.getImageMetadata(0);
      //ImageIO.write(theImage.getRenderedImage(), "jpg", outStream);
      iWriter.write(iReader.getStreamMetadata(), theImage, iWriter.getDefaultWriteParam());

      inStream.close();
      outStream.flush();
      outStream.close();

      // Now we have a partial JPEG - we have to use that as our starting point
      inStream = ImageIO.createImageInputStream(tmpFile);
      if (null == inStream) {
         throw new IOException("Cannot create input stream for " + tmpFile.getName());
      }
      iReader = ImageIOConverter.findReader(outType, "com.sun.media");
      if (null == iReader) {
         throw new IOException("Cannot find reader for " + outType);
      }
      iReader.setInput(inStream, false, false);
      outStream = ImageIO.createImageOutputStream(new File(outName));
      if (null == outStream) {
         throw new IOException("Cannot create output stream for " + outName);
      }
      iWriter.setOutput(outStream);
      theImage     = iReader.readAll(0, null);

      RenderedImage      rImage      = theImage.getRenderedImage();
      IIOMetadata        theMetadata = theImage.getMetadata();
      String []          formatNames = theMetadata.getMetadataFormatNames();
      IIOMetadataNode    rootNode    = (IIOMetadataNode) theMetadata.getAsTree(formatNames[0]);

      // Find the EXIF node
      NodeList        unknownList    = null;
      IIOMetadataNode exifNode       = null;
      IIOMetadataNode markerMetadata = (IIOMetadataNode) rootNode.getElementsByTagName("markerSequence").item(0);
      if (null == markerMetadata) {
         markerMetadata = rootNode;
      }
      unknownList = markerMetadata.getElementsByTagName("unknown");

      // Find the EXIF data node, if it's there
      if (unknownList.getLength() > 0) {
         for (int iX=0; iX<unknownList.getLength(); iX++) {
            IIOMetadataNode marker = (IIOMetadataNode) unknownList.item(iX);
            if ("225".equals(marker.getAttribute("MarkerTag"))) {
               exifNode = marker;
               break;
            }
         }
      }

      // Create a new correctly-formatted node
      IIOMetadataNode newExifNode = ExifUtil.createExifMetadataNode(iReader, tiffMetadata, (BufferedImage) rImage);

      // Include it in the tree
      if (null == exifNode) {
         markerMetadata.appendChild(newExifNode);   
      } else {
         exifNode.setUserObject(newExifNode.getUserObject());
      }

      // Update the metadata
      theMetadata = iWriter.getDefaultImageMetadata(new ImageTypeSpecifier(theImage.getRenderedImage()), iWriter.getDefaultWriteParam());
      theMetadata.setFromTree(formatNames[0], rootNode);

      iWriter.write(iReader.getStreamMetadata(), 
            new IIOImage(theImage.getRenderedImage(), theImage.getThumbnails(), theMetadata),
            iWriter.getDefaultWriteParam());


      iWriter.dispose();
      outStream.close();

      iReader.dispose();
      inStream.close();         

   }

}
