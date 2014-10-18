package com.pataniqa.coursera.potlatch.store.local;

import com.pataniqa.coursera.potlatch.model.HasId;

interface LocalCRUD<T extends HasId> extends LocalQuery<T>, LocalSaveDelete<T> {

}
