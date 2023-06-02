package service.channel.server;

import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ServiceExecutor;
import service.channel.exchange.heroku.api.DefaultResponseWorker;
import service.channel.exchange.heroku.api.ResponseWorker;

import javax.annotation.Nonnull;

public class TestExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(TestExecutor.class);

    private final ApplicationScreen applicationScreen;

    public TestExecutor(@Nonnull ApplicationScreen applicationScreen) {
        this.applicationScreen = applicationScreen;
    }

    @Override
    public void execute() {
        ResponseWorker responseWorker = new DefaultResponseWorker();
        TestServerDialog dialog = new TestServerDialog(applicationScreen, responseWorker);
        dialog.showing();

        logger.info("Service is running!");
    }
}
