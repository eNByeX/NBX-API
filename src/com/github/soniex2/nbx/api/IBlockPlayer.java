package com.github.soniex2.nbx.api;

import com.github.soniex2.nbx.api.nbs.NBSBlock;
import com.github.soniex2.nbx.api.nbs.NBSTick;

public interface IBlockPlayer {

	/**
	 * Plays a NBSBlock.
	 * @param block the NBSBlock
	 */
	public void play(NBSBlock block);

	/**
	 * Plays a NBSBlock at a given volume.
	 * @param block the NBSBlock
	 * @param volume the volume
	 */
	public void play(NBSBlock block, float volume);

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
