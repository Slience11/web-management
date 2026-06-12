# Tlias Web Management

Spring Boot + MyBatis based web management backend.

## Environment Variables

Set these variables before running the application:

```bash
DB_URL=jdbc:mysql://localhost:3307/tlias
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
JWT_SIGN_KEY=your_jwt_signing_key
DEFAULT_EMP_PASSWORD=change_this_default_password
ALIYUN_OSS_ENDPOINT=https://oss-cn-beijing.aliyuncs.com
ALIYUN_OSS_BUCKET_NAME=your_bucket_name
ALIYUN_OSS_REGION=cn-beijing
```

Aliyun OSS credentials are loaded by the official SDK from:

```bash
OSS_ACCESS_KEY_ID=your_access_key_id
OSS_ACCESS_KEY_SECRET=your_access_key_secret
```

## Run

```bash
mvn spring-boot:run
```

## Build Without Tests

```bash
mvn -DskipTests package
```
