package core.gui;

import javax.swing.JLabel;
import javax.swing.JTextField;

import core.Parser;

public class GParserItem {
	private JLabel label;
	private JTextField field;
	private Parser parser;
	private Object value;
	public JLabel getLabel() {
		return label;
	}
	public void setLabel(JLabel label) {
		this.label = label;
	}
	public JTextField getField() {
		return field;
	}
	public void setField(JTextField field) {
		this.field = field;
	}
	public Parser getParser() {
		return parser;
	}
	public void setParser(Parser parser) {
		this.parser = parser;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
