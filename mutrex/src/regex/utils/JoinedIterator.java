package regex.utils;

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

/**
 * An JoinedIterator is an Iterator that wraps a number of Iterators.
 *
 * This class makes multiple iterators look like one to the caller. When any
 * method from the Iterator interface is called, the JoinedIterator will
 * delegate to a single underlying Iterator. The JoinedIterator will invoke the
 * Iterators in sequence until all Iterators are exhausted.
 *
 */
public class JoinedIterator<T> implements Iterator<T> {

	// wrapped iterators
	private List<Iterator<T>> iterators;

	// index of current iterator in the wrapped iterators array
	private int currentIteratorIndex;

	// the current iterator
	private Iterator<T> currentIterator;

	// the last used iterator
	private Iterator<T> lastUsedIterator;

	public JoinedIterator(List<Iterator<T>> iterators) {
		this.iterators = iterators;
	}

	
	
	public JoinedIterator(Iterator<T> ... iterators) {
		this(Arrays.asList(iterators));
	}

	@Override
	public boolean hasNext() {
		updateCurrentIterator();
		return currentIterator.hasNext();
	}

	@Override
	public T next() {
		updateCurrentIterator();
		return currentIterator.next();
	}

	@Override
	public void remove() {
		updateCurrentIterator();
		lastUsedIterator.remove();
	}

	// call this before any Iterator method to make sure that the current
	// Iterator
	// is not exhausted
	protected void updateCurrentIterator() {

		if (currentIterator == null) {
			if (iterators.size() == 0) {
				currentIterator = EmptyIterator.emptyIterator;
			} else {
				currentIterator = iterators.get(0);
			}
			// set last used iterator here, in case the user calls remove
			// before calling hasNext() or next() (although they shouldn't)
			lastUsedIterator = currentIterator;
		}

		while (!currentIterator.hasNext()
				&& currentIteratorIndex < iterators.size() - 1) {
			currentIteratorIndex++;
			currentIterator = iterators.get(currentIteratorIndex);
		}
	}

}

/*
 * Copyright 2004 BEA Systems, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

class EmptyIterator<T> implements java.util.Iterator<T> {
	public static final EmptyIterator emptyIterator = new EmptyIterator();

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public T next() {
		return null;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}