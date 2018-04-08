package io.github.skywalkerdarren.simpleaccounting.model;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * @author darren
 * @date 2018/3/31
 */

class StatsCursorWrapper extends CursorWrapper {
    StatsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

}
