package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.Cursor;

import com.pataniqa.coursera.potlatch.model.User;

public class LocalUserStore extends BaseCreateUpdateDelete<User> implements LocalUsers {

    public LocalUserStore(LocalDatabase helper) {
        super(new UserCreator(), LocalSchema.User.TABLE_NAME, helper);
    }

    private static class UserCreator extends BaseCreator<User> implements Creator<User> {

        @Override
        public ContentValues getCV(User data) {
            ContentValues rValue = new ContentValues();
            rValue.put(LocalSchema.Cols.ID, data.getId());
            rValue.put(LocalSchema.Cols.USER_NAME, data.getName());
            return rValue;
        }

        @Override
        public User getFromCursor(Cursor cursor) {
            long userID = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.ID));
            String name = cursor.getString(cursor.getColumnIndex(LocalSchema.Cols.USER_NAME));
            return new User(userID, name);
        }
    }
}