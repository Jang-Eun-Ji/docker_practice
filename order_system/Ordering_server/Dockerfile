FROM openjdk:11 as stage1

WORKDIR /app

# gradle빌드시에 필요한 파일들을 workdir로 copy
# 왼쪽은 로컬에 오른쪽에는 나의 워크 스테이지로 카피하는거
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# /app/build/libs/*.jar파일아래 명령어를 통해 생성
RUN ./gradlew bootJar

FROM openjdk:11

WORKDIR /app

# libs밑에 파일을 복사해서 app.jar파일에 넣기위한 자동화
COPY --from=stage1 /app/build/libs/*.jar app.jar

# CMD또는 ENTRYPOINT를 통해 컨테이너 실행
ENTRYPOINT [ "java", "-jar", "app.jar" ]