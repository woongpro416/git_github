--테이블 조회
SELECT * FROM reviews;
SELECT * FROM qna;
SELECT * FROM orders;
SELECT * FROM carts;
SELECT * FROM items;
SELECT * FROM member;

-- 기존 테이블 삭제
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS qna CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS carts CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS member CASCADE;

-- 1. 회원 테이블
CREATE TABLE member (
    member_id   BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    login_id    VARCHAR(50) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    phone       VARCHAR(50),
    address     TEXT,
    create_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    role        VARCHAR(20) NOT NULL DEFAULT 'USER',

    CONSTRAINT uk_member_login_id UNIQUE (login_id),
    CONSTRAINT uk_member_email UNIQUE (email),
    CONSTRAINT ck_member_role CHECK (role IN ('USER', 'ADMIN'))
);

COMMENT ON TABLE member IS '회원 정보';
COMMENT ON COLUMN member.login_id IS '로그인 아이디';
COMMENT ON COLUMN member.role IS 'USER / ADMIN';

-- 2. 상품 테이블
CREATE TABLE items (
    item_id      BIGSERIAL PRIMARY KEY,
    item_name    VARCHAR(50) NOT NULL,
    img_path     VARCHAR(150) NOT NULL,
    price        INTEGER NOT NULL,
    create_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    stock        INTEGER NOT NULL DEFAULT 0,
    category     VARCHAR(20),

    CONSTRAINT ck_items_price CHECK (price >= 0),
    CONSTRAINT ck_items_stock CHECK (stock >= 0),
    CONSTRAINT ck_items_category CHECK (category IN ('NEW', 'BEST', 'RECOMMEND'))
);

COMMENT ON TABLE items IS '상품 정보';
COMMENT ON COLUMN items.category IS 'NEW / BEST / RECOMMEND';

-- 3. 장바구니 테이블
CREATE TABLE carts (
    cart_id      BIGSERIAL PRIMARY KEY,
    member_id    BIGINT NOT NULL,
    item_id      BIGINT,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity     INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_carts_member FOREIGN KEY (member_id)
        REFERENCES member(member_id) ON DELETE CASCADE,
    CONSTRAINT fk_carts_item FOREIGN KEY (item_id)
        REFERENCES items(item_id) ON DELETE CASCADE,
    CONSTRAINT ck_carts_quantity CHECK (quantity >= 1)
);

COMMENT ON TABLE carts IS '장바구니 항목. current code 기준으로 item_id NULL 허용';

-- 4. 주문 테이블
CREATE TABLE orders (
    order_id      BIGSERIAL PRIMARY KEY,
    member_id     BIGINT NOT NULL,
    address       TEXT,
    payment       VARCHAR(30) NOT NULL,
    amount        INTEGER NOT NULL,
    item_ids      TEXT,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status        VARCHAR(20) NOT NULL DEFAULT 'ORDER',

    CONSTRAINT fk_orders_member FOREIGN KEY (member_id)
        REFERENCES member(member_id) ON DELETE CASCADE,
    CONSTRAINT ck_orders_amount CHECK (amount >= 0),
    CONSTRAINT ck_orders_status CHECK (status IN ('ORDER', 'SHIPPING', 'COMPLETE', 'CANCEL')),
    CONSTRAINT ck_orders_payment CHECK (payment IN ('card', 'bank'))
);

COMMENT ON TABLE orders IS '주문 헤더. current code에서는 item_ids 를 쉼표 문자열로 저장';
COMMENT ON COLUMN orders.item_ids IS '예: 1,3,5';

-- 5. QnA 테이블
CREATE TABLE qna (
    id           BIGSERIAL PRIMARY KEY,
    level        INTEGER NOT NULL,
    parent_id    BIGINT,
    title        VARCHAR(255) NOT NULL,
    content      TEXT NOT NULL,
    writer       VARCHAR(100) NOT NULL,
    view_count   INTEGER NOT NULL DEFAULT 0,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT ck_qna_level CHECK (level IN (1, 2)),
    CONSTRAINT ck_qna_view_count CHECK (view_count >= 0),
    CONSTRAINT fk_qna_parent FOREIGN KEY (parent_id)
        REFERENCES qna(id) ON DELETE CASCADE
);

COMMENT ON TABLE qna IS '1: 질문, 2: 답변';
COMMENT ON COLUMN qna.writer IS 'current code 기준으로 member FK가 아닌 작성자 문자열';

-- 6. 리뷰 테이블
CREATE TABLE reviews (
    review_id     BIGSERIAL PRIMARY KEY,
    member_id     BIGINT NOT NULL,
    order_id      BIGINT NOT NULL,
    item_id       BIGINT NOT NULL,
    rating        INTEGER NOT NULL,
    content       TEXT NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reviews_member FOREIGN KEY (member_id)
        REFERENCES member(member_id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_order FOREIGN KEY (order_id)
        REFERENCES orders(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_item FOREIGN KEY (item_id)
        REFERENCES items(item_id) ON DELETE CASCADE,
    CONSTRAINT ck_reviews_rating CHECK (rating BETWEEN 1 AND 5),
    CONSTRAINT uk_reviews_order_item UNIQUE (order_id, item_id)
);

COMMENT ON TABLE reviews IS '주문한 상품 리뷰';

-- 인덱스
CREATE INDEX idx_member_name ON member(name);
CREATE INDEX idx_member_role ON member(role);

CREATE INDEX idx_items_name ON items(item_name);
CREATE INDEX idx_items_category ON items(category);
CREATE INDEX idx_items_create_at ON items(create_at DESC);

CREATE INDEX idx_carts_member_id ON carts(member_id);
CREATE INDEX idx_carts_item_id ON carts(item_id);
CREATE INDEX idx_carts_member_item ON carts(member_id, item_id);

CREATE INDEX idx_orders_member_id ON orders(member_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at DESC);
CREATE INDEX idx_orders_member_status ON orders(member_id, status);

CREATE INDEX idx_qna_parent_id ON qna(parent_id);
CREATE INDEX idx_qna_created_at ON qna(created_at DESC);
CREATE INDEX idx_qna_writer ON qna(writer);

CREATE INDEX idx_reviews_item_id ON reviews(item_id);
CREATE INDEX idx_reviews_member_id ON reviews(member_id);
CREATE INDEX idx_reviews_order_id ON reviews(order_id);
CREATE INDEX idx_reviews_created_at ON reviews(created_at DESC);

COMMIT;


