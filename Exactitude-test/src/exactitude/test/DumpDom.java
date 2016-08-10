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

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * @author Brendan Murray
 * @date 4 May 2010
 * 
 */
public class DumpDom {
	private static final String DOM_ATTRIBUTE = "ATTRIBUTE";
	private static final String DOM_CDATA_SECTION = "CDATA SECTION";
	private static final String DOM_COMMENT = "COMMENT";
	private static final String DOM_DOCUMENT = "DOCUMENT";
	private static final String DOM_DOCUMENT_FRAGMENT = "DOCUMENT FRAGMENT";
	private static final String DOM_DOCUMENT_TYPE = "DOCUMENT TYPE";
	private static final String DOM_ELEMENT = "ELEMENT";
	private static final String DOM_ENTITY = "ENTITY";
	private static final String DOM_ENTITY_REFERENCE = "ENTITY REFERENCE";
	private static final String DOM_PROCESSING_INSTRUCTION = "PROCESSING_INSTRUCTION";
	private static final String DOM_TEXT = "TEXT";
	private static final String DOM_NOTATION = "NOTATION";
	
	public void dump(Document doc) {
		dumpLoop((Node) doc, "");
	}

	private void dumpLoop(Node node, String indent) {
		switch (node.getNodeType()) {
		case Node.ATTRIBUTE_NODE:
			dumpAttributeNode((Attr) node, indent);
			break;
		case Node.CDATA_SECTION_NODE:
			dumpCDATASectionNode((CDATASection) node, indent);
			break;
		case Node.COMMENT_NODE:
			dumpCommentNode((Comment) node, indent);
			break;
		case Node.DOCUMENT_NODE:
			dumpDocumentNode((Document) node, indent);
			break;
		case Node.DOCUMENT_FRAGMENT_NODE:
			dumpDocumentFragmentNode((DocumentFragment) node, indent);
			break;
		case Node.DOCUMENT_TYPE_NODE:
			dumpDocumentTypeNode((DocumentType) node, indent);
			break;
		case Node.ELEMENT_NODE:
			dumpElementNode((Element) node, indent);
			break;
		case Node.ENTITY_NODE:
			dumpEntityNode((Entity) node, indent);
			break;
		case Node.ENTITY_REFERENCE_NODE:
			dumpEntityReferenceNode((EntityReference) node, indent);
			break;
		case Node.NOTATION_NODE:
			dumpNotationNode((Notation) node, indent);
			break;
		case Node.PROCESSING_INSTRUCTION_NODE:
			dumpProcessingInstructionNode((ProcessingInstruction) node, indent);
			break;
		case Node.TEXT_NODE:
			dumpTextNode((Text) node, indent);
			break;
		default:
			System.out.println(indent + "Unknown node");
			break;
		}

		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++)
			dumpLoop(list.item(i), indent + "   ");
	}

	/* Display the contents of a ATTRIBUTE_NODE */
	private void dumpAttributeNode(Attr node, String indent) {
		System.out.println(indent + DOM_ATTRIBUTE + " " +
				node.getName() + "=\"" + node.getValue() + "\"");
	}

	/* Display the contents of a CDATA_SECTION_NODE */
	private void dumpCDATASectionNode(CDATASection node, String indent) {
		System.out.println(indent + DOM_CDATA_SECTION +" length=" + node.getLength());
		System.out.println(indent + "\"" + node.getData() + "\"");
	}

	/* Display the contents of a COMMENT_NODE */
	private void dumpCommentNode(Comment node, String indent) {
		System.out.println(indent + DOM_COMMENT +" length=" + node.getLength());
		System.out.println(indent + "  " + node.getData());
	}

	/* Display the contents of a DOCUMENT_NODE */
	private void dumpDocumentNode(Document node, String indent) {
		System.out.println(indent + DOM_DOCUMENT);
	}

	/* Display the contents of a DOCUMENT_FRAGMENT_NODE */
	private void dumpDocumentFragmentNode(DocumentFragment node, String indent) {
		System.out.println(indent + DOM_DOCUMENT_FRAGMENT);
	}

	/* Display the contents of a DOCUMENT_TYPE_NODE */
	private void dumpDocumentTypeNode(DocumentType node, String indent) {
		System.out.println(indent + DOM_DOCUMENT_TYPE + ": " + node.getName());
		if (node.getPublicId() != null)
			System.out.println(indent + " Public ID: " + node.getPublicId());
		if (node.getSystemId() != null)
			System.out.println(indent + " System ID: " + node.getSystemId());
		NamedNodeMap entities = node.getEntities();
		if (entities.getLength() > 0) {
			for (int i = 0; i < entities.getLength(); i++) {
				dumpLoop(entities.item(i), indent + "  ");
			}
		}
		NamedNodeMap notations = node.getNotations();
		if (notations.getLength() > 0) {
			for (int i = 0; i < notations.getLength(); i++)
				dumpLoop(notations.item(i), indent + "  ");
		}
	}

	/* Display the contents of a ELEMENT_NODE */
	private void dumpElementNode(Element node, String indent) {
		System.out.println(indent + DOM_ELEMENT + ": <" + node.getTagName() + ">");
		NamedNodeMap nm = node.getAttributes();
		for (int i = 0; i < nm.getLength(); i++)
			dumpLoop(nm.item(i), indent + "  ");
		
	}

	/* Display the contents of a ENTITY_NODE */
	private void dumpEntityNode(Entity node, String indent) {
		System.out.println(indent + DOM_ENTITY + ": " + node.getNodeName());
	}

	/* Display the contents of a ENTITY_REFERENCE_NODE */
	private void dumpEntityReferenceNode(EntityReference node, String indent) {
		System.out.println(indent + DOM_ENTITY_REFERENCE + ": " + node.getNodeName());
	}

	/* Display the contents of a NOTATION_NODE */
	private void dumpNotationNode(Notation node, String indent) {
		System.out.println(indent + DOM_NOTATION);
		System.out.print(indent + "  " + node.getNodeName() + "=");
		if (node.getPublicId() != null)
			System.out.println(node.getPublicId());
		else
			System.out.println(node.getSystemId());
	}

	/* Display the contents of a PROCESSING_INSTRUCTION_NODE */
	private void dumpProcessingInstructionNode(ProcessingInstruction node, String indent) {
		System.out.println(indent + DOM_PROCESSING_INSTRUCTION + ": target=" + node.getTarget());
		System.out.println(indent + "  " + node.getData());
	}

	/* Display the contents of a TEXT_NODE */
	private void dumpTextNode(Text node, String indent) {
		System.out.println(indent + DOM_TEXT + "[" + node.getLength() + 
				"] = \"" + node.getData().trim() + "\"");
	}

}
