const { parseArgs } = require('node:util');
const { Firestore } = require('@google-cloud/firestore');
const { latestSaturday, syncDraws } = require('../sync-draws');

async function main() {
  const { values } = parseArgs({
    options: {
      project: { type: 'string' },
      scheduled: { type: 'boolean' },
    },
  });
  if (!values.project) {
    throw new Error('Specify --project <project-id> before syncing Firestore');
  }

  const db = new Firestore({ projectId: values.project });
  try {
    const result = await syncDraws(db, values.scheduled ? latestSaturday() : null);
    console.log(`Firestore (${values.project}): latest round ${result.latestRound}, inserted ${result.inserted}, already present ${result.unchanged}`);
  } finally {
    await db.terminate();
  }
}

main().catch((error) => {
  console.error(error.message);
  process.exitCode = 1;
});
