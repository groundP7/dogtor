-- MySQL 사용자 인증 방식을 mysql_native_password로 변경
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '1234';
FLUSH PRIVILEGES; 