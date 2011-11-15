package org.bff.javampd.objects;

/**
 * Represents audio information about MPD
 */
public class MPDAudioInfo {
    private int sampleRate;
    private int bits;
    private int channels;

    /**
     * Returns the sample rate
     *
     * @return the sample rate
     */
    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * Sets the sample rate
     *
     * @param sampleRate the sample rate
     */
    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    /**
     * Returns the number of bits
     *
     * @return the number of bits
     */
    public int getBits() {
        return bits;
    }

    /**
     * Sets the number of bits
     *
     * @param bits the number of bits
     */
    public void setBits(int bits) {
        this.bits = bits;
    }

    /**
     * Returns the number of channels
     *
     * @return the number of channels
     */
    public int getChannels() {
        return channels;
    }

    /**
     * Sets the number of channels
     *
     * @param channels the number of channels
     */
    public void setChannels(int channels) {
        this.channels = channels;
    }
}
