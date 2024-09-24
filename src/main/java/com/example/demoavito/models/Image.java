package com.example.demoavito.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "originalFileName")
    private String originalFileName;
    @Column(name = "size")
    private Long size;
    @Column(name = "ContentType")
    private String ContentType;
    @Column(name = "isPreviewImage")
    private boolean isPreviewImage;
    @Lob//для хранения двоичных данных
    @Column( length = 65555)
    private byte[] bytes;
    @ManyToOne  (cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    private Product product;
}
