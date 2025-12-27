package de.pbc.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;

/**
 * A programmable filter. Add anywhere where {@link Filter}s are used:
 * {@code <ProgrammableFilter/>} and {@code packages="de.pbc.utils"}. Creates a
 * single instance which is used everywhere. Filters are applied in the order
 * returned by {@link #getFilters()} and apply the same rules as {@link Result}.
 * {@code <ProgrammableFilter invert="true"/>} inverts all results.
 */
@Plugin(name = "ProgrammableFilter", category = Node.CATEGORY)
public class ProgrammableFilter extends AbstractFilter {

	// FACTORY ------------------------------------------------------- //

	private static final ProgrammableFilter INSTANCE = new ProgrammableFilter();

	private static final AbstractFilter INVERTED = new AbstractFilter() {
		@Override
		public Result filter(LogEvent event) {
			Result result = INSTANCE.filter(event);
			if (result.equals(Result.DENY)) {
				return Result.NEUTRAL;
			} else {
				return Result.DENY;
			}
		}
	};

	public static ProgrammableFilter getInstance() {
		return INSTANCE;
	}

	@PluginFactory
	public static Filter factory(@PluginAttribute(value = "invert", defaultBoolean = false) boolean invert) {
		if (invert) {
			return INSTANCE;
		} else {
			return INVERTED;
		}
	}

	// VARIABLES ----------------------------------------------------- //

	private List<Function<LogEvent, Result>> filters = new LinkedList<>();

	// PUBLIC -------------------------------------------------------- //

	public List<Function<LogEvent, Result>> getFilters() {
		return filters;
	}

	public void setFilters(List<Function<LogEvent, Result>> filters) {
		this.filters = filters;
	}

	/**
	 * @return an {@link AutoCloseable} (without {@code throws}), which removes the
	 *         provided {@code filter} when closed
	 */
	public TemporaryFilter addTemporaryFilter(Function<LogEvent, Result> filter) {
		filters.add(filter);
		return new TemporaryFilter(filter);
	}

	@Override
	public Result filter(LogEvent event) {
		for (Function<LogEvent, Result> filter : filters) {
			Result result = filter.apply(event);
			if (!result.equals(Result.NEUTRAL)) {
				return result;
			}
		}
		return Result.NEUTRAL;
	}

	// INNER CLASSES ------------------------------------------------- //

	public class TemporaryFilter implements AutoCloseable {

		// VARIABLES ------------------------------------------------- //

		private final Function<LogEvent, Result> filter;

		// CONSTRUCTOR ----------------------------------------------- //

		protected TemporaryFilter(Function<LogEvent, Result> filter) {
			this.filter = filter;
		}

		// PUBLIC ---------------------------------------------------- //

		@Override
		public void close() {
			filters.remove(filter);
		}
	}

}