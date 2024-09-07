# 🛒E-commerce 프로젝트
- 선착순 구매
- 대규모 트래픽 처리
## 📌프로젝트 소개 
온라인 쇼핑몰 사이트를 모티브로 만든 쇼핑 사이트 입니다. 마이크로서비스 아키텍쳐를 활용해 유연한 확장과 유지보수가 가능합니다. 
## 🚧Architecture
![스크린샷 2024-09-07 111423](https://github.com/user-attachments/assets/e47a3da6-fcb7-4a85-a5a5-5b717fa4eb48)
## 🧑‍💻사용 기술
![스크린샷 2024-09-07 111452](https://github.com/user-attachments/assets/5eb8a584-012a-4de7-8c0d-67d03c30855e)
## 📁ERD
![선착순 구매-ERD 수정 (1)](https://github.com/user-attachments/assets/2148d763-5aef-4e52-8ac9-5c53d4ed2110)
## 🔥주요 기능
- Spring Security, JWT를 사용한 인증 / 인가
  + 개인정보(비밀번호, 이메일) 암호화
- Redis 캐싱을 사용한 재고 데이터 접근
- Pessimistic Lock을 사용한 재고 데이터 정합성 유지
- API Gateway를 통한 filter 구현
## 💣트러블 슈팅 | 성능 최적화
- 10,000명의 구매 프로세스 선착순 주문 처리
- MSA 분리시 인증 인가 오류 

