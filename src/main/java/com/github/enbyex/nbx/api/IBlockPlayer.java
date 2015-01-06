package com.github.enbyex.nbx.api;

import com.github.enbyex.nbx.api.nbs.*;

public interface IBlockPlayer {

    /**
     * Plays a NBSBlock.
     *
     * @param block the NBSBlock
     */
    public void play(NBSBlock block);

    /**
     * Plays a NBSBlock at a given volume.
     *
     * @param block  the NBSBlock
     * @param volume the volume
     */
    public void play(NBSBlock block, float volume);

    /**
     * Plays all notes in a NBSTick.
     *
     * @param tick the NBSTick to play the notes from
     */
    public void play(NBSTick tick);

    /**
     * Plays all notes in a NBSTick.
     *
     * @param tick    the NBSTick to play the notes from
     * @param volumes a volume array
     */
    public void play(NBSTick tick, byte[] volumes);

    /**
     * Plays all notes in a NBSTick.
     *
     * @param tick    the NBSTick to play the notes from
     * @param volumes a volume array
     */
    public void play(NBSTick tick, int[] volumes);

    /**
     * Plays all notes in a NBSTick.
     *
     * @param tick    the NBSTick to play the notes from
     * @param volumes a volume array
     */
    public void play(NBSTick tick, float[] volumes);

    /**
     * Plays a NBSSong.
     *
     * @param song   the NBSSong to play
     * @param header the NBSHeader to use while playing
     * @throws InterruptedException if any thread has interrupted the current thread. The
     *                              interrupted status of the current thread is cleared when this
     *                              exception is thrown.
     */
    public void play(NBSSongData song, NBSHeader header)
            throws InterruptedException;

    public void play(NBSSong song)
            throws InterruptedException;

    /**
     * Tests if this block player supports custom instruments.
     *
     * @return a <code>boolean</code> indicating if this block player supports
     * the <code>setCustomInstrument</code> method.
     */
    public boolean customInstrumentsSupported();

    public void setCustomInstrument(int id, IInstrument i);

    /**
     * Closes this IBlockPlayer.
     */
    public void close();

}
