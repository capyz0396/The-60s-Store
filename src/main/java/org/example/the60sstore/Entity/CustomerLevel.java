package org.example.the60sstore.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_levels")
public class CustomerLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "levelid")
    private int levelId;

    @Column(name = "level_name")
    private String levelName;

    @Column(name = "min_points")
    private int minPoints;

    @Column(name = "max_points")
    private Integer maxPoints;

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public void setMinPoints(int minPoints) {
        this.minPoints = minPoints;
    }

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }
}
