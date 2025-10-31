package de.pbc.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * This rate counter limits the number of requests per minute and spaces them
 * evenly. The minute starts at the first call to {@link #request()} or
 * {@link #restart()} At the end of the minute, the rate counter is restarted.
 */
public class MinuteRateCounter implements RateCounter {

	// VARIABLES ----------------------------------------------------- //

	private LocalDateTime minute;

	private final int maxRequests;

	private final long requestSpacing;

	private int requests;

	private boolean halted;

	// CONSTRUCTOR --------------------------------------------------- //

	public MinuteRateCounter(int maxRequests) {
		this.maxRequests = maxRequests;
		// equal spacing of requests per minute
		requestSpacing = (60 * 1000) / maxRequests;
	}

	// PUBLIC -------------------------------------------------------- //

	@Override
	public synchronized void request() {
		if (minute == null || LocalDateTime.now().isAfter(minute.plusMinutes(1))) {
			restart();
		}

		long waitTime;
		if (halted || requests >= maxRequests) {
			// wait till next minute
			waitTime = LocalDateTime.now().until(minute.plusMinutes(1), ChronoUnit.MILLIS);
		} else {
			// schedule according to spacing
			waitTime = LocalDateTime
					.now()
					.until(minute.plus(requests * requestSpacing, ChronoUnit.MILLIS), ChronoUnit.MILLIS);
		}

		requests++;

		if (waitTime > 0) {
			try {
				// wait to spread out requests over the minute
				wait(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized void halt(LocalDateTime haltAt) {
		if (haltAt.isBefore(minute.plusMinutes(1))) {
			halted = true;
		}
	}

	@Override
	public synchronized void restart() {
		minute = LocalDateTime.now();
		requests = 0;
		halted = false;
	}

	@Override
	public synchronized void reset() {
		minute = null;
	}

}