## Akuma的问答社区

## 资料
- **Spring文档**：https://spring.io/guides
- **Spring Web**：https://spring.io/guides/gs/serving-web-vontent/
- **Spring**：https://docs.spring.io
- **参照模板**：https://elasticsearch.cn/explore
- **Github deploy key**：http://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys
- **Github OAuth**：https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/
- **Bootstrap**：https://v3.bootcss.com/
- **mvn repository**：https://mvnrepository.com/
- **moment**：http://momentjs.cn/
- **Editor.md**：https://github.com/pandao/editor.md
- **maven settings.xml**：http://maven.apache.org/settings.html

## 工具
- **Git**：https://git-scm.com/download
- **Flyway**：https://flywaydb.org/
- **lombok**：https://projectlombok.org/

### 脚本
```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate

git checkout -b dev
```

## 部署
### 依赖
- git
- jdk
- maven
- mysql
### 步骤
- yum update
- yum install git
- mkdir App
- cd App
- git clone
- yum install maven
- mvn -v
- mvn compile package
- mvn clean compile flyway:migrate
- mvn clean compile flyway:migrate -Pproduction
- mvn package
- java -jar -Dspring.profiles.active=production target/community-0.0.1-SNAPSHOT.jar