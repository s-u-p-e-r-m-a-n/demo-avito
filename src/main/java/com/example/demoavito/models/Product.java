package com.example.demoavito.models;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     @Column(name = "id")
    private Long id;
  @Column(name = "title")
    private String title;
  @Column(name = "description",columnDefinition = "text")
    private String description;
  @Column(name = "price")
    private int price;
 @Column(name = "city")
    private String city;
 @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
 @JoinColumn()
 private User user;
  @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy="product")
  List<Image> images=new ArrayList<>();
  private Long previewImage;
  private LocalDateTime DateOfCreate;
  @PrePersist
  private void init(){
      DateOfCreate=LocalDateTime.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
      String formattedDateTime = DateOfCreate.format(formatter);
      DateOfCreate = LocalDateTime.parse(formattedDateTime, formatter);


  }
  public void addImageToProduct(Image image){
image.setProduct(this);
images.add(image);

  }

}
