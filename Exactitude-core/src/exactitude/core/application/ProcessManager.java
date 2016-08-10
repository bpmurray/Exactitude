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
package exactitude.core.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import exactitude.core.Descriptors.ConfigItemDescriptor;
import exactitude.core.comparator.IComparator;
import exactitude.core.converter.IConverter;
import exactitude.core.converter.IMetadataConverter;
import exactitude.core.util.LogUtil;

/**
 * This class instantiates the concrete classes for conversion and comparison from the information
 * in the configuration. It also executes the first converter, and executes the comparators, returning
 * an array containing the results of the comparisons.
 * 
 * @author Brendan Murray
 */
public class ProcessManager {
   static private ArrayList<IConverter> converter = null;
   static private ArrayList<IComparator> comparator = null;

   /**
    * Constructor - this builds arrays of the concrete classes from the configuration information.
    * 
    * @param desc - the converter information
    * @param comp - the comparator information
    */
   @SuppressWarnings("unchecked")
   public ProcessManager(ArrayList<ConfigItemDescriptor> desc, ArrayList<ConfigItemDescriptor> comp) {
      if (LogUtil.isLogging(Level.INFO)) {
         LogUtil.getLogger().entering(this.getClass().getName(), "ProcessManager(["+desc.size()+"], ["+comp.size()+"]");
      }
      try {
         converter = new ArrayList<IConverter>();
         for (ConfigItemDescriptor convObj : desc) {
            Class<IConverter> iconv = (Class<IConverter>) Class.forName(convObj.getClassName());
            if (null != iconv) {
               IConverter item = (IConverter) iconv.newInstance();
               item.setFromType(convObj.getShortNameFrom());
               item.setToType(convObj.getShortNameTo());
               String mName = convObj.getMetaClassName();
               if (null != mName && !mName.isEmpty()) {
                  Class<IMetadataConverter>imeta = (Class<IMetadataConverter>) Class.forName(convObj.getMetaClassName());
                  item.setMetaConverter((IMetadataConverter) imeta.newInstance());
               }
               converter.add(item);
            }
         }
         comparator = new ArrayList<IComparator>();
         for (ConfigItemDescriptor compObj : comp) {
            Class<IComparator> icomp = (Class<IComparator>) Class.forName(compObj.getClassName());
            if (null != icomp) {
               IComparator item = (IComparator) icomp.newInstance();
               item.setFormat(compObj.getFormatType());
               comparator.add(item);
            }
         }
      } catch (Exception ex) {
         System.out.println("Error " + ex.getLocalizedMessage());
         ex.printStackTrace();
      }
      if (LogUtil.isLogging(Level.INFO)) {
         LogUtil.getLogger().exiting(this.getClass().getName(), "ProcessManager");
      }
   }

   /**
    * This executes the converter. It takes the converter first encountered in the
    * configuration, and uses it to convert the files.
    * 
    * @param fileName1 - The name of the source file
    * @param fileName2 - The name of the target output file
    */
   public void convert(String fileName1, String fileName2) throws IOException {
      if (LogUtil.isLogging(Level.INFO)) {
         LogUtil.getLogger().entering(this.getClass().getName(), "convert(" + fileName1 + ", "+fileName2 + ")");
      }
      IConverter conv = converter.get(0); // Just use the first for now
      conv.convert(fileName1, conv.getFromType(), fileName2, conv.getToType());
      if (LogUtil.isLogging(Level.INFO)) {
         LogUtil.getLogger().exiting(this.getClass().getName(), "convert");
      }
   }

   /**
    * This compares the resultant files by executing each of the comparison objects.
    * A smaller number in the result field indicates a closer match.
    * 
    * @param fileName1    - the name of the original file
    * @param fileName2    - the name of the converted output file
    * @param smartCompare - if the comparison is to terminate early for a definite response
    *
    * @return An array of values between zero and one, indicating the closeness of the files.
    */
   public double[] compare(String fileName1, String fileName2, boolean smartCompare) throws IOException {
      if (LogUtil.isLogging(Level.INFO)) {
         LogUtil.getLogger().entering(this.getClass().getName(), "compare(" + fileName1 + ", "+fileName2 + ")");
      }
      double[] results = new double[comparator.size()];

      int iX = 0;
      for (IComparator comp : comparator) {
         results[iX] = comp.compareFiles(fileName1, fileName2);
         if (smartCompare && results[iX] > comp.getThreshold())
            break;
         iX++;
      }
      // If we exited early, shorten the array
      if (iX++ < results.length) {
         double[] newResults = new double[iX];
         for (int iY=0; iY<iX; iY++) {
            newResults[iY] = results[iY];
         }
         return newResults;
      }
      if (LogUtil.isLogging(Level.INFO)) {
         LogUtil.getLogger().exiting(this.getClass().getName(), "compare");
      }
      return results;
   }

}
