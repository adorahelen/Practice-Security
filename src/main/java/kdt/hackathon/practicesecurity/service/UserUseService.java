//package kdt.hackathon.practicesecurity.service;
//
//import kdt.hackathon.practicesecurity.entity.User;
//import kdt.hackathon.practicesecurity.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class UserUseService { //
//
//    // 1. UserDetailService : 보안 때문에 만든, 서비스(유저디테일 반환하는)
//    // 2. UserRegisterService : 유저 등록 서비스 (회원가입)
//
//    private final UserRepository userRepository;
//
//    public User findById(String userId) {
//        return userRepository.findByUserId(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found, 유저 없습니다."));
//    }
//}
