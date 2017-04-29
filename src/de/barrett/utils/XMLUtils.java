package de.barrett.utils;

import java.util.StringJoiner;

import org.w3c.dom.Node;

public class XMLUtils {
	
	// PUBLIC --------------------------------------------------------- //
	
	public static String getDeepNodeValue(Node node) {
		StringJoiner out = new StringJoiner(" ");
		
		if (node.hasChildNodes()) {
			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				String val = getDeepNodeValue(node.getChildNodes().item(i));
				if (val != null && val.trim().length() != 0)
					out.add(val.trim());
			}
		} else if (node.getNodeValue() != null && node.getNodeValue().trim().length() != 0) {
			out.add(node.getNodeValue().trim());
		}
		
		return out.toString();
	}
	
}