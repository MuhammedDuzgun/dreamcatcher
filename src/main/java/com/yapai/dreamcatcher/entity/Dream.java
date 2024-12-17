package com.yapai.dreamcatcher.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "dreams")
public class Dream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String dream;

    @Column(length = 500)
    private String dreamInterpretation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "dream", orphanRemoval = true)
    private List<Comment> comments;

    public Dream() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDream() {
        return dream;
    }

    public void setDream(String dream) {
        this.dream = dream;
    }

    public String getDreamInterpretation() {
        return dreamInterpretation;
    }

    public void setDreamInterpretation(String dreamInterpretation) {
        this.dreamInterpretation = dreamInterpretation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Dream{" +
                "id=" + id +
                ", dream='" + dream + '\'' +
                ", dreamInterpretation='" + dreamInterpretation + '\'' +
                ", user=" + user +
                ", comments=" + comments +
                '}';
    }
}
