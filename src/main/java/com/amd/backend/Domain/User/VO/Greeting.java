package com.amd.backend.Domain.User.VO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class Greeting {
    @Value("${greeting.message")        // org.springframework.beans.factory.annotation.Value;
    private String message;
}
