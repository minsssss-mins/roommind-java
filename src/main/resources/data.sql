--  카테고리 초기 데이터 삽입
--  major_category: 대분류 - 가구, 디지털
--  middle_category: 중분류 - 소파, 침대 등등
--
--  서버 시작 시 자동으로 기본적인 카테고리들을 생성하기 위함 - smh

-- IGNORE 는 기존 데이터가 있으면 무시
INSERT IGNORE INTO Category (major_category, middle_category)
VALUES
('가구', '소파'),
('가구', '침대'),
('가구', '의자'),
('가구', '식탁'),
('디지털', 'TV'),
('디지털', '냉장고'),
('디지털', '전자레인지');


-- ===========================
-- Product Seed Data
-- ===========================
INSERT IGNORE INTO Product
(category_id, product_name, brand, original_price, sale_price, stock, description)
VALUES
-- 가구 - 소파 (category_id = 1)
(1, '슬림 4인 카우치 소파', '캄포구스', 720000, 659000, 12, '슬림한 디자인의 4인 카우치형 패브릭 소파'),
(1, '툰 가죽 소파 (1~3인용)', '툰', 580000, 529000, 15, '블랙 인테리어에 어울리는 가죽 소파'),
(1, '누베스 아쿠아텍스 패브릭 소파 (2~3인용)', '누베스', 390000, 349000, 18, '아쿠아텍스 소재의 편안한 패브릭 소파'),
(1, '카페 패브릭 2인 접이식 소파베드', '카페', 310000, 279000, 14, '2인용 접이식 패브릭 소파베드'),
(1, '매스 아쿠아텍스 2인 패브릭 소파', '매스', 290000, 259000, 20, '아쿠아텍스 소재의 2인 패브릭 소파'),
(1, '눕 데일리 구스 아쿠아텍스 4인 모듈 소파', '한샘', 890000, 820000, 7, '구스 충전재의 4인용 모듈형 소파'),
(1, '라운지 구스 아쿠아텍스 4인 모듈 소파', '라운지', 650000, 599000, 10, '구스 패딩의 모듈형 4인 소파'),
(1, '앤고 아쿠아텍스 2인 패브릭 소파', '앤고', 280000, 249000, 19, '아쿠아텍스 소재의 2인 패브릭 소파'),
(1, '포레스트 4인 기능성 워셔블 모듈 소파', '포레스트', 690000, 629000, 11, '워셔블 기능의 4인 모듈 패브릭 소파'),
(1, '모니 패브릭 1인 좌식 소파', '모니', 140000, 119000, 25, '각도 조절 가능한 1인 좌식 소파'),


-- 가구 - 침대 (category_id = 2)
(2, '어텀 시즌2 원목 침대 SS/Q', '어텀', 420000, 379000, 13, 'SS/Q 선택 가능한 원목 프레임 침대'),
(2, '오름 평상형 원목 침대 SS/Q/K', '오름', 450000, 409000, 11, '2컬러 평상형 원목 침대'),
(2, '플러피 조야 패브릭 침대 SS/Q', '플러피', 320000, 289000, 17, '무헤드/헤드 선택 가능한 패브릭 침대'),
(2, '비안 아쿠아텍스 패브릭 침대 SS/Q/K', '비안', 390000, 349000, 14, '아쿠아텍스 소재 매트리스 선택형 침대'),
(2, '뷰티레스트 시트러스 라지킹 침대', '뷰티레스트', 890000, 829000, 6, '라지킹 사이즈 프리미엄 침대'),
(2, '밀리 일반형 침대 SS/Q', '밀리', 360000, 329000, 12, '매트리스 포함 일반형 침대'),
(2, '뷰티레스트 자스민 퀸 침대 + 협탁', '뷰티레스트', 720000, 669000, 7, '자스민 원단 퀸 프레임 + 협탁 세트'),
(2, '몬드 호텔 패브릭 침대 SS/Q/K', '몬드', 580000, 529000, 10, '호텔 감성 패브릭 침대 (매트 포함)'),
(2, '클로즈 침대 Q/K + 라텍스탑 매트리스', '클로즈', 640000, 599000, 9, '노뜨 라텍스탑 매트리스 포함 세트'),
(2, '에디트 평상형 침대 SS/Q/K', '에디트', 330000, 299000, 20, '평상형 프레임 매트 포함'),


