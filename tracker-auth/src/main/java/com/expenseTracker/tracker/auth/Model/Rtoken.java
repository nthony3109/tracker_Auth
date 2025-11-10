package com.expenseTracker.tracker.auth.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token_refresher")
public class Rtoken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
       private   Long id;
    @Column(nullable = false, unique = true)
    private  String refreshToken;
    @Column(nullable = false)
    private LocalDateTime expiryDate;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_field_id", referencedColumnName = "id", nullable = false)
    private UserField user;

    public  void setUserField(UserField user) {
        this.user= user;
    }

}
