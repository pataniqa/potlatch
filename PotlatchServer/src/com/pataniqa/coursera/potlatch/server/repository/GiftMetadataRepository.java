package com.pataniqa.coursera.potlatch.server.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GiftMetadataRepository extends PagingAndSortingRepository<ServerGiftMetadata, ServerGiftMetadataPk>{

}

