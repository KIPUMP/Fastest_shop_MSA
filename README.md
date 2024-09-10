# 🛒 E-commerce 프로젝트 : fastest-shop
## 📌프로젝트 소개 
온라인 쇼핑몰 사이트를 모티브로 만든 쇼핑 프로젝트입니다. 
fastest-shop은 드로우(Draw), 래플(Raffle)과 같은 온라인 오픈런(Open-Run)과 같은 선착순 구매 기능을 제공합니다.  
또한 마이크로서비스 아키텍쳐를 활용해 유연한 확장과 유지보수가 가능합니다.
## 🚧Architecture
![스크린샷 2024-09-09 121743](https://github.com/user-attachments/assets/f71f320a-fb99-4ea8-a828-65f56c4be67f)
## 🧑‍💻사용 기술
![스크린샷 2024-09-10 075444](https://github.com/user-attachments/assets/4d01436c-8ef6-4c05-a955-d2a50b0df7e1)
## 📁ERD
![선착순 구매-ERD 수정 (1)](https://github.com/user-attachments/assets/2148d763-5aef-4e52-8ac9-5c53d4ed2110)
## 🔥주요 기능
- Spring Security, JWT를 사용한 인증 / 인가
  + 개인정보(비밀번호, 이메일) 암호화
- Redis Caching을 사용한 재고 데이터 접근
  + 재고 데이터 조회 속도 향상(289ms → 38ms)
- 실시간 재고 데이터 반영
  + 비관적 락, @Transactional을 통한 데이터베이스 정합성 유지
- API Gateway를 통한 라우팅 및 인가기능 구현
  + 클라이언트의 요청 마이크로서비스 부하 분산 
## 💣트러블 슈팅 | 성능 최적화
- 구매 프로세스 선착순 주문 처리
- MSA 분리시 순환 참조에 대한 오류 해결

