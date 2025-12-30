 

//페이지
const MAIN_URL = "/";  //메인 페이지


const URL_CALENDAR = `${MAIN_URL}calendar`;// 캘린더 페이지
const URL_ICHIRAN = `${MAIN_URL}ichiran`; // 일람 페이지
const URL_CREATE_ACOUNT = `${MAIN_URL}createAcount`; // 회원가입 이메일 등록 페이지
const URL_CREATE_PASSWORD = `${MAIN_URL}createPassword`; // 회원가입 비밀번호 등록 페이지
const URL_LOGIN = `${MAIN_URL}login`; // 로그인페이지


//API MAPPER
const API_URL = `/api`;   // API 매핑  (추후 변경 대비)

//API 
const API_URL_LOGIN = `${API_URL}/login`;
const API_URL_LOGOUT = `${API_URL}/logout`;
const API_URL_SIGNUP_EMAIL = `${API_URL}/signUpEmail`;
const API_URL_SIGNUP_PASSWORD = `${API_URL}/signUpPassword`
const API_URL_PUT_WORKTIME = `${API_URL}/put/worktime`;