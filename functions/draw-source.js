const resultPage = 'https://www.dhlottery.co.kr/lt645/winNumber';
const resultEndpoint = 'https://www.dhlottery.co.kr/lt645/selectPstLt645Info.do';
const rangeSize = 200;

function validateDraw(draw) {
  const { round, drawDate, numbers, bonusNumber } = draw;
  if (!Number.isSafeInteger(round) || round < 1) {
    throw new Error(`Invalid round: ${round}`);
  }

  const parsedDate = new Date(`${drawDate}T00:00:00Z`);
  if (typeof drawDate !== 'string' || !Number.isFinite(parsedDate.getTime()) ||
      parsedDate.toISOString().slice(0, 10) !== drawDate) {
    throw new Error(`Invalid draw date for round ${round}: ${drawDate}`);
  }

  if (!Array.isArray(numbers) || numbers.length !== 6) {
    throw new Error(`Expected six winning numbers for round ${round}`);
  }
  const allNumbers = [...numbers, bonusNumber];
  if (allNumbers.some((number) => !Number.isInteger(number) || number < 1 || number > 45) ||
      new Set(allNumbers).size !== 7) {
    throw new Error(`Invalid winning or bonus numbers for round ${round}`);
  }

  return { round, drawDate, numbers: [...numbers].sort((a, b) => a - b), bonusNumber };
}

function validateDrawRange(draws, first, last) {
  const ordered = draws.map(validateDraw).sort((a, b) => a.round - b.round);
  if (ordered.length !== last - first + 1 ||
      ordered.some((draw, index) => draw.round !== first + index)) {
    throw new Error(`Expected every round from ${first} through ${last} exactly once`);
  }
  if (ordered.some((draw, index) => index > 0 && draw.drawDate <= ordered[index - 1].drawDate)) {
    throw new Error(`Draw dates are out of order for rounds ${first} through ${last}`);
  }
  return ordered;
}

async function fetchOfficialDraws(url, request = fetch) {
  const response = await request(url, {
    headers: { Accept: 'application/json' },
    signal: AbortSignal.timeout(15000),
  });
  if (!response.ok) {
    throw new Error(`Donghaeng Lottery returned HTTP ${response.status}`);
  }
  const payload = await response.json();
  if (!Array.isArray(payload?.data?.list)) {
    throw new Error('Donghaeng Lottery response is missing data.list');
  }

  return payload.data.list.map((row) => {
    const compactDate = String(row.ltRflYmd);
    if (!/^\d{8}$/.test(compactDate)) {
      throw new Error(`Unexpected date format for round ${row.ltEpsd}`);
    }
    return validateDraw({
      round: row.ltEpsd,
      drawDate: `${compactDate.slice(0, 4)}-${compactDate.slice(4, 6)}-${compactDate.slice(6)}`,
      numbers: [row.tm1WnNo, row.tm2WnNo, row.tm3WnNo, row.tm4WnNo, row.tm5WnNo, row.tm6WnNo],
      bonusNumber: row.bnsWnNo,
    });
  });
}

async function fetchLatestDraw(request = fetch) {
  const draws = await fetchOfficialDraws(new URL(resultEndpoint), request);
  if (draws.length !== 1) {
    throw new Error(`Expected one latest draw, received ${draws.length}`);
  }
  return draws[0];
}

async function fetchDrawRange(first, last, request = fetch) {
  const draws = [];
  for (let start = first; start <= last; start += rangeSize) {
    const end = Math.min(start + rangeSize - 1, last);
    const url = new URL(resultEndpoint);
    url.searchParams.set('srchStrLtEpsd', String(start));
    url.searchParams.set('srchEndLtEpsd', String(end));
    const page = await fetchOfficialDraws(url, request);
    draws.push(...validateDrawRange(page, start, end));
  }
  return validateDrawRange(draws, first, last);
}

function sameDraw(left, right) {
  return left.round === right.round && left.drawDate === right.drawDate &&
    left.bonusNumber === right.bonusNumber &&
    left.numbers.length === right.numbers.length &&
    left.numbers.every((number, index) => number === right.numbers[index]);
}

module.exports = { fetchDrawRange, fetchLatestDraw, resultPage, sameDraw, validateDraw, validateDrawRange };
