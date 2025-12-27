package de.pbc.utils.test;

import java.util.function.Function;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import de.pbc.utils.ProgrammableRewrite;
import de.pbc.utils.ProgrammableRewrite.TemporaryRewriter;

@TestInstance(Lifecycle.PER_CLASS)
class ProgrammableRewriteTest {

	@BeforeAll
	void setUpBeforeClass() throws Exception {
		System.setProperty("log4j.configurationFile", "test/de/pbc/utils/test/ProgrammableRewriteTest.xml");
	}

	@Test
	void test() {
		Logger log = LogManager.getLogger();
		log.info("info1");
		ProgrammableRewrite.getInstance().getRewriters().add(new Function<LogEvent, LogEvent>() {
			@Override
			public LogEvent apply(LogEvent t) {
				return new Log4jLogEvent.Builder(t).setLevel(Level.WARN).build();
			}
		});
		log.info("info2");
		try (TemporaryRewriter tr = ProgrammableRewrite.getInstance().addTemporaryRewriter(new Function<>() {
			@Override
			public LogEvent apply(LogEvent t) {
				return new Log4jLogEvent.Builder(t).setLevel(Level.ERROR).build();
			}
		})) {
			log.info("info3");
		}
		log.info("info4");
		ProgrammableRewrite.getInstance().getRewriters().clear();
		log.info("info5");
	}

}