package service.channel.search.ui;

public interface LocationPanel {
    /**
     * @return searching zone as written below
     * @see repository.repos.channel.SearchParams#locationZone
     */
    int getSearchingZone();

    /**
     * @return value to searching
     */
    String getSearchingValue();
}
