package de.pbc.utils;

import java.util.Collection;
import java.util.Objects;

public class AutoCloseableCollection<E extends AutoCloseable, C extends Collection<E>> implements AutoCloseable {

	// VARIABLES ----------------------------------------------------- //

	private final C c;

	// CONSTRUCTOR --------------------------------------------------- //

	public AutoCloseableCollection(C c) {
		this.c = c;
	}

	// PUBLIC -------------------------------------------------------- //

	public C getCollection() {
		return c;
	}

	@Override
	public void close() throws Exception {
		Exception exFirst = null;

		for (E e : c) {
			try {
				e.close();
			} catch (Exception ex) {
				if (Objects.isNull(exFirst)) {
					exFirst = ex;
				} else {
					exFirst.addSuppressed(ex);
				}
			}
		}

		if (Objects.nonNull(exFirst)) {
			throw exFirst;
		}
	}

}