package repository.repos.channel;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SearchParams {
    public static SearchParams SEARCH_BUFFER = null;

    public static final int LOCATION_ZONE_IGNORE = 0;
    public static final int LOCATION_ZONE_ALL = 1;
    public static final int LOCATION_ZONE_DEPARTMENT = 2;
    public static final int LOCATION_ZONE_AREA = 3;
    public static final int LOCATION_ZONE_PROCESS = 4;
    public static final int LOCATION_ZONE_INSTALLATION = 5;

    public int month;
    public int year;
    public int locationZone = LOCATION_ZONE_ALL;
    public String locationValue = EMPTY;
}
