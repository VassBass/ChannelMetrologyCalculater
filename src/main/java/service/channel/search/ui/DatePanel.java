package service.channel.search.ui;

public interface DatePanel {
    int IGNORE = 0;
    int JANUARY = 1;
    int FEBRUARY = 2;
    int MARCH = 3;
    int APRIL = 4;
    int MAY = 5;
    int JUNE = 6;
    int JULY = 7;
    int AUGUST = 8;
    int SEPTEMBER = 9;
    int OCTOBER = 10;
    int NOVEMBER = 11;
    int DECEMBER = 12;

    /**
     * @return month number
     * @see #IGNORE
     * @see #JANUARY
     * @see #FEBRUARY
     * @see #MARCH
     * @see #APRIL
     * @see #MAY
     * @see #JUNE
     * @see #JULY
     * @see #AUGUST
     * @see #SEPTEMBER
     * @see #OCTOBER
     * @see #NOVEMBER
     * @see #DECEMBER
     */
    int getMonth();

    /**
     * @return date is ignoring = 0 or number of year
     */
    int getYear();
}