-- 가구 - 의자 (category_id = 3)
(3, '뮤즈 팔걸이 메쉬 컴퓨터 의자', '뮤즈', 89000, 79000, 18, '팔걸이 적용된 편한 메쉬 사무용 의자'),
(3, '페블 디자인 식탁 의자', '페블', 59000, 52000, 20, '패브릭/가죽 선택 가능한 미드센츄리 디자인'),
(3, 'OLIVER 스툴', 'OLIVER', 39000, 35000, 25, '심플한 디자인의 보조 스툴'),
(3, '아크 디자인 스툴', '아크', 45000, 39900, 22, '3컬러 인테리어 디자인 스툴'),
(3, '레이븐 원목 이지클린 식탁 의자', '레이븐', 129000, 115000, 14, '원목 + 이지클린 쿠션 구조의 식탁 의자'),
(3, '코지 우드 러그 원목 식탁 의자', '코지', 110000, 99000, 15, '우드 + 스틸 조합의 디자인 체어'),
(3, '듀이 플라스틱 식탁 의자', '듀이', 59000, 52000, 19, '카페 감성 플라스틱 디자인 의자'),
(3, '뮤즈 헤드형 메쉬 컴퓨터 의자', '뮤즈', 119000, 99000, 12, '헤드레스트 포함된 편안한 메쉬 의자'),
(3, '포니 패브릭/가죽 디자인 식탁 의자', '포니', 79000, 69000, 21, '미드센츄리 패브릭 가죽 디자인'),
(3, '메탈 인테리어 스툴', '메탈', 45000, 39900, 24, '부클레 디자인의 보조 인테리어 스툴'),


-- 가구 - 식탁 (category_id = 4)
(4, '코넬 이지 1600 데스크 세트', '코넬', 159000, 145000, 18, '1600 사이즈 책상 + 책장 일체형 데스크'),
(4, '오테카 원목 책상 1200', '오테카', 139000, 125000, 20, '누적판매 3만건 원목 책상'),
(4, '유닛 1200 코너 데스크 세트', '유닛', 189000, 169000, 15, '코너형 컴퓨터 책상 + 책장 세트'),
(4, '듀플 리버서블 오피스 데스크', '듀플', 179000, 159000, 14, '리버서블 구조의 화이트 오피스 책상'),
(4, '노트르 화이트 사무용 책상', '노트르', 129000, 115000, 19, '화이트 톤 사무용 컴퓨터 책상'),
(4, '원목 전동 모션데스크', '전동모션', 299000, 279000, 12, '5분 조립 가능한 원목 모션 데스크'),
(4, '제로데스크 에보 컴퓨터 책상', '제로데스크', 159000, 145000, 17, '홈오피스용 에보 컴퓨터 책상'),
(4, '라운드 스퀘어 홈오피스 책상', '라운드스퀘어', 139000, 125000, 16, 'E0 자재 라운드형 데스크'),
(4, '게이밍/1인용/2인용 컴퓨터 책상', '게이밍', 99000, 89000, 23, '1~2인 구성 가능한 게이밍 책상'),
(4, '에어 E0 컴퓨터 데스크 800~1600', '에어', 109000, 98000, 22, '멀티탭 덕트 증정 E0 등급 데스크'),


