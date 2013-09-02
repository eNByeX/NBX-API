package com.github.soniex2.nbx.api.nbs;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class NBSTick implements Iterable<NBSBlock> {

	private NBSBlock[] notes;
	private int modCount = 0;

	public NBSTick(int layers) {
		notes = new NBSBlock[layers & 0x7FFF];
	}

	public short getLayers() {
		return (short) notes.length;
	}

	public NBSBlock getNote(int layer) {
		return notes[layer];
	}

	public void setNote(NBSBlock note, int layer) {
		if (layer >= notes.length)
			resize((short) (layer+1));
		notes[layer] = note;
		modCount++;
	}

	public boolean isEmpty() {
		for (NBSBlock x : notes) {
			if (x != null)
				return false;
		}
		return true;
	}

	public void resize(short layers) {
		if (layers < 1) {
			throw new IllegalArgumentException(
					"Tick must have at least one layer");
		}
		NBSBlock[] newNotes = new NBSBlock[layers];
		System.arraycopy(notes, 0, newNotes, 0, layers < notes.length ? layers
				: notes.length);
		notes = newNotes;
		modCount++;
	}

	@Override
	public String toString() {
		String s = "[";
		for (int x = 0; x < notes.length; x++) {
			s += String.valueOf(notes[x]) + ", ";
		}
		s = s.substring(0, s.length() - 2);
		s += "]";
		return s;
	}

	public NBSTick copy() {
		NBSTick t = new NBSTick(notes.length);
		int x = 0;
		for (NBSBlock note : this) {
			t.setNote(note, x);
			x++;
		}
		return t;
	}

	@Override
	public Iterator<NBSBlock> iterator() {
		return new Iterator<NBSBlock>() {

			private int cursor = 0;
			private int expectedModCount = modCount;

			@Override
			public boolean hasNext() {
				return cursor != notes.length;
			}

			@Override
			public NBSBlock next() {
				checkForComodification();
				try {
					int i = cursor;
					NBSBlock next = notes[i];
					cursor = i + 1;
					return next;
				} catch (IndexOutOfBoundsException e) {
					checkForComodification();
					throw new NoSuchElementException();
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			public void checkForComodification() {
				if (expectedModCount != modCount)
					throw new ConcurrentModificationException();

			}

		};
	}
}
