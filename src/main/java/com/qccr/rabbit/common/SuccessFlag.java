package com.qccr.rabbit.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessFlag {
  boolean success;
  Object message;
  String error;
}
