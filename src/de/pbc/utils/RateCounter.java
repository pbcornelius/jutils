package de.pbc.utils;

import java.time.LocalDateTime;

public interface RateCounter {

	// PUBLIC -------------------------------------------------------- //

	/**
	 * Request permission to run; will wait to stay within rate limit.
	 */
	public void request();

	/**
	 * Halts all future requests until restart.
	 * 
	 * @param haltAt implementation dependent
	 */
	public void halt(LocalDateTime haltAt);

	/**
	 * Restart rate counter now.
	 */
	public void restart();

	/**
	 * The next request will issue a restart.
	 */
	public void reset();

}