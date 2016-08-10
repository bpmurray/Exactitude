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

import java.util.ArrayList;
import java.util.logging.Level;

import exactitude.core.Descriptors.ConfigItemDescriptor;
import exactitude.core.Descriptors.DescriptorParser;
import exactitude.core.util.LogUtil;

/**
 * This factory class instantiates and initialises a <code>ProcessManager</code>.
 * 
 * @author Brendan Murray
 */
public class ProcessManagerFactory {

   /**
    * getConverter - Instantiate the converter manager to handle conversions
    * 
    * @param dp       - a <code>DescriptorParser</code> object that is produced as a
    *                   result of parsing the configuration file.
    * @param fromType - the source file format
    * @param toType   - the output file type
    * @return ProcessManager to manage conversions from the <code>fromType</code> to the <code>toType</code>
    *          If the types are not compatible, returns null.
    */
   public static ProcessManager getConverterManager(DescriptorParser dp, String fromType, String toType) {
      if (LogUtil.isLogging(Level.INFO)) {
         LogUtil.getLogger().entering("exactitude.core.application.ProcessManagerFactory", "getConverterManager(dp, " + fromType + ", "+toType + ")");
      }
      ProcessManager cm = null;
      ArrayList<ConfigItemDescriptor> cd = dp.getConverterDescriptor(fromType, toType);

      if (null != cd && cd.size() > 0) {
         cm = new ProcessManager(cd, dp.getComparatorDescriptor(cd.get(0).getFormatType()));
      }
      if (LogUtil.isLogging(Level.INFO)) {
         LogUtil.getLogger().exiting("exactitude.core.application.ProcessManagerFactory", "getConverterManager");
      }
      return cm;
   }

}
