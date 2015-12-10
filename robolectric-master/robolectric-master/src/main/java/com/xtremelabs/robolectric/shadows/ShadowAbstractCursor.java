package com.xtremelabs.robolectric.shadows;

import android.database.AbstractCursor;
import android.database.CursorWindow;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.internal.RealObject;

import java.util.HashMap;
import java.util.Map;


@Implements(AbstractCursor.class)
public class ShadowAbstractCursor {
    @RealObject
    private AbstractCursor realAbstractCursor;

    protected Map<String, Object> currentRow;
    protected int currentRowNumber = -1;
    protected Map<String, Integer> columnNames = new HashMap<String, Integer>();
    protected String[] columnNameArray;
    protected Map<Integer, Map<String, Object>> rows = new HashMap<Integer, Map<String, Object>>();
    protected int rowCount;

    @Implementation
    public int getCount() {
        return rowCount;
    }
    
    @Implementation
    public boolean moveToFirst() {
        setPosition(0);
        return realAbstractCursor.getCount() > 0;
    }
    
    @Implementation
    public boolean moveToLast() {
    	if( realAbstractCursor.getCount() == 0 ) {
    		return false;
    	}
    	setPosition( realAbstractCursor.getCount() - 1 );
    	return true;
    }

    @Implementation
    public int getPosition() {
        return currentRowNumber;
    }


    @Implementation
    public boolean moveToPosition(int pos) {
        if (pos >= realAbstractCursor.getCount()) {
            return false;
        }

        setPosition(pos);
        return true;
    }

    /**
     * Set currentRowNumber(Int) and currentRow (Map)
     *
     * @param pos = the position to set
     */
    private void setPosition(int pos) {
        currentRowNumber = pos;
        if ((-1 == currentRowNumber) || (rowCount == currentRowNumber)) {
            currentRow = null;
        } else {
            currentRow = rows.get(currentRowNumber);
        }
    }

    @Implementation
    public boolean moveToNext() {
        if (currentRowNumber + 1 >= realAbstractCursor.getCount()) {
            currentRowNumber = realAbstractCursor.getCount();
            return false;
        }
        setPosition(++currentRowNumber);
        return true;
    }

    @Implementation
    public boolean moveToPrevious() {
        if (currentRowNumber < 0 || realAbstractCursor.getCount() == 0) {
            return false;
        }
        setPosition(--currentRowNumber);
        return true;
    }

    @Implementation
    public CursorWindow getWindow() {
        return null;
    }

    @Implementation
    public String[] getColumnNames() {
        return columnNameArray;
    }

    @Implementation
    public int getColumnCount() {
        return getColumnNames().length;
    }

    @Implementation
    public boolean isFirst() {
        return currentRowNumber == 0;
    }

    @Implementation
    public boolean isLast() {
        return currentRowNumber == realAbstractCursor.getCount() - 1;
    }

    @Implementation
    public boolean isBeforeFirst() {
        return currentRowNumber < 0;
    }

    @Implementation
    public boolean isAfterLast() {
        return currentRowNumber >= realAbstractCursor.getCount();
    }
}