-- 디지털 - TV (category_id = 5)
(5, '화이트 43인치 4K 스마트TV', '화이트에디션', 520000, 469000, 15, '43인치 4K UHD 화이트 스마트 TV'),
(5, '화이트 50인치 QLED 스마트TV', '화이트에디션', 670000, 599000, 12, '50인치 QLED 화이트 스마트 TV'),
(5, '화이트 43인치 QLED 스마트TV', '화이트에디션', 640000, 575000, 20, '43인치 QLED 화이트 스마트 TV'),
(5, '무빙큐빅스 화이트 32인치 HD TV', '무빙큐빅스', 410000, 369000, 14, '32인치 HD 화이트 무빙큐빅스 TV'),
(5, '삼성 65인치 4K 스마트TV (리퍼)', '삼성', 890000, 759000, 8, '65인치 4K UHD 리퍼 삼성 스마트 TV'),
(5, 'LG 43인치 UHD TV', 'LG', 620000, 549000, 10, '43인치 LG UHD TV'),
(5, '무빙큐빅스 화이트 40인치 FHD TV', '무빙큐빅스', 480000, 429000, 18, '40인치 FHD 무빙큐빅스 스마트 TV'),
(5, '삼성 55인치 4K 스마트TV (리퍼)', '삼성', 760000, 689000, 9, '55인치 4K UHD 리퍼 삼성 TV'),
(5, '대형 이동식 TV 스탠드 75인치 호환', '삼탠바이미', 290000, 255000, 25, '75인치까지 호환되는 이동식 TV 스탠드'),
(5, '화이트 55인치 QLED 스마트TV', '화이트에디션', 720000, 659000, 11, '55인치 QLED 화이트 스마트 TV'),


-- 디지털 - 냉장고 (category_id = 6)
(6, 'LG 모던엣지 344L 오브제 냉장고', 'LG', 890000, 829000, 12, '상냉장 하냉동 구조의 344L 오브제 냉장고'),
(6, '컨버터블 321 세트 냉장고 패키지', 'XYZ', 2380000, 2250000, 5, '3모듈 조합형 컨버터블 세트 냉장고'),
(6, 'LG 디오스 AI 오브제 836L 냉장고', 'LG', 1190000, 1120000, 8, '디오스 AI 기반 오브제 냉장고'),
(6, '오브제 컨버터블 김치냉장고 321L', '오브제', 810000, 759000, 11, '컨버터블 패키지 김치냉장고'),
(6, '레트로 미니 냉장고 121L', '레트로', 189000, 169000, 22, '감성 디자인의 121L 미니 냉장고'),
(6, '샤인 멀티냉각 507L 일반 냉장고', '샤인', 610000, 569000, 10, '멀티냉각 시스템 507L 일반 냉장고'),
(6, '모드비 312L 피트인 콤비냉장고', '모드비', 540000, 499000, 16, '4컬러 파스텔 피트인 콤비냉장고'),
(6, '스탠드형 슈퍼슬림 김치냉장고 80L', 'ARK', 330000, 299000, 17, '슈퍼슬림 80L 김치냉장고'),
(6, '레트로 85L 미니 냉장고', '레트로', 159000, 139000, 23, '2도어 소형 미니 냉장고'),
(6, '155L 스탠드 냉동고', 'CFZ', 290000, 259000, 18, '간접 냉각 시스템 155L 냉동고');


-- ===========================
-- File Seed Data
-- ===========================

-- 소파
INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/sofa', 'sofa01.avif', 0, 0
FROM Product p WHERE p.product_name = '슬림 4인 카우치 소파';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/sofa', 'sofa02.avif', 0, 0
FROM Product p WHERE p.product_name = '툰 가죽 소파 (1~3인용)';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/sofa', 'sofa03.avif', 0, 0
FROM Product p WHERE p.product_name = '누베스 아쿠아텍스 패브릭 소파 (2~3인용)';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/sofa', 'sofa04.avif', 0, 0
FROM Product p WHERE p.product_name = '카페 패브릭 2인 접이식 소파베드';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/sofa', 'sofa05.avif', 0, 0
FROM Product p WHERE p.product_name = '매스 아쿠아텍스 2인 패브릭 소파';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/sofa', 'sofa06.avif', 0, 0
FROM Product p WHERE p.product_name = '눕 데일리 구스 아쿠아텍스 4인 모듈 소파';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/sofa', 'sofa07.avif', 0, 0
FROM Product p WHERE p.product_name = '라운지 구스 아쿠아텍스 4인 모듈 소파';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/sofa', 'sofa08.avif', 0, 0
FROM Product p WHERE p.product_name = '앤고 아쿠아텍스 2인 패브릭 소파';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/sofa', 'sofa09.avif', 0, 0
FROM Product p WHERE p.product_name = '포레스트 4인 기능성 워셔블 모듈 소파';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/sofa', 'sofa10.avif', 0, 0
FROM Product p WHERE p.product_name = '모니 패브릭 1인 좌식 소파';



