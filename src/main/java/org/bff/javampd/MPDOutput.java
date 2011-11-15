/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bff.javampd;

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
     * Sets the id of the output
     *
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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
            return (true);
        }

        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }

        MPDOutput output = (MPDOutput) object;
        if (this.getId() == output.getId()) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * Returns the hash code for this object.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + getName().length();
        hash = 31 * hash + (null == getName() ? 0 : getName().hashCode());
        return (hash);
    }
}
