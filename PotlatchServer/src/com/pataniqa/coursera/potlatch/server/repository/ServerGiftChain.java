package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.pataniqa.coursera.potlatch.model.GiftChain;

@EqualsAndHashCode(exclude = { "giftChainName" })
@ToString
@Entity
public class ServerGiftChain {
    public static final String ID = "gift_id";

    @Getter @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = ID) private long id;

    @Getter private String giftChainName;

    public ServerGiftChain() {
        // no args constructor
    }

    public ServerGiftChain(GiftChain giftChain) {
        this.id = giftChain.getId();
        this.giftChainName = giftChain.getName();
    }

    public GiftChain toClient() {
        return new GiftChain(id, giftChainName);
    }

}
