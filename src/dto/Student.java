package dto;


import lombok.*;

@Data // Getter ,Setter ,toString
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private int id;
    private String name;
    private String studentId;
}
