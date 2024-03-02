package org.example.the60sstore.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "access_history")
public class AccessHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accessid")
    private Integer accessId;

    @ManyToOne
    @JoinColumn(name = "customerid")
    private Customer customer;

    @Column(name = "access_date")
    private LocalDateTime accessDate;

    public Integer getAccessId() {
        return accessId;
    }

    public void setAccessId(Integer accessId) {
        this.accessId = accessId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(LocalDateTime accessDate) {
        this.accessDate = accessDate;
    }
}
