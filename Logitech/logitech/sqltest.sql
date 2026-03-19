-- ✅ 기존 데이터 전체 초기화 (순서 중요!)
TRUNCATE TABLE reviews RESTART IDENTITY CASCADE;
TRUNCATE TABLE qna RESTART IDENTITY CASCADE;
TRUNCATE TABLE orders RESTART IDENTITY CASCADE;
TRUNCATE TABLE carts RESTART IDENTITY CASCADE;
TRUNCATE TABLE items RESTART IDENTITY CASCADE;
TRUNCATE TABLE member RESTART IDENTITY CASCADE;

-- ✅ 1. 회원 (비밀번호는 BCrypt 암호화된 'test1234!' 값)
INSERT INTO member (name, login_id, password, email, phone, address, create_at, role) VALUES
('관리자', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHi2', 'admin@logitech.com', '010-1234-5678', '서울시 강남구', NOW(), 'ADMIN'),
('이용자1', 'user1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHi2', 'user1@logitech.com', '010-1111-2222', '서울시 마포구', NOW(), 'USER'),
('이용자2', 'user2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHi2', 'user2@logitech.com', '010-3333-4444', '서울시 송파구', NOW(), 'USER'),
('이용자3', 'user3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHi2', 'user3@logitech.com', '010-5555-6666', '부산시 해운대구', NOW(), 'USER');

-- ✅ 2. 장바구니 자동 생성 (회원가입 시 자동 생성되지만 더미용)
INSERT INTO carts (member_id, item_id, quantity, created_at) VALUES
(2, 1, 1, NOW()),
(3, 2, 2, NOW());

-- ✅ 3. 상품
INSERT INTO items (item_name, img_path, price, create_at, stock, category) VALUES
('G102', '/images/items/g102.png', 25000, NOW(), 100, 'BEST'),
('G304', '/images/items/g304.png', 32000, NOW(), 100, 'BEST'),
('G502', '/images/items/g502.png', 15000, NOW(), 100, 'BEST'),
('lift_vertical', '/images/items/lift_vertical.png', 45000, NOW(), 100, 'RECOMMEND'),
('m240', '/images/items/m240.png', 24000, NOW(), 100, 'NEW'),
('m331', '/images/items/m331.png', 28000, NOW(), 100, 'BEST'),
('m650', '/images/items/m650.png', 32000, NOW(), 100, 'NEW'),
('mx_anywhere', '/images/items/mx_anywhere.png', 55000, NOW(), 100, 'RECOMMEND'),
('mx_master4', '/images/items/mx_master4.png', 35000, NOW(), 100, 'NEW'),
('pro_x_superlight', '/images/items/pro_x_superlight.png', 90000, NOW(), 100, 'RECOMMEND'),
('pro_x2', '/images/items/pro_x2.png', 30000, NOW(), 100, 'NEW');

-- ✅ 4. 주문
INSERT INTO orders (member_id, address, payment, amount, item_ids, created_at, status) VALUES
(2, '서울시 마포구 123', 'card', 25000, '1', NOW(), 'ORDER'),
(2, '서울시 마포구 123', 'bank', 57000, '2,3', NOW(), 'ORDER'),
(3, '서울시 송파구 456', 'card', 32000, '2', NOW(), 'CANCEL'),
(3, '서울시 송파구 456', 'card', 45000, '4', NOW(), 'ORDER'),
(4, '부산시 해운대구 789', 'bank', 24000, '5', NOW(), 'ORDER');

-- ✅ 5. 리뷰
INSERT INTO reviews (member_id, item_id, order_id, rating, content, created_at) VALUES
(2, 1, 1, 5, 'G102 정말 좋아요! 클릭감이 훌륭합니다.', NOW()),
(3, 4, 4, 4, 'lift_vertical 손목이 편해졌어요. 적응 기간이 좀 필요하긴 해요.', NOW());

-- ✅ 6. Q&A (level=1: 질문, level=2: 답변, parentId로 연결)
INSERT INTO qna (level, parent_id, title, content, writer, view_count, created_at) VALUES
(1, NULL, '배송은 얼마나 걸리나요?', '주문했는데 배송이 언제 오는지 궁금합니다.', '이용자1', 5, NOW()),
(1, NULL, '반품 방법이 궁금해요', '단순변심으로 반품하고 싶은데 어떻게 하면 되나요?', '이용자2', 3, NOW()),
(1, NULL, 'G102 재고 있나요?', 'G102 마우스 재고 확인 부탁드립니다.', '이용자3', 2, NOW());

-- Q&A parentId 업데이트 (질문 등록 후 자기 id로 설정)
UPDATE qna SET parent_id = id WHERE level = 1;

-- 답변 등록
INSERT INTO qna (level, parent_id, title, content, writer, view_count, created_at) VALUES
(2, 1, '답변', '주문 후 2-3일 이내 배송됩니다. 평일 오후 2시 이전 주문 시 당일 출고됩니다 😊', '관리자', 0, NOW()),
(2, 2, '답변', '수령 후 7일 이내 반품 가능합니다. Q&A 게시판에 문의주시면 안내해드리겠습니다!', '관리자', 0, NOW());