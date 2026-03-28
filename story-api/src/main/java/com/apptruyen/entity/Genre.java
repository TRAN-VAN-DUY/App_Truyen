package com.apptruyen.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "genres")
@NoArgsConstructor @AllArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    // Explicit getters
    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getSlug() { return slug; }

    // Explicit setters
    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSlug(String slug) { this.slug = slug; }

    // Builder support
    public static GenreBuilder builder() {
        return new GenreBuilder();
    }

    public static class GenreBuilder {
        private Integer id;
        private String name;
        private String slug;

        public GenreBuilder id(Integer id) { this.id = id; return this; }
        public GenreBuilder name(String name) { this.name = name; return this; }
        public GenreBuilder slug(String slug) { this.slug = slug; return this; }

        public Genre build() {
            Genre g = new Genre();
            g.id = this.id;
            g.name = this.name;
            g.slug = this.slug;
            return g;
        }
    }
}
