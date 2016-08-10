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

import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;


/**
 * @author Brendan Murray
 *
 * This class handles the conversion of TIFF metadata to JPEG format
 */
public class MetaTIFtoJPG extends ImageIOMetadataConverter {

   /**
    * Get a basic JPEG writer
    *
    * @return The Writer object
    */
   ImageWriter getBasicJPGWriter() {
      Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
      if (null == writers || !writers.hasNext()) {
         writers = ImageIO.getImageWritersBySuffix("jpg");
      }
      if (null == writers || !writers.hasNext()) {
         return null;
      }

      // Look for the basic plugin
      ImageWriter iWriter = writers.next();
      while (writers.hasNext()) {
         ImageWriter temp = writers.next();
         if (temp.getOriginatingProvider().getPluginClassName().startsWith("com.sun.imageio.")) {
            iWriter = temp;
            break;
         }
      }
      return iWriter;
   }


   /* (non-Javadoc)
    * @see exactitude.core.converter.IMetadataConverter#convertStreamMetadata(javax.imageio.ImageReader, javax.imageio.ImageWriter, javax.imageio.metadata.IIOMetadata)
    */
   public IIOMetadata convertStreamMetadata(ImageReader iReader, ImageWriter iWriter, IIOMetadata metadata) throws IOException {
      if (null == metadata) {
         return null;
      }

      IIOMetadataNode OutStreamMetadataNode = (IIOMetadataNode)  metadata.getAsTree(exactitude.core.Constants.JPEG_SFORMAT);;         

      IIOMetadata outStreamMetadata = iWriter.getDefaultStreamMetadata(iWriter.getDefaultWriteParam());
      if (null == outStreamMetadata) {
         ImageWriter tmpWriter = getBasicJPGWriter();
         if (null != tmpWriter) {
            IIOMetadata tmpStreamMetadata = tmpWriter.getDefaultStreamMetadata(tmpWriter.getDefaultWriteParam());
            outStreamMetadata = iWriter.convertStreamMetadata(tmpStreamMetadata, null);
            tmpWriter.dispose();
         }
      }


      if (null != outStreamMetadata && null != OutStreamMetadataNode) {
         outStreamMetadata.mergeTree(OutStreamMetadataNode.getNodeName(), OutStreamMetadataNode);      
      }

      return outStreamMetadata;
   }

   /* (non-Javadoc)
    * @see exactitude.core.converter.IMetadataConverter#convertMetadata(javax.imageio.ImageReader)
    */
   @Override
   public IIOMetadata convertMetadata(ImageReader iReader, ImageWriter iWriter, ImageTypeSpecifier type, IIOMetadata metadata) throws IOException {
      if (null == metadata) {
         return null;
      }

      String inputFormat = iReader.getOriginatingProvider().getNativeImageMetadataFormatName();

      if (null == inputFormat) {
         inputFormat = exactitude.core.Constants.TIFF_FORMAT;
      }
      String outputFormat = iWriter.getOriginatingProvider().getNativeImageMetadataFormatName();
      if (null == outputFormat) {
         outputFormat = exactitude.core.Constants.JPEG_FORMAT;
      }

      IIOMetadataNode jpgMetadataNode = (IIOMetadataNode) metadata.getAsTree(inputFormat);;
      IIOMetadata jpgMetadata = iWriter.convertImageMetadata(metadata, type, null);

      if (null == jpgMetadata) {
         jpgMetadata = iWriter.getDefaultImageMetadata(type, iWriter.getDefaultWriteParam());
         if (null == jpgMetadata) {
            ImageWriter tmpWriter = getBasicJPGWriter();
            if (null != tmpWriter) {
               IIOMetadata tmpMetadata = tmpWriter.getDefaultImageMetadata(type, tmpWriter.getDefaultWriteParam());
               jpgMetadata = iWriter.convertImageMetadata(tmpMetadata, type, null);
            }
            tmpWriter.dispose();
         }
      }
      if (null != jpgMetadataNode && null !=jpgMetadata) {
         jpgMetadata.mergeTree(jpgMetadataNode.getNodeName(), jpgMetadataNode);
      }
      return jpgMetadata;   
   }





}
