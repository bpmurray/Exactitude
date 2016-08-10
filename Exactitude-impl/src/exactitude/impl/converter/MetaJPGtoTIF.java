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

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import org.w3c.dom.NodeList;

import exactitude.impl.utils.ExifUtil;

/**
 * @author Brendan Murray
 *
 * This class handles the converstion of JPEG metadata to TIFF format
 */
public class MetaJPGtoTIF extends ImageIOMetadataConverter {

   /**
    * Uses a TIFFImageReader plugin to parse the given exif data into tiff
    * tags. The returned IIOMetadata is in whatever format the tiff ImageIO
    * plugin uses. If there is no tiff plugin, then this method returns null.
    *
    * @param The raw EXIF array
    * @return The TIFF metadata
    */
   @SuppressWarnings("unused")
   private static IIOMetadata getTiffMetaFromEXIF(byte[] exif) {
      java.util.Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(exactitude.core.Constants.TIFF_TYPE);

      ImageReader iReader;
      if (!readers.hasNext()) {
         return null;
      }

      iReader = readers.next();

      // skip the 6 byte exif header
      ImageInputStream wrapper = new MemoryCacheImageInputStream(
            new java.io.ByteArrayInputStream(exif, exactitude.core.Constants.METAHeaderLenEXIF, exif.length - exactitude.core.Constants.METAHeaderLenEXIF));
      iReader.setInput(wrapper, true, false);

      IIOMetadata exifMeta;
      try {
         exifMeta = iReader.getImageMetadata(0);
      } catch (Exception e) {
         // shouldn't happen
         throw new Error(e);
      }

      iReader.dispose();
      return exifMeta;
   }

   /**
    * Uses a TIFFImageReader plugin to parse the given iptc data into tiff
    * tags. The returned IIOMetadata is in whatever format the tiff ImageIO
    * plugin uses. If there is no tiff plugin, then this method returns null.
    *
    * @param The raw iptc data
    * @return The TIFF metadata
    */
   private static IIOMetadata getTiffMetaFromIPTC(byte[] iptc) {
      java.util.Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(exactitude.core.Constants.TIFF_TYPE);

      ImageReader iReader;
      if (!readers.hasNext()) {
         return null;
      }

      iReader = readers.next();

      // skip the 14 byte iptc header
      ImageInputStream wrapper = new MemoryCacheImageInputStream(
            new java.io.ByteArrayInputStream(iptc, exactitude.core.Constants.METAHeaderLenIPTC, iptc.length - exactitude.core.Constants.METAHeaderLenIPTC));
      iReader.setInput(wrapper, true, false);

      IIOMetadata iptcMeta;
      try {
         iptcMeta = iReader.getImageMetadata(0);
      } catch (Exception e) {
         // shouldn't happen
         throw new Error(e);
      }

      iReader.dispose();
      return iptcMeta;
   }

   /* (non-Javadoc)
    * @see exactitude.core.converter.IMetadataConverter#convertMetadata(javax.imageio.ImageReader)
    */
   @Override
   public IIOMetadata convertMetadata(ImageReader iReader, ImageWriter iWriter, ImageTypeSpecifier type, IIOMetadata metadata) throws IOException {
      if (null == metadata)
         return null;

      String inputFormat = iReader.getOriginatingProvider().getNativeImageMetadataFormatName();
      if (null == inputFormat) {
         inputFormat = exactitude.core.Constants.JPEG_FORMAT;
      }
      String outputFormat = iWriter.getOriginatingProvider().getNativeImageMetadataFormatName();
      if (null == outputFormat) {
         outputFormat = exactitude.core.Constants.TIFF_FORMAT;
      }

      IIOMetadataNode jpgMetadata = null;
      IIOMetadataNode tiffMetadataNode = null;
      IIOMetadataNode iptcMetadataNode = null;
      IIOMetadata tiffMetadata = iWriter.getDefaultImageMetadata(type, null);

      // We now have the metadata for this image in this reader
      if (null != metadata) {
         jpgMetadata = (IIOMetadataNode) metadata.getAsTree(inputFormat);
         boolean gotEXIF = false;
         boolean gotIPTC = false;

         IIOMetadataNode markerMetadata = (IIOMetadataNode) jpgMetadata.getElementsByTagName("markerSequence").item(0);
         NodeList extraData = markerMetadata.getElementsByTagName("unknown");
         for (int iY=0; iY<extraData.getLength(); iY++) {
            IIOMetadataNode marker = (IIOMetadataNode) extraData.item(iY);
            String markerTag = marker.getAttribute("MarkerTag");
            if ("225".equals(markerTag)) {
               if (!gotEXIF) {
                  IIOMetadata exifMeta = ExifUtil.getEXIFMetadata(marker);
                  if (null != exifMeta) {
                     tiffMetadataNode = (IIOMetadataNode) exifMeta.getAsTree(outputFormat);
                  }
               }   
               gotEXIF = true;
            } else if ("237".equals(markerTag)) {
               if (!gotIPTC) {
                  byte[] iptcBytes = (byte[]) marker.getUserObject();
                  IIOMetadata iptcMeta = getTiffMetaFromIPTC(iptcBytes); // is this correct?
                  if (null != iptcMeta) {
                     iptcMetadataNode = (IIOMetadataNode) iptcMeta.getAsTree(outputFormat);
                  }
               }
               gotIPTC = true;
            }
         }
      }

      if (null != tiffMetadataNode) {
         tiffMetadata.setFromTree(tiffMetadataNode.getNodeName(), tiffMetadataNode);
      }
      if (null != iptcMetadataNode) {
         tiffMetadata.mergeTree(tiffMetadata.getNativeMetadataFormatName(), iptcMetadataNode);
      }
      return tiffMetadata;      
   }

   /* (non-Javadoc)
    * @see exactitude.core.converter.IMetadataConverter#convertStreamMetadata(javax.imageio.ImageReader, javax.imageio.ImageWriter, javax.imageio.metadata.IIOMetadata)
    */
   public IIOMetadata convertStreamMetadata(ImageReader iReader, ImageWriter iWriter, IIOMetadata metadata) throws IOException {
      if (null == metadata) {
         return null;
      }

      IIOMetadata inStreamMetadata  = iReader.getStreamMetadata();
      IIOMetadataNode OutStreamMetadataNode = null;         
      if (null != inStreamMetadata) {
         OutStreamMetadataNode = (IIOMetadataNode)  inStreamMetadata.getAsTree(exactitude.core.Constants.JPEG_SFORMAT);
      }

      IIOMetadata outStreamMetadata = iWriter.getDefaultStreamMetadata(iWriter.getDefaultWriteParam());
      if (null != outStreamMetadata) {
         outStreamMetadata.setFromTree(OutStreamMetadataNode.getNodeName(), OutStreamMetadataNode);      
      }

      return outStreamMetadata;
   }

}
