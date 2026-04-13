package com.sweet_hjr.server.domain.influencer.repository;

import com.sweet_hjr.server.domain.influencer.entity.Influencer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfluencerRepository extends JpaRepository<Influencer, Long> {
}