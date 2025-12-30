package de.pbc.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewriteAppender;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.ThresholdFilter;

/**
 * A programmable {@link RewritePolicy}. Use as {@code <ProgrammableRewrite/>}
 * and {@code packages="de.pbc.utils"}. Creates a single instance which is used
 * everywhere. Rewriters are applied in the order returned by
 * {@link #getRewriters()}.<br/>
 * <br/>
 * If you modify the {@link Level}, bear in mind that {@link Level} checking is
 * done in the order given by the configuration. {@link Level} checking within
 * the parent {@link RewriteAppender} is done before any {@link RewritePolicy
 * RewritePolicies} are applied. So, if a changed {@link Level} should be
 * reflected in the output, a {@link ThresholdFilter} needs to be added to
 * downstream {@link Appender}s.
 */
@Plugin(name = "ProgrammableRewrite", category = Core.CATEGORY_NAME)
public class ProgrammableRewrite implements RewritePolicy {

	// FACTORY ------------------------------------------------------- //

	private static final ProgrammableRewrite INSTANCE = new ProgrammableRewrite();

	@PluginFactory
	public static ProgrammableRewrite getInstance() {
		return INSTANCE;
	}

	// VARIABLES ----------------------------------------------------- //

	private List<Function<LogEvent, LogEvent>> rewriters = new LinkedList<>();

	// PUBLIC -------------------------------------------------------- //

	public List<Function<LogEvent, LogEvent>> getRewriters() {
		return rewriters;
	}

	public void setRewriters(List<Function<LogEvent, LogEvent>> rewriters) {
		this.rewriters = rewriters;
	}

	/**
	 * @return an {@link AutoCloseable} (without {@code throws}), which removes the
	 *         provided {@code rewriter} when closed
	 */
	public TemporaryRewriter addTemporaryRewriter(Function<LogEvent, LogEvent> rewriter) {
		rewriters.add(rewriter);
		return new TemporaryRewriter(rewriter);
	}

	@Override
	public LogEvent rewrite(LogEvent source) {
		for (Function<LogEvent, LogEvent> rewriter : rewriters) {
			source = rewriter.apply(source);
		}
		return source;
	}

	// INNER CLASSES ------------------------------------------------- //

	public class TemporaryRewriter implements AutoCloseable {

		// VARIABLES ------------------------------------------------- //

		private final Function<LogEvent, LogEvent> rewriter;

		// CONSTRUCTOR ----------------------------------------------- //

		protected TemporaryRewriter(Function<LogEvent, LogEvent> rewriter) {
			this.rewriter = rewriter;
		}

		// PUBLIC ---------------------------------------------------- //

		@Override
		public void close() {
			rewriters.remove(rewriter);
		}
	}

}