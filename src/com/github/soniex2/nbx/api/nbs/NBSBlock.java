package com.github.soniex2.nbx.api.nbs;

public final class NBSBlock {

	public final int inst;
	public final int note;

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

	public String toString() {
		return inst + ":" + note;
	}

}
