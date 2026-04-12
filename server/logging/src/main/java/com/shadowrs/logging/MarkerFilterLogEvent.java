package com.shadowrs.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.List;

/**
 * @author Shadowrs tardisfan121@gmail.com
 */
public class MarkerFilterLogEvent extends Filter<ILoggingEvent> {

    protected FilterReply onMatch = FilterReply.NEUTRAL;
    protected FilterReply onMismatch = FilterReply.NEUTRAL;

    final public void setOnMatch(String action) {
        if ("NEUTRAL".equals(action)) {
            onMatch = FilterReply.NEUTRAL;
        } else if ("ACCEPT".equals(action)) {
            onMatch = FilterReply.ACCEPT;
        } else if ("DENY".equals(action)) {
            onMatch = FilterReply.DENY;
        }
    }

    final public void setOnMismatch(String action) {
        if ("NEUTRAL".equals(action)) {
            onMismatch = FilterReply.NEUTRAL;
        } else if ("ACCEPT".equals(action)) {
            onMismatch = FilterReply.ACCEPT;
        } else if ("DENY".equals(action)) {
            onMismatch = FilterReply.DENY;
        }
    }
    Marker markerToMatch;

    @Override
    public void start() {
        if (markerToMatch != null) {
            super.start();
        } else {
            addError("The marker property must be set for [" + getName() + "]");
        }
    }

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }
        //System.out.printf("%s check for %s, match=%s, mismatch=%s%n", Arrays.toString(event.getMarkerList().stream().map(e -> e == null ? "?" : e.getName()).toArray()), markerToMatch, onMatch, onMismatch);
		final List<Marker> markerList = event.getMarkerList();
		if (markerList != null && markerList.contains(markerToMatch)) {
            return onMatch;
        } else {
            return onMismatch;
        }
    }

    /**
     * The marker to match in the event.
     *
     * @param markerStr
     */
    public void setMarker(String markerStr) {
        if (markerStr != null) {
            this.markerToMatch = MarkerFactory.getMarker(markerStr);
        }
    }
}

