package com.marublosso.worktrack.worktrack_backend.service.biz.java.features;

import com.marublosso.worktrack.worktrack_backend.dto.SignUpRequestDto;
import com.marublosso.worktrack.worktrack_backend.entity.UserAuth;
import com.marublosso.worktrack.worktrack_backend.entity.UserProfiles;
import com.marublosso.worktrack.worktrack_backend.repository.repo.UserAuthRepository;
import com.marublosso.worktrack.worktrack_backend.repository.repo.UserProfilesRepository;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.util.checktools.IsVaildEmail;
import com.marublosso.worktrack.worktrack_backend.exception.InvalidFormatException;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignUpService {

    private static final String ROLE_USER = "USER";
    private static final String DEFAULT_USER_NAME = "사용자 미등록";
    private static final String DEFAULT_USER_DEPT= "근무지 미등록";

    private final UserAuthRepository userAuthRepository;
    private final IsVaildEmail isVaildEmail;
    private final UserProfilesRepository userProfilesRepository;

     // DI (스프링 빈으로 주입)

    public SignUpService(UserAuthRepository userAuthRepository, IsVaildEmail isVaildEmail, UserProfilesRepository userProfilesRepository) {
        this.userAuthRepository = userAuthRepository;
        this.isVaildEmail = isVaildEmail;
        this.userProfilesRepository = userProfilesRepository;
    }

    /******************************************************************
     * 중복 이메일인지 체크한다.
     * 
     * @param signUpRequestDto 컨트롤러에서 받아온 비밀번호
     * @InvalidFormatException 이메일 형식이 올바르지 않을경우 예외 발생
     * 
     */
    public boolean signUpAccountCheck(SignUpRequestDto signUpRequestDto) {

        String email = signUpRequestDto.getUserEmail();

        // 이메일 형식 체크
        if (!isVaildEmail.isVaildEmail(email)) {
            throw new InvalidFormatException("올바르지 않은 이메일입니다: " + email);
        }

        UserAuth user = UserAuth.builder()
                .email(email)
                .build();

        // 존재하는 유저 인지 체크
        int result = userAuthRepository.searchAcountOnlyInfo(user); // results = 테이블에 존재하는 유저의 수

        if (result == 0) {
            // 0건 : 존재 하지않음
            return true;
        } else
            // 1건 이상 : 이미 존재함
            return false;
    }

    /******************************************************************
     * 이메일과 비밀번호를 받아 USER DB에 등록 처리한다.
     * 
     * @param sessionSignUpRequestDto 세션에 등록된 이메일
     * @param signUpRequestDto        컨트롤러에서 받아온 비밀번호
     * @Transactional 트랜잭션 처리 (추후 인서트 처리 증가 대비)
     * @return 등록된 유저의 PK ID
     * 
     */
    @Transactional
    public Long signUp(String email, SignUpRequestDto signUpRequestDto) {

        UserAuth user = UserAuth.builder()
                .email(email)
                .passwordHash(signUpRequestDto.getPassword())
                .build();

        // 1. DB에 유저 정보 등록
        Long id = userAuthRepository.insertNewUser(user);

        // DB에 유저 프로필 정보 등록
        UserProfiles userProfiles = UserProfiles.builder()
                .id(id)
                .name(DEFAULT_USER_NAME)
                .dept(DEFAULT_USER_DEPT)
                .role(ROLE_USER)
                .build();
        
        // 2. DB에 유저 프로필 초기치 등록
        userProfilesRepository.insertUserProfile(userProfiles);

        // 최종 등록된 유저 PK ID 반환
        return id;
    }

}
