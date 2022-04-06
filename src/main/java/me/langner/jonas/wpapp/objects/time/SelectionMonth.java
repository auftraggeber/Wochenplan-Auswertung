package me.langner.jonas.wpapp.objects.time;

import java.time.Month;

/**
 * Beinhaltet alle Monate für das select-Feld und deren Informationen.
 * @author Jonas Langner
 * @version 1.0
 * @since 1.0.1
 */
public enum SelectionMonth {

    NONE("Keinen", -1,null),
    JANUARY("Januar", 1, Month.JANUARY),
    FEBRUARY("Februar",2, Month.FEBRUARY),
    MARCH("März",3, Month.MARCH),
    APRIL("April",4, Month.APRIL),
    MAY("Mai",5, Month.MAY),
    JUNE("Juni",6, Month.JUNE),
    JULY("Juli",7, Month.JULY),
    AUGUST("August",8, Month.AUGUST),
    SEPTEMBER("September",9, Month.SEPTEMBER),
    OCTOBER("Oktober",10, Month.OCTOBER),
    NOVEMBER("November",11, Month.NOVEMBER),
    DECEMBER("Dezember",12, Month.DECEMBER);


    private String german;
    private int index;
    private Month month;

    private SelectionMonth(String german, int index, Month month){
        this.german = german;
        this.index = index;
        this.month = month;
    }

    @Override
    public String toString() {
        return german;
    }

    public int getIndex() {
        return index;
    }

    public Month getMonth() {
        return month;
    }
}
