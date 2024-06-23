package com.order.execution.order_execution.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "side")
    private String side;

    @Column(name = "position_side")
    private String positionSide;

    @Column(name = "type")
    private String type;

    @Column(name = "time_in_force")
    private String timeInForce;

    @Column(name = "quantity")
    private String quantity;

    @Column(name = "reduce_only")
    private String reduceOnly;

    @Column(name = "price")
    private String price;

    @Column(name = "stop_price")
    private String stopPrice;

    @Column(name = "close_position")
    private String closePosition;

    @Column(name = "activation_price")
    private String activationPrice;

    @Column(name = "callback_rate")
    private String callbackRate;

    @Column(name = "working_type")
    private String workingType;

    /**
     * time living of order
     */
    @Column(name = "good_till_date")
    private String goodTillDate;

    @Column(name = "recw_window")
    private String recvWindow;

    @Column(name = "exchange")
    private String exchange;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "is_executed")
    private Boolean isExecuted;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

}
