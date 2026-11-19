const assert = require('node:assert/strict');
const { test } = require('node:test');
const { fetchDrawRange, fetchLatestDraw, validateDraw, validateDrawRange } = require('../draw-source');

const first = { round: 1, drawDate: '2002-12-07', numbers: [10, 23, 29, 33, 37, 40], bonusNumber: 16 };
const second = { round: 2, drawDate: '2002-12-14', numbers: [9, 13, 21, 25, 32, 42], bonusNumber: 2 };

function officialRow(draw) {
  return {
    ltEpsd: draw.round,
    ltRflYmd: draw.drawDate.replaceAll('-', ''),
    bnsWnNo: draw.bonusNumber,
    ...Object.fromEntries(draw.numbers.map((number, index) => [`tm${index + 1}WnNo`, number])),
  };
}

test('normalizes the latest official result', async () => {
  const request = async () => Response.json({ data: { list: [officialRow(first)] } });
  assert.deepEqual(await fetchLatestDraw(request), first);
});

test('queries a history range and accepts the source returning it in descending order', async () => {
  const request = async (url) => {
    assert.equal(url.searchParams.get('srchStrLtEpsd'), '1');
    assert.equal(url.searchParams.get('srchEndLtEpsd'), '2');
    return Response.json({ data: { list: [officialRow(second), officialRow(first)] } });
  };
  assert.deepEqual(await fetchDrawRange(1, 2, request), [first, second]);
});

test('rejects a missing or repeated round in a history range', () => {
  assert.throws(() => validateDrawRange([first], 1, 2), /exactly once/);
  assert.throws(() => validateDrawRange([first, first], 1, 2), /exactly once/);
});

test('rejects invalid dates and a bonus number repeated among the winning numbers', () => {
  assert.throws(() => validateDraw({ ...first, drawDate: '2002-02-30' }), /Invalid draw date/);
  assert.throws(() => validateDraw({ ...first, bonusNumber: 23 }), /Invalid winning or bonus/);
});
