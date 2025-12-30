package com.marublosso.worktrack.worktrack_backend.service.biz.java.features;

import com.marublosso.worktrack.worktrack_backend.dto.SignUpRequestDto;
import com.marublosso.worktrack.worktrack_backend.entity.User;
import com.marublosso.worktrack.worktrack_backend.repository.repo.LoginRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignUpService {

    private final LoginRepository loginRepository;

    public SignUpService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    /******************************************************************
     * 중복 이메일인지 체크한다.
     * 
     * @param signUpRequestDto 컨트롤러에서 받아온 비밀번호
     * @exception 에러 에러 처리 안함  (미구현)
     * 
     */
    public Boolean SignUpAccountCheck(SignUpRequestDto signUpRequestDto) {

        User user = User.builder()
                .username(signUpRequestDto.getUserEmail())
                .build();

        // 존재하는 유저 인지 체크
        int result = loginRepository.SearchAcountOnlyInfo(user); // results = 테이블에 존재하는 유저의 수

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
     * @param signUpRequestDto 컨트롤러에서 받아온 비밀번호
     * @Transactional 트랜잭션 처리 (추후 인서트 처리 증가 대비)
     * @exception 에러 에러 처리 안함  (미구현)
     * 
     */
    @Transactional
    public void SignUp(String email, SignUpRequestDto signUpRequestDto) {

        User user = User.builder()
                .username(email)
                .password_hash(signUpRequestDto.getPassword())
                .name("미설정")
                .dept("현장 미등록") // TODO : 어썸한 다른 디폴트 멘트를 구상해야함
                .role("USER") // 일반유저
                .build();

        // DB에 유저 정보 등록
        loginRepository.InsertNewUser(user);

    }

}
