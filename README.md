# 🛒 E-commerce 프로젝트 : fastest-shop
## 📌프로젝트 소개 
온라인 쇼핑몰 사이트를 모티브로 만든 쇼핑 프로젝트입니다. 
fastest-shop은 드로우(Draw), 래플(Raffle), 오픈런(Open-Run)과 같은 이커머스 선착순 구매 기능을 제공합니다.  
또한 마이크로서비스 아키텍쳐를 활용해 유연한 확장과 유지보수가 가능합니다.
## 🚧 아키텍쳐
![스크린샷 2024-09-09 121743](https://github.com/user-attachments/assets/f71f320a-fb99-4ea8-a828-65f56c4be67f)
## 🧑‍💻 사용기술
- Language
<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
</div>
- Framework & Library
<div align=center> 
<svg role="img" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><title>Spring Boot</title><path d="m23.693 10.7058-4.73-8.1844c-.4094-.7106-1.4166-1.2942-2.2402-1.2942H7.2725c-.819 0-1.8308.5836-2.2402 1.2942L.307 10.7058c-.4095.7106-.4095 1.873 0 2.5837l4.7252 8.189c.4094.7107 1.4166 1.2943 2.2402 1.2943h9.455c.819 0 1.826-.5836 2.2402-1.2942l4.7252-8.189c.4095-.7107.4095-1.8732 0-2.5838zM10.9763 5.7547c0-.5365.4377-.9742.9742-.9742s.9742.4377.9742.9742v5.8217c0 .5366-.4377.9742-.9742.9742s-.9742-.4376-.9742-.9742zm.9742 12.4294c-3.6427 0-6.6077-2.965-6.6077-6.6077.0047-2.0896.993-4.0521 2.6685-5.304a.8657.8657 0 0 1 1.2142.1788.8657.8657 0 0 1-.1788 1.2143c-2.1602 1.6048-2.612 4.6592-1.0072 6.8194 1.6049 2.1603 4.6593 2.612 6.8195 1.0072 1.2378-.9177 1.9673-2.372 1.9673-3.9157a4.8972 4.8972 0 0 0-1.9861-3.925c-.386-.2824-.466-.8284-.1836-1.2143.2824-.386.8283-.466 1.2143-.1835 1.6895 1.2471 2.6826 3.2238 2.6873 5.3228 0 3.6474-2.965 6.6077-6.6077 6.6077z"/></svg>
</div>
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
    + Pessimistic Lock, Redis의 RLock을 통한 동시성 처리를 위한 재고 데이터 반영
    + 결제 실패 및 동시 요청 시, 데이터베이스 정합성 유지
    + 10000명의 가상 사용자를 대상으로 재고가 10개인 제품에 한해서 부하테스트 실시
      * ![스크린샷 2024-09-03 141154](https://github.com/user-attachments/assets/650a55ee-f867-442a-8a13-c9adc4d37aaa)
- Redis 상품 정보 조회 성능 향상
  + 상품 아이디 번호(Key)를 통한 상품 정보 캐싱
    * ![레디스_key_value 등록](https://github.com/user-attachments/assets/d5a2bfd6-eef5-4f05-980f-b85024d04cc8)
  + 10000명의 동시 접근 실시
    * ![재고조회_레디스_캐싱비교](https://github.com/user-attachments/assets/40da41b3-71a1-4d9a-9db9-f44a326af4e2)
    *  캐싱 전(DB select query를 통한 직접 접근)과 비교하여 인메모리 DB Redis를 사용한 후 속도가 약 99% 정도 증가하였습니다.  

