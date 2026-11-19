const { FieldValue } = require('@google-cloud/firestore');
const { resultPage, sameDraw, validateDraw } = require('./draw-source');

const writeBatchSize = 100;

async function readLatestStoredDraw(db) {
  const snapshot = await db.collection('draws').orderBy('round', 'desc').limit(1).get();
  if (snapshot.empty) return null;

  const document = snapshot.docs[0];
  const draw = validateDraw(document.data());
  if (document.id !== String(draw.round)) {
    throw new Error(`Unexpected document ID for stored round ${draw.round}: ${document.id}`);
  }
  return draw;
}

async function saveDraws(db, draws) {
  const collection = db.collection('draws');
  let inserted = 0;
  for (let offset = 0; offset < draws.length; offset += writeBatchSize) {
    const batch = draws.slice(offset, offset + writeBatchSize);
    const references = batch.map((draw) => collection.doc(String(draw.round)));
    inserted += await db.runTransaction(async (transaction) => {
      const existing = await transaction.getAll(...references);
      let created = 0;
      for (const [index, draw] of batch.entries()) {
        const snapshot = existing[index];
        if (snapshot.exists) {
          if (!sameDraw(validateDraw(snapshot.data()), draw)) {
            throw new Error(`Stored data disagrees with the incoming result for round ${draw.round}`);
          }
          continue;
        }
        transaction.create(references[index], {
          ...draw,
          sourceUrl: resultPage,
          importedAt: FieldValue.serverTimestamp(),
        });
        created++;
      }
      return created;
    });
  }
  return { inserted, unchanged: draws.length - inserted };
}

module.exports = { readLatestStoredDraw, saveDraws };
