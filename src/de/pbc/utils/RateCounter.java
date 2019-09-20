package de.pbc.utils;

import java.time.LocalDateTime;

public interface RateCounter {
	
	// PUBLIC -------------------------------------------------------- //
	
	public void request();
	
	public void halt(LocalDateTime haltAt);
	
}