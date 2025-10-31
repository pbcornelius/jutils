package de.pbc.utils.test;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import de.pbc.utils.MinuteRateCounter;
import de.pbc.utils.RateCounter;

class TestMinuteRateCounter {

//	@Test
	void evenly() {
		System.out.println(LocalDateTime.now());
		RateCounter rc = new MinuteRateCounter(30);

		for (int i = 0; i < 60; i++) {
			rc.request();
			System.out.println(i + ": " + LocalDateTime.now());
		}
	}

	@Test
	void interrupted() throws InterruptedException {
		System.out.println(LocalDateTime.now());
		RateCounter rc = new MinuteRateCounter(30);

		for (int i = 0; i < 60; i++) {
			rc.request();
			System.out.println(i + ": " + LocalDateTime.now());
			if (i % 10 == 0) {
				synchronized (this) {
					wait(10 * 1000);
				}
			}
		}
	}

}