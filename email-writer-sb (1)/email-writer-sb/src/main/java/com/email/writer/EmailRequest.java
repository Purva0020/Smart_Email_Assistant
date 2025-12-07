package com.email.writer;


import com.fasterxml.jackson.databind.JsonSerializer;
import lombok.Data;

import java.util.Collection;

@Data
public class EmailRequest {
   private String emailContent;
   private String tone;


}
