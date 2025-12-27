package de.pbc.utils.test;

import java.util.function.Function;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.LogEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import de.pbc.utils.ProgrammableFilter;
import de.pbc.utils.ProgrammableFilter.TemporaryFilter;

/**
 * Every Appender should manage its own resource (e.g., a file), so the example
 * here should not be followed.
 */
@TestInstance(Lifecycle.PER_CLASS)
class ProgrammableFilterTest {

	@BeforeAll
	void setUpBeforeClass() throws Exception {
		System.setProperty("log4j.configurationFile", "test/de/pbc/utils/test/ProgrammableFilterTest.xml");
	}

	@Test
	void test() {
		ProgrammableFilter.getInstance().getFilters().add(new Function<LogEvent, Filter.Result>() {
			@Override
			public Result apply(LogEvent event) {
				if (event.getLevel().equals(Level.ERROR) && event.getThrown() != null
						&& event.getThrown().getMessage().contains("302")) {
					for (StackTraceElement e : event.getThrown().getStackTrace()) {
						if (e.getClassName() == "de.pbc.utils.test.ProgrammableFilterTest$B") {
							System.out.println(String.format("denied: %s", event.getMessage().getFormattedMessage()));
							return Result.DENY;
						}
					}
					return Result.NEUTRAL;
				}
				return Result.NEUTRAL;
			}
		});
		A.a();
		A.b();
		B.b();
	}

	public static class A {

		static Logger LOG = LogManager.getLogger();

		public static void a() {
			LOG.info("A.a");
			try {
				throw new RuntimeException("302");
			} catch (Exception e) {
				LOG.error("error message 1", e);
			}
		}

		public static void b() {
			LOG.info("A.b");
			try {
				throw new RuntimeException("404");
			} catch (Exception e) {
				LOG.error("error message 2", e);
			}
		}

	}

	public static class B {

		static Logger LOG = LogManager.getLogger();

		public static void b() {
			LOG.info("B.b");
			A.a();
			A.b();
			try (TemporaryFilter tempFilter = ProgrammableFilter
					.getInstance()
					.addTemporaryFilter(new Function<LogEvent, Filter.Result>() {
						@Override
						public Result apply(LogEvent event) {
							if (event.getLevel().equals(Level.ERROR) && event.getThrown() != null
									&& event.getThrown().getMessage().contains("404")) {
								for (StackTraceElement e : event.getThrown().getStackTrace()) {
									if (e.getClassName() == "de.pbc.utils.test.ProgrammableFilterTest$B") {
										System.out
												.println(String
														.format("denied: %s",
																event.getMessage().getFormattedMessage()));
										return Result.DENY;
									}
								}
								return Result.NEUTRAL;
							}
							return Result.NEUTRAL;
						}
					})) {
				A.b();
			}
			A.b();
			A.a();
		}

	}

}