package com.example.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderEntry is a Querydsl query type for OrderEntry
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOrderEntry extends EntityPathBase<OrderEntry> {

    private static final long serialVersionUID = -564046530L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderEntry orderEntry = new QOrderEntry("orderEntry");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QOrder order;

    public final com.example.shop.entity.QProduct product;

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public QOrderEntry(String variable) {
        this(OrderEntry.class, forVariable(variable), INITS);
    }

    public QOrderEntry(Path<? extends OrderEntry> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderEntry(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderEntry(PathMetadata metadata, PathInits inits) {
        this(OrderEntry.class, metadata, inits);
    }

    public QOrderEntry(Class<? extends OrderEntry> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order")) : null;
        this.product = inits.isInitialized("product") ? new com.example.shop.entity.QProduct(forProperty("product")) : null;
    }

}