-- 침대
INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/bed', 'bed01.avif', 0, 0
FROM Product p WHERE p.product_name = '어텀 시즌2 원목 침대 SS/Q';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/bed', 'bed02.avif', 0, 0
FROM Product p WHERE p.product_name = '오름 평상형 원목 침대 SS/Q/K';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/bed', 'bed03.avif', 0, 0
FROM Product p WHERE p.product_name = '플러피 조야 패브릭 침대 SS/Q';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/bed', 'bed04.avif', 0, 0
FROM Product p WHERE p.product_name = '비안 아쿠아텍스 패브릭 침대 SS/Q/K';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/bed', 'bed05.avif', 0, 0
FROM Product p WHERE p.product_name = '뷰티레스트 시트러스 라지킹 침대';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/bed', 'bed06.avif', 0, 0
FROM Product p WHERE p.product_name = '밀리 일반형 침대 SS/Q';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/bed', 'bed07.avif', 0, 0
FROM Product p WHERE p.product_name = '뷰티레스트 자스민 퀸 침대 + 협탁';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/bed', 'bed08.avif', 0, 0
FROM Product p WHERE p.product_name = '몬드 호텔 패브릭 침대 SS/Q/K';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/bed', 'bed09.avif', 0, 0
FROM Product p WHERE p.product_name = '클로즈 침대 Q/K + 라텍스탑 매트리스';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/bed', 'bed10.avif', 0, 0
FROM Product p WHERE p.product_name = '에디트 평상형 침대 SS/Q/K';




-- 의자
INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/chair', 'chair01.avif', 0, 0
FROM Product p WHERE p.product_name = '뮤즈 팔걸이 메쉬 컴퓨터 의자';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/chair', 'chair02.avif', 0, 0
FROM Product p WHERE p.product_name = '페블 디자인 식탁 의자';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/chair', 'chair03.avif', 0, 0
FROM Product p WHERE p.product_name = 'OLIVER 스툴';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/chair', 'chair04.avif', 0, 0
FROM Product p WHERE p.product_name = '아크 디자인 스툴';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/chair', 'chair05.avif', 0, 0
FROM Product p WHERE p.product_name = '레이븐 원목 이지클린 식탁 의자';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/chair', 'chair06.avif', 0, 0
FROM Product p WHERE p.product_name = '코지 우드 러그 원목 식탁 의자';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/chair', 'chair07.avif', 0, 0
FROM Product p WHERE p.product_name = '듀이 플라스틱 식탁 의자';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/chair', 'chair08.avif', 0, 0
FROM Product p WHERE p.product_name = '뮤즈 헤드형 메쉬 컴퓨터 의자';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/chair', 'chair09.avif', 0, 0
FROM Product p WHERE p.product_name = '포니 패브릭/가죽 디자인 식탁 의자';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/chair', 'chair10.avif', 0, 0
FROM Product p WHERE p.product_name = '메탈 인테리어 스툴';



