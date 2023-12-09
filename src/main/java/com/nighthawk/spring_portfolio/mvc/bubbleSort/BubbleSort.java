package com.nighthawk.spring_portfolio.mvc.bubbleSort;

import com.vladmihalcea.hibernate.type.json.JsonType;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Convert(attributeName ="bubble", converter = JsonType.class)
public class BubbleSort {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    
    @Column
    private Integer number;

    @Column
    private double time;

    public BubbleSort(Integer number, double time) {
        this.number = number;
        this.time = time;
    }
}
