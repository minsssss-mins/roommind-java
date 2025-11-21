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
