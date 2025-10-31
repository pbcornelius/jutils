package de.pbc.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * This rate counter limits the number of requests per hour and spaces them
 * evenly. The hour starts at the first call to {@link #request()} or
 * {@link #restart()} At the end of the hour, the rate counter is restarted.
 */
public class HourRateCounter implements RateCounter {

	// VARIABLES ----------------------------------------------------- //

	private LocalDateTime hour;

	private final int maxRequests;

	private final long requestSpacing;

	private int requests;

	private boolean halted;

	// CONSTRUCTOR --------------------------------------------------- //

	public HourRateCounter(int maxRequests) {
		this.maxRequests = maxRequests;
		// equal spacing of requests per hour
		requestSpacing = (60 * 60 * 1000) / maxRequests;
	}

	// PUBLIC -------------------------------------------------------- //

	@Override
	public synchronized void request() {
		if (hour == null || LocalDateTime.now().isAfter(hour.plusHours(1))) {
			restart();
		}

		long waitTime;
		if (halted || requests >= maxRequests) {
			// wait till next hour
			waitTime = LocalDateTime.now().until(hour.plusHours(1), ChronoUnit.MILLIS);
		} else {
			// schedule according to spacing
			waitTime = LocalDateTime
					.now()
					.until(hour.plus(requests * requestSpacing, ChronoUnit.MILLIS), ChronoUnit.MILLIS);
		}

		requests++;

		if (waitTime > 0) {
			try {
				// wait to spread out requests over the hour
				wait(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized void halt(LocalDateTime haltAt) {
		if (haltAt.isBefore(hour.plusHours(1))) {
			halted = true;
		}
	}

	@Override
	public synchronized void restart() {
		hour = LocalDateTime.now();
		requests = 0;
		halted = false;
	}

	@Override
	public synchronized void reset() {
		hour = null;
	}

}