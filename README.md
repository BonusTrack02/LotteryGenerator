# LotteryGenerator

## You can download on...
[Google Play link](https://play.google.com/store/apps/details?id=com.bonustrack02.lotterygenerator)

## 추첨 결과 저장

Firestore의 `draws/{회차}` 문서에 `round`, `drawDate` (`YYYY-MM-DD`), `numbers` (본번호 6개), `bonusNumber`, `sourceUrl`, `importedAt`을 저장합니다. 초기 적재와 주간 갱신은 동행복권의 JSON 조회를 사용합니다. [동행복권 회차별 당첨번호](https://www.dhlottery.co.kr/lt645/winNumber)

GitHub Actions의 **Sync lotto draws**가 매주 토요일 21:10 KST에 새 회차를 확인합니다. 결과가 아직 저장되지 않았으면 21:40과 23:00 KST에 다시 시도합니다. Actions 탭의 **Run workflow**로 최신 발표 결과를 수동으로 확인할 수도 있습니다. 공개 저장소에 60일간 활동이 없으면 GitHub가 예약 실행을 비활성화할 수 있습니다. [GitHub Actions 예약 실행](https://docs.github.com/en/actions/reference/workflows-and-actions/events-that-trigger-workflows#schedule)

워크플로는 Workload Identity Federation으로 인증합니다. 저장소의 Actions 변수에 `LOTTO_FIRESTORE_PROJECT`, `LOTTO_WIF_PROVIDER`, `LOTTO_SYNC_SERVICE_ACCOUNT`를 지정합니다. 서비스 계정에는 Firestore의 조회, 트랜잭션 시작·종료, 새 문서 생성 권한이 필요합니다. 인증 제공자는 이 저장소의 기본 브랜치에서 실행되는 `sync-lotto-draws.yml`에 연결합니다. [Google Cloud GitHub 인증 설정](https://docs.cloud.google.com/iam/docs/workload-identity-federation-with-deployment-pipelines)

로컬 작업에는 Node.js 22와 대상 Firestore 프로젝트 권한이 있는 Application Default Credentials를 사용합니다. [로컬 ADC 설정](https://docs.cloud.google.com/docs/authentication/provide-credentials-adc)

```sh
npm --prefix functions ci
npm --prefix functions test
npm --prefix functions run seed -- --dry-run
gcloud auth application-default login --project lotterygenerator-55a23
npm --prefix functions run seed -- --project lotterygenerator-55a23
npm --prefix functions run sync -- --project lotterygenerator-55a23
```

초기 적재는 1회부터 최신 회차까지 모두 검증한 뒤, 비어 있는 문서를 회차 순으로 추가합니다. 같은 내용이 이미 저장된 회차는 건너뛰고, 기록과 원본이 다른 회차를 발견하면 해당 전송을 중단합니다. 중간에 중단되면 같은 명령으로 다시 실행할 수 있습니다.

예약 실행이 실패하면 해당 실행 로그를 확인하고 **Run workflow**로 다시 시도합니다. 동행복권 결과를 수동으로 확인한 경우 다음 회차를 아래 형식으로 등록할 수 있습니다.

```json
{
  "round": 1250,
  "drawDate": "2026-11-14",
  "numbers": [1, 5, 18, 23, 27, 28],
  "bonusNumber": 8
}
```

```sh
npm --prefix functions run seed -- --project lotterygenerator-55a23 --draw-file /path/to/draw.json
```
