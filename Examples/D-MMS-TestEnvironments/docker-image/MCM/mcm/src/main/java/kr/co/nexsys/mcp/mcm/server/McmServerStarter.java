package kr.co.nexsys.mcp.mcm.server;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@RequiredArgsConstructor
@Component
public class McmServerStarter implements ApplicationListener<ApplicationReadyEvent> {

    private final McmServer mcmServer;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        mcmServer.start();
    }
}