-- 식탁
INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/table', 'table01.avif', 0, 0
FROM Product p WHERE p.product_name = '코넬 이지 1600 데스크 세트';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/table', 'table02.avif', 0, 0
FROM Product p WHERE p.product_name = '오테카 원목 책상 1200';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/table', 'table03.avif', 0, 0
FROM Product p WHERE p.product_name = '유닛 1200 코너 데스크 세트';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/table', 'table04.avif', 0, 0
FROM Product p WHERE p.product_name = '듀플 리버서블 오피스 데스크';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/table', 'table05.avif', 0, 0
FROM Product p WHERE p.product_name = '노트르 화이트 사무용 책상';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/table', 'table06.avif', 0, 0
FROM Product p WHERE p.product_name = '원목 전동 모션데스크';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/table', 'table07.avif', 0, 0
FROM Product p WHERE p.product_name = '제로데스크 에보 컴퓨터 책상';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/table', 'table08.avif', 0, 0
FROM Product p WHERE p.product_name = '라운드 스퀘어 홈오피스 책상';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/table', 'table09.avif', 0, 0
FROM Product p WHERE p.product_name = '게이밍/1인용/2인용 컴퓨터 책상';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/table', 'table10.avif', 0, 0
FROM Product p WHERE p.product_name = '에어 E0 컴퓨터 데스크 800~1600';



-- TV
INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/tv', 'tv01.avif', 0, 0
FROM Product p WHERE p.product_name = '화이트 43인치 4K 스마트TV';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/tv', 'tv02.avif', 0, 0
FROM Product p WHERE p.product_name = '화이트 50인치 QLED 스마트TV';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/tv', 'tv03.avif', 0, 0
FROM Product p WHERE p.product_name = '화이트 43인치 QLED 스마트TV';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/tv', 'tv04.avif', 0, 0
FROM Product p WHERE p.product_name = '무빙큐빅스 화이트 32인치 HD TV';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/tv', 'tv05.avif', 0, 0
FROM Product p WHERE p.product_name = '삼성 65인치 4K 스마트TV (리퍼)';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/tv', 'tv06.avif', 0, 0
FROM Product p WHERE p.product_name = 'LG 43인치 UHD TV';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/tv', 'tv07.avif', 0, 0
FROM Product p WHERE p.product_name = '무빙큐빅스 화이트 40인치 FHD TV';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/tv', 'tv08.avif', 0, 0
FROM Product p WHERE p.product_name = '삼성 55인치 4K 스마트TV (리퍼)';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/tv', 'tv09.avif', 0, 0
FROM Product p WHERE p.product_name = '대형 이동식 TV 스탠드 75인치 호환';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/tv', 'tv10.avif', 0, 0
FROM Product p WHERE p.product_name = '화이트 55인치 QLED 스마트TV';



-- 냉장고
INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/fridge', 'fridge01.avif', 0, 0
FROM Product p WHERE p.product_name = 'LG 모던엣지 344L 오브제 냉장고';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/fridge', 'fridge02.avif', 0, 0
FROM Product p WHERE p.product_name = '컨버터블 321 세트 냉장고 패키지';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/fridge', 'fridge03.avif', 0, 0
FROM Product p WHERE p.product_name = 'LG 디오스 AI 오브제 836L 냉장고';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/fridge', 'fridge04.avif', 0, 0
FROM Product p WHERE p.product_name = '오브제 컨버터블 김치냉장고 321L';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/fridge', 'fridge05.avif', 0, 0
FROM Product p WHERE p.product_name = '레트로 미니 냉장고 121L';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/fridge', 'fridge06.avif', 0, 0
FROM Product p WHERE p.product_name = '샤인 멀티냉각 507L 일반 냉장고';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/fridge', 'fridge07.avif', 0, 0
FROM Product p WHERE p.product_name = '모드비 312L 피트인 콤비냉장고';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/fridge', 'fridge08.avif', 0, 0
FROM Product p WHERE p.product_name = '스탠드형 슈퍼슬림 김치냉장고 80L';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/fridge', 'fridge09.avif', 0, 0
FROM Product p WHERE p.product_name = '레트로 85L 미니 냉장고';

INSERT IGNORE INTO File (uuid, product_id, save_dir, file_name, file_type, file_size)
SELECT UUID(), p.product_id, 'uploads/product/seed/fridge', 'fridge10.avif', 0, 0
FROM Product p WHERE p.product_name = '155L 스탠드 냉동고';
