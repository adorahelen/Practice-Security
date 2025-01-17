# 개발상황

## 09/11
- 스프링 시큐리티에 제공하는 기본화면(ID:user, pw: 콘솔에 발급된 키)
  
<img width="800" alt="image" src="https://github.com/user-attachments/assets/7468fa39-7be3-449e-bb50-6756675bb708">
<img width="800" alt="image" src="https://github.com/user-attachments/assets/b16da22d-9905-4ae3-aa00-cda4f6e47d51">

- ULID방식으로 기본키 생성(해쉬인지 아닌지 헷갈림)
    * 단방향 암호화 : 평문으로 복호화가 불가능하다. (해쉬) SHA, MD, HAS, WHIRLPOOL, (솔트는 해쉬값에 +알파)
    * 양방향 암호화 : 평문 <-> 암호, 복호화 가능(토큰)
      
<img width="800" alt="image" src="https://github.com/user-attachments/assets/e96ae8e8-078f-4b55-a561-ae5704de38d3">


## 09/12
- 로그인 화면/회원가입 화면

<img width="800" alt="image" src="https://github.com/user-attachments/assets/bafc182c-3269-422f-af17-49f0e35f9abd">
<img width="800" alt="image" src="https://github.com/user-attachments/assets/c226941e-bdd0-497c-8743-a9f415290fcb">

## 09/13
- 로그인 성공 + 로그아웃

<img width="800" alt="image" src="https://github.com/user-attachments/assets/c38c40ef-e144-4651-b97d-d1ac69d176d2">
<img width="800" alt="image" src="https://github.com/user-attachments/assets/da863ce2-ebb6-4a29-9baf-9defce9a1fcd">

- 999에러(원인 추측 1.스태틱 폴더 없는데 **허용명시 2. 로그인succesfuldefault,true없어서 3./erro예외처리 안해서?)
 

## 09/14
- 권한 부여(디폴트 유저, 어드민은 수동으로 변경)
  
<img width="800" alt="image" src="https://github.com/user-attachments/assets/4b5f1cf0-049b-41ae-84e2-b9ffd1f0eb03">

- ERROR 9510 :Exception loading sessions from persistent storage
    * java.io.InvalidClassException
    * local class incompatible: stream classdesc serialVersionUID = -8764145416934979531,
    * local class serialVersionUID = -8462780352409935610 (정상적으로 돌아가지만, 서버 가동 초반 오류 로그 발생)
    * 톰캣이 웹 어플리케이션 세션 상태를 저장하고 불러오는데서 발생 (User entity 수정이 원인 - 권한 추가 Role_USER) 


## 09/15 
- 일반 유저가 관리자 홈페이지에 접속한 결과 : ROLE_USER => /admin
<img width="800" alt="image" src="https://github.com/user-attachments/assets/8a2fbc30-18f7-45f0-b7a8-1ce6620f972f">

- 관리자가, 관리자 홈페이지에 접속한 화면 : ROLE_ADMIN => /admin
<img width="800" alt="image" src="https://github.com/user-attachments/assets/1070f026-3661-4303-b440-94e8a486bd49">


## 09/16
- 세션 설정(스프링 시큐리티, 기본제공 -> 조정)

<img width="800" alt="image" src="https://github.com/user-attachments/assets/95c35960-2fb7-4e28-bbdd-118a53acd0cb">




## 09/17
- 구글 OAuth2 

<img width="800" alt="image" src="https://github.com/user-attachments/assets/b6e733bc-e945-465d-ab6d-00e20b81bb92">
<img width="800" alt="image" src="https://github.com/user-attachments/assets/64808f81-144e-435a-8929-28453809d388">
<img width="800" alt="image" src="https://github.com/user-attachments/assets/1779df7a-868a-4cb8-a3ae-47067f2691df">


## 09/18
- JWT 토큰 : base64-encoded secret key cannot be null or empty.
    * key 값으로 설정한 문자열 길이가 너무 짧으면 발생
    * 미쳐 생각 못했는데, 엔티티 부분에 수정이 많아져서(특히 ULID 적용) -> 토큰 에러 증가함 

<img width="800" alt="image" src="https://github.com/user-attachments/assets/55e6b098-1123-42bf-8cc9-89076aafcda2">


## 09/27
- 순환참조
<img width="800" alt="image" src="https://github.com/user-attachments/assets/68d9b1f7-67f5-457d-acbb-d5001be632f8">






