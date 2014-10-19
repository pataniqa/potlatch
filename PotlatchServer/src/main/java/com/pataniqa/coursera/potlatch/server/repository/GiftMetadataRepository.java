package com.pataniqa.coursera.potlatch.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pataniqa.coursera.potlatch.server.model.ServerGiftMetadata;
import com.pataniqa.coursera.potlatch.server.model.ServerGiftMetadataPk;

/**
 * A repository that stores whether gifts have been liked or flagged by users.
 */
@Repository
public interface GiftMetadataRepository extends
        CrudRepository<ServerGiftMetadata, ServerGiftMetadataPk> {
    
}
