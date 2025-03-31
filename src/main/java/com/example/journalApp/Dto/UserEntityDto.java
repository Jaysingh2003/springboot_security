package com.example.journalApp.Dto;

import lombok.*;
import java.util.List;





@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntityDto {

    private Long id;
    private String userName;
    private List<String> role;
}
