# 🛒 E-commerce 프로젝트 : fastest-shop
## 📌프로젝트 소개 
온라인 쇼핑몰 사이트를 모티브로 만든 쇼핑 프로젝트입니다. 
fastest-shop은 드로우(Draw), 래플(Raffle), 오픈런(Open-Run)과 같은 이커머스 선착순 구매 기능을 제공합니다.  
또한 마이크로서비스 아키텍쳐를 활용해 유연한 확장과 유지보수가 가능합니다.
## 🚧 아키텍쳐
![스크린샷 2024-09-09 121743](https://github.com/user-attachments/assets/f71f320a-fb99-4ea8-a828-65f56c4be67f)
## 🧑‍💻 사용기술
- Language
  + Java
- Framework & Library
  + Spring boot
  + Spring Security
  + Spring Cloud
  + Spring Data Jpa
- Database & Caching
  + MySQL
  + Redis
- Infra
  + Docker
## 📁 ERD
![선착순 구매-ERD 수정 (1)](https://github.com/user-attachments/assets/2148d763-5aef-4e52-8ac9-5c53d4ed2110)
## 🔥 주요 기능
- Spring Security를 통한 회원 인증
- API Gateway를 통한 라우팅 및 회원 인가 기능 구현
- Netfilx Eureka server-client를 통한 MSA 기반 서비스 구현
- Redis Caching을 통한 제품 정보 조회
- MySQL Pessimistic Lock을 통한 선착순 구매 동시성 처리
- @PreUpdate, @PrePersist를 통한 상품 상태 및 주문 상태 관리
## 💣 Trouble shooting | development
- 구매 프로세스 선착순 주문 처리
https://kibeom2000.tistory.com/153
- Redis 상품 정보 조회 성능 향상
  + 상품 아이디 번호(Key)를 통한 상품 정보 캐싱
    * ![레디스_key_value 등록](https://github.com/user-attachments/assets/d5a2bfd6-eef5-4f05-980f-b85024d04cc8)
  + 10000명의 동시 접근 실시
    * ![재고조회_레디스_캐싱비교](https://github.com/user-attachments/assets/40da41b3-71a1-4d9a-9db9-f44a326af4e2)
    *  캐싱 전(DB select query를 통한 직접 접근)과 비교하여 인메모리 DB Redis를 사용한 후 속도가 약 99% 정도 증가하였습니다.  

