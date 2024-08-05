//package com.smart.demo.security;
//
//import jakarta.persistence.AttributeConverter;
//import jakarta.persistence.Converter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Converter
//public class PasswordConverter implements AttributeConverter<String, String> {
//
//    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    @Override
//    public String convertToDatabaseColumn(String attribute) {
//        return passwordEncoder.encode(attribute); // 비밀번호 암호화
//    }
//
//    @Override
//    public String convertToEntityAttribute(String dbData) {
//        return dbData; // 복호화할 필요 없음
//    }
//}
