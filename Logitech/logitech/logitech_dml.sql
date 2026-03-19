-- Logitech 쇼핑몰 프로젝트 DML (current code 기준)
-- PostgreSQL 15+
-- 비밀번호는 Spring Security BCrypt 해시값 사용

BEGIN;

-- 1. 회원 더미 데이터
INSERT INTO member (name, login_id, password, email, phone, address, role)
VALUES
('관리자', 'admin1', '$2a$10$KcsWvMvriP99EcD84.IbeOUcW.Vt70hEQyq50PW0EFLFlFzomhuxq', 'admin@myshop.com', '010-0000-1111', '인천', 'ADMIN'),
('박재웅', 'pjw416', '$2a$10$IcDxUaPzYG5A109XIv1q3exT7UxRSZai4eF4RjLCAXejY19/IoZli', 'pjw@myshop.com', '010-1234-4321', '인천 남동구', 'USER'),
('테스트회원', 'user100', '$2a$10$IcDxUaPzYG5A109XIv1q3exT7UxRSZai4eF4RjLCAXejY19/IoZli', 'user100@myshop.com', '010-2222-3333', '서울 강서구', 'USER');

-- 2. 상품 더미 데이터
INSERT INTO items (item_name, img_path, price, stock, category)
VALUES
('G102', '/images/items/g102.png', 25000, 50, 'BEST'),
('G304', '/images/items/g304.png', 32000, 35, 'BEST'),
('G502', '/images/items/g502.png', 59000, 20, 'RECOMMEND'),
('Lift Vertical', '/images/items/lift_vertical.png', 45000, 18, 'RECOMMEND'),
('M240', '/images/items/m240.png', 24000, 40, 'NEW'),
('M331', '/images/items/m331.png', 28000, 25, 'NEW'),
('MX Master 3S', '/images/items/mx_master4.png', 129000, 12, 'RECOMMEND'),
('Pebble 2 M350s', '/images/items/m350s.png', 34900, 28, 'NEW');

-- 3. 장바구니 더미 데이터
-- current code 호환을 위해 회원가입 직후 빈 cart row를 넣을 수도 있지만,
-- 실제 조회 시 null item 처리가 번거로워 여기서는 정상 데이터만 넣음
INSERT INTO carts (member_id, item_id, quantity)
VALUES
(2, 1, 2),
(2, 4, 1),
(3, 2, 1);

-- 4. 주문 더미 데이터
INSERT INTO orders (member_id, address, payment, amount, item_ids, status)
VALUES
(2, '인천 남동구 논고개로205번길', 'card', 95000, '1,4,4', 'ORDER'),
(2, '인천 남동구 논고개로205번길', 'bank', 32000, '2', 'COMPLETE'),
(3, '서울 강서구 화곡로', 'card', 59000, '3', 'CANCEL');

-- 5. QnA 더미 데이터
-- 질문 등록
INSERT INTO qna (level, parent_id, title, content, writer, view_count)
VALUES
(1, NULL, 'G304 무선 연결이 끊깁니다', 'USB 리시버 연결 후 간헐적으로 끊김 현상이 있습니다. 해결 방법이 있을까요?', 'pjw416', 12),
(1, NULL, 'Lift Vertical은 사무용으로 괜찮나요?', '손목 부담이 적은지 궁금합니다.', 'user100', 7);

-- 질문의 parent_id 를 자기 id 로 세팅 (현재 서비스 로직과 동일)
UPDATE qna
SET parent_id = id
WHERE level = 1 AND parent_id IS NULL;

-- 답변 등록
INSERT INTO qna (level, parent_id, title, content, writer, view_count)
VALUES
(2, 1, 'RE: G304 무선 연결이 끊깁니다', '배터리 상태와 USB 포트 위치를 먼저 확인해 보시고, 다른 포트에도 연결해 보세요.', 'admin1', 0),
(2, 2, 'RE: Lift Vertical은 사무용으로 괜찮나요?', '장시간 사무 작업용으로 많이 사용하는 편이며 손목 각도 측면에서 만족도가 높습니다.', 'admin1', 0);

-- 6. 리뷰 더미 데이터
INSERT INTO reviews (member_id, order_id, item_id, rating, content)
VALUES
(2, 1, 1, 5, '가성비가 좋고 클릭감이 마음에 듭니다.'),
(2, 1, 4, 4, '버티컬 마우스가 생각보다 금방 적응됩니다.'),
(2, 2, 2, 5, '무선 연결이 안정적이고 휴대성이 좋습니다.');

COMMIT;

-- 확인용 조회문
-- SELECT * FROM member ORDER BY member_id;
-- SELECT * FROM items ORDER BY item_id;
-- SELECT * FROM carts ORDER BY cart_id;
-- SELECT * FROM orders ORDER BY order_id;
-- SELECT * FROM qna ORDER BY id;
-- SELECT * FROM reviews ORDER BY review_id;
