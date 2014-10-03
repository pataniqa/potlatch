package com.pataniqa.coursera.potlatch.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.pataniqa.coursera.potlatch.model.GiftChain;

@NoArgsConstructor
@EqualsAndHashCode(exclude = { "name" })
@ToString
@Entity
public class ServerGiftChain {
    
    public static final String ID = "gift_chainid";
    @Getter @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = ID) private long id;

    @Getter @Setter private String name;

    public ServerGiftChain(GiftChain giftChain) {
        this.id = giftChain.getId();
        this.name = giftChain.getName();
    }

    public ServerGiftChain update(GiftChain giftChain) {
        this.name = giftChain.getName();
        return this;
    }

}
