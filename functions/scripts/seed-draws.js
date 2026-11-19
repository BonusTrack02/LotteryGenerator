const { readFile } = require('node:fs/promises');
const { parseArgs } = require('node:util');
const { fetchDrawRange, fetchLatestDraw, sameDraw, validateDraw } = require('../draw-source');

async function main() {
  const { values } = parseArgs({
    options: {
      project: { type: 'string' },
      'dry-run': { type: 'boolean' },
      'draw-file': { type: 'string' },
    },
  });
  if (!values['dry-run'] && !values.project) {
    throw new Error('Specify --project <project-id> before writing to Firestore');
  }

  let draws;
  if (values['draw-file']) {
    draws = [validateDraw(JSON.parse(await readFile(values['draw-file'], 'utf8')))];
  } else {
    const latest = await fetchLatestDraw();
    draws = await fetchDrawRange(1, latest.round);
    if (!sameDraw(draws.at(-1), latest)) {
      throw new Error('The latest result disagrees with the historical results');
    }
  }

  const last = draws.at(-1);
  console.log(`Validated ${draws.length} draws (${draws[0].round}–${last.round}), through ${last.drawDate}`);
  if (values['dry-run']) return;

  const { Firestore } = require('@google-cloud/firestore');
  const { readLatestStoredDraw, saveDraws } = require('../draw-store');
  const db = new Firestore({ projectId: values.project });

  try {
    if (values['draw-file']) {
      const stored = await readLatestStoredDraw(db);
      const isNext = stored && last.round === stored.round + 1 && last.drawDate > stored.drawDate;
      if (!isNext && !(stored && sameDraw(stored, last))) {
        throw new Error('A manual result must match the latest stored result or be the next draw');
      }
    }

    const { inserted, unchanged } = await saveDraws(db, draws);
    console.log(`Firestore (${values.project}): inserted ${inserted}, already present ${unchanged}`);
  } finally {
    await db.terminate();
  }
}

main().catch((error) => {
  console.error(error.message);
  process.exitCode = 1;
});
