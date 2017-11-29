package br.com.configuration;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.session.events.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

@Component
public class SessionEndedListener implements ApplicationListener<SessionDestroyedEvent> {

	private final Logger LOGGER = Logger.getLogger(getClass());

	@Override
	public void onApplicationEvent(SessionDestroyedEvent event) {
		LOGGER.info("Destroyed session: {" + event.getSessionId() + "}");
	}
}