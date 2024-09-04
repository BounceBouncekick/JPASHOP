# JPASHOP
# 1.프로젝트 환경설정

### 사용기술

* Spring Boot
* Gradle
* JPA
* MySQL
* Spring Security
* JAVA
* token(JWT,REFRESH)
* Junit5
* Redis


# 2.기능명세서

## 회원
* 회원가입
* 로그인
* 로그아웃

## 상품
* 상품등록
* 상품수정
* 상품삭제
* 상품조회

## 주문
* 주문
* 주문취소
* 매출액

## 장바구니
* 물품추가
* 물품조회
* 물품취소

## 배송
* 주소 등록

# 성능 개선
* Redis캐칭을 통해 주문시간단축 98,971ms -> 11,680ms [88%의 감소] ([자세히보기](https://boudle.tistory.com/30)) 
* 비동기 처리도입 Redis캐칭 상품 100개 100000만개 주문 54890ms ->
  Executorservice + Redis 캐칭 상품 100개 100000만개 주문시간단축 22779ms [58% 감소] ([자세히보기](https://boudle.tistory.com/33))
* JPA N+1 @Fetch(FetchMode.SUBSELECT)를 이용하여 문제 해결([자세히보기](https://boudle.tistory.com/35))

# 코드 개선
* 전략패턴으로 비즈니스 로직 분리 ([자세히보기](https://boudle.tistory.com/36))

# 배포
* 배포 ([자세히보기](https://boudle.tistory.com/37))


# 3.ERD

![JPASHOP](https://github.com/user-attachments/assets/6907a952-8944-49a7-9a53-969d70d5565e)



