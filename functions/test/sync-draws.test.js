const assert = require('node:assert/strict');
const { test } = require('node:test');
const { latestSaturday, syncDraws } = require('../sync-draws');

const stored = { round: 1, drawDate: '2002-12-07', numbers: [10, 23, 29, 33, 37, 40], bonusNumber: 16 };
const storedAsOfficial = {
  ltEpsd: 1, ltRflYmd: '20021207', bnsWnNo: 16,
  tm1WnNo: 10, tm2WnNo: 23, tm3WnNo: 29, tm4WnNo: 33, tm5WnNo: 37, tm6WnNo: 40,
};

function database() {
  const query = {
    orderBy: () => query,
    limit: () => query,
    get: async () => ({ empty: false, docs: [{ id: '1', data: () => stored }] }),
  };
  return { collection: () => query };
}

function officialResponse() {
  return Response.json({ data: { list: [storedAsOfficial] } });
}

test('scheduled date uses the latest Saturday in Seoul, including after a delayed start', () => {
  assert.equal(latestSaturday(new Date('2026-11-20T14:59:59Z')), '2026-11-14');
  assert.equal(latestSaturday(new Date('2026-11-20T15:00:00Z')), '2026-11-21');
  assert.equal(latestSaturday(new Date('2026-11-21T16:00:00Z')), '2026-11-21');
});

test('manual sync checks the official source even when Firestore is up to date', async () => {
  const result = await syncDraws(database(), null, officialResponse);
  assert.deepEqual(result, { latestRound: 1, inserted: 0, unchanged: 1 });
});

test('scheduled sync probes the source only while the expected draw is missing', async () => {
  const unexpectedRequest = () => assert.fail('An already stored result needs no source request');
  assert.equal((await syncDraws(database(), '2002-12-07', unexpectedRequest)).inserted, 0);
  await assert.rejects(syncDraws(database(), '2002-12-14', officialResponse), /No new result/);
});
