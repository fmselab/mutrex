package regex.utils;

import java.util.ArrayList;

/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * An JoinedRandomIterator is an Iterator that wraps a number of Iterators. it
 * returns the next from a random iterator
 */
public class JoinedRandomIterator<T> implements Iterator<T> {
	// wrapped iterators
	private List<Iterator<T>> iterators;
	Random rnd = new Random();

	public JoinedRandomIterator(List<Iterator<T>> iterators) {
		this.iterators = new ArrayList<>(iterators);
	}

	public JoinedRandomIterator(Iterator<T>... iterators) {
		this(Arrays.asList(iterators));
	}

	@Override
	public boolean hasNext() {
		// exists one that has next
		for (Iterator<T> i : iterators) {
			if (i.hasNext())
				return true;
		}
		return false;
	}

	@Override
	public T next() {
		// chose one random which has next
		while (!iterators.isEmpty()) {
			// choose one
			int take = rnd.nextInt(iterators.size());
			Iterator<T> rnditerator = iterators.get(take);
			if (rnditerator.hasNext())
				return rnditerator.next();
			else
				iterators.remove(take);
		}
		throw new NoSuchElementException();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
