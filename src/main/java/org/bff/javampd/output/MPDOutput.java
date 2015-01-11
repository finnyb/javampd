package org.bff.javampd.output;

/**
 * Represent a MPD output.
 *
 * @author Bill
 */
public class MPDOutput {
    private int id;
    private String name;
    private boolean enabled;

    /**
     * Constructor for MPDOutput
     *
     * @param id the output's id
     */
    public MPDOutput(int id) {
        this.id = id;
    }

    /**
     * Returns the id of the output
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the output
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the output
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns whether the output is enabled
     *
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether the output is enabled
     *
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }

        MPDOutput output = (MPDOutput) object;
        return this.getId() == output.getId();
    }

    /**
     * Returns the hash code for this object.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }
}
