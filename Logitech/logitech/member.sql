CREATE TABLE member (
    member_id BIGSERIAL PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	member_login_id VARCHAR(50) NOT NULL,
	password VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL UNIQUE,
	phone VARCHAR(50),
	address TEXT,
	create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	role VARCHAR(255)
);

CREATE UNIQUE INDEX idx_member_email ON member(email);


--더미 회원 데이터
INSERT INTO member (name, member_login_id, password, email, phone, address, role)
VALUES ('관리자','admin1','$2a$10$KcsWvMvriP99EcD84.IbeOUcW.Vt70hEQyq50PW0EFLFlFzomhuxq','admin@myshop.com','010-0000-1111', '인천', 'ADMIN')

INSERT INTO member (name, member_login_id, password, email, phone, address, role)
VALUES ('박재웅','pjw416','$2a$10$IcDxUaPzYG5A109XIv1q3exT7UxRSZai4eF4RjLCAXejY19/IoZli, ','pjw@myshop.com','010-1234-4321', '인천남동','USER')


--회원 정보 조회
SELECT * FROM member;		--findAll()

--특정 회원 정보 조회
SELECT * FROM member WHERE email LIKE 'manage%';		--findByEmailContainsIgnoreCase(manage)
SELECT * FROM member WHERE address LIKE '&인천%'		--