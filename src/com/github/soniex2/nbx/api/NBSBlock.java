package com.github.soniex2.nbx.api;

public final class NBSBlock {

	private int inst;
	private int note;

	public NBSBlock(int instrument, int note) {
		if (note > 87 || note < 0) {
			throw new IllegalArgumentException("Valid range for note is 0-87");
		} else if (instrument > 13 || instrument < 0) {
			throw new IllegalArgumentException(
					"Valid range for instrument is 0-13");
		}
		inst = instrument & 0x7F;
		this.note = note & 0x7F;
	}

	public int getInstrument() {
		return inst;
	}

	public int getNote() {
		return note;
	}

}
