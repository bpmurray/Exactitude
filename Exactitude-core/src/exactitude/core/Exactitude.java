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
package exactitude.core;

import java.io.IOException;
import java.util.logging.Level;

import exactitude.core.Descriptors.DescriptorParser;
import exactitude.core.application.ProcessManager;
import exactitude.core.application.ProcessManagerFactory;
import exactitude.core.util.LogUtil;

/**
 * This is the class that drives the conversion and comparison. While it's in the core,
 * it's still the driver of the whole thing.
 * 
 * @author Brendan Murray
 */

public class Exactitude {
   private static DescriptorParser config = null;

   /*
    * Initialize logging and the XML Document.
    * Note that this is synchronised so that it can be called from multiple places
    */
   private static synchronized void loadConfig() {
      // If it's not initialised
      if (null == config) {
         config = new DescriptorParser();
      }
   }

   /**
    * Constructor
    */
   public Exactitude() {
      // Load the configuration
      loadConfig();
   }


   /**
    * Process the API call - instantiate and execute the converters and comparators
    *
    * @param fromFile          The source filename
    * @param fromFormat      The source file format
    * @param toFile         The target filename
    * @param toFormat          The target file format
    * @param compareOnly       If the write phase is to be omitted
    * @param smartCompare       If the comparison should be intelligent
    * 
    * @return   The array of the comparison results    
    */
   public double[] processRaw(String fromFile, String fromFormat, String toFile, String toFormat, boolean compareOnly, boolean smartCompare) {
      if (LogUtil.isLogging(Level.INFO)) {
         LogUtil.getLogger().entering("exactitude.core.Exactitude", "process(" + fromFile + ", " +
               fromFormat + ", " + toFile + ", "+ toFormat + ", " + compareOnly + ")");
      }

      ProcessManager cm = ProcessManagerFactory.getConverterManager(config, fromFormat, toFormat);

      double[] difference;

      if (null != cm) {

         try {
            if (!compareOnly) {
               cm.convert(fromFile, toFile);
            }
            difference = cm.compare(fromFile, toFile, smartCompare);
         } catch (IOException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            return null;
         }

      } else {
         return null;
      }
      if (LogUtil.isLogging(Level.INFO)) {
         LogUtil.getLogger().exiting("exactitude.core.Exactitude", "process");
      }
      return difference;
   }


   /**
    * Process the API call - instantiate and execute the converters and comparators 
    *
    * @param fromFile          The source filename
    * @param fromFormat      The source file format
    * @param toFile         The target filename
    * @param toFormat          The target file format
    * @param compareOnly       If the write phase is to be omit
    * @param smartCompare       If the comparison should be intelligent
    * 
    * @return   The average of the comparison results    
    */
   public double process(String fromFile, String fromFormat, String toFile, String toFormat, boolean compareOnly, boolean smartCompare) {
      if (LogUtil.isLogging(Level.INFO)) {
         LogUtil.getLogger().entering("exactitude.core.Exactitude", "process(" + fromFile + ", " +
               fromFormat + ", " + toFile + ", "+ toFormat + ", " + compareOnly + ")");
      }

      double returnVal = 0.0;

      double[] difference = processRaw(fromFile, fromFormat, toFile, toFormat, compareOnly, smartCompare);

      if (null != difference) {
         // Average out the results
         for (double result : difference) {
            if (result > 0.0)
               returnVal += result;
         }

         returnVal /= difference.length;
      } else {
         returnVal = 1.0;
      }
      
      if (LogUtil.isLogging(Level.INFO)) {
         LogUtil.getLogger().exiting("exactitude.core.Exactitude", "process");
      }
      return returnVal;
   }

}
