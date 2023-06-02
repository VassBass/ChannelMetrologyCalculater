package service.channel.exchange.heroku.model;

import java.util.List;

public class ResponseWrapper {
    private String date;
    private List<Check> checks;

    public String getDate() {
        return date;
    }

    public List<Check> getChecks() {
        return checks;
    }
}
