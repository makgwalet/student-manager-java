package com.thabang.makgwale.studentmanager.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "student")
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String first_name;

    @Column(nullable = false, unique = true, name = "student_number")
    private String studentNumber;

    @Column(nullable = false)
    private String Last_name;

    @Column(nullable = false)
    private String mobile_number;

    @Email
    @Column(nullable = false)
    private String email_address;

    @DateTimeFormat(pattern="dd/MM/yyyy")
    @Column(nullable = false)
    private Date dateOfBirth;

    private Integer current_score;

    private Double average_score;


    @JsonManagedReference  //allow Jackson to better handle the relation
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Score> scores;

}
