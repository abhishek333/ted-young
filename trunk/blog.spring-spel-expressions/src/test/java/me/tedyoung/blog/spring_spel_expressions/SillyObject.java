package me.tedyoung.blog.spring_spel_expressions;

/**
 * A silly object used to demonstrate SpEL expressions.
 */
public class SillyObject {
	/**
	 * The property.
	 */
	private String property = "Test Property";
	
	/**
	 * Creates a new SillyObject.
	 */
	public SillyObject() {
	}
	
	/**
	 * A test method.
	 * @return 10.
	 */
	public int method() {
		return 10;
	}

	/**
	 * Gets the property.
	 * @return the property.
	 * @see {@link #property}.
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Sets the property.
	 * @param property the new property to set.
	 * @see {@link #property}.
	 */
	public void setProperty(String property) {
		this.property = property;
	}
	
	
}
