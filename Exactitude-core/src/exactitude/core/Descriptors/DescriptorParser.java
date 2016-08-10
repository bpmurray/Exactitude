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
 * or implied, of Brendan Murray.
 *
 */
package exactitude.core.Descriptors;


import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exactitude.core.util.LogUtil;


/**
 * @author Brendan Murray
 *
 * This loads the data from the XML configuration file
 */
public class DescriptorParser {

   private Document dom;
   private XPath    xpath;

   /**
    * @return the dom
    */
   public Document getDom() {
      return dom;
   }

   /**
    * Constructor - initialise the DOM
    * 
    * @param fileName - the XML file on which to base this file type
    */
   public DescriptorParser(String fileName) {
      try {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setNamespaceAware(true);
         DocumentBuilder builder = factory.newDocumentBuilder();
         dom = builder.parse(new File(fileName));
      } catch (Exception ex) {
         System.out.println("Error initialising the DOM for " + fileName);
         ex.printStackTrace();
      }
      xpath = XPathFactory.newInstance().newXPath();

      // Find the logging setting
      Node logNode = dom.getElementsByTagName("logging").item(0);
      if (null != logNode) {
         logNode = logNode.getAttributes().getNamedItem("Level");
         if (null != logNode) {
            LogUtil.setLogging(Level.parse(logNode.getNodeValue()));
         }
      }

   }

   /**
    * Constructor - initialise the DOM
    * 
    * No params - use the default
    */
   public DescriptorParser() {
      this(exactitude.core.Constants.DEFAULT_DESCRIPTOR);
   }

   /**
    * Creates an array of objects that encapsulate the information from the configuration file. The
    * actual parts of the configuration is determined by the XPath expression, and the file types.
    * 
    * @param exprConfig - An XPath expression to filter the XML content
    * @param fromType   - The source file type
    * @param toType     - The target file type
    * @return - An array of descriptors containing the information from the XML data
    */
   public ArrayList<ConfigItemDescriptor> getConfigItemDescriptor(XPathExpression exprConfig, String fromType, String toType) {
      ArrayList<ConfigItemDescriptor> ret = new ArrayList<ConfigItemDescriptor>();

      try{
         NodeList convList = (NodeList) exprConfig.evaluate(dom, XPathConstants.NODESET);

         for(int iX=0; iX<convList.getLength(); iX++) {
            Node convNode = convList.item(iX);

            // Find the name
            XPathExpression exprName = xpath.compile("name/child::text()");
            Node nameNode = (Node) exprName.evaluate(convNode,XPathConstants.NODE);
            String nameValue = (nameNode == null) ? "" : nameNode.getNodeValue().trim();

            // Find the description
            XPathExpression exprDesc = xpath.compile("description/child::text()");
            Node descNode = (Node) exprDesc.evaluate(convNode,XPathConstants.NODE);
            String descValue = (descNode == null) ? "" : descNode.getNodeValue().trim();

            // Find the type
            XPathExpression exprType = xpath.compile("format/child::text()");
            Node typeNode = (Node) exprType.evaluate(convNode,XPathConstants.NODE);
            String typeValue = (typeNode == null) ? "" : typeNode.getNodeValue().trim();

            // Find the class
            XPathExpression exprClass = xpath.compile("class/child::text()");
            Node classNode = (Node) exprClass.evaluate(convNode,XPathConstants.NODE);
            String classValue = (classNode == null) ? "" : classNode.getNodeValue().trim();

            // Find the metadata converter class
            XPathExpression exprMetaClass = xpath.compile("metadataconverter/child::text()");
            Node metaClassNode = (Node) exprMetaClass.evaluate(convNode,XPathConstants.NODE);
            String metaClassValue = (metaClassNode == null) ? "" : metaClassNode.getNodeValue().trim();

            // Add to the return
            ret.add(new ConfigItemDescriptor(nameValue, fromType, toType, descValue, typeValue, classValue, metaClassValue));
         }
      } catch (Exception ex) {
         System.out.println("Error " + ex.getLocalizedMessage());
         ex.printStackTrace();
      }
      return ret;
   }      

   /**
    * Creates an array of objects that encapsulate the information from the configuration file. The
    * parts of the configuration is restricted to converters that support the specified file types.
    * 
    * @param fromType - The source file type
    * @param toType   - The target file type
    * @return - An array of descriptors of converters
    */
   public ArrayList<ConfigItemDescriptor> getConverterDescriptor(String fromType, String toType) {
      try{
         //The expression extracts all subnodes of the item tagged with the provided ftype
         XPathExpression exprConfig = xpath.compile("/config/converterlist//converter" +
               "[fromlist/from='" + fromType +"' and tolist/to='" + toType + "']");
         return getConfigItemDescriptor(exprConfig, fromType, toType);
      } catch (Exception ex) {
         System.out.println("Error " + ex.getLocalizedMessage() +
               " when searching the configuration file for converter for " + fromType + " to " + toType);
         ex.printStackTrace();
      }
      return null;
   }

   /**
    * Creates an array of objects that encapsulate the information from the configuration file. The
    * parts of the configuration is restricted to comparators that support the specified file types.
    * 
    * @param format - The file type
    * @return - An array of descriptors of comparators
    */
   public ArrayList<ConfigItemDescriptor> getComparatorDescriptor(String format) {
      try{
         //The expression extracts all subnodes of the item tagged with the provided ftype
         XPathExpression exprConfig = xpath.compile("/config/comparatorlist//comparator[format='" + format +"']");
         return getConfigItemDescriptor(exprConfig, format, null);
      } catch (Exception ex) {
         System.out.println("Error " + ex.getLocalizedMessage() +
               " when searching the configuration file for comparator for " + format);
         ex.printStackTrace();
      }
      return null;
   }

}
