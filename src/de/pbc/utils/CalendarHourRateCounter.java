package de.pbc.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

/*
 * - The request() method will automatically pace the calling thread to
 *   stay within the specified rate limit per calendar hour.
 * - The method halt() halts all future executions for the current hour.
 *   Invocations need to supply the date & time of when the incident
 *   occurred that required the halting to avoid locking future hours.
 */
public class CalendarHourRateCounter implements RateCounter {
	
	// VARIABLES ----------------------------------------------------- //
	
	private LocalDateTime hour;
	
	private final int maxRequests;
	
	private final long requestSpacing;
	
	private int requests;
	
	private boolean halted;
	
	// CONSTRUCTOR --------------------------------------------------- //
	
	public CalendarHourRateCounter(int maxRequests) {
		this.maxRequests = maxRequests;
		// equal spacing of requests per hour - 5 minutes at the end
		requestSpacing = (60 * 60 * 1000 - 5 * 60 * 1000) / maxRequests;
	}
	
	// PUBLIC -------------------------------------------------------- //
	
	@Override
	public synchronized void request() {
		if (hour == null || LocalDateTime.now().isAfter(hour.plusHours(1))) {
			reset();
		}
		
		long waitTime;
		if (halted || requests >= maxRequests) {
			// wait till next hour
			waitTime = LocalDateTime.now().until(hour.plusHours(1), ChronoUnit.MILLIS);
		} else {
			// schedule according to spacing
			waitTime = LocalDateTime.now()
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
		if (haltAt.getHour() == hour.getHour()) {
			halted = true;
		}
	}
	
	// PRIVATE ------------------------------------------------------- //
	
	private synchronized void reset() {
		hour = LocalDateTime.now().with(StartOfHour.INST);
		requests = 0;
		halted = false;
	}
	
	// INNER CLASSES ------------------------------------------------- //
	
	public static class StartOfHour implements TemporalAdjuster {
		
		// VARIABLES ------------------------------------------------- //
		
		public static StartOfHour INST = new StartOfHour();
		
		// PUBLIC ---------------------------------------------------- //
		
		@Override
		public Temporal adjustInto(Temporal temporal) {
			return temporal.with(ChronoField.MINUTE_OF_HOUR, 0)
					.with(ChronoField.SECOND_OF_MINUTE, 0)
					.with(ChronoField.MILLI_OF_SECOND, 0);
		}
		
	}
	
}