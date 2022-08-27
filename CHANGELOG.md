# Changelog

## [1.0.2] - 2022-08-27

### FIX
- QA
  - 장바구니에 추가된 아이템이 9개 초과인 경우 9+로 표시
  - 주문 메뉴 중복 추가 오류 해결
  - LinearRecyclerView 장바구니 추가 버튼 클릭 리스너 추가
  - ViewPager2 Fragment Lifecycle 관련 SharedFlow 중복 이벤트 처리 오류 해결
  - 주문 완료 후 기기 회전 대응 추가

### REFACTOR
- ImageLoader LRU Cache Algorithm 적용
- Hilt Module Refactoring

## [1.0.1] - 2022-08-26

### HOTFIX
- 배송 완료 시간 20분에서 10초로 변경하면서 UI 갱신 코드에는 기존의 20분으로 처리되고 있던 것을 긴급하게 수정하였습니다.

## [1.0.0] - 2022-08-26

### Release 1.0.0