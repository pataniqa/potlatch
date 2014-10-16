package com.pataniqa.coursera.potlatch.store.local;

import com.pataniqa.coursera.potlatch.model.User;

public interface LocalUsers extends LocalQuery<User>, LocalSaveDelete<User> {

}
