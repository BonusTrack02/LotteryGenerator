const { fetchDrawRange, fetchLatestDraw, sameDraw } = require('./draw-source');
const { readLatestStoredDraw, saveDraws } = require('./draw-store');

const seoulDate = new Intl.DateTimeFormat('sv-SE', {
  timeZone: 'Asia/Seoul',
  year: 'numeric',
  month: '2-digit',
  day: '2-digit',
});

function latestSaturday(now = new Date()) {
  const date = new Date(`${seoulDate.format(now)}T00:00:00Z`);
  const daysSinceSaturday = (date.getUTCDay() + 1) % 7;
  date.setUTCDate(date.getUTCDate() - daysSinceSaturday);
  return date.toISOString().slice(0, 10);
}

async function syncDraws(db, expectedDate = null, request = fetch) {
  const stored = await readLatestStoredDraw(db);
  if (!stored) {
    throw new Error('The draws collection is empty. Seed the full draw history first.');
  }
  if (expectedDate && stored.drawDate >= expectedDate) {
    return { latestRound: stored.round, inserted: 0, unchanged: 0 };
  }

  const latest = await fetchLatestDraw(request);
  if (expectedDate && latest.drawDate < expectedDate) {
    throw new Error(`No new result for ${expectedDate} KST yet. Last published round: ${latest.round}`);
  }
  if (sameDraw(stored, latest)) {
    return { latestRound: stored.round, inserted: 0, unchanged: 1 };
  }
  if (latest.round <= stored.round) {
    throw new Error(`Source round ${latest.round} is older than or differs from stored round ${stored.round}`);
  }

  const first = stored.round + 1;
  const draws = latest.round === first ? [latest] : await fetchDrawRange(first, latest.round, request);
  if (draws[0].drawDate <= stored.drawDate || !sameDraw(draws.at(-1), latest)) {
    throw new Error(`Unexpected official results for rounds ${first} through ${latest.round}`);
  }

  return { latestRound: latest.round, ...await saveDraws(db, draws) };
}

module.exports = { latestSaturday, syncDraws };
