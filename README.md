# K-TMS

## 1. 프로젝트 개요

K-TMS는 기존 TESS TMS의 보안 관제 및 대응 시스템 기능을 클라우드에서 사용할 수 있습니다.


## 2. K-TMS 특장점

<table>
  <tr>
    <td>PCRE/YARA</td>
    <td>PCRE/YARA 완벽 지원</td>
  </tr>
  <tr>
    <td>위협 탐지 RAW 데이터</td>
    <td>Snort/PCRE 기반 탐지 데이터의 RAW 데이터 수집리</td>
  </tr>
  <tr>
    <td>파일 수집</td>
    <td>트래픽 내 파일 전송 자동 탐지/수집</td>
  </tr>
  <tr>
    <td>직관적인 UI</td>
    <td>데이터 시각화 및 다양한 통계 데이터의 직관적 UI 제공</td>
  </tr>
  <tr>
    <td>실시간 분석 시간 절감</td>
    <td>실시간 이벤트 분석을 위한 UI 제공</td>
  </tr>
</table>

## 3. K-TMS 설치

K-TMS는 기본적으로 VM 설치버전과 컨테이너 버전을 지원합니다. 

## 4. 최초 내려 받기
```
cd existing_repo
git init
git clone [git tas cloud주소]
```
## 5. 프로그램 실행 방법
```
cd tascloud
bash tascloud_build.sh
```
