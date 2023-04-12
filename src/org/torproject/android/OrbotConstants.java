/* Copyright (c) 2009, Nathan Freitas, Orbot/The Guardian Project - http://openideals.com/guardian */
/* See LICENSE for licensing information */

package org.torproject.android;

public interface OrbotConstants {

	String TAG = "Orbot";

	String PREFS_KEY = "OrbotPrefs";
	String PREFS_KEY_TORIFIED = "PrefTord";

	int FILE_WRITE_BUFFER_SIZE = 2048;
	
	//path to check Tor against
	String URL_TOR_CHECK = "https://check.torproject.org";
	
    String URL_TOR_BRIDGES = "https://bridges.torproject.org/bridges?transport=";

	String PREF_BRIDGES_UPDATED = "pref_bridges_enabled";
	//public final static String PREF_BRIDGES_OBFUSCATED = "pref_bridges_obfuscated";
    String PREF_OR = "pref_or";
    String PREF_OR_PORT = "pref_or_port";
    String PREF_OR_NICKNAME = "pref_or_nickname";
    String PREF_REACHABLE_ADDRESSES = "pref_reachable_addresses";
    String PREF_REACHABLE_ADDRESSES_PORTS = "pref_reachable_addresses_ports";
	int RESULT_CLOSE_ALL = 0;
	
	String PREF_DISABLE_NETWORK = "pref_disable_network";
	
	String PREF_TOR_SHARED_PREFS = "org.torproject.android_preferences";
	
	int MAX_LOG_LENGTH = 10000;
	
	String PREF_SOCKS = "pref_socks";
	
}
