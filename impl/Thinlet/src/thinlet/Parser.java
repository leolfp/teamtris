package thinlet;


import java.lang.reflect.*;
import java.util.*;
import javax.xml.parsers.*; 
import org.xml.sax.*; 
import org.xml.sax.helpers.*; 

public class Parser extends DefaultHandler {
	
	private int level;
	private Widget widget;
	private final Widget root;
	private ArrayList<Widget> path;
	private boolean inConstraint = false;
	private Constraint constraint;
	
	public Parser(Widget widget, String resource) throws Exception {
		this.widget = this.root = widget;
		path = new ArrayList<Widget>();
		parse(resource);
	}
	
	private void startElement(String name) {
		String classname = "thinlet." + Character.toUpperCase(name.charAt(0)) + name.substring(1);
		try {
			Class<?> clazz = Class.forName(classname);
			if(!name.equals("constraint")){
				Widget newwidget = (Widget) clazz.newInstance();
				if (level > 0) { widget.add(newwidget); widget = newwidget; }
				path.add(widget); level++;
			} else {
				inConstraint = true;
				constraint = (Constraint) clazz.newInstance();
			}
		} catch (Throwable exc) {
			throw new IllegalArgumentException(classname, exc);
		}
	}
	
	private void setAttribute(String name, String svalue) {
		Object target = inConstraint ? constraint : widget;
		
		if ("style".equals(name)) try {








			return;
		} catch (Throwable exc) {
			throw new IllegalArgumentException(svalue, exc);
		}
		
		Method setter = findMethod(target, name);
		Class<?> type = setter.getParameterTypes()[0];
		Object value = null;
		if (type == String.class) {
			value = svalue;
		} else if ((type == Integer.TYPE) || ((type == Integer.class))) {
			value = Integer.valueOf(svalue);
		} else if ((type == Boolean.TYPE) || ((type == Boolean.class))) {
			value = "true".equalsIgnoreCase(svalue);
		} else throw new IllegalArgumentException(type.getName());
		try {
			
			setter.invoke(target, value); 
		} catch (Throwable exc) {
			throw new IllegalArgumentException(name, exc);
		}
	}
	
	private void characters(String text) {
		if (!inConstraint && text.trim().length() > 0) widget.add(new Text(text));
	}
	
	private void endElement() {
		if(!inConstraint){
			this.root.addNamedChild(widget);
			int n = path.size();
			if (n == 1) path = null;
			else {
				path.remove(n - 1);
				widget = path.get(n - 2);
			}
			level--;
		} else {
			inConstraint = false;
			constraint = null;
		}
	}
	
	private static final Method findMethod(Object target, String name) {
		name = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
		Method[] methods = target.getClass().getMethods();
		for (int j = 0; j < methods.length; j++) {
			if (name.equals(methods[j].getName()) &&
					(methods[j].getParameterTypes().length == 1)) {
				return methods[j];
			}
		}
		throw new IllegalArgumentException(name);
	}
	



	
	
	private void parse(String resource) {
		try {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			saxParser.parse(getClass().getClassLoader().getResourceAsStream(resource), this);
		} catch (Throwable exc) {
			throw new IllegalArgumentException(resource, exc);
		}
	}
	
	public void startElement(String uri, String localname, String name, Attributes attributes) throws SAXException {
		startElement(name);
		for (int i = 0; i < attributes.getLength(); i++) {
			setAttribute(attributes.getQName(i), attributes.getValue(i));
		}
	}
	
	public void characters(char[] chars, int start, int length) throws SAXException {
		StringBuffer sb = new StringBuffer(length);
		boolean whitespace = false;
		for (int i = 0; i < length; i++) {
			char c = chars[start + i];
			if (Character.isWhitespace(c)) {
				if (!whitespace) { sb.append(' '); whitespace = true; }
			} else {
				sb.append(c); whitespace = false;
			}
		}
		
		characters(new String(sb.toString()));
	}
	
	public void endElement(String uri, String localname, String name) throws SAXException {
		endElement();
	}
	
	
	


























































































































}
