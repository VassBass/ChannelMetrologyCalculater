package service.channel.exchange.heroku.api;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public interface ResponseWorker {
    @Nullable String getContent(@Nonnegative int id);
}
