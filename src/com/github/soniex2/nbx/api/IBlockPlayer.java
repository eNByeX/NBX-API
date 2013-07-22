package com.github.soniex2.nbx.api;

public interface IBlockPlayer {

	/**
	 * Plays a NBSBlock.
	 * @param block the NBSBlock
	 * @return the thread playing the note
	 * @throws Throwable 
	 */
	public void play(NBSBlock block);

	/**
	 * Plays all notes in a NBSTick.
	 * @param tick the NBSTick to play the notes from
	 */
	public void play(NBSTick tick);

	/**
	 * Closes this IBlockPlayer.
	 */
	public void close();

}
