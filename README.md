# 개발상황

- 스프링 시큐리티에 제공하는 기본화면(ID:user, pw: 콘솔에 발급된 키)
  
<img width="1010" alt="image" src="https://github.com/user-attachments/assets/7468fa39-7be3-449e-bb50-6756675bb708">
<img width="1010" alt="image" src="https://github.com/user-attachments/assets/b16da22d-9905-4ae3-aa00-cda4f6e47d51">

- ULID방식으로 기본키 생성(해쉬인지 아닌지 헷갈림)
    * 단방향 암호화 : 평문으로 복호화가 불가능하다. (해쉬) SHA, MD, HAS, WHIRLPOOL, (솔트는 해쉬값에 +알파)
    * 양방향 암호화 : 평문 <-> 암호, 복호화 가능(토큰)
      
<img width="1280" alt="image" src="https://github.com/user-attachments/assets/e96ae8e8-078f-4b55-a561-ae5704de38d3">

