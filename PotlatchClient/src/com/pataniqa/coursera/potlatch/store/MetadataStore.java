package com.pataniqa.coursera.potlatch.store;

import com.pataniqa.coursera.potlatch.model.GiftMetadata;

public interface MetadataStore extends Save<GiftMetadata>, Retrieve<GiftMetadata, Long> {

}
