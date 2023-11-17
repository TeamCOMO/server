# 👨‍👦COMO(Code-Moim) Server Developers👨‍👦

| 신민철 | 김승진 |
| :---------:|:----------:|
|<img width="300" alt="image" src="https://avatars.githubusercontent.com/u/48898994?s=400&u=7ced4be7b2af430c876d1453fa5f4f028a9902f9&v=4"> | <img width="300"  alt="image" src="https://avatars.githubusercontent.com/u/54787442?v=4"> | 
| [its-sky](https://github.com/its-sky) | [Kimseungin0529](https://github.com/Kimseungin0529) |
| AWS 인프라 구축(EC2, HTTPS, RDS, S3)</br>유저 로그인(JWT), 게시물, 이미지, 지원 API 구현</br>테스트 코드 작성  | 관심, 댓글 API 구현, 테스트코드 작성 |

<hr>

## 🛠️ Development Environment

| |  |
| --- | --- |
| 통합 개발 환경 | IntelliJ |
| Spring Boot 버전 | 3.0.8 |
| 데이터베이스 | AWS RDS(MariaDB), Redis |
| 배포 | AWS EC2(Amazon Linux 2), S3 |
| Build Tool | Gradle |
| ERD Diagram | ERDCloud |
| Java version | Java 17(LTS)  |
| 패키지 구조 | 도메인별 폴더링 |
| API 테스트 | PostMan, cURL |


## 🔧 Infra Architecture


<br>

<hr>

<br>

### 🔗 [API Docs](https://tulip-politician-642.notion.site/API-0326a210f19a40e9b7b668b5fa36a56b?pvs=4)

<br>

<hr>

<br>

## 📂 Project Structure
```
.
├── como
│   └── domain
│       ├── apply
│       │   ├── controller
│       │   ├── repository
│       │   ├── service
│       │   ├── model
│       │   ├── exception
│       │   └── dto
│       ├── comment
│       │   ├── controller
│       │   ├── repository
│       │   ├── service
│       │   ├── model
│       │   ├── exception
│       │   └── dto
│       ├── image
│       │   ├── repository
│       │   ├── service
│       │   ├── model
│       │   └── exception
│       ├── interest
│       │   ├── controller
│       │   ├── repository
│       │   ├── service
│       │   ├── model
│       │   ├── exception
│       │   └── dto
│       ├── post
│       │   ├── controller
│       │   ├── repository
│       │   ├── service
│       │   ├── model
│       │   ├── exception
│       │   └── dto
│       ├── user
│       │   ├── controller
│       │   ├── repository
│       │   ├── service
│       │   ├── model
│       │   ├── exception
│       └── └── dto
│   └── global
│       ├── common
│       │   ├── filter
│       │   ├── model
│       │   ├── exception
│       │   └── dto
│       ├── auth
│       │   ├── filter
│       │   ├── repository
│       │   ├── model
│       │   ├── exception
│       │   └── service
│       └── config
│   └── ComoApplication.java
```
<br>

<hr>

<br>

# 🎋 Branch

<aside>

> main, develop, feat, refactor, hotfix, fix, chore
>

`main`: 배포 branch

`develop`: 개발 시 코드를 병합하는 branch. 해당 브랜치에서 각 기능을 Pull 함. (with Pull Request)

`feat/{functionName}`: feat/기능명으로 브랜치를 생성하여 특정 기능을 구현할 때 사용함.

`refactor/{functionName}`: refactor/기능명으로 브랜치를 생성하여 구현되어 있는 기능을 수정할 때 사용함.

`hotfix`: 특정 기능의 신속한 수정이 필요할 경우 사용함.

`fix/{functionName}`: refactor/기능명으로 브랜치를 생성하여 특정 기능의 버그를 수정하기 위해 사용함.

`chore/{functionName}`: refactor/기능명으로 기능에 많은 영향을 미치지 않는 수정 사항을 변경할 때 사용함.


</aside>

<br>

<hr>

<br>

# 😃 Commit Convention
```
- [feat] : 새로운 기능 추가
- [fix] : 버그 수정
- [style] : 코드에 영향을 주지 않는 변경사항(오타 수정, 변수명 변경)
- [refactor] : 코드 리팩토링
- [docs] : 주석 추가 및 수정, 문서 수정
- [test] : 테스트 추가, 테스트 리팩토링
- [chore] : 빌드 부분 혹은 패키지 매니저 수정
- [rename] : 파일 혹은 폴더명 수정
- [remove] : 파일 혹은 폴더 삭제
```
