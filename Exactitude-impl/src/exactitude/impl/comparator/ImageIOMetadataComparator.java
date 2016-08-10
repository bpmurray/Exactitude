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
package exactitude.impl.comparator;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import exactitude.comparator.pHash.PHashText;
import exactitude.core.comparator.IComparator;
import exactitude.impl.utils.ExifUtil;
import exactitude.impl.utils.MetadataReaderUtil;

/**
 * @author Brendan Murray
 * 
 * This class compares <code>IIOMetadata</code> objects from the <code>ImageIO</code> library
 * NOTE: I'm not happy with this on second looks - it could be a lot better!
 */

public class ImageIOMetadataComparator extends IComparator {
   /**
    * Do a perceptual hash comparison of the nodes, returning a value indicating similarity
    * 
    * @param xml1 - the first metadata
    * @param xml2 - the second metadata
    * @return a comparison value 0.0-1.0 indicating similarity, a lower value being more similar
    */
   private double compareMetadataXML(IIOMetadataNode xml1, IIOMetadataNode xml2) {
      double returnVal = 1.0;
      try {
         File temp1 = File.createTempFile("compare1", ".txt");
         temp1.deleteOnExit();
         PrintStream pStrm = new PrintStream(temp1);
         ExifUtil.displayNodes(null, xml1, pStrm);
         pStrm.close();

         File temp2 = File.createTempFile("compare1", ".txt");
         temp2.deleteOnExit();
         pStrm = new PrintStream(temp2);
         ExifUtil.displayNodes(null, xml2, pStrm);
         pStrm.close();

         PHashText phash = new PHashText();
         returnVal = phash.compareFiles(temp1.getAbsolutePath(), temp2.getAbsolutePath());         
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getLocalizedMessage());
         if (xml1.isEqualNode(xml2)) {
            returnVal = 0.0;
         }
      }
      return returnVal;
   }


   /**
    * Compare arrays of metadata objects retrieved from the individual files, i.e. how well
    * metadata is converted and preserved in the conversion.
    * 
    * @param arr1 - the metadata from the original file
    * @param arr2 - The metadata from the output file
    * @return - A measure of the accuracy of the conversion, in the range zero to one.
    */
   private double compareMetadataArrays(ArrayList<IIOMetadataNode> arr1, ArrayList<IIOMetadataNode> arr2) {
      double result   = 1.0;

      if (arr1.size() > 0 && arr2.size() > 0) {

         double compare  = 0.0;
         int    foundCnt = 0;
         int    compareCnt = 0;

         // Check if any identical metadata blobs exist
         for (IIOMetadataNode node1 : arr1) {
            String name = node1.getNodeName();
            for (IIOMetadataNode node2 : arr2) {
               if (name.equals(node2.getNodeName())) {
                  result = compareMetadataXML(node1, node2);
                  if (Double.isNaN(result))
                     result = 1.0;
                  compare += result;
                  foundCnt++;
               }
            }
         }
         // None identical - just cross-compare the lot
         if (foundCnt == 0) {
            for (IIOMetadataNode node1 : arr1) {
               for (IIOMetadataNode node2 : arr2) {
                  result = compareMetadataXML(node1, node2);
                  if (!Double.isNaN(result)) {
                     compare += result;
                     compareCnt++;
                  }
               }
            }

            result = compare / compareCnt;
         } else {
            result = (compare + (arr1.size() - foundCnt)) / arr1.size();
         }
      }

      return result;
   }

   /*
    * (non-Javadoc)
    * 
    * @see exactitude.core.comparator.IComparator#compareFiles(java.lang.String,
    * java.lang.String)
    */
   public double compareFiles(String fileName1, String fileName2) {
      ArrayList<IIOMetadataNode> xmlArr1 = null;
      ArrayList<IIOMetadataNode> xmlArr2 = null;
      try {
         ImageInputStream inStream1 = ImageIO.createImageInputStream(new File(fileName1));
         xmlArr1 = MetadataReaderUtil.getAllMetadata(inStream1);
         inStream1.close();

         ImageInputStream inStream2 = ImageIO.createImageInputStream(new File(fileName2));
         xmlArr2 = MetadataReaderUtil.getAllMetadata(inStream2);
         inStream2.close();
      } catch (Exception ex) {
         System.out.println("Metadata comparison error: " + ex.getLocalizedMessage());
      }

      return getWeightedValue(compareMetadataArrays(xmlArr1, xmlArr2));
   }
   
   /* (non-Javadoc)
    * @see exactitude.comparator.pHash.IpHashGeneric#getWeightedValue(double)
    */
   public double getWeightedValue(double val) {
      return (val+1)/10;
   }
   
}
